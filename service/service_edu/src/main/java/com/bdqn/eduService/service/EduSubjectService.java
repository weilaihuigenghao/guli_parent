package com.bdqn.eduService.service;

import com.bdqn.eduService.entity.EduSubject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bdqn.eduService.entity.subject.OneSubject;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-11-15
 */

public interface EduSubjectService extends IService<EduSubject> {

    void saveSubject(MultipartFile file,EduSubjectService subjectService);

    List<OneSubject> getAllOneTwoSubject();
}
