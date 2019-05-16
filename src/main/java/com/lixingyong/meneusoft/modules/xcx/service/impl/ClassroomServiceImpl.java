package com.lixingyong.meneusoft.modules.xcx.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.lixingyong.meneusoft.modules.xcx.dao.ClassroomDao;
import com.lixingyong.meneusoft.modules.xcx.entity.Classroom;
import com.lixingyong.meneusoft.modules.xcx.service.ClassroomService;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @ClassName ContactBookServiceImpl
 * @Description TODO
 * @Author lixingyong
 * @Date 2018/12/11 12:00
 * @Version 1.0
 */
@Service("classroomService")
public class ClassroomServiceImpl extends ServiceImpl<ClassroomDao, Classroom> implements ClassroomService {

    @Override
    public String getClassId(String ownText) {
        Classroom classroom = this.baseMapper.selectList(new EntityWrapper<Classroom>().eq("classroom_name", ownText)).get(0);
        return classroom.getClassroomId();
    }

    @Override
    public List<Classroom> getClassroom(int classroom) {
        return this.baseMapper.selectList(new EntityWrapper<Classroom>().eq("tb_id", classroom));
    }
}
