package com.example.demo.controllers;

import org.springframework.http.HttpStatus;
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

@RestController
@RequestMapping("/api")
public class RestCtrl {
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

    @Data
    static class Info {
        private String name;
        private String gender;
    }
}