package com.fh.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.UUID;

//结合阿里云图片下载
public class FileUtil {
	
	public  static String uploadFile(File file) throws Exception {
		// Endpoint以杭州为例，其它Region请按实际情况填写。
		String endpoint = "http://oss-cn-qingdao.aliyuncs.com";
		String endpointPath="oss-cn-qingdao.aliyuncs.com";
		String accessKeyId = "LTAI4GGe1vBWLm2K2CF7DLhF";
		String accessKeySecret = "WbkUycBrZdcAH1Bi0nI46QT9Lf7MN7";
		String bucketName = "feihu-1";
		// 创建OSSClient实例。
		OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
		// 上传文件流。
		String fileName = file.getName();
		String substring = fileName.substring(fileName.lastIndexOf("."));
		String uuidFile = UUID.randomUUID().toString() + substring;
		InputStream inputStream = new FileInputStream(file);
		ossClient.putObject(bucketName, uuidFile, inputStream);
		// 关闭OSSClient。
		ossClient.shutdown();
		return "https://"+bucketName+"."+endpointPath+"/"+uuidFile;
	}

	public static File readFiles(MultipartFile file) {
		int n;
		File newFile = new File(file.getOriginalFilename());
		try (InputStream in = file.getInputStream(); OutputStream os = new FileOutputStream(newFile)) {
			byte[] buffer = new byte[4096];
			while ((n = in.read(buffer, 0, 4096)) != -1) {
				os.write(buffer, 0, n);
			}
			System.out.println("获取文件成功，暂存目录" + newFile.getAbsolutePath());
		} catch (IOException e) {
			System.out.println("获取文件失败");
		}
		return newFile;
	}


}
