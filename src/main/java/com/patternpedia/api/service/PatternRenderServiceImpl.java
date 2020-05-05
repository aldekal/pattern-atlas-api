package com.patternpedia.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.patternpedia.api.entities.Image;
import com.patternpedia.api.entities.Pattern;
import com.patternpedia.api.filestorage.FileStorageService;
import com.patternpedia.api.rest.model.LatexContent;
import javafx.util.Pair;
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
    private FileStorageService fileStorageService;

    @Autowired
    private ImageService imageService;

    @Override
    public Object renderContent(Pattern pattern) {
        String jsonString = null;
        ObjectMapper mapper = new ObjectMapper();

        //Converting the Object to JSONString
        try {
            jsonString = mapper.writeValueAsString(pattern.getContent());
            // TODO: fix no newline issue. Optimize that only current section gets rendered on update
            System.out.println("notasstring" + pattern.getContent());
            System.out.println("JSONSTRING" + jsonString);
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (jsonString == null)
            return null;

        //get all quantikz occurences
        boolean quantikz = true;
        while (quantikz) {
            Pair<Integer, Integer> occuranceStartEnd = getNextOccurance(jsonString, "\\\\begin{quantikz}", "\\end{quantikz}");
            if (occuranceStartEnd.getKey() != -1 && occuranceStartEnd.getValue() != -1) {
                String renderContent = jsonString.substring(occuranceStartEnd.getKey(), occuranceStartEnd.getValue() + 14)
                .replaceAll("\\\\n", " ").replaceAll("(\\\\t)(?!a)"," ").replaceAll("\\\\\\\\","\\\\");
                List<String> settings = new ArrayList<>();
                settings.add("\\usepackage{tikz} \n");
                settings.add("\\usetikzlibrary{quantikz} \n");
                byte []renderedFile = renderContentViaAPI(renderContent, settings, "svg");
                jsonString = jsonString.replace(jsonString.substring(occuranceStartEnd.getKey(), occuranceStartEnd.getValue() + 14), " " + saveAndUploadFile(renderedFile, "svg") + " ");
                //filesystem version
//              jsonString = jsonString.replace(jsonString.substring(occuranceStartEnd.getKey(), occuranceStartEnd.getValue() + 14), " ![](" + saveAndUploadFile(renderedFile, "svg") + ")");
            }else {
                break;
            }
        }

        boolean qcircuit = true;
        while (qcircuit) {
            Pair<Integer, Integer> occuranceStartEnd = getNextOccurance(jsonString, "\\\\Qcircuit", "end}");
            if (occuranceStartEnd.getKey() != -1 && occuranceStartEnd.getValue() != -1) {
                String renderContent = jsonString.substring(occuranceStartEnd.getKey(), occuranceStartEnd.getValue() + 4)
                        .replaceAll("\\\\n", " ").replaceAll("(\\\\t)(?!a)"," ").replaceAll("\\\\\\\\","\\\\");
                renderContent.replace("end}", "}");
                List<String> settings = new ArrayList<>();
                settings.add("\\usepackage[braket, qm]{qcircuit} \n");
                settings.add("\\usepackage{amsmath}  \n");
                settings.add("\\usepackage{listings} \n");
                settings.add("\\renewcommand{\\arraystretch}{1.5} \n");
                byte []renderedFile = renderContentViaAPI(renderContent, settings, "svg");
                jsonString = jsonString.replace(jsonString.substring(occuranceStartEnd.getKey(), occuranceStartEnd.getValue() + 4), " " + saveAndUploadFile(renderedFile, "svg") + " ");
                //filesystem version
//                jsonString = jsonString.replace(jsonString.substring(occuranceStartEnd.getKey(), occuranceStartEnd.getValue() + 4), " ![](" + saveAndUploadFile(renderedFile, "svg") + ")");
            }else {
                break;
            }
        }


        Map<String, Object> map = null;
        try {
            System.out.println(jsonString);
            map = mapper.readValue(jsonString, Map.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (map == null)
            return null;
        return map;

    }

    public Pair<Integer, Integer> getNextOccurance(String content, String begin, String end) {
        System.out.println("content" + content);
        return new Pair<>(content.indexOf(begin, 0), content.indexOf(end, 0));
    }


    byte[] renderContentViaAPI(String content, List<String> packages, String output){
        LatexContent latexContent = new LatexContent(content,packages,output);
        byte[] file = null;
        try{

            RestTemplate restTemplate = new RestTemplate();
            final String baseUrl = "http://localhost:"+8081+"/renderLatex/";

            URI uri = new URI(baseUrl);
            HttpHeaders headers = new HttpHeaders();
            // set `content-type` header
            headers.setContentType(MediaType.APPLICATION_JSON);
            System.out.println("presend");
            HttpEntity<LatexContent> entity = new HttpEntity<>(latexContent, headers);
            ResponseEntity<byte[]> result = restTemplate.postForEntity(uri, entity, byte[].class);
            file = result.getBody();
        } catch (Exception e){
            e.printStackTrace();
        }
//        System.out.println("filecontent" + new String(file, StandardCharsets.UTF_8));
        System.out.println("returnFileFromAPI");
        return file;
    }

//    String saveAndUploadFile(byte[] file, String output){
//        System.out.println("save");
//        String fileName = null;
//        String fileDownloadUri = null;
//        try {
//            //https://stackoverflow.com/questions/16648549/converting-file-to-multipartfile
//            String name = "renderFile."+output;
//            String originalFileName = "renderFile."+output;
//            String contentType = "image/svg+xml";
//
//
//            MultipartFile multipartFile = new MockMultipartFile(name,
//                    originalFileName, contentType, file);
//            fileName = fileStorageService.storeFile(multipartFile);
//            fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
//                    .path("/downloadFile/")
//                    .path(fileName)
//                    .toUriString();
//            System.out.println(fileDownloadUri);
//            return fileDownloadUri;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    };

    String saveAndUploadFile(byte[] file, String output) {
        System.out.println("save");
        Image image = new Image();
        image.setData(file);
        image.setFileName("latexRender" + Instant.now().getEpochSecond());
        image.setFileType("svg");
        Image uploadedImage = imageService.createImage(image);
        return "<SVG>" + uploadedImage.getId().toString() + "</SVG>";

//        String fileName = null;
//        String fileDownloadUri = null;
//        try {
//            //https://stackoverflow.com/questions/16648549/converting-file-to-multipartfile
//            String name = "renderFile."+output;
//            String originalFileName = "renderFile."+output;
//            String contentType = "image/svg+xml";
//
//
//            MultipartFile multipartFile = new MockMultipartFile(name,
//                    originalFileName, contentType, file);
//            fileName = fileStorageService.storeFile(multipartFile);
//            fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
//                    .path("/downloadFile/")
//                    .path(fileName)
//                    .toUriString();
//            System.out.println(fileDownloadUri);
////            System.out.print(new String(imageService.getImageById(uploadedImage.getId()).getData(), StandardCharsets.UTF_8).replaceAll("\"", "\\\""));
//            return "<SVG>" + uploadedImage.getId().toString() +"</SVG>";
////            return fileDownloadUri;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    };
    }
}
