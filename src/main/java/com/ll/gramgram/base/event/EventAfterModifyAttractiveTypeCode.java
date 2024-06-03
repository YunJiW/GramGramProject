package com.ll.gramgram.base.event;

import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class EventAfterModifyAttractiveTypeCode extends ApplicationEvent {

    private final LikeablePerson likeablePerson;
    private final int oldAttractiveTypeCode;

    public EventAfterModifyAttractiveTypeCode(Object source, LikeablePerson likeablePerson, int oldAttractiveTypeCode) {
        super(source);
        this.likeablePerson = likeablePerson;
        this.oldAttractiveTypeCode = oldAttractiveTypeCode;
    }
}
