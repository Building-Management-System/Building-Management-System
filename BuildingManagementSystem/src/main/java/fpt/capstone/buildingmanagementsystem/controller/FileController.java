package fpt.capstone.buildingmanagementsystem.controller;

import fpt.capstone.buildingmanagementsystem.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin
public class FileController {

    @Autowired
    FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(MultipartFile file) {
        return fileService.store(file);
    }

    @GetMapping("files")
    public ResponseEntity<?> getAllFiles() {
        return fileService.getALlFiles();
    }

    @GetMapping("/files/{id}")
    public ResponseEntity<?> getFile(@PathVariable String id) {
        return fileService.getFile(id);
    }
}
