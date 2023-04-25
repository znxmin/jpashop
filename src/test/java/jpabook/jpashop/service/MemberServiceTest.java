package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

//    @Autowired
//    EntityManager entityManager;

    @Test
//    @Rollback(false)
    public void 회원가입() throws Exception {
        // given
        Member member = new Member();
        member.setName("lim");

        // when
        Long savedId = memberService.join(member);

        // then
        // em.flush();
        // DB에 INSERT 쿼리가 실행되는 로그를 보고 싶으면
        // @Autowired로 EntityManager 의존성 받아와서 flush 강제 호출하면 된다

        assertEquals(member, memberRepository.findOne(savedId));
        // 같은 트랜잭션 안에서는 영속성 컨텍스트에 PK로 저장된 엔티티가 있을 경우
        // DB에서 조회해 온 엔티티를 사용하지 않고 버린다.
        // 즉 영속성 컨텍스트에 있는 엔티티를 다시 불러온 것이므로 같은 엔티티이다.
        // 다른 트랜잭션이었다면 == 비교는 실패함
    }

    @Test
    public void 중복_회원_예외() throws Exception {
        // given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        // when
        memberService.join(member1);

        // then
        assertThatThrownBy(() -> memberService.join(member2)).isInstanceOf(IllegalStateException.class);

        // fail("윗 라인에서 예외가 발생해야 한다.");
    }

}