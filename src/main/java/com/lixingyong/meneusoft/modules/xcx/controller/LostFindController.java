package com.lixingyong.meneusoft.modules.xcx.controller;

import com.lixingyong.meneusoft.common.exception.WSExcetpion;
import com.lixingyong.meneusoft.common.utils.R;
import com.lixingyong.meneusoft.common.validator.ValidatorUtils;
import com.lixingyong.meneusoft.modules.xcx.annotation.LoginUser;
import com.lixingyong.meneusoft.modules.xcx.annotation.Token;
import com.lixingyong.meneusoft.modules.xcx.entity.LostFind;
import com.lixingyong.meneusoft.modules.xcx.service.LostFindService;
import com.lixingyong.meneusoft.modules.xcx.vo.LostFindInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api("失物招领")
@RestController
public class LostFindController {
    @Autowired
    private LostFindService lostFindService;
    @ApiOperation("获取失物招领信息")
    @Token
    @GetMapping("/lost_finds")
    public R getLostFinds(@RequestParam("page") int page, @RequestParam("page_size") int pageSize,
                          @RequestParam("category") String category, @RequestParam("my") int my, @LoginUser String userId){
        List<LostFind> lostFindList;
        if(my == 1){
            lostFindList = lostFindService.getLostFindList(page, pageSize, category, Integer.valueOf(userId));
        } else{
            lostFindList = lostFindService.getLostFindList(page, pageSize, category, 0);
        }
        return R.ok().put("data", lostFindList);
    }

    // 新建一个失物招领
    @ApiOperation("新建失物招领")
    @PostMapping("/lost_find")
    @Token
    public R newLostFind(@Valid LostFind lostFind, @LoginUser String userId){
        try {
            // 查找当前用户昵称
            lostFind.setUserId(Integer.valueOf(userId));
            lostFind.setStatus(1);
            lostFindService.addLostFind(lostFind);
            return R.ok("新建失物招领成功");
        }catch (WSExcetpion e){
            return R.error(e.getCode(),e.getMsg());
        }
    }

    // 查看某个失物招领信息的详细内容
    @ApiOperation("失物招领详细内容")
    @GetMapping("/lost_find/{itemId}")
    @Token
    public R lostFindInfo(@PathVariable("itemId") int itemId, @LoginUser String userId){
        LostFindInfoVO lostFindInfoVO = new LostFindInfoVO();
        LostFind lostFind = lostFindService.getLostFind(itemId);
        lostFindInfoVO.setData(lostFind);
        if(lostFind.getUserId() == Integer.valueOf(userId)){
           lostFindInfoVO.setIsMe(1);
        }
        return R.ok(lostFindInfoVO);
    }

    // 更新失物招领信息失物招领
    @ApiOperation("新建失物招领")
    @PostMapping("/lost_find/update")
    @Token
    public R updateLostFind(LostFind lostFind, @LoginUser String userId){
        ValidatorUtils.validateEntity(lostFind);
        lostFind.setUserId(Integer.valueOf(userId));
        lostFindService.updateLostFindInfo(lostFind);
        return R.ok("修改成功");
    }
}
