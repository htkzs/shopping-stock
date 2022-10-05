package com.itheima.stock.algorithm;

import java.sql.PreparedStatement;

//Fibonacci 求第n个元素的值 暴力递归  时间复杂度 2的n次方
public class Fibonacci {
    public static int calculate(int num){
        if(num == 0){
            return 0;
        }
        if(num == 1){
            return 1;
        }
        return calculate(num-1) + calculate(num-2);
    }
    //优化 时间复杂度 O(n) 空间复杂度O(n)
    public static int calculate1(int num){
        //定义一个数组用于记录已经递归求得的值 防止多次重复计算
        int[] FibonacciInit = new int[num+1];
        //return calculate(num-1) + calculate(num-2);
        return recursion(FibonacciInit,num);
    }

    private static int recursion(int[] fibonacciInit, int num) {
        if(num == 0){
            return 0;
        }
        if(num == 1){
            return 1;
        }
        if(fibonacciInit[num] != 0){
            return fibonacciInit[num];
        }
        //得到最初的两个值 并一次保存到数组中，当再次使用时直接判断 有的话直接返回
        fibonacciInit[num] = recursion(fibonacciInit,num-1)+recursion(fibonacciInit,num-2);
        return fibonacciInit[num];
    }
    //双指针迭代法 时间复杂度为o(n) 空间复杂度为O(1)
    private static int iterator(int num){
        if(num == 0){
            return 0;
        }
        if(num == 1){
            return 1;
        }
        int low = 0,high =1;
        for (int i = 2; i <= num; i++) {
           int sum = low + high;
           low = high;
           high =sum;
        }
        return high;
    }

    public static void main(String[] args) {
        //System.out.println(Fibonacci.calculate(8));
        //System.out.println(Fibonacci.calculate1(25));
        System.out.println(iterator(25));
    }
}
