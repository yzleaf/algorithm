package dynamicprogram;

import java.util.*;

public class Other {
    // 795. Number of Subarrays with Bounded Maximum
    // 正整数的数组A，正整数L, R
    // 求连续、非空且其中最大元素满足L<= max_element <=R的子数组个数。
    // 输入: A = [2, 1, 4, 3] L = 2 R = 3
    // 输出: 3
    // 解释: 满足条件的子数组: [2], [2, 1], [3].
    public class NumSubarrayBoundedMaxSolution {
        public int numSubarrayBoundedMax(int[] nums, int left, int right) {
            if (nums == null || nums.length == 0) {
                return 0;
            }
            // arr的所有子数组中，根据子数组最大值情况，可分为三类：(..., L-1], [L, R], [R+1, ...)
            // 范围在[L, R]的个数 == count(..., R] - count(..., L-1]
            return countOfFloor(nums, right) - countOfFloor(nums, left - 1);
        }
        private int countOfFloor(int[] nums, int k) {
            int n = nums.length;
            int count = 0;

            // dp[i]:以nums[i]结尾的满足最大值小于k的数组的个数
            int[] dp = new int[n];
            dp[0] = nums[0] <= k ? 1 : 0; // 比bound小，就有自己1个
            count += dp[0];

            for (int i = 1; i < n; i++) {
                if (nums[i] <= k) {
                    dp[i] = dp[i-1] + 1; // 以nums[i]结尾的：包括前面i-1结尾的每个数组+nums[i]并以之结尾的个数，以及nums[i]自己一个数作为subarray
                } else {
                    dp[i] = 0;
                }
                count += dp[i];
            }
            // 优化： dp[i]可以优化成dp一个变量，减小空间
            return count;
        }
    }

    // 629. K Inverse Pairs Array
    // 找出所有包含从1到n的数字，且恰好拥有k个逆序对的不同的数组的个数。
    // 逆序对：对于数组的第i个和第j个元素，如果满i < j且 a[i] > a[j]
    public class KInversePairsSolution {
        // f(i,j) 表示从1-i有j个逆序对的，不同数组个数
        // f(i,j) = f(i-1,j) + f(i-1,j-1) + ... + f(i-1,j-(i-1))    1式
        //    第i个元素: 放最后   放倒数第2个           放第1个
        // 用j-1替换上式j得：
        // f(i,j-1) = f(i-1,j-1) + f(i-1,j-2) + ... + f(i-1,j-i)    2式

        // 1式-2式得： f(i,j) = f(i-1,j) + f(i,j-1) - f(i-1,j-i)
        // f(i,0)=1, f(i,j)=0 when j<0
        public int kInversePairs(int n, int k) {
            int[][] dp = new int[n+1][k+1];
            int M = 1000000007;

            dp[1][0] = 1;
            for (int i = 2; i < n+1; i++) {
                dp[i][0] = 1;
                for (int j = 1; j < k+1; j++) {
                    if (j >= i) {
                        dp[i][j] = dp[i-1][j] + dp[i][j-1] - dp[i-1][j-i];
                    } else {
                        dp[i][j] = dp[i-1][j] + dp[i][j-1] - 0;
                    }
                    dp[i][j] = dp[i][j] >= 0 ? dp[i][j]%M : (dp[i][j] + M);
                }
            }

            return dp[n][k];
        }
    }

    // 139 Word Break
    // s = "leetcode", wordDict = ["leet","code"]
    // 判断s能否拆成字典里的string的组合
    class Solution {
        public boolean wordBreak(String s, List<String> wordDict) {

            Set<String> hash = new HashSet<>(wordDict);

            // dp[i]表示从[0, i)是否为字典中出现的
            // dp[i+1]需要判断：dp[i]和(i,i+1)是否出现过
            boolean[] dp = new boolean[s.length() + 1];
            dp[0] = true;

            for (int i = 0; i <= s.length(); i ++) {
                for (int j = 0; j < i; j ++) { // 枚举j这个0-i中间的拆分点
                    if (dp[j] && hash.contains(s.substring(j, i))) {
                        dp[i] = true;
                        break; // 只要有一个j满足就可以直接看下一个i
                    }
                }
            }
            // 优化：j从后往前遍历，只要[j,i)长度超过Hash中的最长String，就直接break

            return dp[s.length()];
        }
    }

