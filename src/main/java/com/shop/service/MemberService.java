package com.shop.service;

import com.shop.entity.Member;
import com.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

@Service    // 비즈니스 로직 담당하는 서비스 계층.
@Transactional  // 로직을 처리하다가 에러가 발생하였다면, 변경된 데이터를 로직을 수행하기 이전 상태로 콜백 시켜준다.
@RequiredArgsConstructor    // final이나 @NonNull이 붙은 필드에 생성자를 생성한다.
public class MemberService implements UserDetailsService {  // MemberService가 UserDetailsService를 구현.

    private final MemberRepository memberRepository;

    public Member saveMember(Member member) {
        validateDuplicateMember(member);
        return memberRepository.save(member);

    }

    private void validateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if (findMember != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    /*
     * UserDetailsService 인터페이스의 loadUserByUsername() 메소드를 오버라이딩
     * 로그인 할 유저의 email을 파라미터로 전달받는다.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email);

        if (member == null) {
            throw new UsernameNotFoundException(email);
        }

        /* UserDetail을 구현하고 있는 User 객체를 반환해준다. User 객체를 생성하기 위해 생성자로 회원의 이메일, 비밀번호, role을 파라미터로 넘겨준다. */
        return User.builder()
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }
}
