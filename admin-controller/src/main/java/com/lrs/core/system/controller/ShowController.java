package com.lrs.core.system.controller;

import com.lrs.core.system.config.CommonConfig;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * 上传图片展示类
 */
@Controller
@RequestMapping("/show")
public class ShowController {

    @Resource
    private CommonConfig.UploadConfig uploadConfig;


    //显示本地图片
    @GetMapping(value = "/{filename:.+}")
    public void getImg(@PathVariable("filename") String filename, HttpServletResponse response) throws IOException {
        String address = uploadConfig.getRoot() + "/" + filename;
        sendImageToResponse(response, address);
    }

    @GetMapping(value = "/{folderName}/{filename:.+}")
    public void getImg(@PathVariable("folderName") String folderName, @PathVariable("filename") String filename, HttpServletResponse response) throws IOException {
        String address = uploadConfig.getRoot() + "/" + folderName + "/" + filename;
        sendImageToResponse(response, address);
    }

    @GetMapping(value = "/{folderName}/{folderName2}/{filename:.+}")
    public void getImg(@PathVariable("folderName") String folderName, @PathVariable("folderName2") String folderName2, @PathVariable("filename") String filename, HttpServletResponse response) throws IOException {
        String address = uploadConfig.getRoot() + "/" + folderName + "/" + folderName2 + "/" + filename;
        sendImageToResponse(response, address);
    }

    // 输出图片到HTTP响应
    public void sendImageToResponse(HttpServletResponse response, String filePath) throws IOException {
        if (ObjectUtils.isEmpty(filePath)) {
            throw new IllegalArgumentException("文件路径不能为空.");
        }
        File filePic = new File(filePath);
        if (!filePic.exists()) {
            filePic = new File(System.getProperty("user.dir") + "/" + filePath);
        }
        if (!filePic.exists() || !filePic.isFile()) {
            throw new FileNotFoundException("文件不存在，路径: " + filePath);
        }
        response.setContentType(getContentType(filePath));
        try (FileInputStream fis = new FileInputStream(filePic);
             OutputStream outputStream = response.getOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (FileNotFoundException e) {
            throw new IOException("查找文件失败：err=", e);
        }
    }

    // 根据文件扩展名获取MIME类型
    private String getContentType(String imagePath) {
        String extension = FilenameUtils.getExtension(imagePath).toLowerCase();
        switch (extension) {
            case "png":
                return "image/png";
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            default:
                return "application/octet-stream";
        }
    }
}
