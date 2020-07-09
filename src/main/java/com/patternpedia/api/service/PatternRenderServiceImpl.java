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
            if(oldVersion == null){
                contentOld = "null";
                renderedContentOld = "null";
            } else {
                contentOld = mapper.writeValueAsString(oldVersion.getContent());
                renderedContentOld = mapper.writeValueAsString(oldVersion.getRenderedContent());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (jsonString == null)
            return null;


        //get old quantikz and qcircuit occurences
        ArrayList<String> oldContentOccurances = new ArrayList<>();
        ArrayList<String> oldSVGOccurances = new ArrayList<>();
        while(true){
            Integer[] occuranceStartEndOldQuantikz = getNextOccurance(contentOld, "\\\\begin{quantikz}", "\\end{quantikz}");
            Integer[] occuranceStartEndOldQcircuit = getNextOccurance(contentOld, "\\\\Qcircuit", "end}");
            Integer[] svgOccurencesOld = getNextOccurance(renderedContentOld, "<SVG>", "</SVG>");
            if (((occuranceStartEndOldQcircuit[0] != -1 && occuranceStartEndOldQcircuit[1] != -1) ||
                    (occuranceStartEndOldQuantikz[0] != -1 && occuranceStartEndOldQuantikz[1] != -1)) && svgOccurencesOld[0] != -1 && svgOccurencesOld[1] != -1) {
                if(occuranceStartEndOldQuantikz[0] != -1 && ((occuranceStartEndOldQuantikz[0] < occuranceStartEndOldQcircuit[0]) || occuranceStartEndOldQcircuit[0] == -1 )){
                    //HAS OLD QUANTIKZ CONTENT
                    oldContentOccurances.add(contentOld.substring(occuranceStartEndOldQuantikz[0], occuranceStartEndOldQuantikz[1] + 14)
                            .replaceAll("\\\\n", " ").replaceAll("(\\\\t)(?!a)"," ")
                            .replaceAll("\\\\\\\\","\\\\"));
                    contentOld = contentOld.replace(contentOld.substring(occuranceStartEndOldQuantikz[0], occuranceStartEndOldQuantikz[1] + 14), " ");
                } else if(occuranceStartEndOldQcircuit[0] != -1 && ((occuranceStartEndOldQcircuit[0] < occuranceStartEndOldQuantikz[0]) || occuranceStartEndOldQuantikz[0] == -1 )){
                    //HAS OLD QCircuit CONTENT
                    oldContentOccurances.add(contentOld.substring(occuranceStartEndOldQcircuit[0], occuranceStartEndOldQcircuit[1] + 4)
                            .replaceAll("\\\\n", " ").replaceAll("(\\\\t)(?!a)"," ")
                            .replaceAll("\\\\\\\\","\\\\"));
                    contentOld = contentOld.replace(contentOld.substring(occuranceStartEndOldQcircuit[0], occuranceStartEndOldQcircuit[1] + 4), " ");}

                oldSVGOccurances.add(renderedContentOld.substring(svgOccurencesOld[0], svgOccurencesOld[1] + 6));
                renderedContentOld = renderedContentOld.replace(renderedContentOld.substring(svgOccurencesOld[0], svgOccurencesOld[1] + 6), " ");
            }
            else {
                break;
            }
        }

        int countQuantikz = 0;
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
                    if(countQuantikz < oldContentOccurances.size()){
                        if(jaccardSimilarity.apply(oldContentOccurances.get(countQuantikz), renderContent) > 0.8) {
                          this.discussionService.updateTopicsByImageId(UUID.fromString(oldSVGOccurances.get(countQuantikz).substring(5, oldSVGOccurances.get(countQuantikz).length() - 6)), UUID.fromString(id.substring(5, id.length() - 6)));
                        }
                    }
                }
                countQuantikz++ ;
            }else {
                break;
            }
        }


        int countQcircuit = 0;
        while (true) {
            Integer[] occuranceStartEnd = getNextOccurance(jsonString, "\\\\Qcircuit", "end}");
            if (occuranceStartEnd[0] != -1 && occuranceStartEnd[1] != -1) {
                String renderContent = jsonString.substring(occuranceStartEnd[0], occuranceStartEnd[1] + 4)
                        .replaceAll("\\\\n", " ").replaceAll("(\\\\t)(?!a)"," ")
                        .replaceAll("\\\\\\\\","\\\\");
                boolean occured = false;

                for (int i = 0; i < oldContentOccurances.size(); i++){
                    if(oldContentOccurances.get(i).equals(renderContent)){
                        occured = true;
                        jsonString = jsonString.replace(jsonString.substring(occuranceStartEnd[0], occuranceStartEnd[1] + 4), " " + oldSVGOccurances.get(i) + " ");
                    }
                }

                if(!occured){
                    List<String> settings = new ArrayList<>();
                    settings.add("\\usepackage[braket, qm]{qcircuit} \n");
                    settings.add("\\usepackage{amsmath}  \n");
                    settings.add("\\usepackage{listings} \n");
                    settings.add("\\renewcommand{\\arraystretch}{1.5} \n");
                    byte []renderedFile = renderContentViaAPI(renderContent, settings, "svg");
                    String id = saveAndUploadFile(renderedFile, "svg");
                    jsonString = jsonString.replace(jsonString.substring(occuranceStartEnd[0], occuranceStartEnd[1] + 4), " " + id + " ");
                    if(countQcircuit < oldContentOccurances.size()){
                        if(jaccardSimilarity.apply(oldContentOccurances.get(countQcircuit), renderContent) > 0.8) {
                            this.discussionService.updateTopicsByImageId(UUID.fromString(oldSVGOccurances.get(countQcircuit).substring(5, oldSVGOccurances.get(countQcircuit).length() - 6)), UUID.fromString(id.substring(5, id.length() - 6)));
                        }
                    }
                }
                countQcircuit++ ;
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
            final String baseUrl = "http://localhost:"+8082+"/renderLatex/";
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
