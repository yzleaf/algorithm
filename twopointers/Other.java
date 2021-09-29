package twopointers;

import java.util.*;

public class Other {

    // Number of Airplanes in the Sky
    // 给出飞机的起飞和降落时间的列表，用序列 interval 表示. 请计算出天上同时最多有多少架飞机？
    // 如果多架飞机降落和起飞在同一时刻，我们认为降落有优先权。
    // 输入: [(1, 10), (2, 3), (5, 8), (4, 7)]
    // 输出: 3
    // 解释:
    // 第一架飞机在1时刻起飞, 10时刻降落.
    // 第二架飞机在2时刻起飞, 3时刻降落.
    // 第三架飞机在5时刻起飞, 8时刻降落.
    // 第四架飞机在4时刻起飞, 7时刻降落.
    // 在5时刻到6时刻之间, 天空中有三架飞机.
    public class NumAirplanesSolution {
        class Interval {
            int flag, end; // 起飞和降落时间
            Interval(int flag, int end) {
                this.flag = flag;
                this.end = end;
            }
        }

        final int UP = 1;
        final int DOWN = 0;

        // 将起飞时间和降落时间放到同一个数组中, 标识出是起飞还是降落时间, 然后对数组排序.
        // 遍历数组即可, 碰到起飞计数器加一, 碰到降落计数器减一. 维护最大值作为答案.

        public int countOfAirplanes(List<Interval> airplanes) {
            // 1. 数组第一维表示起飞/降落时间，第二维表示起飞还是降落
            int[][] timeFlag = new int[airplanes.size() * 2][2];
            for (int i = 0; i < timeFlag.length; i += 2) {
                timeFlag[i][0] = airplanes.get(i/2).flag;
                timeFlag[i][1] = UP;
                timeFlag[i+1][0] = airplanes.get(i/2).end;
                timeFlag[i+1][1] = DOWN;
            }

            // 2. 从小到大时间，以及降落优先的顺序 排序
            Arrays.sort(timeFlag, new Comparator<int[]>() {
                @Override
                public int compare(int[] o1, int[] o2) {
                    if (o1[0] == o2[0]) { // 如果时间相等，降落优先于起飞
                        return o1[1] - o2[1];
                    } else { //  按时间从小到大排序
                        return o1[0] - o2[0];
                    }
                }
            });

            // 3. 按顺序统计
            int count = 0, res = 1;
            for (int[] num : timeFlag) {
                if (num[1] == UP) {
                    count ++;
                } else { // 降落
                    count --;
                }
                res = Math.max(res, count);
            }

            return res;
        }
    }

    // 253. Meeting Rooms II
    // Given an array of meeting time intervals intervals where intervals[i] = [starti, endi]
    // return the minimum number of conference rooms required.
    // Input: intervals = [[0,30],[5,10],[15,20]]
    // Output: 2
    public class MeetingRooms2Solution {
        // 方法1，类似于上一题计算飞机数量，变换思维方式
        class TimeStatus {
            public int time;
            public int status; // 1表示会议开始，-1表示会议结束

            public TimeStatus(int time, int status) {
                this.time = time;
                this.status = status;
            }
        }

        public int minMeetingRooms(int[][] intervals) {

            List<TimeStatus> timeSta = new ArrayList<>(); // 建立 时间-状态 对应表
            for (int[] interval : intervals) {
                timeSta.add(new TimeStatus(interval[0], 1));
                timeSta.add(new TimeStatus(interval[1], -1));
            }

            Collections.sort(timeSta, new Comparator<TimeStatus>(){
                public int compare(TimeStatus o1, TimeStatus o2) {
                    if (o1.time != o2.time) {
                        return o1.time - o2.time;
                    } else {
                        return o1.status - o2.status;
                    }
                }
            });

            int cur = 0;
            int res = 0;
            for (int i = 0; i < timeSta.size(); i ++) {
                if (timeSta.get(i).status == 1) {
                    cur ++;
                } else {
                    cur --;
                }
                res = Math.max(res, cur);
            }

            return res;
        }

        // 方法2
        // 利用最小堆
        public int minMeetingRooms2(int[][] intervals) {
            // 按开始时间排序
            Arrays.sort(intervals, (o1, o2) -> o1[0] - o2[0]);
            // 最小堆记录结束时间
            Queue<Integer> minHeapEndTime = new PriorityQueue<>();

            minHeapEndTime.offer(intervals[0][1]);
            for (int i = 1; i < intervals.length; i ++) {
                if (intervals[i][0] >= minHeapEndTime.peek()) { // 跟最早结束的时间比较！！！
                    minHeapEndTime.poll(); // 旧的要删掉，因为被替代了
                }

                minHeapEndTime.offer(intervals[i][1]);
            }

            return minHeapEndTime.size();
        }
    }

    // 238. Product of Array Except Self
    // 返回不包括当前元素的数组其他元素之积
    // 不能用除法（可能当前数为0）
    class ProductExceptSelfSolution {
        public int[] productExceptSelf(int[] nums) {

            int len = nums.length;

            int[] left = new int[len]; // 存放nums[i]左边的所有元素积
            int[] right = new int[len]; // 存放nums[i]右边的所有元素积
            int[] res = new int[len];

            left[0] = 1;
            for (int i = 1; i < len; i ++) {
                left[i] = left[i-1] * nums[i-1];
            }

            right[len - 1] = 1;
            for (int i = len - 2; i >= 0; i --) {
                right[i] = right[i+1] * nums[i+1];
            }

            for (int i = 0; i < len; i ++) {
                res[i] = left[i] * right[i];
            }

            return res;
        }
        // 优化：用输出数组代替left[]，一个变量不断计算right[]中的每一个元素
    }
}
