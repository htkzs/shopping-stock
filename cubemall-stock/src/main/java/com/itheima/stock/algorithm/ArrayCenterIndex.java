package com.itheima.stock.algorithm;

import java.util.Arrays;

/*
给定一个整数数组nums，请编写一个能够返回数组“中心下标”的方法。
中心下标是数组的一个下标，其左侧所有元素相加的和等于右侧所有元素相加的和。
如果数组不存在中心下标，返回-1。如果数组有多个中心下标，应该返回最靠近左边的那一个。
注意:中心下标可能出现在数组的两端。
数组中某一个下标。左右两边的元素之后相等，该下标即为中心素引思路:先统计出整个数组的总和，然后从第一个元素开始叠加
总和递减当前元素，叠加递增当前元素，直到两个值相等。
 */
public class ArrayCenterIndex {
    public static void main(String[] args) {
        System.out.println(pivotIndex(new int[]{1,7,3,6,5,6}));
    }

    private static int pivotIndex(int[] numbers) {
        int sum = Arrays.stream(numbers).sum();
        //定义一个指针 用于数组依次从左到右求和
        int total = 0;
        for(int i =0;i<numbers.length;i++){
            total +=numbers[i];
            if(total == sum){
               return i;
            }
            sum = sum - numbers[i];
        }
        //如果没有找到则返回-1
        return -1;
    }
}
