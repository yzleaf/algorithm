package twopointers;

import java.util.*;

public class SameDirectionPointers {
    // 1. Window Sum
    // 给你一个大小为n的整型数组和一个大小为k的滑动窗口，将滑动窗口从头移到尾
    // 输出从开始到结束每一个时刻滑动窗口内的数的和
    public class WinSumSolution {
        public int[] winSum(int[] nums, int k) {
            // 先计算出前 k 个元素之和 然后循环遍历数组 每次减去头部元素加上尾部元素
            if (nums == null || nums.length < k || k <= 0) {
                return new int[0];
            }

            int[] sums = new int[nums.length - k + 1];
            for (int i = 0; i < k; i++) { // 第一个前k个数的前缀和
                sums[0] += nums[i];
            }

            for (int i = 1; i < sums.length; i++) {
                // 减去头部元素，加上尾部元素
                sums[i] = sums[i-1] - nums[i-1] + nums[i+k-1];
            }

            return sums;

            // 两个指针，一个指向新数组sums，一个指向原数组nums
        }
    }

    // 2. Move Zeroes
    // 283
    // 将0移动到数组的最后面，非零元素保持原数组的顺序
    public class MoveZeroesSolution {
        // 使用两个指针，left为新数组的指针，right为原数组的指针
        // 原数组指针向后扫，遇到非0的数就赋值给新数组的指针位置，并将新数组指针向后移动
        public void moveZeroes(int[] nums) {
            int left = 0, right = 0;
            while (right < nums.length) { // 只复制非0的数，复制结束后剩下的就是0
                if (nums[right] != 0) {
                    nums[left] = nums[right];
                    left++;
                }
                right++;
            }

            // 若新数组指针还未指向尾部，将剩余数组赋值为0
            while (left < nums.length) {
                nums[left] = 0;
                left++;
            }
        }
    }

    // 3. Remove Duplicate Numbers in Array
    // 26
    // 在原数组上去除重复的元素
    // 返回去除重复元素之后的数组（放在开头，顺序无所谓）和元素个数
    public class RemoveDuplicateSolution {
        // 方法1 Hash O(n) time, O(n) space
        public int deduplication(int[] nums) {
            if (nums.length == 0) {
                return 0;
            }

            Set<Integer> hash = new HashSet<>();
            for (int i = 0; i < nums.length; i++) {
                hash.add(nums[i]);
            }

            int resultNumber = 0;
            for (Integer num : hash) { // hash表自动合并后，把hash表里的元素复制出来
                nums[resultNumber] = num;
                resultNumber++;
            }

            return resultNumber;
        }

        // 方法2 O(nlogn) time, O(1) extra space
        public int deduplication2(int[] nums) {
            if (nums.length == 0) {
                return 0;
            }

            Arrays.sort(nums);

            // 把不相等的数全部放到nums数组的最前面
            int newIndex = 0;
            for (int i = 0; i < nums.length; i++) {
                if (nums[i] != nums[newIndex]) { // newIndex是新放入数组的数，当前i是否与之相等（重复）
                    newIndex++;
                    nums[newIndex] = nums[i];
                }
            }
            return newIndex + 1; // 个数
        }
    }

    // 3. Longest Substring Without Repeating Characters
    // 返回最长substring，没有重复字母
    public class LongestSubStringSolution {
        public int lengthOfLongestSubstring(String s) {
            if (s == null || s.length() == 0) {
                return 0;
            }

            int left = 0, right = 1;
            Set<Character> letter = new HashSet<>();
            letter.add(s.charAt(left));

            int res = 1;
            int currMaxLen = 1;

            while (left <= right && right < s.length()) {
                if (letter.contains(s.charAt(right))) {
                    // 右指针所指新的数据 之前出现过
                    // left指针右移，之前的数据减少
                    letter.remove(s.charAt(left));
                    left ++;
                    currMaxLen --;
                } else {
                    // 右指针所指新的数据 之前出现过
                    // right指针右移，数据增加
                    letter.add(s.charAt(right));
                    right ++;
                    currMaxLen ++;
                }
                res = Math.max(res, currMaxLen);
            }
            return res;
        }
    }

    // 76. Minimum Window Substring
    // 求s串最小sub array，要包含t串所有字符
    public class MinWindowSolution {
        public String minWindow(String s, String t) {

            if (s == null || s.length() == 0 || t == null || t.length() == 0){
                return "";
            }

            // ASCII表总长128
            int[] need = new int[128];
            // 需要这些字母的个数
            for (int i = 0; i < t.length(); i ++) {
                need[t.charAt(i)] ++;
            }

            // 左右指针
            int left = 0, right = 0;
            // size记录窗口大小，count是需求的字符个数，start是最小覆盖串开始的index
            int size = Integer.MAX_VALUE, count = t.length(), start = 0;
            // count为0的时候说明s正好包含了所有需要的字符

            // 遍历原始所有字符串
            while (right < s.length()) {
                char c = s.charAt(right);
                if (need[c] > 0) { // 需要字符c，如果不需要的话这个need[c]会变为负值，就不用进入count--了
                    count --;
                }
                need[c] --; // 当前字符加入窗口

                if (count == 0) { // s的窗口中包含了所有需要的字符。在这个嵌套中不断缩小左指针
                    while (left < right && need[s.charAt(left)] < 0) { // 不需要的字符，释放，不断缩小窗口
                        need[s.charAt(left)] ++;
                        left ++;
                    }

                    if (right - left + 1 < size) {
                        size = right - left + 1;
                        start = left;
                    }

                    // left向右移动，现有窗口下一次就不满足了，需要重新开始
                    need[s.charAt(left)] ++;
                    left ++;
                    count ++;
                }
                right ++;
            }
            return size == Integer.MAX_VALUE ? "" : s.substring(start, start + size);
        }
    }
}
