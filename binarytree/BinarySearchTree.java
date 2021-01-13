package binarytree;

import datastructure.TreeNode;

public class BinarySearchTree {

    // 1. Validate Binary Search Tree
    public class ResultType {
        public boolean isBST;
        public TreeNode maxNode, minNode;
        public ResultType(boolean isBST) {
            this.isBST = isBST;
            this.maxNode = this.minNode = null;
        }
    }
    /**
     * @param root: The root of binary tree.
     * @return True if the binary tree is BST, or false
     */
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
        // 当前树最大最小节点
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

    // 2. Convert Binary Search Tree to Doubly Linked List
    public class DoublyListNode {
        int val;
        DoublyListNode next, prev;
        DoublyListNode(int val) {
            this.val = val;
            this.next = this.prev = null;
        }
    }
    class ResultTypeLL {
        DoublyListNode first, last;
        // 当前双向链表的第一个节点，最后一个节点
        public ResultTypeLL(DoublyListNode first, DoublyListNode last) {
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
        ResultTypeLL result = helper(root);
        // 返回结果的第一个节点
        return result.first;
    }
    // 中序遍历
    private ResultTypeLL helper(TreeNode root) {
        if (root == null) {
            return null;
        }

        ResultTypeLL left = helper(root.left);
        ResultTypeLL right = helper(root.right);

        DoublyListNode node = new DoublyListNode(root.val);
        ResultTypeLL result = new ResultTypeLL(null, null); // 当前结果的第一个节点，最后一个节点先置null

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

    // 3. Flatten Binary Tree to Linked List 用前序遍历
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
    private TreeNode helperDC(TreeNode root) {
        if (root == null) {
            return null;
        }

        TreeNode leftLast = helperDC(root.left);
        TreeNode rightLast = helperDC(root.right);

        if (leftLast != null) {
            leftLast.right = root.right; // 左子树的最后一个点的right连到root的右子树上（根左右）
            root.right = root.left; // 左子树的第一个点连到原来root的右边
            root.left = null;
        }

        if (rightLast != null) { // right不空，right是新链表的最后一个元素
            return rightLast;
        }
        if (leftLast != null) { // 这种情况下right就已经空了，否则上一条命令会返回
                                // left不空，left是新链表的最后一个元素
            return leftLast;
        }

        return root;
    }

}
