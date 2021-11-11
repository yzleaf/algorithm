package binarytree;

import datastructure.TreeNode;

import java.util.*;

public class BinaryTreeOther {

    // 1. Binary Search Tree Iterator
    // 173
    // 设计实现一个带有下列属性的二叉查找树的迭代器：next()返回BST中下一个最小的元素
    // 元素按照递增的顺序被访问（比如中序遍历）
    // next()和hasNext()的询问操作要求均摊时间复杂度是O(1)
    public class BSTIterator {
        private Stack<TreeNode> stack;

        public BSTIterator(TreeNode root) {
            this.stack = new Stack<TreeNode>();
            this._leftmostInorder(root);
        }

        // 把当前root下所有左边元素添加到stack里
        private void _leftmostInorder(TreeNode root) {
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
    // 285
    // 找该节点的中序遍历后继节点（大于当前节点的最小值），如果没有返回null
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
            // if是为了剪枝，如果node<k1，node左子树都不管
            if (node.val > k1) {
                travel(node.left, k1, k2); // 左
            }
            if (node.val >= k1 && node.val <= k2) {
                results.add(node.val); // 根
            }
            // if是为了剪枝，如果node>k2，node右子树都不管
            if (node.val < k2) {
                travel(node.right, k1, k2); // 右
            }
        }
    }

