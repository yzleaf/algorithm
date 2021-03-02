package binarysearch;

public class BinarySearchOther {

    // 1. Search a 2D Matrix
    // 74
    // 每行中的整数从左到右是排序的
    // 每行的第一个数大于上一行的最后一个整数。
    public class SearchMatrix1Solution {

        public boolean searchMatrix(int[][] matrix, int target) {
            if (matrix == null || matrix.length == 0) {
                return false;
            }
            if (matrix[0] == null || matrix[0].length == 0) {
                return false;
            }

            int row = matrix.length, column = matrix[0].length;
            int start = 0, end = row * column - 1;
            while (start + 1 < end) {
                int mid = start + (end - start) / 2;
                int number = matrix[mid / column][mid % column];
                if (number == target) {
                    return true;
                } else if (number > target) {
                    end = mid;
                } else { // number < target
                    start = mid;
                }
            }

            if (matrix[start / column][start % column] == target) {
                return true;
            }
            if (matrix[end / column][end % column] == target) {
                return true;
            }

            return false;
        }
    }

    // 2. Search a 2D Matrix II
    // 240
    // 每行中的整数从左到右是排序的，每一列的整数从上到下是排序的。
    // 在每一行或每一列中没有重复的整数。
    // 返回这个值得出现次数
    public class SearchMatrix2Solution {
        // 时间复杂度O(n+m)
        public int searchMatrixII(int[][] matrix, int target) {
            if (matrix == null || matrix.length == 0) {
                return 0;
            }
            if (matrix[0] == null || matrix[0].length == 0) {
                return 0;
            }

            int row = matrix.length;
            int column = matrix[0].length;
            int x = row - 1, y = 0; // 从左下角开始找
            int count = 0;

            while (x >= 0 && y < column) {
                if (matrix[x][y] < target) { // 小于target，向右找（增大当前值）
                    y++;
                } else if (matrix[x][y] > target) { // 大于target，向上找（减小当前值）
                    x--;
                } else { // 相等
                    count++;
                    x--;
                    y++;
                }
            }

            return count;
        }
    }

    // 3. Search for a Range
    // 给定一个包含 n 个整数的排序数组，找出给定目标值 target 的起始和结束位置。
    // 如果目标值不在数组中，则返回[-1, -1]
    public class SearchRangeSolution {
        public int[] searchRange(int[] A, int target) {
            if (A.length == 0) {
                return new int[]{-1, -1};
            }

            int start, end, mid;
            int[] bound = new int[2];

            // left bound (1st occurrence)
            start = 0;
            end = A.length - 1;
            while (start + 1 < end) {
                mid = start + (end - start) / 2;
                if (A[mid] == target) {
                    end = mid;
                } else if (A[mid] < target) {
                    start = mid;
                } else {
                    end = mid;
                }
            }
            if (A[start] == target) {
                bound[0] = start;
            } else if (A[end] == target) {
                bound[0] = end;
            } else { // not find
                bound[0] = bound[1] = -1;
                return bound;
            }

            // right bound (last occurrence)
            start = 0;
            end = A.length - 1;
            while (start + 1 < end) {
                mid = start + (end - start) / 2;
                if (A[mid] == target) {
                    start = mid;
                } else if (A[mid] < target) {
                    start = mid;
                } else {
                    end = mid;
                }
            }
            if (A[end] == target) {
                bound[1] = start;
            } else if (A[start] == target) {
                bound[1] = end;
            } else {
                bound[0] = bound[1] = -1;
                return bound;
            }

            return bound;
        }
    }


    // 4. Total Occurrence of Target
    // 给一个升序的数组，以及一个target，找到它在数组中出现的次数
    public class TotalOccurrenceSolution {
        // Same as Q3
        // return bound[1] - bound[0] + 1;
        public int totalOccurrence(int[] A, int target) {
            return -1;
        }
    }


    // 5. Smallest Rectangle Enclosing Black Pixels
    // 302
    // image: a binary matrix with '0'白色像素 and '1'黑色像素
    // x, y: the location of one of the black pixels
    // return 返回囊括所有黑色像素点的矩阵的最小面积
    public class MinAreaSolution {

        // 找到一个可以包含所有1的矩形
        // O(MlogN + NlogM)
        public int minArea(char[][] image, int x, int y) {
            if (image == null || image.length == 0 || image[0].length == 0) {
                return 0;
            }

            int row = image.length;
            int column = image[0].length;

            int left = findLeft(image, 0, y); // 找到最左边出现1的列
            int right = findRight(image, y, column - 1); // 找到最右边出现1的列
            int top = findTop(image, 0, x); // 找到最上面出现1的行
            int bottom = findBottom(image, x, row - 1); // 找到最下面出现1的行

            return (right - left + 1) * (top - bottom + 1);
        }

        private int findLeft(char[][] image, int start, int end) {
            while (start + 1 < end) {
                int mid = start + (end - start) / 2;
                if (isEmptyColumn(image, mid)) { // 全是0，没有1
                    start = mid;
                } else {
                    end = mid;
                }
            }
            if (isEmptyColumn(image, start)) {
                return end; // 原始给的为1的点
            }

            return start;
        }
        private int findRight(char[][] image, int start, int end) {
            while (start + 1 < end) {
                int mid = start + (end - start) / 2;
                if (isEmptyColumn(image, mid)) {
                    end = mid;
                } else {
                    start = mid;
                }
            }
            if (isEmptyColumn(image, end)) {
                return start;
            }

            return end;
        }
        private int findTop(char[][] image, int start, int end) {
            while (start + 1 < end) {
                int mid = start + (end - start) / 2;
                if (isEmptyRow(image, mid)) {
                    start = mid;
                } else {
                    end = mid;
                }
            }
            if (isEmptyRow(image, start)) {
                return end;
            }

            return start;
        }
        private int findBottom(char[][] image, int start, int end) {
            while (start + 1 < end) {
                int mid = start + (end - start) / 2;
                if (isEmptyRow(image, mid)) {
                    end = mid;
                } else {
                    start = mid;
                }
            }
            if (isEmptyRow(image, end)) {
                return start;
            }

            return end;
        }

        private boolean isEmptyColumn(char[][] image, int col) {
            for (int i = 0; i < image.length; i++) {
                if (image[i][col] == '1') {
                    return false;
                }
            }
            return true;
        }
        private boolean isEmptyRow(char[][] image, int row) {
            for (int j = 0; j < image[0].length; j++) {
                if (image[row][j] == '1') {
                    return false;
                }
            }
            return true;
        }
    }
}
