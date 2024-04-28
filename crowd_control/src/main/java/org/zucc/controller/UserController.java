package org.zucc.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.zucc.entity.User;
import org.zucc.service.UserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

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

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/user/login";
    }
}
