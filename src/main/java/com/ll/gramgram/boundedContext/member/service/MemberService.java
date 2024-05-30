package com.ll.gramgram.boundedContext.member.service;


import com.ll.gramgram.base.rsData.RsData;
import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import com.ll.gramgram.boundedContext.member.entity.Member;
import com.ll.gramgram.boundedContext.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.awt.datatransfer.Clipboard;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public RsData<Member> join(String username, String password) {

        return join("GRAMGRAM",username,password);
    }

    public RsData<Member> join(String ProviderTypeCode,String username,String password){
        if (findByUsername(username).isPresent()) {
            return RsData.of("F-1", "해당 아이디(%s)는 사용중입니다.".formatted(username));
        }

        if(StringUtils.hasText(password)){
            password = passwordEncoder.encode(password);
        }

        Member member = Member.builder()
                .ProviderTypeCode(ProviderTypeCode)
                .username(username)
                .password(password)
                .build();

        memberRepository.save(member);


        return RsData.of("S-1", "회원가입이 완료되었습니다.", member);
    }

    @Transactional(readOnly = true)
    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    @Transactional
    public void UpdateInstaMember(Member member, InstaMember instaMember) {
        member.addInstaMember(instaMember);
        memberRepository.save(member);

    }

    public RsData<Member> whenSocialLogin(String providerTypeCode, String username) {
        Optional<Member> findMember = findByUsername(username);

        if(findMember.isPresent()){
            return RsData.of("S-1","로그인 되었습니다.", findMember.get());
        }

        return join(providerTypeCode,username,"");
    }
}
