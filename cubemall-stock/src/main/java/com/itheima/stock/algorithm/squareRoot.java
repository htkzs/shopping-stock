package com.itheima.stock.algorithm;

/*
在不使用sqrt(x)函数的情况下,得到x的平方根的整数部分
重点考察:二分法、牛顿迭代
二分法 查找一个数的平方根的整数部分
 */
public class squareRoot {
    public static int binarySearch(int x) {
        int index = -1, l = 0, r = x;
        while (l <= r) {
            int mid = l + (r - l) / 2;
            if (mid * mid <= x) {
                index = mid;
                l = mid + 1;
            } else {
                r = mid - 1;
            }
        }
        return index;
    }
    public static void main(String[] args) {
        System.out.println(squareRoot.binarySearch(25));
        System.out.println(squareRoot.newton(25));
    }
    //使用newton迭代法
    /*

     */
    private static int newton(int x) {
        if(x == 0){
            return 0;
        }
        return (int)sqrt(x,x);
    }
    //参数i就为要求取的一个目标值 为double类型
    //(i + x/i)/2 的由来可以解释为  如果 12 = 2*6  则2的平法为4 6的平法为36  （2+6）/2 =4 4的平法为 16为更接近于12的一个值
    /*
    则如果使用 x 参数代表12 2代表i x/i则为6  (i + x/i)/2 则为4
     */
    private static double sqrt(double i, int x) {
        double res = (i + x/i)/2;
        if(res == i){
            return i;
        }else{
            return sqrt(res,x);
        }

    }
}
