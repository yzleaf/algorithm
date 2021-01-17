package binarysearch;

public class TemplateBinarySearch {

    // 二分法模板

    /**
     * @param nums: The integer array.
     * @param target: Target to find.
     * @return The 1st position of target. Position starts from 0.
     */

    public int binarySearch(int[] nums, int target) {
        if (nums == null || nums.length == 0) {
            return -1;
        }

        int start = 0, end = nums.length - 1;
        while (start + 1 < end) { // 某些二分案例中找夹缝的值，用此判断不会死循环
            int mid = start + (end - start) / 2; // 防止溢出
            if (nums[mid] == target) { // 先记录，但未必是1st的数；为了找到1st，尽量往左靠
                end = mid;
            } else if (nums[mid] < target) {
                start = mid;
            } else { // nums[mid] > target
                end = mid;
            }
        }

        if (nums[start] == target) { // 先找左边的
            return start;
        }
        if (nums[end] == target) {
            return end;
        }

        return -1;
    }

}
