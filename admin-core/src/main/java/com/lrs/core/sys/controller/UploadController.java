package com.lrs.core.sys.controller;

import com.lrs.common.utils.ImgUtil;
import com.lrs.common.utils.PathsUtils;
import com.lrs.common.utils.date.DateUtil;
import com.lrs.core.entity.ApiResultEnum;
import com.lrs.core.entity.ResponseModel;
import com.lrs.core.exception.ApiException;
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

	@Value("${upload.path.root}")
	public String rootPath;

    @Value("${upload.path.pre}")
    public String prePath;


	@PostMapping(value = "/image")
	@ResponseBody
	public ResponseModel imgUpload(@RequestParam(value = "file") MultipartFile file){
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
            return new ResponseModel(ApiResultEnum.ERROR_IO,null);
        }
		return new ResponseModel(prePath+folder+fileName);
	}
	

}
