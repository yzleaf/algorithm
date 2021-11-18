package array;

import java.util.*;

public class Array {
    // 1. Merge Two Sorted Arrays 2
    public class MergeSortedArraySolution2 {
        public int[] mergeSortedArray(int[] A, int[] B) {
            if (A == null || B == null) {
                return null;
            }

            int[] result = new int[A.length + B.length];
            int i = 0, j = 0, index = 0;
            while (i < A.length && j < B.length) {
                if (A[i] < B[j]) {
                    result[index++] = A[i++];
                } else {
                    result[index++] = B[j++];
                }
            }

            while (i < A.length) {
                result[index++] = A[i++];
            }
            while (j < B.length) {
                result[index++] = B[j++];
            }

            return result;
        }
    }

    // 2. Merge Sorted Array
    // 合并到A数组里，A有足够的空间，没有别的额外空间
    // 输入：[1,2,5] 3 [3,4] 2
    // 输出：[1,2,3,4,5]
    public class MergeSortedArraySolution {
        // 从后往前合并，不会覆盖前面的
        public void mergeSortedArray(int[] A, int m, int[] B, int n) {
            int i = m - 1, j = n - 1, index = m + n - 1;
            while (i >= 0 && j >= 0) {
                if (A[i] > B[j]) {
                    A[index--] = A[i--];
                } else {
                    A[index--] = B[j--];
                }
            }

            while (i >= 0) {
                A[index--] = A[i--];
            }
            while (j >= 0) {
                A[index--] = B[j--];
            }
        }
    }

    // 3. Intersection of Two Arrays
    // 求两个数组的交集，结果中的每个元素必须是唯一的。
    // 结果需要为升序。
    // 输入: nums1 = [1, 2, 2, 1], nums2 = [2, 2],
    // 输出: [2]
    public class IntersectionSolution {
        // 方法1 hashset
        public int[] intersection1(int[] nums1, int[] nums2) {
            if (nums1 == null || nums2 == null) {
                return null;
            }

            Set<Integer> hash = new HashSet<>();
            for (int i = 0; i < nums1.length; i++) {
                hash.add(nums1[i]);
            }

            Set<Integer> resultHash = new HashSet<>();
            for (int i = 0; i < nums2.length; i++) {
                if (hash.contains(nums2[i])) { // 会自动检查!resultHash.contains(nums2[i])
                    resultHash.add(nums2[i]);
                }
            }

            int size = resultHash.size();
            int[] result = new int[size];
            int index = 0;
            for (Integer num : resultHash) {
                result[index++] = num;
            }

            return result;
        }

        // 方法2 sort & merge
        public int[] intersection2(int[] nums1, int[] nums2) {
            if (nums1 == null || nums2 == null) {
                return null;
            }
            Arrays.sort(nums1);
            Arrays.sort(nums2);

            int i = 0, j = 0, index = 0;
            int[] temp = new int[nums1.length]; // 放交集的数组，小于任一个数组长度，无所谓取哪个
            while (i < nums1.length && j < nums2.length) {
                if (nums1[i] == nums2[j]) {
                    if (index == 0 || temp[index-1] != nums1[i]) { // 去除重复点
                        temp[index] = nums1[i];
                    }
                    i++;
                    j++;
                    index++;
                } else if (nums1[i] < nums2[j]) {
                    i++;
                } else {
                    j++;
                }
            }

            int[] result = new int[index];
            for (int k = 0; k < index; k++) {
                result[k] = temp[k];
            }
            return result;
        }

        // 方法3 sort & binary search
        public int[] intersection3(int[] nums1, int[] nums2) {
            if (nums1 == null || nums2 == null) {
                return null;
            }

            Set<Integer> hash = new HashSet<>();
            // nums1排序后，二分查找每一个target（来源于nums2）
            Arrays.sort(nums1);
            for (int i = 0; i < nums2.length; i++) {
                if (hash.contains(nums2[i])) {
                    continue;
                }
                if (binarySearch(nums1, nums2[i])) {
                    hash.add(nums2[i]);
                }
            }

            int[] result = new int[hash.size()];
            int index = 0;
            for (Integer num : hash) {
                result[index++] = num;
            }

            return result;
        }
        private boolean binarySearch(int[] nums, int target) {
            if (nums == null || nums.length == 0) {
                return false;
            }
            int start = 0, end = nums.length - 1;
            while (start + 1 < end) {
                int mid = start + (end - start) / 2;
                if (nums[mid] == target) {
                    return true;
                } else if (nums[mid] < target) {
                    start = mid;
                } else {
                    end = mid;
                }
            }

            if (nums[start] == target) {
                return true;
            }
            if (nums[end] == target) {
                return true;
            }

            return false;
        }
    }

