package com.godzillajim.tracker.services;

import com.godzillajim.tracker.exceptions.TrackerAuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;

@Service
public class FileStorageService {
    private Path fileStorageLocation;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties){
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
            .toAbsolutePath().normalize();
    }
    public String storeFile(MultipartFile file) throws Exception {

        String filename = new Date().toString().replaceAll(":","-").replaceAll(" ", "")+StringUtils.cleanPath(file.getOriginalFilename());
        try{
            File file1 = new File(fileStorageLocation + filename);
            if(!file1.exists()){
                file1.createNewFile();
            }
            Path filePath = Path.of(file1.getPath());
            file.transferTo(filePath);
            return filename;
        } catch(Exception e){
            throw new Exception(e);
        }
    }
    public Resource loadFileAsResource(String fileName) throws Exception {
        try{
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(!resource.exists()){
                throw new FileNotFoundException("File not created yet");
            }
            return resource;
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
}
