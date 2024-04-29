package org.zucc.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.zucc.entity.Systems;
import org.zucc.service.SystemService;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/system")
@CrossOrigin
@Api(value = "系统管理")
public class SystemController {
    @Resource
    private SystemService systemService;

    @Resource
    private RedisTemplate redisTemplate;
    @GetMapping("/list")
    @ApiOperation("获取系统列表")
    public List<Systems> systemsList() {
        return systemService.list();
    }


    @GetMapping("/time/{systemName}")
    @ApiOperation("获取系统时间")
    public String getCurrentTime(@PathVariable("systemName") String systemName) {
        return (String) redisTemplate.opsForValue().get(systemName+"_Time");
    }
}
