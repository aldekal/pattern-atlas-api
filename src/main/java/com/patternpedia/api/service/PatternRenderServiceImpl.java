package com.patternpedia.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.patternpedia.api.entities.DiscussionTopic;
import com.patternpedia.api.entities.Image;
import com.patternpedia.api.entities.Pattern;
import com.patternpedia.api.rest.model.LatexContent;
import org.apache.commons.text.similarity.JaccardSimilarity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.Instant;
import java.util.*;

@Component
public class PatternRenderServiceImpl implements PatternRenderService {
    @Autowired
    private ImageService imageService;

    @Autowired
    private DiscussionService discussionService;

    @Override
    public Object renderContent(Pattern pattern, Pattern oldVersion) {
        String jsonString = null;
        String contentOld = "";
        String renderedContentOld = "";
        ObjectMapper mapper = new ObjectMapper();
        try {
            jsonString = mapper.writeValueAsString(pattern.getContent());
            contentOld = mapper.writeValueAsString(oldVersion.getContent());
            renderedContentOld = mapper.writeValueAsString(oldVersion.getRenderedContent());
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (jsonString == null)
            return null;

        //get all quantikz occurences
        ArrayList<String> oldContentOccurances = new ArrayList<>();
        ArrayList<String> oldSVGOccurances = new ArrayList<>();
        while(true){
            Integer[] occuranceStartEndOld = getNextOccurance(contentOld, "\\\\begin{quantikz}", "\\end{quantikz}");
            System.out.println("\\end{quantikz}".length());
            Integer[] svgOccurencesOld = getNextOccurance(renderedContentOld, "<SVG>", "</SVG>");
            if (occuranceStartEndOld[0] != -1 && occuranceStartEndOld[1] != -1 && svgOccurencesOld[0] != -1 && svgOccurencesOld[1] != -1) {
                oldContentOccurances.add(contentOld.substring(occuranceStartEndOld[0], occuranceStartEndOld[1] + 14)
                        .replaceAll("\\\\n", " ").replaceAll("(\\\\t)(?!a)"," ")
                        .replaceAll("\\\\\\\\","\\\\"));
                oldSVGOccurances.add(renderedContentOld.substring(svgOccurencesOld[0], svgOccurencesOld[1] + 6));
                contentOld = contentOld.replace(contentOld.substring(occuranceStartEndOld[0], occuranceStartEndOld[1] + 14), " ");
                renderedContentOld = renderedContentOld.replace(renderedContentOld.substring(svgOccurencesOld[0], svgOccurencesOld[1] + 6), " ");
            }
            else {
                break;
            }
        }
        oldContentOccurances.forEach(s -> System.out.println("OLD CONTENT" + s));
        oldSVGOccurances.forEach(s -> System.out.println("OLD SVG" +s));
        int count = 0;
        JaccardSimilarity jaccardSimilarity =  new JaccardSimilarity();
        while (true) {

            Integer[] occuranceStartEnd = getNextOccurance(jsonString, "\\\\begin{quantikz}", "\\end{quantikz}");
            if (occuranceStartEnd[0] != -1 && occuranceStartEnd[1] != -1) {
                String renderContent = jsonString.substring(occuranceStartEnd[0], occuranceStartEnd[1] + 14)
                        .replaceAll("\\\\n", " ").replaceAll("(\\\\t)(?!a)"," ")
                        .replaceAll("\\\\\\\\","\\\\");
                boolean occured = false;

                for (int i = 0; i < oldContentOccurances.size(); i++){
                    if(oldContentOccurances.get(i).equals(renderContent)){
                        occured = true;
                        jsonString = jsonString.replace(jsonString.substring(occuranceStartEnd[0], occuranceStartEnd[1] + 14), " " + oldSVGOccurances.get(i) + " ");
                        System.out.print("INSIDE OF EQUALS FOR " + i + "  " + jsonString);
                    }
                }

                if(!occured){
                    List<String> settings = new ArrayList<>();
                    settings.add("\\usepackage{tikz} \n");
                    settings.add("\\usetikzlibrary{quantikz} \n");
                    byte []renderedFile = renderContentViaAPI(renderContent, settings, "svg");
                    String id = saveAndUploadFile(renderedFile, "svg");
                    jsonString = jsonString.replace(jsonString.substring(occuranceStartEnd[0], occuranceStartEnd[1] + 14), " " + id + " ");
                    if(count < oldContentOccurances.size()){
                        if(jaccardSimilarity.apply(oldContentOccurances.get(count), renderContent) > 0.9) {
                          System.out.println(id);
                          this.discussionService.updateTopicsByImageId(UUID.fromString(oldSVGOccurances.get(count).substring(5, oldSVGOccurances.get(count).length() - 6)), UUID.fromString(id.substring(5, id.length() - 6)));
                        }
                    }
                    System.out.println("OUTSIDE OF EQUALS" + jsonString);
                }
                count++ ;
            }else {
                break;
            }
        }

        while (true) {
            Integer[] occuranceStartEnd = getNextOccurance(jsonString, "\\\\Qcircuit", "end}");
            if (occuranceStartEnd[0] != -1 && occuranceStartEnd[1] != -1) {
                String renderContent = jsonString.substring(occuranceStartEnd[0], occuranceStartEnd[1] + 4)
                        .replaceAll("\\\\n", " ").replaceAll("(\\\\t)(?!a)"," ").replaceAll("\\\\\\\\","\\\\");
                renderContent.replace("end}", "}");
                List<String> settings = new ArrayList<>();
                settings.add("\\usepackage[braket, qm]{qcircuit} \n");
                settings.add("\\usepackage{amsmath}  \n");
                settings.add("\\usepackage{listings} \n");
                settings.add("\\renewcommand{\\arraystretch}{1.5} \n");
                byte []renderedFile = renderContentViaAPI(renderContent, settings, "svg");
                jsonString = jsonString.replace(jsonString.substring(occuranceStartEnd[0], occuranceStartEnd[1] + 4), " " + saveAndUploadFile(renderedFile, "svg") + " ");
            }else {
                break;
            }
        }


        Map<String, Object> map = null;
        try {
            map = mapper.readValue(jsonString, Map.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (map == null)
            return null;
        return map;

    }

    public Integer[] getNextOccurance(String content, String begin, String end) {
        return new Integer[]{content.indexOf(begin, 0), content.indexOf(end, 0)};
    }


    public byte[] renderContentViaAPI(String content, List<String> packages, String output){
        LatexContent latexContent = new LatexContent(content,packages,output);
        byte[] file = null;
        try{
            RestTemplate restTemplate = new RestTemplate();
            final String baseUrl = "http://localhost:"+8081+"/renderLatex/";
            URI uri = new URI(baseUrl);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<LatexContent> entity = new HttpEntity<>(latexContent, headers);
            ResponseEntity<byte[]> result = restTemplate.postForEntity(uri, entity, byte[].class);
            file = result.getBody();
        } catch (Exception e){
            e.printStackTrace();
        }
        return file;
    }


    public String saveAndUploadFile(byte[] file, String output) {
        Image image = new Image();
        image.setData(file);
        image.setFileName("latexRender" + Instant.now().getEpochSecond());
        image.setFileType("svg");
        Image uploadedImage = imageService.createImage(image);
        return "<SVG>" + uploadedImage.getId().toString() + "</SVG>";
    }
}
