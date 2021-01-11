package binarytree;

import java.util.*;

public class BinaryTreeOther {

    // 1. Binary Search Tree Iterator
    public class BSTIterator {
        private Stack<TreeNode> stack;

        public BSTIterator(TreeNode root) {
            this.stack = new Stack<TreeNode>();
            this._leftmostInorder(root);
        }

        private void _leftmostInorder(TreeNode root) {
            // 把所有左边元素添加到stack里
            while (root != null) {
                stack.push(root);
                root = root.left;
            }
        }

        /** @return the next smallest number */
        public int next() {
            // 栈顶元素是next smallest，先推出来
            TreeNode topmostNode = stack.pop();

            // 如果当前node有右孩子，需要在right子树中找到最小值，即为当前栈顶元素的next smallest
            if (topmostNode.right != null) {
                _leftmostInorder(topmostNode.right);
            }

            return topmostNode.val;
        }

        /** @return whether we have a next smallest number */
        public boolean hasNext() {
            return stack.size() > 0;
        }

    }


    // 2. Inorder Successor in Binary Search Tree
    // 找后继节点（大于当前节点的最小值）
    public class InorderSuccessorSolution {

        public TreeNode inorderSuccessor(TreeNode root, TreeNode p) {
            if (root == null || p == null) {
                return null;
            }

            if (root.val <= p.val) {
                // 到右子树上找，先找到等于p的节点
                return inorderSuccessor(root.right, p);
            } else {
                // root比p大了以后，就在这颗树上不断往左找
                TreeNode left = inorderSuccessor(root.left, p);
                return (left != null) ? left : root;
            }
        }
    }

    // 3. Search Range in Binary Search Tree
    // 给定一个二叉查找树和范围[k1, k2]。按照升序返回给定范围内的节点值
    public class SearchRangeSolution {

        private ArrayList<Integer> results;

        /**
         * @param root: The root of the binary search tree.
         * @param k1,k2: range k1 to k2.
         * @return Return all keys that k1<=key<=k2 in increasing order.
         */
        public ArrayList<Integer> searchRange(TreeNode root, int k1, int k2) {
            results = new ArrayList<Integer>();
            travel(root, k1, k2);
            return results;
        }
        private void travel(TreeNode node, int k1, int k2) {
            if (node == null) {
                return;
            }
            // if是为了剪枝，如果node<k1，左子树都不管
            if (node.val > k1) {
                travel(node.left, k1, k2);
            }
            if (node.val >= k1 && node.val <= k2) {
                results.add(node.val);
            }
            // if是为了剪枝，如果node>k2，右子树都不管
            if (node.val < k2) {
                travel(node.right, k1, k2);
            }
        }
    }

    // 4. Insert Node in a Binary Search Tree
    public class InsertNodeSolution {
        /**
         * @param root: The root of the binary search tree.
         * @param node: insert this node into the binary search tree
         * @return The root of the new binary search tree.
         */
        public TreeNode insertNode(TreeNode root, TreeNode node) {
            if (root == null) { // 最后一层，叶子节点往下，返回自己在这一层插入
                return node;
            }

            if (node.val < root.val) {
                root.left = insertNode(root.left, node);
            } else { // node.val >= root.val
                root.right = insertNode(root.right, node);
            }
            return root;
        }
    }

    // 5. Remove Node in Binary Search Tree
    public class RemoveNodeSolution {

        public TreeNode deleteNode(TreeNode root, int key) {
            if (root == null) {
                return null;
            }

            if (key > root.val) { // 去right subtree删除
                root.right = deleteNode(root.right, key);
            } else if (key < root.val) { // 去left subtree删除
                root.left = deleteNode(root.left, key);
            } else { // 相等，删除当前节点
                if (root.left == null && root.right == null) { // 如果是叶子节点
                    root = null;
                } else if (root.right != null) { // 如果不是叶子，且存在right subtree
                    // 用当前节点的successor替换，再去right subtree递归删除这个successor
                    root.val = successor(root);
                    root.right = deleteNode(root.right, root.val);
                } else { // 如果不是叶子，没有right subtree，只有left subtree
                    // 用当前节点的predecessor替换，再去left subtree递归删除这个predecessor
                    root.val = predecessor(root);
                    root.left = deleteNode(root.left, root.val);
                }
            }

            return root;
        }

        // 先找到right subtree，再不断向左找
        // 在上面的判断中已经保证了会有右子树，即root.right肯定不空
        public int successor(TreeNode root) {
            root = root.right;
            while (root.left != null) {
                root = root.left;
            }
            return root.val;
        }

        // 先找到left subtree，再不断向右找
        // 在上面的判断中已经保证了会有左子树，即root.left肯定不空
        public int predecessor(TreeNode root) {
            root = root.left;
            while (root.right != null) {
                root = root.right;
            }
            return root.val;
        }

    }


}
