package twopointer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class Solution {

    public Solution() {
    }

    // Q1/167: input array is sorted ascend, let two numbers sum -> target
    public int[] twoSum(int[] numbers, int target) {
        if (numbers == null) {
            return null;
        }

        // two pointer -> larger and smaller number
        // if sum > target, let larger number decrease
        // if sum < target, let smaller number increase
        int i = 0;
        int j = numbers.length - 1;
        while (i < j) {
            int sum = numbers[i] + numbers[j];

            if (sum == target) {
                return new int[]{i + 1, j + 1};
            }
            else if (sum < target) {
                i ++;
            }
            else {
                j --;
            }
        }
        return null;
    }

    // Q2/633: whether a number is the square sum of two number
    public boolean squareSum(int target) {
        if (target < 0) {
            return false;
        }
        int i = 0;
        int j = (int)Math.sqrt(target);
        while (i <= j) {
            int powSum = i * i + j * j;

            if (powSum == target) {
                return true;
            }
            else if (powSum > target) {
                j --;
            }
            else {
                i ++;
            }
        }
        return false;
    }



    // Q3/345 reverse the vowels in a string
    private final static HashSet<Character> vowels = new HashSet<>(
            Arrays.asList('a','e','i','o','u','A','E','I','O','U')
    );
    public String reverseVowels(String s) {
        if (s == null) {
            return null;
        }
        int i = 0;
        int j = s.length() - 1;
        char[] result = new char[s.length()]; // new array

        while (i <= j) {
            char ci = s.charAt(i);
            char cj = s.charAt(j);
            if (!vowels.contains(ci)) { // find the left vowel first
                result[i++] = ci;
            }
            else if (!vowels.contains(cj)) { // find the right vowel secondly
                result[j--] = cj;
            }
            else { // contain vowel, reverse
                result[i++] = cj;
                result[j--] = ci;
            }
        }
        return new String(result);
    }

    // Q4/680 delete a char, whether it is a Palindrome
    private boolean isPalindrome(String s, int i, int j) {
        while (i < j) {
            if (s.charAt(i++) != s.charAt(j--)) {
                return false;
            }
        }
        return true;
    }
    public boolean validPalindrome(String s) {
        for (int i = 0, j = s.length() - 1; i < j; i++,j--) {
            if (s.charAt(i) != s.charAt(j)) {
                // delete left char or right char
                return isPalindrome(s, i, j-1) || isPalindrome(s, i+1, j);
            }
        }
        return true;
    }

    // Q5/88 Merge 2 sorted array to nums1 array
    public int[] merge(int[] nums1, int m, int[] nums2, int n) {
        int index1 = m - 1;
        int index2 = n - 1;
        int indexMerge = m + n - 1;

        // iterate from the end to the start
        while (index1 >= 0 || index2 >= 0) {
            if (index1 < 0) {
                nums1[indexMerge--] = nums2[index2--];
            }
            else if (index2 < 0) {
                nums1[indexMerge--] = nums1[index1--];
            }
            else if (nums1[index1] > nums2[index2]) {
                nums1[indexMerge--] = nums1[index1--];
            }
            else { // nums1 < nums2
                nums1[indexMerge--] = nums2[index2--];
            }
        }

        return nums1;
    }

    // Q6/141 whether there is a cycle in the linked list
    public class ListNode
    {
        int val;
        ListNode next;
        public ListNode(int x){
            val = x;
            next = null;
        }
    }
    public boolean hasCycle(ListNode head) {
        // 2 pointer, first pointer: 1 step, second pointer: 2 step
        // if there is a cycle, the 2 pointer will meet
        if (head == null) {
            return false;
        }
        ListNode l1 = head;
        ListNode l2 = head.next;
        while (l1 != null && l2 != null && l2.next != null) {
            if (l1 == l2) {
                return true;
            }
            l1 = l1.next;
            l2 = l2.next.next;
        }
        return false;
    }

    // Q7/524 delete some characters, form the longest target string
    private boolean isSubstr(String s, String target) {
        // whether target is the substring
        int i = 0, j = 0;
        while (i < s.length() && j < target.length()) {
            if (s.charAt(i) == target.charAt(j)) {
                j++;
            }
            i++;
        }
        return j == target.length();
    }
    public String findLongestWord(String s, List<String> d) {
        String longestWord = "";
        for (String target : d) {
            int l1 = longestWord.length();
            int l2 = target.length();
            if (l1 > l2 || (l1 == l2 && longestWord.compareTo(target) < 0)) {
                continue;
            }
            if (isSubstr(s, target)) {
                longestWord = target;
            }
        }
        return longestWord;
    }


}
