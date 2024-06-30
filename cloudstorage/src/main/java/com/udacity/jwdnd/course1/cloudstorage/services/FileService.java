package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.File;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {
    private final FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }


    public void insertFile(File file){
        fileMapper.insertFile(file);
    }

    public List<File> getAllFiles() {
        return fileMapper.getFiles();
    }

    public int deleteFile(int id) {
        return fileMapper.deleteFile(id);
    }

    public File getFileById(int id) {
        return fileMapper.getFile(id);
    }

    public boolean checkFileNameExist(String filename) {
        List<File> files = fileMapper.getFiles();
        for (File file : files) {
            if (file.getFilename().equals(filename)) {
                return true;
            }
        }
        return false;
    }
}
