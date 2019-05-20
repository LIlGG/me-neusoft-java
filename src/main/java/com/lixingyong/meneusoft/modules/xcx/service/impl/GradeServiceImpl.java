package com.lixingyong.meneusoft.modules.xcx.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.lixingyong.meneusoft.modules.xcx.dao.ContactBookDao;
import com.lixingyong.meneusoft.modules.xcx.dao.GradeDao;
import com.lixingyong.meneusoft.modules.xcx.entity.ContactBook;
import com.lixingyong.meneusoft.modules.xcx.entity.Grade;
import com.lixingyong.meneusoft.modules.xcx.service.ContactBookService;
import com.lixingyong.meneusoft.modules.xcx.service.GradeService;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

;

/**
 * @ClassName GradeServiceImpl
 * @Description TODO
 * @Author lixingyong
 * @Date 2018/12/11 12:00
 * @Version 1.0
 */
@Service("gradeServiceImpl")
public class GradeServiceImpl extends ServiceImpl<GradeDao, Grade> implements GradeService {

    @Override
    public List<Grade> getGradeList(Integer userId) {
        return this.baseMapper.selectList(new EntityWrapper<Grade>().eq("user_id", userId).orderBy("year", false).orderBy("term", true));
    }

    @Override
    public void insertOrUpdateGrades(List<Grade> grades) {
        this.insertOrUpdateBatch(grades);
    }

    @Override
    public void delGradeAll(Integer userId) {
        this.baseMapper.delete(new EntityWrapper<Grade>().eq("user_id", userId));
    }

    @Override
    public List<Grade> getGradeList(String courseId) {
        return this.baseMapper.selectList(new EntityWrapper<Grade>().eq("course_id", courseId));
    }
}
