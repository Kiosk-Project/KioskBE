package com.study.springboot.datas;

import com.study.springboot.enumeration.Place;
import com.study.springboot.enumeration.UserRole;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KioskSession {
    private Boolean isLogin = false;
    private String userId;
    private UserRole userRole;
    private Place userPlace;


    @Builder
    public KioskSession(Boolean isLogin, String userId, UserRole userRole) {
        this.isLogin = isLogin;
        this.userId = userId;
        this.userRole = userRole;
    }

    public void login(){
        isLogin = true;
    }

    public static KioskSession makeAdminSession(String userId){
        return KioskSession.builder()
                .userId(userId)
                .userRole(UserRole.ADMIN)
                .build();
    }

    public static KioskSession makeUserSession(String userId){
        return KioskSession.builder()
                .userId(userId)
                .userRole(UserRole.USER)
                .build();
    }

    public void setPlace(Place place){
        this.userPlace = place;
    }
}
