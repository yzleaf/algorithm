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
    // 0 代表海，1 代表岛，如果两个 1 相邻，那么这两个 1 属于同一个岛。我们只考虑上下左右为相邻。
    public class NumIslandsSolution {
        // 四个方向移动的坐标：下，右，左，上
        int[] deltaX = {0, 1, -1, 0};
        int[] deltaY = {1, 0, 0, -1};

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
            boolean[][] visited = new boolean[row][column];

            for (int i = 0; i < row; i++) {
                for (int j = 0; j < column; j++) {
                    if (grid[i][j] && !visited[i][j]) { // gird[][]为1且未被访问过
                        bfs(grid, i, j, visited);
                        islands++;
                    }
                }
            }
            return islands;
        }
        private void bfs(boolean[][] grid, int x, int y, boolean[][] visited) {
            Queue<Coordinate> queue = new LinkedList<>();

            queue.offer(new Coordinate(x, y)); // 当前点进队列，进行BFS
            visited[x][y] = true;

            while (!queue.isEmpty()) {
                Coordinate coor = queue.poll();
                for (int direction = 0; direction < 4; direction++) { // 遍历这个点的四个方向
                    int newX = coor.x + deltaX[direction];
                    int newY = coor.y + deltaY[direction];
                    if (!isValid(grid, newX, newY, visited)) {
                        continue;
                    }
                    queue.offer(new Coordinate(newX, newY));
                    visited[newX][newY] = true;
                }
            }

        }
        private boolean isValid(boolean[][] grid, int x, int y, boolean[][] visited) {
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
    // 给一个二维网格，每一个格子都有一个值，2 代表墙，1 代表僵尸，0 代表人类(数字 0, 1, 2)
    // 僵尸每天可以将上下左右最接近的人类感染成僵尸，但不能穿墙。将所有人类感染为僵尸需要多久，如果不能感染所有人则返回 -1。
    public class ZombieSolution {
        public int PEOPLE = 0;
        public int ZOMBIE = 1;
        public int WALL = 2;

        public int[] deltaX = {1, 0, 0, -1};
        public int[] deltaY = {0, 1, -1, 0};

        /**
         * @param grid a 2D integer grid
         * @return an integer
         */
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
                int size = queue.size(); // 分层次bfs
                for (int i = 0; i < size; i++) {
                    Coordinate zombie = queue.poll();
                    for (int direction = 0; direction < 4; direction++) { // 四个方向移动
                        Coordinate adj = new Coordinate(zombie.x + deltaX[direction],
                                                        zombie.y + deltaY[direction]);
                        if (!isValidPeople(adj, grid)) {
                            continue;
                        }

                        grid[adj.x][adj.y] = ZOMBIE;
                        people--;

                        if (people == 0) {
                            return days;
                        }

                        queue.offer(adj);
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
            Map<Integer, Integer> distance = new HashMap<Integer, Integer>();

            int row = grid.length, column = grid[0].length;
            queue.offer(source);
            distance.put(source.x * column + source.y, 0); // 利用x * column + y，每个点会对应一个distance

            while (!queue.isEmpty()) {
                Coordinate point = queue.poll();
                if (point.x == destination.x && point.y == destination.y) {
                    return distance.get(point.x * column + point.y);
                }

                for (int direction = 0; direction < 8; direction++) {
                    int adjX = point.x + dx[direction];
                    int adjY = point.y + dy[direction];
                    if (!isValid(adjX, adjY, grid)) {
                        continue;
                    }
                    if (distance.containsKey(adjX * column + adjY)) { // 之前已经踩过这个点了
                        continue;
                    }

                    distance.put(adjX * column + adjY, distance.get(adjX * column + adjY) + 1); // 距离+1
                    queue.offer(new Coordinate(adjX, adjY));
                }
            }

            return -1;
        }

        private boolean isValid(int x, int y, boolean[][] grid) {
            if (x < 0 || x >= grid.length) {
                return false;
            }
            if (y < 0 || y >= grid[0].length) {
                return false;
            }
            return !grid[x][y]; // 0：可以踩 1：不能踩
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

            // 遍历每一个点，如果是空地，计算到房子的距离和->最后比较返回最小
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[0].length; j++) {
                    if (grid[i][j] == EMPTY) {
                        ans = Math.min(ans, bfs(grid, i ,j));
                    }
                }
            }

            return (ans == Integer.MAX_VALUE) ? -1 : ans;
        }
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
                    Coordinate curCoor = queue.poll(); // 当前层的所有点
                    for (int direction = 0; direction < 4; direction++) {
                        Coordinate nextCoor = new Coordinate(curCoor.x + dx[direction],
                                                             curCoor.y + dy[direction]);

                        if (isValid(nextCoor, grid, visited)) {
                            visited[nextCoor.x][nextCoor.y] = true;
                            if (grid[nextCoor.x][nextCoor.y] == HOUSE) {
                                sum += step; // 所有房子到当前点的和
                            }
                            if (grid[nextCoor.x][nextCoor.y] == EMPTY) {
                                queue.offer(nextCoor);
                            }
                        }
                    } // 4 direction
                } // level size
            } // while

            // 检查是否有当前邮局到不了的房子
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[0].length; j++) {
                    if (grid[i][j] == 1 && !visited[i][j]) {
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
}
