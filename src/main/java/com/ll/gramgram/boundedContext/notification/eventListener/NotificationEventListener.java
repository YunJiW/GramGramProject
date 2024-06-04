package com.ll.gramgram.boundedContext.notification.eventListener;

import com.ll.gramgram.base.event.EventAfterLike;
import com.ll.gramgram.base.event.EventAfterModifyAttractiveTypeCode;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.boundedContext.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
@Slf4j
public class NotificationEventListener {

    private final NotificationService notificationService;

    //누군가가 호감 표시시 이벤트 발생
    @EventListener
    public void listen(EventAfterLike event) {
        log.info("호감표시 발생 ={}", event);
        LikeablePerson likeablePerson = event.getLikeablePerson();
        notificationService.makeLike(likeablePerson);

    }

    //호감 사유 변경시 발생
    @EventListener
    public void listen(EventAfterModifyAttractiveTypeCode event) {
        log.info("호감 수정 발생 = {}", event);
        LikeablePerson likeablePerson = event.getLikeablePerson();

        notificationService.makeModifyAttractive(likeablePerson,event.getOldAttractiveTypeCode());

    }
}
