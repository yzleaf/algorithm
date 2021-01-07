package sort;

import java.util.*;

public class Test {

    public static void main(String[] args) {

        Solution solution = new Solution();

        // Q1
        int[] q1 = new int[]{15,27,3,94,5};
        int res1 = solution.findKthLargestQuick(q1, 3);
        System.out.println(res1);
    }
}
