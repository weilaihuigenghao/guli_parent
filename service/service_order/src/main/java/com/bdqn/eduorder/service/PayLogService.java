package com.bdqn.eduorder.service;

import com.bdqn.eduorder.entity.PayLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 支付日志表 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-11-24
 */
public interface PayLogService extends IService<PayLog> {
    //生成微信支付二维码接口
    Map createNative(String orderNo);

    //根据订单号查询支付状态
    Map<String, String> queryPayStatus(String orderNo);

    //向支付表添加记录，同时更新订单状态
    void updateOrderStatus(Map<String, String> map);
}
