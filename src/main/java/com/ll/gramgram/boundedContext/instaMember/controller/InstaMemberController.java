package com.ll.gramgram.boundedContext.instaMember.controller;


import com.ll.gramgram.base.Rq;
import com.ll.gramgram.base.rsData.RsData;
import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import com.ll.gramgram.boundedContext.instaMember.form.ConnectForm;
import com.ll.gramgram.boundedContext.instaMember.service.InstaMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/instaMember")
public class InstaMemberController {

    private final Rq rq;

    private final InstaMemberService instaMemberService;


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/connect")
    public String connect() {
        return "/user/instaMember/connect";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/connect")
    public String connect(@Valid ConnectForm connectForm) {
        RsData<InstaMember> rsData = instaMemberService.connect(rq.getMember(), connectForm.getUsername(), connectForm.getGender());
        return rq.redirectWithMsg("/likeablePerson/add", "인스타그램 계정이 연결되었습니다.");
    }


}
