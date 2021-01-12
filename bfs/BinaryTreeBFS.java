package bfs;

import binarytree.TreeNode;

import java.util.*;

public class BinaryTreeBFS {

    // 1. 给出一棵二叉树，返回其节点值的层次遍历（逐层从左往右访问）
    public class BFSSolution {
        public List<List<Integer>> levelOrder(TreeNode root) {
            List<List<Integer>> result = new ArrayList<>();

            if (root == null) {
                return result;
            }

            Queue<TreeNode> queue = new LinkedList<TreeNode>();
            queue.offer(root); // 进队列

            while (!queue.isEmpty()) {
                // 每一层级的操作
                List<Integer> level  = new ArrayList<Integer>();
                int size = queue.size();
                for (int i = 0; i < size; i++) {
                    TreeNode head = queue.poll(); // 取出队列头元素
                    level.add(head.val);

                    // 取下一层级的进队列
                    if (head.left != null) {
                        queue.offer(head.left);
                    }
                    if (head.right != null) {
                        queue.offer(head.right);
                    }
                }
                result.add(level);
            }

            return result;
        }
    }

    // 2. Binary Tree Serialization
    // serialize a binary tree to a string
    class SerializeSolution {

        public String serialize(TreeNode root) {
            if (root == null) {
                return "{}";
            }

            ArrayList<TreeNode> array = new ArrayList<TreeNode>();
            array.add(root);

            // 把二叉树放入数组中，随着元素不断放入size会不断增大
            for (int i = 0; i < array.size(); i++) {
                TreeNode node = array.get(i);
                if (node == null) { // 因为是空，就不会继续往下，添加它的左右子树了
                    continue;
                }
                array.add(node.left);
                array.add(node.right);
                // 这个array是可以含null节点的
            }

            // 移除最后一部分的null，它们是叶子节点留下来的左右子树记录
            while (array.get(array.size() - 1) == null) {
                array.remove(array.size() - 1);
            }


            StringBuilder sb = new StringBuilder();
            sb.append("{");

            sb.append(array.get(0).val);
            for (int i = 1; i < array.size(); i++) {
                if (array.get(i) == null) {
                    sb.append(",#");
                } else {
                    sb.append(",");
                    sb.append(array.get(i).val);
                }
            }
            sb.append("}");

            return sb.toString();
        }
    }

    class DeserializeSolution {

        public TreeNode deserialize(String data) {

            if (data.equals("{}")) {
                return null;
            }

            // 索引从0开始，取[1,*)左闭右开区间，因为去掉了头尾的大括号{}
            String[] vals = data.substring(1, data.length() - 1).split(",");

            ArrayList<TreeNode> array = new ArrayList<TreeNode>();
            TreeNode root = new TreeNode(Integer.parseInt(vals[0]));
            array.add(root);

            int index = 0; // 记录二叉树里node的index
            boolean isLeftChild = true; // 一个node赋值一个left孩子后要翻转

            for (int i = 1; i < vals.length; i++) {
                if (!vals[i].equals("#")) { // 不是#
                    TreeNode node = new TreeNode(Integer.parseInt(vals[i]));
                    if (isLeftChild) {
                        array.get(index).left = node; // index表示二叉树的节点下标
                    } else { // right child
                        array.get(index).right = node;
                    }
                    array.add(node);
                    // 这个array是不含null节点的
                }
                if (!isLeftChild) { // 遍历到right child，index加到下一个节点
                    index++;
                }
                isLeftChild = !isLeftChild;
            }

            return root;
        }
    }

}
