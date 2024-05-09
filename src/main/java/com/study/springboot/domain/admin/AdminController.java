package com.study.springboot.domain.admin;

import com.study.springboot.datas.Message;
import com.study.springboot.datas.MessageService;

import com.study.springboot.domain.orderSystem.OrderItem;
import com.study.springboot.domain.orderSystem.dto.*;
import com.study.springboot.domain.user.User;
import com.study.springboot.domain.user.dto.UserDto;
import com.study.springboot.domain.user.service.UserService;
import com.study.springboot.domain.orderSystem.OrderList;
import com.study.springboot.domain.orderSystem.service.OrderListService;
import com.study.springboot.domain.product.Product;
import com.study.springboot.domain.product.dto.ProductDto;
import com.study.springboot.domain.product.dto.RequestProductEditDto;
import com.study.springboot.domain.product.dto.RequestProductRemoveDto;
import com.study.springboot.domain.product.dto.RequestSearchDto;
import com.study.springboot.enumeration.ProductCategory;
import com.study.springboot.enumeration.SearchCategory;
import com.study.springboot.enumeration.error.StatusCode;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
//@Secured({"ROLE_ADMIN"})
public class AdminController {

    private final AdminService adminService;
    private final MessageService messageService;


    /*
    User
     */

    //회원 목록
    @GetMapping("/user")
    public ResponseEntity<Message> userList(@RequestParam(value="type", required = false) @Nullable String type,
                                                @RequestParam(value="text", required = false) @Nullable String text,
                                                @RequestParam(value="page", required = false, defaultValue = "0") @Nullable int page
                                                  ){

        Page<UserDto> userDtoPage = adminService.getUserList(type, text, page);
        if(userDtoPage==null){
            Message message = messageService.userListNotFoundMessage();
            return ResponseEntity.ok(message);
        }

        Message message = messageService.userListFoundSuccessMessage(userDtoPage);

        return ResponseEntity.ok().body(message);
    }

    //회원 상세 조회
    @GetMapping("/user/{id}")
    public ResponseEntity getUser(@PathVariable("id") Long id){
        Optional<User> optional = adminService.getUser(id);
        Message message;
        if(optional.isPresent()){
            UserDto dto = new UserDto(optional.get());
            message = messageService.userFoundSuccessMessage(dto);
            return ResponseEntity.ok(message);
        }

        message = messageService.userNotFound();
        return ResponseEntity.ok().body(adminService.getUser(id));
    }

    //회원 삭제
    @DeleteMapping("/user/{id}")
    public ResponseEntity deleteUser(@PathVariable("id") String userId){
        Optional<User> deleted = adminService.deleteUser(userId);

        if(deleted.isPresent()){
            Message message = messageService.userDeleteSuccessMessage();
            return ResponseEntity.ok(message);
        }
        Message message = messageService.userNotFoundMessage();
        return ResponseEntity.ok().body(message);
    }

    //회원 수정
    @PutMapping("/user/{id}")
    public ResponseEntity updateUser(@PathVariable("id") Long id, @RequestBody UserDto dto){
        System.out.println("dtoname" + dto.getUserName());
        Optional<User> optional = adminService.updateUser(id, dto);
        if(optional.isPresent()){
            User user = optional.get();
            user.update(dto);
            Message message = messageService.userUpdateSuccessMessage();
            return ResponseEntity.ok(message);
        }

            Message message = messageService.userNotFoundMessage();
            return ResponseEntity.ok(message);
    }



    /*
    OrderList
     */

    //주문 목록 조회
    @GetMapping("/order")
    public ResponseEntity orderList(@RequestParam(value="type", required = false) @Nullable String type,
                                                        @RequestParam(value="text", required = false) @Nullable String text,
                                                        @RequestParam(value="page", required = false, defaultValue = "0") @Nullable int page
    ){
        Page<OrderListDto> dto = adminService.getOrderList(type, text, page);
        if(dto == null){
            Message message = messageService.orderListNotFoundMessage();
            return ResponseEntity.ok(message);
        }

        Message message = messageService.orderListFoundSuccessMessage(dto);
        return ResponseEntity.ok().body(message);
    }

    //주문 상세 조회
    @GetMapping("/order/{id}")
    public ResponseEntity getOrderList(@PathVariable("id") Long id){
        Optional<OrderListDetailDto> optional = adminService.getOrder(id);
        if(optional.isPresent()){
            Message message = messageService.orderFoundSuccessMessage(optional.get());
            return ResponseEntity.ok(message);
        }

        Message message = messageService.orderListNotFoundMessage();
        return ResponseEntity.ok().body(message);
    }

    //주문 삭제
    @DeleteMapping("/order/{id}")
    public ResponseEntity deleteOrderList(@PathVariable("id") Long id){
        Optional<OrderList> optional = adminService.deleteOrderList(id);
        if(optional.isPresent()){
            Message message = messageService.orderListDeleteSuccessMessage();
            return ResponseEntity.ok(message);
        }

        Message message = messageService.orderListNotFoundMessage();
        return ResponseEntity.ok(message);
    }

    //주문 수정
    @PutMapping("/order/{id}")
    public ResponseEntity updateOrderList(@PathVariable("id") Long id, @RequestBody OrderListUpdateDto dto){
        Optional<OrderList> optional = adminService.updateOrderList(id, dto);

        Message message;
        if(optional.isPresent()){
              message = messageService.orderListUpdateSuccessMessage();
              return ResponseEntity.ok(message);
        }

        message = messageService.orderListNotFoundMessage();
        return ResponseEntity.ok().body(message);
    }

