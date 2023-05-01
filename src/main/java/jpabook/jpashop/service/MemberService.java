package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberJpaRepository;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberJpaRepository memberJpaRepository;

    /**
     * 회원가입
     *
     * @param member
     * @return id
     */
    @Transactional
    public Long join(Member member) {
        validateDuplicatedMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    /**
     * 중복 회원 검증
     *
     * @param member
     */
    private void validateDuplicatedMember(Member member) {
        // List<Member> members = memberRepository.findByName(member.getName());
        List<Member> members = memberJpaRepository.findByName(member.getName());
        if (!members.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    /**
     * 회원 전체 조회
     *
     * @return List<Member>
     */
    public List<Member> findMembers() {
        // return memberRepository.findAll();
        return memberJpaRepository.findAll();
    }

    /**
     * 회원 단건 조회
     *
     * @param id
     * @return Member
     */
    public Member findOne(Long id) {
        // return memberRepository.findOne(id);
        return memberJpaRepository.findById(id).get();
    }

    // 변경감지를 이용한 업데이트
    @Transactional
    public void update(Long id, String name) {
        // Member member = memberRepository.findOne(id);
        Member member = memberJpaRepository.findById(id).get();
        member.setName(name);
    }
}
