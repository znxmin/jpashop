package jpabook.jpashop.repository.order.simplequery;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {

    private final EntityManager em;

    public List<OrderSimpleQueryDto> findOrderDtos() {
        return em.createQuery(
                "select new jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto(o.id, m.name, o.orderDateTime, o.status, d.address)" +
                        " from Order o" +
                        " join o.member m" +
                        " join o.delivery d", OrderSimpleQueryDto.class
        ).getResultList();
    }
    // createQuery 메서드의 두 번째 인자로 DTO를 넘기려면 select new DTO생성자() 필요
    // DTO의 생성자 인자로 엔티티를 그대로 넘겨줄 수 없다 (new DTO(o) 불가능)
    // fetch join과 비교했을 때 필요한 속성만 조회할 수 있다.
    // fetch join은 DTO를 조회할 때는 사용할 수 없다.
}
