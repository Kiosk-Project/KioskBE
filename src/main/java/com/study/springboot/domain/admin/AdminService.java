package com.study.springboot.domain.admin;


import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.springboot.datas.AdminMessage;
import com.study.springboot.datas.Message;
import com.study.springboot.datas.MessageService;
import com.study.springboot.domain.orderSystem.OrderItem;
import com.study.springboot.domain.orderSystem.OrderList;
import com.study.springboot.domain.orderSystem.dto.*;
import com.study.springboot.domain.orderSystem.repository.OrderItemRepository;
import com.study.springboot.domain.orderSystem.repository.OrderListRepository;
import com.study.springboot.domain.product.Product;
import com.study.springboot.domain.product.QProduct;
import com.study.springboot.domain.product.dto.ProductDto;
import com.study.springboot.domain.product.dto.RequestProductEditDto;
import com.study.springboot.domain.product.repository.ProductRepository;
import com.study.springboot.domain.product.service.ProductService;
import com.study.springboot.domain.user.User;
import com.study.springboot.domain.user.dto.UserDto;
import com.study.springboot.domain.user.repository.UserRepository;
import com.study.springboot.enumeration.OrderListStatus;
import com.study.springboot.enumeration.ProductCategory;
import com.study.springboot.enumeration.SearchCategory;
import com.study.springboot.enumeration.error.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final ProductRepository productRepository;
    private final ProductService productService;
    private final MessageService messageService;
    private final OrderListRepository orderListRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final JPAQueryFactory queryFactory;


    /*
    User
     */

    //회원 목록 조회
    @Transactional
    public Page<UserDto> getUserList(String type, String text, int page){
        PageRequest pageRequest = PageRequest.of(page, 5, Sort.by("userJoinDate").descending());

        Page<UserDto> userDtoPage = null;
        if(type == null){
            userDtoPage = userRepository.findUsersByUserDelYn(false, pageRequest).map(UserDto::new);
        } else if(type.equals("id")){
            userDtoPage = userRepository.findByUserIdContains(text, pageRequest).map(UserDto::new);
        } else if(type.equals("name")){
            userDtoPage = userRepository.findByUserNameContains(text, pageRequest).map(UserDto::new);
        }

        return userDtoPage;

    }

    //회원 상세 조회
    public Optional<User> getUser(Long id){
        Optional<User> optional = userRepository.findById(id);
        return optional;

    }

    //회원 삭제
    @Transactional
    public Optional<User> deleteUser(String userId){
        Optional<User> optional = userRepository.findByUserId(userId);
        if(optional.isPresent())
        {
            User user = optional.get();
            user = user.deleteUser();
            userRepository.save(user);
            return Optional.ofNullable(user);
        }

        return Optional.ofNullable(null);
    }

    //회원 수정
    @Transactional
    public Optional<User> updateUser(Long id, UserDto dto){
        Optional<User> optional = userRepository.findById(id);

        if(optional.isEmpty()){
            return Optional.empty();
        }

        optional.get().update(dto);
        return optional;

    }


    /*
    Product
     */

    //상품 상세 조회
    @Transactional(readOnly = true)
    public Optional<Product> findById(Long id){
        return productRepository.findById(id);
    }

    //상품 목록 조회
    @Transactional(readOnly = true)
    public Page<ProductDto> findAllProduct(int page, String orderBy){
        PageRequest pageRequest = PageRequest.of(page, 5, Sort.by(orderBy).ascending());
        System.out.println("orderBy" + orderBy);

        Page<ProductDto> productDtoPage =  productRepository.findProductByProductDelYn(false, pageRequest).map(ProductDto::new);
        return productDtoPage;
    }

    //상품 삭제
    @Transactional
    public Optional<ProductDto> removeProduct(String productCode){
        Optional<Product> optional = productService.findByCode(productCode);

        if(optional.isEmpty()){
            return Optional.empty();
        }

        Product product = optional.get();
        product.delete();

        return Optional.ofNullable(new ProductDto(product));
    }

    //상품코드로 상품 조회
    @Transactional(readOnly = true)
    Optional<Product> findProductByCode(String code){
        return productRepository.findProductByProductCode(code);
    }

    //상품 수정
    @Transactional
    Optional<ProductDto> editProduct(RequestProductEditDto dto){
        Optional<Product> optional = productRepository.findProductByProductCode(dto.getProductCode());

        if(optional.isEmpty()){
            return Optional.empty();
        }

        Product product = optional.get();
        Long id = product.getId();

        ProductCategory newCategory= null;
        for(ProductCategory p : ProductCategory.values()){
            if(p.getValue().equals(dto.getCategory())){
                newCategory = p;
                break;
            }
        }

        product = dto.toEntity(id);
        Product result = productRepository.save(product);

        return Optional.ofNullable(new ProductDto(result));

    }

    //상품 검색
    @Transactional(readOnly = true)
    List<Product> findProductsBy(final SearchCategory searchCategory, final ProductCategory productCategory, final String searchKeyword, final int page, final int pageSize){


        QProduct product = QProduct.product;
        List<Product> list = queryFactory.selectFrom(product)
                .where(
                        product.category.stringValue().eq(productCategory.getValue())
                                .and(
                                        product.productCode.contains(searchKeyword)
                                                .or(product.productName.contains(searchKeyword))
                                )
                )
                .fetch();

        return list;

    }

