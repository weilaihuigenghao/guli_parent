package com.bdqn.educenter.controller;

import com.bdqn.commonutilss.JwtUtils;
import com.bdqn.educenter.entity.UcenterMember;
import com.bdqn.educenter.service.UcenterMemberService;
import com.bdqn.educenter.untity.ConstanWxUtils;
import com.bdqn.educenter.untity.HttpClientUtils;
import com.bdqn.servicebase.exceptionhander.bdqnException;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.util.HashMap;

@Controller  //只是请求地址，不需要返回数据
@RequestMapping("/api/ucenter/wx")
@CrossOrigin
public class WxApiController {

    @Autowired
    private UcenterMemberService memberService;

    //1.生成微信二维码
    @GetMapping("login")
    public String WxCode(){
        //固定地址，后面拼接参数
       /* String url="https://open.weixin.qq.com/connect/qrconnect?appid="+ ConstanWxUtils.WX_OPEN_APP_ID
                +"&response_type";*/

        // 微信开放平台授权baseUrl %s相当于？代表占位符
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
        "?appid=%s" +
        "&redirect_uri=%s" +
        "&response_type=code" +
        "&scope=snsapi_login" +
        "&state=%s" +
        "#wechat_redirect";

        //redirect_uri进行urlEncode编码处理
        String redireUrl=ConstanWxUtils.WX_OPEN_REDIRECT_URL;

        try {
            redireUrl= URLEncoder.encode(redireUrl,"utf-8");
        }catch (Exception e){
            e.printStackTrace();
        }

         String url =String.format(
                baseUrl,
                ConstanWxUtils.WX_OPEN_APP_ID,
                 redireUrl,
                "bdqn"
        );
        //重定向到请求微信地址里面
        return "redirect:"+url;
    }
    //2.获取扫描人信息，添加数据
    @GetMapping("callback")
    public String callback(String code,String state){//code  类似于手机验证码，生成唯一随机的值 state原样传递，传什么过去就传什么回来
        try {
            //1.获取code值，临时票据，类似于验证码


            //2.拿着code请求，微信固定的地址，得到两个值，accsess_token,和openid
            //向认证服务器发送请求换取access_token
            String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                    "?appid=%s" +
                    "&secret=%s" +
                    "&code=%s" +
                    "&grant_type=authorization_code";

            //拼接三个参数
            String accessTokenUrl =String.format(
                    baseAccessTokenUrl,
                    ConstanWxUtils.WX_OPEN_APP_ID,
                    ConstanWxUtils.WX_OPEN_APP_SECRET,
                    code

            );
            //请求这个拼接好的地址
            //使用httplient发送请求，得到返回结果
            String accessTokenInfo = HttpClientUtils.get(accessTokenUrl);
           // System.err.println("accessTokenInfo:"+accessTokenInfo);

            //从accessTokenInfo里面取出来两个值，accsess_token,和openid
            //把accessTokenInfo字符串转换为map集合，根据Map里面key获取对应值
            //使用json转换工具Gson
            Gson gson=new Gson();
            HashMap mapAccessToken = gson.fromJson(accessTokenInfo, HashMap.class);
            String access_token =(String) mapAccessToken.get("access_token");
            String openid =(String) mapAccessToken.get("openid");



            //把扫描人信息添加到数据库里面
            //判断数据表里面是否存在相同微信信息，根据openId判断
           UcenterMember member= memberService.getOpenIdMember(openid);
           if(member==null){//如果openid为空就添加到数据库
               //3.拿着accsess_token,和openid再去请求微信提供的固定的地址，获取到扫描人信息
               //访问微信的资源服务器，获取用户信息
               String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                       "?access_token=%s" +
                       "&openid=%s";

               //拼接两个参数
               String userInfoUrl =String.format(
                       baseUserInfoUrl,
                       access_token,
                       openid
               );
               //发送请求
               String userInfo = HttpClientUtils.get(userInfoUrl);
               //System.err.println("userInfo:"+userInfo);
               //获取返回userInfo字符串扫描人信息
               HashMap userInfoMap = gson.fromJson(userInfo, HashMap.class);
               String nickname = (String)userInfoMap.get("nickname");//昵称
               String headimgurl = (String)userInfoMap.get("headimgurl");//头像

               member=new UcenterMember();
               member.setOpenid(openid);
               member.setNickname(nickname);
               member.setAvatar(headimgurl);
               memberService.save(member);
           }
           //使用jwt生成token字符串
            String jwtToken = JwtUtils.getJwtToken(member.getId(), member.getNickname());
           //最后返回首页面，通过路径传递token字符串

            //最后，返回到首页
            return "redirect:http://localhost:3000?token="+jwtToken;

        }catch (Exception e){
            throw new bdqnException(20001,"登录失败");
        }



    }


}
