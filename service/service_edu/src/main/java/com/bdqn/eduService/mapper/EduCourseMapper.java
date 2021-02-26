package com.bdqn.eduService.mapper;

import com.bdqn.eduService.entity.EduCourse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bdqn.eduService.entity.frontvo.CourseWebVo;
import com.bdqn.eduService.entity.vo.CoursePublishVo;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author testjava
 * @since 2020-11-15
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {
    public CoursePublishVo getCoursePublishVo(String courseId);

    //根据课程id编写sql语句查询课程信息
    CourseWebVo getBaseCourseInfo(String courseId);
}
