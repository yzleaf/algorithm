package dynamicprogram;

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

}