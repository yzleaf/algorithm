package binarysearch;

import java.util.*;

public class BinarySearchPosition {

    // 1. find bad version
    // 278
    public class FirstBadVersionSolution {

        public boolean isBadVersion(int k) {
            return false;
        }

        /**
         * @param n: An integers.
         * @return An integer which is the first bad version.
         */
        public int findFirstBadVersion(int n) {
            int start = 1, end = n;
            while (start + 1 < end) {
                int mid = start + (end - start) / 2;
                if (isBadVersion(mid)) { // 先记录，不确定是不是第一个
                    end = mid;
                } else {
                    start = mid;
                }
            }

            if (isBadVersion(start)) {
                return start;
            }

            return end;
        }
    }

    // 2. Search in a big sorted array
    // 给一个按照升序排序的非负整数数组。这个数组很大以至于你只能通过固定的接口 ArrayReader.get(k) 来访问第k个数
    // 找到给出的整数target第一次出现的位置
    public class SearchBigSortedArraySolution {
        public class ArrayReader {
            public int get(int index) {
                // return the number on given index,
                // return 2147483647 if the index is invalid.
                return 1;
            }
        };

        /**
         * @param reader: An instance of ArrayReader.
         * @param target: An integer
         * @return An integer which is the first index of target.
         */
        public int searchBigSortedArray(ArrayReader reader, int target) {
            int start = 0, end = 1;
            while (reader.get(end) < target) {
                end <<= 1; // 不断*2
            }

            while (start + 1 < end) {
                int mid = start + (end - start) / 2;
                if (reader.get(mid) == target) {
                    end = mid;
                } else if (reader.get(mid) < target) {
                    start = mid;
                } else { // > target
                    end = mid;
                }
            }

            if (reader.get(start) == target) {
                return start;
            }
            if (reader.get(end) == target) {
                return end;
            }

            return -1;
        }
    }

    // 3. Find Minimum in Rotated Sorted Array
    // 153
    public class FindMinSolution {
        /**
         * @param nums: a rotated sorted array
         * @return the minimum number in the array
         */
        public int findMin(int[] nums) {
            if (nums == null || nums.length == 0) {
                return -1;
            }

            int start = 0, end = nums.length - 1;
            while (start + 1 < end) {
                int mid = start + (end - start) / 2;
                // 如果找的是数组内的最大值，则和nums[start]比较
                if (nums[mid] > nums[end]) { // 不用[start]比较，因为如果数组不rotated，start=mid会被覆盖，找不到start这个最小值
                    start = mid;
                } else {
                    end = mid;
                }
            }

            return Math.min(nums[start], nums[end]);
        }
    }

    // 4. Maximum Number in Mountain Sequence
    // 在先增后减的序列中找最大值
    public class MountainSequenceSolution {
        /**
         * @param nums a mountain sequence which increase firstly and then decrease
         * @return then mountain top
         */
        public int mountainSequence(int[] nums) {
            if (nums == null || nums.length == 0) {
                return -1;
            }

            int start = 0, end = nums.length - 1;
            while (start + 1 < end) {
                int mid = start + (end - start) / 2;
                // 比较元素和右相邻元素
                if (nums[mid] > nums[mid + 1]) {
                    end = mid;
                } else { // nums[mid] <= nums[mid+1]
                    start = mid;
                }
            }

            return Math.max(nums[start],nums[end]);
        }
    }

    // 5. Find Peak Element
    // 162
    // 假定P是峰值的位置则满足A[P] > A[P-1]且A[P] > A[P+1]
    // 返回数组中任意一个峰值的位置
    public class FindPeakSolution {
        // 数组满足条件 A[0] < A[1] && A[n-2] > A[n-1] （一定有峰值）条件1
        public int findPeak(int[] A) {
            if (A.length == 1) { // 只有一个数的时候，本身是peak
                return 0;
            }

            // int start = 1, end = A.length - 1 - 1; 在条件1的情况下可以这样写
            int start = 0, end = A.length - 1;
            while (start + 1 < end) {
                int mid = start + (end - start) / 2;
                if (A[mid] > A[mid - 1] && A[mid] > A[mid + 1]) { // A[mid]>A[mid-1] && A[mid]>A[mid+1] -> Peak
                    return mid;
                } else if (A[mid] < A[mid - 1]) { // 下降沿
                    end = mid;
                } else { // A[mid] < A[mid + 1] 上升沿
                    start = mid;
                }
            }

            // 前面直接return mid，到这一步只有可能是边缘的情况
            if (A[start] > A[end]) {
                return start;
            } else {
                return end;
            }
        }
    }

