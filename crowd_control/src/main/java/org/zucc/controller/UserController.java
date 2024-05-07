package org.zucc.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.zucc.dao.UserDao;
import org.zucc.entity.NumberOfPeople;
import org.zucc.entity.Systems;
import org.zucc.entity.User;
import org.zucc.service.NumberOfPeopleService;
import org.zucc.service.SystemService;
import org.zucc.service.UserService;
import org.zucc.utils.CastClass;
import org.zucc.utils.Constant;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/user")
@Slf4j
@CrossOrigin
@ResponseBody
public class UserController {
    @Resource
    private UserService userService;

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/register")
    public String register(@RequestParam("userName") String userName, @RequestParam("password") String password) {
        User user = new User();
        user.setUserName(userName);
        user.setPassword(password);
        return userService.registerInfo(user);
    }

    @PostMapping("/login")
    public String login(@RequestParam("userName") String userName, @RequestParam("password") String password,
                        @RequestParam("role")String role,
                        int num, String system, HttpSession httpSession) {
        User user = new User();
        user.setUserName(userName);
        user.setRole(role);
        user.setPassword(password);
        return userService.loginInfo(user, num, system, httpSession);
    }

    @GetMapping("/currentuser")
    @ResponseBody
    public User currentUser(HttpSession httpSession) {
        /*
            获取当前登录的用户
         */
        try {
            return userService.getUserByName(
                    ((User) httpSession.getAttribute("currentUser"))
                            .getUserName()
            );
        } catch (NullPointerException e) {
            log.error("没有登录，无法获取当前用户信息");
            return new User();
        }
    }

    @GetMapping("/index")
    public String Index() {
        return "index";
    }

    @GetMapping("/question")
    public String getQuestion() {
        return "question";
    }
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private SystemService systemService;
    @Resource
    private UserDao userDao;
    @Resource
    private NumberOfPeopleService numberOfPeopleService;
    @GetMapping("/logout")
    public String logout(@RequestParam("systemId") String systemId,@RequestParam("role")String role) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("systemname", systemId);
        List<User> userList = userService.list(queryWrapper);
        if (userList.size()==1){
            User user = userList.get(0);
            user.setSystemName("");
            user.setRole("");
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("id", user.getId());
            user.setStatus(Constant.STATUS_OFFLINE);
            userDao.update(user,wrapper);
            Systems systems = systemService.getById(systemId);

            Object object = redisTemplate.opsForValue().get(systems.getSystemName() + "_NumberOfPeople");
            List<NumberOfPeople> numberOfPeopleList = CastClass.castList(object, NumberOfPeople.class);
            numberOfPeopleService.updateBatchById(numberOfPeopleList);

            redisTemplate.delete(systems.getSystemName() + "_NumberOfPeople");
            redisTemplate.delete(systems.getSystemName() + "_Time");
            redisTemplate.delete(systems.getSystemName() + "_Deploys");
            redisTemplate.delete(systems.getSystemName() + "_operate");
        }else if (userList.size()>1){
            QueryWrapper<User> queryWrapper2 = new QueryWrapper<>();
            queryWrapper.eq("systemname", systemId);
            queryWrapper.eq("role", role);
            List<User> userList2 = userService.list(queryWrapper);
            User user = userList2.get(0);
            user.setSystemName(null);
            user.setRole(null);
            user.setStatus(Constant.STATUS_OFFLINE);
            userService.updateById(user);
        }
        return "redirect:/user/login";
    }
}
