package binarytree;

import datastructure.TreeNode;

public class ResultTypeProb {

    // 1. Balanced Binary Tree 返回是否是AVL
    private class ResultTypeBBT {
        public boolean isBalanced;
        public int maxDepth;
        public ResultTypeBBT(boolean isBalanced, int maxDepth) {
            this.isBalanced = isBalanced;
            this.maxDepth = maxDepth;
        }
    }

    /**
     * @param root: The root of binary tree.
     * @return True if this Binary tree is Balanced, or false.
     */
    public boolean isBalanced(TreeNode root) {
        return helper(root).isBalanced;
    }

    private ResultTypeBBT helper(TreeNode node) {

        if (node == null) {
            return new ResultTypeBBT(true, 0);
        }

        ResultTypeBBT left = helper(node.left);
        ResultTypeBBT right = helper(node.right);

        // subtree not balanced
        if (!left.isBalanced || !right.isBalanced) {
            return new ResultTypeBBT(false, -1);
        }

        // root not balanced
        if (Math.abs(left.maxDepth - right.maxDepth) > 1) {
            return new ResultTypeBBT(false, -1);
        }

        return new ResultTypeBBT(true, Math.max(left.maxDepth, right.maxDepth) + 1);
    }



    // 2. Subtree with Maximum Average
    private class ResultTypeMA {
        public int sum, size;
        public ResultTypeMA(int sum, int size) {
            this.sum = sum;
            this.size = size;
        }
    }

    private TreeNode subtree = null;
    private ResultTypeMA subtreeResult = null;

    /**
     * @param root: the root of binary tree
     * @return the root of the maximum average of subtree
     */
    public TreeNode findSubtree(TreeNode root) {
        helperMA(root);
        return subtree;
    }
    private ResultTypeMA helperMA(TreeNode node) {
        if (node == null) {
            return new ResultTypeMA(0, 0);
        }
        // 分治取左右子树的average
        ResultTypeMA left = helperMA(node.left);
        ResultTypeMA right = helperMA(node.right);

        // 当前subtree的结果
        ResultTypeMA result = new ResultTypeMA(left.sum + right.sum + node.val,
                                               left.size + right.size + 1);

        // 打擂台比较最大平均数
        if (subtree == null ||
            subtreeResult.sum * result.size < result.sum * subtreeResult.size) { // int型，用乘法不用除法

            subtree = node;
            subtreeResult = result;
        }

        return result;
    }

    // 3. Lowest Common Ancestor
    // 两个节点A, B的最近公共父节点(LCA)

    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode node1, TreeNode node2) {
        if (root == null) {
            return null;
        }

        // 如果root为A或者B，则直接返回自己
        if (root == node1 || root == node2) {
            return root;
        }

        // Divide 分别在左右子树中找A, B
        TreeNode left = lowestCommonAncestor(root.left, node1, node2);
        TreeNode right = lowestCommonAncestor(root.right, node1, node2);

        // Conquer
        if (left != null && right != null) {
            return root;
        }
        if (left != null && right == null) { // right空
            return left;
        }
        if (right != null && left == null) { // left空
            return right;
        }

        return null; // 没找到A, B 返回null
    }
}