    // 983. Minimum Cost For Tickets
    // 1天、7天、30天的票价和出行日期的数组，返回花最少的钱能cover所有旅游日子
    public class MinCostSolution {
        // dp[i] 表示到第 i 天结束时完成之前所有旅行的最低消费
        // 第i天不不旅行
        //     dp[i] = dp[i-1]
        // 第i天出去旅行，有三种情况
        //     1.买一天票，之前一天消费dp[i-1]加上，花了dp[i-1] + cost[0]
        //     2.买七天票，之前8天dp[i-7]加上，在之后的一天买了7天票，正好cover今天，花了dp[i-7] + cost[1]
        //     3.买三十天票，之前31天dp[i-30]加上，在之后的一天买了30天，正好cover今天，花了dp[i-30] + cost[2]
        // 取最小值 dp[i] = min(dp[i - 1] + cost[0], dp[i - 7] + cost[1], dp[i - 30] + cost[2])
        public int mincostTickets(int[] days, int[] costs) {
            int[] dayTravel = new int[366];
            int[] dp = new int[366];
            // 把旅行日子的index置为1
            for (int d : days) {
                dayTravel[d] = 1;
            }

            dp[0] = 0;
            for (int i = 1; i < dp.length; i ++) {
                if (dayTravel[i] == 1) { // 今天有旅行
                    dp[i] = (i >= 1 ? dp[i - 1] : 0) + costs[0]; // 右括号的位置要把前面都包上
                    dp[i] = Math.min(dp[i], (i >= 7 ? dp[i - 7] : 0) + costs[1]);
                    dp[i] = Math.min(dp[i], (i >= 30 ? dp[i - 30] : 0) + costs[2]);
                } else { // 今天没有旅行
                    dp[i] = dp[i - 1];
                }
            }

            return dp[dp.length - 1];
        }
    }

    // 1216. Valid Palindrome III
    // 给定字符串，计算最少的删除次数，使之成为回文串
    // 返回是否能在k步内完成
    public class ValidPalindrome3Solution {
        // 方法2 从bottom到top的dp方法
        public boolean isValidPalindrome2(String s, int k) {
            int n = s.length();
            char[] sArr = s.toCharArray();

            // dp[i][j]:从i到j构成回文串需要处理的次数
            int[][] dp = new int[n][n];

            // dp外层循环从子问题的长度开始，不断增加
            for (int len = 2; len <= n; len ++) {
                for (int i = 0, j = len - 1; j < n; i ++, j ++) {
                    if (sArr[i] == sArr[j]) {
                        // [0][3] <- [1][2] 可以看出在长度len为2的时候已经得到这个i+1, j-1了
                        dp[i][j] = dp[i + 1][j - 1];
                    } else {
                        // 不相等时，需要多处理1次，跳过一个字符（可能处理左边i，也可能处理右边j）
                        dp[i][j] = Math.min(dp[i + 1][j], dp[i][j - 1]) + 1;
                    }
                }
            }

            return dp[0][n - 1] <= k;
        }

        // 方法1 从top到bottom的记忆化递归方法
        int[][] dp;
        public boolean isValidPalindrome1(String s, int k) {
            int n = s.length();
            dp = new int[n][n];
            for (int i = 0; i < n; i ++) {
                for (int j = 0; j < n; j ++) {
                    dp[i][j] = -1;
                }
            }
            return palindromeCount(s, 0, n - 1) <= k;
        }
        // 当前字串为回文串需要处理的个数
        private int palindromeCount(String s, int i, int j) {
            if (dp[i][j] != -1) {
                return dp[i][j]; //  记忆化 递归
            }
            if (i >= j) { // 相等的时候只有一个字符，肯定回文，不需要处理
                return 0;
            }
            if (i == j - 1) {
                return s.charAt(i) == s.charAt(j) ? 0 : 1;
            }

            // index相差2以上
            if (s.charAt(i) == s.charAt(j)) {
                dp[i][j] = palindromeCount(s, i + 1, j - 1);
            } else {
                dp[i][j] = Math.min(palindromeCount(s, i + 1, j), palindromeCount(s, i, j - 1)) + 1;
            }

            return dp[i][j];
        }
    }

