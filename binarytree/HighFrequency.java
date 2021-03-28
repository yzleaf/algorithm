package binarytree;

import datastructure.*;

import java.util.*;

public class HighFrequency {
    // 1. binary tree flipping
    // 给定一个二叉树，其中所有右节点，是兄弟节点且为叶节点或null
    // 将其倒置并将其转换为树，其中原来的右节点变为左叶子节点。返回新的根节点。
    // 父变为右子，兄弟变为左子
    public class UpsideDownBinaryTreeSolution {
        public TreeNode upsideDownBinaryTree(TreeNode root) {
            if (root == null) {
                return null;
            }

            return dfs(root);
        }
        private TreeNode dfs(TreeNode cur) {
            if (cur.left == null) {
                return cur;
            }

            // 左子树不空的情况
            TreeNode newRoot = dfs(cur.left); // 原树最后的一个左节点，也是新树的根节点（newRoot一直没有操作没有变化过）

            cur.left.right = cur; // 父变为右子
            cur.left.left = cur.right; // 兄弟变为左子
            cur.left = null;
            cur.right = null;

            return newRoot;
        }
    }

    // 2. 二叉树叶子顺序遍历 · binary tree leaves order traversal
    // 收集并移除每一个层次的所有叶子（从最外层开始，叶子可能不在一层），重复直到树为空。
    // 与107(bfs)不同
    public class FindLeavesSolution {

        Map<Integer, List<Integer>> depth; // 存放每一个深度的叶子节点

        public List<List<Integer>> findLeaves(TreeNode root) {
            List<List<Integer>> result = new ArrayList<>();
            depth = new HashMap<>();

            int RootDepth = findDepth(root);

            for (int i = 1; i <= RootDepth; i++) { // 从最底层的叶子节点开始遍历
                result.add(depth.get(i));
            }

            return result;
        }

        private int findDepth(TreeNode cur) { // 返回当前节点的深度，并更新每个深度的hash表
            if (cur == null) { // 空节点深度为0
                return 0;
            }

            int d = Math.max(findDepth(cur.left), findDepth(cur.right)) + 1;

            depth.putIfAbsent(d, new ArrayList<>()); // 如果不存在key，就新建
            depth.get(d).add(cur.val);
            return d;
        }
    }

    // 3. 最大子树 · Maximum Subtree
    // 给你一棵二叉树，找二叉树中的一棵子树，他的所有节点之和最大
    // 返回这棵子树的根节点
    public class FindSubtreeSolution {
        // 以每个点为根节点root（即每一个子树），找到左子树的节点x和与右子树的节点和y。
        // 如果root.val + x.val + y.val > 当前已知最大值，就更新答案
        public TreeNode result = null;
        public int MaximumSum = Integer.MIN_VALUE;

        public TreeNode findSubtree(TreeNode root) {
            findSum(root);
            return result;
        }
        private int findSum(TreeNode cur) {
            if (cur == null) {
                return 0;
            }

            int leftWeight = findSum(cur.left); // 左子树的和
            int rightWeight = findSum(cur.right); // 右子树的和
            int curSum = leftWeight + rightWeight + cur.val; // 当前树的和
            if (result == null || curSum > MaximumSum) {
                MaximumSum = curSum;
                result = cur;
            }

            return curSum;
        }
    }

