package com.ecommerce.project.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface iFileService {

    String uploadImage(String path, MultipartFile file) throws IOException;
}
