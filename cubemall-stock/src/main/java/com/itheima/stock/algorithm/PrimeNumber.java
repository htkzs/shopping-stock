package com.itheima.stock.algorithm;

import java.util.Scanner;

//暴力算法求给定数值范围内素数的个数
public class PrimeNumber {
    public static int primeNum(int number){
        int count = 0;
        for(int i =2;i<number;i++){
            count += prime(i)?1:0;
        }
        return count;
    }
    private static boolean prime(int i) {
        for (int j =2;j<i;j++){
            if (i%j == 0){
                return false;
            }
        }
        return true;
    }

    /*
    private static boolean prime(int i) {
        for (int j = 2; j * j <= i; j++){
            if (i%j == 0){
                return false;
            }
        }
        return true;
    }
   */
   //埃氏筛选法
    public static int eratosthenes(int n){
        boolean[] isPrime = new boolean[n]; //false代表素数 true标记合数
        int count = 0;
        for (int i=2;i<n;i++){
            if(!isPrime[i]){
                count++;
                for(int j =2*i;j<n;j+=i){ //j用来标记合数
                    isPrime[j] = true;
                }
            }
        }
        return count;
    }
    //优化后的埃氏筛选法
    public static int optimize_eratosthenes(int n){
        boolean[] isPrime = new boolean[n]; //false代表素数 true标记合数
        int count = 0;
        for (int i=2;i<n;i++){
            if(!isPrime[i]){
                count++;
                for(int j =i*i;j<n;j+=i){ //j用来标记合数
                    isPrime[j] = true;
                }
            }
        }
        return count;
    }


    public static void main(String[] args) {
        System.out.println("请输入一个大于2的int数值:");
        Scanner scanner = new Scanner(System.in);
        //会阻塞线程的执行 等待用户输入结果
        int i = scanner.nextInt();
        System.out.println("用户输入的数字是："+i);
        System.out.println(PrimeNumber.eratosthenes(i));;
        System.out.println(PrimeNumber.optimize_eratosthenes(i));
        // int primeNum = PrimeNumber.primeNum(i);
        //System.out.println("求得的素数的个数为"+primeNum);


    }
}
