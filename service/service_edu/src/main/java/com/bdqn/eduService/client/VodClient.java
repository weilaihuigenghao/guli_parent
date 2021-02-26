package com.bdqn.eduService.client;

import com.bdqn.commonutilss.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Component  //表示交给spring容器管理
@FeignClient(name = "service-vod",fallback = VodFileDegradeFeignClient.class)//调用的服务名称
public interface VodClient {
    //定义要调用的方法路径
    //根据视频id删除阿里云视频
    //这里的@PathVariable注解一定要指定参数名称，否则会出错
    @DeleteMapping("/eduvod/video/removeAlyVideo/{id}")
    public R removeAlyVideo(@PathVariable("id")  String id);


    //删除多个阿里云视频的方法
    //参数多个视频id 用List集合传递多个参数id
    @DeleteMapping("/eduvod/video/delete-batch")
    public R deleteBatch(@RequestParam("videoIdList") List<String> videoIdList);
}
