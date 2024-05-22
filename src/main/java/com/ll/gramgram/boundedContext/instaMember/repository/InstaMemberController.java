package com.ll.gramgram.boundedContext.instaMember.repository;


import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/instaMember")
public class InstaMemberController {

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/connect")
    public String connect(){
        return "/user/instaMember/connect";
    }
}
