package com.lixingyong.meneusoft.modules.xcx.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.lixingyong.meneusoft.modules.xcx.dao.CourseScheduleDao;
import com.lixingyong.meneusoft.modules.xcx.entity.Course;
import com.lixingyong.meneusoft.modules.xcx.entity.CourseSchedule;
import com.lixingyong.meneusoft.modules.xcx.service.CourseScheduleService;
import com.lixingyong.meneusoft.modules.xcx.utils.Week;
import com.lixingyong.meneusoft.modules.xcx.vo.CourseParam;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;


/**
 * @ClassName CourseServiceImpl
 * @Description TODO
 * @Author lixingyong
 * @Date 2018/12/11 12:00
 * @Version 1.0
 */
@Service("courseScheduleService")
public class CourseScheduleServiceImpl extends ServiceImpl<CourseScheduleDao, CourseSchedule> implements CourseScheduleService {

    @Override
    public List<CourseSchedule> getCourseList(String classroomName, int id, int weeks, String day) {
        EntityWrapper<CourseSchedule> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("classroom_name", classroomName);
        entityWrapper.eq("day", day);
        entityWrapper.eq("term_id", id);
        entityWrapper.like("all_week", String.valueOf(weeks));
        return  this.baseMapper.selectList(entityWrapper);
    }

    @Override
    public List<CourseSchedule> getCourseList(long id, CourseParam courseParam) {
        EntityWrapper<CourseSchedule> ew = new EntityWrapper<>();
        ew.eq("course_id", id);
        if(!courseParam.getDay().isEmpty() && Integer.valueOf(courseParam.getDay()) > 0){
            ew.like("day", Week.getWeekName(Integer.valueOf(courseParam.getDay())));
        }
        if(!courseParam.getSession().isEmpty() && Integer.valueOf(courseParam.getSession()) > 0){
            ew.like("session", String.valueOf(courseParam.getSession()));
        }
        return this.baseMapper.selectList(ew);
    }

    @Override
    public List<CourseSchedule> getCourseScheduleList(HashMap<String, String> params) {
        EntityWrapper<CourseSchedule> ew = new EntityWrapper<>();
        ew.like("teacher_name", params.get("teacher_name"));
        return this.baseMapper.selectList(ew);
    }

    @Override
    public List<CourseSchedule> getSearchCourseList(long id, HashMap<String, String> params) {
        EntityWrapper<CourseSchedule> wrapper = new EntityWrapper<>();
        if(params.containsKey("teacher_name")){
            wrapper.like("teacher_name", params.get("teacher_name"));
        }
        wrapper.eq("course_id", id);
        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public List<CourseSchedule> getCourseList(Long courseId) {
        return this.baseMapper.selectList(new EntityWrapper<CourseSchedule>().eq("course_id",courseId));
    }
}
