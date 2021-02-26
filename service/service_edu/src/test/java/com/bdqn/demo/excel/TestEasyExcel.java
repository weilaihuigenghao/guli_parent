package com.bdqn.demo.excel;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

public class TestEasyExcel {
    public static void main(String[] args) {
        //实现excel的写入操作
        //设置写入文件夹地址和excke文件名称
       String filename="D:\\write.xlsx";

        //调用EasyExcel里面的方法实现写操作
        //write两个方法，第一个参数是文件路径名称，第二个参数是实体类class
        //EasyExcel.write(filename,DemoData.class).sheet("学生列表").doWrite(getData());


        EasyExcel.read(filename,DemoData.class,new ExcelListener()).sheet().doRead();
    }
    //创建方法返回List集合
    private static List<DemoData>getData(){
        List<DemoData> list=new ArrayList<>();
        for (int i=0;i <10; i++){
            DemoData data=new DemoData();
            data.setSno(i);
            data.setSname("lucy"+i);
            list.add(data);
        }
        return list;
    }
}
