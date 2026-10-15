package com.grupo12.epicentro_gourmet.controllers;

import com.grupo12.epicentro_gourmet.helpers.ViewRouteHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(){
        return ViewRouteHelper.LOGIN;
    }

    @GetMapping("loginsuccess")
    public String loginSuccess(){
        return ViewRouteHelper.REDIRECT_INDEX;
    }


}
