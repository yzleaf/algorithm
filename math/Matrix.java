package math;

public class Matrix {
    // 1. Rotate Image
    // 给定一个N×N的二维矩阵表示图像，90度顺时针旋转图像。
    // 要求原地完成
    public class RotateImageSolution {
        // 翻转的四个点对应坐标
        // 1 (r, c)
        // 2 (c, n – 1 - r)
        // 3 (n – 1 - r, n – 1 - c)
        // 4 (n – 1 - c, r)

        // 方法1，把矩阵划分为四个部分依次旋转
        public void rotate(int[][] matrix) {
            int n = matrix.length;

            for (int r = 0; r < n / 2 + 1; r++) { // 行取到一半+1（行和列只要有一个+1就可以了，一个会转到另一个上）
                for (int c = 0; c < n / 2; c++) { // 列取一半
                    int tmp = matrix[r][c];
                    matrix[r][c] = matrix[n-1-c][r];
                    matrix[n-1-c][r] = matrix[n-1-r][n-1-c];
                    matrix[n-1-r][n-1-c] = matrix[c][n-1-r];
                    matrix[c][n-1-r] = tmp;
                }
            }
        }

        // 方法2 旋转 = 上下翻转后 + 交换x, y(转置)
        // 顺时针90:先上下,再对角线
        // 逆时针90:先左右,再对角线
        public void rotate2(int[][] matrix) {
            int n = matrix.length;

            // 上下翻转
            for (int r = 0; r < n / 2; r++) {
                for (int c = 0; c < n; c++) {
                    int tmp = matrix[r][c];
                    matrix[r][c] = matrix[n-r-1][c];
                    matrix[n-r-1][c] = tmp;
                }
            }

            // 对角线交换
            for (int r = 0; r < n; r++) {
                for (int c = 0; c < r; c++) { // int c = r; c < n; c++
                    int tmp = matrix[r][c];
                    matrix[r][c] = matrix[c][r];
                    matrix[c][r] = tmp;
                }
            }
        }
    }

    // 2. Sparse Matrix Multiplication
    // 给定两个 稀疏矩阵 A 和 B，返回AB的结果。
    public class MatrixMultiplicationSolution {
        // 常规乘法
        public int[][] multiply(int[][] A, int[][] B) {
            int aRow = A.length;
            int aCol = A[0].length;
            int bRow = B.length; // a的列和b的行相等
            int bCol = B[0].length;
            int[][] C = new int[aRow][bCol];

            for (int i = 0; i < aRow; i++) {
                for (int j = 0; j < bCol; j++) {
                    for (int k = 0; k < aCol; k++) { // A一行分别乘B一列累加
                        C[i][j] += A[i][k] * B[k][j];
                    }
                }
            }

            return C;
        }

        // 改进，把A里的0元素跳过
        public int[][] multiply2(int[][] A, int[][] B) {
            int aRow = A.length;
            int aCol = A[0].length;
            int bRow = B.length; // a的列和b的行相等
            int bCol = B[0].length;
            int[][] C = new int[aRow][bCol];

            for (int i = 0; i < aRow; i++) {
                for (int k = 0; k < aCol; k++) { // 与常规做法交换了for循环的次序
                    if (A[i][k] == 0) { // 如果A[][]为0的话，它与B各个元素相乘都为0（作为C矩阵中的一个累加因子）
                        continue;
                    }
                    for (int j = 0; j < bCol; j++) {
                        C[i][j] += A[i][k] * B[k][j];
                    }
                }
            }

            return C;
        }

        // 再改进，把B矩阵中0元素拿走，用List记录非0元素的行列坐标
        // 这样第三重for循环只要去加B的非零元素
    }
}
