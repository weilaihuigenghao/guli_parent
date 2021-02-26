package com.bdqn.msmservice.controller;

import com.bdqn.commonutilss.R;
import com.bdqn.msmservice.service.MsmService;
import com.bdqn.msmservice.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("edumsm/msm")
@CrossOrigin
public class MsmContronller {
    @Autowired
    private MsmService msmService;

    @Autowired
    private RedisTemplate<String,String>redisTemplate;

    //发送短信的方法
    @GetMapping("send/{phone}")
    public R send(@PathVariable String phone){
        //从redis获取验证码如果获取到直接返回
       String code= redisTemplate.opsForValue().get(phone);
        if(!StringUtils.isEmpty(code)){
            return R.ok();
        }
        //如果redis获取不到，进入阿里云发送
        //生成随机值，传递给阿里云进行发送
        code= RandomUtil.getFourBitRandom();
        Map<String,Object>param=new HashMap<>();
        param.put("code",code);

        //调用msmService发送短信的方法
       boolean isSend= msmService.send(param,phone);
       if(isSend){
           //发送成功，把发送成功的验证码放到redis里面
           //设置有效时间为5分钟
           redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);
          return R.ok();
       }
       else{
         return  R.error().message("短信发送失败");
       }

    }
}
