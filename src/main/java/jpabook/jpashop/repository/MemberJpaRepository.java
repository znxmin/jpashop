package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {
    List<Member> findByName(String name);
    // select m from Member m where m.name = :name
    // 인터페이스만 상속해주면 구현체를 자동으로 생성해준다.
}
