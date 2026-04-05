package com.ecommerce.project.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileService implements iFileService{

    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {
        //Obtener nombre del archivo original
        String originalFileName = file.getOriginalFilename();

        //nombrar el archivo de forma unica para evitar conflictos de nombres. (Generar un nombre unico)
        String randomId = UUID.randomUUID().toString();
        //mat.jpg --> 1234 --> 1234.jpg
        String fileName = randomId.concat(originalFileName.substring(originalFileName.lastIndexOf('.')));
        String filePath = path + File.separator + fileName;

        //Comprobar si la rutha existe y crearla
        File folder = new File(path);
        if(!folder.exists())
            folder.mkdir();

        //proceso de carga al servidor
        Files.copy(file.getInputStream(), Paths.get(filePath));

        //Retornar el nombre del archivo
        return fileName;

    }
}
