package linkedlist;

import datastructure.*;

import java.util.*;

public class SortList {

    // 1. Sort List
    // 在 O(nlogn) 时间复杂度和常数级的空间复杂度下给链表排序
    public class SortListSolution {
        public ListNode sortList(ListNode head) {
            if (head == null || head.next == null) {
                return head;
            }

            ListNode mid = findMiddle(head);

            ListNode right = sortList(mid.next);
            mid.next = null;
            ListNode left = sortList(head);

            return merge(left, right);
        }
        private ListNode findMiddle(ListNode head) {
            ListNode slow = head, fast = head.next;
            while (fast != null && fast.next != null) {
                slow = slow.next;
                fast = fast.next.next;
            }
            return slow;
        }
        private ListNode merge(ListNode head1, ListNode head2) {
            ListNode dummy = new ListNode(0);
            ListNode tail = dummy;

            while (head1 != null && head2 != null) {
                if (head1.val < head2.val) {
                    tail.next = head1;
                    head1 = head1.next;
                } else {
                    tail.next = head2;
                    head2 = head2.next;
                }
                tail = tail.next;
            }

            if (head1 != null) {
                tail.next = head1;
            } else {
                tail.next = head2;
            }

            return dummy.next;
        }
    }

    // 2. convert sorted list to balanced bst
    // 给出一个所有元素以升序排序的单链表，将它转换成一棵高度平衡的二叉搜索树
    public class SortedListToBSTSolution {
        // O(nlogn)
        public TreeNode sortedListToBST(ListNode head) {
            return buildTree(head, null);
        }

        public TreeNode buildTree(ListNode left, ListNode right) {
            if (left == right) {
                return null;
            }
            ListNode mid = getMedian(left, right);
            TreeNode root = new TreeNode(mid.val);
            root.left = buildTree(left, mid); // 左闭右开区间，因为不知道mid的前一个节点，所以不用左闭右闭mid-1这种方法
            root.right = buildTree(mid.next, right);
            return root;
        }

        public ListNode getMedian(ListNode left, ListNode right) {
            ListNode fast = left;
            ListNode slow = left;
            while (fast != right && fast.next != right) {
                fast = fast.next;
                fast = fast.next;
                slow = slow.next;
            }
            return slow;
        }

        // O(n)
        // BST的中序遍历就是按升序排列的链表
        ListNode globalHead;

        public TreeNode sortedListToBST2(ListNode head) {
            globalHead = head;
            int length = getLength(head);
            return buildTree(0, length - 1); // 左闭右闭区间（下标）
        }

        public int getLength(ListNode head) {
            int length = 0;
            while (head != null) {
                length++;
                head = head.next;
            }
            return length;
        }

        public TreeNode buildTree(int left, int right) {
            if (left > right) {
                return null;
            }
            int mid = (left + right) / 2;
            TreeNode root = new TreeNode(0);
            // 左根右
            root.left = buildTree(left, mid - 1);
            root.val = globalHead.val;
            globalHead = globalHead.next; // 当前层head往后走一步，BST中序遍历根节点的值和链表升序对应上
            root.right = buildTree(mid + 1, right);
            return root;
        }
    }

    // 3. delete node in the middle of singly linked list
    // 给定一个单链表中的一个等待被删除的节点(非表头或表尾) 在 O(1) 时间复杂度删除该链表节点
    public class DeleteNodeSolution {
        public void deleteNode(ListNode node) {
            if (node == null || node.next == null) {
                return;
            }
            ListNode temp = node.next;
            node.val = temp.val;
            node.next = temp.next;

            return;
        }
    }

}
