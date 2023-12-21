package com.lrs.core.system.controller;

import com.lrs.common.utils.PathsUtils;
import com.lrs.core.system.config.CommonConfig;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
    public void getImg(@PathVariable("filename") String filename, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String address =  uploadConfig.getRoot()+"/"+filename;
        writeImg(response, PathsUtils.getAbsolutePath(address));
    }

    @GetMapping(value = "/{folderName}/{filename:.+}")
    public void getImg(@PathVariable("folderName") String folderName, @PathVariable("filename") String filename, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String address =  uploadConfig.getRoot()+"/"+folderName+"/"+filename;
        writeImg(response,PathsUtils.getAbsolutePath(address));
    }

    @GetMapping(value = "/{folderName}/{folderName2}/{filename:.+}")
    public void getImg(@PathVariable("folderName") String folderName, @PathVariable("folderName2") String folderName2, @PathVariable("filename") String filename, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String address =  uploadConfig.getRoot()+"/"+folderName+"/"+folderName2+"/"+filename;
        writeImg(response, PathsUtils.getAbsolutePath(address));
    }

    //获取图片的绝对地址
    public FileInputStream query_getPhotoImageBlob(String adress) {
        FileInputStream is = null;
        File filePic = new File(adress);
        try {
            is = new FileInputStream(filePic);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return is;
    }

    //输出图片
    public void writeImg(HttpServletResponse response, String address) throws IOException {
        response.setContentType("image/jpeg");
        FileInputStream is =this.query_getPhotoImageBlob(address);
        if (is != null){
            int i = is.available(); // 得到文件大小
            byte data[] = new byte[i];
            is.read(data); // 读数据
            is.close();
            response.setContentType("image/jpeg"); // 设置返回的文件类型
            OutputStream toClient = response.getOutputStream(); // 得到向客户端输出二进制数据的对象
            toClient.write(data); // 输出数据
            toClient.close();
        }
    }
}
