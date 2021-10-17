package bfs;

import java.util.*;

public class MatrixBFS {

    class Coordinate {
        int x, y;
        public Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    // 1. 岛屿的个数 Number of Islands
    // 200
    // 0 代表海，1 代表岛，如果两个 1 相邻，那么这两个 1 属于同一个岛。我们只考虑上下左右为相邻。
    public class NumIslandsSolution {
        // 四个方向移动的坐标：下，右，左，上
        int[] deltaX = {0, 1, -1, 0};
        int[] deltaY = {1, 0, 0, -1};

        boolean[][] visited; // 矩阵中的点是否被遍历处理过

        /**
         * @param grid a boolean 2D matrix
         * @return an integer
         */
        public int numIslands(boolean[][] grid) {
            if (grid == null || grid.length == 0) {
                return 0;
            }
            if (grid[0] == null || grid[0].length == 0) {
                return 0;
            }

            int islands = 0;
            int row = grid.length, column = grid[0].length;
            visited = new boolean[row][column];

            for (int i = 0; i < row; i++) {
                for (int j = 0; j < column; j++) {
                    if (grid[i][j] && !visited[i][j]) { // gird[][]为1且未被访问过
                        bfs(grid, i, j); // 找到联通的岛屿并标记
                        islands++;
                    }
                }
            }
            return islands;
        }
        private void bfs(boolean[][] grid, int x, int y) {
            Queue<Coordinate> queue = new LinkedList<>();

            queue.offer(new Coordinate(x, y)); // 当前点进队列，进行BFS
            visited[x][y] = true;

            while (!queue.isEmpty()) {
                Coordinate coor = queue.poll();
                for (int direction = 0; direction < 4; direction++) { // 遍历这个点的四个方向
                    int newX = coor.x + deltaX[direction];
                    int newY = coor.y + deltaY[direction];
                    if (!isValid(grid, newX, newY)) {
                        continue;
                    }
                    queue.offer(new Coordinate(newX, newY));
                    visited[newX][newY] = true;
                }
            }

        }
        private boolean isValid(boolean[][] grid, int x, int y) {
            int row = grid.length, column = grid[0].length;
            if (x < 0 || x >= row || y < 0 || y >= column) {
                return false;
            }
            if (visited[x][y]) { // 已经访问过
                return false;
            }
            return grid[x][y]; // grid[][]只有是1的情况下才有效，进行后续的扩展和计数
        }
    }

    // 2. 僵尸矩阵 Zombie in Matrix
    // 给一个二维网格，每一个格子都有一个值，2 代表墙，1 代表僵尸，0 代表人类（数字 0, 1, 2）
    // 僵尸每天可以将上下左右最接近的人类感染成僵尸，但不能穿墙。将所有人类感染为僵尸需要多久，如果不能感染所有人则返回 -1。
    public class ZombieSolution {
        public int PEOPLE = 0;
        public int ZOMBIE = 1;
        public int WALL = 2;

        public int[] deltaX = {1, 0, 0, -1};
        public int[] deltaY = {0, 1, -1, 0};

        public int zombie(int[][] grid) {
            if (grid == null || grid.length == 0 || grid[0].length == 0) {
                return 0;
            }

            int row = grid.length;
            int column = grid[0].length;

            // initialize the queue & count people
            int people = 0;
            Queue<Coordinate> queue = new LinkedList<Coordinate>();
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < column; j++) {
                    if (grid[i][j] == PEOPLE) {
                        people++;
                    } else if (grid[i][j] == ZOMBIE) { // 原始zombie先加入队列
                        queue.offer(new Coordinate(i, j));
                    }
                }
            }

            if (people == 0) { // 现有人数为0
                return 0;
            }

