package dfs;

import java.util.*;

public class Permutation {

    // 1. 全排列 · Permutations
    // 给定一个数字列表，返回其所有可能的排列
    // 假设没有重复数字
    public class Permute1Solution {
        List<List<Integer>> result;

        public List<List<Integer>> permute(int[] nums) {
            result = new ArrayList<>();
            if (nums == null || nums.length == 0) {
                return result;
            }

            boolean[] visited = new boolean[nums.length];
            List<Integer> permutation = new ArrayList<>();
            helper(nums, visited, permutation);

            return result;
        }
        private void helper(int[] nums, boolean[] visited, List<Integer> permutation) {

            if (permutation.size() == nums.length) {
                result.add(new ArrayList<Integer>(permutation));
                return;
            }

            for (int i = 0; i < nums.length; i++) {
                if (visited[i]) { // 当前点已进入数组
                    continue;
                }

                permutation.add(nums[i]);
                visited[i] = true;
                helper(nums, visited, permutation);

                permutation.remove(permutation.size() - 1);
                visited[i] = false;
            }
        }
    }

    // 2. 带重复元素的排列 · Permutations II
    // 输入：[1,2,2]
    // 输出：
    // [
    //  [1,2,2],
    //  [2,1,2],
    //  [2,2,1]
    // ]
    public class Permute2Solution {
        List<List<Integer>> result;

        public List<List<Integer>> permute(int[] nums) {
            result = new ArrayList<>();
            if (nums == null || nums.length == 0) {
                return result;
            }

            Arrays.sort(nums); // 排序为了后续去除重复元素

            boolean[] visited = new boolean[nums.length];
            List<Integer> permutation = new ArrayList<>();
            helper(nums, visited, permutation);

            return result;
        }
        private void helper(int[] nums, boolean[] visited, List<Integer> permutation) {

            if (permutation.size() == nums.length) {
                result.add(new ArrayList<Integer>(permutation));
                return;
            }

            for (int i = 0; i < nums.length; i++) {
                if (visited[i]) { // 当前点已进入数组
                    continue;
                }

                // i-1没有被访问过（2'不能替代2）（因为每次从i=0开始，需要加这个条件）
                if (i > 0 && nums[i] == nums[i - 1] && !visited[i - 1]) {
                    continue;
                }

                permutation.add(nums[i]);
                visited[i] = true;
                helper(nums, visited, permutation);

                permutation.remove(permutation.size() - 1);
                visited[i] = false;
            }
        }
    }

    // 3. N-Queens
    // n个皇后放置在n*n的棋盘上，皇后彼此之间不能相互攻击
    // 给定一个整数n，返回所有不同的n皇后问题的解决方案
    // 每个解决方案包含一个明确的n皇后放置布局，其中“Q”和“.”分别表示一个皇后和一个空位置。
    // 输入:4
    // 输出:
    // [
    //  // Solution 1
    //  [".Q..",
    //   "...Q",
    //   "Q...",
    //   "..Q."
    //  ],
    //  // Solution 2
    //  ["..Q.",
    //   "Q...",
    //   "...Q",
    //   ".Q.."
    //  ]
    // ]
    public class NQueensSolution {
        List<List<String>> result;

        List<List<String>> solveNQueens(int n) {
            result = new ArrayList<>();
            if (n <= 0) {
                return result;
            }

            List<Integer> cols = new ArrayList<>();
            search(cols, n);

            return result;
        }
        // 已经放置了n个皇后，cols表示每个皇后所在的列
        // n皇后一定在n行
        private void search(List<Integer> cols, int n) {
            if (cols.size() == n) { // 已经放置完成一种方案
                result.add(draw(cols));
                return;
            }

            for (int colIndex = 0; colIndex < n; colIndex++) {
                if (!isValid(cols, colIndex)) {
                    continue;
                }
                // 如果合法，放在当前位置，递归下一行的皇后
                cols.add(colIndex);
                search(cols, n);
                cols.remove(cols.size() - 1);
            }
        }
        private boolean isValid(List<Integer> cols, int col) {
            int row = cols.size(); // 当前待放置点应该在的行号

            // 与已经放置好的每一个元素比较
            for (int rowIndex = 0; rowIndex < cols.size(); rowIndex++) {
                if (cols.get(rowIndex) == col) { // 在同一列
                    return false;
                }
                if (Math.abs(cols.get(rowIndex) - col) == Math.abs(rowIndex - row)) { // 在斜线上
                    return false;
                }
            }

            return true;
        }
        private List<String> draw(List<Integer> cols) {
            List<String> drawRes = new ArrayList<>();
            for (int i = 0; i < cols.size(); i++) { // 行
                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < cols.size(); j++) { // 列
                    sb.append(j == cols.get(j) ? 'Q' : '.');
                }
                drawRes.add(sb.toString());
            }
            return drawRes;
        }
    }

}
