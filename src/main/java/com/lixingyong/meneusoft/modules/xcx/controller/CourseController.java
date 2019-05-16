package com.lixingyong.meneusoft.modules.xcx.controller;

import com.lixingyong.meneusoft.common.utils.R;
import com.lixingyong.meneusoft.common.utils.SetUtil;
import com.lixingyong.meneusoft.modules.xcx.entity.Course;
import com.lixingyong.meneusoft.modules.xcx.entity.CourseSchedule;
import com.lixingyong.meneusoft.modules.xcx.service.CourseScheduleService;
import com.lixingyong.meneusoft.modules.xcx.service.CourseService;
import com.lixingyong.meneusoft.modules.xcx.vo.CourseListVO;
import com.lixingyong.meneusoft.modules.xcx.vo.CourseParam;
import io.swagger.annotations.Api;
import lombok.Data;
import org.apache.commons.collections.SetUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Api("寻课功能")
@RestController
@RequestMapping("/course")
public class CourseController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private CourseScheduleService courseScheduleService;
    @RequestMapping("/all")
    public R getCourseList(CourseParam courseParam){
        List<CourseListVO> courseListVOS = new LinkedList<>();
        try {
            List<Course> courseList = courseService.getCourseList(courseParam);
            if(courseList.isEmpty()){
                return R.ok(courseListVOS);
            }
            for(Course course : courseList){
                List<CourseSchedule> courseSchedules = courseScheduleService.getCourseList(course.getId(), courseParam);
                getCourseListVO(courseListVOS, course, courseSchedules);
            }
            return R.ok(courseListVOS);
        }catch (Exception e){
            return R.error("参数不正确，请检查");
        }
    }

    @PostMapping("search")
    public R searchCourse(@RequestParam HashMap<String, String> params){
        int page = Integer.valueOf(params.get("page"));
        int pageSize =  Integer.valueOf(params.get("page_size"));
        List<CourseListVO> courseListVOS = new LinkedList<>();
        List<Course> courses = new ArrayList<>();
        if(!params.containsKey("teacher_name") && params.containsKey("name")){
            courses = courseService.getCourseList(params);
        } else {
            List<CourseSchedule> courseSchedules = courseScheduleService.getCourseScheduleList(params);
            if(courseSchedules.isEmpty()){
                return R.ok(courseListVOS);
            }
            SortedSet <Long> ids = new TreeSet<>();
            for(CourseSchedule cs : courseSchedules){
                ids.add(cs.getCourseId());
            }
            List <Long> list = new LinkedList<>(ids);
            int min = (page - 1) * pageSize;
            int max = page * pageSize;
            for(int i = min; i < list.size() && i < max; i++){
                courses.add(courseService.getCourseList(list.get(i)));
            }
        }
        if(courses.isEmpty()){
            return R.ok(courseListVOS);
        }
        for(Course course : courses){
            List<CourseSchedule> courseSchedules = courseScheduleService.getSearchCourseList(course.getId(), params);
            getCourseListVO(courseListVOS, course, courseSchedules);
        }
        return R.ok(courseListVOS);
    }

    private void getCourseListVO(List<CourseListVO> courseListVOS, Course course, List<CourseSchedule> courseSchedules) {
        if(courseSchedules.isEmpty()){
            return;
        }
        CourseListVO courseListVO = new CourseListVO();
        courseListVO.setId(course.getId());
        courseListVO.setExamType(course.getExamType());
        courseListVO.setType(course.getType());
        courseListVO.setClassHour(course.getClassHour());
        courseListVO.setName(course.getName());
        courseListVO.setCollege(course.getCollege());
        courseListVO.setCredit(course.getCredit());
        courseListVO.setLessonId(course.getLessonId());
        Set<String> teacher = new HashSet<>();
        SortedSet<Integer> allWeek = new TreeSet<>();
        SortedSet<String> day = new TreeSet<>();
        SortedSet <Integer> session = new TreeSet<>();
        for(CourseSchedule cs : courseSchedules){
            teacher.add(cs.getTeacherName());
            String[] weeks = cs.getAllWeek().split(",");
            for(String week : weeks) {
                allWeek.add(Integer.valueOf(week));
            }
            day.add(cs.getDay());
            String[] sessions = cs.getSession().split(",");
            for(String s : sessions){
                session.add(Integer.valueOf(s));
            }
        }
        courseListVO.setTeachers(SetUtil.toPatterString(teacher, ","));
        courseListVO.setDays(SetUtil.toPatterString(day, ","));
        courseListVO.setAllWeek(SetUtil.toPatterString(allWeek, ","));
        courseListVO.setSessions(SetUtil.toPatterString(session, ","));
        courseListVOS.add(courseListVO);
    }
}
