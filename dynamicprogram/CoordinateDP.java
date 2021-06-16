package dynamicprogram;

import java.util.Deque;
import java.util.LinkedList;

public class CoordinateDP {
    // 1. Minimum Path Sum
    // 给定一个只含非负整数的m*n网格，找到一条从左上角到右下角的可以使数字和最小的路径
    // 同一时间只能向下或者向右移动一步
    public class MinPathSumSolution {
        public int minPathSum(int[][] grid) {
            if (grid == null) {
                return 0;
            }

            int row = grid.length;
            int col = grid[0].length;
            int[][] f = new int[row][col]; // f表示从 0，0 到当前点的最短距离

            // 初始化
            f[0][0] = grid[0][0];
            for (int i = 1; i < row; i++) { // 第一列，从上到下只有一条路径
                f[i][0] = f[i-1][0] + grid[i][0];
            }
            for (int j = 1; j < col; j++) { // 第一行，从左到右只有一条路径
                f[0][j] = f[0][j-1] + grid[0][j];
            }

            // 选择：从左边过来的与上面过来的相比，较小的路径
            for (int i = 1; i < row; i++) {
                for (int j = 1; j < col; j++) {
                    f[i][j] = Math.min(f[i][j-1], f[i-1][j]) + grid[i][j];
                }
            }

            return f[row-1][col-1];
        }
    }

