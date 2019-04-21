package com.lixingyong.meneusoft.modules.xcx.service;

import com.baomidou.mybatisplus.service.IService;
import com.lixingyong.meneusoft.modules.xcx.entity.Lecture;

import java.util.List;


public interface LectureService extends IService<Lecture> {

    void insertOrUpdateLectures(List<Lecture> lectures);

    List<Lecture> getLectures(String title,int page, int pageSize, String startTime);
}
