package com.lixingyong.meneusoft.modules.xcx.controller;

import com.lixingyong.meneusoft.common.utils.R;
import com.lixingyong.meneusoft.common.utils.SetUtil;
import com.lixingyong.meneusoft.modules.xcx.annotation.LoginUser;
import com.lixingyong.meneusoft.modules.xcx.annotation.Token;
import com.lixingyong.meneusoft.modules.xcx.entity.*;
import com.lixingyong.meneusoft.modules.xcx.service.*;
import com.lixingyong.meneusoft.modules.xcx.vo.CourseEvaluateParam;
import com.lixingyong.meneusoft.modules.xcx.vo.CourseListVO;
import com.lixingyong.meneusoft.modules.xcx.vo.CourseParam;
import com.lixingyong.meneusoft.modules.xcx.vo.CourseScheduleVO;
import io.swagger.annotations.Api;
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
    @Autowired
    private CourseCountService courseCountService;
    @Autowired
    private CourseEvaluateService courseEvaluateService;
    @Autowired
    private CourseGradeService courseGradeService;
    @Autowired
    private WechatService wechatService;
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
                CourseListVO courseListVO = getCourseListVO( course, courseSchedules);
                if(courseListVO != null){
                    courseListVOS.add(getCourseListVO( course, courseSchedules));
                }
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
            CourseListVO courseListVO = getCourseListVO( course, courseSchedules);
            if(courseListVO != null){
                courseListVOS.add(getCourseListVO( course, courseSchedules));
            }

        }
        return R.ok(courseListVOS);
    }

    @Token
    @GetMapping("/details")
    public R getCourseDetails(@RequestParam("course_id") Long courseId, @RequestParam("lesson_id") String lessonId, @LoginUser String userId){
        Map<String, Object> result = new HashMap<>();
        // 获取当前课程基本信息
        Course course = courseService.getCourseList(courseId);
        if(course == null){
            return R.error("未找到课程信息");
        }
        // 获取当前课程教学信息
        List<CourseSchedule> courseSchedules = courseScheduleService.getCourseList(courseId);
        Iterator iterator = courseSchedules.iterator();
        while(iterator.hasNext()){
            CourseSchedule courseSchedule = (CourseSchedule) iterator.next();
            if(courseSchedule.getClassroomName() == null){
                iterator.remove();
            }
        }
        course.setCourseSchedules(courseSchedules);
        // 以下为获取课程统计信息
        CourseCount courseCount = courseCountService.getCourseCount(courseId);
        CourseScheduleVO courseScheduleVO = getCourseScheduleVO(course, courseSchedules);
        courseCount.setCourseId(courseId);
        courseCount.setCredit(course.getCredit());
        courseCount.setExamType(course.getExamType());
        courseCount.setLessonId(course.getLessonId());
        courseCount.setName(course.getName());
        courseCount.setTeacher(courseScheduleVO.getTeachers());
        courseCount.setType(course.getType());
        courseCount.setDay(courseScheduleVO.getDay());
        // 以下为获取课程评价信息
        List<CourseEvaluate> courseEvaluates = courseEvaluateService.getCourseEvaluateList(courseId);
        // 以下为获取课程历年来成绩信息
        List<CourseGrade> courseGrades = courseGradeService.getCourseGradeList(courseId);
        // 获取当前用户对当前课程的评价信息，一个用户只能评价一次
        CourseEvaluate myEvaluate = courseEvaluateService.getMyEvaluate(courseId, Long.valueOf(userId));
        result.put("evaluate", myEvaluate);
        result.put("course_grades", courseGrades);
        result.put("course_evaluates", courseEvaluates);
        result.put("course_count", courseCount);
        result.put("course", course);
        return R.ok().put("data", result);
    }

    // 提交评价内容
    @Token
    @PostMapping("/comment")
    public R getCourseComment(CourseEvaluateParam courseEvaluateParam, @LoginUser String userId){
        if(courseEvaluateParam.getId() == 0){
            // 当前为新增评价内容，首先判断当前用户是否在当前课程有过评价内容
            int count = courseEvaluateService.getCurUserCourseEvaluate(courseEvaluateParam.getCourseId(), Long.valueOf(userId));
            if(count > 0){
                return R.error("此课程已被当前用户评价");
            }
            // 获取课程信息
            Course course = courseService.getCourseList(courseEvaluateParam.getCourseId());
            // 获取用户信息
            Wechat wechat = wechatService.selectByUserId(userId);
            if(course == null || wechat == null){
                return R.error("获取课程信息或用户信息失败");
            }
            try {
                courseEvaluateService.addCourseEvaluate(courseEvaluateParam,course,wechat);
                return R.ok("新增评论成功");
            }catch (Exception e){
                return R.error("新增评论失败");
            }
        }else {
            // 当前内容为编辑评价内容，先获取之前的信息
            CourseEvaluate courseEvaluate = courseEvaluateService.getMyEvaluate(courseEvaluateParam.getId());
            if (courseEvaluate == null) {
                return R.error("获取原始评价信息失败");
            }
            courseEvaluate.setTask(courseEvaluateParam.getTask());
            courseEvaluate.setCallName(courseEvaluateParam.getCallName());
            courseEvaluate.setExamType(courseEvaluateParam.getExamType());
            courseEvaluate.setAssess(courseEvaluateParam.getAssess());
            courseEvaluate.setComment(courseEvaluateParam.getComment());
            courseEvaluateService.updateCourseEvaluate(courseEvaluate);
            return R.ok("更新评论成功");
        }
    }

    @GetMapping("/comment/info")
    public R getCourseCommentInfo(@RequestParam("id") int evaluateId){
        CourseEvaluate courseEvaluate = courseEvaluateService.getMyEvaluate(evaluateId);
        if(courseEvaluate != null){
            return R.ok(courseEvaluate);
        }
        return R.error("获取评价信息失败");
    }

    private CourseScheduleVO getCourseScheduleVO(Course course, List<CourseSchedule> courseSchedules) {
        CourseScheduleVO scheduleVO = new CourseScheduleVO();
        scheduleVO.setCourse(course);
        if(courseSchedules.isEmpty()){
            return scheduleVO;
        }
        Set<String> teacher = new HashSet<>();
        SortedSet<Integer> allWeek = new TreeSet<>();
        SortedSet<Integer> studentNo = new TreeSet<>();
        SortedSet<String> className = new TreeSet<>();
        SortedSet<String> day = new TreeSet<>();
        SortedSet <Integer> session = new TreeSet<>();
        SortedSet<String> classroomName = new TreeSet<>();
        SortedSet<Integer> termId = new TreeSet<>();
        for(CourseSchedule cs : courseSchedules){
            teacher.add(cs.getTeacherName());
            studentNo.add(cs.getStudentNo());
            String[] weeks = cs.getAllWeek().split(",");
            for(String week : weeks) {
                allWeek.add(Integer.valueOf(week));
            }
            String[] classNames = cs.getClassName().split("\\s+");
            className.addAll(Arrays.asList(classNames));
            day.add(cs.getDay());
            if(cs.getClassroomName() != null){
                classroomName.add(cs.getClassroomName());
            }
            termId.add(cs.getTermId());
            String[] sessions = cs.getSession().split(",");
            for(String s : sessions){
                session.add(Integer.valueOf(s));
            }
            scheduleVO.setTeachers(SetUtil.toPatterString(teacher, ","));
            scheduleVO.setAllWeek(SetUtil.toPatterString(allWeek, ","));
            scheduleVO.setClassName(SetUtil.toPatterString(className, ","));
            scheduleVO.setClassroomName(SetUtil.toPatterString(classroomName, ","));
            scheduleVO.setDay(SetUtil.toPatterString(day, ","));
            scheduleVO.setSession(SetUtil.toPatterString(session, ","));
            scheduleVO.setStudent_no(SetUtil.toPatterString(studentNo, ","));
            scheduleVO.setTermId(SetUtil.toPatterString(termId, ","));
        }
        return scheduleVO;
    }

    private CourseListVO  getCourseListVO(Course course, List<CourseSchedule> courseSchedules) {
        if(courseSchedules.isEmpty()){
            return null;
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
        return courseListVO;
    }
}
