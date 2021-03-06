package dfs;

import java.util.*;

public class Combination {

    // 1. Subsets
    // 给定一个含不同整数的集合，返回其所有的子集
    // 时间复杂度：O(n * 2^n) 一共 2^n个状态，每种状态需要O(n)的时间来构造子集。
    // 空间复杂度：O(n) 临时数组的空间代价是 O(n)，递归时栈空间的代价为 O(n)。（不考虑返回空间的情况下）

    public class Subsets1Solution {
        List<List<Integer>> results;

        public List<List<Integer>> subsets(int[] nums) {
            results = new ArrayList<>();
            Arrays.sort(nums);

            dfs(nums, 0, new ArrayList<Integer>()); // 从第0个数开始
            return results;
        }

        // 1 递归的定义
        // 在Nums中找到所有以subset元素开头，加入其它元素的集合，并放到results
        private void dfs(int[] nums, int index, List<Integer> subset) {
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
            dfs(nums, index + 1, subset); // 找到所有以1，2开头的集合

            // 不选nums[index]
            // 删掉刚加进来的点，开始一个新的递归
            // 类似于树的左右子树，回到父节点到另一支
            subset.remove(subset.size() - 1); // 回到[1]
            dfs(nums, index + 1, subset);
        }

    }

    // 2. subsets2
    // 给定一个可能具有重复数字的列表，返回其所有可能的子集
    public class Subsets2Solution {
        List<List<Integer>> results;

        // 方法1，每个数选或不选构成类似二叉树结果
        public List<List<Integer>> subsetsWithDup(int[] nums) {
            results = new ArrayList<>();

            Arrays.sort(nums);
            dfs(nums, 0, -1, new ArrayList<>());

            return results;
        }
        private void dfs(int[] nums,
                         int index,
                         int lastSelectedIndex,
                         List<Integer> subset) {
            if (index == nums.length) {
                results.add(new ArrayList<Integer>(subset));
                return;
            }

            // Step.1 不选当前值，往后选数递归
            dfs(nums, index + 1, lastSelectedIndex, subset);

            // [1,2,2']只能选择[1,2]，不能选择[1,2']。但是[2,2']可以选择
            // 如果当前值与前一个重复，并且前一个没有被选过（避开了[2,2']这种情况），当前值也不能选，直接返回
            if (index > 0 && nums[index] == nums[index - 1] && index - 1 != lastSelectedIndex) {
                return;
            }

            // Step.2 选择当前值，添加入subset
            subset.add(nums[index]);
            dfs(nums, index + 1, index, subset);

            subset.remove(subset.size() - 1); // 删掉新添加进来的数
        }

        // 方法2，按元素个数，遍历每一层
        public List<List<Integer>> subsetsWithDup2(int[] nums) {
            results = new ArrayList<>();
            if (nums == null) {
                return results;
            }
            if (nums.length == 0) { // 添加空子集
                results.add(new ArrayList<Integer>());
                return results;
            }
            Arrays.sort(nums);

            List<Integer> subset = new ArrayList<>();
            dfs(nums, 0, subset); // 从index=0开始的子集

            return results;
        }
        private void dfs(int[] nums, int startIndex, List<Integer> subset) {
            results.add(new ArrayList<Integer>(subset));

            // 把剩余的所有元素依次添加到不同的subset里
            for (int i = startIndex; i <= nums.length; i++) {
                // 如果有重复元素，本层只取第一个数
                // [1,2,2']只取[1,2]
                if (i != startIndex && nums[i] == nums[startIndex]) {
                    continue;
                }
                subset.add(nums[i]);
                dfs(nums, i + 1, subset);
                subset.remove(subset.size() - 1); // 删掉刚加进来的数
            }

            // 当startIndex到达length的时候就不进入for循环了，所以不会再调用dfs
            // 所以可以也不写return
            return;
        }
    }

    // 3. Combination Sum
    // 给定一个候选数字的集合candidates和一个目标值target. 找到candidates中所有的和为target的组合.
    // 在同一个组合中,candidates中的某个数字不限次数地出现
    public class CombinationSum1Solution {
        List<List<Integer>> result;
        // 方法2，按元素个数，遍历每一层
        public  List<List<Integer>> combinationSum(int[] candidates, int target) {
            result = new ArrayList<>();

            if (candidates == null || candidates.length == 0) {
                return result;
            }

            Arrays.sort(candidates);
            List<Integer> combination = new ArrayList<>(); // 每一个符合要求的list
            dfs2(candidates, 0, target, combination);

            return result;
        }
        private void dfs2(int[] candidates, int startIndex, int target, List<Integer> combination) {
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
                dfs2(candidates, i, target - candidates[i], combination); // 同一元素可以被选多次
                combination.remove(combination.size() - 1);
            }
        }

