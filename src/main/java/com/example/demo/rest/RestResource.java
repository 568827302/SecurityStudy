package com.example.demo.rest;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.Data;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;

@RestController
@RequestMapping("/api")
public class RestResource {
    @GetMapping(value = { "/greeting" })
    @ResponseStatus(HttpStatus.CREATED)
    public String greeting() {
        return "Hello World!";
    }

    @PostMapping(value = "/greeting")
    public String postGreeting(@RequestParam String name, @RequestBody Info info) {
        return "Hello World, " + name + "!\n info: " + info.name + ", " + info.gender;
    }

    @PutMapping(value = "/greeting/{name}")
    public String putGreeting(@PathVariable String name) {
        return "Hello World, " + name;
    }

    @GetMapping("/name")    // rest接口请求，因为没有请求体，所以需要打开httpBasic()，接手Authorization请求头来接手用户名密码参数通过认证
    public String getName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
    @GetMapping("/getAuthentication")       // 可以使用注入代替该方式，参考下面两个接口
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
    @GetMapping("/getAuthentication2")
    public Authentication getAuthentication(Authentication authentication) {
        return authentication;
    }
    @GetMapping("/getAuthentication3")  // Authentication接口的父接口, 该方式与Authentication接口内容一样
    public Principal getAuthentication(Principal principal) {
        return principal;
    }

    @Data
    static class Info {
        private String name;
        private String gender;
    }
}