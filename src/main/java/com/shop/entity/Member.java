package com.shop.entity;

import com.shop.constant.Role;
import com.shop.dto.MemberFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Table(name = "member_id")
@Getter
@Setter
@ToString
public class Member {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Column(unique = true)  // 회원은 이메일이 유일한 구분 요소이기 때문에 동일한 값이 데이터베이스에 들어오지 않도록 unique 속성 지정
    private String email;

    private String password;

    private String address;

    @Enumerated(EnumType.STRING)    // enum 타입을 엔티티 속성으로 지정 가능. enum의 특성상 기본적으로 순서가 저장되는데, enum 순서가 바뀔 경우 문제 발생할 수 있으므로 string으로 저장.
    private Role role;

    /*
     * Member 엔티티를 생성하는 메소드.
     */
    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {
        Member member = new Member();
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setAddress(memberFormDto.getAddress());
        String password = passwordEncoder.encode(memberFormDto.getPassword());  // encoder bean을 파라미터로 넘겨서 비밀번호 암호화
        member.setPassword(password);
        member.setRole(Role.USER);

        return member;
    }
}