//    private BooleanExpression eqSearchCategory(SearchCategory)


    /*
    OrderList
     */

    //주문 목록 조회
    @Transactional
    public Page<OrderListDto> getOrderList(String type, String text, int page){
        PageRequest pageRequest = PageRequest.of(page, 5, Sort.by("orderListTime").descending());
        int total = 0;

        Page<OrderListDto> orderListDtoPage=null;
        if(type==null){
            orderListDtoPage = orderListRepository.findOrderListByOrderListDelYn(false, pageRequest).map(OrderListDto::new);
            total = orderListRepository.findAll().size();
        } else if(type.equals("status")){
            OrderListStatus status = OrderListStatus.valueOf(text.toUpperCase());
            orderListDtoPage = orderListRepository.findByOrderListStatus(status, pageRequest).map(OrderListDto::new);
            total = orderListRepository.findByOrderListStatus(status).size();
        }

        return orderListDtoPage;
    }

    //주문 상세 조회
    public Optional<OrderListDetailDto> getOrder(Long id){
        Optional<OrderList> optional = orderListRepository.findById(id);
        if(optional.isEmpty()) return Optional.empty();

        OrderListDetailDto orderListDetailDto = OrderListDetailDto.builder()
                .orderList(optional.get())
                .items(optional.get().getOrderItems())
                .build();

        return Optional.ofNullable(orderListDetailDto);
    }

    //주문 삭제
    @Transactional
    public Optional<OrderList> deleteOrderList(Long id){
        Optional<OrderList> optional =orderListRepository.findById(id);
        if(optional.isPresent()){
            OrderList list = optional.get();
            list.delete();
            return optional;
        }
        return Optional.ofNullable(null);

    }

    //주문 수정
    @Transactional
    public Optional<OrderList> updateOrderList(Long id, OrderListUpdateDto dto){
        Optional<OrderList> optional = orderListRepository.findById(id);

        if(optional.isPresent()){
            OrderList orderList = optional.get();
            orderList.update(dto.getOrderListTime(),dto.getOrderListTotalPrice(), OrderListStatus.valueOf(dto.getOrderListStatus()));
            return optional;
        }

        return Optional.ofNullable(null);

    }

    //주문 상세 수정
    @Transactional
    public Optional<OrderItem> updateOrderItem(Long orderListId,OrderItemUpdateRequestDto dto){
        Optional<OrderItem> optional = orderItemRepository.findById(dto.getId());

        if(!optional.isPresent()){
            return Optional.ofNullable(null);
        }

        OrderItem orderItem = optional.get();

        if(!orderItem.getOrderList().getId().equals(orderListId)){
            return Optional.ofNullable(null);
        }

        orderItem.updateAmountAndPrice(dto.getOrderItemAmount(), dto.getOrderItemPrice());

        return Optional.of(orderItem);
    }

    //주문 통계 - 날짜별 주문 수입 조회
    public Optional<OrderRevenueListDto> getOrderRevenue(String type, int year, int month){

        List<OrderRevenueResponseDto> result = new ArrayList<>();
        List<Object[]> summary = null;

        // month: 월별 통계 , year: 년도별 통계
        if(type.equals("month")){
            summary = orderListRepository.findOrderMonth(year, month);
        } else if(type.equals("year")){
            summary = orderListRepository.findOrderYear(year);
        }

        if(summary==null){
            return Optional.ofNullable(null);
        }

        for(Object[] dto : summary){
            OrderRevenueResponseDto resDto = OrderRevenueResponseDto.builder()
                    .orderListDate(dto[0].toString())
                    .orderListTotalPrice(dto[1].toString())
                    .build();
            result.add(resDto);
        }

        return Optional.of(OrderRevenueListDto.builder()
                .type(type)
                .year(year)
                .month(month)
                .OrderRevenueList(result)
                .build());
    }

    //주문 통계 - 날짜별 주문 횟수 조회
    public Optional<OrderCountListDto> getOrderCount(String type, int year, int month){

        List<OrderCountResponseDto> result = new ArrayList<>();
        List<Object[]> summary = null;

        if(type.equals("month")){
            // 일별 통계
            summary = orderListRepository.findOrderMonth(year, month);
        } else if(type.equals("year")){
            // 월별 통계
            summary = orderListRepository.findOrderYear(year);
        }

        if(summary==null){
            return Optional.ofNullable(null);
        }

        for(Object[] dto : summary){
            OrderCountResponseDto resDto = OrderCountResponseDto.builder()
                    .orderListDate(dto[0].toString())
                    .orderListCount(dto[2].toString())
                    .build();
            result.add(resDto);
        }

        return Optional.ofNullable(OrderCountListDto.builder()
                .type(type)
                .year(year)
                .month(month)
                .OrderCountList(result)
                .build());
    }

    //주문 통계 - 날짜별 주문 횟수 조회
    public Optional<OrderProductCountListDto> getOrderProductCount(String type, int year, int month){

        List<OrderProductCountResponseDto> result = new ArrayList<>();
        List<Object[]> summary = null;

        if(type.equals("month")){
            // 일별 통계
            summary = orderListRepository.findOrderMonthProduct(year, month);
        } else if(type.equals("year")){
            // 월별 통계
            summary = orderListRepository.findOrderYear(year);
        }

        if(summary==null){
            return Optional.ofNullable(null);
        }

        for(Object[] dto : summary){
            OrderProductCountResponseDto resDto = OrderProductCountResponseDto.builder()
                    .orderProductName(dto[0].toString())
                    .orderProductCount(dto[1].toString())
                    .build();
            result.add(resDto);
        }

        return Optional.ofNullable(OrderProductCountListDto.builder()
                .type(type)
                .year(year)
                .month(month)
                .OrderProductCountList(result)
                .build());
    }

    //주문 아이템 수정 완료
    public Message orderItemUpdateSuccessMessage(){
        return new AdminMessage(
                StatusCode.ORDER_ITEM_UPDATE_SUCCESS,
                StatusCode.ORDER_ITEM_UPDATE_SUCCESS.getValue(),
                "주문 아이템 수정 성공");
    }
}

