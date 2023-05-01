package jpabook.jpashop.api;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order : all) {
            // Order 엔티티와 @ManyToOne 연관관계 가진 Member, Delivery
            // Lazy Loading 강제 초기화
            order.getMember().getName();
            // Order 엔티티와 @OneToOne 연관관계 가진 Delivery
            order.getDelivery().getAddress();
            // Order 엔티티와 @OneToMany 연관관계 가진 OrderItem
            List<OrderItem> orderItems = order.getOrderItems();
            // OrderItem 엔티티와 @ManyToOne 연관관계 가진 Item
            orderItems.stream().forEach(o -> o.getItem().getName());
        }
        return all;
    }
}
