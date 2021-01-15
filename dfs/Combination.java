package dfs;

import java.util.*;

public class Combination {

    // 1. Subsets
    // 给定一个含不同整数的集合，返回其所有的子集
    // 时间复杂度：O(n * 2^n) 一共 2^n个状态，每种状态需要O(n)的时间来构造子集。
    // 空间复杂度：O(n) 临时数组的空间代价是 O(n)，递归时栈空间的代价为 O(n)。（不考虑返回空间的情况下）

    public class Subsets1Solution {
        public List<List<Integer>> subsets(int[] nums) {
            List<List<Integer>> results = new ArrayList<>();
            Arrays.sort(nums);

            dfs(nums, 0, new ArrayList<Integer>(), results); // 从第0个数开始
            return results;
        }

        // 1 递归的定义
        // 在Nums中找到所有以subset元素开头，加入其它元素的集合，并放到results
        private void dfs(int[] nums, int index, List<Integer> subset, List<List<Integer>> results) {
            // 3 递归的出口
            if (index == nums.length) {
                results.add(new ArrayList<>(subset)); // 拷贝一整个subset, tree最后一层时间复杂度O(n), 总时间复杂度n * 2^n
                return;
            }

            // 2 递归的拆解：如何进入下一层
            // 选择nums[index]和不选择nums[index]
            // 这个点取或者不取的二叉树，在最后一层是所有结果

            // 选了nums[index]
            subset.add(nums[index]); // [1] -> [1, 2]
            dfs(nums, index + 1, subset, results); // 找到所有以1，2开头的集合

            // 不选nums[index]
            // 删掉刚加进来的点，开始一个新的递归
            // 类似于树的左右子树，回到父节点到另一支
            subset.remove(subset.size() - 1); // 回到[1]
            dfs(nums, index + 1, subset, results);
        }

    }

    // 2. subsets2
    public class Subsets2Solution {
        // 方法1，每个数选或不选构成类似二叉树结果
        public List<List<Integer>> subsetsWithDup(int[] nums) {
            List<List<Integer>> results = new ArrayList<>();

            Arrays.sort(nums);
            dfs(nums, 0, -1, new ArrayList<>(), results);

            return results;
        }
        private void dfs(int[] nums,
                         int index,
                         int lastSelectedIndex,
                         List<Integer> subset,
                         List<List<Integer>> results) {
            if (index == nums.length) {
                results.add(new ArrayList<Integer>(subset));
                return;
            }

            // Step.1 不选当前值，往后选数递归
            dfs(nums, index + 1, lastSelectedIndex, subset, results);

            // [1,2,2']只能选择[1,2]，不能选择[1,2']。但是[2,2']可以选择
            // 如果当前值与前一个重复，并且前一个没有被选过（避开了[2,2']这种情况），直接返回
            if (index > 0 && nums[index] == nums[index - 1] && index - 1 != lastSelectedIndex) {
                return;
            }

            // Step.2 选择当前值，添加入subset
            subset.add(nums[index]);
            dfs(nums, index + 1, index, subset, results);

            subset.remove(subset.size() - 1); // 删掉新添加进来的数
        }

        // 方法2，按元素个数，遍历每一层
        public List<List<Integer>> subsetsWithDup2(int[] nums) {
            List<List<Integer>> results = new ArrayList<List<Integer>>();
            if (nums == null) {
                return results;
            }
            if (nums.length == 0) { // 添加空子集
                results.add(new ArrayList<Integer>());
                return results;
            }
            Arrays.sort(nums);

            List<Integer> subset = new ArrayList<Integer>();
            helper(nums, 0, subset, results);

            return results;
        }
        private void helper(int[] nums, int startIndex, List<Integer> subset, List<List<Integer>> results) {
            results.add(new ArrayList<Integer>(subset));

            // 把剩余的所有元素依次添加到不同的subset里
            for (int i = startIndex; i <= nums.length; i++) {
                // 如果有重复元素，本层只取第一个数
                // [1,2,2']只取[1,2]
                if (i != startIndex && nums[i] == nums[startIndex]) {
                    continue;
                }
                subset.add(nums[i]);
                helper(nums, i + 1, subset, results);
                subset.remove(subset.size() - 1); // 删掉刚加进来的数
            }
        }
    }
}
