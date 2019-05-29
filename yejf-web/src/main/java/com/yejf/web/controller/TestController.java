package com.yejf.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yejf
 * @date 2019/5/29 11:46
 */
@RestController
public class TestController {

    @GetMapping("/test")
    public String test(){
        return "yejf-web";
    }
}
