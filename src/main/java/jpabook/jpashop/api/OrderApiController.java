package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.query.OrderFlatDto;
import jpabook.jpashop.repository.order.query.OrderItemQueryDto;
import jpabook.jpashop.repository.order.query.OrderQueryDto;
import jpabook.jpashop.repository.order.query.OrderQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

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

    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<OrderDto> collect = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());
        return collect;
    }

    @GetMapping("/api/v3/orders")
    public List<OrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithItem();
        List<OrderDto> collect = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());
        return collect;
    }

    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> ordersV3_page(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "100") int limit
    ) {
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);
        List<OrderDto> collect = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());
        return collect;
    }

    @GetMapping("/api/v4/orders")
    public List<OrderQueryDto> ordersV4() {
        List<OrderQueryDto> orderQueryDtos = orderQueryRepository.findOrderQueryDtos();
        return orderQueryDtos;
    }

    @GetMapping("/api/v5/orders")
    public List<OrderQueryDto> ordersV5() {
        List<OrderQueryDto> orderQueryDtos = orderQueryRepository.findAllByDto_optimization();
        return orderQueryDtos;
    }

    @GetMapping("/api/v6/orders")
    public List<OrderQueryDto> ordersV6() {
        List<OrderFlatDto> orderFlatDtos = orderQueryRepository.findAllByDto_flat();

        return orderFlatDtos.stream()
                .collect(Collectors.groupingBy
                        (o -> new OrderQueryDto(o.getOrderId(), o.getName(), o.getOrderDateTime(), o.getOrderStatus(), o.getAddress()),
                                Collectors.mapping(o -> new OrderItemQueryDto(o.getOrderId(), o.getItemName(), o.getOrderPrice(), o.getCount()),
                                        Collectors.toList())
                        ))
                .entrySet()
                .stream()
                .map(
                        e -> new OrderQueryDto(e.getKey().getOrderId(), e.getKey().getName(), e.getKey().getOrderDateTime(), e.getKey().getOrderStatus(), e.getKey().getAddress(), e.getValue())
                ).collect(Collectors.toList());
    }

    @Data
    static class OrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDateTime;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItems;

        public OrderDto(Order order) {
            this.orderId = order.getId();
            this.name = order.getMember().getName();
            this.orderDateTime = order.getOrderDateTime();
            this.orderStatus = order.getStatus();
            this.address = order.getDelivery().getAddress();
            this.orderItems = order.getOrderItems()
                    .stream()
                    .map(orderItem -> new OrderItemDto(orderItem))
                    .collect(Collectors.toList());
        }
    }

    @Data
    static class OrderItemDto {

        private String itemName;
        private int orderPrice;
        private int count;

        public OrderItemDto(OrderItem orderItem) {
            this.itemName = orderItem.getItem().getName();
            this.orderPrice = orderItem.getOrderPrice();
            this.count = orderItem.getCount();
        }
    }

}
