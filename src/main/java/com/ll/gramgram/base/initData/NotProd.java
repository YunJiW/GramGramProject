package com.ll.gramgram.base.initData;

import com.ll.gramgram.boundedContext.member.service.MemberService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"dev", "test"})
public class NotProd {

    CommandLineRunner initData(MemberService memberService) {
        return args -> {
            memberService.join("user10", "1234");
            memberService.join("admin", "1234");
            memberService.join("user2", "12345");
        };
    }
}
