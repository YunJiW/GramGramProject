package com.ll.gramgram.boundedContext.likeablePerson.service;

import com.ll.gramgram.base.rsData.RsData;
import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import com.ll.gramgram.boundedContext.instaMember.service.InstaMemberService;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.boundedContext.likeablePerson.repository.LikeablePersonRepository;
import com.ll.gramgram.boundedContext.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class LikeablePersonService {

    private final LikeablePersonRepository likeablePersonRepository;

    private final InstaMemberService instaMemberService;

    public RsData<LikeablePerson> like(Member member, String username, int attractiveTypeCode) {
        InstaMember fromInstaMember = member.getInstaMember();
        InstaMember toInstaMember = instaMemberService.findByUsernameOrCreate(username);

        if (member.getInstaMember().getUsername().equals(username)) {
            return RsData.of("F-1", "본인은 호감상대로 저장할 수 없습니다.");
        }

        LikeablePerson likeablePerson = LikeablePerson.builder()
                .fromInstaMember(fromInstaMember)
                .fromInstaMemberUsername(fromInstaMember.getUsername())
                .toInstaMember(toInstaMember)
                .toInstaMemberUsername(toInstaMember.getUsername())
                .attractiveTypeCode(attractiveTypeCode)
                .build();

        likeablePersonRepository.save(likeablePerson);

        fromInstaMember.addFromLikeablePerson(likeablePerson);
        toInstaMember.addToLikeablePerson(likeablePerson);


        return RsData.of("S-1", "입력하신 인스타 유저(%s)를 호감상대로 등록되었습니다.".formatted(username), likeablePerson);

    }

    @Transactional
    public RsData deleteLikeablePerson(InstaMember instaMember, Long id) {
        LikeablePerson likeablePerson = findById(id).orElse(null);

        if (likeablePerson == null) {
            return RsData.of("F-1", "취소된 호감 대상입니다.");
        }

        if (!Objects.equals(instaMember.getId(), likeablePerson.getFromInstaMember().getId())) {
            log.info("권한이 없습니다.");
            return RsData.of("F-1", "권한이 없습니다.");
        }
        String tolikeablePersonUsername = likeablePerson.getToInstaMember().getUsername();

        likeablePersonRepository.delete(likeablePerson);

        likeablePerson.getFromInstaMember().removeFromLikeablePerson(likeablePerson);
        likeablePerson.getToInstaMember().removeToLikeablePerson(likeablePerson);

        return RsData.of("S-1", "%s 님을 호감 취소하엿습니다.".formatted(tolikeablePersonUsername));
    }

    public Optional<LikeablePerson> findById(Long id) {

        return likeablePersonRepository.findById(id);
    }
}
