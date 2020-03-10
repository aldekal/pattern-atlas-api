package com.patternpedia.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.patternpedia.api.entities.Pattern;
import com.patternpedia.api.filestorage.FileStorageProperties;
import com.patternpedia.api.filestorage.FileStorageService;
import com.patternpedia.api.rest.model.AlgorithmType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.core.io.Resource;

import java.io.*;

@Component
public class PatternRenderServiceImpl implements PatternRenderService {

    @Autowired
    private FileStorageService fileStorageService;

    @Override
    public String renderContent(Pattern pattern) {
        String jsonString = null;
        ObjectMapper mapper = new ObjectMapper();
        //Converting the Object to JSONString
        try {
            jsonString = mapper.writeValueAsString(pattern.getContent());
            System.out.println("JSONSTRING" + jsonString);
        } catch (Exception e){

        }
        System.out.println("renderContent");
        AlgorithmType type = checkForAlgorithmInput(jsonString);
        switch (type) {
            case QUANTIKZ:
                return renderQuantikz(jsonString);
            case QCIRCUIT:
                break;
            case NONE:
                return null;
        }

        return null;
    }

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
        if (content.contains("\\begin{quantikz}")) {
            String quantikzContent;
            String originalContent = content;
            String fileDownloadUri = null;
            quantikzContent = content.substring(content.indexOf("\\begin{quantikz}"), content.indexOf("\\end{quantikz}") + 14);
            String latexDocClass = "\\documentclass[border=0.50001bp,convert={convertexe={imgconvert},outext=.png}]{standalone} \n";
            String latexPackages = "\\usepackage{tikz} \n" + "\\usetikzlibrary{quantikz} \n";
            String docStart = "\\begin{document} \n";
            String docEnd = "\\end{document} \n";

            try (FileWriter fileWriter = new FileWriter(new File("renderFile.tex"))) {
                fileWriter.write(latexDocClass + latexPackages + docStart + quantikzContent + docEnd);
            } catch (IOException e) {
                System.out.println(e.toString());
                e.printStackTrace();
            }
            try {
                Runtime rt = Runtime.getRuntime();
                // On Windows put "cmd /c"
                Process pr = rt.exec("cmd /c pdflatex -shell-escape renderFile.tex");
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
            String showimage =  " ![](" + fileDownloadUri +")";
            System.out.println(content.replace(quantikzContent , showimage));
            return content.replace(quantikzContent , showimage).replaceAll("\\\\" , "\\\\\\\\" );
        }
        return null;
    }

}
