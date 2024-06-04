package com.ll.gramgram.boundedContext.notification.controller;

import com.ll.gramgram.base.Rq;
import com.ll.gramgram.boundedContext.notification.entity.Notification;
import com.ll.gramgram.boundedContext.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/notification")
@Slf4j
public class NotificationController {

    private final Rq rq;

    private final NotificationService notificationService;

    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    public String showList(Model model) {

        if (!rq.getMember().hasConnectedInstaMember()) {
            return rq.redirectWithMsg("/notification/list", "인스타 인증을 해야 알림을 볼수 있습니다.");
        }

        List<Notification> notifications = notificationService.findByToInstaMember(rq.getMember().getInstaMember());

        model.addAttribute("notifications", notifications);

        return "/user/notification/list";

    }
}
