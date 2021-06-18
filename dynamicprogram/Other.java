package dynamicprogram;

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
}
