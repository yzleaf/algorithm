package twopointer;

import java.util.*;

public class Test {

    public static void main(String[] args) {

//        // Q1
//        int[] test = new int[]{1,2,3,4,5};
//        Solution solution = new Solution();
//        int[] res = solution.twoSum(test,5);
//
////        for (int i = 0; i<res.length; i++) {
////            System.out.println(res[i]);
////        }
//
//        // Q2
//        boolean resSquare = solution.squareSum(6);
////        System.out.println(resSquare);
//
//        // Q3
//        String testVowels = "leetcode";
//        String resVowels = solution.reverseVowels(testVowels);
////        System.out.println(resVowels);
//
//        // Q4
//        String palindrome = "abcdeeba";
//        boolean resPal = solution.validPalindrome(palindrome);
////        System.out.println(resPal);
//
//        // Q5
//        int[] s1 = new int[]{1,4,5,6,0,0,0};
//        int[] s2 = new int[]{2,3,8};
//        int[] resMerge = solution.merge(s1, 4, s2, 3);
////        for (int i = 0; i<resMerge.length; i++) {
////            System.out.println(resMerge[i]);
////        }
//
//        // Q7
//        String s7 = "abpcplea";
//        List<String> d = Arrays.asList("ale","apple","monkey","plea");
//        String s7Res = solution.findLongestWord(s7, d);
//        System.out.println(s7Res);
        HashMap<Integer, Integer> hash = new HashMap<>();
        hash.put(0,1);
        System.out.println(hash.get(0));
        System.out.println(hash.get(2));
    }
}
