package com.bdqn.eduService.controller;


import com.bdqn.commonutilss.R;
import com.bdqn.eduService.entity.subject.OneSubject;
import com.bdqn.eduService.service.EduSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-11-15
 */
@RestController
@RequestMapping("/eduservice/subject")
@CrossOrigin
public class EduSubjectController {
    @Autowired
    private EduSubjectService subjectService;


    //添加课程分类
    //获取上传过来的文件，把文件内容读取出来
    @PostMapping("addSubject")
    public R addSubject(MultipartFile file){
        //上传过来excle文件
        subjectService.saveSubject(file,subjectService);
        return R.ok();
    }
    //课程分类列表(树形)
    @GetMapping("getAllSubject")
    public R getAllSubject(){
        //list集合泛型里面返回的一级分类
      List<OneSubject> list= subjectService.getAllOneTwoSubject();
        return R.ok().data("list",list);
    }

}

