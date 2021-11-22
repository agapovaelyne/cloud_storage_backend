package com.example.CloudKeeper.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
@Data
@NoArgsConstructor
public class CloudMultipartFile extends CloudFile implements MultipartFile  {

    public CloudMultipartFile(CloudFile file) {
        super(file.getName(), file.getType(), file.getData(), file.getFileSize());
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public String getOriginalFilename() {
        return super.getName();
    }

    @Override
    public String getContentType() {
        return super.getType();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public long getSize() {
        return super.getFileSize();
    }

    @Override
    public byte[] getBytes() throws IOException {
        return super.getData();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(super.getData());
    }

    @Override
    public void transferTo(File file) throws IOException, IllegalStateException {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(super.getData());
        }
    }
}
