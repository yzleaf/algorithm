package linkedlist;

import datastructure.ListNode;

import java.util.*;

public class LinkedList {

    // 1. 翻转链表 · Reverse Linked List
    // 输入: 1->2->3->null
    // 输出: 3->2->1->null
    public class RerverseSolution {
        /**
         * @param head: The head of linked list.
         * @return The new head of reversed linked list.
         */
        // .1 iteration
        public ListNode reverse(ListNode head) {
            //prev表示前继节点
            ListNode prev = null;
            while (head != null) {
                //temp记录下一个节点，head是当前节点
                ListNode temp = head.next;
                head.next = prev;
                prev = head;
                head = temp;
            }
            return prev;
        }

        // .2 递归做法
        public ListNode reverseRecursion(ListNode head) {
            if (head == null || head.next == null) {
                return head;
            }
            ListNode next = head.next;
            // 姑且相信递归可以把后面的翻转都搞定，并且找到最后的节点（也就是新表的头）
            // 一直向上传递，但newHead没有再改变了
            ListNode newHead = reverseRecursion(next);
            next.next = head;
            head.next = null;
            return newHead;
        }
    }

    // 2. Reverse Nodes in k-Group
    // 将这个链表从头指针开始每k个翻转一下
    // 链表元素个数不是k的倍数，最后剩余的不用翻转
    public class ReverseKGroupSolution {
        public ListNode reverseKGroup(ListNode head, int k) {

            ListNode dummy = new ListNode(0); // 指向整个链表的head节点
            dummy.next = head;

            head = dummy; // head会不断变化
            while (true) {
                head = reverseK(head, k);
                if (head == null) {
                    break;
                }
            }

            return dummy.next;
        }
        // head -> ( n1 -> n2 ... nk ) -> nk+1
        // =>
        // head -> ( nk -> nk-1 .. n1 ) -> nk+1
        // return n1 （返回指向下一个k链表的节点）
        private ListNode reverseK(ListNode head, int k) {
            // nk从头节点开始不断向后遍历
            ListNode nk = head;
            for (int i = 0; i < k; i++) { // 因为head=dummy，所以此循环到达k各数的最后一个元素
                                          // if判断是前k-1个元素
                if (nk == null) {
                    return null;
                }
                nk = nk.next;
            }

            if (nk == null) { // 跳出循环后，得到k个数里的最后一个元素
                return null;
            }

            // Reverse
            ListNode n1 = head.next;
            ListNode nkplus = nk.next;

            ListNode prev = null;
            ListNode curr = n1;
            while (curr != nkplus) {
                ListNode temp = curr.next;
                curr.next = prev;
                prev = curr;
                curr = temp;
            }

            // connect这k个数与其他
            head.next = nk;
            n1.next = nkplus;

            return n1;
        }

        // 方法2答案看起来更容易理解简洁一些
        public ListNode reverseKGroup2(ListNode head, int k) {

            if (k <= 0) {
                return head;
            }
            ListNode dummy = new ListNode(0);
            dummy.next = head;

            ListNode prev = dummy;
            ListNode curr = head;

            while (curr != null) {
                // get the tail node in a k-group
                int count = 1;
                ListNode tail = curr;
                while (tail != null && count < k) {
                    tail = tail.next;
                    count++;
                }
                // if the rest nodes length is less than k
                // then remains the same
                if (tail == null) {
                    break;
                }

                // do the k-group reverse
                //     prev   (before reversed)     next
                //      |             |              |
                // 2 -> 1   ->    (3  ->  4)    ->   5
                //                 |      |
                //                curr   tail
                ListNode next = tail.next;
                reverse(curr, k);

                //     prev   (after reversed)     next
                //      |             |              |
                // 2 -> 1   ->    (4  ->  3)    ->   5
                //                 |      |
                //                tail   curr
                prev.next = tail;
                curr.next = next;

                //             (after reversed)
                //                    |
                // 2 -> 1   ->    (4  ->  3)    ->   5
                //                        |          |
                //                       prev       curr
                prev = curr;
                curr = next;
            }
            return dummy.next;
        }

        private void reverse(ListNode head, int k) {
            ListNode prev = null;
            ListNode curr = head;
            for (int i = 0; i < k; i++) {
                ListNode next = curr.next;
                curr.next = prev;
                prev = curr;
                curr = next;
            }
        }
    }

