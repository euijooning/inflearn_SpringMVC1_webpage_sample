package hello.itemservice.web.from;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/form/items")
@RequiredArgsConstructor
public class FormItemController {

    private final ItemRepository itemRepository;


    @GetMapping
    public String items(Model model) {
        // 상품 목록을 조회
        List<Item> items = itemRepository.findAll();
        // 조회한 상품 목록을 모델에 추가
        model.addAttribute("items", items);
        // "form/items" 뷰 템플릿을 호출하여 상품 목록을 화면에 표시
        return "form/items";
    }


    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        // 경로 변수로부터 받은 itemId를 사용하여 상품을 조회.
        Item item = itemRepository.findById(itemId);
        // 조회한 상품을 모델에 추가. 이후 뷰 템플릿에서 사용할 수 있다.
        model.addAttribute("item", item);
        // "form/item" 뷰 템플릿을 호출하여 상품 상세 정보를 화면에 표시.
        return "form/item";
    }


    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "form/addForm";
    }


    /**
     * 상품을 추가하는 메서드.
     *
     * @param item               추가할 상품 정보를 나타내는 객체
     * @param redirectAttributes Spring MVC 리다이렉트 시 데이터를 전달하기 위한 객체
     * @return 상품 추가 후 해당 상품 상세 정보 페이지로 리다이렉트
     */
    @PostMapping("/add")
    public String addItem(@ModelAttribute Item item, RedirectAttributes redirectAttributes) {
        // 잘못봄. 여기에 로그를 추가했어야 함...
        log.info("item.open={}", item.getOpen());

        // 상품 정보를 데이터베이스에 저장하고 저장된 상품 정보를 반환.
        Item savedItem = itemRepository.save(item);

        // 리다이렉트 시 URL에 파라미터를 추가하기 위해 RedirectAttributes를 사용.
        // "itemId" 파라미터에 저장된 상품의 ID를 추가.
        redirectAttributes.addAttribute("itemId", savedItem.getId());

        // "status" 파라미터를 추가하고 값을 true로 설정. (예: 성공적으로 상품을 추가한 상태를 나타냄)
        redirectAttributes.addAttribute("status", true);

        // 상품 추가 후 해당 상품의 상세 정보 페이지로 리다이렉트.
        return "redirect:/form/items/{itemId}";
    }



    /**
     * 지정된 ID를 가진 아이템의 수정 폼을 표시한다.
     *
     * @param itemId 수정할 아이템의 ID
     * @param model 뷰에 데이터를 담기 위한 모델
     * @return 수정 폼 뷰의 이름
     */
    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        // 아이템 레포지토리에서 아이템을 ID를 사용하여 검색.
        Item item = itemRepository.findById(itemId);

        // 검색한 아이템을 모델에 추가하여 뷰에서 표시할 수 있도록 함.
        model.addAttribute("item", item);

        // 수정 폼 뷰의 이름을 반환.
        return "form/editForm";
    }


    /**
     * 상품을 수정하는 메서드.
     *
     * @param itemId 수정할 상품의 ID
     * @param item   수정된 상품 정보를 나타내는 객체
     * @return 상품 수정 후 해당 상품 상세 정보 페이지로 리다이렉트
     */
    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        // 상품 수정 메서드를 호출하여 상품 정보를 업데이트 (itemRepository.update 메서드 사용)
        itemRepository.update(itemId, item);

        // 수정된 상품 정보를 포함하는 해당 상품의 상세 정보 페이지로 리다이렉트.
        return "redirect:/form/items/{itemId}";
    }

}
