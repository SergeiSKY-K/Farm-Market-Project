package telran.java57.farmmarket.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import telran.java57.farmmarket.service.FileServiceImpl;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/files")
public class FileController {
    private final FileServiceImpl fileServiceImpl;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Map<String, String>> upload(@RequestPart("file") MultipartFile file,
                                                      @RequestParam(value = "fileName", required = false) String fileName) throws Exception {
        System.out.println(">> /files reached, file=" + (file != null ? file.getOriginalFilename() : "null"));
        String url = fileServiceImpl.upload(file, fileName);
        return ResponseEntity.ok(Map.of("url", url));
    }
}
