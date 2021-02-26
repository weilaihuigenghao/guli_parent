package com.bdqn.eduService.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bdqn.eduService.entity.EduTeacher;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-11-09
 */
public interface EduTeacherService extends IService<EduTeacher> {
    //1.前台分页查询的方法
    Map<String, Object> getTeacherFrontList(Page<EduTeacher> pageTeacher);
}