    // 935. Knight Dialer
    // 4*3 数字键盘上，数字位置可以有Knight，按日走
    // Given an integer n, return how many distinct phone numbers of length n we can dial.
    // 其实就是求n步后有多少种当前位置的走法之和，类似于688
    public class KnightDialerSolution {

        public int knightDialer(int n) {
            // dp[k][i][j] 经过k步到达(i,j)的步数
            // dp[k][i][j] = sum dp[k-1][x][y] （k-1轮所有(x,y)能够到达(i,j)的数加起来）
            final int MOD = 1000_000_007;
            int[] dx = {-2, -2, -1, -1, 1, 1, 2, 2};
            int[] dy = {-1, 1, -2, 2, -2, 2, -1, 1};

            // 在(i,j)的步数，初始位置为1，除去两个不能放置的点
            int[][] dp0 = new int[4][3];
            for (int i = 0; i < 4; i ++) {
                for (int j = 0; j < 3; j ++) {
                    if (i == 3 && j != 1) {
                        continue;
                    }
                    dp0[i][j] = 1;
                }
            }

            for (int step = 1; step < n; step ++) {
                int[][] dp1 = new int[4][3];
                for (int i = 0; i < 4; i ++) {
                    for (int j = 0; j < 3; j ++) {
                        for (int k = 0; k < 8; k ++) {
                            int nextX = i + dx[k];
                            int nextY = j + dy[k];
                            if (nextX < 0 || nextX >= 4 || nextY < 0 || nextY >= 3) {
                                continue;
                            }
                            if (nextX == 3 && nextY != 1) { // * #不能走
                                continue;
                            }
                            dp1[nextX][nextY] = (dp1[nextX][nextY] + dp0[i][j]) % MOD;
                        }
                    }
                }
                dp0 = dp1;
            }

            // 最后一步跳出循环后，把dp0上所有的点的步数加起来
            int res = 0;
            for (int i = 0; i < 4; i ++) {
                for (int j = 0; j < 3; j ++) {
                    res = (res + dp0[i][j]) % MOD;
                }
            }
            return res;
        }
    }

    // 10. Regular Expression Matching
    // .表示任意字母 字母和*表示这个字母出现任意次，看两个字符串是否匹配
    // s = "aab", p = "c*a*b"
    // 匹配c*可以表示出现0次 a*表示a出现两次
    public class IsMatchSolution {
        public boolean isMatch(String s, String p) {

            // dp[i][j]：s取i个字符，p取j个字符，是否匹配
            boolean[][] dp = new boolean[s.length() + 1][p.length() + 1];

            // 1. base case
            dp[0][0] = true;
            // p取0个字符，s取i个，肯定是false
            // s取0个字符，p取i个，可能存在a*b*c*这种情况（都为0）
            for (int j = 1; j <= p.length(); j ++) {
                if (p.charAt(j - 1) == '*') { // j-1为index
                    dp[0][j] = dp[0][j - 2];
                }
                // 题目已经说了first char不会为*
            }

            // 2. 中间转移状态
            for (int i = 1; i <= s.length(); i ++) {
                for (int j = 1; j <= p.length(); j ++) {
                    // 2.1 如果当前的char相等，则由上一个状态决定
                    if (s.charAt(i - 1) == p.charAt(j - 1) || p.charAt(j - 1) == '.') {
                        dp[i][j] = dp[i - 1][j - 1];
                    } else if (p.charAt(j - 1) == '*') {
                        // 2.2 如果当前char不相等，需要考虑当前p是*
                        // 2.2.1 匹配前一个char，把*当成前面的一个或多个字符
                        // s: abcddddd
                        // p: abcd*
                        // 需要看s中当前d前面的所有字符是否与abcd*匹配
                        if (p.charAt(j - 2) == s.charAt(i - 1) || p.charAt(j - 2) == '.') {
                            dp[i][j] = dp[i - 1][j] || dp[i][j - 2];
                            // [i-1][j]指的是abcdddd与abcd*是否匹配
                            // [i][j-2]指的是abcd与abcdd*这种情况是匹配的，但是上面一个式子无法满足（abcd与abcdd不匹配）
                        } else {
                            // 2.2.2 把*当成0个字符
                            // s: abcd
                            // p: abcde*
                            dp[i][j] = dp[i][j - 2];
                        }
                    }
                }
            }

            // 3. 结果状态
            return dp[s.length()][p.length()];
        }
    }
}
