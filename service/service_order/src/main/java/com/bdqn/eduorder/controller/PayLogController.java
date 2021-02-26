package com.bdqn.eduorder.controller;


import com.bdqn.commonutilss.R;
import com.bdqn.eduorder.service.PayLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-11-24
 */
@RestController
@RequestMapping("/eduorder/paylog")
@CrossOrigin
public class PayLogController {
    @Autowired
    private PayLogService payLogService;

    //生成微信支付二维码接口
    //参数是订单号
    @GetMapping("createNative/{orderNo}")
    public R createNative(@PathVariable String orderNo){
        //返回信息，包含二维码地址，还有其它需要的信息
       Map map= payLogService.createNative(orderNo);
        System.err.println("*****返回二维码的map集合"+map);

        return R.ok().data(map);
    }
    //查询订单支付状态
    //参数，订单号，根据订单号查询 支付状态
    @GetMapping("queryPayStatus/{orderNo}")
    public R queryPayStatus(@PathVariable String orderNo){
       Map<String,String>map= payLogService.queryPayStatus(orderNo);
        System.err.println("*****查询订单状态的map集合"+map);
       if(map==null){
           return R.error().message("支付出错了");
       }
       //如果返回map不为空,通过Map获取订单状态
        if(map.get("trade_state").equals("SUCCESS")){
            //添加记录到支付表，同时更新订单表订单状态
            payLogService.updateOrderStatus(map);
            return R.ok().message("支付成功");
        }
        return R.ok().message("支付中");

    }
}

