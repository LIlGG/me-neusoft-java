package com.lixingyong.meneusoft.modules.xcx.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.lixingyong.meneusoft.common.utils.DateUtils;
import com.lixingyong.meneusoft.modules.xcx.dao.FeedbackDao;
import com.lixingyong.meneusoft.modules.xcx.entity.Feedback;
import com.lixingyong.meneusoft.modules.xcx.service.FeedbackService;
import com.lixingyong.meneusoft.modules.xcx.vo.Issue;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


/**
 * @ClassName TagServiceImpl
 * @Description TODO
 * @Author lixingyong
 * @Date 2018/12/11 12:00
 * @Version 1.0
 */
@Service("feedbackService")
public class FeedbackServiceImpl extends ServiceImpl<FeedbackDao, Feedback> implements FeedbackService {
    @Override
    public List<Feedback> getFeedbacks(Integer userId) {
        return this.baseMapper.selectList(new EntityWrapper<Feedback>().eq("user_id", userId).orderBy("number", false));
    }

    @Override
    public int frequencyValidate(Integer userId) {
        String newDate = DateUtils.format(new Date());
        return this.baseMapper.selectCount(new EntityWrapper<Feedback>().eq("user_id", userId).like("created_at",newDate+"%"));
    }

    @Override
    public void saveFeedBackInfo(int userId, int number, String title, String stat, String tags) {
        Feedback feedback = new Feedback();
        feedback.setUserId(userId);
        feedback.setNumber(number);
        feedback.setTitle(title);
        feedback.setStat(stat);
        feedback.setTags(tags);
        this.baseMapper.insert(feedback);
    }

    @Override
    public void updateIssue(int id, Issue issue) {
        Feedback feedback = this.selectOne(new EntityWrapper<Feedback>().eq("number", id));
        feedback.setTitle(issue.getTitle());
        feedback.setStat(issue.getState());
        this.baseMapper.updateById(feedback);
    }

    @Override
    public void deleteIssue(int id) {
        this.baseMapper.delete(new EntityWrapper<Feedback>().eq("number", id));
    }
}
