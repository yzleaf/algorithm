package binarytree;

import java.util.*;

import datastructure.*;

public class BinarySearchTree {

    // 1. Validate Binary Search Tree
    // 98
    public class ValidBSTSolution {

        public class ResultType {
            public boolean isBST;
            public TreeNode maxNode, minNode;
            public ResultType(boolean isBST) {
                this.isBST = isBST;
                this.maxNode = this.minNode = null;
            }
        }

        // 方法1
        public boolean isValidBST(TreeNode root) {
            return divConquer(root).isBST;
        }
        private ResultType divConquer(TreeNode root) {
            if (root == null) {
                return new ResultType(true);
            }

            ResultType left = divConquer(root.left);
            ResultType right = divConquer(root.right);

            if (!left.isBST || !right.isBST) { // 只要左右子树又非BST，整棵树就不是
                return new ResultType(false);
            }

            if (left.maxNode != null && left.maxNode.val >= root.val) {
                return new ResultType(false);
            }
            if (right.minNode != null && right.minNode.val <= root.val) {
                return new ResultType(false);
            }

            // is BST
            // 记录当前树最大最小节点
            ResultType result = new ResultType(true);
            result.minNode = left.minNode != null ? left.minNode : root;
            result.maxNode = right.maxNode != null ? right.maxNode : root;

            return result;
        }

        // 方法2
        public boolean isValidBST2(TreeNode root) {
            return divConq(root, Long.MIN_VALUE, Long.MAX_VALUE);
        }
        private boolean divConq(TreeNode root, long min, long max){ // 当前subtree的root，最小值min，最大值max
            if (root == null){
                return true;
            }
            if (root.val <= min || root.val >= max){
                return false;
            }
            // 代表左右子树的值域
            return divConq(root.left, min, Math.min(max, root.val)) &&
                    divConq(root.right, Math.max(min, root.val), max);
        }
    }

    // 2. Convert Binary Search Tree to Doubly Linked List
    // 426
    // 将一个二叉查找树按照中序遍历转换成双向链表
    public class BSTDoublyListSolution {
        // 方法1 分治
        // 方法2似乎更好理解一点
        class ResultType {
            DoublyListNode first, last;
            // 当前双向链表的第一个节点，最后一个节点
            public ResultType(DoublyListNode first, DoublyListNode last) {
                this.first = first;
                this.last = last;
            }
        }
        // 采用分治法
        // 左根右：把当前root连接上left子树的最后一个节点，连接上right子树的第一个节点
        public DoublyListNode bstToDoublyList(TreeNode root) {
            if (root == null) {
                return null;
            }
            ResultType result = helper(root);
            // 返回结果的第一个节点
            return result.first;
        }
        // 中序遍历
        private ResultType helper(TreeNode root) {
            if (root == null) {
                return null;
            }

            ResultType left = helper(root.left);
            ResultType right = helper(root.right);

            DoublyListNode node = new DoublyListNode(root.val);
            ResultType result = new ResultType(null, null); // 当前结果的第一个节点，最后一个节点先置null

            if (left == null) {
                result.first = node;
            } else {
                // 把root和left部分连接上
                result.first = left.first;
                left.last.next = node;
                node.prev = left.last;
            }

            if (right == null) {
                result.last = node;
            } else {
                // 把root和right部分连接上
                result.last = right.last;
                right.first.prev = node;
                node.next = right.first;
            }

            return result;
        }

        // 方法2 Traverse
        TreeNode first = null; // smallest
        TreeNode last = null; // largest

        public TreeNode treeToDoublyList(TreeNode root) {
            if (root == null) {
                return null;
            }

            inorder(root);

            // 最后，把前后节点连上
            first.left = last;
            last.right = first;

            return first;
        }
        private void inorder(TreeNode curr) {
            if (curr == null) {
                return;
            }

            inorder(curr.left);
            // 左边处理完，左边的最后一个节点

            if (last != null) {
                last.right = curr;
                curr.left = last;
            } else {
                first = curr;
            }
            last = curr;
            // 根处理完，把根设为最后一个节点

            inorder(curr.right);
            // 右边处理完

        }
    }


    // 3. Flatten Binary Tree to Linked List 用前序遍历
    // 114
    // 将一棵二叉树按照前序遍历拆解成为一个 假链表
    // 假链表：用二叉树的 right 指针，来表示链表中的 next 指针
    //     1
    //    / \
    //   2   5
    //  / \   \
    // 3   4   6
    //
    // 1
    //  \
    //   2
    //    \
    //     3
    //      \
    //       4
    //        \
    //         5
    //          \
    //           6

    public class FlattenBinaryTreeSolution {
        // traverse
        private TreeNode lastNode = null;
        public void flatten(TreeNode root) {
            if (root == null) {
                return;
            }

            // 当前链表的最后一个节点
            // 当有新的节点出现时，添加至链表中（用right来指向）
            if (lastNode != null) {
                lastNode.left = null;
                lastNode.right = root;
            }

            lastNode = root;
            TreeNode right = root.right; // 先保存右子树，否则会被递归left的操作冲掉
            flatten(root.left);
            flatten(right);
        }

