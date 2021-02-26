package com.bdqn.educms.controller;

import com.bdqn.commonutilss.R;
import com.bdqn.educms.entity.CrmBanner;
import com.bdqn.educms.service.CrmBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/educms/bannerfront")
@CrossOrigin
/**
 * 前台banner显示
 */
public class BannerFromContronner {
    @Autowired
    private CrmBannerService crmBannerService;
    //查询所有banner
    @GetMapping("getAllBanner")
    public R getAllBanner(){
       List<CrmBanner>list= crmBannerService.selectAllBanner();
        return R.ok().data("list",list);
    }

}
