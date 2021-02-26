package com.bdqn.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.bdqn.oss.service.OssService;
import com.bdqn.oss.utils.ConstantPropertiesUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService {
    @Override
    public String uploadFileAvatar(MultipartFile file) {
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = ConstantPropertiesUtils.END_POIND;

        String accessKeyId = ConstantPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtils.ACCESS_KEY_SECRET;
        String bucketName=ConstantPropertiesUtils.BUCKET_NAME;

        try{
            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

            // 获取上传文件输入流。
            InputStream inputStream =file.getInputStream();
            //获取文件名称
             String fileName = file.getOriginalFilename();

             //在文件名称里面获取唯一随机的值
             String uuid = UUID.randomUUID().toString().replaceAll("-", "");

            fileName= uuid+fileName;

            //把文件按照日期进行分类
            //获取当前日期
             String dataPath = new DateTime().toString("yyyy/MM/dd");

             //拼接文件格式
            fileName= dataPath+"/"+fileName;
            //调用OSS方法实现上传
            //第一个参数 Bucket名称
            //第二个参数 上传到OSS文件路径和文件名称
            //第三个参数  上传文件输入流
            ossClient.putObject(bucketName, fileName, inputStream);

            // 关闭OSSClient。
            ossClient.shutdown();
            //把上传之后文件路径返回

          //  https://edu-bdqn-0712.oss-cn-beijing.aliyuncs.com/k.jfif

            String url="https://"+bucketName+"."+endpoint+"/"+fileName;
            return url;

        }catch(Exception e){
            e.printStackTrace();
            return null;
        }



    }
}
