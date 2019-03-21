package com.lixingyong.meneusoft.modules.xcx.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.lixingyong.meneusoft.modules.xcx.dao.DetailDao;
import com.lixingyong.meneusoft.modules.xcx.entity.Detail;
import com.lixingyong.meneusoft.modules.xcx.service.DetailService;
import org.springframework.stereotype.Service;

/**
 * @ClassName DetailServiceImpl
 * @Description TODO
 * @Author lixingyong
 * @Date 2018/12/11 12:00
 * @Version 1.0
 */
@Service("detailService")
public class DetailServiceImpl extends ServiceImpl<DetailDao, Detail> implements DetailService {
}
