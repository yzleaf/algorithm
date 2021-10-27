package array;

import java.util.*;

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
                result = Math.max(result, sum - prefixSumMinSoFar); // 到目前为止最大的差值（某一区间和的最大值=sum-prefixSumMinSoFar）
                prefixSumMinSoFar = Math.min(sum, prefixSumMinSoFar); // 更新最小的prefix sum 为下一个“到目前为止”做准备
            }

            return result;
        }
    }

    // 2. Subarray Sum
    // 给定一个整数数组，找到和为0的子数组
    // 返回满足要求的子数组的起始位置和结束位置
    public class SubarraySumSolution {
        public ArrayList<Integer> subarraySum(int[] nums) {
            ArrayList<Integer> result = new ArrayList<>();
            Map<Integer, Integer> hash = new HashMap<>();

            // hash表，sum是第i个位置的前缀和（从开头到当前点的和）
            // (sum, index)
            hash.put(0, -1);

            int sum = 0;
            for (int i = 0; i < nums.length; i++) {
                sum += nums[i]; // 不断累加前缀和
                // 前缀和曾经在a出现，现在又出现，表示从a+1开始到当前点的区间和为0（因为前缀和是开头到当前点的和）
                if (hash.containsKey(sum)) {
                    result.add(hash.get(sum) + 1);
                    result.add(i);
                    break;
                }

                // 前缀和第一次出现，放入hash表
                hash.put(sum, i);
            }

            return result;
        }
    }

    // 3. Subarray Sum Closest
    // 找到一个和最接近于零的子数组
    // 返回起始位置和结束位置
    public class SubarraySumClosestSolution {
        // 先对数组求一遍前缀和，对前缀和排序，令排完序的前缀和是B数组
        // 题目要求子数组的和最接近0，也就是B数组两个值相减最接近0
        // 排完序后，我们只要找相邻元素做差就好了
        public int[] subarraySumClosest(int[] nums) {
            int[] result = new int[2];

            if (nums == null || nums.length == 0) {
                return result;
            }
            if (nums.length == 1) {
                result[0] = result[1] = 0;
                return result;
            }

            Map<Integer, Integer> hash = new HashMap<>();
            // 前缀和数组会多出一个不取任何数，当前的前缀和为0
            int[] prefixSum = new int[nums.length + 1];
            hash.put(0, -1); // (sum, index), index为-1，前缀和为0
                             // 这个数据存在的意义在于如果一个序列从开头到当前最小，则左边起始位是-1 + 1 = 0
            int sum = 0;
            prefixSum[0] = 0;

            for (int i = 0; i < nums.length; i++) {
                sum += nums[i];
                if (hash.containsKey(sum)) {
                    result[0] = hash.get(sum) + 1;
                    result[1] = i;
                    return result;
                }

                hash.put(sum, i);
                prefixSum[i + 1] = sum; // 放置前缀和, 用来排序的数组
            }

            // 对前缀和数组排序
            Arrays.sort(prefixSum);

            // 求数组相邻元素的最小差值
            int minDiff = Integer.MAX_VALUE;
            int left = 0, right = 0;
            for (int i = 0; i < prefixSum.length - 1; i++) {
                int diff = prefixSum[i + 1] - prefixSum[i];
                if (minDiff > diff) {
                    minDiff = diff;
                    left = prefixSum[i];
                    right = prefixSum[i + 1];
                }
            }
            if (hash.get(left) < hash.get(right)) { // 根据sum得到index
                result[0] = hash.get(left) + 1;
                result[1] = hash.get(right);
            } else { // >=
                result[0] = hash.get(right) + 1;
                result[1] = hash.get(left);
            }

            return result;
        }
    }

    // 523. Continuous Subarray Sum
    // 找到连续的subarray（长度大于2），和为输入k的倍数
    public class SubarraySumKSolution {
        public boolean checkSubarraySum(int[] nums, int k) {
            // 前缀和，差为subarray的和
            // 差为k的倍数，表示各自除以k的余数相等
            // 用Map记录余数对应的下标，只要index之差大于2就是答案
            Map<Integer, Integer> remainderIndex = new HashMap<>();

            int preSum = 0;
            remainderIndex.put(0, - 1);
            for (int i = 0; i < nums.length; i ++) {
                preSum += nums[i];
                int remainder = preSum % k;
                if (!remainderIndex.containsKey(remainder)) {
                    remainderIndex.put(remainder, i);
                } else { // 包括这个余数，直接计算index之差
                    int preIndex = remainderIndex.get(remainder);
                    if (i - preIndex >= 2) {
                        return true;
                    }
                }

            }

            return false;
        }
    }
}
