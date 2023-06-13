package com.lib.controller;

import com.lib.domain.ImageFile;
import com.lib.dto.ImageFileDTO;
import com.lib.dto.response.ImageSavedResponse;
import com.lib.dto.response.LibResponse;
import com.lib.dto.response.ResponseMessage;
import com.lib.service.ImageFileService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/files")
public class ImageFileController {


    private final ImageFileService imageFileService;

    public ImageFileController(ImageFileService imageFileService) {
        this.imageFileService = imageFileService;
    }


    @PostMapping("/upload")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ImageSavedResponse> uploadFile(
            @RequestParam("file") MultipartFile file){

        String imageId = imageFileService.saveImage(file);

        ImageSavedResponse response =
                new ImageSavedResponse(
                        imageId,ResponseMessage.IMAGE_SAVED_RESPONSE_MESSAGE,true);
        return ResponseEntity.ok(response);

    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> getImage(@PathVariable String id) {
        ImageFile imageFile = imageFileService.getImageById(id);

        return ResponseEntity.ok().header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment;filename=" + imageFile.getName()).
                body(imageFile.getImageData().getData());
    }

    @GetMapping("/imagefile/{imagefile}")
    public ResponseEntity<byte[]> getImageById(@PathVariable String id) {
        ImageFile imageFile = imageFileService.getImageById(id);

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.IMAGE_PNG);

        return new ResponseEntity<>(
                imageFile.getImageData().getData(),
                header,
                HttpStatus.OK);

    }
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ImageFileDTO>> getAllImages(){

        List<ImageFileDTO> imageFiles= imageFileService.findAllImages();
        return ResponseEntity.ok(imageFiles);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LibResponse> deleteImage(@PathVariable String id){
        imageFileService.removeById(id);

        LibResponse response = new LibResponse(
                ResponseMessage.IMAGE_DELETED_RESPONSE_MESSAGE,true);
        return ResponseEntity.ok(response);
    }


}
