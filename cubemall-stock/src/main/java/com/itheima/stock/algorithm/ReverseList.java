package com.itheima.stock.algorithm;

/*
链表反转
解法1∶迭代，重复某一过程，每一次处理结果作为下一次处理的初始值，这些初始值类似于状态、每次处理都会改变状态、直至到达最终状态
从前往后遍历链表，将当前节点的next指向上一个节点，因此需要一个变量存储上一个节点prev，当前节点处理
完需要寻找下一个节点，因此需要一个变量保存当前节点curr，处理完后要将当前节点赋值给prev，并将next指针赋伯给curr，因此需要一个变量提前保存下一个节点的指针next
 */
public class ReverseList {
    //声明一个数据Node节点的数结构
    static class ListNode {
        int value;
        ListNode next;

        public ListNode(int value, ListNode next) {
            this.value = value;
            this.next = next;
        }

        @Override
        public String toString() {
            return "ListNode{" +
                    "value=" + value +
                    ", next=" + next +
                    '}';
        }
    }
         public static ListNode iterate(ListNode head) {
            //声明两个存储变量
            ListNode prev = null,next;
            //定义一个循坏变量
            ListNode curr = head;
            while(curr != null) {
                //使用next变量保存 指向下一个节点的指针
                next = curr.next;
                //将元素的next的指向置为null
                curr.next = prev;
                //使用prev保存当前节点元素引用
                prev = curr;
                // 移动当前指针指向下一个节点
                curr = next;
            }
            return prev;
         }
         public static ListNode recursion(ListNode head){
            //如果链表为空 则不需要反转 直接返回head 如果节点的next为null代表当前节点为最后一个节点
             if(head == null || head.next == null){
                 return head;
             }
            //递归逻辑 一次反转两个节点 从后向前反转
             ListNode new_head = recursion(head.next); //递归找到最后一个节点
             //两个元素反转指针对的指向
             head.next.next = head;
             //防止发生环路
             head.next = null;
             return new_head;
         }
        public static void main(String[] args) {
            ListNode node5 = new ListNode(5,null);
            ListNode node4 = new ListNode(4,node5);
            ListNode node3 = new ListNode(5,node4);
            ListNode node2 = new ListNode(2,node3);
            ListNode node1 = new ListNode(1,node2);
            //ListNode prev = ReverseList.iterate(node1);
            ListNode new_list = recursion(node1);
            System.out.println(new_list);

            //System.out.println(prev);
        }
}
