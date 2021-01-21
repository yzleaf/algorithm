package linkedlist;

import datastructure.ListNode;

public class DummyNode {

    // 3. 链表划分 · Partition List
    // 给定一个单链表和数值x，划分链表使得所有小于x的节点排在大于等于x的节点之前
    // 输入: 1->4->3->2->5->2->null, x = 3
    // 输出: 1->2->2->4->3->5->null
    // 样例解释: 要保持原有的相对顺序。
    public class PartitionSolution {

        // 双指针方法，用两个指针将两个部分分别串起来。最后在将两个部分拼接起来。
        // left指针用来串起来所有小于x的结点，right指针用来串起来所有大于等于x的结点。
        public ListNode partition(ListNode head, int x) {
            if (head == null) {
                return null;
            }

            ListNode leftDummy = new ListNode(0);
            ListNode rightDummy = new ListNode(0);
            ListNode left = leftDummy;
            ListNode right = rightDummy;

            while (head != null) {
                if (head.val < x) {
                    left.next = head;
                    left = head; // left走到跟head一样（相当于到了left.next)
                } else { // >=
                    right.next = head;
                    right = head;
                }
                head = head.next;
            }

            // 合并两部分链表
            right.next = null;
            left.next = rightDummy.next;

            return leftDummy.next;
        }
    }

    // 4. 合并两个排序链表 · Merge Two Sorted Lists
    // 输入:  list1 =  1->3->8->11->15->null, list2 = 2->null
    // 输出: 1->2->3->8->11->15->null
    public class MergeTwoListsSolution {
        public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
            ListNode dummy = new ListNode(0);
            ListNode lastNode = dummy;

            while (l1 != null && l2 != null) {
                if (l1.val < l2.val) {
                    lastNode.next = l1;
                    l1 = l1.next;
                } else {
                    lastNode.next = l2;
                    l2 = l2.next;
                }
                lastNode = lastNode.next;
            }

            if (l1 != null) { // l2已经遍历完成
                lastNode.next = l1;
            } else { // l1已经遍历完成
                lastNode.next = l2;
            }