    // 2. 不同的路径 · Unique Paths
    // 有一个机器人的位于一个 m × n 个网格左上角
    // 机器人每一时刻只能向下或者向右移动一步，机器人试图达到网格的右下角，问有多少条不同的路径？
    public class UniquePathsSolution {
        // 方法1 DP
        // 时间O(mn) 空间O(mn)
        public int uniquePaths(int m, int n) {
            int[][] f = new int[m][n];
            f[0][0] = 1;

            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    if (i == 0 || j == 0) { // 第一行和第一列都只有一条路径
                        f[i][j] = 1;
                    } else {
                        f[i][j] = f[i-1][j] + f[i][j-1];
                    }
                }
            }
            return f[m-1][n-1];
        }

        // 方法2 优化的DP
        // 时间O(mn) 空间O(n)
        public int uniquePaths2(int m, int n) {
            int[] dp = new int[n];
            dp[0] = 1;

            // dp[j]表示到达(i, j)最多的路径数
            // 当前点需要来自于左边和上面两个数，而dp[j]可以理解为来自于上面的，dp[j-1]可以理解来自于左边的
            for (int i = 0; i < m; i++) {
                for (int j = 1; j < n; j++) {
                    dp[j] += dp[j-1];
                }
            }
            return dp[n-1];
        }


        // 方法3 组合方法（数学）
        // 机器人从左上角走到右下角，需要向下走m-1步，向右走n-1步，一共走m+n-2步
        // 问题转化为：从m+n-2步中选出m-1步向下，其余步数自然是向右，有多少种组合
        // 时间O(min(m,n)) 空间O(1)
        public int uniquePaths3(int m, int n) {

            // corner case
            if (m == 1 || n == 1) {
                return 1;
            }
            // 保证m <= n，提高效率
            if (m > n) { // swap(m, n)

                int temp = n;
                n = m;
                m = temp;
            }

            // 计算阶乘
            double numer = 1; // 分子
            double denomi = 1; // 分母
            for (int i = 1; i <= m - 1 ; i++) { // 1->m-1阶乘
                denomi *= i;
            }
            for (int i = n; i <= m + n - 2; i++) { // n->m+n-2阶乘
                numer *= i;
            }

            return (int)(numer / denomi);
        }
    }

    // 3. Climbing Stairs
    // 假设你正在爬楼梯，需要n步你才能到达顶部。但每次你只能爬一步或者两步，你能有多少种不同的方法爬到楼顶部
    // 输入:  n= 3
    // 输出: 3
    // 解释：
    //    1) 1, 1, 1
    //    2) 1, 2
    //    3) 2, 1
    public class ClimbStairsSolution {
        // 考虑最后一步走1阶还是走2阶。
        // 方案数Dpn = 最后一步走1阶的方案数 + 最后一步走2阶的方案数。
        // Dpn = Dpn-1 + Dpn-2.
        public int climbStairs(int n) {
            if (n <= 1) {
                return n;
            }
            int last = 1; // 最后一步走一阶，走到当前点时之前所有的方案数（初始为1）
            int lastLast = 1; // 最后一步走两阶，走到当前点时之前所有的方案数（初始为1）
            int now = 0; // 从2开始一直到n

            for (int i = 2; i <= n; i++) {
                now = last + lastLast;
                lastLast = last;
                last = now;
            }
            return now;
        }
    }

    // 4. Jump Game
    // 给出一个非负整数数组，你最初定位在数组的第一个位置。　　　
    // 数组中的每个元素代表你在那个位置可以跳跃的最大长度。　　　　
    // 判断你是否能到达数组的最后一个位置。
    public class JumpGameSolution {
        public boolean canJump(int[] A) {
            boolean[] can = new boolean[A.length]; // 能否跳到当前位置
            can[0] = true;

            for (int i = 1; i < A.length; i++) { // 能否到达i位置
                for (int j = 0; j < i; j++) { // 能否从j跳到i
                    if (can[j] && j + A[j] >= i) { // 从j位置+可以跳跃的长度大于等于i位置
                        can[i] = true;
                        break;
                    }
                }
            }

            return can[A.length - 1];
        }
        // 优化空间复杂度
        // 如果可以跳到i点，则说明一定可以跳到i前面的任意一点。所以，如果i位为True，前面的位置一定是True
        public boolean canJump2(int[] A) {
            boolean dp = true; // 是否能够到达当前位置
                               // 第一个点默认能到达

            for (int i = 1; i < A.length; i++) { // 能否到达i位置
                for (int j = 0; j < i; j++) { // 能否从j跳到i
                    if (j + A[j] >= i) { // 从j位置+可以跳跃的长度大于等于i位置
                        dp = true;
                        break;
                    } else {
                        dp = false;
                    }
                }
                if (dp == false) { // 无法到达当前i点，直接返回
                    return false;
                }
            }

            return dp;
        }
    }

    // 5. Jump Game II
    // 给出一个非负整数数组，你最初定位在数组的第一个位置
    // 数组中的每个元素代表你在那个位置可以跳跃的最大长度
    // 你的目标是使用最少的跳跃次数到达数组的最后一个位置
    public class JumpGame2Solution {

        public int jump(int[] A) {
            // steps[i]表示跳到下标为i的位置最少需要几次跳跃
            int[] steps = new int[A.length];

            steps[0] = 0;
            for (int i = 1; i < A.length; i++) {
                steps[i] = Integer.MAX_VALUE;
            }

            for (int i = 1; i < A.length; i++) {
                for (int j = 0; j < i; j++) {
                    if (steps[j] != Integer.MAX_VALUE && j + A[j] >= i) { // 能从j到达i
                        steps[i] = Math.min(steps[j] + 1, steps[i]); // 从j跳过来需要+1，之前遍历已经记录了一个steps[i]需要比较
                                                                     // 每个step[i]都能记录下到达当前点的最小跳跃次数
                    }
                }
            }
            return steps[A.length - 1];
        }
    }

    // 1696 Jump Game VI
    // 最多可以往前跳k步，但你不能跳出数组的边界
    // 返回你能得到的 最大得分
    public class JumpGame6Solution {
        // dp存当前得到的最大值（前面的k个dp数里取最大值 + nums[i]）
        // 但是如果用for循环遍历前面的k个数，会一直重复计算。考虑用滑动窗口最大值来记录（239）
        public int maxResult(int[] nums, int k) {
            int n = nums.length;
            int[] dp = new int[n];
            // 初始化dp数组，因为nums可能存在负值
            for (int i = 0; i < n; i++) {
                dp[i] = Integer.MIN_VALUE;
            }

            dp[0] = nums[0]; // 第一个数只能在开始没的选

            Deque<Integer> deque = new LinkedList<>(); // 滑动窗口存从大到小的至多k个数
            deque.offerLast(0);

            for (int i = 1; i < n; i++) {
                dp[i] = dp[deque.peekFirst()] + nums[i]; // 前k个dp里的最大index

                // 剔除队尾小的数，因为不可能会取到他们了
                while (!deque.isEmpty() && dp[i] >= dp[deque.peekLast()]) {
                    deque.pollLast();
                }
                deque.offerLast(i);

                // 判断队首是否在windows内，不在就要剔除
                if (deque.peekFirst() <= i - k) {
                    deque.pollFirst();
                }
            }

            return dp[n-1];
        }
    }

    // 1690. Stone Game VII
    // 有 n 块石子排成一排。每个玩家的回合中，可以移除最左边的石头或最右边的石头，并获得剩余石头值之和的得分。
    // 先手玩家（肯定赢）最大化差值，后手玩家最小化差值，返回两个玩家得分的差值
    public class stoneGame7Solution {
        public int stoneGameVII(int[] stones) {
            int n = stones.length;

            // sum[i][j]：表示从i到j的石头价值总和
            int sum[][] = new int[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = i; j < n; j++) {
                    if (i == j) {
                        sum[i][j] = stones[i];
                    } else {
                        sum[i][j] = sum[i][j-1] + stones[j];
                    }
                }
            }

            // 不管先手还是后手，总是在max relative score:(我total score - 对手total score)（对于后手来说，因为肯定输，所以输的少一点也是max负数）
            // -> max(我current score - (对手剩下游戏total score - 我剩下游戏total score))
            // -> max(我current score - 对手在剩下游戏的relative score)

            // dp[i][j]：表示剩下的石头堆为i到j时，本轮操作以后，我total score与对手total score的最大值(当前玩家不一定是先手Alice)
            // i = j: dp[i][j] = 0，因为删了就取不到其他值了,A取走它，剩0，B也只有0。0-0=0
            // i < j:
            //      i = j-1，剩下2个石头，删掉小的一个，本轮成绩高，另一个没分 -> 分差为：Max(stones[i], stones[j]) - 0
            //      i < j-1，本轮A从左端或右端删除，取更大的那个 -> 分差为：Max(sum[i+1][j] - dp[i+1][j], sum[i][j-1] - dp[i][j-1])
            //               左端删：剩下石头中，B比A的total score得分多dp[i+1][j]
            //               右端删：剩下石头中，B比A的total score得分多dp[i][j-1]
            int dp[][] = new int[n][n];
            for (int i = n; i >= 0; i--) { // 因为后面dp的i是从i+1得来的
                for (int j = i + 1; j < n; j++) { // 因为后面dp的j是从j-1得来的
                    if (j == i + 1) {
                        dp[i][j] = Math.max(stones[i], stones[j]);
                    } else {
                        dp[i][j] = Math.max(sum[i+1][j] - dp[i+1][j], sum[i][j-1] - dp[i][j-1]);
                    }
                }
            }
            return dp[0][n-1];
        }
    }

    // 871 Minimum Number of Refueling Stops

    // 每个 station[i] 代表一个加油站，它位于出发位置东面station[i][0]英里处，并且有station[i][1]升汽油。
    // 最初有 startFuel 升燃料。它每行驶 1 英里就会用掉 1 升汽油
    // 为了到达目的地，汽车所必要的最低加油次数是多少？如果无法到达目的地，则返回 -1
    public class MinRefuelStopsSolution {
        public int minRefuelStops(int target, int startFuel, int[][] stations) {
            int n = stations.length; // 车站个数

            // dp[i]表示加了i次油能够行驶的最大距离
            int dp[] = new int[n + 1];
            dp[0] = startFuel; // 刚开始能够行驶的最远距离

            for (int i = 0; i < n; i++) { // i个车站
                for (int t = i; t >= 0; t--) { // 加t次油
                    if (dp[t] >= stations[i][0]) { // 能够到达这个车站
                        dp[t + 1] = Math.max(stations[i][1] + dp[t], dp[t+1]);
                    }
                }
            }

            for (int i = 0; i < n + 1; i++) {
                if(dp[i] >= target) { // 到终点了没有油也算
                    return i;
                }
            }

            return -1;
        }
    }

}
