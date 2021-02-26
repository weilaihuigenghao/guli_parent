package com.bdqn.educenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bdqn.commonutilss.JwtUtils;
import com.bdqn.commonutilss.MD5;
import com.bdqn.educenter.entity.UcenterMember;
import com.bdqn.educenter.entity.vo.RegisterVo;
import com.bdqn.educenter.mapper.UcenterMemberMapper;
import com.bdqn.educenter.service.UcenterMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bdqn.servicebase.exceptionhander.bdqnException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-11-22
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {
   @Autowired
   private RedisTemplate<String,String> redisTemplate;

    //登录的方法
    @Override
    public String login(UcenterMember member) {
        //获取登录手机号和密码
        String mobile=member.getMobile();
        String password=member.getPassword();
        //手机号和密码非空判断
        if(StringUtils.isEmpty(mobile) ||StringUtils.isEmpty(password)){
            throw new bdqnException(20001,"登录失败");
        }
        //判断手机号是否正确
        QueryWrapper<UcenterMember>wrapper=new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
        UcenterMember mobileMember = baseMapper.selectOne(wrapper);
        //判断查询对象是否为空
        if(mobileMember==null){//没有这个手机号
            throw new bdqnException(20001,"登录失败");
        }
        //判断密码
        //把输入的密码进行加密，在和数据库进行比较
        if(!MD5.encrypt(password).equals(mobileMember.getPassword())){
            throw new bdqnException(20001,"登录失败");
        }
        //判断用户是否禁用
        if(mobileMember.getIsDisabled()){
            throw new bdqnException(20001,"登录失败");
        }
        //登录成功
        //生成token字符串，使用jwt工具类
        //得到id得到昵称，把token生成
        String jwtToken =JwtUtils.getJwtToken(mobileMember.getId(),mobileMember.getNickname());
        return jwtToken;
    }
    //注册的方法
    @Override
    public void register(RegisterVo registerVo) {
        //获取注册的数据
        String code = registerVo.getCode(); //验证码
        String mobile = registerVo.getMobile(); //手机号
        String nickname = registerVo.getNickname(); //昵称
        String password = registerVo.getPassword(); //密码

        //非空判断
        if(StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)
                || StringUtils.isEmpty(code) || StringUtils.isEmpty(nickname)) {
            throw new bdqnException(20001,"注册失败");
        }
        //判断验证码
        //获取redis验证码
        String redisCode = redisTemplate.opsForValue().get(mobile);
        if(!code.equals(redisCode)){
            throw new bdqnException(20001,"注册失败");
        }
        //判断手机号是否重复，表里面存在相同手机号不进行添加
        QueryWrapper<UcenterMember> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("mobile",mobile);
        Integer count = baseMapper.selectCount(queryWrapper);
        if(count>0){
            throw new bdqnException(20001,"注册失败");
        }
        //数据添加数据库中
        UcenterMember member = new UcenterMember();
        member.setMobile(mobile);
        member.setNickname(nickname);
        member.setPassword(MD5.encrypt(password));//密码需要加密的
        member.setIsDisabled(false);//用户不禁用
        member.setAvatar("http://thirdwx.qlogo.cn/mmopen/vi_32/3HEmJwpSzguqqAyzmBwqT6aicIanswZibEOicQInQJI3ZY1qmu59icJC6N7SahKqWYv24GvX5KH2fibwt0mPWcTJ3fg/132");
        baseMapper.insert(member);

    }
    //根据openId判断
    @Override
    public UcenterMember getOpenIdMember(String openid) {
        QueryWrapper<UcenterMember>wrapper=new QueryWrapper<>();
        wrapper.eq("openid",openid);
        UcenterMember member = baseMapper.selectOne(wrapper);
        return member;
    }
    //查询某一天注册人数
    @Override
    public Integer countRegisterDay(String day) {

        return baseMapper.countRegisterDay(day);
    }
}
