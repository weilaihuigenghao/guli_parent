package com.bdqn.educenter.controller;


import com.bdqn.commonutilss.JwtUtils;
import com.bdqn.commonutilss.R;
import com.bdqn.commonutilss.ordervo.UcenterMemberOrder;
import com.bdqn.educenter.entity.UcenterMember;
import com.bdqn.educenter.entity.vo.RegisterVo;
import com.bdqn.educenter.service.UcenterMemberService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-11-22
 */
@RestController
@RequestMapping("/educenter/member")
@CrossOrigin
public class UcenterMemberController {
    @Autowired
    private UcenterMemberService ucenterMemberService;

    //登录的方法
    @PostMapping("login")
    public R login(@RequestBody UcenterMember member){
        //调用service方法进行登录
        //返回token值，使用jwt生成登录字符串
       String token= ucenterMemberService.login(member);
        return R.ok().data("token",token);
    }
    //注册
    @PostMapping("register")
    public R register(@RequestBody RegisterVo registerVo){
        ucenterMemberService.register(registerVo);
        return R.ok();
    }
    //根据token获取用户信息
    @GetMapping("getMemberInfo")
    public R getMemberInfo(HttpServletRequest request){
        //调用jwtUtils中的方法，根据request对象获取头信息，返回用户id
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        //查询数据库根据用户id获取用户信息
        UcenterMember member = ucenterMemberService.getById(memberId);
        return R.ok().data("userInfo",member);
    }

    //根据用户id获取用户信息
    @PostMapping("getUserInfoOrder/{id}")
    public UcenterMemberOrder getUserInfoOrder(@PathVariable String id){
        UcenterMember member =ucenterMemberService.getById(id);
        //把member对象里面的值复制到ucenterMemberOrder对象
        UcenterMemberOrder ucenterMemberOrder=new UcenterMemberOrder();
        BeanUtils.copyProperties(member,ucenterMemberOrder);
        return ucenterMemberOrder;
    }
    //查询某一天注册人数
    @GetMapping("countRegister/{day}")
    public R countRegister(@PathVariable String day){
       Integer count= ucenterMemberService.countRegisterDay(day);
        return R.ok().data("countRegister",count);
    }
}