    // 4. 二叉树垂直遍历 · Binary Tree Vertical Order Traversal
    // 314
    // 逐列从上到下。如果两个节点在同一行和同一列中，则顺序应 从左到右。
    // 输入： {3,9,8,4,0,1,7}
    // 输出：[[4],[9],[3,0,1],[8],[7]]
    // 解释：
    //     3
    //    /\
    //   /  \
    //   9   8
    //  /\  /\
    // /  \/  \
    // 4  01   7
    public class VerticalOrderSolution {
        // 对于一棵树，我们设其根节点的位置为0。
        // 对于任一非叶子节点，若其位置为x，设其左儿子的位置为x-1，右儿子位置为x+1。
        // 按照以上规则bfs遍历整棵树统计所有节点的位置，然后按位置从小到大输出所有节点。
        public List<List<Integer>> verticalOrder(TreeNode root) {
            List<List<Integer>> result = new ArrayList<>();
            if (root == null) {
                return result;
            }

            // 第几col列 -> 所有node值
            Map<Integer, List<Integer>> colToNode = new TreeMap<>((a, b) -> a - b); // col从小到大排列
            Queue<Integer> colQueue = new LinkedList<>();
            Queue<TreeNode> NodeQueue = new LinkedList<>();

            colQueue.offer(0);
            NodeQueue.offer(root);

            while (!NodeQueue.isEmpty()) { // bfs
                int col = colQueue.poll();
                TreeNode cur = NodeQueue.poll();

                colToNode.putIfAbsent(col, new ArrayList<>());
                colToNode.get(col).add(cur.val);

                if (cur.left != null) {
                    colQueue.offer(col - 1);
                    NodeQueue.offer(cur.left);
                }
                if (cur.right != null) {
                    colQueue.offer(col + 1);
                    NodeQueue.offer(cur.right);
                }
            }

            for (Integer i : colToNode.keySet()) { // 遍历treeMap表，它已经按key排好序了
                result.add(colToNode.get(i));
            }
            // for (int i = Collections.min(hash.keySet()); i <= Collections.max(hash.keySet()); i++)
            // 如果用hashMap要这样从小到大遍历
            return result;
        }
    }

    // 5. 二叉树中的最大路径和 · Binary Tree Maximum Path Sum
    // 124
    // 给出一棵二叉树，寻找一条路径使其路径和最大，路径可以在任一节点中开始和结束（路径和为两个节点之间所在路径上的节点权值之和）
    public class MaxPathSumSolution {
        // 挑选左右子树最大值是分治
        private int max = Integer.MIN_VALUE;

        public int maxPathSum(TreeNode root) {
            findSum(root);
            return max;
        }
        private int findSum(TreeNode cur) { // 找到当前点为root的树的最大和
            if (cur == null) {
                return 0;
            }

            int left = findSum(cur.left);
            int right = findSum(cur.right);

            // curMax表示当前点与其分别加上左右子树的最大sum
            // 因为curMax要往上传，只能有一条路径，所以左右子树不可能同时出现
            int curMax = Math.max(left, right) + cur.val; // 先比加上左边的树大还是加上右边的树大
            curMax = Math.max(curMax, cur.val); // 再比只有cur这个点是不是更大

            // 在全局max中，不往上一层传，左右子树可以同时出现（即走的是从left到right的路径）
            max = Math.max(max, Math.max(curMax, left + right + cur.val));

            return curMax;
        }
    }

    // 6. 二叉树的最大路径和 II · Binary Tree Maximum Path Sum II
    // 给一棵二叉树，找出从根节点出发的路径中，和最大的一条
    // 必须从根节点出发
    public class MaxPathSum2Solution {
        // 跟上一题是一样的，只不过这题一定要从根出发，没有左右子树同时出现的情况
        public int maxPathSum(TreeNode root) {
            if (root == null) {
                return Integer.MIN_VALUE;
            }
            int left = maxPathSum(root.left);
            int right = maxPathSum(root.right);

            int curMax = Math.max(left, right) + root.val;
            curMax = Math.max(curMax, root.val); // 只有cur这个点，是不是更大

            return curMax;
        }
        // 用dp思想来理解就是，保存了从当前点开始到底的最大sum
    }

    // 7. 从前序与中序遍历序列构造二叉树
    // 前序遍历 preorder = [3,9,20,15,7]
    // 中序遍历 inorder = [9,3,15,20,7]
    //     3
    //   / \
    //  9  20
    //    /  \
    //   15   7
    public class BuildTreeSolution {
        private Map<Integer, Integer> index;

