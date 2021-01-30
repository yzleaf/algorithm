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
}
