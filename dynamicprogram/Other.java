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

}
