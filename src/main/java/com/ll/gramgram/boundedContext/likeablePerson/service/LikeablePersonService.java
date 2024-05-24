package com.ll.gramgram.boundedContext.likeablePerson.service;

import com.ll.gramgram.base.rsData.RsData;
import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import com.ll.gramgram.boundedContext.instaMember.service.InstaMemberService;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.boundedContext.likeablePerson.repository.LikeablePersonRepository;
import com.ll.gramgram.boundedContext.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeablePersonService {

    private final LikeablePersonRepository likeablePersonRepository;

    private final InstaMemberService instaMemberService;

    public RsData<LikeablePerson> like(Member member, String username, int attractiveTypeCode) {
        InstaMember toInstaMember = instaMemberService.findByUsernameOrCreate(username);

        if (member.getInstaMember().getUsername().equals(username)) {
            return RsData.of("F-1", "본인은 호감상대로 저장할 수 없습니다.");
        }

        LikeablePerson likeablePerson = LikeablePerson.builder()
                .fromInstaMember(member.getInstaMember())
                .fromInstaMemberUsername(member.getInstaMember().getUsername())
                .toInstaMember(toInstaMember)
                .toInstaMemberUsername(toInstaMember.getUsername())
                .attractiveTypeCode(attractiveTypeCode)
                .build();

        likeablePersonRepository.save(likeablePerson);

        return RsData.of("S-1", "입력하신 인스타 유저(%s)를 호감상대로 등록되었습니다.".formatted(username), likeablePerson);

    }
}
