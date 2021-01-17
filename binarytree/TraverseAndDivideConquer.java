package binarytree;

import datastructure.TreeNode;

import java.util.*;

public class TraverseAndDivideConquer {

    // 1. Maximum Depth of Binary Tree
    // 给定一个二叉树，找出其最大深度
    public class MaxDepthSolution {
        // divide conquer
        public int maxDepth(TreeNode root) {
            if (root == null) {
                return -1;
            }
            int left = maxDepth(root.left);
            int right = maxDepth(root.right);

            return Math.max(left, right) + 1;
        }

        // traverse
        private int depth;
        public int maxDepthTraverse(TreeNode root) {
            depth = -1;
            helper(root, 0);
            return depth;
        }
        private void helper(TreeNode node, int curDepth) {
            if (node == null) {
                return;
            }
            if (curDepth > depth) {
                depth = curDepth;
            }

            helper(node.left, curDepth + 1);
            helper(node.right, curDepth + 1);
        }
    }

    // 2. Binary Tree Paths
    // 给一棵二叉树，找出从根节点到叶子节点的所有路径
    // 输入：{1,2,3,#,5}
    // 输出：["1->2->5","1->3"]
    public class BinaryTreePathsSolution {

        public List<String> binaryTreePaths(TreeNode root) {
            List<String> paths = new ArrayList<>();

            if (root == null) {
                return paths;
            }

            List<String> leftPaths = binaryTreePaths(root.left);
            List<String> rightPaths = binaryTreePaths(root.right);

            for (String path: leftPaths) {
                paths.add(root.val + "->" + path);
            }
            for (String path: rightPaths) {
                paths.add(root.val + "->" + path);
            }
            // if root is a leaf
            if (paths.size() == 0) {
                paths.add("" + root.val);
            }
            return paths;
        }
    }


    // 3. Minimum Subtree
    // 给一棵二叉树, 找到和为最小的子树, 返回其根节点
    // 输入输出数据范围都在int内
    public class MinSubTreeSolution {
        private int minSum;
        private TreeNode minRoot;

        public TreeNode findSubtree(TreeNode root) {
            minSum = Integer.MAX_VALUE;
            minRoot = null;

            getSum(root);
            return minRoot;
        }
        private int getSum(TreeNode node) {
            if (node == null) {
                return 0;
            }

            // divide 分别得到left和right的sum
            // conquer 左右相加再加node.val
            int sum = getSum(node.left) + getSum(node.right) + node.val;
            if (sum < minSum) {
                minSum = sum;
                minRoot = node;
            }

            return sum;
        }
    }

}