    // 3. 复制带随机指针的链表 · Copy List with Random Pointer
    // 给出一个链表，每个节点包含一个额外增加的随机指针可以指向链表中的任何节点或空的节点
    // 返回一个深拷贝的链表
    public class CopyRandomListSolution {
        class RandomListNode {
            int label;
            RandomListNode next, random;
            public RandomListNode(int x) {
                label = x;
                next = null;
                random = null;
            }
        }
        // 方法1 HashMap新旧节点一一对应
        public RandomListNode copyRandomList(RandomListNode head) {
            if (head == null) {
                return null;
            }
            Map<RandomListNode, RandomListNode> map = new HashMap<>();
            RandomListNode dummy = new RandomListNode(0);
            RandomListNode pre = dummy;
            RandomListNode newNode;
            // head指向原始链表
            // pre和newNode指向新的链表

            while (head != null) {
                if (map.containsKey(head)) {
                    newNode = map.get(head);
                } else {
                    newNode = new RandomListNode(head.label);
                    map.put(head, newNode);
                }
                pre.next = newNode; // 与上一个节点连接

                if (head.random != null) {
                    if (map.containsKey(head.random)) {
                        newNode.random = map.get(newNode.random);
                    } else {
                        newNode.random = new RandomListNode(newNode.label);
                        map.put(head.random, newNode.random);
                    }
                }

                pre = newNode; // pre往后走一步到现在新增的这个head节点
                head = head.next; // head往后走一步
            }
            return dummy.next;
        }
        // 方法2 使用O(1)空间
        // 第一遍扫的时候运用next指针，开始数组是1->2->3->4，扫描过程中先建立copy节点 1->1`->2->2`->3->3`->4->4`
        // 第二遍copy的时候去建立边的copy，拆分节点, 一边扫描一边拆成两个链表
        // 这里用到两个dummy node。第一个链表变回  1->2->3 , 然后第二变成 1`->2`->3`
        public RandomListNode copyRandomList2(RandomListNode head) {
            if (head == null) {
                return null;
            }
            copyNext(head);
            copyRandom(head);
            return splitList(head);
        }
        private void copyNext(RandomListNode head) {
            while (head != null) {
                RandomListNode newNode = new RandomListNode(head.label);
                newNode.random = head.random; // newNode的random先指向head的random，方便后续确认是否为null，后面再copyRandom再操作
                newNode.next = head.next;
                head.next = newNode;
                head = head.next.next;
            }
        }
        private void copyRandom(RandomListNode head) {
            while (head != null) {
                if (head.next.random != null) { // 新建的newNode
                    head.next.random = head.random.next;
                }
                head = head.next.next;
            }
        }
        private RandomListNode splitList(RandomListNode head) {
            RandomListNode newHead = head.next;
            while (head != null) {
                RandomListNode temp = head.next;
                head.next = temp.next;
                head = head.next; // 跨过了temp
                if (temp.next != null) {
                    temp.next = temp.next.next;
                }
            }
            return newHead;
        }
    }

    // 4. Linked List Cycle
    // 给定一个链表，判断它是否有环
    public class HasCycleSolution {
        // 快指针每次走两步，慢指针一次走一步。
        // 在慢指针进入环之后，快慢指针之间的距离每次缩小1，所以最终能相遇。
        public boolean hasCycle(ListNode head) {
            if (head == null || head.next == null) {
                return false;
            }

            ListNode fast, slow;
            fast = head.next;
            slow = head;

            while (fast != slow) {
                if (fast == null || slow == null) {
                    return false;
                }
                fast = fast.next.next;
                slow = slow.next;
            }

            return true;
        }
    }

    // 5. Intersection of Two Linked Lists
    // 找到两个单链表最开始的交叉节点
    public class GetIntersectionNode {
        public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
            if (headA == null || headB == null) {
                return null;
            }

            // .1 分别找出两个长度
            ListNode currA = headA, currB = headB;
            int lengthA = 0, lengthB = 0;
            while (currA != null) {
                lengthA++;
                currA = currA.next;
            }
            while (currB != null) {
                lengthB++;
                currB = currB.next;
            }

            // .2 让长的先走到剩余长度和短的一样地方
            currA = headA;
            currB = headB;
            while (lengthA > lengthB) {
                currA = currA.next;
                lengthA--;
            }
            while (lengthB > lengthA) {
                currB = currB.next;
                lengthB--;
            }

            // .3 然后让两个同时走到第一个node相同的地方, 返回结果即可
            while (currA != currB) {
                currA = currA.next;
                currB = currB.next;
            }

            return currA;
        }

        // 方法2
        public ListNode getIntersectionNode2(ListNode headA, ListNode headB) {

            if (headA == null || headB == null) {
                return null;
            }

            ListNode a = headA;
            ListNode b = headB;
            // a,b分别从前往后遍历,将a的尾接到b的头，b的尾接到a的头（俩链表长度一样，最后的部分是相交的相同部分）
            // 接完后仍然继续遍历，如果有相交的部分，会在最后的相交点返回
            while (a != b) {
                a = a == null ? headB : a.next;
                b = b == null ? headA : b.next;
            }
            return a;
        }
    }

}
