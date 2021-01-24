package twopointers;

import java.util.*;

public class TwoSum {
    // 1. Two Sum
    // 给一个整数数组，找到两个数使得他们的和等于一个给定的数 target
    // 返回两个数的下标
    public class TwoSum1Solution {
        // 方法1 HashMap
        public int[] twoSum(int[] numbers, int target) {
            // 用一个hashmap，key记录target-numbers[i]的值，value记录numbers[i]的i的值
            // 如果碰到一个numbers[j]在hashmap中存在，那么说明前面的某个numbers[i]和numbers[j]的和为target，i和j即为答案
            Map<Integer, Integer> hash = new HashMap<>();
            for (int i = 0; i < numbers.length; i++) {
                if (hash.containsKey(numbers[i])) {
                    int[] result = {hash.get(numbers[i]), i};
                    return result;
                }
                hash.put(target - numbers[i], i);
            }

            int[] result = {};
            return result;
        }

        // 方法2 2 pointers
        public int[] twosum2(int[] numbers, int target) {
            // 对数组排序，在排序后的数组中利用双指针从左右向中间寻找
            // numbers[i] + numbers [j] == target 说明找到答案
            // numbers[i] + numbers [j] < target 说明最小的数应该往上加
            // numbers[i] + numbers [j] > target 说明最大的数应该往下减
            int[] backUp = new int[numbers.length];
            System.arraycopy(numbers, 0, backUp, 0, numbers.length);
            Arrays.sort(numbers);

            int i = 0, j = numbers.length - 1;
            while (i < j) {
                if (numbers[i] + numbers[j] == target) { // 找到答案
                    break;
                } else if (numbers[i] + numbers [j] < target) { // 左指针右移
                    i ++;
                } else { // 右指针左移
                    j --;
                }
            }

            int a = 0,b = 0;// 标记是否找到，避免两个相同的数，会一直进第一个if
            for (int k = 0; k < numbers.length; k++) {
                if (backUp[k] == numbers[i] && a == 0) {
                    i = k;
                    a = 1;
                }
                else if (backUp[k] == numbers[j] && b == 0) {
                    j = k;
                    b = 1;
                }
                else if (a == 1 && b == 1) {
                    break;
                }
            }
            int[] ans = new int[2];
            ans[0] = i;
            ans[1] = j;
            Arrays.sort(ans);
            return ans;
        }
    }

    // 2. Two Sum - Input array is sorted
    // 给定一个已经 按升序排列 的数组，找到两个数使他们加起来的和等于特定数。
    // 函数应该返回这两个数的下标，index1必须小于index2。注意返回的值不是 0-based。
    public class TwoSumSortedSolution {
        public int[] twoSum(int[] nums, int target) {
            if (nums == null || nums.length < 2) {
                return null;
            }

            int start = 0, end = nums.length - 1;
            while (start < end) {
                if (nums[start] + nums[end] == target) { // 返回第几个数，需要+1
                    int[] result = new int[2];
                    result[0] = start + 1;
                    result[1] = end + 1;
                    return result;
                }
                if (nums[start] + nums[end] < target) {
                    start++;
                } else {
                    end--;
                }
            }
            // 没有找到
            return null;
        }
    }

    // 3. Two Sum - Unique pairs
    // 给一整数数组, 找到数组中有多少组 不同的元素对 有相同的和, 且和为给出的 target 值
    // 返回对数
    // 输入: nums = [1,1,2,45,46,46], target = 47
    // 输出: 2 (1 + 46 = 47, 2 + 45 = 47)
    public class TwoSumPairsSolution {

        public int twoSum(int[] nums, int target) {
            if (nums == null || nums.length < 2) {
                return 0;
            }
            Arrays.sort(nums);
            int cnt = 0;
            int left = 0, right = nums.length - 1;
            while (left < right) {
                if (nums[left] + nums[right] == target) {
                    cnt ++;
                    left ++;
                    right --;
                    // 去除重复元素
                    while (left < right && nums[left] == nums[left - 1]) {
                        left ++;
                    }
                    while (left < right && nums[right] == nums[right + 1]) {
                        right --;
                    }
                } else if (nums[left] + nums[right] < target) {
                    left ++;
                } else {
                    right --;
                }
            }
            return cnt;
        }
    }

    // 4. 三数之和 · 3Sum
    // 给出一个有n个整数的数组S，在S中找到三个整数a, b, c，找到所有使得 a + b + c = 0 的三元组
    public class ThreeSumSolution {
        // 用2sum的方法
        List<List<Integer>> results;
        public List<List<Integer>> threeSum(int[] nums) {
            results = new ArrayList<>();
            if (nums == null || nums.length < 3) {
                return results;
            }

            Arrays.sort(nums);

            for (int i = 0; i < nums.length - 2; i++) { // 因为需要取3个数，所以length-2
                // 跳过第一个数相等的结果
                if (i > 0 && nums[i] == nums[i-1]) {
                    continue;
                }

                int left = i + 1, right = nums.length - 1;
                int target = -nums[i];
                twoSum(nums, left, right, target);
            }

            return results;
        }

