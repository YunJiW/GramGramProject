package com.ll.gramgram.base;

import com.ll.gramgram.base.rsData.RsData;
import com.ll.gramgram.boundedContext.member.entity.Member;
import com.ll.gramgram.boundedContext.member.service.MemberService;
import com.ll.gramgram.boundedContext.notification.service.NotificationService;
import com.ll.gramgram.standard.util.Ut;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Date;


@Component
@RequestScope
public class Rq {

    private final MemberService memberService;
    private final HttpServletRequest req;
    private final HttpServletResponse resp;
    private final HttpSession session;

    private final NotificationService notificationService;

    private final User user;

    private Member member = null;

    public Rq(MemberService memberService, NotificationService notificationService, HttpServletRequest req, HttpServletResponse resp, HttpSession session) {
        this.memberService = memberService;
        this.notificationService = notificationService;
        this.req = req;
        this.resp = resp;
        this.session = session;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal() instanceof User) {
            this.user = (User) authentication.getPrincipal();
        } else {
            this.user = null;
        }


    }

    public boolean isLogin() {
        return user != null;
    }

    public boolean isLogout() {
        return !isLogin();
    }

    public Member getMember() {
        if (isLogout()) return null;

        if (member == null) {
            member = memberService.findByUsername(user.getUsername()).orElseThrow();
        }
        return member;
    }

    public String historyBack(String msg) {
        String referer = req.getHeader("referer");
        String key = "historyBackErrorMsg___" + referer;
        req.setAttribute("localStorageKeyAboutHistoryBackErrorMsg", key);
        req.setAttribute("historyBackErrorMsg", msg);
        return "common/js";
    }

    public String historyBack(RsData rsData) {
        return historyBack(rsData.getMsg());
    }

    public String redirectWithMsg(String url, RsData rsData) {
        return redirectWithMsg(url, rsData.getMsg());
    }

    public String redirectWithMsg(String url, String msg) {
        return "redirect:" + urlWithMsg(url, msg);
    }

    private String urlWithMsg(String url, String msg) {
        return Ut.url.modifyQueryParam(url, "msg", msgWithTtl(msg));

    }

    private String msgWithTtl(String msg) {

        return Ut.url.encode(msg) + ";ttl=" + new Date().getTime();
    }

    public boolean hasUnreadNotifications() {
        if (isLogout()) return false;

        Member actor = getMember();

        if(!actor.hasConnectedInstaMember()){
            return false;
        }


        return notificationService.countUnreadNotificationsByToInstaMember(getMember().getInstaMember());
    }
}
