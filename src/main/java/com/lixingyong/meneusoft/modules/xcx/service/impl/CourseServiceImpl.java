package com.lixingyong.meneusoft.modules.xcx.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.lixingyong.meneusoft.common.exception.WSExcetpion;
import com.lixingyong.meneusoft.modules.xcx.dao.ClassroomDao;
import com.lixingyong.meneusoft.modules.xcx.dao.CourseDao;
import com.lixingyong.meneusoft.modules.xcx.entity.Classroom;
import com.lixingyong.meneusoft.modules.xcx.entity.Course;
import com.lixingyong.meneusoft.modules.xcx.service.ClassroomService;
import com.lixingyong.meneusoft.modules.xcx.service.CourseService;
import com.lixingyong.meneusoft.modules.xcx.vo.CourseParam;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;


/**
 * @ClassName CourseServiceImpl
 * @Description TODO
 * @Author lixingyong
 * @Date 2018/12/11 12:00
 * @Version 1.0
 */
@Service("courseService")
public class CourseServiceImpl extends ServiceImpl<CourseDao, Course> implements CourseService {

    @Override
    public List<Course> getCourseList(CourseParam courseParam) throws Exception {
        Page<Course> pages = new Page<>(courseParam.getPage(), courseParam.getPage_size());
        EntityWrapper<Course> ew = new EntityWrapper<>();
        if(!courseParam.getClassHour().isEmpty() && Integer.valueOf(courseParam.getClassHour()) > 0){
            ew.eq("class_hour", courseParam.getClassHour());
        }
        if(!courseParam.getCredit().isEmpty() && Long.valueOf(courseParam.getCredit()) > 0){
            ew.eq("credit", courseParam.getCredit());
        }
        ew.like("exam_type", courseParam.getExamType());
        ew.like("type" , courseParam.getType());
        ew.like("college", courseParam.getCollege());

        String[] order = courseParam.getOrder().split("-");
        if(order[1].equals("desc")){
            ew.orderBy(order[0], false);
        } else{
            ew.orderBy(order[0], true);
        }
        return this.baseMapper.selectPage(pages, ew);
    }

    @Override
    public List<Course> getCourseList(HashMap<String, String> params) {
        Page<Course> pages = new Page<>(Integer.valueOf(params.get("page")),Integer.valueOf(params.get("page_size")));
        EntityWrapper<Course> ew = new EntityWrapper<>();
        String[] order = params.get("order").split("-");
        if(order[1].equals("desc")){
            ew.orderBy(order[0], false);
        } else{
            ew.orderBy(order[0], true);
        }
        ew.like("name", params.get("name"));
        return this.baseMapper.selectPage(pages,ew);
    }

    @Override
    public Course getCourseList(long courseId) {
        return this.baseMapper.selectById(courseId);
    }
}
