package com.bdqn.servicebase.exceptionhander;



import com.bdqn.commonutilss.R;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice //统一异常处理
@Slf4j
public class GlobalExceptionHander {
    @ExceptionHandler(Exception.class)//指定出现什么异常执行这个方法
    @ResponseBody//为了返回数据
    public R error(Exception e){
        e.printStackTrace();
        return R.error().message("执行了全局异常处理。。");
    }

    /**
     * 特定异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(ArithmeticException.class)//指定出现什么异常执行这个方法
    @ResponseBody//为了返回数据
    public R error(ArithmeticException e){
        e.printStackTrace();

        log.error(e.getMessage());
        return R.error().message("执行了ArithmeticException异常处理。。");
    }
    @ExceptionHandler(bdqnException.class)//指定出现什么异常执行这个方法
    @ResponseBody//为了返回数据
    public R error(bdqnException e){
        e.printStackTrace();
        return R.error().code(e.getCode()).message(e.getMsg());
    }

}
