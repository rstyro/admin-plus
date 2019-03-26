package com.lrs.core.admin.controller;

import com.lrs.common.constant.ApiResultEnum;
import com.lrs.common.constant.ResponseModel;
import com.lrs.common.constant.Result;
import com.lrs.common.exception.ApiException;
import com.lrs.common.utils.ImgUtil;
import com.lrs.common.utils.PathsUtils;
import com.lrs.common.utils.date.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("/upload")
public class UploadController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("${admin.upload.root}")
	public String rootPath;

    @Value("${admin.upload.pre}")
    public String prePath;


	@PostMapping(value = "/image")
	@ResponseBody
	public Result imgUpload(@RequestParam(value = "file") MultipartFile file){
		if (file.isEmpty()) {
           throw new ApiException(ApiResultEnum.FILE_IS_NULL,null);
        }
        // 获取文件名
        String fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        String folder = "/"+ DateUtil.getDays()+"/";
        fileName  = System.currentTimeMillis()+suffixName;
        File dest = ImgUtil.createFile(PathsUtils.getAbsolutePath(rootPath+folder+fileName));
        try {
            file.transferTo(dest);
        }catch (IOException e) {
            logger.error(e.getMessage(),e);
            throw new ApiException(ApiResultEnum.ERROR_IO);
        }
        return Result.ok(prePath+folder+fileName);
	}
	

}
