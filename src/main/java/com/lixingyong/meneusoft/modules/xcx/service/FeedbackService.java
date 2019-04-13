package com.lixingyong.meneusoft.modules.xcx.service;

import com.baomidou.mybatisplus.service.IService;
import com.lixingyong.meneusoft.modules.xcx.entity.Feedback;
import com.lixingyong.meneusoft.modules.xcx.vo.Issue;

import java.util.List;

public interface FeedbackService extends IService<Feedback> {
    List<Feedback> getFeedbacks(Integer valueOf);

    int frequencyValidate(Integer valueOf);

    void saveFeedBackInfo(int userId, int number, String title, String open, String tags);

    void updateIssue(int id, Issue issue);

    void deleteIssue(int id);
}
