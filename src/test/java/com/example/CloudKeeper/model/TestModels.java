package com.example.CloudKeeper.model;

import com.example.CloudKeeper.DTO.AuthorizationRequestDTO;
import com.example.CloudKeeper.DTO.AuthorizationTokenDTO;
import com.example.CloudKeeper.entity.CloudFile;
import com.example.CloudKeeper.entity.Role;
import com.example.CloudKeeper.entity.User;
import com.example.CloudKeeper.repository.FileRepository;
import com.example.CloudKeeper.repository.UserRepository;
import com.example.CloudKeeper.security.JwtUtils;
import com.example.CloudKeeper.service.CloudService;
import com.example.CloudKeeper.service.UserService;
import lombok.Data;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Optional.of;
import static org.mockito.ArgumentMatchers.any;

@Data
public class TestModels {

    private final MultipartFile multipartFileMock = Mockito.mock(MultipartFile.class);
    private final FileRepository fileRepositoryMock = Mockito.mock(FileRepository.class);
    private final UserRepository userRepositoryMock = Mockito.mock(UserRepository.class);
    private final CloudService cloudServiceMock = Mockito.mock(CloudService.class);

    private final int limit = 3;
    private final String authToken = "authTocken";
    private final String filename = "filename1";
    private final String newFilename = "newFilename1";
    private final Map<String, String> bodyParams = new HashMap<>();
    private final List<CloudFile> fileList = new ArrayList<>();

    private final CloudFile file = new CloudFile(filename, "text/plain", filename.getBytes(), 2345);
    private final long fileId = 1;
    private final Map<String, UserDetails> tokenStorage = new ConcurrentHashMap<>();

    private final String login = "Test user";
    private final String password = "Test_p@ssword123";
    private final long userId = 1l;
    private final JwtUtils JwtUtilsMock = Mockito.mock(JwtUtils.class);
    private final UserService userServiceMock = Mockito.mock(UserService.class);

    private final AuthorizationRequestDTO authRequest = new AuthorizationRequestDTO(login, password);
    private final AuthorizationRequestDTO badAuthRequest = new AuthorizationRequestDTO(null, null);
    private final AuthorizationTokenDTO authTokenDTO = new AuthorizationTokenDTO(authToken);
    private final AuthenticationManager authenticationManagerMock = Mockito.mock(AuthenticationManager.class);
    private final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(login, password);

    private final User user = new User(userId, login, password, Set.of(new Role(EnumRoles.ROLE_USER)));
    private final UserDetails userPrincipal = new UserDetails() {
        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return null;
        }

        @Override
        public String getPassword() {
            return password;
        }

        @Override
        public String getUsername() {
            return login;
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    };

    public TestModels() {
        file.setId(fileId);
    }

    public Map<String, String> getBodyParams() {
        bodyParams.put("filename", newFilename);
        return bodyParams;
    }

    public List<CloudFile> getFileList() {
        fileList.add(file);
        fileList.add(new CloudFile("filename2", "txt", "filename2".getBytes(), 3456));
        fileList.add(new CloudFile("filename3", "txt", "filename3".getBytes(), 4567));
        return fileList;
    }

    public CloudFile getEditedFile() {
        CloudFile editedFile = file;
        file.setName(newFilename);
        return file;
    }

    public String getWebAuthToken() {
        return "Bearer_" + authToken;
    }

    public UserDetails getUserPrincipal() {
        return userPrincipal;
    }

    public AuthenticationManager getAuthenticationManagerMock() {
        Authentication authenticationMock = Mockito.mock(Authentication.class);
        Mockito.when(authenticationManagerMock.authenticate(Mockito.any()))
                .thenReturn(authenticationMock);
        Mockito.when(authenticationMock.getPrincipal())
                .thenReturn(userPrincipal);
        return authenticationManagerMock;
    }

    public JwtUtils getJwtUtilsMock() {
        Mockito.when(JwtUtilsMock.generateJwtToken(any()))
                .thenReturn(authToken);
        return JwtUtilsMock;
    }

    public UserService getUserServiceMock() {
        Mockito.when(userServiceMock.loadUserByUsername(login))
                .thenReturn(userPrincipal);
        return userServiceMock;
    }

    public FileRepository getFileRepositoryMock() {
        Mockito.when(fileRepositoryMock.save(file))
                .thenReturn(file);
        Mockito.when(fileRepositoryMock.removeByUserIdAndName(userId, filename))
                .thenReturn(of(file.getId()));
        Mockito.when(fileRepositoryMock.findByUserIdAndName(userId, filename))
                .thenReturn(of(file));
        Mockito.when(fileRepositoryMock.findAllByUserIdWithLimit(userId, limit))
                .thenReturn(fileList);
        return fileRepositoryMock;
    }

    public UserRepository getUserRepositoryMock() {
        Mockito.when(userRepositoryMock.findByLogin(Mockito.anyString()))
                .thenReturn(of(user));
        Mockito.when(userRepositoryMock.findById(any()))
                .thenReturn(of(user));
        return userRepositoryMock;
    }

    public CloudService getCloudServiceMock() {
        try {
            Mockito.when(cloudServiceMock.uploadFile(authToken, filename, multipartFileMock))
                    .thenReturn(file);
            Mockito.when(cloudServiceMock.removeFile(authToken, filename))
                    .thenReturn(file.getId());
            Mockito.when(cloudServiceMock.downloadFile(authToken, filename))
                    .thenReturn(file);
            Mockito.when(cloudServiceMock.editFile(authToken, filename, newFilename))
                    .thenReturn(file);
            Mockito.when(cloudServiceMock.getFiles(authToken, limit))
                    .thenReturn(fileList);
            Mockito.when(cloudServiceMock.login(authRequest))
                    .thenReturn(authToken);
            Mockito.when(cloudServiceMock.login(badAuthRequest))
                    .thenReturn(null);
        } catch (IOException exc) {
        }
        return cloudServiceMock;
    }
}
