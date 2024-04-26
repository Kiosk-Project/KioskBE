package com.study.springboot.domain.member.dto;


import com.study.springboot.domain.member.User;
import com.study.springboot.domain.orderSystem.OrderList;
import com.study.springboot.enumeration.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDto {

    private String userId;

    private String userName;

    private String userPw;

    private LocalDate userJoinDate;

    private Integer userPoint;

    private UserRole userRole;

    private List<OrderList> orderList;

    UserDto(User entity){
        this.userId = entity.getUserId();
        this.userPw = entity.getUserPw();
        this.userName = entity.getUserName();
        this.userRole = entity.getUserRole();
        this.userPoint = entity.getUserPoint();
        this.userJoinDate = entity.getUserJoinDate();
        this.orderList = entity.getOrderList();
    }

}
