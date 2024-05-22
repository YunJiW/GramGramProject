package com.ll.gramgram.boundedContext.instaMember.service;

import com.ll.gramgram.base.rsData.RsData;
import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import com.ll.gramgram.boundedContext.instaMember.repository.InstaMemberRepository;
import com.ll.gramgram.boundedContext.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InstaMemberService {

    private final InstaMemberRepository instaMemberRepository;

    @Transactional
    public RsData<InstaMember> connect(Member member, String username, String gender) {
        if (findByUsername(username).isPresent()) {
            return RsData.of("F-5", "이미 다른 사용자와 연결되어있습니다.");
        }

        RsData<InstaMember> instaMemberRsData = create(username, gender);

        member.addInstaMember(instaMemberRsData.getData());

        return instaMemberRsData;

    }

    public Optional<InstaMember> findByUsername(String username) {
        return instaMemberRepository.findByUsername(username);
    }


    private RsData<InstaMember> create(String username, String gender) {
        InstaMember instaMember = InstaMember.builder()
                .username(username)
                .gender(gender)
                .build();

        instaMemberRepository.save(instaMember);

        return RsData.of("S-1", "인스타계정이 등록되었습니다.", instaMember);

    }
}