    // 4. Insert Node in a Binary Search Tree
    // 701
    public class InsertNodeSolution {
        /**
         * @param root: The root of the binary search tree.
         * @param node: insert this node into the binary search tree
         * @return The root of the new binary search tree.
         */
        public TreeNode insertNode(TreeNode root, TreeNode node) {
            // 从大的层面来说，空树，直接返回待插入的节点
            // 从小的层面来说，最后一层，叶子节点往下，返回自己在这一层插入
            if (root == null) {
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

    // 270 Closest Binary Search Tree Value
    // 给一棵非空二叉搜索树以及一个target值，找到在BST中最接近给定值的节点值
    class ClosestValueSolution {
        // 方法1 找到上下边界，再判断更接近的
        public int closestValue(TreeNode root, double target) {

            TreeNode lower = lowerBound(root, target);
            TreeNode upper = upperBound(root, target);
            if (lower == null) {
                return upper.val;
            }
            if (upper == null) {
                return lower.val;
            }

            if (upper.val - target < target - lower.val) {
                return upper.val;
            } else {
                return lower.val;
            }
        }
        // 小于target的最大的数
        private TreeNode lowerBound(TreeNode root, double target) {
            if (root == null) {
                return null;
            }

            TreeNode curr = null;
            if (root.val >= target) {
                curr = lowerBound(root.left, target);
                return curr;
            } else {
                curr = lowerBound(root.right, target);
                return curr != null ? curr : root;

            }
        }
        // 大于或者等于target的最小的数（！！！记住要等于）
        private TreeNode upperBound(TreeNode root, double target) {
            if (root == null) {
                return null;
            }

            TreeNode curr = null;
            if (root.val < target) {
                curr = upperBound(root.right, target);
                return curr;
            } else {
                curr = upperBound(root.left, target);
                return curr != null ? curr : root;
            }
        }

        // 方法2 直接递归判断
        public int closestValue2(TreeNode root, double target) {
            int closest = root.val;
            int curr = root.val;
            while (root != null) {
                curr = root.val;
                closest = Math.abs(curr - target) < Math.abs(closest - target) ? curr : closest;
                // target < root 只能向左子树找（因为右子树只会让差距更大），反之亦然
                root = target < root.val ? root.left : root.right;
            }
            return closest;
        }
    }

    // 637. Average of Levels in Binary Tree
    // 计算二叉树 每一层 的平均值
    // DFS：修改每一次遇到相同level的sum数值
    // BFS: 每一层的遍历，总和sum/size
    // 还是优先考虑用BFS做吧
    public class AverageSolution {
        // 方法1 BFS
        public List<Double> averageOfLevels1(TreeNode root) {
            List<Double> res = new ArrayList<>();
            if (root == null) {
                return null;
            }

            Queue<TreeNode> queue = new LinkedList<>();
            queue.offer(root); // 要先把根节点加进去！！！

            while (!queue.isEmpty()) {
                int level = queue.size();
                double sum = 0;
                for (int i = 0; i < level; i ++) {
                    TreeNode curr = queue.poll();
                    sum += curr.val;
                    if (curr.left != null) {
                        queue.offer(curr.left);
                    }
                    if (curr.right != null) {
                        queue.offer(curr.right);
                    }
                }
                res.add(sum / level);
            }
            return res;
        }
        // 方法2 DFS
        List<Double> sum; // sum一定要设成Double，因为累加可能会超
        List<Integer> number;
        public List<Double> averageOfLevels(TreeNode root) {
            sum = new ArrayList<>();
            number = new ArrayList<>();
            List<Double> res = new ArrayList<>();

            if (root == null) {
                return res;
            }

            dfs(root, 0);
            for (int i = 0; i < sum.size(); i ++) {
                res.add(sum.get(i) * 1.0 / number.get(i));
            }

            return res;
        }
        private void dfs(TreeNode curr, int level) {
            if (curr == null) {
                return;
            }
            // 刚进入这一层，需要新建节点，后面才可以set
            if (sum.size() == level) {
                sum.add(0 * 1.0);
                number.add(0);
            }

            sum.set(level, sum.get(level) + curr.val);
            number.set(level, number.get(level) + 1);

            if (curr.left != null) {
                dfs(curr.left, level + 1);
            }
            if (curr.right != null) {
                dfs(curr.right, level + 1);
            }
        }
    }

    // 863. All Nodes Distance K in Binary Tree
    // 找到离target节点距离为k的所有节点
    class DistanceKSolution {
        // 方法1，将树构建成一个无向图，再用BFS从target节点开始往它的邻居找
        Map<Integer, List<Integer>> hash = new HashMap<>();
        public List<Integer> distanceK(TreeNode root, TreeNode target, int k) {
            List<Integer> res = new ArrayList<>();
            if (root == null || target == null) {
                return res;
            }

            // node --- all neighbors
            constructGraph(root);

            Queue<Integer> queue = new LinkedList<>();
            queue.offer(target.val);
            Set<Integer> visited = new HashSet<>();
            visited.add(target.val);

            int step = 0;
            while (!queue.isEmpty()) {
                int size = queue.size();
                for (int i = 0; i < size; i ++) {
                    int curr = queue.poll();
                    if (step == k) {
                        res.add(curr);
                    }
                    for (Integer next : hash.get(curr)) {
                        if (visited.contains(next)) {
                            continue;
                        }
                        queue.offer(next);
                        visited.add(next);
                    }
                }
                step ++;
            }

            return res;
        }
        private void constructGraph(TreeNode curr) {
            if (curr == null) {
                return;
            }

            hash.putIfAbsent(curr.val, new ArrayList<>());

            if (curr.left != null) {
                hash.get(curr.val).add(curr.left.val);

                hash.putIfAbsent(curr.left.val, new ArrayList<>());
                hash.get(curr.left.val).add(curr.val);

                constructGraph(curr.left);
            }
            if (curr.right != null) {
                hash.get(curr.val).add(curr.right.val);

                hash.putIfAbsent(curr.right.val, new ArrayList<>());
                hash.get(curr.right.val).add(curr.val);

                constructGraph(curr.right);
            }
        }

        // 方法2
        // 用HashMap记录每个节点的父节点，之后二叉树变成三个方向来寻找：left right parent
        // 加入一个from的节点，来避免重复操作走回头路
        Map<Integer, TreeNode> parents = new HashMap<>();
        List<Integer> res = new ArrayList<>();
        public List<Integer> distanceK2(TreeNode root, TreeNode target, int k) {
            // 1. 从root出发，记录每个父节点
            findParents(root);

            // 2. 从target出发，寻找深度为k的节点
            findRes(target, null, 0, k);

            return res;
        }
        private void findParents(TreeNode curr) {
            if (curr == null) {
                return;
            }
            if (curr.left != null) {
                parents.put(curr.left.val, curr);
                findParents(curr.left);
            }
            if (curr.right != null) {
                parents.put(curr.right.val, curr);
                findParents(curr.right);
            }
        }
        private void findRes(TreeNode curr, TreeNode from, int depth, int k) {
            if (curr == null) {
                return;
            }

            // 距离为k，加入结果数组
            if (depth == k) {
                res.add(curr.val);
                return;
            }

            // 往另外三个方向找depth+1
            if (curr.left != null) {
                findRes(curr.left, curr, depth + 1, k);
            }
            if (curr.right != null) {
                findRes(curr.right, curr, depth + 1, k);
            }
            if (parents.get(curr.val) != from) { // 不等于from，它来自的节点
                findRes(parents.get(curr.val), curr, depth + 1, k);
            }
        }
    }

}
