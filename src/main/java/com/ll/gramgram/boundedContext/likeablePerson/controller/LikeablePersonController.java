package com.ll.gramgram.boundedContext.likeablePerson.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/likeablePerson")
public class LikeablePersonController {

    @GetMapping("/add")
    public String add() {
        return "/user/likeablePerson/add";
    }
}
