package dynamicprogram;

import java.util.*;

public class Basic {

    // 1 Triangle
    // 给定一个数字三角形，找到从顶部到底部的最小路径和。每一步可以移动到下面一行的相邻数字上。
    // Input the following triangle:
    // [
    //     [2],
    //    [3,4],
    //   [6,5,7],
    //  [4,1,8,3]
    // ]
    // Output: 11
    // Explanation: The minimum path sum from top to bottom is 11 (i.e., 2 + 3 + 5 + 1 = 11)
    public class MinimumTotalSolution {
        // 时间复杂度 O(n^2)
        // 空间复杂度 O(n^2)
        public int minimumTotal(int[][] triangle) {
            if (triangle == null || triangle.length == 0) {
                return -1;
            }
            if (triangle[0] == null || triangle[0].length == 0) {
                return -1;
            }

            // f[x][y] = minimum path value from 0,0 to x,y
            int n = triangle.length;
            int[][] f = new int[n][n];

            // 初始化三角形最左一边和最右一边（因为 0，0 到当前点的最短距离只有一条
            f[0][0] = triangle[0][0];
            for (int i = 1; i < n; i++) {
                f[i][0] = f[i-1][0] + triangle[i][0];
                f[i][i] = f[i-1][i-1] + triangle[i][i];
            }

            // top-down自顶向下求 0，0 到 当前点的最短距离
            // 左上角和右上角下来的路径做比较，取较短的
            for (int i = 1; i < n; i++) {
                for (int j = 1; j < i; j++) {
                    f[i][j] = Math.min(f[i-1][j], f[i-1][j-1]) + triangle[i][j];
                }
            }

            // 最后一行比较取最小的
            int best = f[n-1][0];
            for (int i = 1; i < n; i++) {
                best = Math.min(best, f[n-1][i]);
            }
            return best;
        }

        // 方法2
        // 时间复杂度 O(n^2)
        // 空间复杂度 O(n)
        public int minimumTotal2(int[][] triangle) {
            if (triangle == null || triangle.length == 0) {
                return -1;
            }
            if (triangle[0] == null || triangle[0].length == 0) {
                return -1;
            }
            // f[x][y] = minimum path value from 0,0 to x,y
            int n = triangle.length;
            int[][] f = new int[2][n]; // 一行存上一行的数，一行存当前行的数。缩小空间复杂度

            // 初始化三角形最左一边和最右一边（因为 0，0 到当前点的最短距离只有一条）
            f[0][0] = triangle[0][0];
            for (int i = 1; i < n; i++) {
                f[i % 2][0] = f[(i-1) % 2][0] + triangle[i][0];
                f[i % 2][i] = f[(i-1) % 2][i-1] + triangle[i][i];
            }

            // top-down自顶向下求 0，0 到 当前点的最短距离
            // 左上角和右上角下来的路径做比较，取较短的
            for (int i = 1; i < n; i++) {
                for (int j = 1; j < i; j++) {
                    f[i % 2][j] = Math.min(f[(i-1) % 2][j], f[(i-1) % 2][j-1]) + triangle[i][j];
                }
            }

            // 最后一行比较取最小的
            int best = f[(n-1) % 2][0];
            for (int i = 1; i < n; i++) {
                best = Math.min(best, f[(n-1) % 2][i]);
            }
            return best;
        }
    }

    // 2. 分割回文串 · Palindrome Partitioning
    // 给定字符串 s, 需要将它分割成一些子串, 使得每个子串都是回文串
    public class PalindromePartitionSolution {
        List<List<String>> results;
        boolean[][] isPalindrome;

        public List<List<String>> partition(String s) {
            results = new ArrayList<>();
            if (s == null || s.length() == 0) {
                return results;
            }

            getIsPalindrome(s);
            List<String> partition = new ArrayList<>(); // 一种分割方案里的所有子串
            helper(s, 0, partition);

            return results;
        }
        private void helper(String s, int startIndex, List<String> partition) {
            if (startIndex == s.length()) {
                results.add(new ArrayList<String>(partition));
                return;
            }

            for (int i = startIndex; i < s.length(); i++) {
                String subStr = s.substring(startIndex, i + 1); // 左闭右开区间[start, end)
                if (isPalindrome[startIndex][i]) {
                    partition.add(subStr);
                    helper(s, i + 1, partition);
                    partition.remove(partition.size() - 1);
                }
            }
        }
        //   b c d e f
        // b 1
        // c   1
        // d     1
        // e       1
        // f         1
        // 从i-j是否为回文串
        private void getIsPalindrome(String s) {
            int n = s.length();
            isPalindrome = new boolean[n][n];

            for (int i = 0; i < n; i++) { // i - i 只有一个数 肯定回文
                isPalindrome[i][i] = true;
            }
            for (int i = 0; i < n - 1; i++) { // i - i+1 两个数相等才能为回文
                isPalindrome[i][i + 1] = (s.charAt(i) == s.charAt(i + 1));
            }

            for (int i = n - 3; i >= 0; i--) { // n-1行有一个数，n-2行有两个数，已经判断过
                for (int j = i + 2; j < n; j++) { // 每行的前两个数，已经判断过
                    isPalindrome[i][j] = isPalindrome[i + 1][j - 1] && s.charAt(i) == s.charAt(j);
                }
            }
        }
    }

    // 3. 最长连续序列 · Longest Consecutive Sequence

    // 1143. Longest Common Subsequence
    // 返回两个String的最长公共子序列
    public class LCSSolution {
        public int longestCommonSubsequence(String text1, String text2) {

            int m = text1.length();
            int n = text2.length();

            // dp[i][j]表示 text1从0到i-1和text2从0刀j-1的longest common subsequence
            // 多设置一位是为了让dp[0][j]和dp[i][0]表示空串
            int[][] dp = new int[m + 1][n + 1];
            for (int i = 1; i <= m; i ++) {
                for (int j = 1; j <= n; j ++) {
                    if (text1.charAt(i - 1) == text2.charAt(j - 1)) { // 最后一位相同，lcs增加一位
                        dp[i][j] = dp[i - 1][j - 1] + 1;
                    } else { // 最后一位不同，取相邻更大的那一个
                        dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                    }
                }
            }

            return dp[m][n];
        }
    }

}