    // 6. Search in Rotated Sorted Array
    // 33
    // 给定一个目标值进行搜索，如果在数组中找到目标值返回数组中的索引位置，否则返回-1
    // 没有重复元素，不存在start==mid的情况
    public class SearchRotatedSortedArraySolution {
        // 方法1 先找到最小点，再来一次二分
        // 方法2 直接二分分类讨论
        public int search(int[] A, int target) {

            if (A == null || A.length == 0) {
                return -1;
            }

            int start = 0, end = A.length - 1;
            while (start + 1 < end) {
                int mid = start + (end - start) / 2;
                if (A[mid] == target) {
                    return mid;
                }

                if (A[start] < A[mid]) { // mid在left part
                    if (A[start] <= target && target <= A[mid]) {
                        end = mid;
                    } else { // 在left part, start不可能大于target
                        // 只有target > A[mid]
                        start = mid;
                    }
                } else { // mid在right part
                    if (A[mid] <= target && target <= A[end]) {
                        start = mid;
                    } else { // 在right part, end不可能小于target
                        // 只有target < A[mid]
                        end = mid;
                    }
                }
            } // while

            if (A[start] == target) {
                return start;
            }
            if (A[end] == target) {
                return end;
            }

            return -1;
        }
    }

    // 81. 搜索旋转排序数组 II
    // 判断给定的目标值是否存在于数组中。如果 nums 中存在这个目标值 target ，则返回 true ，否则返回 false
    // 有重复元素，可能存在start==mid的情况
    public class SearchRotatedSortedArraySolution2 {
        public boolean search(int[] nums, int target) {
            if (nums == null || nums.length == 0) {
                return false;
            }

            int start = 0, end = nums.length - 1;
            while (start + 1 < end) {
                int mid = start + (end - start) / 2;
                if (nums[mid] == target) {
                    return true;
                }

                // start == mid
                // 10111和11101这种。此种情况下 nums[start] == nums[mid]，分不清到底是前面有序还是后面有序
                // 此时 start++ 即可。相当于去掉一个重复的干扰项。
                if (nums[start] == nums[mid]) {
                    start ++;
                    continue;
                }

                if (nums[start] < nums[mid]) { // left part
                    if (nums[start] <= target && target <= nums[mid]) {
                        end = mid;
                    } else {
                        start = mid;
                    }
                } else { // >= right part
                    if (nums[mid] <= target && target <= nums[end]) {
                        start = mid;
                    } else {
                        end = mid;
                    }
                }
            }

            // 跳出循环后还是得做这个判断
            if (nums[start] == target || nums[end] == target) {
                return true;
            } else {
                return false;
            }
        }
    }
    
    // 658. Find K Closest Elements
    // 给定一个排序好的数组 arr ，两个整数 k 和 x ，从数组中找到最靠近 x（两数之差最小）的 k 个数。返回的结果必须要是按升序排好的
    public class FindCloseSolution {
        // 方法1
        // 二分找到小于x的最接近的数，双指针左右扩展区间成k个
        public List<Integer> findClosestElements(int[] arr, int k, int x) {
            List<Integer> result = new ArrayList();
            int left = findLowerClosest(arr, x);
            int right = left + 1;

            // left和right开始向两边扩展
            // 注意区间边界，left和right过了边界就要看另一个方向
            while (right - left <= k) { // 这个小于等于k一定要画图，考虑k=1,k=2的情况
            // 或者直接用 for (int i = 0; i < k ; i++) 直接保证了k个数
                if (left == -1) {
                    right ++;
                    continue;
                }
                if (right == arr.length) {
                    left --;
                    continue;
                }
                if (x - arr[left] <= arr[right] - x) { // 距离相等时，index小的保留
                    left --;
                } else {
                    right ++;
                }
            }

            for (int i = left + 1; i < right; i ++) {
                result.add(arr[i]);
            }

            return result;
        }
        private int findLowerClosest(int[] arr, int x) {
            int start = 0, end = arr.length - 1;
            // 找到第一个小于且最接近x的数
            while (start + 1 < end) {
                int mid = start + (end - start) / 2;
                if (arr[mid] < x) {
                    start = mid;
                } else {
                    end = mid;
                }
            }
            if (arr[end] < x) {
                return end;
            }
            if (arr[start] < x) {
                return start;
            }
            return -1; // 找不到，数组全部大于x，返回的是-1，这样后面的right+1就会是第一个数index=0
        }

        // 方法2 找到左边界，然后返回mid + k个数
        // arr[mid]到arr[mid+k]之间有k+1个数，所以我们需要比较边界两个数哪个更接近x
        public List<Integer> findClosestElements2(int[] arr, int k, int x) {
            List<Integer> result = new ArrayList();

            // mid 到 mid+k 有 k+1 个元素
            // mid更接近的话，mid+k及其往右的元素都不满足
            // mid+k更接近的话，mid及其往左的元素都不满足
            int start = 0, end = arr.length - k;

            while (start < end) {
                int mid = start + (end - start) / 2;
                // 不用绝对值是因为[1,1,2,2,2,2,2,3,3], 3, 3为测试用例
                // 如果在四个2的区间，会出错。规避duplicate的情况
                // Math.abs(arr[mid] - x) <= Math.abs(arr[mid + k] - x)
                if (x - arr[mid] <= arr[mid + k] - x) { // =时候，取index小的
                    end = mid;
                } else {
                    start = mid + 1;
                }
            }


            for (int i = start; i < start + k; i++) {
                result.add(arr[i]);
            }
            return result;
        }
    }
}
