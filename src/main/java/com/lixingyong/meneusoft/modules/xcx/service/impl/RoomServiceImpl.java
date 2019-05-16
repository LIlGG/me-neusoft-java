package com.lixingyong.meneusoft.modules.xcx.service.impl;

import com.lixingyong.meneusoft.modules.xcx.entity.Course;
import com.lixingyong.meneusoft.modules.xcx.entity.CourseSchedule;
import com.lixingyong.meneusoft.modules.xcx.service.CourseService;
import com.lixingyong.meneusoft.modules.xcx.service.RoomService;
import com.lixingyong.meneusoft.modules.xcx.vo.ClassUse;
import com.lixingyong.meneusoft.modules.xcx.vo.RoomVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service("roomService")
public class RoomServiceImpl implements RoomService {
    @Autowired
    private CourseService courseService;
    @Override
    public RoomVO getRoom(List<CourseSchedule> courseScheduleList, String classroomName) {
        List<ClassUse> classUses = new LinkedList<>();
        // 判断是否有课程，无课程则今天本教室全部为空闲
        if(courseScheduleList.isEmpty()){
            // 设置本教室1-5节全部为空闲
            for(int i = 0; i < 5; i++){
                ClassUse classUse = new ClassUse();
                classUse.setUse(0);
                classUses.add(classUse);
            }
        } else {
            for(int i = 1; i <= 10; i+= 2){
                ClassUse classUse = new ClassUse();
                classUse.setUse(0);
                for(int j = 0; j < courseScheduleList.size(); j++){
                    CourseSchedule courseSchedule = courseScheduleList.get(j);
                    if(courseSchedule.getAllWeek().contains(String.valueOf(i))){
                        Course course = courseService.selectById(courseSchedule.getCourseId());
                        classUse.setJsm(course.getName());
                        classUse.setKcm("课");
                        classUse.setTeacher(courseSchedule.getTeacherName());
                        classUse.setUse(1);
                        classUse.setClassNames(courseSchedule.getClassName());
                        courseScheduleList.remove(j);
                        break;
                    }
                }
                classUses.add(classUse);
            }
        }
        RoomVO roomVO = new RoomVO();
        String[] roomNames = classroomName.split("-");
        if(roomNames.length == 2){
            roomVO.setRoomName(roomNames[1]);
        } else {
            roomVO.setRoomName(classroomName);
        }
        roomVO.setClassUse(classUses);
        return roomVO;
    }
}
