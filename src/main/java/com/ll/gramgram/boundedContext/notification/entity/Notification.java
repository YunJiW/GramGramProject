package com.ll.gramgram.boundedContext.notification.entity;

import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import jakarta.persistence.*;
import lombok.*;
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
@AllArgsConstructor
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
    private InstaMember toInstaMember;

    @ManyToOne
    @ToString.Exclude
    private InstaMember fromInstaMember;

    private String typeCode;

    private String oldGender;

    private int oldAttractiveTypeCode;

    private String newGender;

    private int newAttractiveTypeCode;

    public boolean isRead() {
        return readDate != null;
    }

    public void markAsRead(){
        readDate = LocalDateTime.now();
    }
}
