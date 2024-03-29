package dfs;

import java.util.*;

public class Other {
    // 473 Matchsticks to Square
    // 输入为小女孩拥有火柴的数目，每根火柴用其长度表示。
    // 输出即为是否能用所有的火柴拼成正方形
    public class MakesquareSolution {
        public boolean makesquare(int[] matchsticks) {
            int total = 0;
            for (int val : matchsticks) { // 所有火柴长度
                total += val;
            }

            if (total == 0 || total % 4 != 0) { // 不是正方形
                return false;
            }

            Arrays.sort(matchsticks); // 升序，保证先取大的火柴（因为后面递归currIndex是减小的）。因为不是Integer，不能用Collections.reverseOrder
            return dfs(matchsticks, matchsticks.length - 1, total / 4, new int[4]);

        }
        // 用数组当前火柴依次放在四条边看结果
        // currIndex当前访问的火柴位置
        // target正方形最终的边长
        // sides当前正方形四边的长度（长度为4的数组）
        public boolean dfs(int[] nums, int currIndex, int target, int[] sides) {
            if (currIndex < 0) { // 所有火柴访问完
                return sides[0] == sides[1] && sides[1] == sides[2] && sides[2] == sides[3]; // 四边是否相等（true or false）
            }

            for (int i = 0; i < sides.length; i++) {
                // 当前火柴放到sides[i]这个边上
                // sides[i] == sides[i - 1]即上一个分支的值和当前分支的一样，上一个分支没有成功，说明这个分支也不会成功，直接跳过即可。
                if (sides[i] + nums[currIndex] > target || (i > 0 && sides[i] == sides[i-1])) {
                    continue;
                }
                sides[i] += nums[currIndex];
                if (dfs(nums, currIndex - 1, target, sides)) {
                    return true;
                }
                sides[i] -= nums[currIndex];
            }

            return false;
        }
    }

    // 22. Generate Parentheses
    // n 代表生成括号的对数，返回有效的括号组合
    // 输入：n = 3
    // 输出：["((()))","(()())","(())()","()(())","()()()"]
    public class GenerateParenthesisSolution {
        // 左括号的个数大于右括号，再添加是有效的
        // 不断递归添加括号
        public List<String> generateParenthesis(int n) {
            List<String> result = new ArrayList<>();
            if (n == 0) {
                return result;
            }
            dfs(result, "", 0, 0, n);
            return result;
        }
        private void dfs(List<String> res, String curr, int left, int right, int n) {
            if (left == n && right == n) {
                res.add(curr);
                return;
            }

            if (left < right) { // 右括号多，不合法，直接返回
                return;
            }

            if (left < n) {
                dfs(res, curr + "(", left + 1, right, n);
            }
            if (right < n) {
                dfs(res, curr + ")", left, right + 1, n);
            }
        }
    }

    // 1012. 至少有 1 位重复的数字
    // 给定正整数 N，返回小于等于 N 且具有至少 1 位重复数字的正整数的个数
    // 3562
    // 找每一位各不相同的，最后再用总数减它
    // 第一种情况，总共只有1，2,3位。固定第一位，剩下的九个数排列
    // 4th 3th 2th 1th total
    //            1-9 9xA(9,0)
    //        1-9 0-9 9xA(9,1)
    //    1-9 0-9 0-9 9xA(9,2)
    // 第二种情况，有4位。必须先确认比N小，再确认有没有重复
    // 4th 3th 2th 1th total
    //1-2 0-9 0-9 0-9 2xA(9,3)
    // 3  0-4 0-9 0-9 5xA(8,2)
    // 3   5  0-5 0-9 6xA(7,1)
    // 3   5   6  0-1 2xA(6,0)
    // 3   5   6   2  1
    public class NumDuplicateSolution {
        public int numDupDigitsAtMostN(int n) {
            // 把整数变成每一位的数字
            List<Integer> digits = new ArrayList<>();
            for (int x = n+1; x > 0; x /= 10) {
                digits.add(0,x % 10); // 在第0位不断加入高位的数
            }

            int res = 0, len = digits.size();
            // 1. 总位数小于n的位数（第一种情况）
            for (int i = 1; i < len; i ++) {
                res += 9 * A(9, i - 1);
            }

            // 2. 与n位数相等
            // 计算相同前缀情况下，后面的数据可以有多少种排法
            HashSet<Integer> seen = new HashSet<>();
            for (int i = 0; i < len; i ++) {
                // 第一位不能从0开始，所以i=0时j要从1开始
                // 在前缀确定和原数据一样的情况下，当前位还要保证比原始数据小
                for (int j = i > 0 ? 0 : 1; j < digits.get(i); j ++) {
                    if (!seen.contains(j)) { // 有相同的数字就重复了，不符合条件
                        res += A(9-i, len-i-1);
                    }
                }
                if (seen.contains(digits.get(i))) { // 在前缀中出现了相同的数，不用再往下了
                    break;
                }
                seen.add(digits.get(i)); // 原始数据中的前缀
            }
            return n - res;
        }
        private int A(int n, int m) {
            return fact(n) / fact(n-m);
        }
        private int fact(int n) {
            if (n == 1 || n == 0) {
                return 1;
            }
            return fact(n-1) * n;
        }
    }

