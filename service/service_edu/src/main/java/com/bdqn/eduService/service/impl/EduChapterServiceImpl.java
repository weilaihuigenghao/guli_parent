package com.bdqn.eduService.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bdqn.eduService.entity.EduChapter;
import com.bdqn.eduService.entity.EduVideo;
import com.bdqn.eduService.entity.chapter.ChapterVo;
import com.bdqn.eduService.entity.chapter.ViderVo;
import com.bdqn.eduService.mapper.EduChapterMapper;
import com.bdqn.eduService.service.EduChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bdqn.eduService.service.EduVideoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-11-15
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    @Autowired
    private EduVideoService videoService;

    @Override
    public List<ChapterVo> getChapterVideoByCourseId(String courseId) {
        //1.根据课程id查询课程里面所有的章节
        QueryWrapper<EduChapter> wrapperChapter=new QueryWrapper<>();
        wrapperChapter.eq("course_id",courseId);
         List<EduChapter> eduChapterList = baseMapper.selectList(wrapperChapter);

         //2.根据课程id查询课程里面所有的小节
        QueryWrapper<EduVideo> wrapperVideo=new QueryWrapper<>();
        wrapperVideo.eq("course_id",courseId);
         List<EduVideo> eduVideoList = videoService.list(wrapperVideo);

         //创建List集合，封装最终数据
        List<ChapterVo> finalList=new ArrayList<>();

        //3.遍历查询章节list集合进行封装
        //遍历查询章节list集合
        for(int i=0;i<eduChapterList.size();i++){
            //每个章节
             EduChapter eduChapter = eduChapterList.get(i);
             //eduChapter复制到ChapterVo里面去
            ChapterVo chapterVo=new ChapterVo();
            BeanUtils.copyProperties(eduChapter,chapterVo);
            //把chapterVo放到最终集合里面去
            finalList.add(chapterVo);

            //创建集合，用于封装章节的小节
            List<ViderVo> videoList=new ArrayList<>();
            //4.遍历查询小节list集合，进行封装
            for (int m = 0; m < eduVideoList.size(); m++) {
                //得到每个小节
                 EduVideo eduVideo = eduVideoList.get(m);
                 //判断小节里面chapterid和章节里面的id是否一样
                if(eduVideo.getChapterId().equals(eduChapter.getId())){
                    //进行封装
                    ViderVo viderVo=new ViderVo();
                    BeanUtils.copyProperties(eduVideo,viderVo);
                    //放到小节封装集合
                    videoList.add(viderVo);

                }
            }
            //把封装之后小节的list集合，放到章节对象里面
            chapterVo.setChildren(videoList);

        }
        return finalList;
    }
    //删除章节方法
    @Override
    public boolean deleteChapter(String chapterId) {
        //根据chapterid章节id 查询小节表，如果里面有数据，就不能删除
        QueryWrapper<EduVideo>wrapper=new QueryWrapper<>();
        wrapper.eq("chapter_id",chapterId);
        int count = videoService.count(wrapper); //不需要得到数据直接用count不用list集合
        //判断
        if(count>0){
            throw new ArithmeticException("20001 不能删除");
        }
        else{//里面没有数据就删除章节
            //删除章节
             int result = baseMapper.deleteById(chapterId);
             return result>0;
        }

    }

    @Override
    public void removeChapterByCourseId(String courseId) {
        //根据课程id删除章节
        QueryWrapper<EduChapter> wrapper=new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        baseMapper.delete(wrapper);
    }
}
