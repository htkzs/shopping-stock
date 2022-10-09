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
    //二分查找算法求解 时间复杂度 logn
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
    //牛顿迭代法 求取一个数的平方根的整数部分问题
    public static int newTunRows(int n){
        if(n == 0){
            return 0;
        }
        return (int)sqrt(n,n);
    }
    /*
    一个等差数列的合的一个求解 （x+x*x）/2 = n 则 x+x*x = 2n  x*x= zn-x  求取x 相当于求取一个数的平方根问题 可以使用牛顿迭代法
    牛顿迭代法的基本公式为 （x+n/x)/2  x为目标值 n为参数值
    （x+n/x)/2  将目标值替换为 zn-x 则变为  （x+ （2n-x）/x）/2
     */
    private static double sqrt(double x, int n) {
        double res = (x+ (2*n-x)/x)/2;
        if(res == x){
            return x;
        }else{
            return sqrt(res,n);
        }
    }

    public static void main(String[] args) {
        System.out.println(numberRows(10));
        System.out.println(binarySearchNumbersRows(28));
        System.out.println(newTunRows(28));
    }
}
