package hello.itemservice.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data // 예제이므로 이 어노테이션 사용
@NoArgsConstructor
public class Item {
    private Long id;
    private String itemName;
    // 아래 두 개는 null 일 수도 있기 때문에 Integer
    private Integer price;
    private Integer quantity;


    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}