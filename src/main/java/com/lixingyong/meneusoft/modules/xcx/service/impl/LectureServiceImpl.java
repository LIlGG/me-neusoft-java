package com.lixingyong.meneusoft.modules.xcx.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.lixingyong.meneusoft.modules.xcx.dao.LectureDao;
import com.lixingyong.meneusoft.modules.xcx.entity.Lecture;
import com.lixingyong.meneusoft.modules.xcx.service.LectureService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName LectureServiceImpl
 * @Description TODO
 * @Author lixingyong
 * @Date 2018/12/11 12:00
 * @Version 1.0
 */
@Service("lectureService")
public class LectureServiceImpl extends ServiceImpl<LectureDao, Lecture> implements LectureService {

    @Override
    public void insertOrUpdateLectures(List<Lecture> lectures) {
        this.insertOrUpdateBatch(lectures);
    }

    @Override
    public List<Lecture> getLectures(String title, int page, int pageSize, String startTime) {
        Page<Lecture> pages = new Page<>(page, pageSize);
        if(startTime.equals("")){
            return this.baseMapper.selectPage(pages,new EntityWrapper<Lecture>().orderBy("start_time", false).like("title", title==null ? "%":"%"+title+"%"));
        }
        return this.baseMapper.selectPage(pages,new EntityWrapper<Lecture>().eq("start_time", startTime).eq("title",  title==null ? "%":"%"+title+"%").orderBy("start_time", false));
    }
}
