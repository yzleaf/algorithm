package array;

public class Subarray {

    // 1. Maximum Subarray
    // 给定一个整数数组，找到一个具有最大和的子数组（子数组是连续的），返回其最大和
    // 输入：[−2,2,−3,4,−1,2,1,−5,3]
    // 输出：6
    // 解释：符合要求的子数组为[4,−1,2,1]，其最大和为6
    public class MaxSubArraySolution {
        // 方法1 greedy
        public int maxSubArray1(int[] nums) {
            if (nums == null || nums.length == 0) {
                return 0;
            }
            // maxAns记录全局最大值 sum记录当前子数组的和
            int maxAns = Integer.MIN_VALUE;
            int sum = 0;

            for (int i = 0; i < nums.length; i++) {
                sum += nums[i];
                maxAns = Math.max(maxAns, sum);
                // 如果sum<0, 表示当前的子数组和已经为负，累加到后面会使和更小，因此令相当于放弃当前的子数组，重新开始
                sum = Math.max(sum, 0);
            }

            return maxAns;
        }

        // 方法2 prefix sum
        public int maxSubArray2(int[] nums) {
            if (nums == null || nums.length == 0) {
                return 0;
            }

            int sum = 0; // 当前的总和prefix sum（从开头到当前点）
            int prefixSumMinSoFar = 0; // 到目前为止最小的一个prefix sum
            int result = Integer.MIN_VALUE;
            for (int i = 0; i < nums.length; i++) {
                sum += nums[i];
                result = Math.max(result, sum - prefixSumMinSoFar); // 到目前为止最大的差值
                prefixSumMinSoFar = Math.min(sum, prefixSumMinSoFar); // 更新最小的prefix sum 为下一个“到目前为止”做准备
            }

            return result;
        }
    }
}
