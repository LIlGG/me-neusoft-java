package com.lixingyong.meneusoft.modules.xcx.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.lixingyong.meneusoft.modules.xcx.dao.ClassroomDao;
import com.lixingyong.meneusoft.modules.xcx.dao.CourseCountDao;
import com.lixingyong.meneusoft.modules.xcx.entity.Classroom;
import com.lixingyong.meneusoft.modules.xcx.entity.CourseCount;
import com.lixingyong.meneusoft.modules.xcx.service.ClassroomService;
import com.lixingyong.meneusoft.modules.xcx.service.CourseCountService;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @ClassName ContactBookServiceImpl
 * @Description TODO
 * @Author lixingyong
 * @Date 2018/12/11 12:00
 * @Version 1.0
 */
@Service("courseCountService")
public class CourseCountServiceImpl extends ServiceImpl<CourseCountDao, CourseCount> implements CourseCountService {

    @Override
    public CourseCount getCourseCount(long courseId) {
        CourseCount courseCount = new CourseCount();
        List<CourseCount> list = this.baseMapper.selectList(new EntityWrapper<CourseCount>().eq("course_id", courseId));
        if(list.isEmpty()){
            return courseCount;
        }else {
           return list.get(0);
        }
    }

    @Override
    public void insertOrUpdateCourseCountAll(List<CourseCount> courseCountList) {
        this.insertOrUpdateBatch(courseCountList);
    }
}
