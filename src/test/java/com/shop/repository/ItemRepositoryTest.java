package com.shop.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.constant.ItemSellStatus;
import com.shop.entity.Item;
import com.shop.entity.QItem;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.thymeleaf.util.StringUtils;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;


@SpringBootTest // 통합 테스트 어노테이션. 모든 빈을 IoC 컨테이너에 등록
@TestPropertySource(locations = "classpath:application-test.properties")  // 같은 설정이 있다면 app보다 test.properties에 우선순위 둠
class ItemRepositoryTest {

    @PersistenceContext
    EntityManager em;   // 영속성 컨텍스트를 사용하기 위해 @PersistenceContext 어노테이션을 이용해 EntityManager 빈 주입

    @Autowired  // ItemRepository 사용하기 위해 의존성 주입
    ItemRepository itemRepository;

    @Test
    @DisplayName("상품 저장 테스트")   // Junit5 어노테이션. 코드 실행 시 지정한 테스트명 노출
    public void createItemTest() {
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setRegTime(LocalDateTime.now());
        Item savedItem = itemRepository.save(item);
        System.out.println(savedItem.toString());
    }

    /*
     * 테스트 데이터 생성(10개 상품 저장)하는 메소드
     */
    public void createItemList() {
        for (int i = 1; i <= 10; i++) {
            Item item = new Item();
            item.setItemNm("테스트 상품" + i);   // 상품명
            item.setPrice(10000 + i);
            item.setItemDetail("테스트 상품 상세 설명" + i); // 상품상세설명
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            Item savedItem = itemRepository.save(item);
        }
    }

    @Test
    @DisplayName("상품명 조회 테스트")
    public void findByItemNmTest() {
        this.createItemList();  // 상품 제작
        List<Item> itemList = itemRepository.findByItemNm("테스트 상품1");   // "테스트 상품1" 이라는 상품명 있으면 리스트에 할당
        for (Item item : itemList) {
            System.out.println(item.toString());    // 조회 결과 얻은 item 객체 출력
        }
    }

    @Test
    @DisplayName("상품명, 상품상세설명 or 테스트")
    public void findByItemOrItemDetailTest() {
        this.createItemList();  // 상품 제작
        List<Item> itemList = itemRepository.findByItemNmOrItemDetail("테스트 상품1", "테스트 상품 상세 설명5");
        for (Item item : itemList) {
            System.out.println(item.toString());    // 두개의 상품 출력
        }

    }

    @Test
    @DisplayName("가격 LessThan 테스트")
    public void findByPriceLessThanTest() {
        this.createItemList();
        List<Item> itemList = itemRepository.findByPriceLessThan(10005);
        for (Item item : itemList) {
            System.out.println(item.toString());    // 10001, 10002, 10003, 10004 출력
        }
    }

    @Test
    @DisplayName("가격 내림차순 조회 테스트")
    public void findByPriceLessThanOrderByPriceDesc() {
        this.createItemList();
        List<Item> itemList = itemRepository.findByPriceLessThanOrderByPriceDesc(10005);
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("@Query를 이용한 상품 조회 테스트")
    public void findByItemDetailTest() {
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemDetail("테스트 상품 상세 설명");
        for (Item item : itemList) {
            System.out.println(item.toString());    // "테스트 상품 상세 설명"을 상품상세설명으로 갖고 있는 아이템을 가격이 높은 순으로 조회
        }
    }

    @Test
    @DisplayName("nativeQuery 속성을 이용한 상품 조회 테스트")
    public void findByItemDetailByNative() {
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemDetailByNative("테스트 상품 상세 설명");
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("Querydsl 조회 테스트1")
    public void queryDslTest() {
        this.createItemList();
        JPAQueryFactory queryFactory = new JPAQueryFactory(em); // JPAQueryFactory 사용해서 쿼리 동적 생성. 생성자 파라미터로는 EntityManager 객체
        QItem qItem = QItem.item;   // Querydsl을 통해 쿼리를 생성하기 위해 플러그인으로 자동 생성된 QItem 객체 이용

        // 자바 소스코드지만 SQL문과 비슷하게 작성할 수 있다!
        JPAQuery<Item> query = queryFactory.selectFrom(qItem)
                .where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))
                .where(qItem.itemDetail.like("%" + "테스트 상품 상세 설명" + "%"))
                .orderBy(qItem.price.desc());

        List<Item> itemList = query.fetch();    // fetch : JPAQuery 메소드. 쿼리 결과를 리스트로 반환한다.

        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    public void createItemList2() {
        for (int i = 1; i <= 5; i++) {
            Item item = new Item();
            item.setItemNm("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("테스트 상풍 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            itemRepository.save(item);
        }
        for (int i = 6; i <= 10; i++) {
            Item item = new Item();
            item.setItemNm("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("테스트 상품 상세 설명" + i);
            item.setStockNumber(0);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            itemRepository.save(item);
        }
    }

    /*
     * 상품 데이터 만드는 메소드. 1~5는 상품의 판매상태가 SELL(판매중)이고, 6~10은 SOLD_OUT으로 세팅
     */
    @Test
    @DisplayName("상품 Querydsl 조회 테스트 2")
    public void queryDslTest2() {
        this.createItemList2();

        BooleanBuilder booleanBuilder = new BooleanBuilder();   // BooleanBuilder : 쿼리에 들어갈 조건을 만들어주는 빌더. 메소드 체인 형식으로 사용 가능
        QItem item = QItem.item;
        
        String itemDetail = "테스트 상품 상세 설명";
        int price = 10003;
        String itemSellStat = "SELL";

        booleanBuilder.and(item.itemDetail.like("%" + itemDetail + "%"));   // 필요한 상품을 조회하는데 필요한 and 조건 추가
        booleanBuilder.and(item.price.gt(price));

        if (StringUtils.equals(itemSellStat, ItemSellStatus.SELL)) {    // 상태가 SELL일 때만 빌더에 판매 상태 조건을 동적으로 추가
            booleanBuilder.and(item.itemSellStatus.eq(ItemSellStatus.SELL));
        }

        // PageRequest.of : 데이터를 페이징해 조회. page: 조회할 페이지 번호, size: 한 페이지 당 조회할 데이터 개수
        Pageable pageable = PageRequest.of(0, 5);
        Page<Item> itemPagingResult = itemRepository.findAll(booleanBuilder, pageable);
        System.out.println("total elements : " + itemPagingResult.getTotalElements());

        List<Item> resultItemList = itemPagingResult.getContent();
        for (Item resultItem : resultItemList) {
            System.out.println(resultItem.toString());
        }
    }
}