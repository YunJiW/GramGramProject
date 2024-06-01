package com.ll.gramgram.boundedContext.likeablePerson.controller;

import com.ll.gramgram.base.Rq;
import com.ll.gramgram.base.rsData.RsData;
import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.boundedContext.likeablePerson.form.AddForm;
import com.ll.gramgram.boundedContext.likeablePerson.form.ModifyForm;
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
            log.info("호감표시 실패");
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
    @PostMapping("delete/{id}")
    public String deletelike(@PathVariable("id") Long id) {

        RsData rsData = likeablePersonService.deleteLikeablePerson(rq.getMember().getInstaMember(), id);

        if (rsData.isFail()) {
            log.info("삭제 불가능");
            return rq.historyBack(rsData.getMsg());
        }


        log.info("삭제 완료");
        return rq.redirectWithMsg("/likeablePerson/list", rsData.getMsg());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String modify(@PathVariable("id") Long id, Model model) {
        LikeablePerson likeablePerson = likeablePersonService.findById(id).orElseThrow();

        RsData modifyLikeData = likeablePersonService.canModifyLike(rq.getMember(), likeablePerson);

        if (modifyLikeData.isFail()) {
            rq.historyBack(modifyLikeData.getMsg());
        }

        model.addAttribute("likeablePerson", likeablePerson);

        return "/user/likeablePerson/modify";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("modify/{id}")
    public String modifyLike(@PathVariable("id") Long id, @Valid ModifyForm modifyForm) {

        RsData likeData = likeablePersonService.modifyLike(rq.getMember(), id, modifyForm.getAttractiveTypeCode());

        if (likeData.isFail()) {
            return rq.historyBack(likeData.getMsg());
        }

        return rq.redirectWithMsg("/likeablePerson/list", likeData.getMsg());

    }
}
