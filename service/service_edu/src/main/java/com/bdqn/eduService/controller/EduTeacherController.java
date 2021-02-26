package com.bdqn.eduService.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bdqn.commonutilss.R;
import com.bdqn.eduService.entity.EduTeacher;
import com.bdqn.eduService.entity.vo.TeacherQuery;
import com.bdqn.eduService.service.EduTeacherService;
import com.bdqn.servicebase.exceptionhander.bdqnException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-11-09
 */

@Api(description="讲师管理")
@RestController
@RequestMapping("/eduservice/teacher")
@CrossOrigin //解决跨域问题
public class EduTeacherController {
    @Autowired
    private EduTeacherService eduTeacherService;
    @ApiOperation(value = "所有讲师列表")
    @GetMapping("findAll")
    public R findAllTeacher(){
      List<EduTeacher> list= eduTeacherService.list(null);
      return R.ok().data("items",list);
    }
    @ApiOperation(value = "逻辑删除讲师")
    @DeleteMapping("{id}")
    public R removeTeacher(@ApiParam(name = "id", value = "讲师ID", required = true)@PathVariable String id){
       boolean flag= eduTeacherService.removeById(id);
       if (flag){
           return R.ok();
       }
       else {
           return R.error();
       }

    }
    @GetMapping("pageTeacher/{current}/{limit}")
    public R pageListTeacher(@PathVariable long current,@PathVariable long limit){
        //创建一个Page对象
        Page<EduTeacher> page=new Page<>(current,limit);

        try {
            int i=10/0;
        }catch (Exception e){
            throw new bdqnException(20001,"执行了自定义异常处理。。。");
        }


        //调用方法实现分页
        //调用方法的时候会把底层封装，把分页所有数据封装到Page对象里面
        eduTeacherService.page(page,null);

       long total= page.getTotal();//总记录数
      List<EduTeacher> records= page.getRecords();//数据List集合

        return R.ok().data("total",total).data("records",records);
    }

    //条件查询带分页的方法
    @PostMapping("pageTeacherCondition/{current}/{limit}")
    //@RequestBody 使用Json传递数据，把json数据封装到对应对象里面
   // required = false 表示参数可以没有  @RequestBody(required = false)
    public R pageTeacherCondition(@PathVariable long current, @PathVariable long limit,@RequestBody(required = false) TeacherQuery teacherQuery){
        Page<EduTeacher> pageTeacher=new Page<>(current,limit);
        //构建条件
        QueryWrapper<EduTeacher> queryWrapper=new QueryWrapper<>();


        //多条件组合查询 判断条件值是否为空，如果不为空就拼接条件
         String name = teacherQuery.getName();
         Integer level = teacherQuery.getLevel();
         String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();

        if(!StringUtils.isEmpty(name)){
            queryWrapper.like("name",name);
        }
        if(!StringUtils.isEmpty(level)){
            queryWrapper.eq("level",level);
        }
        if(!StringUtils.isEmpty(begin)){
            queryWrapper.ge("gmt_create",begin);
        }
        if(!StringUtils.isEmpty(end)){
            queryWrapper.le("gmt_create",end);
        }

        //排序
        queryWrapper.orderByDesc("gmt_create");
        //调用方法实现分页查询
        eduTeacherService.page(pageTeacher,queryWrapper);

        long total= pageTeacher.getTotal();//总记录数
        List<EduTeacher> records= pageTeacher.getRecords();//数据List集合

        return R.ok().data("total",total).data("records",records);
    }
    //添加讲师的方法
    @PostMapping("addTeacher")
    public R addTeacher(@RequestBody EduTeacher eduTeacher){
         boolean save = eduTeacherService.save(eduTeacher);
         if(save){
             return R.ok();
         }
         else{
             return R.error();
         }
    }
    //根据讲师id进行查询
    @GetMapping("getTeacher/{id}")
    public R getTeacher(@PathVariable String id){
         EduTeacher eduTeacher = eduTeacherService.getById(id);
         return R.ok().data("teacher",eduTeacher);
    }
    //讲师修改功能
    @PostMapping("updateTeacher")
    public R updateTeacher(@RequestBody EduTeacher eduTeacher){
         boolean flog = eduTeacherService.updateById(eduTeacher);
        if(flog){
            return R.ok();
        }
        else{
            return R.error();
        }
    }

}

