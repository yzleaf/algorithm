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
    // 105
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

    // 路径总和
    // 112
    // 给你二叉树的根节点root 和一个表示目标和的整数targetSum
    // 判断该树中是否存在 根节点到叶子节点 的路径，这条路径上所有节点值相加等于目标和targetSum
    public class PathSumSolution {
        public boolean hasPathSum(TreeNode root, int targetSum) {
            if (root == null) {
                return false;
            }
            if (root.left == null && root.right == null) { // 叶子节点
                return root.val == targetSum; // 最后一个点是否等于剩余的sum
            }

            int sumNext = targetSum - root.val;
            return hasPathSum(root.left, sumNext) || hasPathSum(root.right, sumNext);
        }
    }

    // 二叉树的直径
    // 543
    // 直径长度是任意两个结点路径长度中的最大值。这条路径可能穿过也可能不穿过根结点。
    public class DiameterOfBinaryTreeSolution {
        private int result; // 记录每次路径经过的节点数，取最大
        public int diameterOfBinaryTree(TreeNode root) {
            result = 1;
            depth(root);
            // 直径长度为所有经过的节点数-1
            return result - 1;
        }

        // 该节点为root的子树深度
        private int depth(TreeNode curr) {
            if (curr == null) {
                return 0;
            }

            int left = depth(curr.left); // 左孩子为root的子树深度
            int right = depth(curr.right);
            int currDepth = Math.max(left, right) + 1; // curr当前节点为root的深度

            // left + right + 1: 以curr当前节点为起点的路径，经过节点数的最大值（+1是加自己）
            result = Math.max(result, left + right + 1);

            return currDepth;
        }
    }

    // 合并二叉树
    // 617
    // 输入:
    // Tree 1                     Tree 2
    //          1                         2
    //         / \                       / \
    //        3   2                     1   3
    //       /                           \   \
    //      5                             4   7
    // 输出:
    //	     3
    //	    / \
    //	   4   5
    //	  / \   \
    //	 5   4   7
    public class MergeTreesSolution {
        public TreeNode mergeTrees(TreeNode root1, TreeNode root2) {
            if (root1 == null) {
                return root2;
            }
            if (root2 == null) {
                return root1;
            }
            // 上面的判断包括了两者都为null的情况

            TreeNode left = mergeTrees(root1.left, root2.left);
            TreeNode right = mergeTrees(root1.right, root2.right);

            TreeNode curr = new TreeNode(root1.val + root2.val);

            // 需要把左右子树给接上
            curr.left = left;
            curr.right = right;

            return curr;
        }
    }

    // 最长同值路径
    // 687
    // 找到最长的路径，这个路径中的每个节点具有相同值。
    // 注意：两个节点之间的路径长度由它们之间的边数表示。
    // 输入:
    //
    //              5
    //             / \
    //            4   5
    //           / \   \
    //          1   1   5
    // 输出:2
    public class LongestUnivaluePath {
        private int result; // 全局的最长路径
        public int longestUnivaluePath(TreeNode root) {
            result = 0;
            longestPath(root);
            return result;
        }
        private int longestPath(TreeNode curr) {
            if (curr == null) { // 空节点
                return 0;
            }
            int currMax = 0; // 以当前curr节点为起点的最长同值路径（只能是单边的，因为递归需要返回）
            int left = longestPath(curr.left);
            int right = longestPath(curr.right);

            // .1 左子树与root相同
            if (curr.left != null && curr.left.val == curr.val) {
                currMax = Math.max(currMax, left + 1);
            }

            // .2 右子树与root相同
            if (curr.right != null && curr.right.val == curr.val) {
                currMax = Math.max(currMax, right + 1);
            }

            // .3 左子树-root-右子树都相等
            if (curr.left != null && curr.left.val == curr.val &&
                curr.right != null && curr.right.val == curr.val) {
                result = Math.max(result, left + right + 2); // 加上左右两条边
            }

            result = Math.max(result, currMax); // 比较全局的和单边的

            return currMax;
        }
    }

    // 输出二叉树
    // 655
    // 在一个 m*n 的二维字符串数组中输出二叉树
    // 输入:
    //     1
    //    / \
    //   2   3
    //    \
    //     4
    // 输出:
    // [["", "", "", "1", "", "", ""],
    //  ["", "2", "", "", "", "3", ""],
    //  ["", "", "4", "", "", "", ""]]
    public class PrintTreeSolution {
        private List<List<String>> result;
        public List<List<String>> printTree(TreeNode root) {
            int height = getHeight(root);
            int length = (1 << height) - 1; // 2^h - 1

            result = new ArrayList<>();
            // 将res全部置为“ ”
            for (int i = 0; i < height; i++) {
                List<String> arr = new ArrayList<>();
                for (int j = 0; j < length; j++) {
                    arr.add("");
                }
                result.add(arr);
            }

            // 填充数字
            fill(root, 0, 0, length - 1);

            return result;
        }
        // 第i行，左边界l，右边界r
        // 实际上相当于是一个二分，在左子树去寻找相应的位置，满足根节点在区域中间，左孩子在左边的中间，右孩子在右边的中间
        private void fill(TreeNode curr, int i, int l, int r) {
            if (curr == null) {
                return;
            }
            int mid = l + (r - l) / 2;

            result.get(i).set(mid, Integer.toString(curr.val)); // 第i行的mid设置为当前值

            fill(curr.left, i + 1, l, mid - 1); // i+1行
            fill(curr.right, i + 1, mid + 1, r);
        }
        private int getHeight(TreeNode curr) {
            if (curr == null) {
                return 0;
            }
            return Math.max(getHeight(curr.left), getHeight(curr.right)) + 1;
        }
    }

    // 完全二叉树的节点个数
    // 222
    // 完全二叉树 的根节点 root ，求出该树的节点个数
    public class CountNodesSolution {
        // 暴力法，但是没有考虑完全二叉树的性质
        // public int countNodes(TreeNode root) {
        //     if (root == null){
        //         return 0;
        //     }
        //     return countNodes(root.left) + countNodes(root.right) + 1;
        // }

        // 完全二叉树是一棵空树或者它的叶子节点只出在最后两层，若最后一层不满则叶子节点只在最左侧。
        public int countNodes(TreeNode root) {
            if (root == null) {
                return 0;
            }

            int left = countLevel(root.left); // 左子树的高度
            int right = countLevel(root.right); // 右子树的高度
            if (left == right) { // 左子树一定是满二叉树，因为节点已经填充到右子树了
                                 // 左子树的节点直接得到2^left - 1
                                 // 再对右子树递归统计
                return countNodes(root.right) + (1 << left) - 1 + 1; // +1是这个root节点
            } else { // left != right, 此时最后一层不满，但倒数第二层已经满了，可以直接得到右子树的节点个数
                     // 再对左子树递归统计
                return countNodes(root.left) + (1 << right) - 1 + 1;
            }
            // 时间复杂度：T(n) = T(n/2) + logn 使用主定理的特殊情况，可以求得时间复杂度是O(logn * logn)
        }
        private int countLevel(TreeNode curr) { // 不用递归，因为是完全二叉树，可以更快
            int level = 0;
            while (curr != null) {
                level ++;
                curr = curr.left;
            }
            return level;
        }
    }

    // 修剪二叉搜索树
    // 669
    // 给你二叉搜索树的根节点 root ，同时给定最小边界low 和最大边界 high。通过修剪二叉搜索树，使得所有节点的值在[low, high]中
    class TrimBSTSolution {
        public TreeNode trimBST(TreeNode root, int low, int high) {
            if (root == null) {
                return null;
            }

            if (root.val < low) { // BST, root比low小,把左孩子连同root全部裁掉
                root = root.right;
                // 裁掉之后继续看右孩子的剪裁情况 剪裁后重新赋值给root
                root = trimBST(root, low, high);
            } else if (root.val > high) { // BST, 如果数字比high大, 就把右孩子连同root全部裁掉.
                root = root.left;
                // 裁掉之后继续看左节点的剪裁情况
                root = trimBST(root, low, high);
            } else { // 如果数字在区间内,就去裁剪左右子节点
                root.left = trimBST(root.left, low, high);
                root.right = trimBST(root.right, low, high);
            }
            return root;
        }
    }

    // 打家劫舍 III
    // 337
    // 如果两个直接相连的房子在同一天晚上被打劫，房屋将自动报警。
    // 计算在不触动警报的情况下，小偷一晚能够盗取的最高金额
    // 输入: [3,4,5,1,3,null,1]
    //
    //     3
    //    / \
    //   4   5
    //  / \   \
    // 1   3   1
    // 输出: 9 解释:小偷一晚能够盗取的最高金额 4 + 5 = 9.
    public class RobSolution {
        // 方法1 递归+Hash
        // 二叉树只有左右两个孩子，一个爷爷最多 2 个儿子，4 个孙子
        // 相邻的不能偷
        // 4个孙子偷的钱 + 爷爷的钱 VS 两个儿子偷的钱 哪个组合钱多，就当做当前节点能偷的最大钱数
        Map<TreeNode, Integer> hash; // 以当前节点为root能偷到的最大钱数（这个节点可能偷也可能不偷）
        public int rob(TreeNode root) {
            hash = new HashMap<>();
            return robInternal(root);
        }
        private int robInternal(TreeNode curr) {
            if (curr == null) {
                return 0;
            }
            if (hash.containsKey(curr)) {
                return hash.get(curr);
            }

            // .1 爷爷+两个孙子
            int money = curr.val; // 当前节点的钱数
            if (curr.left != null) { // 当前节点左边的两个孙子
                money += robInternal(curr.left.left) + robInternal(curr.left.right);
            }
            if (curr.right != null) { // 当前节点右边的两个孙子
                money += robInternal(curr.right.left) + robInternal(curr.right.right);
            }

            // .2 只有爸爸
            int result = robInternal(curr.left) + robInternal(curr.right);

            result = Math.max(result, money);
            // 每次递归都会做这样的判断，再选择较大的

            hash.put(curr, result);
            return result;
        }

        // 方法2
        // 每个节点选择偷或不偷 result[], 0不偷 1偷
        // .1 当前节点选择不偷：当前节点能偷到的最大钱数 = 左孩子能偷到的钱 + 右孩子能偷到的钱
        // .2 当前节点选择偷：当前节点能偷到的最大钱数 = 左孩子选择自己不偷时能得到的钱 + 右孩子选择不偷时能得到的钱 + 当前节点的钱数
        public int rob2(TreeNode root) {
            int[] result = robInternal2(root);
            return Math.max(result[0], result[1]);
        }
        private int[] robInternal2(TreeNode curr) { // 当前节点为root能偷到的最大钱
            int[] result = new int[2];
            if (curr == null) {
                return result;
            }

            int[] left = robInternal2(curr.left); // 左孩子能偷到的最大的钱
            int[] right = robInternal2(curr.right); // 右孩子能偷到的最大的钱

            // 当前节点不偷，左右孩子拿最多的钱出来（孩子那个点可偷可不偷）
            result[0] = Math.max(left[0], left[1]) + Math.max(right[0], right[1]);

            // 当前节点偷，左右孩子不偷
            result[1] = left[0] + right[0] + curr.val;

            return result;
        }
    }
}
