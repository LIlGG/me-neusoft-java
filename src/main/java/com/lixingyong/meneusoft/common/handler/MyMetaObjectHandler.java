package com.lixingyong.meneusoft.common.handler;

import com.baomidou.mybatisplus.mapper.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @ClassName MyMetaObjectHandler
 * @Description TODO MyBatis自动注入管理
 * @Author lixingyong
 * @Date 2018/12/11 10:49
 * @Version 1.0
 */
@Component
public class MyMetaObjectHandler extends MetaObjectHandler {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void insertFill(MetaObject metaObject) {
        logger.info("新增");
        Object createdAt = getFieldValByName("created_at", metaObject);
        Object updatedAt = getFieldValByName("updated_at", metaObject);
        Object deletedAt = getFieldValByName("deleted_at", metaObject);

        if(null == createdAt){
            setFieldValByName("createdAt",new Date(), metaObject);
        }
        if(null == updatedAt){
            setFieldValByName("updatedAt", new Date(), metaObject);
        }

        if(null == deletedAt){
            setFieldValByName("deleted_at", null, metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        logger.info("更新");
        Object updateAt =  getFieldValByName("updated_at", metaObject);
        if(null == updateAt){
            setFieldValByName("updatedAt", new Date(), metaObject);
        }
    }
}
