package com.example.webstorydemo.services;

import com.example.webstorydemo.model.payload.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String uploadToCloudinary(MultipartFile file);
}
