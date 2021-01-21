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
    // 结果中的每个元素必须是唯一的。
    // 结果需要为升序。
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
            int[] temp = new int[nums1.length]; // 交集，小于任一个数组长度，无所谓取哪个
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
            }

            return findKth(A, 0, B, 0, n/2 + 1); // 奇数长度，中位数为第n/2 + 1个数
        }
        // find kth number of two sorted array
        private int findKth(int[] A, int startOfA, int[] B, int startOfB, int k) {
            if (startOfA >= A.length) {
                return B[startOfB + k - 1];
            }
            if (startOfB >= B.length) {
                return A[startOfA + k - 1];
            }

            if (k == 1) {
                return Math.min(A[startOfA], B[startOfB]);
            }

            int halfKthA = startOfA + k/2 - 1 < A.length ?
                           A[startOfA + k/2 - 1] :
                           Integer.MAX_VALUE;
            int halfKthB = startOfB + k/2 - 1 < B.length ?
                           B[startOfB + k/2 - 1] :
                           Integer.MAX_VALUE;

            if (halfKthA < halfKthB) {
                return findKth(A, startOfA + k/2, B, startOfB, k - k/2);
            } else {
                return findKth(A, startOfA, B, startOfB + k/2, k - k/2);
            }
        }
    }
}
