package com.CMS.kinoCMS.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileUploadService {
    @Value("${upload.path}")
    private String uploadPath;

    public String uploadFile(MultipartFile file) throws IOException{
        if(file == null || file.isEmpty()){
            throw new IllegalArgumentException("File is empty or null");
        }
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
        String uuidFile = UUID.randomUUID().toString();
        String resultFilename = uuidFile + "." + file.getOriginalFilename();
        file.transferTo(new File(uploadPath + "/" + resultFilename));

        return resultFilename;
    }
}