        private void twoSum(int[] nums, int left, int right, int target) {
            while (left < right) {
                if (nums[left] + nums[right] == target) {
                    ArrayList<Integer> triple = new ArrayList<>();
                    triple.add(-target);
                    triple.add(nums[left]);
                    triple.add(nums[right]);
                    results.add(triple);

                    left ++;
                    right --;
                    // 跳过重复元素
                    while (left < right && nums[left] == nums[left - 1]) {
                        left ++;
                    }
                    while (left < right && nums[right] == nums[right + 1]) {
                        right --;
                    }
                } else if (nums[left] + nums[right] < target) {
                    left ++;
                } else {
                    right --;
                }
            }
        }
    }

    // 5. 三角形计数 · Triangle Count
    // 给定一个整数数组，在该数组中，寻找三个数，分别代表三角形三条边的长度
    // 问可以寻找到多少组这样的三个数来组成三角形
    public class TriangleCountSolution {
        // “较短的两边之和大于最长边”就可以保证是三角形
        public int triangleCount(int[] S) {
            if (S == null || S.length < 2) {
                return 0;
            }
            int result = 0;
            int left = 0, right = S.length - 1;
            Arrays.sort(S);

            // i循环的是最大边
            // 剩余的两条边left从左到右，right从最大边开始向左
            for (int i = 0; i < S.length - 1; i++) {
                left = 0;
                right = i - 1;
                while (left < right) {
                    if (S[left] + S[right] > S[i]) {
                        result += (right - left); // 这个区间里的所有数都可以组成三角形
                        right --;
                    } else {
                        left ++;
                    }
                }
            }

            return result;
        }
    }

    // 6. Two Sum - Less than or equal to target
    // 给定一个整数数组，找出这个数组中有多少对的和是小于或等于目标值
    // 返回对数
    public class TwoSumLessThanTargetSolution {
        // 类似于三角形那题
        public int twoSum(int[] nums, int target) {
            if (nums == null || nums.length < 2) {
                return 0;
            }
            Arrays.sort(nums);
            int result = 0;
            int left = 0, right = nums.length - 1;
            while (left < right) {
                if (nums[left] + nums[right] <= target) {
                    result += right - left;
                    left ++;
                } else { // >
                    right --;
                }
            }
            return result;
        }
    }

    // 7. Two Sum - Greater than target
    // 给一组整数，问能找出多少对整数，他们的和大于一个给定的目标值
    public class TwoSumGreaterThanTargetSolution {
        public int twoSum(int[] nums, int target) {
            if (nums == null || nums.length < 2) {
                return 0;
            }
            Arrays.sort(nums);
            int result = 0;
            int left = 0, right = nums.length - 1;
            while (left < right) {
                if (nums[left] + nums[right] > target) {
                    result += right - left;
                    right --;
                } else {
                    left ++;
                }
            }
            return result;
        }
    }

    // 8. Two Sum Closest
    // 给定整数数组num，从中找到两个数字使得他们和最接近target
    // 返回两数和与 target 的差的 绝对值
    public class TwoSumClosestSolution {

        public int twoSumClosest(int[] nums, int target) {
            if (nums == null || nums.length < 2) {
                return -1;
            }

            Arrays.sort(nums);

            int left = 0, right = nums.length - 1;
            int diff = Integer.MAX_VALUE;
            while (left < right) {
                if (nums[left] + nums[right] < target) { // 如果right减小只会差距越来越大
                    diff = Math.min(diff, target - nums[left] - nums[right]);
                    left ++;
                } else {
                    diff = Math.min(diff, nums[left] + nums[right] - target);
                    right --;
                }
            }

            return diff;
        }
    }

