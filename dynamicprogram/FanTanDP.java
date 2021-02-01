package dynamicprogram;

import java.util.*;

public class FanTanDP {
    // 1. Longest Increasing Subsequence
    // 300
    // 给定一个整数序列，找到最长上升子序列（LIS），返回LIS的长度
    // sequence是可以不连续的
    public class LongestIncreasingSubsequenceSolution {
        public int longestIncreasingSubsequence(int[] nums) {
            if (nums == null || nums.length == 0) {
                return 0;
            }

            int n = nums.length;

            // 设 dp[i] 表示以 nums[i] 为结尾的最长上升子序列的长度
            // 为了保证元素单调递增，只能从i前面且末尾元素比nums[i]小的状态转移过来
            int[] dp = new int[n];

            // 初始化 以自己结尾 默认为1
            for (int i = 0; i < n; i++) {
                dp[i] = 1;
            }

            // 判断状态
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < i; j++) { // 从A[j]到A[i]是否上升，且记录到i位置的最大增序列长度
                    if (nums[j] < nums[i]) {
                        dp[i] = Math.max(dp[j] + 1, dp[i]);
                    }
                }
            }

            int max = 0;
            for (int i = 0; i < n; i++) {
                max = Math.max(max, dp[i]);
            }
            return max;
        }

        // 方法2 贪心+二分
        // 建立dp数组，下标表示最长子序列长度，值表示当前这个子序列长度的最小结尾数据
        // 从前往后遍历nums数组，更改dp数组的值
        //    如果nums大于dp数组当前下标对应的值，直接更新至dp数组的下一位（最长子序列+1，新数值进入）
        //    如果nums小于等于dp数组当前下标对应的值，利用二分查找在当前下标之前找到nums应该在的位置并更新至nums值（更新最小结尾数）
        // 返回dp最大的有效index
        public int longestIncreasingSubsequence2(int[] nums) {
            int[] minLast = new int[nums.length + 1]; // 当前长度下最小的尾部元素
            minLast[0] = Integer.MIN_VALUE;
            for (int i = 1; i <= nums.length; i++) {
                minLast[i] = Integer.MAX_VALUE;
            }

            for (int i = 0; i < nums.length; i++) {
                // find the first number in minLast >= nums[i]
                // 如果nums[i]大于现有元素，会在后面一位直接添加
                // 如果nums[i]小于现有元素，会到前面去找，并且替换
                int index = binarySearch(minLast, nums[i]);
                minLast[index] = nums[i];
            }

            // 返回最长子序列结果
            for (int i = nums.length; i >= 1; i--) {
                if (minLast[i] != Integer.MAX_VALUE) {
                    return i;
                }
            }
            return 0;
        }
        // find the first number >= num 找第一个大于等于的元素替换它
        private int binarySearch(int[] minLast, int num) {
            int start = 0, end = minLast.length - 1;
            while (start + 1 < end) {
                int mid = start + (end - start) / 2;
                if (minLast[mid] < num) {
                    start = mid;
                } else {
                    end = mid;
                }
            }

            // 因为nums数组是n+1的，所以start从无穷小开始，所以start踩的位置永远都小于num，这个if其实是不起作用的
            // 放在这是为了跟模板一致
            if (minLast[start] == num) { // 先找左边的
                return start;
            }

            return end;
        }

    }

    // 2. Russian Doll Envelopes
    // 354
    // 给一定数量的信封，带有整数对 (w, h) 分别代表信封宽度和高度。一个信封的宽高均大于另一个信封时可以放下另一个信封。
    // 求最大的信封嵌套层数
    public class MaxEnvelopesSolution {
        // 先按宽度排序，再对高度求最长子序列
        public int maxEnvelopes(int[][] envelopes) {
            int n = envelopes.length;
            // 按宽度升序排列
            // 如果宽度一样，则按高度降序排列（因为如果高度升序的话，会把相同宽度的信封这个数组也算作一个增加的序列，但实际上宽度相同是不能嵌套的）
            Arrays.sort(envelopes, (a, b) -> a[0] != b[0] ? a[0] - b[0]
                                                          : b[1] - a[0]);

            // 对高度数组寻找 LIS
            int[] height = new int[n];
            for (int i = 0; i < n; i++) {
                height[i] = envelopes[i][1];
            }

            return lengthOfLIS(height);
        }
        // 求最长子序列，见上一题
        private int lengthOfLIS(int[] nums) {
            return 0;
        }
    }

    // 3. 最大整除子集 · Largest Divisible Subset
    // 给一个由 无重复的正整数 组成的集合
    // 找出满足任意两个元素 (Si, Sj) 都有 Si % Sj = 0 或 Sj % Si = 0 成立的最大子集
    public class LargestDivisibleSubsetSolution {
        // 1、对数组进行自然排序，找到某一时刻的状态
        // 2、状态分析（要找当前点的最大整数子集）
        //     第一次：取到1：1前面没有数，自己组成一个整除子集 [1]
        //     第二次：取到2：拿2跟之前的数进行对比，会有三种情况，以1为例
        //            如果2是1的倍数，那么把2放入1的最大整除子集，成为2的最大整除子集
        //            如果2不是1的倍数，那么2再往前比
        //            如果2比完前面所有的数都没有发现最大整除子集，那么把2自己作为一个整除子集
        //            很明显2的最大整除子集是 [1,2]
        //     第三次：取到3：拿2跟之前的数进行对比，会有三种情况，与第二次逻辑一致，
        //            结果是[1,3]

        public List<Integer> largestDivisibleSubset(int[] nums) {
            int[] lengths = new int[nums.length]; // 当前数加入后最大整除子集的长度
            int[] lasts = new int[nums.length]; // 当前数加入的最大整除子集的上一个数坐标，为了输出这个子集用

            Arrays.sort(nums);
            for (int i = 0; i < nums.length; i++) {
                int curLength = -1;
                int curLastIndex = -1;

                // 普通方法
                for (int j = i - 1; j >= 0; j--) { // 不断往前比，如果发现能找到被当前数整除的数，且长度最长，记录上一个数的坐标跟长度
                    if (nums[i] % nums[j] == 0 && lengths[j] + 1 > curLength) { // 可以整除j这个数且最大长度更长
                        curLength = lengths[j] + 1;
                        curLastIndex = j; // 记录上一个数
                    }
                }
                // 如果找不到，就设置为1，把自己作为这个整除子集
                lengths[i] = curLength == -1 ? 1 : curLength;
                lasts[i] = curLastIndex == -1 ? i : curLastIndex;
            }

            int maxLength = -1;
            int index = -1;
            // 找哪个数具有最大长度的整除子集
            for (int i = 0; i < lengths.length; i++) {
                if (lengths[i] > maxLength) {
                    maxLength = lengths[i];
                    index = i;
                }
            }

            List<Integer> result = new ArrayList<>();
            for (int i = 0; i < maxLength; i++) {
                result.add(nums[index]);
                index = lasts[index];
            }

            return result;
        }

        public List<Integer> largestDivisibleSubset2(int[] nums) {
            int[] lengths = new int[nums.length]; // 当前数加入后最大整除子集的长度
            int[] lasts = new int[nums.length]; // 当前数加入的最大整除子集的上一个数坐标，为了输出这个子集用

            Arrays.sort(nums);
            Map<Integer, Integer> valueToIndex = new HashMap<>(); // 存储排好序的nums数组里的value和index
            for (int i = 0; i < nums.length; i++) {
                valueToIndex.put(nums[i], i);
            }

            for (int i = 0; i < nums.length; i++) {
                int curLength = -1;
                int curLastIndex = -1;

                // 优化方法，找这个数的因子
                for (Integer factor : getFactors(nums[i])) {
                    if (valueToIndex.containsKey(factor)) {
                        int factorIndex = valueToIndex.get(factor);
                        if (lengths[factorIndex] + 1 > curLength) { // 可以整除j这个数且最大长度更长
                            curLength = lengths[factorIndex] + 1;
                            curLastIndex = factorIndex; // 记录上一个数
                        }
                    }
                }

                // 如果找不到，就设置为1，把自己作为这个整除子集
                lengths[i] = curLength == -1 ? 1 : curLength;
                lasts[i] = curLastIndex == -1 ? i : curLastIndex;
            }

            int maxLength = -1;
            int index = -1;
            // 找哪个数具有最大长度的整除子集
            for (int i = 0; i < lengths.length; i++) {
                if (lengths[i] > maxLength) {
                    maxLength = lengths[i];
                    index = i;
                }
            }

            List<Integer> result = new ArrayList<>();
            for (int i = 0; i < maxLength; i++) {
                result.add(nums[index]);
                index = lasts[index];
            }

            return result;
        }

        private List<Integer> getFactors(int num) { // 获得因子
            List<Integer> factors = new ArrayList();
            if (num == 1) {
                return factors;
            }
            int factor = 1;
            while (factor * factor <= num) { // sqrt n的时间
                if (num % factor == 0) {
                    factors.add(factor);
                    if (factor != 1 && num / factor != factor) { // 可理解成对称方向的factor，这里必须往里面加这个数才可以让while的条件成立
                        factors.add(num / factor);
                    }
                }
                factor++;
            }
            return factors;
        }
    }

    // 4. Frog Jump
    // 403
    // 一只青蛙正要过河，这条河分成了 x 个单位，每个单位可能存在石头，青蛙可以跳到石头上，但它不能跳进水里
    // 刚开始时青蛙在第一块石头上，假设青蛙第一次跳只能跳一个单位的长度
    // 如果青蛙本次跳 k 个单位，那么它下一次只能跳 k - 1 ，k 或者 k + 1 个单位

    // 给出石头的位置为 [0,1,3,5,6,8,12,17]
    // 总共8块石头。第一块石头在 0 位置，第二块石头在 1 位置，第三块石头在 3 位置，最后一块石头在 17 位置。
    // 返回 true。
    // 青蛙可以通过跳 1 格到第二块石头，跳 2 格到第三块石头，跳 2 格到第四块石头，跳 3 格到第六块石头，跳 4 格到第七块石头，最后跳 5 格到第八块石头
    public class FrogJumpSolution {
        // 采用散列表（hashmap），对于{key:value}键值对，key表示石头的位置，value是step的集合，step表示青蛙上次跳动到这里时用的步数（可以从多个点跳到这）
        // 每块石头，已知跳到这里用了step步，那么就看从该位置跳step-1、step和step+1能不能跳到新的石头，如果可以，在新的石头的value处加入这个步数
        public boolean canCross(int[] stones) {
            // 输入已经默认了石子的数量 ≥ 2 且 < 1100；第一个石子的位置永远是0，所以不需要做corner判断

            // 石头位置 -> 上次跳到这个步数集合
            Map<Integer, HashSet<Integer>> dp = new HashMap<>();
            for (int i = 0; i < stones.length; i++) {
                dp.put(stones[i], new HashSet<Integer>());
            }
            dp.get(stones[0]).add(0); // 一开始在第一块石头上，上次的步数应该为0

            for (int i = 0; i < stones.length - 1; i++) {
                int stone = stones[i];
                for (int step : dp.get(stone)) {
                    // step-1
                    if (dp.containsKey(stone + step - 1) && step > 1) {
                        dp.get(stone + step - 1).add(step - 1);
                    }
                    // step
                    if (dp.containsKey(stone + step)) {
                        dp.get(stone + step).add(step);
                    }
                    // step+1
                    if (dp.containsKey(stone + step + 1)) {
                        dp.get(stone + step + 1).add(step + 1);
                    }
                }
            }

            return dp.get(stones[stones.length - 1]).size() > 0; // 最后一个石头的step是否有数
        }
    }

}
