package com.example.demo.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class SecuredRestAPIIntTests {
    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())        // 如果不加这个的话，默认的测试案例不走安全验证，如果加上，那么就要经过安全框架验证
                .build();
    }

    @Test
    @WithMockUser(roles = "USER")   // 如果加了 springSecurity() 安全验证，该标签可以模拟角色或者用户登录
    void givenAuthRequest_shouldSuccessWith201() throws Exception {
        mockMvc.perform(
                get("/api/greeting")
        ).andExpect(status().is(201));
    }

}