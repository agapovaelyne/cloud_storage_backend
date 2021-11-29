package com.example.CloudKeeper.repository;

import com.example.CloudKeeper.entity.CloudFile;
import com.example.CloudKeeper.model.TestModels;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.of;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CloudRepositoryTest {
    private final TestModels testModels = new TestModels();

    private final FileRepository fileRepositoryMock = Mockito.mock(FileRepository.class);
    private final UserRepository userRepositoryMock = Mockito.mock(UserRepository.class);
    private final CloudRepository cloudRepository = new CloudRepository(userRepositoryMock, fileRepositoryMock);

    @BeforeEach
    void login_test_token() {
        cloudRepository.login(testModels.getAuthToken(), testModels.getUserPrincipal());
        Mockito.when(userRepositoryMock.findByLogin(Mockito.anyString()))
                .thenReturn(of(testModels.getUser()));
    }

    @Test
    void uploadFile_test() {
        Mockito.when(fileRepositoryMock.save(testModels.getFile()))
                .thenReturn(testModels.getFile());
        Optional<CloudFile> actual = cloudRepository.uploadFile(testModels.getFile(), testModels.getWebAuthToken());
        Optional<CloudFile> expected = of(testModels.getFile());
        assertEquals(expected, actual);
    }

    @Test
    void removeFile_test() {
        Mockito.when(fileRepositoryMock.removeByUserIdAndName(testModels.getUserId(), testModels.getFilename()))
                .thenReturn(of(testModels.getFile().getId()));
        Optional<Long> actual = cloudRepository.removeFile(testModels.getWebAuthToken(), testModels.getFilename());
        Optional<Long> expected = of(testModels.getFile().getId());
        assertEquals(expected, actual);
    }

    @Test
    void downloadFile_test() {
        Mockito.when(fileRepositoryMock.findByUserIdAndName(testModels.getUserId(), testModels.getFilename()))
                .thenReturn(of(testModels.getFile()));
        Optional<CloudFile> actual = cloudRepository.downloadFile(testModels.getWebAuthToken(), testModels.getFilename());
        Optional<CloudFile> expected = of(testModels.getFile());
        assertEquals(expected, actual);
    }

    @Test
    void editFile_test() {
        Mockito.when(fileRepositoryMock.findByUserIdAndName(testModels.getUserId(), testModels.getFilename()))
                .thenReturn(of(testModels.getFile()));
        Mockito.when(fileRepositoryMock.save(testModels.getFile()))
                .thenReturn(testModels.getFile());
        Optional<CloudFile> actual = cloudRepository.editFile(testModels.getWebAuthToken(), testModels.getFilename(), testModels.getNewFilename());
        Optional<CloudFile> expected = of(testModels.getFile());
        assertEquals(expected, actual);
    }

    @Test
    void getFiles_test() {
        Mockito.when(fileRepositoryMock.findAllByUserIdWithLimit(testModels.getUserId(), testModels.getLimit()))
                .thenReturn(testModels.getFileList());
        Optional<List<CloudFile>> actual = cloudRepository.getFiles(testModels.getWebAuthToken(), testModels.getLimit());
        Optional<List<CloudFile>> expected = of(testModels.getFileList());
        assertEquals(expected, actual);
    }
}
