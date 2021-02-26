package com.bdqn.eduService.controller;

import com.bdqn.commonutilss.R;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/eduservice/user")
@CrossOrigin
public class EduLoginController {
    //lohin方法
    @PostMapping("login")
    public R login() {

        return R.ok().data("token","admin");
    }
    //lohin方法
    @GetMapping("info")
    public R info() {

        return R.ok().data("roles","admin").data("name","admin").data("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
    }


}
