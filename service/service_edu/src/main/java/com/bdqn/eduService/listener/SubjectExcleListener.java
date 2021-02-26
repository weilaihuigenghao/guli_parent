package com.bdqn.eduService.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bdqn.eduService.entity.EduSubject;
import com.bdqn.eduService.entity.excel.SubjectData;
import com.bdqn.eduService.service.EduSubjectService;
import com.bdqn.servicebase.exceptionhander.bdqnException;

//监听器类
public class SubjectExcleListener extends AnalysisEventListener<SubjectData> {

    //因为SubjectExcleListener不能交给spring管理，需要自己new对象，不能注入其它对象
    public EduSubjectService subjectService;

    public SubjectExcleListener(EduSubjectService subjectService) {
        this.subjectService = subjectService;
    }
    public SubjectExcleListener() {

    }

    @Override
    public void invoke(SubjectData subjectData, AnalysisContext analysisContext) {
        /*if(subjectData==null){
            throw new bdqnException(20001,"文件为空");
        }*/
        //判断一级分类是否重复
        EduSubject existOneSubject=this.existOneSubject(subjectService,subjectData.getOneSubjectName());
        if(existOneSubject==null){//一级分类为空，可以添加
            existOneSubject=new EduSubject();
            existOneSubject.setParentId("0");
            existOneSubject.setTitle(subjectData.getOneSubjectName());
            subjectService.save(existOneSubject);

        }
        //获取一级分类id值
        String pid=existOneSubject.getId();
        //添加二级分类
       EduSubject existTwoSubject= this.existTwoSubject(subjectService,subjectData.getTwoSubjectName(),pid);
        //判断二级分类是否重复
        if(existTwoSubject==null){
            existTwoSubject=new EduSubject();
            existTwoSubject.setParentId(pid);
            existTwoSubject.setTitle(subjectData.getTwoSubjectName());//二级分类名称
            subjectService.save(existTwoSubject);
        }

    }
    //判断一级分类不能重复添加
    private EduSubject existOneSubject(EduSubjectService subjectService,String name){
        QueryWrapper wrapper=new QueryWrapper<>();
        wrapper.eq("title",name);
        wrapper.eq("parent_id","0");
        EduSubject oneSubject = subjectService.getOne(wrapper);
        return oneSubject;
    }
    //判断二级分类不能重复添加
    private EduSubject existTwoSubject(EduSubjectService subjectService,String name,String pid){
        QueryWrapper wrapper=new QueryWrapper<>();
        wrapper.eq("title",name);
        wrapper.eq("parent_id",pid);
        EduSubject twoSubject = subjectService.getOne(wrapper);
        return twoSubject;
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
