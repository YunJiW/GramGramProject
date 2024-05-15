package com.ll.gramgram.boundedContext.member.controller;


import com.ll.gramgram.boundedContext.member.form.JoinForm;
import com.ll.gramgram.boundedContext.member.service.MemberService;
import com.ll.gramgram.standard.util.Ut;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/join")
    public String join() {
        return "user/member/join";
    }

    @PostMapping("/join")
    public String join(@Valid JoinForm joinForm) {
        memberService.join(joinForm.getUsername(), joinForm.getPassword());
        return "redirect:/member/login?msg=" + Ut.url.encode("회원가입이완료되었습니다.\n 로그인 후 이용해주세요.");
    }

    @GetMapping("/login")
    public String login() {
        return "user/member/login";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public String me() {
        return "user/member/me";
    }
}
