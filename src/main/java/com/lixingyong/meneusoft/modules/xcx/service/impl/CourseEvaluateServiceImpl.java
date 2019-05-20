package com.lixingyong.meneusoft.modules.xcx.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.lixingyong.meneusoft.modules.xcx.dao.ContactBookDao;
import com.lixingyong.meneusoft.modules.xcx.dao.CourseEvaluateDao;
import com.lixingyong.meneusoft.modules.xcx.entity.ContactBook;
import com.lixingyong.meneusoft.modules.xcx.entity.Course;
import com.lixingyong.meneusoft.modules.xcx.entity.CourseEvaluate;
import com.lixingyong.meneusoft.modules.xcx.entity.Wechat;
import com.lixingyong.meneusoft.modules.xcx.service.ContactBookService;
import com.lixingyong.meneusoft.modules.xcx.service.CourseEvaluateService;
import com.lixingyong.meneusoft.modules.xcx.vo.CourseEvaluateParam;
import org.springframework.stereotype.Service;

import java.util.List;

;

/**
 * @ClassName ContactBookServiceImpl
 * @Description TODO
 * @Author lixingyong
 * @Date 2018/12/11 12:00
 * @Version 1.0
 */
@Service("courseEvaluateService")
public class CourseEvaluateServiceImpl extends ServiceImpl<CourseEvaluateDao, CourseEvaluate> implements CourseEvaluateService {

    @Override
    public List<CourseEvaluate> getCourseEvaluateList(Long courseId) {
        return this.baseMapper.selectList(new EntityWrapper<CourseEvaluate>().eq("course_id", courseId));
    }

    @Override
    public CourseEvaluate getMyEvaluate(Long courseId, Long valueOf) {
        CourseEvaluate  courseEvaluate = new CourseEvaluate();
        List<CourseEvaluate> courseEvaluates =  this.baseMapper.selectList(new EntityWrapper<CourseEvaluate>().eq("course_id", courseId).eq("user_id", valueOf));
        if(courseEvaluates.isEmpty()){
            courseEvaluate.setStatus(-1);
            return courseEvaluate;
        } else {
            return courseEvaluates.get(0);
        }
    }

    @Override
    public int getCurUserCourseEvaluate(long courseId, Long valueOf) {
        return this.baseMapper.selectCount(new EntityWrapper<CourseEvaluate>().eq("course_id", courseId).eq("user_id", valueOf));
    }

    @Override
    public void addCourseEvaluate(CourseEvaluateParam courseEvaluateParam, Course course, Wechat wechat) {
        CourseEvaluate courseEvaluate = new CourseEvaluate();
        courseEvaluate.setStatus(1);
        courseEvaluate.setAvatar(wechat.getAvatarUrl());
        courseEvaluate.setComment(courseEvaluateParam.getComment());
        courseEvaluate.setCourseId(course.getId());
        courseEvaluate.setCourseName(course.getName());
        courseEvaluate.setExamType(courseEvaluateParam.getExamType());
        courseEvaluate.setLessonId(course.getLessonId());
        courseEvaluate.setNickName(wechat.getNickName());
        courseEvaluate.setType(course.getType());
        courseEvaluate.setUserId(wechat.getUserId());
        courseEvaluate.setCallName(courseEvaluateParam.getCallName());
        courseEvaluate.setTask(courseEvaluateParam.getTask());
        courseEvaluate.setAssess(courseEvaluateParam.getAssess());
        this.baseMapper.insert(courseEvaluate);
    }

    @Override
    public CourseEvaluate getMyEvaluate(int id) {
        return this.baseMapper.selectById(id);
    }

    @Override
    public void updateCourseEvaluate(CourseEvaluate courseEvaluate) {
        this.baseMapper.updateById(courseEvaluate);
    }

    @Override
    public void insertOrUpdateCourseEvaluate(List<CourseEvaluate> courseEvaluateList) {
        this.insertOrUpdateBatch(courseEvaluateList);
    }
}
