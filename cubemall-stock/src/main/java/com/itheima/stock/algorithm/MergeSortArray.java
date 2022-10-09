package com.itheima.stock.algorithm;

import java.util.Arrays;

/*
合并两个有序数组
两个有序整数数组nums1和nums2，将nums2合并到nums1中，使nums1成为一个有序数组。初始化nums1和nums2的元素数量分别为m和n。
二 nums1的空间大小等于m+n，这样它就有足够的空间保存来自nums2的元素。
 */
public class MergeSortArray {
    public static void main(String[] args) {
        int[] nums1 = new int[]{1,3,5,7,9,0,0,0,0};
        int[] nums2 = new int[]{2,4,6,8};
        //System.out.println(Arrays.toString(merge(nums1,5,nums2,4)));
        System.out.println(Arrays.toString(mergeByTwoPointer(nums1, 5, nums2, 4)));
    }

    private static int[] merge(int[] nums1, int m, int[] nums2, int n) {
        System.arraycopy(nums2,0,nums1,m,n);
        Arrays.sort(nums1);
        return nums1;
    }
    //双指针法
    public static int[] mergeByTwoPointer(int[] nums1, int m, int[] nums2,int n){
        int[] nums1_copy = new int[m];
        System.arraycopy(nums1,0,nums1_copy,0,m);
        int p1 = 0;
        int p2 = 0;
        int p = 0;
        while(p1 < m && p2 < n){
            nums1[p++] = nums1_copy[p1] < nums2[p2] ? nums1_copy[p1++] : nums2[p2++];
        }
        //某个数组以经被全部拷贝完成后会跳出while循环进入if 进入if的数组的剩余元素将会被直接拷贝。
        if(p1<m){
            System.arraycopy(nums1_copy,p1,nums1,p1+p2,m+n-p1-p2);
        }
        if(p2<n){
            System.arraycopy(nums2,p2,nums1,p1+p2,m+n-p1-p2);
        }
        return nums1;
    }

}
