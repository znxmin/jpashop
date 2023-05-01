package jpabook.jpashop.service;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문
     *
     * @param memberId
     * @param itemId
     * @param count
     * @return orderId
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        // 엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        // 배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        // 주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);
        // OrderItem 여러 개 생성해서 주문 생성 가능

        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        // 주문 저장
        orderRepository.save(order);
        // Order 엔티티에 Delivery, OrderItems Cascade 옵션 지정되어 있어서
        // Order 엔티티만 영속성 컨텍스트에 flush 해주면 Delivery, OrderItems 엔티티도 알아서 DB에 반영됨

        return order.getId();
    }

    /**
     * 주문 취소
     *
     * @param orderId
     */
    @Transactional
    public void cancelOrder(Long orderId) {
        // 주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        // 주문 취소
        order.cancel();
        // JPA의 변경 감지 기능으로 SQL 업데이트 쿼리를 날릴 필요 없음
    }

    /**
     * 검색
     *
     * @param orderSearch
     * @return List<Order>
     */
    public List<Order> findOrders(OrderSearch orderSearch) {
        // return orderRepository.findAllByString(orderSearch);
        return orderRepository.findAllQueryDsl(orderSearch);
    }
}
