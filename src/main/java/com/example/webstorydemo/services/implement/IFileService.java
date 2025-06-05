package com.example.webstorydemo.services.implement;

import com.cloudinary.Cloudinary;
import com.example.webstorydemo.exceptions.external.FileFailedUploadException;
import com.example.webstorydemo.services.FileService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@AllArgsConstructor
@Service
@Slf4j
public class IFileService implements FileService {
    private final Cloudinary cloudinary;

    //upload file len cloud
    @Override
    public String uploadToCloudinary(MultipartFile file)  {
        try{
            //Map data = this.cloudinary.uploader().upload(file.getBytes(), Map.of());
            var data = this.cloudinary.uploader().upload(file.getBytes(), Map.of());
            //System.out.println(data.get("url"));
            return data.get("url")!=null ? data.get("url").toString() : null;
        } catch (IOException io){
            log.info(io.getMessage());
            throw new FileFailedUploadException("Failed upload file");
        }
    }
}
