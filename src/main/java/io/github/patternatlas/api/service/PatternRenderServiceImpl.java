package io.github.patternatlas.api.service;

import java.net.URLEncoder;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.patternatlas.api.entities.Image;
import io.github.patternatlas.api.entities.Pattern;
import io.github.patternatlas.api.rest.model.LatexContent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PatternRenderServiceImpl implements PatternRenderService {
    private final String baseAPIEndpoint;
    Logger logger = LoggerFactory.getLogger(PatternRenderServiceImpl.class);

    public PatternRenderServiceImpl(
            @Value("${io.github.patternatlas.api.latexrenderer.hostname}") String hostname,
            @Value("${io.github.patternatlas.api.latexrenderer.port}") int port
    ) {
        this.baseAPIEndpoint = String.format("http://%s:%d/renderLatex/", hostname, port);
    }

    @Autowired
    private ImageService imageService;

    @Autowired
    private DiscussionService discussionService;

    /**
     * @param pattern    Pattern received from Frontend (not saved in DB yet)
     * @param oldVersion Database version of the received Pattern
     * @return content JSON that includes ids of the rendered LaTeX code instead of the LaTeX code
     */
    @Override
    public Object renderContent(Pattern pattern, Pattern oldVersion) {
        String jsonString = null;
        String contentOld = "";
        String renderedContentOld = "";
        ObjectMapper mapper = new ObjectMapper();
        try {
            jsonString = mapper.writeValueAsString(pattern.getContent());
            if (oldVersion == null) {
                contentOld = "null";
                renderedContentOld = "null";
            } else {
                contentOld = mapper.writeValueAsString(oldVersion.getContent());
                renderedContentOld = mapper.writeValueAsString(oldVersion.getRenderedContent());
            }
        } catch (Exception e) {
            return null;
        }

        if (jsonString == null)
            return null;

        //get old quantikz and qcircuit occurences and match them with the related svg tags and imageids
        ArrayList<String> oldContentOccurances = new ArrayList<>();
        ArrayList<String> oldSVGOccurances = new ArrayList<>();
        while (true) {
            Integer[] occuranceStartEndOldQuantikz = getNextOccurance(contentOld, "\\\\begin{quantikz}", "\\end{quantikz}");
            Integer[] occuranceStartEndOldQcircuit = getNextOccurance(contentOld, "\\\\Qcircuit", "end}");
            Integer[] svgOccurencesOld = getNextOccurance(renderedContentOld, "<SVG>", "</SVG>");
            if (((occuranceStartEndOldQcircuit[0] != -1 && occuranceStartEndOldQcircuit[1] != -1) ||
                    (occuranceStartEndOldQuantikz[0] != -1 && occuranceStartEndOldQuantikz[1] != -1)) && svgOccurencesOld[0] != -1 && svgOccurencesOld[1] != -1) {
                if (occuranceStartEndOldQuantikz[0] != -1 && ((occuranceStartEndOldQuantikz[0] < occuranceStartEndOldQcircuit[0]) || occuranceStartEndOldQcircuit[0] == -1)) {
                    //HAS OLD QUANTIKZ CONTENT
                    oldContentOccurances.add(contentOld.substring(occuranceStartEndOldQuantikz[0], occuranceStartEndOldQuantikz[1] + 14)
                            .replaceAll("\\\\n", " ").replaceAll("(\\\\t)(?!a)", " ")
                            .replaceAll("\\\\\\\\", "\\\\"));
                    contentOld = contentOld.replace(contentOld.substring(occuranceStartEndOldQuantikz[0], occuranceStartEndOldQuantikz[1] + 14), " ");
                } else if (occuranceStartEndOldQcircuit[0] != -1 && ((occuranceStartEndOldQcircuit[0] < occuranceStartEndOldQuantikz[0]) || occuranceStartEndOldQuantikz[0] == -1)) {
                    //HAS OLD QCircuit CONTENT
                    oldContentOccurances.add(contentOld.substring(occuranceStartEndOldQcircuit[0], occuranceStartEndOldQcircuit[1] + 4)
                            .replaceAll("\\\\n", " ").replaceAll("(\\\\t)(?!a)", " ")
                            .replaceAll("\\\\\\\\", "\\\\"));
                    contentOld = contentOld.replace(contentOld.substring(occuranceStartEndOldQcircuit[0], occuranceStartEndOldQcircuit[1] + 4), " ");
                }

                oldSVGOccurances.add(renderedContentOld.substring(svgOccurencesOld[0], svgOccurencesOld[1] + 6));
                renderedContentOld = renderedContentOld.replace(renderedContentOld.substring(svgOccurencesOld[0], svgOccurencesOld[1] + 6), " ");
            } else {
                break;
            }
        }

        int countQuantikz = 0;
        while (true) {
            Integer[] occuranceStartEnd = getNextOccurance(jsonString, "\\\\begin{quantikz}", "\\end{quantikz}");
            if (occuranceStartEnd[0] != -1 && occuranceStartEnd[1] != -1) {
                String renderContent = jsonString.substring(occuranceStartEnd[0], occuranceStartEnd[1] + 14)
                        .replaceAll("\\\\n", " ").replaceAll("(\\\\t)(?!a)", " ")
                        .replaceAll("\\\\\\\\", "\\\\");
                boolean occured = false;

                for (int i = 0; i < oldContentOccurances.size(); i++) {
                    if (oldContentOccurances.get(i).equals(renderContent)) {
                        occured = true;
                        jsonString = jsonString.replace(jsonString.substring(occuranceStartEnd[0], occuranceStartEnd[1] + 14), " " + oldSVGOccurances.get(i) + " ");
                    }
                }

                if (!occured) {
                    List<String> settings = new ArrayList<>();
                    settings.add("\\usepackage{tikz} \n");
                    settings.add("\\usetikzlibrary{quantikz} \n");
                    byte[] renderedFile = renderContentViaAPI(renderContent, settings, "svg");
                    String id = saveAndUploadFile(renderedFile, "svg");
                    jsonString = jsonString.replace(jsonString.substring(occuranceStartEnd[0], occuranceStartEnd[1] + 14), " " + id + " ");
                    if (countQuantikz < oldContentOccurances.size()) {
                        this.discussionService.updateTopicsByImageId(UUID.fromString(oldSVGOccurances.get(countQuantikz).substring(5, oldSVGOccurances.get(countQuantikz).length() - 6)), UUID.fromString(id.substring(5, id.length() - 6)));
                    }
                }
                countQuantikz++;
            } else {
                break;
            }
        }

        int countQcircuit = 0;
        while (true) {
            Integer[] occuranceStartEnd = getNextOccurance(jsonString, "\\\\Qcircuit", "end}");
            if (occuranceStartEnd[0] != -1 && occuranceStartEnd[1] != -1) {
                String renderContent = jsonString.substring(occuranceStartEnd[0], occuranceStartEnd[1] + 4)
                        .replaceAll("\\\\n", " ").replaceAll("(\\\\t)(?!a)", " ")
                        .replaceAll("\\\\\\\\", "\\\\");
                boolean occured = false;

                for (int i = 0; i < oldContentOccurances.size(); i++) {
                    if (oldContentOccurances.get(i).equals(renderContent)) {
                        occured = true;
                        jsonString = jsonString.replace(jsonString.substring(occuranceStartEnd[0], occuranceStartEnd[1] + 4), " " + oldSVGOccurances.get(i) + " ");
                    }
                }

                if (!occured) {
                    List<String> settings = new ArrayList<>();
                    settings.add("\\usepackage[braket, qm]{qcircuit} \n");
                    settings.add("\\usepackage{amsmath}  \n");
                    settings.add("\\usepackage{listings} \n");
                    settings.add("\\renewcommand{\\arraystretch}{1.5} \n");
                    renderContent = renderContent.replace(" end}", " }");
                    byte[] renderedFile = renderContentViaAPI(renderContent, settings, "svg");
                    String id = saveAndUploadFile(renderedFile, "svg");
                    jsonString = jsonString.replace(jsonString.substring(occuranceStartEnd[0], occuranceStartEnd[1] + 4), " " + id + " ");
                    if (countQcircuit < oldContentOccurances.size()) {
                        this.discussionService.updateTopicsByImageId(UUID.fromString(oldSVGOccurances.get(countQcircuit).substring(5, oldSVGOccurances.get(countQcircuit).length() - 6)), UUID.fromString(id.substring(5, id.length() - 6)));
                    }
                }
                countQcircuit++;
            } else {
                break;
            }
        }

        Map<String, Object> map = null;
        try {
            map = mapper.readValue(jsonString, Map.class);
        } catch (Exception e) {
            return null;
        }
        return map;
    }

    public Integer[] getNextOccurance(String content, String begin, String end) {
        return new Integer[] {content.indexOf(begin, 0), content.indexOf(end, 0)};
    }

    /**
     * @param content  LaTeX code
     * @param packages LaTeX packages
     * @param output   Output Format for example "svg"
     * @return Renderresult as byte array
     */
    public byte[] renderContentViaAPI(String content, List<String> packages, String output) {
        LatexContent latexContent = new LatexContent(content, packages, output);
        byte[] file = null;
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            String url = baseAPIEndpoint + "?content="
                    + URLEncoder.encode(latexContent.getContent(), "UTF-8");
            for (String latexPackage : latexContent.getLatexPackages()
            ) {
                url += "&packages=" + URLEncoder.encode(latexPackage, "UTF-8");
            }
            ResponseEntity<byte[]> result = restTemplate.getForEntity(url, byte[].class);
            file = result.getBody();
        } catch (Exception e) {
            log.error("could not render LaTeX: " + e.getMessage());
            return null;
        }
        return file;
    }

    /**
     * @param file   File to upload as byte array
     * @param output Filetype for example "svg"
     * @return ImageId surrounded by <SVG></SVG> Tags
     */
    public String saveAndUploadFile(byte[] file, String output) {
        Image image = new Image();
        image.setData(file);
        image.setFileName("latexRender" + Instant.now().getEpochSecond());
        image.setFileType(output);
        Image uploadedImage = imageService.createImage(image);
        return "<SVG>" + uploadedImage.getId().toString() + "</SVG>";
    }
}
