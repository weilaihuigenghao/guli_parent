package com.bdqn.vodtest.kuang;

public class TestThread implements Runnable{
    private static String winner;

    @Override
    public void run() {
        for (int i = 0; i <= 100; i++) {
            //模拟兔子休息
            if(Thread.currentThread().getName().equals("兔子") && i%10==0){
                try {
                    //Thread.sleep("200");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }


            //判断比赛是否结束
            boolean flag=gameOver(i);
            if(flag){
                break;
            }
            System.out.println(Thread.currentThread().getName()+"跑了--"+i+"步");

        }
    }
    //判断是否完成比赛
    public boolean gameOver(int steps){
        //判断是否有胜利者
        if(winner!=null){
            return true;
        }
        if(steps==100){
            winner=Thread.currentThread().getName();
            System.out.println("winner is"+winner);
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        TestThread testThread=new TestThread();
        new Thread(testThread,"兔子").start();
        new Thread(testThread,"乌龟").start();
    }
}
