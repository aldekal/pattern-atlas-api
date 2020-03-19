package com.patternpedia.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.patternpedia.api.entities.Pattern;
import com.patternpedia.api.filestorage.FileStorageService;
import com.patternpedia.api.rest.model.AlgorithmType;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.io.*;
import java.util.*;

@Component
public class PatternRenderServiceImpl implements PatternRenderService {

    @Autowired
    private FileStorageService fileStorageService;

    @Override
    public Object renderContent(Pattern pattern) {
        String jsonString = null;
        ObjectMapper mapper = new ObjectMapper();

        //Converting the Object to JSONString
        try {
            jsonString = mapper.writeValueAsString(pattern.getContent());
            // TODO: fix no newline issue. Optimize that only current section gets rendered on update
            jsonString = jsonString.replaceAll("\\\\n", " ").replaceAll("(\\\\t)(?!a)"," ").replaceAll("\\\\\\\\","\\\\");
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
            Pair<Integer, Integer> occuranceStartEnd = getNextOccurance(jsonString, "\\begin{quantikz}", "\\end{quantikz}");
            if (occuranceStartEnd.getKey() != -1 && occuranceStartEnd.getValue() != -1) {
                jsonString = renderLatex(jsonString, occuranceStartEnd.getKey(), occuranceStartEnd.getValue(),AlgorithmType.QUANTIKZ);
            }else {
                break;
            }
        }

        //get all qcircuit occurences
        boolean qcircuit = true;
        while (qcircuit) {
            Pair<Integer, Integer> occuranceStartEnd = getNextOccurance(jsonString, "\\Qcircuit", "end}");
            if (occuranceStartEnd.getKey() != -1 && occuranceStartEnd.getValue() != -1) {
                jsonString = renderLatex(jsonString, occuranceStartEnd.getKey(), occuranceStartEnd.getValue(),AlgorithmType.QCIRCUIT);
            }else {
                break;
            }
        }

        Map<String, Object> map = null;
        try {
            System.out.println(jsonString);
            map = mapper.readValue(jsonString.replaceAll("\\\\", "\\\\\\\\"), Map.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (map == null)
            return null;
        return map;

//        AlgorithmType type = checkForAlgorithmInput(jsonString);
//        switch (type) {
//            case QUANTIKZ:
//                Map<String, Object> map = null;
//                try {
//                    String returnJson = renderQuantikz(jsonString);
//                    System.out.println(returnJson);
//                    map = mapper.readValue(returnJson, Map.class);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                return map;
//            case QCIRCUIT:
//                break;
//            case NONE:
//                return null;
//        }


    }

    public Pair<Integer, Integer> getNextOccurance(String content, String begin, String end) {
        System.out.println("content" + content);
        return new Pair<>(content.indexOf(begin, 0), content.indexOf(end, 0));
    }

//    @Override
//    public Map<Integer, Integer> getBeginAndEndIndexes(String content, String begin, String end) {
//        int lastIndexBegin = 0;
//        List<Integer> beginList = new ArrayList<>();
//        while (lastIndexBegin != -1) {
//            lastIndexBegin = content.indexOf(begin, lastIndexBegin);
//            if (lastIndexBegin != -1) {
//                beginList.add(lastIndexBegin);
//                lastIndexBegin += begin.length();
//            }
//        }
//
//        int lastIndexEnd = 0;
//        List<Integer> endList = new ArrayList<>();
//        while (lastIndexEnd != -1) {
//            lastIndexEnd = content.indexOf(end, lastIndexEnd);
//            if (lastIndexEnd != -1) {
//                endList.add(lastIndexEnd);
//                lastIndexEnd += end.length();
//            }
//        }
//
//        if (beginList.size() == endList.size()) {
//            Map<Integer, Integer> map = new HashMap<Integer, Integer>();
//            for (int i = 0; i < beginList.size(); i++) {
//                map.put(beginList.get(i), endList.get(i));
//            }
//            return map;
//        }
//        return null;
//    }

    @Override
    public AlgorithmType checkForAlgorithmInput(String content) {
        if (content != null) {
            if (content.contains("\\begin{quantikz}")) {
                System.out.println("Quantikz");
                return AlgorithmType.QUANTIKZ;
            }
        }
        if (content.contains("\\Qcircuit")) {
            return AlgorithmType.QCIRCUIT;
        }
        return AlgorithmType.NONE;
    }

    @Override
    public String renderQuantikz(String content) {
//        if (content.contains("\\begin{quantikz}")) {
//            String quantikzContent;
//            String fileDownloadUri = null;
//            quantikzContent = content.substring(content.indexOf("\\begin{quantikz}"), content.indexOf("\\end{quantikz}") + 14);
//            String latexDocClass = "\\documentclass[border=0.50001bp,convert={convertexe={imgconvert},outext=.png}]{standalone} \n";
//            String latexPackages = "\\usepackage{tikz} \n" + "\\usetikzlibrary{quantikz} \n";
//            String docStart = "\\begin{document} \n";
//            String docEnd = " \\end{document} \n";
//
//            try (FileWriter fileWriter = new FileWriter(new File("renderFile.tex"))) {
//                fileWriter.write(latexDocClass + latexPackages + docStart + quantikzContent + docEnd);
//            } catch (IOException e) {
//                System.out.println(e.toString());
//                e.printStackTrace();
//            }
//            try {
//                Runtime rt = Runtime.getRuntime();
//                // On Windows put "cmd /c"
//                Process pr = rt.exec("cmd /c pdflatex -shell-escape renderFile.tex");
//                pr.waitFor();
//
//            } catch (Exception e) {
//                System.out.println(e.toString());
//                e.printStackTrace();
//            }
//            //https://www.callicoder.com/spring-boot-file-upload-download-rest-api-example/
//            String fileName = null;
//            try {
//                //https://stackoverflow.com/questions/16648549/converting-file-to-multipartfile
//                Path path = Paths.get("./renderFile.png");
//                String name = "renderFile.png";
//                String originalFileName = "renderFile.png";
//                String contentType = "image/png";
//                byte[] pictureContent = null;
//                try {
//                    pictureContent = Files.readAllBytes(path);
//                } catch (final IOException e) {
//                }
//                MultipartFile multipartFile = new MockMultipartFile(name,
//                        originalFileName, contentType, pictureContent);
//                fileName = fileStorageService.storeFile(multipartFile);
//                fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
//                        .path("/downloadFile/")
//                        .path(fileName)
//                        .toUriString();
//                System.out.println(fileDownloadUri);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            String showimage = "![](" + fileDownloadUri + ")";
//            return content.replace(quantikzContent, showimage).replaceAll("\\\\", "\\\\\\\\");
//        }
        return null;
    }


    @Override
    public String renderLatex(String content, int begin, int end, AlgorithmType type) {
        String renderContent = "";
        String latexPackages = "";
        String latexDocClass = "\\documentclass[border=0.50001bp,convert={convertexe={imgconvert},outext=.png}]{standalone} \n";
        String docStart = "\\begin{document} \n";
        String docEnd = " \\end{document} \n";
        String fileDownloadUri = null;

        if (type.equals(AlgorithmType.QUANTIKZ)) {
            System.out.println(content + " " + begin + " "+ end);
            renderContent = content.substring(begin, end + 14);
            latexPackages = "\\usepackage{tikz} \n" + "\\usetikzlibrary{quantikz} \n";
        } else if(type.equals(AlgorithmType.QCIRCUIT)) {
            renderContent = content.substring(begin, end + 4);
            latexPackages = "\\usepackage[braket, qm]{qcircuit} \n" + "\\usepackage{amsmath}  \n" + "\\usepackage{listings} \n" + "\\renewcommand{\\arraystretch}{1.5} \n";
        }

        try (FileWriter fileWriter = new FileWriter(new File("renderFile.tex"))) {
            fileWriter.write(latexDocClass + latexPackages + docStart + renderContent + docEnd);
        } catch (IOException e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
        try {
//            Runtime rt = Runtime.getRuntime();
//            // On Windows put "cmd /c"
//            Process pr = rt.exec("cmd /c pdflatex renderFile.tex");

            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command("cmd.exe", "/c", "pdflatex -shell-escape renderFile.tex");
            Process process = processBuilder.start();

            StringBuilder output = new StringBuilder();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }

            int exitVal = process.waitFor();
            if (exitVal == 0) {
                System.out.println("Success!");
                System.out.println(output);
            } else {
                //abnormal...
            }

//            pr.waitFor();
            System.out.println("executing render");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
        //https://www.callicoder.com/spring-boot-file-upload-download-rest-api-example/
        String fileName = null;
        try {
            //https://stackoverflow.com/questions/16648549/converting-file-to-multipartfile
            Path path = Paths.get("./renderFile.png");
            String name = "renderFile.png";
            String originalFileName = "renderFile.png";
            String contentType = "image/png";
            byte[] pictureContent = null;
            try {
                pictureContent = Files.readAllBytes(path);
            } catch (final IOException e) {
            }
            MultipartFile multipartFile = new MockMultipartFile(name,
                    originalFileName, contentType, pictureContent);
            fileName = fileStorageService.storeFile(multipartFile);
            fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/downloadFile/")
                    .path(fileName)
                    .toUriString();
            System.out.println(fileDownloadUri);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(fileDownloadUri);
        String showimage = "![](" + fileDownloadUri + ") ";
        return content.replace(renderContent, showimage);


    }


}