    // 4. Median of two Sorted Arrays
    // 两个排序的数组A和B分别含有m和n个数，找到两个排序数组的中位数，要求时间复杂度应为O(log (m+n))
    public class MedianSortedArraysSolution {
        public double findMedianSortedArrays(int[] A, int[] B) {
            int n = A.length + B.length;

            if (n % 2 == 0) { // 偶数长度，中位数为最中间的两个数的平均数
                return (
                        findKth(A, 0, B, 0, n/2) +
                        findKth(A, 0, B, 0, n/2 + 1)
                ) / 2.0;
            } else {
                return findKth(A, 0, B, 0, n/2 + 1); // 奇数长度，中位数为第n/2 + 1个数
            }


        }
        // find kth number of two sorted array
        // 第k个数，不是下标为k的数，所以后面返回的要-1
        private int findKth(int[] A, int startOfA, int[] B, int startOfB, int k) {
            if (startOfA >= A.length) {
                return B[startOfB + k - 1];
            }
            if (startOfB >= B.length) {
                return A[startOfA + k - 1];
            }

            if (k == 1) { // 第一个数->取最小的
                return Math.min(A[startOfA], B[startOfB]);
            }

            // 比较两个数组k/2的大小，看删除哪部分的数
            int halfKthA = startOfA + k/2 - 1 < A.length ?
                           A[startOfA + k/2 - 1] :
                           Integer.MAX_VALUE;
            int halfKthB = startOfB + k/2 - 1 < B.length ?
                           B[startOfB + k/2 - 1] :
                           Integer.MAX_VALUE;

            if (halfKthA < halfKthB) {
                return findKth(A, startOfA + k/2, B, startOfB, k - k/2); // A的靠右部分 B的靠左部分
            } else {
                return findKth(A, startOfA, B, startOfB + k/2, k - k/2); // A的靠左部分 B的靠右部分
            }
        }
    }

    // 41. First Missing Positive
    // 找到数组中缺失的最小正整数
    // 要求时间O(n) 空间O(1)
    class FirstMissingPositiveSolution {
        public int firstMissingPositive(int[] nums) {
            // 用原地hash表（数组模拟Hash）
            // 把每个数，存在对应（数值-1）的index上。最后遍历数组看哪个index缺少这个对应的数
            int n = nums.length;
            for (int i = 0; i < n; i ++) {
                // 用while是交换到当前i位置的数会继续交换下去
                // 在指定范围内，没有正确的位置才交换
                // 在正确位置的数不会继续进入while循环，所以整个事件复杂度还是O(n)
                while (nums[i] > 0 && nums[i] <= n && i != nums[i] - 1) {
                    if (nums[i] == nums[nums[i] - 1]) { // 避免两个交换的数相等，造成死循环
                        break;
                    }
                    swap(nums, i, nums[i] - 1);
                }
            }

            for (int i = 0; i < n; i ++) {
                if (nums[i] - 1 != i) {
                    return i + 1;
                }
            }

            return n + 1; // 数组中有所有的数，所以返回数组后面的一个数
        }
        private void swap(int[] nums, int i, int j) {
            int temp = nums[i];
            nums[i] = nums[j];
            nums[j] = temp;
        }
    }

    // 932. Beautiful Array
    // 判断数组是否是Beautiful array
    // For every 0 <= i < j < n, there is no index k with i < k < j where 2 * nums[k] == nums[i] + nums[j].
    public class BeautifulArraySolution {
        // A[]: Beautiful 可以保证 a * A[] + b也是beautiful （因为数组中每个元素都加减乘不影响原来的beautiful关系）
        // 两个数组合并，如果左边是奇数，右边是偶数，那么新的数组必然满足beautiful（因为2 * y != x + z，因为 奇+偶=奇）

        // 可以用HashMap来存结果，加速多次查找（似乎不是这题的必须）
        Map<Integer, int[]> hash;
        public int[] beautifulArray(int n) {
            hash = new HashMap<>();
            return constructBeautiful(n);
        }
        private int[] constructBeautiful(int n) {
            if (hash.containsKey(n)) {
                return hash.get(n);
            }

            int[] res = new int[n];
            if (n == 1) {
                res[0] = 1;
            } else {
                int t = 0;
                // left左边放奇数
                int[] left = constructBeautiful((n + 1)/ 2); // 奇数多一个，因为中间mid应该为奇
                for (int leftNum : left) {
                    res[t] = 2 * leftNum - 1;
                    t ++;
                }
                // right右边放偶数
                int[] right = constructBeautiful(n / 2);
                for (int rightNum : right) {
                    res[t] = 2 * rightNum;
                    t ++;
                }
            }

            hash.put(n, res);
            return res;
        }
    }
}
