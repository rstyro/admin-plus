package com.lrs.common.utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ImgUtil {
	
	
	 /**
	 * 上传文件
	 * @param filePath 文件名 
	 * @param in   io流
	 * @return  返回最终的路径
	 * @throws IOException 
	 */
	public static String uploadImg(String rootPATH,String filePath,InputStream in) throws IOException{
		System.out.println("rootPATH="+rootPATH);
		System.out.println("filePath="+filePath);
		String rusultPath = rootPATH+filePath;
		createFile(rusultPath);
		File realFile =new File(rusultPath);
		FileUtils.copyInputStreamToFile(in, realFile);
		return "/upload/show"+filePath;
	}
	
	
	 /**
     * 文件或文件夹不存在则创建
     * @param dir 文件夹
     * @param filepath 文件名
     */
	public static void createFile(String dir,String filepath){
		File file = new File(dir);
		if(!file.exists()){
			file.mkdirs();
		}
		file = new File(dir+filepath);
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				System.out.println("文件创建失败");
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 创建文件，如果文件夹不存在将被创建
	 * 
	 * @param destFileName
	 *            文件路径
	 */
	public static File createFile(String destFileName) {
		File file = new File(destFileName);
		if (file.exists())
			return null;
		if (destFileName.endsWith(File.separator))
			return null;
		if (!file.getParentFile().exists()) {
			if (!file.getParentFile().mkdirs())
				return null;
		}
		try {
			if (file.createNewFile())
				return file;
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
