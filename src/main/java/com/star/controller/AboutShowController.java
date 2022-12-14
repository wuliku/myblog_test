package com.star.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Description: 关于我界面显示控制器，就是一个静态的网页
 * @Date: Created in 11:03 2020/4/17
 * @Author: ONESTAR
 * @QQ群: 530311074
 * @URL: https://onestar.newstar.net.cn/
 */
public class AboutShowController {

    @GetMapping("/about")
    public String about() {
        return "about";
    }
}