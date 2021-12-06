package com.example.CloudKeeper.controller;

import com.example.CloudKeeper.DTO.AuthorizationRequestDTO;
import com.example.CloudKeeper.DTO.AuthorizationTokenDTO;
import com.example.CloudKeeper.DTO.ErrorResponseDTO;
import com.example.CloudKeeper.entity.CloudFile;
import com.example.CloudKeeper.exception.ErrorInputData;
import com.example.CloudKeeper.exception.CloudException;
import com.example.CloudKeeper.exception.UnauthorizedException;
import com.example.CloudKeeper.service.CloudService;
import org.apache.log4j.Logger;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
@Validated
public class CloudController {

    private final CloudService cloudService;
    private static final Logger LOGGER = Logger.getLogger(CloudController.class);

    public CloudController(CloudService cloudService) {
        this.cloudService = cloudService;
    }

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthorizationTokenDTO> login(@Valid @RequestBody AuthorizationRequestDTO authorization) {
        String authToken = cloudService.login(authorization);
        return authToken != null ? new ResponseEntity<>(new AuthorizationTokenDTO(authToken), HttpStatus.OK) : new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping(value = "/logout")
    public HttpStatus logout(@RequestHeader("auth-token") String authToken) {
        cloudService.logout(authToken);
        return HttpStatus.OK;
    }

    @PostMapping(value = "/file", produces = MediaType.APPLICATION_JSON_VALUE, consumes = {"multipart/form-data"})
    public ResponseEntity<?> uploadFile(@RequestHeader("auth-token") String authToken, @Valid @RequestParam String filename, @RequestBody MultipartFile file) throws IOException {
        cloudService.uploadFile(authToken, filename, file);
        LOGGER.info(String.format("File %s uploaded", filename));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/file", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> removeFile(@RequestHeader("auth-token") String authToken, @Valid @RequestParam String filename) {
        cloudService.removeFile(authToken, filename);
        LOGGER.info(String.format("File %s removed", filename));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/file", produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> downloadFile(@RequestHeader("auth-token") String authToken, @Valid @RequestParam String filename) {
        CloudFile file = cloudService.downloadFile(authToken, filename);
        LOGGER.info(String.format("File %s downloaded", filename));
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .body(file.getData());
    }

    @PutMapping(value = "/file", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> editFile(@RequestHeader("auth-token") String authToken, @Valid @RequestParam String filename, @RequestBody Map<String, String> bodyParams) {
        cloudService.editFile(authToken, filename, bodyParams.get("filename"));
        LOGGER.info(String.format("File %s renamed to %s", filename, bodyParams.get("filename")));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/list")
    public List<CloudFile> getFiles(@RequestHeader("auth-token") String authToken, @RequestParam("limit") int limit) {
        List<CloudFile> fileList = cloudService.getFiles(authToken, limit);
        LOGGER.info(String.format("File list provided. Limit %d", limit));
        return fileList;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationExc(MethodArgumentNotValidException exc) {
        return handleRunTimeExc(new ErrorInputData("Error input data: " + exc.getLocalizedMessage()));
    }

    @ExceptionHandler(ErrorInputData.class)
    public ResponseEntity<ErrorResponseDTO> handleRunTimeExc(ErrorInputData exc) {
        LOGGER.error(String.format("Error %s : %s", exc.getClass().getName(), exc.getLocalizedMessage()));
        return new ResponseEntity<>(new ErrorResponseDTO(exc.getLocalizedMessage(), exc.getId()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponseDTO> handleRunTimeExc(UnauthorizedException exc) {
        LOGGER.error(String.format("Error %s : %s", exc.getClass().getName(), exc.getLocalizedMessage()));
        return new ResponseEntity<>(new ErrorResponseDTO(exc.getLocalizedMessage(), exc.getId()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(CloudException.class)
    public ResponseEntity<ErrorResponseDTO> handleRunTimeExc(CloudException exc) {
        LOGGER.error(String.format("Error %s : %s", exc.getClass().getName(), exc.getLocalizedMessage()));
        return new ResponseEntity<>(new ErrorResponseDTO(exc.getLocalizedMessage(), exc.getId()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponseDTO> handleMaxSizeException(MaxUploadSizeExceededException exc) {
        return handleRunTimeExc(new CloudException("File too large: " + exc.getLocalizedMessage()));
    }
}
