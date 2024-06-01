package com.ll.gramgram.base.appConfig;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {


    @Getter
    private static long likeablePersonFromMax;


    @Value("${custom.likeablePerson.from.max}")
    public void setLikeablePersonFromMax(long likeablePersonFromMax) {
        AppConfig.likeablePersonFromMax = likeablePersonFromMax;
    }
}
