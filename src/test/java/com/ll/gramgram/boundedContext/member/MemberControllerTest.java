package com.ll.gramgram.boundedContext.member;

import com.ll.gramgram.boundedContext.member.controller.MemberController;
import com.ll.gramgram.boundedContext.member.entity.Member;
import com.ll.gramgram.boundedContext.member.service.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class MemberControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("회원가입 폼")
    void t1() throws Exception {

        ResultActions resultActions = mvc.perform(get("/member/join"))
                .andDo(print());

        resultActions.andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("join"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(content().string(containsString("""
                        <input type="text" name="username" """
                        .stripIndent().trim())))
                .andExpect(content().string(containsString("""
                        <input type="password" name="password"
                        """.stripIndent().trim())))
                .andExpect(content().string(containsString("""
                        <input type="submit" value="회원가입" """.stripIndent().trim())));
    }

    @Test
    @DisplayName("회원가입 진행")
    void t2() throws Exception {

        ResultActions resultActions = mvc.perform(post("/member/join")
                        .with(csrf())
                        .param("username", "user10")
                        .param("password", "1234"))
                .andDo(print());

        resultActions.andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("join"))
                .andExpect(status().is3xxRedirection());

        Member member = memberService.findByUsername("user10").orElse(null);

        assertThat(member).isNotNull();


    }

}