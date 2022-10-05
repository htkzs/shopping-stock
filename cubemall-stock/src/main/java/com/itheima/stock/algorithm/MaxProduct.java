package com.itheima.stock.algorithm;

import java.util.Arrays;

/*
求出一个数组中最大的三个数的乘积 分为三种情况 1.数组全部为正数 则最大的三个正数相乘即为最大的
                                        2.数组全部为负数 则需要求得三个最大的负数的乘积
                                        3.如果有负数也有正数 则需要两个最小的负数 和一个最大的正数相乘
                                        考虑到绝对值 则1和3 合并为一种情况  共需要考虑两种情况
 */
public class MaxProduct {
     //借助排序算法 算法复杂度nlogn
     public static int sort(int[] numbers){
         Arrays.sort(numbers);
         int n = numbers.length;
         return Math.max(numbers[0]*numbers[1]*numbers[n-1],numbers[n-1]*numbers[n-2]*numbers[n-3]);
     }
     //线性搜索法
    public static int getMaxMin(int[] numbers){
         //找最大的三个值和最小的两个值
         int min1 = Integer.MAX_VALUE,min2 = Integer.MAX_VALUE;
         int max1 = Integer.MIN_VALUE,max2 = Integer.MIN_VALUE,max3 = Integer.MIN_VALUE;

         for(int x : numbers){
             if(x < min1){
                 min2 = min1;
                 min1 = x;
             }else if(x < min2){
                 min2 = x;
             }
             if (x > max1){
                 max3 = max2;
                 max2 = max1;
                 max1 = x;
             }else if(x>max2){
                 max3 =max2;
                 max2 = x;
             }else if(x>max3){
                 max3 = x;
             }
         }
         return  Math.max(min1*min2*max1,max1*max2*max3);
    }

    public static void main(String[] args) {
        System.out.println(sort(new int[]{1,2,3,4,5,6}));
        System.out.println(getMaxMin(new int[]{1,2,3,4,5,6}));
    }
}
