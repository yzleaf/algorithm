package binarytree;

import datastructure.TreeNode;

public class ResultTypeProb {

    // 1. Balanced Binary Tree
    // 给定一个二叉树,确定它是高度平衡的（两个子树的深度相差不会超过1）
    // 返回是否是AVL
    public class BalancedTreeSolution {

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

            // current node not balanced
            if (Math.abs(left.maxDepth - right.maxDepth) > 1) {
                return new ResultTypeBBT(false, -1);
            }

            return new ResultTypeBBT(true, Math.max(left.maxDepth, right.maxDepth) + 1);
        }
    }


    // 2. Subtree with Maximum Average
    // 给一棵二叉树，找到有最大平均值的子树
    // 返回子树的根结点
    public class MaxAverageSubTreeSolution {

        private class ResultTypeMA {
            public int sum, size;
            public ResultTypeMA(int sum, int size) {
                this.sum = sum;
                this.size = size;
            }
        }

        private TreeNode subtree;
        private ResultTypeMA subtreeResult;

        public TreeNode findSubtree(TreeNode root) {
            subtree = null;
            subtreeResult = null;

            helper(root);
            return subtree;
        }

        private ResultTypeMA helper(TreeNode node) {
            if (node == null) {
                return new ResultTypeMA(0, 0);
            }

            // 分治取左右子树的average
            ResultTypeMA left = helper(node.left);
            ResultTypeMA right = helper(node.right);

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
    }


    // 3. Lowest Common Ancestor
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

            // Divide 分别在左右子树中找A, B
            TreeNode left = lowestCommonAncestor(root.left, node1, node2);
            TreeNode right = lowestCommonAncestor(root.right, node1, node2);

            // Conquer
            if (left != null && right != null) {
                return root;
            }
            if (left != null && right == null) { // left有，right空，得返回left的东西（说到底就是最底层的node1或者2）
                return left;
            }
            if (right != null && left == null) { // left空
                return right;
            }

            return null; // 当前subtree中没找到A, B这两个点，返回null
        }
    }


}
