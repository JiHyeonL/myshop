package com.shop.controller;

import com.shop.dto.ItemDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
// RequestMapping : 클라이언트의 요청을 어떤 컨트롤러가 처리할지 매핑. 이 경우 타임리프 경로로 오는 요청을 ThymeleafExController가 처리함
@RequestMapping(value = "/thymeleaf")
public class ThymeleafExController {
    @GetMapping(value = "/ex01")
    public String thymeleafExample01(Model model) {
        model.addAttribute("data", "타임리프 예제 입니다."); // 뷰에 전달할 데이터를 모델 객체를 이용해 key,value 구조로 넣어준다.
        return "thymeleafEx/thymeleafEx01"; // templates 폴더를 기준으로 뷰의 위치와 이름(thymeleafEx01.html)을 반환.
    }

    /*
     * ItemDto 객체 생성 후 모델에 데이터를 담아서 뷰에 전달
     */
    @GetMapping(value = "/ex02")
    public String thymeleafExample02(Model model) {
        ItemDto itemDto = new ItemDto();
        itemDto.setItemDetail("제품 상세 설명");
        itemDto.setItemNm("테스트 상품1");
        itemDto.setPrice(10000);
        itemDto.setRegTime(LocalDateTime.now());

        model.addAttribute("itemDto", itemDto);
        return "thymeleafEx/thymeleafEx02";
    }

    @GetMapping(value = "/ex03")
    public String thymeleafExample03(Model model) {

        List<ItemDto> itemDtoList = new ArrayList<>();

        // 화면에서 출력할 10개의 itemDto 객체 생성 후 itemDtoList에 넣어줌
        for (int i = 1; i <= 10; i++) {
            ItemDto itemDto = new ItemDto();
            itemDto.setItemDetail("상품 상세 설명" + i);
            itemDto.setItemNm("테스트 상품" + i);
            itemDto.setPrice(1000 * i);
            itemDto.setRegTime(LocalDateTime.now());

            itemDtoList.add(itemDto);
        }

        // 화면에서 출력할 itemDtoList를 model에 담아서 뷰에 전달
        model.addAttribute("itemDtoList", itemDtoList);
        return "thymeleafEx/thymeleafEx03";
    }

    @GetMapping(value = "/ex04")
    public String thymeleafExample04(Model model) {
        List<ItemDto> itemDtoList = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            ItemDto itemDto = new ItemDto();
            itemDto.setItemDetail("상품 상세 설명" + i);
            itemDto.setItemNm("테스트 상품" + i);
            itemDto.setPrice(1000 * i);
            itemDto.setRegTime(LocalDateTime.now());

            itemDtoList.add(itemDto);
        }

        // 화면에서 출력할 itemDtoList를 model에 담아서 뷰에 전달
        model.addAttribute("itemDtoList", itemDtoList);
        return "thymeleafEx/thymeleafEx04";
    }

    @GetMapping(value = "/ex05")
    public String thymeleafExample05() {
        return "thymeleafEx/thymeleafEx05";
    }

    /*
     * thymeleafEx05에서 전달했던 매개변수와 같은 이름의 String 변수 param1, param2를 파라미터로 설정하면 자동으로 데이터가 바인딩 된다.
     * 매개변수를 모델에 담아서 뷰로 전달함.
     */
    @GetMapping(value = "/ex06")
    public String thymeleafExample06(String param1, String param2, Model model) {
        model.addAttribute("param1", param1);
        model.addAttribute("param2", param2);
        return "thymeleafEx/thymeleafEx06";
    }
}
