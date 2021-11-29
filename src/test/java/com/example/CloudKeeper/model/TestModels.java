package com.example.CloudKeeper.model;

import com.example.CloudKeeper.entity.CloudFile;
import com.example.CloudKeeper.entity.Role;
import com.example.CloudKeeper.entity.User;
import lombok.Data;
import org.mockito.Mockito;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class TestModels {

    private final MultipartFile multipartFileMock = Mockito.mock(MultipartFile.class);

    private final int limit = 3;
    private final String authToken = "authTocken";
    private final String filename = "filename1";
    private final  String newFilename = "newFilename1";
    private final Map<String, String> bodyParams = new HashMap<>();
    private final List<CloudFile> fileList = new ArrayList<>();

    private final CloudFile file = new CloudFile(filename, "text/plain", filename.getBytes() , 2345);
    private final long fileId = 1;
    private final Map<String, UserDetails> tokenStorage = new ConcurrentHashMap<>();

    private final String login = "Test user";
    private final String password = "Test password";
    private final long userId = 1l;

    private final User user = new User(userId, login,password, Set.of(new Role(EnumRoles.ROLE_USER)));
    private final UserDetails userPrincipal =  new UserDetails() {
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
        fileList.add(new CloudFile("filename2", "txt", "filename2".getBytes() , 3456));
        fileList.add(new CloudFile("filename3", "txt", "filename3".getBytes() , 4567));
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
}
