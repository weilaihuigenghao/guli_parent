package com.bdqn.vod.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.bdqn.commonutilss.R;
import com.bdqn.vod.service.VodService;
import com.bdqn.vod.utils.ConstanVideoUtils;
import com.bdqn.vod.utils.InitVodCilent;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class VodServiceImpl implements VodService {

    @Override
    //上传视频到阿里云
    public String uploadVideoAly(MultipartFile file) {
        try {
            //accessKeyId, accessKeySecret
            //fileName：上传文件原始名称
            // 01.03.09.mp4
            String fileName = file.getOriginalFilename();
            //title：上传之后显示名称
            String title = fileName.substring(0, fileName.lastIndexOf("."));
            //inputStream：上传文件输入流
            InputStream inputStream = file.getInputStream();
            UploadStreamRequest request = new UploadStreamRequest(ConstanVideoUtils.ACCESS_KEY_ID,ConstanVideoUtils.ACCESS_KEY_SECRET, title, fileName, inputStream);

            UploadVideoImpl uploader = new UploadVideoImpl();
            UploadStreamResponse response = uploader.uploadStream(request);

            String videoId = null;
            if (response.isSuccess()) {
                videoId = response.getVideoId();
            } else { //如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因
                videoId = response.getVideoId();
            }
            return videoId;
        }catch(Exception e) {
            e.printStackTrace();
            return null;
        }

    }
    //删除多个阿里云视频的方法
    @Override
    public void removeMoreAlyVideo(List videoIdList) {
        try {
            //初始化对象
            DefaultAcsClient client = InitVodCilent.initVodClient(ConstanVideoUtils.ACCESS_KEY_ID, ConstanVideoUtils.ACCESS_KEY_SECRET);
            //创建删除视频request对象
            DeleteVideoRequest request=new DeleteVideoRequest();

            //把集合参数用逗号分隔输出
            String videpIds = StringUtils.join(videoIdList.toArray(), ",");

            //向request设置视频id
            request.setVideoIds(videpIds);
            //调用初始化对象的方法实现删除
            client.getAcsResponse(request);

        }catch (Exception e){
            e.printStackTrace();
            throw new ArithmeticException("删除视频失败");
        }
    }

    public static void main(String[] args) {
        List<String> list=new ArrayList<>();
        list.add("11");
        list.add("22");
        list.add("33");
        String join = StringUtils.join(list.toArray(), ",");
        System.out.println(join);
    }
}
