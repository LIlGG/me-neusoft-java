package com.lixingyong.meneusoft.modules.xcx.controller;

import com.lixingyong.meneusoft.common.utils.DateUtils;
import com.lixingyong.meneusoft.common.utils.R;
import com.lixingyong.meneusoft.modules.xcx.entity.Classroom;
import com.lixingyong.meneusoft.modules.xcx.entity.CourseSchedule;
import com.lixingyong.meneusoft.modules.xcx.entity.Term;
import com.lixingyong.meneusoft.modules.xcx.service.ClassroomService;
import com.lixingyong.meneusoft.modules.xcx.service.CourseScheduleService;
import com.lixingyong.meneusoft.modules.xcx.service.RoomService;
import com.lixingyong.meneusoft.modules.xcx.service.TermService;
import com.lixingyong.meneusoft.modules.xcx.vo.RoomVO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Api("自习")
@RestController
public class ClassroomController {
    @Autowired
    private ClassroomService classroomService;
    @Autowired
    private TermService termService;
    @Autowired
    private CourseScheduleService courseScheduleService;
    @Autowired
    private RoomService roomService;
    @PostMapping("classroom")
    public R getClassroomList(@RequestParam("classroom") int classroom){
        List<RoomVO> roomList = new LinkedList<>();
        // 获取当前所选教室的所有班级
        List<Classroom> classrooms = classroomService.getClassroom(classroom);
        // 获取当前所处日期
        Term term = termService.getCurTermInfo();
        if(term == null){
            return R.error("放假期间无法获取");
        }
        // 判断当前属于第几周
       int  weeks = DateUtils.getWeek(term.getStartTime(), term.getEndTime());
        // 判断日期属于星期几
        String day = DateUtils.getWeekOfDate(new Date());
        // 根据教室名、学期、周期、星期获取课程信息
        for(Classroom cr : classrooms){
            List<CourseSchedule> courseScheduleList = courseScheduleService.getCourseList(cr.getClassroomName(),term.getId(), weeks, day);
            // 将获取到的课程信息进行整理
            RoomVO roomVO = roomService.getRoom(courseScheduleList, cr.getClassroomName());
            roomList.add(roomVO);
        }
        return R.ok().put("rooms", roomList);
    }
}
