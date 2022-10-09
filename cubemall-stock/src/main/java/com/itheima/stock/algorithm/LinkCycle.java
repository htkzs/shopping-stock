package com.itheima.stock.algorithm;

import java.util.HashSet;
import java.util.Set;

/*
环形链表
给定一个链表，判断链表中是否有环。
如果链表中有某个节点,可以通过连续跟踪next 指针再次到达该节点，则链表中存在环
如果链表中存在环，则返回 true。否则，返回false 。
 */
public class LinkCycle {
    //定义节点的数据结构
    static class ListNode{
        int value;
        ListNode next;

        public ListNode(int value, ListNode next) {
            this.value = value;
            this.next = next;
        }
    }
    //以空间换取效率 暴力破解 时间复杂度和空间复杂度都为O(n)
    private static boolean hasCycle(ListNode head){
        Set<ListNode> set  = new HashSet<ListNode>();
        //遍历链表
        while(head != null){
            if(! set.add(head)){
                return true;
            }
            //相当于移动指针
            head = head.next;
        }
        return false;
    }
    //双指针法
    private static boolean twoPointer(ListNode head){
        if(head ==null || head.next == null){
            return false;
        }
        //定义一个快指针和一个慢指针
        ListNode slow = head;
        ListNode quick = head.next;
        while(slow != quick){
            //这里直接判断两次 减少判断的次数
            if(quick == null || quick.next == null){
                return false;
            }
            slow = slow.next;
            //目的让快指针提前进入环
            quick = quick.next.next;
        }
        return true;
    }

    public static void main(String[] args) {
        ListNode node5 = new ListNode(5,null);
        ListNode node4 = new ListNode(4,node5);
        ListNode node3 = new ListNode(3,node4);
        ListNode node2 = new ListNode(2,node3);
        ListNode node1 = new ListNode(1,node2);
        //设置使之成为一个环形链表
        node5.next = node1;
        System.out.println(hasCycle(node1));
        System.out.println(twoPointer(node1));
    }

}
