package com.ll.gramgram.boundedContext.instaMember.repository;


import com.ll.gramgram.base.Rq;
import com.ll.gramgram.boundedContext.instaMember.form.ConnectForm;
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

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/connect")
    public String connect() {
        return "/user/instaMember/connect";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/connect")
    public String connect(@Valid ConnectForm connectForm) {
        return rq.redirectWithMsg("/pop", "인스타그램 계정이 연결되었습니다.");
    }


}
