package com.itheima.stock.algorithm;

/*
一个有序数组nums，原地删除重复出现的元素，使每个元素只出现一次,返回删除后数组的新长度。
不能使用额外的数组空间，必须在原地修改输入数组并在使用O(1)额外空间的条件下完成。
例:输入:[0,1,2,2,3,3,4]
输出:5
重点考察:双指针算法
数组完成排序后，我们可以放置两个指针i和j，其中i是慢指针，而j是快指针。只要nums[i]=nums[j]，我们就增加j以跳过重复项。
当遇到 nums[i] ! = nums[j]时，跳过重复项的运行已经结束，必须把nums[j])的值复制到 nums[ i + 1]。然后递再次重复相同的过程，直到j到达数组的末尾为止。
 */
public class RemoveDuplicates {
    public static int RemoveDuplicates_algorithm(int[] numbers){
        if(numbers.length == 0){
            return 0;
        }
        //定义两个指针 重复时覆盖的方式
        int i = 0;
        for(int j = 1;j < numbers.length;j++){
            if(numbers[i] !=numbers[j]){
                i++;
                numbers[i] = numbers[j];
            }
        }
        return i+1;
    }
    public static void main(String[] args) {
        int[] numbers = new int[]{0,1,2,2,3,3,4};
        int i = RemoveDuplicates.RemoveDuplicates_algorithm(numbers);
        System.out.println("求得去重后的数组的长度为："+i);
    }
}
