package other;

import java.util.*;

public class IntervalProblems {
    // 1. missing interval
    // 给定一个排序整数数组，其中元素的取值范围为[lower，upper] (包括边界)
    // 返回其缺少的范围。
    // given [0, 1, 3, 50, 75], lower = 0 and upper = 99
    // return ["2", "4->49", "51->74", "76->99"].
    public class FindMissingRangesSolution {
        public List<String> findMissingRanges(int[] nums, int lower, int upper) {
            List<String> result = new ArrayList<>();

            if (nums.length == 0) {
                addRange(result, lower, upper);
            }

            addRange(result, lower, nums[0] - 1);

            for (int i = 1; i < nums.length; i++) { // 前一个值到当前值的空隙
                addRange(result, nums[i-1] + 1, nums[i] - 1);
            }

            addRange(result, nums[nums.length-1] + 1, upper);

            return result;
        }
        private void addRange(List<String> ans, int start, int end) {
            if (start > end) {
                return;
            }
            if (start == end) {
                ans.add(start + "");
                return;
            }
            ans.add(start + "->" + end);
        }
    }

    // 2. 合并区间 · Merge Intervals
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
                }
            }

            return result;
        }
    }

    // 3. 插入区间 · Insert Interval
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
    }

}
