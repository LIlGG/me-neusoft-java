package com.lixingyong.meneusoft.modules.xcx.controller;

import com.lixingyong.meneusoft.api.ufs.UFSUtil;
import com.lixingyong.meneusoft.common.utils.R;
import com.lixingyong.meneusoft.modules.xcx.annotation.LoginUser;
import com.lixingyong.meneusoft.modules.xcx.annotation.Token;
import com.lixingyong.meneusoft.modules.xcx.entity.Grade;
import com.lixingyong.meneusoft.modules.xcx.entity.User;
import com.lixingyong.meneusoft.modules.xcx.service.GradeService;
import com.lixingyong.meneusoft.modules.xcx.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Api("成绩")
@RestController
public class GradeController {
    @Autowired
    private GradeService gradeService;
    @Autowired
    private UserService userService;
    @ApiOperation("获取成绩")
    @GetMapping("/grade")
    @Token
    public R getGrades(@LoginUser String userId){
        List<Grade> gradeList = gradeService.getGradeList(Integer.valueOf(userId));
        return R.ok(gradeList);
    }

    @ApiOperation("更新成绩")
    @PostMapping("/grade")
    @Token
    public R updateGrade(@LoginUser String userId){
        // 根据userId查找用户UFS信息
        User user = userService.getUserInfo(Integer.valueOf(userId));
        if(user == null){
            return R.error("用户不存在");
        }
        if(user.getStudentId() == null || user.getPassword() == null){
            return R.error("UFS账号或密码不存在");
        }
        if(user.getVerify() != 1){
            return R.error("账号无法正常访问UFS系统");
        }
        // 执行登录
        UFSUtil.LoginUFS(user.getId(), user.getStudentId(), user.getPassword());
        // 获取当前用户所在年级
        String year = UFSUtil.baseInfo(user.getId());
        // 获取所有的年份
        List<String> yearList = UFSUtil.schoolYear(user.getId());
        int flag = 0, count = 0;
        for(int i = 0; i < yearList.size(); i++){
            if(yearList.get(i).split("-")[0].contains(year)){
                flag = i;
                break;
            }
        }
        List<Grade> grades = new ArrayList<>();
        for(int i = flag; i < yearList.size(); i++){
            count++;
            // 获取当前学年的成绩
            for(int j = 0; j < 3; j++){
                grades.addAll(UFSUtil.getGradeList(yearList.get(i),j+1, user.getId()));
            }
            if(count >= 4){
                break;
            }
        }
        gradeService.delGradeAll(user.getId());
        // 保存信息
        gradeService.insertOrUpdateGrades(grades);
        // 登出系统，删除cookie
        String session = UFSUtil.getSession(user.getId());
        UFSUtil.logout(session);
        return R.ok("更新成功");
    }
}