        public TreeNode buildTree(int[] preorder, int[] inorder) {
            int n = preorder.length;
            // 中序遍历的值 -> 所在的位置
            index = new HashMap<>();
            for (int i = 0; i < n; i++) {
                index.put(inorder[i], i);
            }
            // 数组从0 -> n-1 整个长度进行不断递归（左闭右闭区间）
            return myBuildTree(preorder, inorder, 0, n - 1, 0 , n - 1);
        }
        // 根据前序遍历得到根节点（第一个），然后在中序遍历中找到根节点的位置，它的左边就是左子树的节点，右边就是右子树的节点
        // preorder = [3,9,20,15,7], inorder = [9,3,15,20,7]
        // 首先根据 preorder 找到根节点是3, 然后根据根节点将inorder分成左子树和右子树
        // 左子树 inorder[9]
        // 右子树 inorder [15,20,7]
        // 然后把相应的preorder的数组也加进来
        // 左子树 preorder[9] inorder[9]
        // 右子树 preorder[20 15 7]  inorder[15,20,7]
        private TreeNode myBuildTree(int[] preorder, int[] inorder, int preLeft, int preRight, int inLeft, int inRight) {
            if (preLeft > preRight) {
                return null;
            }

            // 前序遍历第一个点是root，在中序遍历中找到root，就可以把左右子树分开
            int preRoot = preLeft;
            int inRoot = index.get(preorder[preRoot]);

            TreeNode root = new TreeNode(preorder[preRoot]); // 构建root节点

            // 左子树 数目
            int sizeLeft = inRoot - inLeft;

            // 左子树
            // preorder: 第一个是root，左子树从root+1开始，到加满个数结束
            // inorder: 左子树从当前的开头开始，到root-1结束
            root.left = myBuildTree(preorder, inorder, preLeft + 1, preLeft + sizeLeft, inLeft, inRoot - 1);
            // 右子树
            // preorder: 第一个是root，右子树从root+左子树长度之后开始，到当前长度结束
            // inorder: 左子树从root+1开始，到当前的结尾结束
            root.right = myBuildTree(preorder, inorder, preLeft + sizeLeft + 1, preRight, inRoot + 1, inRight);
            return root;
        }
    }

    // 8. 二叉树的右视图
    // 199
    // 给定一棵二叉树，想象自己站在它的右侧，按照从顶部到底部的顺序，返回从右侧所能看到的节点值。
    // 输入: [1,2,3,null,5,null,4]
    // 输出: [1, 3, 4]
    // 解释:
    //    1            <---
    //  /   \
    // 2     3         <---
    //  \     \
    //   5     4       <---
    public class RightSideViewSolution {
        // 按照 根结点 -> 右子树 -> 左子树 的顺序访问，就可以保证每层都是最先访问最右边的节点的
        List<Integer> result;
        public List<Integer> rightSideView(TreeNode root) {
            result = new ArrayList<>();
            dfs(root, 0); // 从根节点第0层开始
            return result;
        }
        private void dfs(TreeNode curr, int depth) {
            if (curr == null) {
                return;
            }

            // 如果当前节点所在深度还没有出现在res里，说明在该深度下当前节点是第一个被访问的节点，因此将当前节点加入res中
            if (result.size() == depth) {
                result.add(curr.val);
            }
            depth ++;
            dfs(curr.right, depth);
            dfs(curr.left, depth);
        }
        // 方法2 BFS分层次遍历，取最后一个
    }

    // 对称二叉树
    // 101
    // 给定一个二叉树，检查它是否是镜像对称的
    //     1
    //    / \
    //   2   2
    //  / \ / \
    // 3  4 4  3
    public class SymmetricBTSolution {
        // 方法1 递归
        // 通过「同步移动」两个指针的方法来遍历这棵树，p指针和 q指针一开始都指向这棵树的根
        // 随后 p右移时，q左移，p左移时，q右移。
        // 每次检查当前 p和 q节点的值是否相等，如果相等再判断左右子树是否对称。
        public boolean isSymmetric(TreeNode root) {
            return check(root, root);
        }
        public boolean check(TreeNode p, TreeNode q) { // 判断是否镜像对称
            if (p == null && q == null) {
                return true;
            }

            if (p == null || q == null) { // 此时p和q不同时为null了
                return false;
            }

            return p.val == q.val && check(p.left, q.right) && check(p.right, q.left);
        }

        // 方法2 迭代
        // 每次提取两个结点并比较它们的值（队列中每两个连续的结点应该是相等的，而且它们的子树互为镜像）
        public boolean check2(TreeNode p, TreeNode q) {
            Queue<TreeNode> queue = new LinkedList<>();
            queue.offer(p);
            queue.offer(q);
            while (!queue.isEmpty()) {
                p = queue.poll();
                q = queue.poll();
                if (p == null && q == null) {
                    return true;
                }
                if (p == null || q == null || p.val != q.val) {
                    return false;
                }

                queue.offer(p.left);
                queue.offer(q.right);

                queue.offer(p.right);
                queue.offer(q.left);
            }
            return true;
        }

    }
}