    // 827. Making A Large Island
    // 把一个0改成1，求最大的岛屿面积
    public class LargestIslandSolution {
        int[][] grid;
        int[] dx = {1, 0, 0, -1};
        int[] dy = {0, 1, -1, 0};
        int row;
        int col;

        public int largestIsland(int[][] grid) {
            this.grid = grid;
            this.row = grid.length;
            this.col = grid[0].length;

            int res = 0;

            // 1. 标记联通岛屿为不同的序号（2，3，4...）并求面积
            int index = 2;
            int area[] = new int[row * col + 2];
            for (int i = 0; i < row; i ++) {
                for (int j = 0; j < col; j ++) {
                    if (grid[i][j] == 1) {
                        area[index] = dfs(i, j, index);
                        res = Math.max(res, area[index]); // 记录最大的面积

                        index ++;
                    }
                }
            }

            // 2. 改0为1，遍历周围四个方向的岛屿的序号，把面积sum
            for (int i = 0; i < row; i ++) {
                for (int j = 0; j < col; j ++) {
                    if (grid[i][j] == 0) {
                        // 把周围四个方向的index所代表的的面积加起来
                        // 因为周围有可能是同一个岛屿，避免多次相加，所以用Hash
                        Set<Integer> island = new HashSet<>();
                        for (int[] next : neighbor(i, j)) {
                            if (grid[next[0]][next[1]] > 1) {
                                island.add(grid[next[0]][next[1]]);
                            }
                        }
                        int currArea = 1;
                        for (Integer num : island) {
                            currArea += area[num];
                        }
                        res = Math.max(res, currArea);
                    }
                }
            }

            return res;
        }
        private int dfs(int i, int j, int index) {
            int area = 1;
            grid[i][j] = index;
            for (int[] next : neighbor(i, j)) {
                if (grid[next[0]][next[1]] == 1) {
                    area += dfs(next[0], next[1], index);
                }
            }
            return area;
        }
        private List<int[]> neighbor(int i, int j) {
            List<int[]> next = new ArrayList<>();
            for (int k = 0; k < 4; k ++) {
                int nextX = i + dx[k];
                int nextY = j + dy[k];
                if (nextX >= 0 && nextX < row && nextY >= 0 && nextY < col) {
                    next.add(new int[]{nextX, nextY});
                }
            }
            return next;
        }
    }

    // 934. Shortest Bridge
    // 矩阵中有两个岛（用1表示），建一座最短距离的桥使他们联通
    public class ShortestBridgeSolution {
        int m;
        int n;
        Queue<int[]> queue;

        public int shortestBridge(int[][] grid) {
            m = grid.length;
            n = grid[0].length;
            queue = new LinkedList<>();

            // 1. dfs找第一个联通的岛屿（把1标记为2）
            boolean found = false;
            for (int i = 0; i < m; i ++) {
                if (found) // 找到就可以退出了
                    break;
                for (int j = 0; j < n; j ++) {
                    if (grid[i][j] == 1) {
                        dfs(grid, i, j);
                        found = true;
                        break;
                    }
                }
            }

            // 2. bfs找第二个岛屿（过程中把0标记为2，之后只要遇到1意味着是另一个岛屿，则return）
            int step = 0;
            int[] dx = {1, -1, 0, 0};
            int[] dy = {0, 0, 1, -1};
            while (!queue.isEmpty()) {
                int size = queue.size();
                for (int i = 0; i < size; i ++) {
                    int[] curr = queue.poll();
                    for (int j = 0; j < 4; j ++) {
                        int nextX = curr[0] + dx[j];
                        int nextY = curr[1] + dy[j];
                        if (nextX < 0 || nextX >= m || nextY < 0 || nextY >= m) {
                            continue;
                        }
                        if (grid[nextX][nextY] == 2) { // 当前岛屿不用管
                            continue;
                        } else if (grid[nextX][nextY] == 1) { // 找到另一个岛屿了
                            return step;
                        } else { // 0
                            queue.offer(new int[]{nextX, nextY});
                            grid[nextX][nextY] = 2; // 别忘了标记
                        }
                    }
                }
                step ++;
            }

            return -1;
        }
        private void dfs(int[][] grid, int i, int j) {
            if (i < 0 || i >= m || j < 0 || j >= n || grid[i][j] != 1) {
                return;
            }
            grid[i][j] = 2;
            queue.offer(new int[]{i, j}); // 将这个岛屿经过的所有点都加入队列中

            dfs(grid, i + 1, j);
            dfs(grid, i - 1, j);
            dfs(grid, i, j + 1);
            dfs(grid, i, j - 1);
        }
    }
}