            return dummy.next;
        }
    }

    // 5. Reverse Linked List II
    // 翻转链表中第m个节点到第n个节点的部分
    // 输入: 1->2->3->4->5->NULL, m = 2 and n = 4,
    // 输出: 1->4->3->2->5->NULL.
    public class ReverseBetweenSolution {
        public ListNode reverseBetween(ListNode head, int m, int n) {
            if (m >= n || head == null) {
                return head;
            }

            ListNode dummy = new ListNode(0);
            dummy.next = head;

            head = dummy;
            for (int i = 0; i < m; i++) {
                if (head == null) {
                    return null;
                }
                head = head.next; // 不断往后走，到m的前一个数
            }

            ListNode premNode = head;
            ListNode mNode = head.next;
            ListNode nNode = mNode;
            ListNode postnNode = nNode.next;

            for (int i = m; i < n; i++) { // 对于nNode, 只到第n-1个数
                if (postnNode == null) {
                    return null;
                }
                ListNode temp = postnNode.next;
                postnNode.next = nNode;
                nNode = postnNode; // 最后一次循环执行完这一句，nNode才到区间的最后
                postnNode = temp;
            }

            // 与外部连接
            mNode.next = postnNode;
            premNode.next = nNode; // premNode.next保存了原来头部之后翻转到尾部的节点
            // 现在需要连上原来尾部，现在的头部节点
            return dummy.next;
        }
    }

    // 6. Swap Two Nodes in Linked List
    // 交换链表中值为v1和v2的这两个节点（交换节点，不仅仅交换值）
    public class SwapNodesSolution {
        // 如果二者本身是相邻的, 则一共需要修改三条边(即三个next关系)
        // {node} -> {v = v1} -> {v = v2} -> {node}
        // 如果二者是不相邻的, 则一共需要修改四条边
        // {node} -> {v = v1} -> {some nodes} -> {v = v2} -> {node} (假定v1在v2前)
        public ListNode swapNodes(ListNode head, int v1, int v2) {
            ListNode dummy = new ListNode(0);
            dummy.next = head;

            ListNode node1Prev = null, node2Prev = null;
            ListNode cur = dummy;
            while (cur.next != null) {
                if (cur.next.val == v1) {
                    node1Prev = cur;
                } else if (cur.next.val == v2) {
                    node2Prev = cur;
                }
                cur = cur.next;
            }

            if (node1Prev == null || node2Prev == null) {
                return head;
            }

            if (node2Prev.next == node1Prev) { // 应该可以放到后面的if判断条件中
                // make sure node2Prev.next is not node1Prev
                ListNode t = node1Prev;
                node1Prev = node2Prev;
                node2Prev = t;
            }

            ListNode node1 = node1Prev.next;
            ListNode node2 = node2Prev.next;
            ListNode node2Next = node2.next;
            if (node1Prev.next == node2Prev) { // 相邻
                node1Prev.next = node2;
                node2.next = node1;
                node1.next = node2Next;
            } else { // 不相邻
                node1Prev.next = node2;
                node2.next = node1.next;

                node2Prev.next = node1;
                node1.next = node2Next;
            }

            return dummy.next;
        }
    }

    // 7. Reorder List
    // 给定一个单链表L: L0→L1→…→Ln-1→Ln,
    // 重新排列后为：L0→Ln→L1→Ln-1→L2→Ln-2→…
    public class ReorderListSolution {
        // 先找到中点，然后把后半段倒过来，然后前后交替合并
        public void reorderList(ListNode head) {
            if (head == null || head.next == null) {
                return;
            }

            ListNode mid = findMiddle(head);
            ListNode tail = reverse(mid.next); // mid.next开始的后半段全部翻转
            mid.next = null; // 置空->变成两部分链表

            merge(head, tail);
        }

        private ListNode findMiddle(ListNode head) {
            ListNode slow = head;
            ListNode fast = head.next;
            // mid取到前半部分, fast从head.next开始, 否则slow会多走一步才跳出循环
            // (L1, L2, L3,) L4, L5, L6
            // mid = L3
            while (fast != null && fast.next != null) {
                fast = fast.next.next;
                slow = slow.next;
            }
            return slow;
        }
        private ListNode reverse(ListNode head) {
            ListNode newHead = null;
            while (head != null) {
                ListNode temp = head.next;
                head.next = newHead;
                newHead = head;
                head = temp;
                // 这一连串都是赋值给别人以后改变自己
            }
            return newHead;
        }
        private void merge(ListNode head1, ListNode head2) {
            int index = 0; // 判断奇偶，决定哪条链
            ListNode dummy = new ListNode(0);
            while (head1 != null && head2 != null) {
                if (index % 2 == 0) {
                    dummy.next = head1;
                    head1 = head1.next;
                } else {
                    dummy.next = head2;
                    head2 = head2.next;
                }
                dummy = dummy.next;
                index++;
            }

            if (head1 != null) {
                dummy.next = head1;
            } else {
                dummy.next = head2;
            }
        }

    }

    // 8. Rotate List
    // 给定一个链表，旋转链表，使得每个节点向右移动k个位置（k非负）
    // 输入：1->2->3->4->5  k = 2
    // 输出：4->5->1->2->3
    public class RotateListSolution {

        public ListNode rotateRight(ListNode head, int k) {
            if (head == null) {
                return null;
            }

            int length = getLength(head);
            k = k % length; // 取余数，适用大于链表长度的情况

            ListNode dummy = new ListNode(0);
            dummy.next = head;
            head = dummy;
            ListNode tail = dummy; // 通过tail找到要rotate的边界

            for (int i = 0; i < k; i++) {
                head = head.next;
            }
            while (head.next != null) {
                tail = tail.next;
                head = head.next;
            }

            // k = 2
            //          |                  tail  |      head
            // dummy -> | L1 -> L2 -> L3 -> L4 ->| L5 -> L6
            //          |                        |
            head.next = dummy.next;
            dummy.next = tail.next;
            tail.next = null;

            return dummy.next;
        }
        private int getLength(ListNode head) {
            int length = 0;
            while (head != null) {
                length++;
                head = head.next;
            }
            return length;
        }
    }
}
