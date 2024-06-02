package com.ll.gramgram.boundedContext.instaMember.entity;

import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class InstaMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String username;

    private String gender;

    private long likesCountByGenderWomanAndAttractiveTypeCode1;
    private long likesCountByGenderWomanAndAttractiveTypeCode2;
    private long likesCountByGenderWomanAndAttractiveTypeCode3;
    private long likesCountByGenderManAndAttractiveTypeCode1;
    private long likesCountByGenderManAndAttractiveTypeCode2;
    private long likesCountByGenderManAndAttractiveTypeCode3;

    public Long getLikesCountByGenderWoman() {
        return likesCountByGenderWomanAndAttractiveTypeCode1 + likesCountByGenderWomanAndAttractiveTypeCode2 + likesCountByGenderWomanAndAttractiveTypeCode3;
    }

    public Long getLikesCountByGenderMan() {
        return likesCountByGenderManAndAttractiveTypeCode1 + likesCountByGenderManAndAttractiveTypeCode2 + likesCountByGenderManAndAttractiveTypeCode3;
    }

    public Long getLikesCountByAttractionTypeCode1() {
        return likesCountByGenderWomanAndAttractiveTypeCode1 + likesCountByGenderManAndAttractiveTypeCode1;
    }

    public Long getLikesCountByAttractionTypeCode2() {
        return likesCountByGenderWomanAndAttractiveTypeCode2 + likesCountByGenderManAndAttractiveTypeCode2;
    }

    public Long getLikesCountByAttractionTypeCode3() {
        return likesCountByGenderWomanAndAttractiveTypeCode3 + likesCountByGenderManAndAttractiveTypeCode3;
    }

    public Long getLikes() {
        return getLikesCountByGenderWoman() + getLikesCountByGenderMan();
    }

    @CreatedDate
    private LocalDateTime createDate;

    @LastModifiedDate
    private LocalDateTime modifyDate;


    @OneToMany(mappedBy = "fromInstaMember", cascade = CascadeType.ALL)
    @OrderBy("id desc")
    @Builder.Default
    private List<LikeablePerson> fromLikeablePeople = new ArrayList<>();

    @OneToMany(mappedBy = "toInstaMember", cascade = CascadeType.ALL)
    @OrderBy("id desc")
    @Builder.Default
    private List<LikeablePerson> toLikeablePeople = new ArrayList<>();


    public String getGenderDisplayName() {

        switch (getGender()) {
            case "W":
                return "여성";
            default:
                return "남성";
        }
    }

    public void increaseLikesCount(String gender, int attractiveTypeCode) {
        if (gender.equals("W") && attractiveTypeCode == 1) likesCountByGenderWomanAndAttractiveTypeCode1++;
        if (gender.equals("W") && attractiveTypeCode == 2) likesCountByGenderWomanAndAttractiveTypeCode2++;
        if (gender.equals("W") && attractiveTypeCode == 3) likesCountByGenderWomanAndAttractiveTypeCode3++;
        if (gender.equals("M") && attractiveTypeCode == 1) likesCountByGenderManAndAttractiveTypeCode1++;
        if (gender.equals("M") && attractiveTypeCode == 2) likesCountByGenderManAndAttractiveTypeCode2++;
        if (gender.equals("M") && attractiveTypeCode == 3) likesCountByGenderManAndAttractiveTypeCode3++;
    }

    public void decreaseLikesCount(String gender, int attractiveTypeCode) {
        if (gender.equals("W") && attractiveTypeCode == 1) likesCountByGenderWomanAndAttractiveTypeCode1--;
        if (gender.equals("W") && attractiveTypeCode == 2) likesCountByGenderWomanAndAttractiveTypeCode2--;
        if (gender.equals("W") && attractiveTypeCode == 3) likesCountByGenderWomanAndAttractiveTypeCode3--;
        if (gender.equals("M") && attractiveTypeCode == 1) likesCountByGenderManAndAttractiveTypeCode1--;
        if (gender.equals("M") && attractiveTypeCode == 2) likesCountByGenderManAndAttractiveTypeCode2--;
        if (gender.equals("M") && attractiveTypeCode == 3) likesCountByGenderManAndAttractiveTypeCode3--;
    }

    public void addFromLikeablePerson(LikeablePerson likeablePerson) {
        fromLikeablePeople.add(0, likeablePerson);
    }

    public void addToLikeablePerson(LikeablePerson likeablePerson) {
        toLikeablePeople.add(0, likeablePerson);
    }

    public void removeFromLikeablePerson(LikeablePerson likeablePerson) {
        fromLikeablePeople.removeIf(e -> e.equals(likeablePerson));
    }

    public void removeToLikeablePerson(LikeablePerson likeablePerson) {
        toLikeablePeople.removeIf(e -> e.equals(likeablePerson));
    }

    public void updateGender(String gender) {
        this.gender = gender;
    }
}
