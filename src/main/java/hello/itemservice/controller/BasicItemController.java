package hello.itemservice.controller;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        // 상품 목록을 조회
        List<Item> items = itemRepository.findAll();
        // 조회한 상품 목록을 모델에 추가
        model.addAttribute("items", items);
        // "basic/items" 뷰 템플릿을 호출하여 상품 목록을 화면에 표시
        return "basic/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        // 경로 변수로부터 받은 itemId를 사용하여 상품을 조회합니다.
        Item item = itemRepository.findById(itemId);
        // 조회한 상품을 모델에 추가합니다. 이후 뷰 템플릿에서 사용할 수 있습니다.
        model.addAttribute("item", item);
        // "basic/item" 뷰 템플릿을 호출하여 상품 상세 정보를 화면에 표시합니다.
        return "basic/item";
    }


    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init() {
        // 애플리케이션 초기화 시, 테스트 데이터를 데이터베이스에 저장.
        itemRepository.save(new Item("testA", 10000, 10));
        itemRepository.save(new Item("testB", 20000, 20));
    }

    /*
    컨트롤러 로직은 itemRepository 에서 모든 상품을 조회한 다음에 모델에 담는다.
    그리고 뷰 템플릿을 호출한다.
     */


}
