package com.itheima.stock.algorithm;
/*
总共有n枚硬币,将它们摆成一个阶梯形状,第k行就必须正好有k枚硬币。
给定一个数字n，找出可形成完整阶梯行的总行数。n是一个非负整数，并且在32位有符号整型的范围内。
 */
public class arrangeCoins {
    private static int numberRows(int coinsNum){
        //使用变量i记录行数 和每行所放的硬币数
        for (int i = 1; i <= coinsNum; i++) {
            //放完第i行后还有的硬币总数
            coinsNum = coinsNum - i;
            if(coinsNum <= i){
                return i;
            }
        }
        return 0;
    }
    //二分查找算法求解
    private static int binarySearchNumbersRows(int coinsNum){
        //定义两个边界条件
        int low = 0,high = coinsNum;
        while(low <= high){
            int middle = (high - low)/2+low;
            //计算middle行消耗的硬币总数
            int sumCoins = (middle+1)*middle/2;
            if(coinsNum == sumCoins){
                return middle;
            } else if (coinsNum < sumCoins){
                high = middle-1;
            }else{
                low = middle+1;
            }
        }
        return high;
    }

    public static void main(String[] args) {
        System.out.println(numberRows(10));
        System.out.println(binarySearchNumbersRows(28));
    }
}
