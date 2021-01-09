package binarytree;

import java.util.*;

public class TraverseAndDivideConquer {

    // 1. Maximum Depth of Binary Tree
    // divide conquer
    /**
     * @param root: The root of binary tree.
     * @return An integer depth.
     */
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

    // 2. Binary Tree Paths
    /**
     * @param root: the root of the binary tree
     * @return all root-to-leaf paths
     */
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

    // 3. Minimum Subtree
    private int minSum;
    private TreeNode minRoot;
    /**
     * @param root: the root of binary tree
     * @return the root of the minimum subtree
     */
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

        int sum = getSum(node.left) + getSum(node.right) + node.val;
        if (sum < minSum) {
            minSum = sum;
            minRoot = node;
        }

        return sum;
    }



}
