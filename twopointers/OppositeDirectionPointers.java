package twopointers;

import java.util.*;

public class OppositeDirectionPointers {
    // 1. Valid Palindrome
    // 125
    // 给定一个字符串，判断其是否为一个回文串。只考虑字母和数字
    // 忽略大小写，所有符号
    public class PalindromeSolution {
        // 空数组是回文串
        public boolean isPalindrome(String s) {
            if (s == null || s.length() == 0) {
                return true;
            }
            int start = 0, end = s.length() - 1;

            while (start < end) {
                // 每次都会先跳过符号到字母/数字
                while (start < s.length() && !Character.isLetterOrDigit(s.charAt(start))) {
                    start++;
                }
                if (start == s.length()) { // 如果全是符号，当做回文串
                    return true;
                }

                while (end >= 0 && !Character.isLetterOrDigit(s.charAt(end))) {
                    end--;
                }

                // 将字符串中的字母全部转换为小写，非字母不受影响
                if (Character.toLowerCase(s.charAt(start)) != Character.toLowerCase(s.charAt(end))) {
                    return false;
                } else { // 相等，继续向中间靠拢
                    start++;
                    end--;
                }
            }
            return true;
        }
    }

    // 2. Rotate String
    // 796
    // 给定一个字符串（以字符数组的形式给出）和一个偏移量，根据偏移量原地旋转字符串(从左向右旋转)。
    // 原地旋转（O(1)空间复杂度，所以用翻转三次的方法）
    // 输入:  str="abcdefg", offset = 3
    // 输出:  str="efgabcd"
    public class RotateStringSolution {
        public void rotateString(char[] str, int offset) {
            if (str == null || str.length == 0) {
                return;
            }

            offset = offset % str.length;
            reverse(str, 0, str.length - 1);
            reverse(str, 0, offset - 1); // 前半边翻转
            reverse(str, offset, str.length - 1); // 后半边翻转
        }
        private void reverse(char[] str, int start, int end) {
            for (int i = start, j = end; i < j; i++, j--) {
                char temp = str[i];
                str[i] = str[j];
                str[j] = temp;
            }
        }
    }
    // string->char : s.toCharArray
    // char->string : String.valueOf(str)

    // 3. Recover Rotated Sorted Array
    // 给定一个旋转排序数组，在原地恢复其排序。（升序）
    // 使用O(1)的额外空间和O(n)时间复杂度
    public class RecoverRotatedSortedArraySolution {

        public void recoverRotatedSortedArray(ArrayList<Integer> nums) {
            // 找到第一个比后面的数大的数
            // 以[4,5,1,2,3]为例，找到5，翻转[4,5]得到[5,4]，翻转[1,2,3]得到[3,2,1]
            // 最后翻转[5,4,3,2,1] 得到[1,2,3,4,5]

            for (int index = 0; index < nums.size() - 1; index++) {
                if (nums.get(index) > nums.get(index + 1)) {
                    reverse(nums, 0, index);
                    reverse(nums, index + 1, nums.size() - 1);
                    reverse(nums, 0, nums.size() - 1);
                    return;
                }
            }
        }
        private void reverse(ArrayList<Integer> nums, int start, int end) {
            for (int i = start, j = end; i < j; i++, j--) {
                int temp = nums.get(i);
                nums.set(i, nums.get(j));
                nums.set(j, temp);
            }
        }
    }

}
