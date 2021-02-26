package com.bdqn.eduService.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bdqn.eduService.entity.EduCourse;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bdqn.eduService.entity.EduTeacher;
import com.bdqn.eduService.entity.frontvo.CourseFrontVo;
import com.bdqn.eduService.entity.frontvo.CourseWebVo;
import com.bdqn.eduService.entity.vo.CourseInfoVo;
import com.bdqn.eduService.entity.vo.CoursePublishVo;

import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-11-15
 */
public interface EduCourseService extends IService<EduCourse> {

    String saveCourseInfo(CourseInfoVo courseInfoVo);

    //根据课程id，查询课程基本信息
    CourseInfoVo getCourseInfo(String courseId);

    //修改课程信息
    void updateCourseInfo(CourseInfoVo courseInfoVo);
    //根据课程id查询课程确认信息
    CoursePublishVo publishCourseInfo(String id);
    //删除课程
    void removeCourse(String courseId);

    //1.条件查询带分页查询课程
    Map<String, Object> getCourseFrontList(Page<EduCourse> pageCourse, CourseFrontVo courseFrontVo);

    //根据课程id编写sql语句查询课程信息
    CourseWebVo getBaseCourseInfo(String courseId);
}