        // 方法1 选或者不选
        public List<List<Integer>> combinationSum1(int[] candidates, int target) {

            result = new ArrayList<>();
            Arrays.sort(candidates);

            dfs(candidates, 0, target, new ArrayList<Integer>());
            return result;
        }

        private void dfs(int[] candidates, int startIndex, int target, List<Integer> combination) {
            if (startIndex == candidates.length) {
                if (target == 0) {
                    result.add(new ArrayList<Integer>(combination));
                }
                return;
            }

            if (target < 0) {
                return;
            }

            // 不选这个数，从index+1开始，target不变
            dfs(candidates, startIndex + 1, target, combination);
            // 去除重复元素
            if (startIndex > 0 && candidates[startIndex] == candidates[startIndex - 1]) {
                return;
            }

            // 选这个数
            combination.add(candidates[startIndex]);
            // 当前元素可选多次
            dfs(candidates, startIndex, target - candidates[startIndex], combination);
            combination.remove(combination.size() - 1);
        }
    }

    // 4. Combination Sum II
    // 给定一个数组 num 和一个整数 target. 找到 num 中所有的数字之和为 target 的组合.
    // 在同一个组合中, num 中的每一个数字仅能被使用一次.
    public class CombinationSum2Solution {
        List<List<Integer>> result;

        public List<List<Integer>> combinationSum2(int[] candidates, int target) {
            result = new ArrayList<>();

            if (candidates == null || candidates.length == 0) {
                return result;
            }

            Arrays.sort(candidates);
            List<Integer> combination = new ArrayList<>();
            dfs(candidates, 0 , target, combination);

            return result;
        }
        private void dfs(int[] candidates, int startIndex, int target, List<Integer> combination) {
            if (target == 0) {
                result.add(new ArrayList<>(combination));
                return;
            }

            // for循环理解为每次替换当前这个位置的数，构成一个新的数列
            // [0 1 1 1 2 3 5]
            // 刚开始会依次遍历数组，选出第一个数[0 ...] [1 ...] 因为1重复了，后续只有[2 ...] [3 ...] [5 ...] （确认一个数）
            // 上面每个数组确认下来第一个数以后，开始试第二个数dfs，以第一组[0 ...]为例，派生出[0 1 ...] 因为1重复了，后续只有[0 2 ...] [0 3 ...] [0 5...] （确认两个数）
            // 上面每个数组确认下来第二个数以后，开始试第三个数dfs，以第一组[0 1 ...]为例，派生出[0 1 1 ...] 因为1重复了，后续只有[0 1 2 ...] [ 0 1 3 ...] [0 1 5 ...] （确认三个数）
            for (int i = startIndex; i < candidates.length; i++) {
                if (candidates[i] > target) {
                    break;
                }
                if (i != startIndex && candidates[i] == candidates[startIndex]) { // 去除重复元素 [1,2,2']只有[1,2]没有[1,2']
                    continue;
                }
                combination.add(candidates[i]);
                dfs(candidates, i + 1, target - candidates[i], combination);
                combination.remove(combination.size() - 1);
            }
        }
    }

    // 5. 分割回文串 · Palindrome Partitioning
    // 给定字符串s, 需要将它分割成一些子串, 使得每个子串都是回文串.
    // 返回所有分割方案的子串
    public class PalindromPartitionSolution {
        List<List<String>> result;

        public List<List<String>> partition(String s) {
            result = new ArrayList<>();
            if (s == null || s.length() == 0) {
                return result;
            }

            List<String> partition = new ArrayList<String>(); // 一种分割方案里的所有子串
            dfs(s, 0, partition);

            return result;
        }
        private void dfs(String s, int startIndex, List<String> partition) {
            if (startIndex == s.length()) {
                result.add(new ArrayList<String>(partition));
                return;
            }
            for (int i = startIndex; i < s.length(); i++) {
                String subStr = s.substring(startIndex, i + 1); // 左闭右开区间[start, end)
                if (isPalindrom(subStr)) {
                    partition.add(subStr);
                    dfs(s, i + 1, partition);
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
