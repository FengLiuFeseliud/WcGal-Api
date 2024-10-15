package com.wcacg.wcgal.controller;


import com.wcacg.wcgal.annotation.NeedToken;
import com.wcacg.wcgal.entity.message.ResponseMessage;
import com.wcacg.wcgal.utils.PathUtils;
import jakarta.servlet.http.HttpServletResponse;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/img")
public class ImgController {

    private byte[] getImgByte(String fileName, int width, int height, float scale, float quality, HttpServletResponse response) throws IOException {
        File file = PathUtils.ImgPathFile(fileName);
        if (!file.exists()){
            return new byte[]{0};
        }

        if (scale > 1){
            scale = 1.0f;
        }

        if ((width == 0 || height == 0) && scale == 0 && quality == 0) {
            return new BufferedInputStream(new FileInputStream(file)).readAllBytes();
        }

        String[] fileStr = fileName.split("\\.");
        if (fileStr.length == 1) {
            return new BufferedInputStream(new FileInputStream(file)).readAllBytes();
        }

        File newFile = PathUtils.ImgPathFile(fileStr[0] + "_" + width + "x" + height + "_" + scale + "_" + quality + "." + fileStr[1]);
        if (newFile.exists()){
            return new BufferedInputStream(new FileInputStream(newFile)).readAllBytes();
        }

        Thumbnails.Builder<File> builder = Thumbnails.of(file);
        if (width != 0 && height != 0){
            builder.size(width, height);
        } else if (scale != 0){
            builder.scale(scale);
        } else {
            return new BufferedInputStream(new FileInputStream(file)).readAllBytes();
        }

        if (quality != 0){
            builder.outputQuality(quality);
        }

        builder.toFile(newFile);
        return new BufferedInputStream(new FileInputStream(newFile)).readAllBytes();
    }

    private ResponseMessage<List<String>> upload(MultipartFile[] files, String subPath) throws IOException {
        List<String> upload = new ArrayList<>();

        for (MultipartFile file : files){
            Image image = ImageIO.read(new BufferedInputStream(file.getInputStream()));
            if (image == null){
                return ResponseMessage.dataError("不支持的图片类型", Collections.singletonList(file.getOriginalFilename()));
            }
        }

        for (MultipartFile file : files){
            File newFile = PathUtils.ImgPathFile(file.getInputStream(), subPath);
            if (!newFile.exists()){
                file.transferTo(newFile);
            }

            upload.add("/img/" + subPath + newFile.getName());
        }
        return ResponseMessage.success(upload);
    }

    /**
     * 获取图片, 带缩放
     * @param fileName 文件名
     * @param width 图片宽度
     * @param height 图片高度
     * @param scale 图片缩放比例
     * @param quality 图片质量
     * @param response 响应头
     * @return 图片字节
     * @throws IOException IO 错误
     */
    @ResponseBody
    @RequestMapping(value  = "/{fileName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] get(@PathVariable String fileName,
                      @RequestParam(value = "width", required = false, defaultValue = "0") int width,
                      @RequestParam(value = "height", required = false, defaultValue = "0") int height,
                      @RequestParam(value = "scale", required = false, defaultValue = "0") float scale,
                      @RequestParam(value = "quality", required = false, defaultValue = "0") float quality,
                      HttpServletResponse response) throws IOException {
        return this.getImgByte(fileName, width, height, scale, quality, response);
    }

    /**
     * 上传图片
     * @param files 图片数组
     * @return 图片路径
     * @throws IOException IO 错误
     */
    @NeedToken
    @PostMapping("/upload")
    public ResponseMessage<List<String>> upload(@RequestParam("file") MultipartFile[] files) throws IOException {
        return this.upload(files, "");
    }

    /**
     * 获取头像图片, 带缩放
     * @param fileName 文件名
     * @param width 图片宽度
     * @param height 图片高度
     * @param scale 图片缩放比例
     * @param quality 图片质量
     * @param response 响应头
     * @return 图片字节
     * @throws IOException IO 错误
     */
    @ResponseBody
    @RequestMapping(value  = "/head/{fileName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getHead(@PathVariable String fileName,
                      @RequestParam(value = "width", required = false, defaultValue = "0") int width,
                      @RequestParam(value = "height", required = false, defaultValue = "0") int height,
                      @RequestParam(value = "scale", required = false, defaultValue = "0") float scale,
                      @RequestParam(value = "quality", required = false, defaultValue = "0") float quality,
                      HttpServletResponse response) throws IOException {
        return this.getImgByte("/head/" + fileName, width, height, scale, quality, response);
    }

    /**
     * 上传头像图片
     * @param files 图片数组
     * @return 图片路径
     * @throws IOException IO 错误
     */
    @NeedToken
    @PostMapping("/head/upload")
    public ResponseMessage<List<String>> uploadHead(@RequestParam("file") MultipartFile[] files) throws IOException {
        return this.upload(files, "/head/");
    }
}
