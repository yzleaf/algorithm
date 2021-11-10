package other;

import java.util.*;

public class IntervalProblems {
    // 1. missing interval
    // 163
    // 给定一个排序整数数组，其中元素的取值范围为[lower，upper] (包括边界)
    // 返回其缺少的范围。
    // given [0, 1, 3, 50, 75], lower = 0 and upper = 99
    // return ["2", "4->49", "51->74", "76->99"].
    public class FindMissingRangesSolution {
        public List<String> findMissingRanges(int[] nums, int lower, int upper) {
            List<String> res = new ArrayList<>();

            if (nums.length == 0) {
                addRange(res, lower, upper);
                return res;
            }

            addRange(res, lower, nums[0] - 1); // lower -> 第一个数

            for (int i = 1; i < nums.length; i++) { // 前一个值到当前值的空隙
                addRange(res, nums[i-1] + 1, nums[i] - 1);
            }

            addRange(res, nums[nums.length-1] + 1, upper); // 最后一个数 -> upper

            return res;
        }
        private void addRange(List<String> res, int start, int end) {
            if (start > end) {
                return;
            }
            if (start == end) { // 添加这个数本身
                res.add(start + "");
                return;
            }
            res.add(start + "->" + end);
        }
    }

    // 2. 合并区间 · Merge Intervals
    // 56
    // 给出若干闭合区间，合并所有重叠的部分
    // 输入:  [(1,3),(2,6),(8,10),(15,18)]
    // 输出: [(1,6),(8,10),(15,18)]
    public class Interval {
        int start, end;
        Interval(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }

    public class MergeIntervalSolution {

        // 将区间按起点从小到大排序，然后从左到右扫一遍找最远的右端点。 交错或包含的区间就合并。
        public List<Interval> merge(List<Interval> intervals) {
            List<Interval> result = new ArrayList<>();
            if (intervals == null) {
                return result;
            }

            // 按Interval的start从小到大排列
            intervals.sort((a, b) -> a.start - b.start); // Collections.sort(intervals, (a,b) -> a.start - b.start);

            Interval last = null;
            for (Interval item : intervals) {
                if (last == null || last.end < item.start) { // 没有交集直接添加当前区间
                    result.add(item);
                    last = item; // last和item指向同一个位置
                } else { // last.end >= item.start -> 有交集
                    // 修改已经添加到list中的上一个end
                    last.end = Math.max(last.end, item.end);

                    // 也可以直接result.get(result.size() - 1)获取元素来修改值
                }
            }

            return result;
        }
    }

    // 3. 插入区间 · Insert Interval
    // 57
    // 给出一个无重叠的按照区间起始端点排序的区间列表。
    // 在列表中插入一个新的区间，你要确保列表中的区间仍然有序且不重叠（如果有必要的话，可以合并区间）
    // 输入: (2, 5) into [(1,2), (5,9)]
    // 输出: [(1,9)]
    public class InsertIntervalSolution {
        // 在List找到满足start递增的合适位置插入这个newInterval，再按照上一题的方法merge
        public List<Interval> insert(List<Interval> intervals, Interval newInterval) {
            List<Interval> result = new ArrayList<>();

            int index = 0;
            while (index < intervals.size() && intervals.get(index).start < newInterval.start) {
                index ++;
            }
            // 跳出循环是第一个大于等于newInterval的位置，把这个位置的所有数据都往后移
            intervals.add(index, newInterval); // 在index位置添加newInterval

            Interval last = null;
            for (Interval item : intervals) {
                if (last == null || last.end < item.start) {
                    result.add(item);
                    last = item;
                } else {
                    last.end = Math.max(last.end, item.end);
                }
            }

            return result;
        }
        // 方法2，考虑到原始区间【不重叠】且【有序】
        public int[][] insert(int[][] intervals, int[] newInterval) {
            List<int[]> res = new ArrayList<>();
            int len = intervals.length;
            int i = 0;
            // 1. 所有区间都在newInterval的左边
            while (i < len && intervals[i][1] < newInterval[0]) {
                res.add(intervals[i]);
                i ++;
            }

            // 2. 和newInterval有重叠
            // 此时[i][1]已经 >= newInterval[0]了
            while (i < len && intervals[i][0] <= newInterval[1]) { // 保证有重叠
                newInterval[0] = Math.min(intervals[i][0], newInterval[0]);
                newInterval[1] = Math.max(intervals[i][1], newInterval[1]);
                i ++;
            }
            res.add(newInterval); // 更新重叠的区间，最后只添加一次！！！

            // 3. 所有区间都在newInterval的右边
            while (i < len && intervals[i][0] > newInterval[1]) {
                res.add(intervals[i]);
                i ++;
            }

            return res.toArray(new int[0][0]);
        }
    }

    // 986. Interval List Intersections
    // 两个数组，求相交区间
    // Input: firstList = [[0,2],[5,10],[13,23],[24,25]], secondList = [[1,5],[8,12],[15,24],[25,26]]
    // Output: [[1,2],[5,5],[8,10],[15,23],[24,24],[25,25]]
    public class IntervalIntersectionSolution {
        // 双指针指向两个数组
        // 每次判断相交区间后，每次比较抛去endpoint靠前的数组
        public int[][] intervalIntersection(int[][] firstList, int[][] secondList) {

            List<int[]> res = new ArrayList<>();
            int i = 0, j = 0;
            while (i < firstList.length && j < secondList.length) {

                // 相交区间的左右端点
                int intersectLeft = Math.max(firstList[i][0], secondList[j][0]);
                int intersectRight = Math.min(firstList[i][1], secondList[j][1]);

                // 需要考虑相等，单独一个点
                if (intersectLeft <= intersectRight) {
                    res.add(new int[]{intersectLeft, intersectRight});
                }

                // endpoint靠前的先抛去
                if (firstList[i][1] < secondList[j][1]) {
                    i ++;
                } else {
                    j ++;
                }
            }

            return res.toArray(new int[res.size()][]);
        }
    }

    // 5. Longest Palindromic Substring
    // 返回最长的回文子串
    public class LongestPalindromeSolution {

        public String longestPalindrome(String s) {

            int start = 0, end = 0;
            // 每次都以当前点为中心，不断向两边走，看是否相等
            for (int i = 0; i < s.length(); i ++) {
                int lenFromCurr = lenFromCenter(s, i, i); // 当前点为中心
                int lenFromCurrAndNext = lenFromCenter(s, i, i+1); // 当前点和隔壁点为中心
                int len = Math.max(lenFromCurr, lenFromCurrAndNext);
                if (len > end - start) {
                    start = i - (len - 1) / 2;
                    end = i + len / 2;
                }
            }
            return s.substring(start, end + 1);
        }

        // 返回以当前中心点向两边走的回文串的长度
        private int lenFromCenter(String s, int centerLeft, int centerRight) {
            int left = centerLeft;
            int right = centerRight;
            while (left >= 0 && right <= s.length() - 1) {
                if (s.charAt(left) == s.charAt(right)) {
                    left --;
                    right ++;
                } else {
                    break;
                }
            }
            // 跳出循环的时候left和right都往两边多走了一步
            return right - left - 1; // 返回回文串总长度
        }
    }
}
