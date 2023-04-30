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
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());

        for (Order order : all) {
            order.getMember().getName(); // Lazy Loading 강제 초기화
            order.getDelivery().getAddress(); // Lazy Loading 강제 초기화
            for (OrderItem orderItem : order.getOrderItems()) {
                orderItem.getOrderPrice(); // Lazy Loading 강제 초기화
                orderItem.getItem().getCategories(); // Lazy Loading 강제 초기화
            }
        }
        return all;
    }
}