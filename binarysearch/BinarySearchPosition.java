package binarysearch;

import com.sun.xml.internal.fastinfoset.tools.XML_SAX_StAX_FI;

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
}
