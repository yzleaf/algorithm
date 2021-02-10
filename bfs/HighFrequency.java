package bfs;

import java.util.*;

public class HighFrequency {
    // 1. 被围绕的区域 · Surrounded Regions
    // 给一个二维的矩阵，包含 'X' 和 'O', 找到所有被 'X' 围绕的区域，并用 'X' 替换其中所有的 'O'
    // 输入:
    //  X X X X
    //  X O O X
    //  X X O X
    //  X O X X
    // 输出:
    //  X X X X
    //  X X X X
    //  X X X X
    //  X O X X
    public class SurroundedRegionsSolution {
        // 先把从边界的O都标记为W，之后BFS把边界O能到达的O都标记为W（意味着所有W都是不能被X包围的）
        // 最后把非W的地方就全都需要标记为 X
        public void surroundedRegions(char[][] board) {
            int row = board.length;
            int col = board[0].length;
            if (row == 0) {
                return;
            }

            // 标记边界
            for (int i = 0; i < row; i++) {
                bfs(board, i, 0); // 左边
                bfs(board, i, col - 1); // 右边
            }
            for (int j = 0; j < col; j++) {
                bfs(board, 0, j); // 上边
                bfs(board, row - 1, j); // 下边
            }

            for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {
                    if (board[i][j] == 'W') { // 还原回去
                        board[i][j] = 'O';
                    } else {
                        board[i][j] = 'X';
                    }
                }
            }
        }
        private void bfs(char[][] board, int x, int y) {
            if (board[x][y] != 'O') {
                return;
            }
            int row = board.length;
            int col = board[0].length;

            int[] dx = { 0, 1, 0, -1 };
            int[] dy = { 1, 0, -1, 0 };

            Queue<Integer> xQueue = new LinkedList<>();
            Queue<Integer> yQueue = new LinkedList<>();
            xQueue.offer(x);
            yQueue.offer(y);
            board[x][y] = 'W'; // means water

            while (!xQueue.isEmpty()) {
                int curX = xQueue.poll();
                int curY = yQueue.poll();
                for (int i = 0; i < 4; i++) {
                    int nextX = curX + dx[i];
                    int nextY = curY + dy[i];

                    if (nextX >= 0 && nextX < row && nextY >= 0 && nextY < col && board[nextX][nextY] == 'O') {
                        xQueue.offer(nextX);
                        yQueue.offer(nextY);
                        board[nextX][nextY] = 'W';
                    }
                }
            } // while
        }
    }

    // 2. Nearest Exit
    // INF是房间，0是出口，把INF改为离最近的出口的距离
    public class RoomNearestExitSolution {
        static final int INF = 2147483647;

        // 从每个出口0开始找，找到房间就标记距离
        public void RoomNearestExit(int[][] room) {
            int row = room.length;
            int col = room[0].length;
            if (row == 0) {
                return;
            }

            int[] dx = { 0, 1, 0, -1 };
            int[] dy = { 1, 0, -1, 0 };

            Queue<Integer> xQueue = new LinkedList<>();
            Queue<Integer> yQueue = new LinkedList<>();

            // 所有的出口0都进入队列，依次BFS
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {
                    if (room[i][j] == 0) {
                        xQueue.offer(i);
                        yQueue.offer(j);
                    }
                }
            }

            while (!xQueue.isEmpty()) {
                int curX = xQueue.poll();
                int curY = yQueue.poll();

                for (int i = 0; i < 4; i++) {
                    int nextX = curX + dx[i];
                    int nextY = curY + dy[i];
                    if (nextX >= 0 && nextX < row && nextY >= 0 && nextY < col && room[nextX][nextY] == INF) {
                        xQueue.offer(nextX);
                        yQueue.offer(nextY);
                        room[nextX][nextY] = room[curX][curY] + 1; // 当前房间距离+1到下一个房间
                    }
                }
            }

        }
    }

}
