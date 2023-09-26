package com.shop.repository;

import com.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 데이터 조회하기 위한 쿼리 메소드를 모아둔 클래스
 * 쿼리 메소드명 문법 : find + (엔티티 이름) + By + 변수이름
 */
public interface ItemRepository extends JpaRepository<Item, Long>, QuerydslPredicateExecutor<Item> {
    List<Item> findByItemNm(String itemNm); // 상품명으로 데이터 조회

    List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail);  // 상품명과 상세 설명을 OR 조건으로 조회

    List<Item> findByPriceLessThan(Integer price);  // price보다 값이 작은 상품 데이터 조회

    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);  // 상품 가격 높은 순서대로(내림차순) 조회

    @Query("select i from Item i where i.itemDetail like %:itemDetail% order by i.price desc")  // Item으로부터 데이터를 select
    List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);    // @Param : 파라미터로 넘어온 값을 JPQL에 들어갈 변수로 지정. itemDetail 변수를 %:% 사이에 넣는다.

    @Query(value="select * from item i where i.item_detail like %:itemDetail% order by i.price desc", nativeQuery = true)   // nativeQuery : 기존의 데베에서 사용하던 쿼리를 그대로 사용가능. 데이터베이스와 독립적인 기존 쿼리 사용할 수 있음.
    List<Item> findByItemDetailByNative(@Param("itemDetail") String itemDetail);

}
