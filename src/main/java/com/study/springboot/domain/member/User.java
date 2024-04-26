package com.study.springboot.domain.member;


import com.study.springboot.domain.orderSystem.OrderList;
import com.study.springboot.enumeration.UserRole;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(uniqueConstraints = {
        @UniqueConstraint(name="ID_USER_ID_PW_CONSTRAINT", columnNames = {"id", "user_id", "user_pw"})
})
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userId;

    @Column
    private String userName;


    @Column(nullable = false)
    private String userPw;

    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate userJoinDate = LocalDate.now();

    @Column
    private Integer userPoint = 0;

    @Enumerated(EnumType.STRING)
    @Column
    private UserRole userRole = UserRole.USER;

    @OneToMany(mappedBy = "user")
    private List<OrderList> orderList = new ArrayList<>();


    @Builder
    private User(Long id, String userId, String userName, String userPw, LocalDate userJoinDate, Integer userPoint, UserRole userRole, List<OrderList> orderList) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.userPw = userPw;
        this.userJoinDate = userJoinDate;
        this.userPoint = userPoint;
        this.userRole = userRole;
        this.orderList = orderList;
    }

    public static User makeAdminUser(String userId, String userName, String userPw){
        User user = User.builder()
                .userId(userId)
                .userName(userName)
                .userPw(userPw)
                .userRole(UserRole.ADMIN)
                .build();

        return user;
    }

    public static User makeUser(String userId, String userName, String userPw){
        User user = User.builder()
                .userId(userId)
                .userName(userName)
                .userPw(userPw)
                .userRole(UserRole.USER)
                .build();

        return user;
    }

}
