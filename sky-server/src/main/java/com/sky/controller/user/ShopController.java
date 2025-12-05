package com.sky.controller.user;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("usershopController")
@RequestMapping("/user/shop")
@Api(tags = "商户端-商户信息管理")
@Slf4j
public class ShopController {
    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/status")
    @ApiOperation("获取商户状态")
    public Result<Integer> getStatus(Integer status){
        log.info("获取商户状态");
        Integer shopStatus = (Integer) redisTemplate.opsForValue().get("status");
        log.info("商户状态: {}", shopStatus);
        return Result.success(shopStatus);
    }
}
