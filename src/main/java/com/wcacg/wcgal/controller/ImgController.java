package com.wcacg.wcgal.controller;


import com.wcacg.wcgal.entity.dto.ArticleDto;
import com.wcacg.wcgal.entity.message.ResponseMessage;
import com.wcacg.wcgal.utils.PathUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/img")
public class ImgController {

    @ResponseBody
    @RequestMapping(value  = "/{fileName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] get(@PathVariable String fileName) throws IOException {
        File file = PathUtils.ImgPathFile(fileName);
        if (!file.exists()){
            return new byte[]{0};
        }
        return new BufferedInputStream(new FileInputStream(file)).readAllBytes();
    }

    @PostMapping("/upload")
    public ResponseMessage<List<String>> upload(@RequestParam("file") MultipartFile[] files) throws IOException {
        List<String> upload = new ArrayList<>();

        for (MultipartFile file : files){
            Image image = ImageIO.read(new BufferedInputStream(file.getInputStream()));
            if (image == null){
                return ResponseMessage.dataError("不支持的图片类型", Collections.singletonList(file.getOriginalFilename()));
            }
        }

        for (MultipartFile file : files){
            File newFile = PathUtils.ImgPathFile(file.getInputStream());
            if (!newFile.exists()){
                file.transferTo(newFile);
            }
            upload.add("http://localhost:8080/img/" + newFile.getName());
        }
        return ResponseMessage.success(upload);
    }
}
