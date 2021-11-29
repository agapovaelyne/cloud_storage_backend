package com.example.CloudKeeper.controller;

import com.example.CloudKeeper.entity.CloudFile;
import com.example.CloudKeeper.model.TestModels;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CloudControllerTest {

    private final TestModels testModels = new TestModels();
    private final CloudController cloudController = new CloudController(testModels.getCloudServiceMock());
    private final ResponseEntity<?> responseOkOnly = new ResponseEntity<>(HttpStatus.OK);

    @Test
    void uploadFile_test() throws IOException {
        ResponseEntity<?> actual = cloudController.uploadFile(testModels.getAuthToken(), testModels.getFilename(), testModels.getMultipartFileMock());
        ResponseEntity<?> expected = responseOkOnly;
        assertEquals(expected, actual);
    }

    @Test
    void removeFile_test() {
        ResponseEntity<?> actual = cloudController.removeFile(testModels.getAuthToken(), testModels.getFilename());
        ResponseEntity<?> expected = responseOkOnly;
        assertEquals(expected, actual);
    }

    @Test
    void downloadFile_test() {
        ResponseEntity<byte[]> actual = cloudController.downloadFile(testModels.getAuthToken(), testModels.getFilename());
        ResponseEntity<byte[]> expected = ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(testModels.getFile().getType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + testModels.getFile().getName() + "\"")
                .body(testModels.getFile().getData());
        assertEquals(expected, actual);
    }

    @Test
    void editFile_test() {
        ResponseEntity<?> actual = cloudController.editFile(testModels.getAuthToken(), testModels.getFilename(), testModels.getBodyParams());
        ResponseEntity<?> expected = responseOkOnly;
        assertEquals(expected, actual);
    }

    @Test
    void getFiles_test() {
        List<CloudFile> actual = cloudController.getFiles(testModels.getAuthToken(), testModels.getLimit());
        List<CloudFile> expected = testModels.getFileList();
        assertEquals(expected, actual);
    }
}
