package com.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 회원가입 후 메인 페이지로 이동할 수 있도록 만들어주는 클래스
 */
@Controller
public class MainController {

    @GetMapping(value = "/")
    public String main() {
        return "main";
    }
}
