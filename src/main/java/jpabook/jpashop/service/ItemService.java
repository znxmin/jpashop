package jpabook.jpashop.service;

import jpabook.jpashop.domain.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long id) {
        return itemRepository.findOne(id);
    }

    // 영속성 컨텍스트에 존재하는 엔티티의 경우, 변경 감지(dirty checking)을 이용한 수정이 가능
    // 트랜잭션이 커밋되는 시점에 자동으로 플러시됨 (repository에 정의된 메서드 호출 불필요)
    // merge(병합)를 이용한 수정보다 변경 감지를 이용한 수정을 권장
    // merge는 전달되지 않은 속성은 null로 업데이트해버리기 때문에 위험
    @Transactional
    public void updateItem(Long itemId, String name, int price, int stockQuantity) {
        // PK가 존재한다는 것은 영속성 컨텍스트가 관리할 수 있음을 의미
        // 트랜잭션이 시작되었고, 영속성 컨텍스트에 엔티티가 올라가서
        // 준영속 상태에서 영속 상태로 변경됨
        Item item = itemRepository.findOne(itemId);
        item.setName(name);
        item.setPrice(price);
        item.setStockQuantity(stockQuantity);
    }
}
