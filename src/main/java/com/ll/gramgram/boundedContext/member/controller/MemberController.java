package com.ll.gramgram.boundedContext.member.controller;


import com.ll.gramgram.base.Rq;
import com.ll.gramgram.base.rsData.RsData;
import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import com.ll.gramgram.boundedContext.member.entity.Member;
import com.ll.gramgram.boundedContext.member.form.JoinForm;
import com.ll.gramgram.boundedContext.member.service.MemberService;
import com.ll.gramgram.standard.util.Ut;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    private final Rq rq;

    @GetMapping("/join")
    public String join() {
        return "user/member/join";
    }

    @PostMapping("/join")
    public String join(@Valid JoinForm joinForm) {
        RsData<Member> joinRs = memberService.join(joinForm.getUsername(), joinForm.getPassword());

        if (joinRs.isFail()) {
            log.info("해당 아이디가 존재 = {}", joinRs.getMsg());

            return rq.historyBack(joinRs.getMsg());
        }
        String msg = joinRs.getMsg() + "\n 로그인 후 이용해주세요.";
        return "redirect:/member/login?msg=" + Ut.url.encode(msg);
    }

    @GetMapping("/login")
    public String login() {
        return "user/member/login";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public String me(Model model) {

        if (!rq.getMember().hasConnectedInstaMember()) {
            return rq.historyBack("먼저 본인의 인스타그램 아이디를 입력해주세요.");
        }
        InstaMember instaMember = rq.getMember().getInstaMember();

        long likesByGenderWomen = instaMember.getToLikeablePeople().stream().filter(likeablePerson -> likeablePerson.getToInstaMember().getGender().equals("W")).count();

        long likesByGenderMen = instaMember.getToLikeablePeople().stream().filter(likeablePerson -> likeablePerson.getToInstaMember().getGender().equals("M")).count();

        long typeCode1 = instaMember.getToLikeablePeople().stream().filter(likeablePerson -> likeablePerson.getAttractiveTypeCode() == 1).count();

        long typeCode2 = instaMember.getToLikeablePeople().stream().filter(likeablePerson -> likeablePerson.getAttractiveTypeCode() == 2).count();
        long typeCode3 = instaMember.getToLikeablePeople().stream().filter(likeablePerson -> likeablePerson.getAttractiveTypeCode() == 3).count();


        model.addAttribute("likes", likesByGenderWomen + likesByGenderMen);
        model.addAttribute("likesByGenderWomen", likesByGenderWomen);
        model.addAttribute("likesByGenderMen", likesByGenderMen);
        model.addAttribute("likesByAttractiveTypeCode1", typeCode1);
        model.addAttribute("likesByAttractiveTypeCode2", typeCode2);
        model.addAttribute("likesByAttractiveTypeCode3", typeCode3);
        return "user/member/me";
    }
}
