package com.example.CloudKeeper.service;

import com.example.CloudKeeper.entity.CloudFile;
import com.example.CloudKeeper.exception.ErrorInputData;
import com.example.CloudKeeper.exception.CloudException;
import com.example.CloudKeeper.repository.CloudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Service
public class CloudService {
    private final CloudRepository cloudRepository;

    @Autowired
    public CloudService(CloudRepository cloudRepository) {
        this.cloudRepository = cloudRepository;
    }

    public CloudFile uploadFile(String authToken, String fileName, MultipartFile file) throws IOException {
        CloudFile cloudFilePOJO = new CloudFile(fileName, file.getContentType(), file.getBytes(), file.getSize());
        return cloudRepository.uploadFile(cloudFilePOJO, authToken).orElseThrow(() -> new ErrorInputData("Couldn't save the file " + fileName));
    }

    public Long removeFile(String authToken, String fileName) {
        return cloudRepository.removeFile(authToken, fileName).orElseThrow(() -> new CloudException("Error delete file " + fileName));
    }

    public CloudFile downloadFile(String authToken, String fileName) {
        return cloudRepository.downloadFile(authToken, fileName).orElseThrow(() -> new CloudException("Error download file " + fileName));
    }

    public CloudFile editFile(String authToken, String fileName, String newFileName) {
        return cloudRepository.editFile(authToken, fileName, newFileName).orElseThrow(() -> new CloudException("Error edit file " + fileName));
    }

    public List<CloudFile> getFiles(String authToken, int limit) {
        return cloudRepository.getFiles(authToken, limit).orElseThrow(() -> new CloudException("Error getting file list"));
    }
}
