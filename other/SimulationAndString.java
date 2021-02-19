package other;

import java.util.*;

public class SimulationAndString {
    // 1. Moving Average from Data Stream
    // 给出一串整数流和窗口大小，计算滑动窗口中所有整数的平均值
    // MovingAverage m = new MovingAverage(3);
    // m.next(1) = 1 // 返回 1.00000
    // m.next(10) = (1 + 10) / 2 // 返回 5.50000
    // m.next(3) = (1 + 10 + 3) / 3 // 返回 4.66667
    // m.next(5) = (10 + 3 + 5) / 3 // 返回 6.00000
    public class MovingAverageSolution {
        // 前缀和+滚动数组
        int id, size;
        double[] sum;
        MovingAverageSolution(int s) {
            id = 0;
            size = s;
            sum = new double[size + 1];
        }

        int mod(int x) {
            return x % (size + 1);
        }

        public double next(int val) {
            id ++;
            sum[mod(id)] = sum[mod(id-1)] + val; // 前缀和
            if (id - size > 0) {
                return (sum[mod(id)] - sum[mod(id-size)]) / size;
            } else { // 不足size个，直接返回
                return sum[mod(id)] / size;
            }
        }
    }

    // 2. 区间求和 I · Interval Sum
    // 给定一个整数数组（下标由 0 到 n-1，其中 n 表示数组的规模），以及一个查询列表。
    // 每一个查询列表有两个整数 [start, end] 。 对于每个查询，计算出数组中从下标 start 到 end 之间的数的总和，并返回在结果列表中。
    public class IntervalSumSolution {
        public class Interval {
            int start, end;
            Interval(int start, int end) {
                this.start = start;
                this.end = end;
            }
        }
        public List<Long> intervalSum(int[] A, List<Interval> queries) {
            List<Long> result = new ArrayList<>();
            if (A == null || A.length == 0) {
                return result;
            }

            Long[] sum = new Long[A.length];
            sum[0] = (long)A[0];
            for (int i = 0; i < A.length; i++) {
                sum[i] = sum[i-1] + A[i];
            }

            for (Interval intr : queries) {
                Long intrSum = A[intr.start] + sum[intr.end] - sum[intr.start];
                result.add(intrSum);
            }

            return result;
        }
    }

    // 3. 镜像数字 · mirror numbers
    // 一个镜像数字是指一个数字旋转180度以后和原来一样(倒着看)
    // 写下一个函数来判断是否数字串是镜像的。数字用字符串来表示。
    public class isStrobogrammaticSolution {
        // 注意：需要确认2和5的写法是否构成镜像数字
        public boolean isStrobogrammatic(String num) {
            int[] map = new int[256];
            map['0'] = '0';
            map['1'] = '1';
            map['6'] = '9';
            map['8'] = '8';
            map['9'] = '6';

            for (int i = 0; i < num.length(); i++) {
                int j = num.length() - i - 1;
                if (map[num.charAt(i)] != num.charAt(j)) {
                    return false;
                }
            }
            return true;
        }
    }

    // 4. 一次编辑距离 · edit distance ii
    // 给定两个字符串 S 和 T, 判断T是否可以通过对S做刚好一次编辑得到。
    // 以下都是一次编辑
    //     在S的任意位置插入一个字符
    //     删除S中的任意一个字符
    //     将S中的任意字符替换成其他字符
    public class IsOneEditDistanceSolution {
        public boolean isOneEditDistance(String s, String t) {
            if (s.length() > t.length()) { // 交换数组使得短的长度永远在前面
                return isOneEditDistance(t, s);
            }

            int diff = t.length() - s.length();

            if (diff > 1) {
                return false;
            }

            if (diff == 0) { // 长度相等，检查是否只有一次变换
                int cnt = 0;
                for (int i = 0; i < s.length(); i++) {
                    if (s.charAt(i) != t.charAt(i)) {
                        cnt++;
                    }
                }
                return (cnt == 1);
            }

            if (diff == 1) { // 长度差1，s增加一位，其他都相等
                for (int i = 0; i < s.length(); i++) {
                    if (s.charAt(i) != t.charAt(i)) {
                        return (s.substring(i).equals(t.substring(i + 1)));
                    }
                }
            }

            return true;
        }
        // 方法2
        public boolean isOneEditDistance2(String s, String t) {
            if (s.length() > t.length()) { // 交换数组使得短的长度永远在前面
                return isOneEditDistance(t, s);
            }

            int diff = t.length() - s.length();

            if (diff > 1) {
                return false;
            }

            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) != t.charAt(i)) {
                    if (diff == 0) { // 长度相等，只能有一位不等
                        return s.substring(i + 1).equals(t.substring(i + 1));
                    } else { // ==1 长度不等，添加一位，后面都相等
                        return s.substring(i).equals(t.substring(i + 1));
                    }
                }
            }

            return s.length() != t.length(); // s已遍历的所有都相等，t比s多出最后一位才可
        }
    }


}
