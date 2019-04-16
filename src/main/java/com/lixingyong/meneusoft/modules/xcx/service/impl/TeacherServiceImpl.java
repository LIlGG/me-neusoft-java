package com.lixingyong.meneusoft.modules.xcx.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.lixingyong.meneusoft.modules.xcx.dao.TeacherDao;
import com.lixingyong.meneusoft.modules.xcx.entity.Teacher;
import com.lixingyong.meneusoft.modules.xcx.service.TeacherService;
import org.springframework.stereotype.Service;

;import java.util.List;

/**
 * @ClassName TeacherServiceImpl
 * @Description TODO
 * @Author lixingyong
 * @Date 2018/12/11 12:00
 * @Version 1.0
 */
@Service("teacherService")
public class TeacherServiceImpl extends ServiceImpl<TeacherDao, Teacher> implements TeacherService {

    @Override
    public void insertOrUpdateTeachers(List<Teacher> teachers) {
        this.insertOrUpdateBatch(teachers);
    }
}
