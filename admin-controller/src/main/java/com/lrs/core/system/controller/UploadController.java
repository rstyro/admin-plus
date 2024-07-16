package com.lrs.core.system.controller;

import com.lrs.common.constant.ApiResultEnum;
import com.lrs.common.exception.ApiException;
import com.lrs.common.utils.ImgUtil;
import com.lrs.common.utils.date.DateUtil;
import com.lrs.common.vo.R;
import com.lrs.core.system.config.CommonConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/upload")
public class UploadController {

    @Resource
    private CommonConfig.UploadConfig uploadConfig;

    /**
     * 上传图片
     * @param file 文件
     */
    @PostMapping(value = "/image")
    public R<String> imgUpload(@RequestParam(value = "file") MultipartFile file){
        if (file.isEmpty()) {
            throw new ApiException(ApiResultEnum.ERROR_IO,null);
        }
        // 获取文件名
        String fileName = file.getOriginalFilename();
        String suffixName = Objects.requireNonNull(fileName).substring(fileName.lastIndexOf("."));
        String folder = "/"+ DateUtil.getDays()+"/";
        fileName  = System.currentTimeMillis()+suffixName;
        File dest = ImgUtil.createFile(uploadConfig.getRoot()+folder+fileName);
        try {
            file.transferTo(Objects.requireNonNull(dest));
        }catch (IOException e) {
            log.error(e.getMessage(),e);
            throw new ApiException(ApiResultEnum.ERROR_IO);
        }
        return R.ok("/show"+folder+fileName);
    }

}
