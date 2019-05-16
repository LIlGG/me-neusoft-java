package com.lixingyong.meneusoft.modules.xcx.service;

import com.lixingyong.meneusoft.modules.xcx.entity.CourseSchedule;
import com.lixingyong.meneusoft.modules.xcx.vo.RoomVO;

import java.util.List;

public interface RoomService {
    RoomVO getRoom(List<CourseSchedule> courseScheduleList, String classroomName);
}
