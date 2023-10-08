package com.shop.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 회원 가입 화면으로부터 넘어오는 가입정보를 담을 dto 생성
 */
@Getter @Setter
public class MemberFormDto {

    private String name;

    private String email;

    private String password;

    private String address;

}
