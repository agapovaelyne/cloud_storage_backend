package com.example.CloudKeeper.service;

import com.example.CloudKeeper.entity.CloudFile;
import com.example.CloudKeeper.model.TestModels;
import com.example.CloudKeeper.repository.CloudRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.List;

import static java.util.Optional.of;

public class CloudServiceTest {
    private final TestModels testModels = new TestModels();
    CloudRepository cloudRepositoryMock = Mockito.mock(CloudRepository.class);
    CloudService cloudService = new CloudService(cloudRepositoryMock, testModels.getAuthenticationManagerMock(), testModels.getJwtUtilsMock());

    @Test
    void uploadFile_test() throws IOException {
        Mockito.when(cloudRepositoryMock.uploadFile(Mockito.any(),Mockito.any()))
                .thenReturn(of(testModels.getFile()));
        CloudFile actual = cloudService.uploadFile(testModels.getAuthToken(), testModels.getFilename(), testModels.getMultipartFileMock());
        CloudFile expected = testModels.getFile();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void removeFile_test() {
        Mockito.when(cloudRepositoryMock.removeFile(testModels.getAuthToken(), testModels.getFilename()))
                .thenReturn(of(testModels.getFile().getId()));
        Long actual = cloudService.removeFile(testModels.getAuthToken(), testModels.getFilename());
        Long expected = testModels.getFile().getId();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void downloadFile_test() throws IOException {
        Mockito.when(cloudRepositoryMock.downloadFile(testModels.getAuthToken(), testModels.getFilename()))
                .thenReturn(of(testModels.getFile()));
        CloudFile actual = cloudService.downloadFile(testModels.getAuthToken(), testModels.getFilename());
        CloudFile expected = testModels.getFile();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void editFile_test() {
        Mockito.when(cloudRepositoryMock.editFile(testModels.getAuthToken(), testModels.getFilename(), testModels.getNewFilename()))
                .thenReturn(of(testModels.getEditedFile()));
        CloudFile actual = cloudService.editFile(testModels.getAuthToken(), testModels.getFilename(), testModels.getNewFilename());
        CloudFile expected = testModels.getEditedFile();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void editFile_newName_test() {
        Mockito.when(cloudRepositoryMock.editFile(testModels.getAuthToken(), testModels.getFilename(), testModels.getNewFilename()))
                .thenReturn(of(testModels.getEditedFile()));
        String actual = cloudService.editFile(testModels.getAuthToken(), testModels.getFilename(), testModels.getNewFilename()).getName();
        String expected = testModels.getNewFilename();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getFiles_test() {
        Mockito.when(cloudRepositoryMock.getFiles(testModels.getAuthToken(), testModels.getLimit()))
                .thenReturn(of(testModels.getFileList()));
        List<CloudFile> actual = cloudService.getFiles(testModels.getAuthToken(), testModels.getLimit());
        List<CloudFile> expected = testModels.getFileList();
        Assertions.assertEquals(expected, actual);
    }
}
