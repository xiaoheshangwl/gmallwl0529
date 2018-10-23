package com.atguigu.gmall.manager.controller;

import com.atguigu.gmall.manager.components.FastDFSTemplate;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.csource.common.MyException;
import org.csource.fastdfs.StorageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RequestMapping("/file")
@Controller
public class FileController {

    @Autowired
    FastDFSTemplate fastDFSTemplate;
    @ResponseBody
    @RequestMapping("/upload")
    public String fileUpload(@RequestParam("file") MultipartFile file){
       if(!file.isEmpty()){
           String originalFilename = file.getOriginalFilename();
           try {
               StorageClient storageClient = fastDFSTemplate.getstorageClient();
               String ext = StringUtils.substringAfterLast(originalFilename, ".");
               try {
                   String[] strings = storageClient.upload_file(file.getBytes(), ext, null);
                   String path = fastDFSTemplate.getPath(strings);
                   return path;
               } catch (MyException e) {
                   log.error("storageClient上传文件异常{}",e);
               }
           } catch (IOException e) {
               log.error("fastDFSTemplate获取storageClient异常{}",e);
           }

       }
        return "";
    }

}
