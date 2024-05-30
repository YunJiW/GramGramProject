package com.ll.gramgram.boundedContext.likeablePerson.controller;

import com.ll.gramgram.base.Rq;
import com.ll.gramgram.base.rsData.RsData;
import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.boundedContext.likeablePerson.form.AddForm;
import com.ll.gramgram.boundedContext.likeablePerson.service.LikeablePersonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/likeablePerson")
public class LikeablePersonController {

    private final Rq rq;
    private final LikeablePersonService likeablePersonService;

    @GetMapping("/add")
    public String add() {
        return "/user/likeablePerson/add";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/add")
    public String add(@Valid AddForm addForm) {
        RsData<LikeablePerson> createRsData = likeablePersonService.like(rq.getMember(), addForm.getUsername(), addForm.getAttractiveTypeCode());

        if (createRsData.isFail()) {
            return rq.historyBack(createRsData);
        }

        return rq.redirectWithMsg("/likeablePerson/list", createRsData);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/list")
    public String list(Model model) {

        InstaMember instaMember = rq.getMember().getInstaMember();
        if (instaMember != null) {
            List<LikeablePerson> likeablePeople = instaMember.getFromLikeablePeople();
            model.addAttribute("likeablePeople", likeablePeople);
        }
        return "/user/likeablePerson/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("delete/{id}")
    public String deletelike(@PathVariable("id") Long id) {

        log.info("삭제 판별");

        LikeablePerson likeablePerson = likeablePersonService.findById(id).orElse(null);

        if (likeablePerson == null) {
            log.info("취소된 호감 대상입니다.");
            return rq.historyBack("이미 취소된 호감대상입니다.");
        }

        if (!Objects.equals(rq.getMember().getInstaMember().getId(), likeablePerson.getFromInstaMember().getId())) {
            log.info("권한이 없습니다.");
            return rq.historyBack("권한 없음");
        }


        log.info("삭제 가능");
        RsData<LikeablePerson> rsData = likeablePersonService.deleteLikeablePerson(likeablePerson);

        if (rsData.isFail()) {
            log.info("실패");
            return rq.historyBack(rsData.getMsg());
        }

        log.info("삭제 완료");
        return rq.redirectWithMsg("/likeablePerson/list", rsData.getMsg());
    }
}
