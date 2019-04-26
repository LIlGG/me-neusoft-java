package com.lixingyong.meneusoft.modules.xcx.service;

import com.baomidou.mybatisplus.service.IService;
import com.lixingyong.meneusoft.modules.xcx.entity.ContactBook;
import com.lixingyong.meneusoft.modules.xcx.entity.Grade;

import java.util.List;


public interface GradeService extends IService<Grade> {

    List<Grade> getGradeList(Integer valueOf);

    void insertOrUpdateGrades(List<Grade> grades);
}
