package com.bdqn.eduService.controller;


import com.bdqn.commonutilss.R;
import com.bdqn.eduService.client.VodClient;
import com.bdqn.eduService.entity.EduVideo;
import com.bdqn.eduService.service.EduVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-11-15
 */
@RestController
@RequestMapping("/eduservice/video")
@CrossOrigin
public class EduVideoController {
    @Autowired
    private EduVideoService videoService;

    @Autowired
    private VodClient vodClient;

    //添加小节
    @PostMapping("addVideo")
    public R addVideo(@RequestBody EduVideo eduVideo){
        videoService.save(eduVideo);
        return R.ok();
    }
    //删除小节，删除对应的阿里云视频
    @DeleteMapping("{id}")
    public R deleteVideo(@PathVariable String id){
        //根据小节id获取视频id
         EduVideo eduVideo = videoService.getById(id);
         String videoSourceId = eduVideo.getVideoSourceId();
         //判断小节里面是否有视频id
        if(!StringUtils.isEmpty(videoSourceId)){
            //根据视频id远程调用实现视频删除
            R result = vodClient.removeAlyVideo(videoSourceId);
            if(result.getCode()==20001){
                throw new ArithmeticException("删除视频失败，熔断器。。");
            }
        }

        videoService.removeById(id);
        return R.ok();
    }
    //修改小节
}