        // divide and conquer
        public void flattenDC(TreeNode root) {
            helperDC(root);
        }
        private TreeNode helperDC(TreeNode node) {
            if (node == null) {
                return null;
            }

            TreeNode leftLast = helperDC(node.left);
            TreeNode rightLast = helperDC(node.right);

            if (leftLast != null) {
                leftLast.right = node.right; // 左子树的最后一个点的right连到root的右子树上（根左右）
                node.right = node.left; // 左子树的第一个点连到原来root的右边
                node.left = null;
            }

            // 返回当前链表的最后一个节点
            if (rightLast != null) { // right不空，right是新链表的最后一个元素
                return rightLast;
            }
            if (leftLast != null) { // 这种情况下right就已经空了，否则上一条命令会返回
                // left不空，left是新链表的最后一个元素
                return leftLast;
            }

            return node; // 都为空，才到这个点本身
        }
    }

    // 4. Convert BST to Greater Tree
    // 538 High Frequency
    // 使原始BST上每个节点的值都更改为在原始树中大于等于该节点值的节点值之和(包括该节点)
    // 输入 : {5,2,13}
    //              5
    //            /   \
    //           2     13
    // 输出 : {18,20,13}
    //             18
    //            /   \
    //          20     13
    public class GreaterTreeSolution {
        // 考查的是二叉树的中序遍历，由于要将节点累加上大于等于它的所有值，所以要优先遍历右子树，得到所有大于等于当前值的和。
        // 然后更新当前节点的值，再搜索左子树的值。
        // 右根左
        int sum = 0;
        public TreeNode convertBST(TreeNode root) {
            travel(root);
            return root;
        }
        private void travel(TreeNode cur) {
            if (cur == null) {
                return;
            }
            travel(cur.right);
            sum += cur.val;
            cur.val = sum; // 更新当前节点的值
            travel(cur.left);
        }
    }

    // 不同的二叉搜索树
    // 96
    // 给定一个整数 n，求以 1 ... n 为节点组成的二叉搜索树有多少种？
    // 输入: 3
    // 输出: 5
    // 解释: 给定 n = 3, 一共有 5 种不同结构的二叉搜索树:
    //   1         3     3      2      1
    //    \       /     /      / \      \
    //     3     2     1      1   3      2
    //    /     /       \                 \
    //   2     1         2                 3
    public class DifferentBST {
        // G(n): n个节点的BST个数
        // f(i): 以i为根节点的BST个数
        // 当i为根节点时，其BST左子树节点个数为i-1个，右子树节点为n-i，总个数f(i) = G(i−1) * G(n−i)
        // G(n) = f(1) + f(2) + ... + f(n)
        //      = G(0) * G(n-1) + G(1) * G(n-2) + ... + G(n-1) * G(0)
        public int numTrees(int n) {
            int[] dp = new int[n+1];
            dp[0] = 1; // 0个节点当作空树（1个树）
            dp[1] = 1;

            for (int i = 2; i <= n; i++) { // i个节点的BST个数
                for (int j = 0; j <= i - 1; j++) { // 计算左子树j个点和右子树i-j-1个点（扣除了i这个根节点）
                    dp[i] += dp[j] * dp[i-j-1];
                }
            }
            // 只需要管个数即可，因为个数定了，根节点的大小也是确定的（因为输入1->n）

            return dp[n];
        }
    }

    // 不同的二叉搜索树II
    // 95
    // 给定一个整数 n，生成所有由 1 ... n 为节点所组成的 二叉搜索树 。
    // 输入：3
    // 输出：
    // [
    //  [1,null,3,2],
    //  [3,2,null,1],
    //  [3,1,null,null,2],
    //  [2,1,3],
    //  [1,null,2,null,3]
    // ]
    public class DifferentBST2 {
        public List<TreeNode> generateTrees(int n) {
            List<TreeNode> result = new ArrayList<>();
            if (n == 0) {
                return result;
            }
            return getAns(1, n); // 从1->n
        }
        // 返回当前层从start到end的所有BST
        private List<TreeNode> getAns(int start, int end) {
            // 返回的是不同根节点的list
            List<TreeNode> result = new ArrayList<>();

            // .1 没有数字，null加入结果
            if (start > end) {
                result.add(null);
                return result;
            }

            // .2 有至少1个数，把每个数字i都作为根节点遍历
            for (int i = start; i <= end; i++) {
                List<TreeNode> leftTrees = getAns(start, i - 1); // 得到所有可能的左子树
                List<TreeNode> rightTrees = getAns(i + 1, end); // 得到所有可能的右子树

                // 从左子树集合中选出一棵左子树，从右子树集合中选出一棵右子树，拼接到根节点上
                // 每次都是把下一层的所有root节点取出来放在左子树和右子树的位置
                for (TreeNode left : leftTrees) {
                    for (TreeNode right : rightTrees) {
                        TreeNode currTree = new TreeNode(i);
                        currTree.left = left;
                        currTree.right = right;
                        result.add(currTree);
                    }
                }
            }

            return result;
        }
    }

    // 将有序数组转换为二叉搜索树
    // 108
    // 给你一个整数数组nums ，其中元素已经按升序排列，请你将其转换为一棵高度平衡二叉搜索树。
    public class SortedArrayToBSTSolution {
        public TreeNode sortedArrayToBST(int[] nums) {
            return helper(nums, 0, nums.length - 1);
        }
        private TreeNode helper(int[] nums, int start, int end) {
            if (start > end) {
                return null;
            }

            // 取中点作为根节点
            int mid = start + (end - start) / 2;
            TreeNode curr = new TreeNode(nums[mid]);
            curr.left = helper(nums, start, mid - 1);
            curr.right = helper(nums, mid + 1, end);

            return curr;
        }
    }
}
