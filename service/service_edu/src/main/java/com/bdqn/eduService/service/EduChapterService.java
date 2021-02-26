package com.bdqn.eduService.service;

import com.bdqn.eduService.entity.EduChapter;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bdqn.eduService.entity.chapter.ChapterVo;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-11-15
 */
public interface EduChapterService extends IService<EduChapter> {
    //课程大纲列表，根据课程id进行查询
    List<ChapterVo> getChapterVideoByCourseId(String courseId);

    //删除章节方法
    boolean deleteChapter(String chapterId);
    //根据课程id删除章节
    void removeChapterByCourseId(String courseId);





}
