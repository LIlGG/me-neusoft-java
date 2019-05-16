package com.lixingyong.meneusoft.modules.xcx.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lixingyong.meneusoft.modules.xcx.entity.Classroom;
import com.lixingyong.meneusoft.modules.xcx.entity.CourseSchedule;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CourseScheduleDao extends BaseMapper<CourseSchedule> {
}
