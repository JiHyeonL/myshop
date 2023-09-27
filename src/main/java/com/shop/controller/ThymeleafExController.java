package com.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
// RequestMapping : 클라이언트의 요청을 어떤 컨트롤러가 처리할지 매핑. 이 경우 타임리프 경로로 오는 요청을 ThymeleafExController가 처리함
@RequestMapping(value = "/thymeleaf")
public class ThymeleafExController {
    @GetMapping(value = "/ex01")
    public String thymeleafExample01(Model model) {
        model.addAttribute("data", "타임리프 예제 입니다."); // 뷰에 전달할 데이터를 모델 객체를 이용해 key,value 구조로 넣어준다.
        return "thymeleafEx/thymeleafEx01"; // templates 폴더를 기준으로 뷰의 위치와 이름(thymeleafEx01.html)을 반환.
    }
}
