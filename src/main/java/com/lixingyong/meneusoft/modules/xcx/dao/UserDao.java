package com.lixingyong.meneusoft.modules.xcx.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lixingyong.meneusoft.modules.xcx.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao extends BaseMapper<User> {
}