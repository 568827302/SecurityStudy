package com.example.demo.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@Slf4j
public class LoginCtrl {
//    @RequestMapping("/login")
//    public String login(HttpServletRequest req) {
////        return "login";
//        if(SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {  // 已认证
//            log.info("已认证，跳转到index");
//            return "redirect:/index";
//        } else {
//            log.info("未认证，去login");
//            return "login";
//        }
//    }
}
