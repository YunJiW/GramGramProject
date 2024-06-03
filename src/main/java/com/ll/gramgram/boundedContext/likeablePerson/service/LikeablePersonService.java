package com.ll.gramgram.boundedContext.likeablePerson.service;

import com.ll.gramgram.base.appConfig.AppConfig;
import com.ll.gramgram.base.event.EventAfterLike;
import com.ll.gramgram.base.event.EventBeforeCancelLike;
import com.ll.gramgram.base.rsData.RsData;
import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import com.ll.gramgram.boundedContext.instaMember.service.InstaMemberService;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.boundedContext.likeablePerson.repository.LikeablePersonRepository;
import com.ll.gramgram.boundedContext.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class LikeablePersonService {

    private final LikeablePersonRepository likeablePersonRepository;

    private final InstaMemberService instaMemberService;

    private final ApplicationEventPublisher publisher;

    @Transactional
    public RsData<LikeablePerson> like(Member member, String username, int attractiveTypeCode) {
        InstaMember fromInstaMember = member.getInstaMember();
        InstaMember toInstaMember = instaMemberService.findByUsernameOrCreate(username).getData();

        if (member.getInstaMember().getUsername().equals(username)) {
            return RsData.of("F-1", "본인은 호감상대로 저장할 수 없습니다.");
        }

        if (!LikeSizeCheck(fromInstaMember)) {
            return RsData.of("F-1", "%d명 이상에게 호감을 표시할 수 없습니다.".formatted(AppConfig.getLikeablePersonFromMax()));

        }
        LikeablePerson searchLikeablePerson = canLike(fromInstaMember, toInstaMember);
        if (searchLikeablePerson != null) {
            if (searchLikeablePerson.getAttractiveTypeCode() == attractiveTypeCode) {
                log.info("중복 호감 표시");
                return RsData.of("F-1", "이미 호감을 표시한 상대입니다.");
            }

            searchLikeablePerson.modifyAttractiveTypCode(attractiveTypeCode);
            return RsData.of("S-2", "호감 수정 완료");


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

        publisher.publishEvent(new EventAfterLike(this, likeablePerson));


        return RsData.of("S-1", "입력하신 인스타 유저(%s)를 호감상대로 등록되었습니다.".formatted(username), likeablePerson);

    }

    private static boolean LikeSizeCheck(InstaMember fromInstaMember) {
        return fromInstaMember.getFromLikeablePeople().size() <= AppConfig.getLikeablePersonFromMax();
    }

    private LikeablePerson canLike(InstaMember fromInstaMember, InstaMember toInstaMember) {
        List<LikeablePerson> fromLikeablePeople = fromInstaMember.getFromLikeablePeople();

        //호감 표시를 한적이 있는지.
        LikeablePerson likeablePerson = fromLikeablePeople.stream()
                .filter(e -> e.getToInstaMember().getUsername().equals(toInstaMember.getUsername()))
                .findFirst()
                .orElse(null);

        if (likeablePerson != null) {
            return likeablePerson;
        }

        return null;

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

        publisher.publishEvent(new EventBeforeCancelLike(this, likeablePerson));

        return RsData.of("S-1", "%s 님을 호감 취소하엿습니다.".formatted(tolikeablePersonUsername));
    }

    public Optional<LikeablePerson> findById(Long id) {

        return likeablePersonRepository.findById(id);
    }

    @Transactional
    public RsData modifyLike(Member member, Long id, int attractiveTypeCode) {

        LikeablePerson likeablePerson = likeablePersonRepository.findById(id).orElseThrow();

        RsData rsData = canModifyLike(member, likeablePerson);

        if (rsData.isFail()) {
            return rsData;
        }

        likeablePerson.modifyAttractiveTypCode(attractiveTypeCode);


        return RsData.of("S-2", "호감 사유 수정이 완료되었습니다.");


    }

    public RsData canModifyLike(Member member, LikeablePerson likeablePerson) {
        if (!member.hasConnectedInstaMember()) {
            return RsData.of("F-1", "먼저 본인의 인스타그램 아이디를 입력해주세요.");
        }

        InstaMember fromInstaMember = member.getInstaMember();

        if (!Objects.equals(fromInstaMember.getId(), likeablePerson.getFromInstaMember().getId())) {
            log.info("권한이 없습니다.");
            return RsData.of("F-1", "권한이 없습니다.");
        }


        return RsData.of("S-1", "수정 가능");
    }
}
