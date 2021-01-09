package binarytree;

import java.util.*;

public class TraverseBinaryTree {

    // 1. Preorder 根左右
    // Non-recursion
    public List<Integer> preorderTraversal(TreeNode root) {
        Stack<TreeNode> stack = new Stack<TreeNode>();
        List<Integer> preorder = new ArrayList<Integer>();

        if (root == null) {
            return preorder;
        }

        stack.push(root);
        while (!stack.empty()) {
            TreeNode node = stack.pop();
            preorder.add(node.val); // 根左右
            if (node.right != null) { // 栈后进先出，所以先push右节点
                stack.push(node.right);
            }
            if (node.left != null) {
                stack.push(node.left);
            }
        }

        return preorder;
    }

    // Traverse
    public ArrayList<Integer> preorderTraversalTra(TreeNode root) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        traverse(root, result);
        return result;
    }
    // 把root为根的preorder加入result里面
    private void traverse(TreeNode root, ArrayList<Integer> result) {
        if (root == null) {
            return;
        }
        result.add(root.val);
        traverse(root.left, result);
        traverse(root.right, result);
    }

    // Divide & Conquer
    public ArrayList<Integer> preorderTraversalDC(TreeNode root) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        if (root == null) { // null or leaf
            return result;
        }

        // divide
        ArrayList<Integer> left = preorderTraversalDC(root.left); // 已完成的left序列
        ArrayList<Integer> right = preorderTraversalDC(root.right);

        // Conquer
        result.add(root.val);
        result.addAll(left); // 把left的所有数据加入result
        result.addAll(right);
        return result;
    }


    // 2. Inorder 左根右
    public List<Integer> inorderTraversal(TreeNode root) {
        Stack<TreeNode> stack = new Stack<TreeNode>();
        List<Integer> inorder = new ArrayList<Integer>();
        while (root != null) { // 左子树全部进栈
            stack.push(root);
            root = root.left;
        }
        while (!stack.empty()) {
            TreeNode curr = stack.pop();
            inorder.add(curr.val);
            if (curr.right != null) { // 如果有右子树，让右子树的所有左子树都进栈
                TreeNode node = curr.right;
                while (node != null) {
                    stack.push(node);
                    node = node.left;
                }
            }
        }

        return inorder;
    }

    // 3. Postorder 左右根
    public List<Integer> postorderTraversal(TreeNode root) {
        Stack<TreeNode> stack = new Stack<TreeNode>();
        Set<TreeNode> checkedSet = new HashSet<TreeNode>();
        List<Integer> result = new ArrayList<Integer>();

        if (root != null) {
            stack.push(root);
        }

        while (!stack.empty()) {
            TreeNode top = stack.pop();
            if (checkedSet.contains(top)) {
                result.add(top.val);
            }
            else {
                // postorder
                stack.push(top); // 二次进栈
                checkedSet.add(top); // 添加进set后，再次出栈时才可以输出
                if (top.right != null) {
                    stack.push(top.right);
                }
                if (top.left != null) {
                    stack.push(top.left);
                }

                // inorder
                /*
                if (top.right != null) {
                    stack.push(top.right);
                }
                stack.push(top);
                checkedSet.add(top);
                if (top.left != null) {
                    stack.push(top.left);
                }
                 */

                // preorder
                /*
                if (top.right != null) {
                    stack.push(top.right);
                }
                if (top.left != null) {
                    stack.push(top.left);
                }
                stack.push(top);
                checkedSet.add(top);
                 */
            }
        } // while

        return result;
    }

}
