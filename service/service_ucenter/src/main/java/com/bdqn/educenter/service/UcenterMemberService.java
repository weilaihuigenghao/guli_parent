package com.bdqn.educenter.service;

import com.bdqn.educenter.entity.UcenterMember;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bdqn.educenter.entity.vo.RegisterVo;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-11-22
 */
public interface UcenterMemberService extends IService<UcenterMember> {
    //登录的方法
    String login(UcenterMember member);
    //注册
    void register(RegisterVo registerVo);
    //根据openId判断
    UcenterMember getOpenIdMember(String openid);

    //查询某一天注册人数
    Integer countRegisterDay(String day);
}
