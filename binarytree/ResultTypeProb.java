package binarytree;

import datastructure.TreeNode;

public class ResultTypeProb {

    // 1. Balanced Binary Tree
    // 110
    // 给定一个二叉树,确定它是高度平衡的（两个子树的深度相差不会超过1）
    // 返回是否是AVL
    public class BalancedTreeSolution {

        private class ResultType {
            public boolean isBalanced;
            public int maxDepth;
            public ResultType(boolean isBalanced, int maxDepth) {
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

        private ResultType helper(TreeNode node) {
            if (node == null) {
                return new ResultType(true, 0);
            }

            ResultType left = helper(node.left);
            ResultType right = helper(node.right);

            // subtree not balanced
            if (!left.isBalanced || !right.isBalanced) {
                return new ResultType(false, -1);
            }

            // current node not balanced
            if (Math.abs(left.maxDepth - right.maxDepth) > 1) {
                return new ResultType(false, -1);
            }

            return new ResultType(true, Math.max(left.maxDepth, right.maxDepth) + 1);
        }
    }


    // 2. Subtree with Maximum Average
    // 1120
    // 给一棵二叉树，找到有最大平均值的子树
    // 返回子树的根结点
    public class MaxAverageSubTreeSolution {

        private class ResultType {
            public int sum, size;
            public ResultType(int sum, int size) {
                this.sum = sum;
                this.size = size;
            }
        }

        private TreeNode subtree;
        private ResultType subtreeResult;

        public TreeNode findSubtree(TreeNode root) {
            subtree = null;
            subtreeResult = null;

            helper(root);
            return subtree;
        }

        private ResultType helper(TreeNode node) {
            if (node == null) {
                return new ResultType(0, 0);
            }

            // 分治取左右子树的average
            ResultType left = helper(node.left);
            ResultType right = helper(node.right);

            // 当前subtree的结果
            ResultType result = new ResultType(left.sum + right.sum + node.val,
                                               left.size + right.size + 1);

            // 打擂台比较最大平均数
            if (subtree == null ||
                subtreeResult.sum * result.size < result.sum * subtreeResult.size) { // int型，用乘法不用除法

                subtree = node;
                subtreeResult = result;
            }

            return result;
        }
    }


    // 3. Lowest Common Ancestor
    // 236
    // 给定一棵二叉树，找到两个节点A, B的最近公共父节点(LCA)
    // 最近公共祖先是两个节点的公共的祖先节点且具有最大深度
    public class LowestCommonAncestorSolution {

        public TreeNode lowestCommonAncestor(TreeNode root, TreeNode node1, TreeNode node2) {
            if (root == null) {
                return null;
            }

            // 如果root为A或者B，则直接返回自己
            // 从大的层面看，如果tree根节点为任一node，则返回根节点
            // 从小的层面看，当前subtree的节点为node1或node2，则返回当前subtree的root
            if (root == node1 || root == node2) {
                return root;
            }

            // Divide 分别在左右子树中找A, B这两个node
            TreeNode left = lowestCommonAncestor(root.left, node1, node2);
            TreeNode right = lowestCommonAncestor(root.right, node1, node2);

            // Conquer
            if (left != null && right != null) { // left和right各自都能找到A或B
                return root;
            }
            if (left != null && right == null) { // left有A或B，right空，得返回left的东西（left其实是之前返回的node1或者2）
                return left;
            }
            if (right != null && left == null) { // right有A或B
                return right;
            }

            return null; // 当前subtree中没找到A, B这两个点，返回null
        }
    }

    // 1644. Lowest Common Ancestor of a Binary Tree II
    // 同上一题236，差别在与本题可能有node不在tree中，要返回null
    // 因此，需要遍历整棵树，不能提前返回
    public class LCA2Solution {
        private boolean pFound = false;
        private boolean qFound = false;
        public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {

            TreeNode lcaNode = lca(root, p, q);
            if (pFound && qFound) { // p和q两个点都在tree中可以找到
                return lcaNode;
            } else {
                return null;
            }
        }
        private TreeNode lca(TreeNode root, TreeNode p, TreeNode q) {
            if (root == null) {
                return null;
            }

            TreeNode left = lca(root.left, p, q);
            TreeNode right = lca(root.right, p, q);

            // 这个要放在divide后面，要不然测试不过？？？
            if (root == p) {
                pFound = true;
                return root;
            }
            if (root == q) {
                qFound = true;
                return root;
            }

            if (left == null && right != null) {
                return right;
            }
            if (left != null && right == null) {
                return left;
            }
            if (left != null && right != null) {
                return root;
            }

            return null;
        }
    }

}
