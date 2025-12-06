package telran.java57.farmmarket.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import telran.java57.farmmarket.dto.FileUploadResponse;
import telran.java57.farmmarket.service.FileServiceImpl;

import java.util.Map;





@RestController
@RequiredArgsConstructor
@RequestMapping("/files")
public class FileController {

    private final FileServiceImpl fileService;

    @PreAuthorize("hasAnyRole('ADMINISTRATOR','SUPPLIER')")
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<FileUploadResponse> upload(
            @RequestPart("file") MultipartFile file,
            @RequestParam(value = "fileName", required = false) String fileName
    ) throws Exception {

        // базовая валидация до сервиса (быстрее вернуть 4xx)
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body(FileUploadResponse.error("Empty file"));
        }
        if (file.getSize() > 10 * 1024 * 1024) { // дубль защиты к spring.servlet.multipart.max-file-size
            return ResponseEntity.status(413).body(FileUploadResponse.error("File too large"));
        }

        var result = fileService.uploadValidated(file, fileName);
        return ResponseEntity.ok(result);
    }
}
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/files")
//public class FileController {
//    private final FileServiceImpl fileServiceImpl;
//
//    @PostMapping(consumes = "multipart/form-data")
//    public ResponseEntity<Map<String, String>> upload(@RequestPart("file") MultipartFile file,
//                                                      @RequestParam(value = "fileName", required = false) String fileName) throws Exception {
//        System.out.println(">> /files reached, file=" + (file != null ? file.getOriginalFilename() : "null"));
//        String url = fileServiceImpl.upload(file, fileName);
//        return ResponseEntity.ok(Map.of("url", url));
//    }
//}
