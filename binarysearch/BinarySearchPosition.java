package binarysearch;

public class BinarySearchPosition {

    // 1. find bad version
    public static class SVNRepo {
        public static boolean isBadVersion(int k) {
            return false;
        }
    }

    /**
     * @param n: An integers.
     * @return An integer which is the first bad version.
     */
    public int findFirstBadVersion(int n) {
        int start = 1, end = n;
        while (start + 1 < end) {
            int mid = start + (end - start) / 2;
            if (SVNRepo.isBadVersion(mid)) {
                end = mid;
            } else {
                start = mid;
            }
        }

        if (SVNRepo.isBadVersion(start)) {
            return start;
        }

        return end;
    }


    // 2. Search in a big sorted array
    public static class ArrayReader {
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
        if (reader.get(start) == target) {
            return end;
        }

        return -1;
    }


    // 3. Find Minimum in Rotated Sorted Array
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
            if (nums[mid] > nums[end]) {
                start = mid;
            } else {
                end = mid;
            }
        }

        return Math.min(nums[start], nums[end]);
    }
}