    // 9. Three Sum Closest
    // 给一个包含n个整数的数组S, 找到和与给定整数 target 最接近的三元组
    // 返回这三个数的和
    public class ThreeSumClosestSolution {
        public int threeSumClosest(int[] nums, int target) {
            if (nums == null || nums.length < 3) {
                return 0;
            }
            long nearest = Integer.MAX_VALUE; // 对nearst-target取绝对值会越界，所以要转long
            Arrays.sort(nums);
            // 遍历第一个数，第二个数从i+1开始往后，第三个数从数组末尾往前
            for (int i = 0; i < nums.length - 2; i++) {
                // 去重剪枝1
                if (i > 0 && nums[i] == nums[i - 1]) {
                    continue;
                }

                int left = i + 1;
                int right = nums.length - 1;
                while (left < right) {
                    int currSum = nums[left] + nums[right] + nums[i];
                    if (currSum == target) {
                        return target;
                    }
                    // 更新nearest
                    if (Math.abs(currSum - target) < Math.abs(nearest - target)) {
                        nearest = currSum;
                    }

                    if (currSum < target) {
                        left ++;
                        while (left < right && nums[left] == nums[left - 1]) { // 去重剪枝2
                            left ++;
                        }
                    } else {
                        right --;
                        while (left < right && nums[right] == nums[right + 1]) { // 去重剪枝3
                            right --;
                        }
                    }
                }
            }
            return (int)nearest;
        }
    }
    public class ThreeSumCLosetSolution2AsTwoClosest {
        public int threeSumClosest(int[] nums, int target) {
            Arrays.sort(nums);
            if (nums == null || nums.length < 3) {
                return 0;
            }
            //假设a<=b<=c，我们循环a来做 对于target-a的2SumClosest
            long result = Integer.MAX_VALUE;
            for (int i = 0; i < nums.length - 2; i++) {
                if (i != 0 && nums[i] == nums[i-1]) {
                    continue;
                }
                int twoSum = twoSumClosest(nums, i, target - nums[i]);  //找Two Sum最接近target-a的
                int threeSumSoFar = twoSum + nums[i];
                result = Math.abs(threeSumSoFar - target) < Math.abs(result - target) ? threeSumSoFar : result;
            }
            return (int)result;
        }

        private int twoSumClosest(int[] nums, int i, int target) {
            int left = i + 1;
            int right = nums.length - 1;
            long nearestSum = Integer.MAX_VALUE;
            while (left < right) {
                int sum = nums[left] + nums[right];
                if (sum < target) {
                    nearestSum = Math.abs(sum-target) < Math.abs(nearestSum-target) ? sum : nearestSum;
                    left++;
                } else {
                    nearestSum = Math.abs(sum-target) < Math.abs(nearestSum-target) ? sum : nearestSum;
                    right--;
                }
            }
            return (int)nearestSum;
        }
    }

    // 10. Four Sum
    // 给一个包含n个数的整数数组S，在S中找到所有使得和为给定整数target的四元组(a, b, c, d)
    public class FourSumSolution {
        List<List<Integer>> result;
        public List<List<Integer>> fourSum(int[] nums, int target) {
            result = new ArrayList<List<Integer>>();
            Arrays.sort(nums);

            for (int i = 0; i < nums.length - 3; i++) {
                if (i > 0 && nums[i] == nums[i - 1]) {
                    continue;
                }

                for (int j = i + 1; j < nums.length - 2; j++) {
                    if (j > i + 1 && nums[j] == nums[j - 1]) {
                        continue;
                    }

                    int left = j + 1;
                    int right = nums.length - 1;
                    while (left < right) {
                        int sum = nums[i] + nums[j] + nums[left] + nums[right];
                        if (sum < target) {
                            left ++;
                        } else if (sum > target) {
                            right --;
                        } else { // ==
                            ArrayList<Integer> temp = new ArrayList<>();
                            temp.add(nums[i]);
                            temp.add(nums[j]);
                            temp.add(nums[left]);
                            temp.add(nums[right]);

                            result.add(temp);

                            left ++;
                            right --;
                            while (left < right && nums[left] == nums[left - 1]) {
                                left ++;
                            }
                            while (left < right && nums[right] == nums[right + 1]) {
                                right --;
                            }
                        }
                    } // while
                } // j
            } // i
            return result;
        }
    }

    // 11. Two Sum - Difference equals to target
    // 给定一个排序后的整数数组，找到两个数的 差 等于目标值。
    // 返回一个包含两个数字的列表[num1, num2], 使得num1与num2的差为target，同时num1必须小于num2
    public class TwoSumDifferenceSolution {
        // num[j]-num[i]< target时，说明j太小，于是我们将j++，直到num[j]-num[i] >= target
        // 若num[j]-num[i] > target，我们将i++
        // 若num[j]-num[i] = target说明我们找到答案
        public int[] twoSum(int[] nums, int target) {
            if (nums == null || nums.length < 2) {
                return new int[]{-1, -1};
            }
            target = Math.abs(target);

            int j = 1;
            // j指针只往后走一遍，整体复杂度O(n)
            for (int i = 0; i < nums.length; i++) {
                // 上一轮i，在j-1时候差值<target，j时候差值>target
                // 这一轮i增大了，j-1位置只有可能差值更小于target，因此j之前的位置都不可能
                j = Math.max(j, i + 1);
                while (j < nums.length && nums[j] - nums[i] < target) {
                    j ++; // 指针右移到num[j] - num[i] >= target
                }
                if (j >= nums.length) {
                    break; // 防止越界
                }
                if (nums[j] - nums[i] == target) {
                    return new int[]{nums[i], nums[j]};
                }
            }

            return new int[]{-1, -1};
        }
    }

}