            // BFS
            int days = 0;
            while (!queue.isEmpty()) {
                days++;
                int size = queue.size(); // 分层次bfs（因为要求天数）
                for (int i = 0; i < size; i++) {
                    Coordinate zombie = queue.poll();
                    for (int direction = 0; direction < 4; direction++) { // 四个方向移动
                        Coordinate next = new Coordinate(zombie.x + deltaX[direction],
                                                        zombie.y + deltaY[direction]);
                        if (!isValidPeople(next, grid)) {
                            continue;
                        }

                        grid[next.x][next.y] = ZOMBIE;
                        people--;

                        if (people == 0) { // 现存人数
                            return days;
                        }

                        queue.offer(next);
                    }
                }
            } // while
            return -1;
        }
        private boolean isValidPeople(Coordinate coor, int[][] grid) {
            int row = grid.length, column = grid[0].length;
            if (coor.x < 0 || coor.x >= row) {
                return false;
            }
            if (coor.y < 0 || coor.y >= column) {
                return false;
            }

            return (grid[coor.x][coor.y] == PEOPLE); // 返回是否为PEOPLE
        }
    }

    // 3. 骑士的最短路线 Knight Shortest Path
    // 给定骑士在棋盘上的 初始 位置(一个2进制矩阵 0 表示空 1 表示有障碍物）
    // 找到到达终点的最短路线，返回路线的长度。如果骑士不能到达则返回-1
    public class KnightShortestSolution {

        // 每次运动的8个方向
        public final int[] dx = {1, 1, -1, -1, 2, 2, -2, -2};
        public final int[] dy = {2, -2, 2, -2, 1, -1, 1, -1};

        /**
         * @param grid: a chessboard included 0 (false) and 1 (true)
         * @param source: a point
         * @param destination: a point
         * @return the shortest path
         */
        public int shortestPath(boolean[][] grid, Coordinate source, Coordinate destination) {
            if (grid == null || grid.length == 0 || grid[0] == null || grid[0].length == 0) {
                return -1;
            }

            Queue<Coordinate> queue = new LinkedList<Coordinate>();

            int row = grid.length, column = grid[0].length;
            queue.offer(source);

            int steps = 0; // 记录步数
            while (!queue.isEmpty()) {
                int size = queue.size();
                for (int i = 0; i < size; i++) {
                    Coordinate point = queue.poll();
                    if (point.x == destination.x && point.y == destination.y) {
                        return steps;
                    }

                    for (int direction = 0; direction < 8; direction++) {
                        int adjX = point.x + dx[direction];
                        int adjY = point.y + dy[direction];
                        if (!isValid(adjX, adjY, grid)) {
                            continue;
                        }

                        // 走过这个点，置为不可再到达，否则步数只会更大
                        grid[adjX][adjY] = true;
                        queue.offer(new Coordinate(adjX, adjY));
                    }
                }
                steps++; // 步数加1，到下一次运动
            } // while

            return -1;
        }

        private boolean isValid(int x, int y, boolean[][] grid) {
            if (x < 0 || x >= grid.length) {
                return false;
            }
            if (y < 0 || y >= grid[0].length) {
                return false;
            }
            return (grid[x][y] == false); // 0：可以踩 1：不能踩
        }
    }

    // 4. Build Post Office II
    // 给出一个二维的网格，每一格可以代表墙2 ，房子1，以及空0。在网格中找到一个位置去建立邮局，使得所有的房子到邮局的距离和是最小的
    // 返回所有房子到邮局的最小距离和，如果没有地方建立邮局，则返回-1
    public class PostOffice {

        public final int EMPTY = 0;
        public final int HOUSE = 1;
        public final int WALL = 2;

        public int shortestDistance(int[][] grid) {
            if (grid == null || grid.length == 0 || grid[0].length == 0) {
                return -1;
            }

            int ans = Integer.MAX_VALUE;
            // 遍历每一个点，如果是空地，计算到房子的距离和->最后比较，返回最小
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[0].length; j++) {
                    if (grid[i][j] == EMPTY) {
                        ans = Math.min(ans, bfs(grid, i ,j));
                    }
                }
            }
            return (ans == Integer.MAX_VALUE) ? -1 : ans;
        }
        // 返回所有房子到当前点的距离和
        private int bfs(int[][] grid, int x, int y) {
            Queue<Coordinate> queue = new LinkedList<>();
            boolean[][] visited = new boolean[grid.length][grid[0].length];

            Coordinate coor = new Coordinate(x, y);
            queue.offer(coor);
            visited[x][y] = true;

            int[] dx = {1, -1, 0, 0};
            int[] dy = {0, 0, 1, -1};

            int sum = 0; // 距离总和
            int step = 0; // 空地往外走的步数

            while (!queue.isEmpty()) {
                step++;
                int size = queue.size();
                for (int i = 0; i < size; i++) {
                    Coordinate curr = queue.poll(); // 当前层的所有点
                    for (int direction = 0; direction < 4; direction++) {
                        Coordinate next = new Coordinate(curr.x + dx[direction],
                                                         curr.y + dy[direction]);

                        if (isValid(next, grid, visited)) {
                            visited[next.x][next.y] = true;
                            if (grid[next.x][next.y] == HOUSE) {
                                sum += step; // 所有房子到当前点的和
                            }
                            if (grid[next.x][next.y] == EMPTY) {
                                queue.offer(next);
                            }
                        }
                    } // 4 direction
                } // level size
            } // while

            // 检查是否有当前邮局到不了的房子
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[0].length; j++) {
                    if (grid[i][j] == HOUSE && !visited[i][j]) {
                        return Integer.MAX_VALUE;
                    }
                }
            }
            return sum;
        }
        private boolean isValid(Coordinate coor, int[][] grid, boolean[][] visited) {
            if (coor.x < 0 || coor.x >= grid.length ||
                coor.y < 0 || coor.y >= grid[0].length) {
                return false;
            }
            if (visited[coor.x][coor.y] = true) {
                return false;
            }

            return grid[coor.x][coor.y] == EMPTY;
        }
    }

    // 773 Sliding Puzzle
    // 2*3 拼图，每次移动一个格子，使其排列成123450的形状
    public class SlidingPuzzleSolution {
        // BFS，找到每次可以移动到0的所有情况

        public int slidingPuzzle(int[][] board) {

            // 把matrix转为String
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < board.length; i ++) {
                for (int j = 0; j < board[0].length; j ++) {
                    sb.append(board[i][j]);
                }
            }
            String initial = sb.toString();
            String target = "123450"; // 最终位置

            // 0在不同位置，其他可以变动的数的位置
            int[][] swapIndex= new int[][]{{1,3}, {0,2,4}, {1,5},
                    {0,4}, {1,3,5}, {2,4}};

            Queue<String> queue = new LinkedList<>();
            queue.offer(initial);

            Set<String> visited = new HashSet<>();
            visited.add(initial);

            int res = 0;
            while (!queue.isEmpty()) {

                int size = queue.size();
                for (int i = 0; i < size; i ++) {
                    String curr = queue.poll();
                    if (curr.equals(target)) {
                        return res;
                    }

                    int idx = -1;
                    for (int index = 0; index < curr.length(); index ++) {
                        if (curr.charAt(index) == '0') {
                            idx = index;
                            break;
                        }
                    }

                    // 遍历可能存在的换数方式
                    int[] swap = swapIndex[idx];
                    for (int index = 0; index < swap.length; index ++) {
                        String next = swapChar(curr, idx, swap[index]);
                        if (!visited.contains(next)) {
                            queue.offer(next);
                            visited.add(next);
                        }
                    }
                }
                res ++;
            }

            return -1;

        }
        private String swapChar(String curr, int idx, int swapIdx) {
            StringBuilder sb = new StringBuilder(curr);
            sb.setCharAt(idx, curr.charAt(swapIdx)); // 0的位置换成数组中可以移动的数
            sb.setCharAt(swapIdx, curr.charAt(idx)); // 换成0

            return sb.toString();
        }
    }
}
