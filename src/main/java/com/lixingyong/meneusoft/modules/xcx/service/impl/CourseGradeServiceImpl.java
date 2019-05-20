package com.lixingyong.meneusoft.modules.xcx.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.lixingyong.meneusoft.modules.xcx.dao.ClassroomDao;
import com.lixingyong.meneusoft.modules.xcx.dao.CourseGradeDao;
import com.lixingyong.meneusoft.modules.xcx.entity.Classroom;
import com.lixingyong.meneusoft.modules.xcx.entity.CourseGrade;
import com.lixingyong.meneusoft.modules.xcx.service.ClassroomService;
import com.lixingyong.meneusoft.modules.xcx.service.CourseGradeService;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @ClassName ContactBookServiceImpl
 * @Description TODO
 * @Author lixingyong
 * @Date 2018/12/11 12:00
 * @Version 1.0
 */
@Service("courseGradeService")
public class CourseGradeServiceImpl extends ServiceImpl<CourseGradeDao, CourseGrade> implements CourseGradeService {
    @Override
    public List<CourseGrade> getCourseGradeList(Long courseId) {
        return this.baseMapper.selectList(new EntityWrapper<CourseGrade>().eq("course_id", courseId));
    }

    @Override
    public void insertOrUpdateCourseGradeAll(List<CourseGrade> courseGradeList) {
        this.insertOrUpdateBatch(courseGradeList);
    }
}
