package com.itheima.stock.algorithm;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/*
给定一个整数数组numbers ，从数组中找出两个数满足相加之和等于目标数target 。
假设每个输入只对应唯一的答案,而且不可以重复使用相同的元素。 target = 12  array[1,2,3,4,5,6,7] 返回值的下标不能为 [5,5]
返回两数的下标值，以数组形式返回
 */
//其中空间复杂度为O(1) 时间复杂度为O(n2)
public class ArraySummation {
    public static int[] solution(int[] numbers,int target){
        for (int i = 0; i < numbers.length ; i++) {
            //这里可以继续优化
            for (int j = 1; j < numbers.length; j++) {
                if(numbers[i]+numbers[j] == target){
                    //返回两个数组的下标
                    return new int[]{i,j};
                }
            }
        }
        return new int[]{};
    }
    //优化1
    public static int[] solution1(int[] numbers,int target){
        for (int i = 0; i < numbers.length ; i++) {
            //这里可以继续优化
            for (int j = 1 + i; j < numbers.length; j++) {
                if(numbers[i]+numbers[j] == target){
                    //返回两个数组的下标
                    return new int[]{i,j};
                }
            }
        }
        return new int[]{};
    }
    //优化2 时间复杂度降低到O(n) 空间复杂度为O(n)
    public static int[] solution2(int[] numbers,int target){
        Map<Integer,Integer> map = new HashMap<Integer,Integer>();
        for (int i = 0; i < numbers.length; i++) {
            if(map.containsKey(target - numbers[i])){
                // 例如 [1, 2, 3, 4, 5, 6] 当i循坏到6时发现map中包含一个4 随即取出值为4的数组的下标
                return new int[]{map.get(target - numbers[i]),i};
            }
            //如果Map中不包含就加入
            map.put(numbers[i], i);
        }
        return new int[]{};
    }
/*
给定一个 升序排列的 整数数组numbers ，从数组中找出两个数满足相加之和等于目标数target 。
假设每个输入只对应唯一的答案,而且不可以重复使用相同的元素。 target = 12  array[1,2,3,4,5,6,7] 返回值的下标不能为 [5,5]
返回两数的下标值，以数组形式返回
 */

    // 时间复杂度为O(nlogn) 空间复杂度为O(1)
    //借助二分查找算法 的前提是必须保证数组有序
    public static int[] twoSearch(int[] numbers, int target){
        //定义最小的那个数就是我们要求的解之一
        for (int i = 0; i < numbers.length; i++) {
            //定义两个指针 low和height
            int low = i,height = numbers.length-1;
            while(low <= height){
                //定位二分查找的mid值
                int mid = (height - low)/2 +low;
                if(target - numbers[low] == numbers[height]){
                    return new int[]{low,height};
                }else if(target - numbers[low] < numbers[mid]){
                    height = mid -1;
                }else{
                    low = mid + 1;
                }
            }
        }
       return new int[]{};
    }
    //双指针法 时间复杂度O(n) 空间复杂度为O(1)
    //思路 使用两个指针分别指向一个数组的两端  通过比较两个数的合 决定要移动那个指针。
    public static int[] twoPoint(int[] numbers, int target){
        //定义两个指针 low和height  首先认为数组两端的两个值为所求的解
        int low = 0,height = numbers.length-1;
        while(low < height){
            if(numbers[low] + numbers[height] == target){
                return new int[]{low,height};
            }else if(numbers[low] + numbers[height] > target){
                height --;
            }else{
                low ++;
            }
        }
        return new int[]{};
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(solution1(new int[]{1, 2, 3, 4, 5, 6}, 10)));
        System.out.println(Arrays.toString(solution2(new int[]{1, 2, 3, 4, 5, 6}, 10)));
        System.out.println(Arrays.toString(twoSearch(new int[]{1, 2, 3, 4, 5, 6}, 10)));
        System.out.println(Arrays.toString(twoPoint(new int[]{1, 2, 3, 4, 5, 6}, 10)));
    }
}
