package com.ll.gramgram.boundedContext.notification.entity;

import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@ToString(callSuper = true)
public class Notification {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDateTime CreateDate;

    @LastModifiedDate
    private LocalDateTime modifyDate;

    private LocalDateTime readDate;

    @ManyToOne
    @ToString.Exclude
    private InstaMember instaMember;

    @ManyToOne
    @ToString.Exclude
    private InstaMember fromInstaMember;

    private String typeCode;

    private String oldGender;

    private int oldAttractiveTypeCode;

    private String newGender;

    private int newAttractiveTypeCode;
}
