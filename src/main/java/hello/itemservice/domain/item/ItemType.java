package hello.itemservice.domain.item;

public enum ItemType {
    BOOK("도서"), FOOD("음식"), ETC("기타");

    private final String description; // 설명

    ItemType(String description) { // 생성자
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
