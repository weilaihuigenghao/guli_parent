package com.bdqn.eduorder.client;

import com.bdqn.commonutilss.ordervo.UcenterMemberOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Component
@FeignClient("service-ucenter")
public interface UcenterClient {
    //根据用户id获取用户信息
    @PostMapping("/educenter/member/getUserInfoOrder/{id}")
    //远程调用PathVariable必须要加参数
    public UcenterMemberOrder getUserInfoOrder(@PathVariable("id") String id);
}
