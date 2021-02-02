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
    // 给定一个可能具有重复数字的列表，返回其所有可能的子集
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
            List<List<Integer>> results = new ArrayList<>();
            if (nums == null) {
                return results;
            }
            if (nums.length == 0) { // 添加空子集
                results.add(new ArrayList<Integer>());
                return results;
            }
            Arrays.sort(nums);

            List<Integer> subset = new ArrayList<>();
            dfs(nums, 0, subset, results); // 从index=0开始的子集

            return results;
        }
        private void dfs(int[] nums, int startIndex, List<Integer> subset, List<List<Integer>> results) {
            results.add(new ArrayList<Integer>(subset));

            // 把剩余的所有元素依次添加到不同的subset里
            for (int i = startIndex; i <= nums.length; i++) {
                // 如果有重复元素，本层只取第一个数
                // [1,2,2']只取[1,2]
                if (i != startIndex && nums[i] == nums[startIndex]) {
                    continue;
                }
                subset.add(nums[i]);
                dfs(nums, i + 1, subset, results);
                subset.remove(subset.size() - 1); // 删掉刚加进来的数
            }
        }
    }

    // 3. Combination Sum
    // 给定一个候选数字的集合candidates和一个目标值target. 找到candidates中所有的和为target的组合.
    // 在同一个组合中,candidates中的某个数字不限次数地出现
    public class CombinationSum1Solution {
        // 方法2，按元素个数，遍历每一层
        public  List<List<Integer>> combinationSum(int[] candidates, int target) {
            List<List<Integer>> result = new ArrayList<>();

            if (candidates == null || candidates.length == 0) {
                return result;
            }

            List<Integer> combination = new ArrayList<>(); // 每一个符合要求的list
            Arrays.sort(candidates);
            helper(candidates, 0, target, combination, result);

            return result;
        }
        private void helper(int[] candidates, int startIndex, int target,
                       List<Integer> combination, List<List<Integer>> result) {
            // 目标target每次减去当前加入的数值
            if (target == 0) {
                result.add(new ArrayList<Integer>(combination));
                return;
            }
            for (int i = startIndex; i < candidates.length; i++) {
                if (candidates[i] > target) { // 因为数组升序排列，如果>，可直接跳出循环
                    break;
                }
                // 去除candidates数组中的重复元素：[1,2,2'] -> [1,2] [1,2']选择第一个
                if (i != startIndex && candidates[i] == candidates[startIndex]) {
                    continue;
                }

                combination.add(candidates[i]);
                helper(candidates, i, target - candidates[i], combination, result);
                combination.remove(combination.size() - 1);
            }
        }

        // 方法1 选或者不选
        public List<List<Integer>> combinationSum1(int[] candidates, int target) {

            List<List<Integer>> result = new ArrayList<>();
            Arrays.sort(candidates);

            dfs(candidates, 0, target, new ArrayList<Integer>(), result);
            return result;
        }

        private void dfs(int[] candidates, int index, int target, List<Integer> combination, List<List<Integer>> results) {
            if (index == candidates.length) {
                if (target == 0) {
                    results.add(new ArrayList<Integer>(combination));
                }
                return;
            }

            if (target < 0) {
                return;
            }

            // 不选这个数，从index+1开始，target不变
            dfs(candidates, index + 1, target, combination, results);
            // 去除重复元素
            if (index > 0 && candidates[index] == candidates[index - 1]) {
                return;
            }
            combination.add(candidates[index]);
            // 当前元素可选多次
            dfs(candidates, index, target - candidates[index], combination, results);
            combination.remove(combination.size() - 1);
        }
    }

    // 4. Combination Sum II
    // 给定一个数组 num 和一个整数 target. 找到 num 中所有的数字之和为 target 的组合.
    // 在同一个组合中, num 中的每一个数字仅能被使用一次.
    public class CombinationSum2Solution {
        public List<List<Integer>> combinationSum2(int[] candidates, int target) {
            List<List<Integer>> result = new ArrayList<>();

            if (candidates == null || candidates.length == 0) {
                return result;
            }

            List<Integer> combination = new ArrayList<>();
            Arrays.sort(candidates);
            helper(candidates, 0 , target, combination, result);

            return result;
        }
        private void helper(int[] candidates, int startIndex, int target,
                       List<Integer> combination, List<List<Integer>> result) {
            if (target == 0) {
                result.add(new ArrayList<>(combination));
                return;
            }

            for (int i = startIndex; i < candidates.length; i++) {
                if (candidates[i] > target) {
                    break;
                }
                if (i != startIndex && candidates[i] == candidates[startIndex]) { // 去除重复元素
                    continue;
                }
                combination.add(candidates[i]);
                helper(candidates, i + 1, target - candidates[i], combination, result);
                combination.remove(combination.size() - 1);
            }
        }
    }

    // 5. 分割回文串 · Palindrome Partitioning
    // 给定字符串s, 需要将它分割成一些子串, 使得每个子串都是回文串.
    // 返回所有分割方案的子串
    public class PalindromPartitionSolution {

        public List<List<String>> partition(String s) {
            List<List<String>> result = new ArrayList<>();
            if (s == null || s.length() == 0) {
                return result;
            }

            List<String> partition = new ArrayList<String>(); // 一种分割方案里的所有子串
            helper(s, 0, partition, result);

            return result;
        }
        private void helper(String s, int startIndex, List<String> partition, List<List<String>> result) {
            if (startIndex == s.length()) {
                result.add(new ArrayList<String>(partition));
                return;
            }
            for (int i = startIndex; i < s.length(); i++) {
                String subStr = s.substring(startIndex, i + 1); // 左闭右开区间[start, end)
                if (isPalindrom(subStr)) {
                    partition.add(subStr);
                    helper(s, i + 1, partition, result);
                    partition.remove(partition.size() - 1);
                }
            }
        }
        private boolean isPalindrom(String s) {
            for (int i = 0, j = s.length() - 1; i < j; i++, j--) {
                if (s.charAt(i) != s.charAt(j)) {
                    return false;
                }
            }
            return true;
        }
    }
    // 还可用DP方法
}