    //주문별 상세 아이템 수정
    @PutMapping("/order/{orderListId}/item")
    public ResponseEntity updateOrderItem(@PathVariable("orderListId") Long orderListId,@RequestBody OrderItemUpdateRequestDto dto){
        Optional<OrderItem> optional = adminService.updateOrderItem(orderListId, dto);

        Message message;
        if(optional.isPresent()){
            message = messageService.orderItemUpdateSuccessMessage();

            return ResponseEntity.ok(message);
        }

        message = messageService.orderItemNotFoundMessage();
        return ResponseEntity.ok().body(message);
    }


    //주문 통계 - 날짜별 주문 금액 조회
    @GetMapping("/order/statistics/revenue")
    public ResponseEntity getOrderRevenue(@RequestParam(value="type", required = false, defaultValue = "month") @Nullable String type,
                                                               @RequestParam(value="year", required = false, defaultValue = "2024") @Nullable int year,
                                                               @RequestParam(value="month", required = false, defaultValue = "4") @Nullable int month){

        Optional<OrderRevenueListDto> optional = adminService.getOrderRevenue(type, year, month);
        Message message;
        if(optional.isPresent()){
            message = messageService.orderRevenueListFoundSuccessMessage(optional.get());
            return ResponseEntity.ok(message);
        }


        message = messageService.orderStatisticsListNotFoundMessage();
        return ResponseEntity.ok(message);
    }

    //주문 통계 - 날짜별 주문 수 조회
    @GetMapping("/order/statistics/orders")
    public ResponseEntity getOrderCount(@RequestParam(value="type", required = false, defaultValue = "month") @Nullable String type,
                                                           @RequestParam(value="year", required = false, defaultValue = "2024") @Nullable int year,
                                                           @RequestParam(value="month", required = false, defaultValue = "4") @Nullable int month){



        Optional<OrderCountListDto> optional = adminService.getOrderCount(type, year, month);

        Message message;
        if(optional.isPresent()){
            message = messageService.orderCountListFoundSuccessMessage(optional.get());
            return ResponseEntity.ok(message);
        }

        message = messageService.orderStatisticsListNotFoundMessage();
        return ResponseEntity.ok(message);
    }

    //주문 통계 - 날짜별 상품 조회
    @GetMapping("/order/statistics/product")
    public ResponseEntity getOrderProductCount(@RequestParam(value="type", required = false, defaultValue = "month") @Nullable String type,
                                        @RequestParam(value="year", required = false, defaultValue = "2024") @Nullable int year,
                                        @RequestParam(value="month", required = false, defaultValue = "4") @Nullable int month){


        Optional<OrderProductCountListDto> optional = adminService.getOrderProductCount(type, year, month);

        Message message;
        if(optional.isPresent()){
            message = messageService.orderProductCountListFoundSuccessMessage(optional.get());
            return ResponseEntity.ok(message);
        }

        message = messageService.orderStatisticsListNotFoundMessage();
        return ResponseEntity.ok(message);
    }

    /*
    product service
     */
    @GetMapping("/product")
    public ResponseEntity productList(@RequestParam(value = "page", defaultValue = "0") int page,
                                      @RequestParam(value = "sort", defaultValue = "product_name") String sortBy){

        Page<ProductDto> dto = adminService.findAllProduct(page, sortBy);
        if(!dto.isEmpty()){
            Message message = messageService.productListFoundSuccessMessage(dto);
            return ResponseEntity.ok(message);
        }

        Message message = messageService.productNotFoundMessage();
        return ResponseEntity.ok(message);
    }

    @PostMapping("/product/remove")
    public ResponseEntity productRemove(@RequestBody RequestProductRemoveDto dto){


        String productCode = dto.getProductCode();

        Optional<ProductDto> optional = adminService.removeProduct(productCode);

        if(optional.isPresent()){
            Message message = messageService.productRemoveSuccessMessage();
            return ResponseEntity.ok(message);
        }

        Message message = messageService.productRemoveFailedMessage();
        return ResponseEntity.ok(message);

    }

    @GetMapping("/product/detail/{code}")
    public ResponseEntity productDetail(@PathVariable(value = "code") Long id){

        System.out.println("상품디테일 " + id);
        Optional<Product> optional = adminService.findById(id);
        if(optional.isPresent()){
            Message message = messageService.productFoundSuccessMessage(new ProductDto(optional.get()));
            return ResponseEntity.ok(message);
        }

        return ResponseEntity.ok(messageService.productNotFoundMessage());
    }

    @PostMapping("/product/detail/edit")
    public ResponseEntity productEdit(@RequestBody RequestProductEditDto dto, HttpSession session){
        //관리자가 아니라면 에러 코드
//        if(!KioskSession.isAdmin(session)){
//            Message message = Message.userNoPermission();
//            return ResponseEntity.ok(message);
//        }

        Optional<ProductDto> optional = adminService.editProduct(dto);
        if(optional.isPresent()){
            Message message = messageService.productEditSuccess();
            return ResponseEntity.ok(message);
        }

        Message message = messageService.productEditFailed();
        return ResponseEntity.ok(message);
    }

    @PostMapping("/product/list/search")
    public ResponseEntity productSearch(@RequestBody RequestSearchDto dto, HttpSession session){
        String searchKeyword = dto.getSearchKeyword();
        Integer page = dto.getPage();
        Integer pageSize = dto.getPageSize();
        ProductCategory productCategory = dto.getProductCategory();
        SearchCategory searchCategory = dto.getSearchCategory();


        List<Product> result = adminService.findProductsBy(searchCategory, productCategory, searchKeyword, page, pageSize);
        return ResponseEntity.ok(result);
    }


}
