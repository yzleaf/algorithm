package binarytree;

import datastructure.*;

public class BinarySearchTree {

    // 1. Validate Binary Search Tree
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
        private boolean divConq(TreeNode root, long min, long max){
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
    // 将一个二叉查找树按照中序遍历转换成双向链表
    public class BSTDoublyListSolution {

        class ResultType {
            DoublyListNode first, last;
            // 当前双向链表的第一个节点，最后一个节点
            public ResultType(DoublyListNode first, DoublyListNode last) {
                this.first = first;
                this.last = last;
            }
        }
        // 采用分治法
        // 把当前root连接上left子树的最后一个节点，连接上right子树的第一个节点
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
    }


    // 3. Flatten Binary Tree to Linked List 用前序遍历
    // 将一棵二叉树按照前序遍历拆解成为一个 假链表
    // 假链表：用二叉树的 right 指针，来表示链表中的 next 指针。
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
            flatten(root.left);
            flatten(root.right);
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

            if (rightLast != null) { // right不空，right是新链表的最后一个元素
                return rightLast;
            }
            if (leftLast != null) { // 这种情况下right就已经空了，否则上一条命令会返回
                // left不空，left是新链表的最后一个元素
                return leftLast;
            }

            return node;
        }
    }

    // 4. Convert BST to Greater Tree
    // High Frequency
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
}
