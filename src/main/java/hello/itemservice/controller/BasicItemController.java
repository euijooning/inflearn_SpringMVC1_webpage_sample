package hello.itemservice.controller;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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


    @GetMapping("/add")
    public String addForm() {
        return "basic/addForm";
    }
    // ---------------------------------------------------------------
    // 주의. 실행 시 앞 매핑 주석처리해야 오류 나지 않는다.

    /**
     * @param itemName
     * @param price
     * @param quantity
     * @param model
     * @return
     */
//    @PostMapping("/add")
    public String addItemV1(@RequestParam String itemName,
                            @RequestParam int price,
                            @RequestParam Integer quantity,
                            Model model) {

        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);
        itemRepository.save(item);

        model.addAttribute("item", item);

        return "basic/item";
    }


    /**
     * @ModelAttribute("item") Item item
     * model.addAttribute("item", item); 자동 추가
     */
//    @PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item, Model model) {
        itemRepository.save(item);
        //model.addAttribute("item", item); //자동 추가, 생략 가능
        return "basic/item";
    }


    /**
     * @ModelAttribute name 생략 가능
     * model.addAttribute(item); 자동 추가, 생략 가능
     * 생략시 model에 저장되는 name은 클래스명 첫글자만 소문자로 등록 Item -> item
     */
//    @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item) {
        itemRepository.save(item);
        return "basic/item";
    }


    /**
     * @ModelAttribute 자체 생략 가능
     * model.addAttribute(item) 자동 추가
     */
    @PostMapping("/add")
    public String addItemV4(Item item) {
        itemRepository.save(item);
        return "basic/item";
    }

    /**
     * 상품 수정 폼을 불러오는 메서드.
     *
     * @param itemId 수정할 상품의 ID
     * @param model  Spring MVC 모델 객체. 뷰로 데이터를 전달하기 위해 사용됨.
     * @return 상품 수정 폼 템플릿 이름
     */
    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        // 상품 ID를 사용하여 데이터베이스에서 해당 상품 정보를 조회.
        Item item = itemRepository.findById(itemId);

        // 조회한 상품 정보를 모델에 추가하여 뷰로 전달.
        model.addAttribute("item", item);

        // 상품 수정 폼 템플릿 이름을 반환.
        return "basic/editForm";
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
        return "redirect:/basic/items/{itemId}";
    }


    /**
     * @param item
     * @return PRG - Post/Redirect/Get
     */
    @PostMapping("/add")
    public String addItemV5(Item item) {
        itemRepository.save(item);
        return "redirect:/basic/items/" + item.getId();
    }

    /**
     * 상품을 추가하는 메서드.
     *
     * @param item               추가할 상품 정보를 나타내는 객체
     * @param redirectAttributes Spring MVC 리다이렉트 시 데이터를 전달하기 위한 객체
     * @return 상품 추가 후 해당 상품 상세 정보 페이지로 리다이렉트
     */
    @PostMapping("/add")
    public String addItemV6(Item item, RedirectAttributes redirectAttributes) {
        // 상품 정보를 데이터베이스에 저장하고 저장된 상품 정보를 반환.
        Item savedItem = itemRepository.save(item);

        // 리다이렉트 시 URL에 파라미터를 추가하기 위해 RedirectAttributes를 사용.
        // "itemId" 파라미터에 저장된 상품의 ID를 추가.
        redirectAttributes.addAttribute("itemId", savedItem.getId());

        // "status" 파라미터를 추가하고 값을 true로 설정. (예: 성공적으로 상품을 추가한 상태를 나타냄)
        redirectAttributes.addAttribute("status", true);

        // 상품 추가 후 해당 상품의 상세 정보 페이지로 리다이렉트.
        return "redirect:/basic/items/{itemId}";
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
