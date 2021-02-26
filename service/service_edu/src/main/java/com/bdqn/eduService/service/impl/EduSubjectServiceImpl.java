package com.bdqn.eduService.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bdqn.eduService.entity.EduSubject;
import com.bdqn.eduService.entity.excel.SubjectData;
import com.bdqn.eduService.entity.subject.OneSubject;
import com.bdqn.eduService.entity.subject.TwoSubject;
import com.bdqn.eduService.listener.SubjectExcleListener;
import com.bdqn.eduService.mapper.EduSubjectMapper;
import com.bdqn.eduService.service.EduSubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-11-15
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    @Override
    public void saveSubject(MultipartFile file,EduSubjectService subjectService) {
        try {
            //文件输入流
             InputStream inputStream = file.getInputStream();
             //调用方法进行读取
            EasyExcel.read(inputStream,SubjectData.class,new SubjectExcleListener(subjectService)).sheet().doRead();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    //课程分类列表(树形)
    @Override
    public List<OneSubject> getAllOneTwoSubject() {
        //查询所有一级分类 parent_id=0
        QueryWrapper wrapperOne=new QueryWrapper<>();
        wrapperOne.eq("parent_id","0");
         List<EduSubject> oneSubjectList = baseMapper.selectList(wrapperOne);

        //查询所有二级分类 parent_id=!0
        QueryWrapper wrapperTwo=new QueryWrapper<>();
        wrapperOne.ne("parent_id","0");
        List<EduSubject> TwoSubjectList = baseMapper.selectList(wrapperTwo);

        //创建List集合，用于存储最终封装数据
        List<OneSubject>findSubjectList=new ArrayList<>();

        //封装一级分类
        //查询出来的所有一级分类list集合遍历，得到每个一个分类对象，获取每个一个分类对象值，封装到要求的List集合里面  List<OneSubject>findSubjectList
        for (int i=0; i<oneSubjectList.size();i++){ //遍历oneSubjectList集合
            //得到oneSubjectList集合的每一个对象
             EduSubject eduSubject = oneSubjectList.get(i);
             //把eduSubject里面的值直接获取出来，放到OneSubject集合里面

            OneSubject oneSubject=new OneSubject();
            /*oneSubject.setId(eduSubject.getId());
            oneSubject.setTitle(eduSubject.getTitle());*/
            BeanUtils.copyProperties(eduSubject,oneSubject);
            //多个OneSubject对象放到oneSubjectList集合里面
            findSubjectList.add(oneSubject);

            //在一级分类循环遍历查询所有二级分类
            //创建List集合封装每个一级分类的二级分类
            List<TwoSubject>twoFinalSubject=new ArrayList<>();
            //遍历二级分类List集合
            for (int m = 0; m < TwoSubjectList.size(); m++) {
                //获取每个二级分类
                 EduSubject tSubject = TwoSubjectList.get(m);
                 //判断二级分类parent_id和一级分类id是否一样
                if(tSubject.getParentId().equals(eduSubject.getId())){
                    //把tSubject1的值复制到TwoSubject里面，放到twoFinalSubject里面
                    TwoSubject twoSubject=new TwoSubject();
                    BeanUtils.copyProperties(tSubject,twoSubject);
                    twoFinalSubject.add(twoSubject);
                }
            }
                //把一级分类下面所有的二级分类再次放到一级分类里面
            oneSubject.setChildren(twoFinalSubject);
        }



        return findSubjectList;
    }
}
