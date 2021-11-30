package com.example.CloudKeeper.repository;

import com.example.CloudKeeper.entity.CloudFile;
import com.example.CloudKeeper.exception.UnauthorizedException;
import com.example.CloudKeeper.service.CloudService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Optional.ofNullable;

@Repository
public class CloudRepository {

    private final FileRepository fileRepository;
    private final UserRepository userRepository;
    private final Logger logger = Logger.getLogger(CloudService.class);

    @Autowired
    public CloudRepository(UserRepository userRepository,
                           FileRepository fileRepository) {
        this.userRepository = userRepository;
        this.fileRepository = fileRepository;
    }

    public Map<String, UserDetails> tokenStorage = new ConcurrentHashMap<>();

    public void login(String authToken, UserDetails userPrincipal) {
        tokenStorage.put(authToken, userPrincipal);
    }

    public Optional<UserDetails> logout(String authToken) {
        return ofNullable(tokenStorage.remove(authToken));
    }

    public Optional<CloudFile> uploadFile(CloudFile cloudFile, String authToken) {
        Optional<Long> userId = getUserId(authToken);
        if (userId.isPresent()) {
            logger.info(String.format("User defined: login %s, id %d", userRepository.findById(userId.get()).get().getLogin(), userId.get()));
            cloudFile.setUserId(userId.get());
            return Optional.of(fileRepository.save(cloudFile));
        } else {
            throw new UnauthorizedException("Invalid auth-token");
        }
    }

    @Transactional
    public Optional<Long> removeFile(String authToken, String fileName) {
        Optional<Long> userId = getUserId(authToken);
        if (userId.isPresent()) {
            logger.info(String.format("User defined: login %s, id %d", userRepository.findById(userId.get()).get().getLogin(), userId.get()));
            return fileRepository.removeByUserIdAndName(userId.get(), fileName);
        } else {
            throw new UnauthorizedException("Invalid auth-token");
        }
    }

    @Transactional
    public Optional<CloudFile> downloadFile(String authToken, String fileName) {
        Optional<Long> userId = getUserId(authToken);
        if (userId.isPresent()) {
            logger.info(String.format("User defined: login %s, id %d", userRepository.findById(userId.get()).get().getLogin(), userId.get()));
            return fileRepository.findByUserIdAndName(userId.get(), fileName);
        } else {
            throw new UnauthorizedException("Invalid auth-token");
        }
    }

    @Transactional
    public Optional<CloudFile> editFile(String authToken, String fileName, String newFileName) {
        Optional<Long> userId = getUserId(authToken);
        if (userId.isPresent()) {
            logger.info(String.format("User defined: login %s, id %d", userRepository.findById(userId.get()).get().getLogin(), userId.get()));
            Optional<CloudFile> cloudFile = fileRepository.findByUserIdAndName(userId.get(), fileName);
            cloudFile.ifPresent(file -> file.setName(newFileName));
            return Optional.of(fileRepository.save(cloudFile.get()));
        } else {
            throw new UnauthorizedException("Invalid auth-token");
        }
    }

    @Transactional
    public Optional<List<CloudFile>> getFiles(String authToken, int limit) {
        Optional<Long> userId = getUserId(authToken);
        if (userId.isPresent()) {
            logger.info(String.format("User defined: login %s, id %d", userRepository.findById(userId.get()).get().getLogin(), userId.get()));
            return ofNullable(fileRepository.findAllByUserIdWithLimit(userId.get(), limit));
        } else {
            throw new UnauthorizedException("Invalid auth-token");
        }
    }

    private Optional<Long> getUserId(String authToken) {
        UserDetails user = tokenStorage.get(authToken.substring(7));
        return user != null ? ofNullable(userRepository.findByLogin(user.getUsername()).get().getId()) : Optional.empty();
    }
}
