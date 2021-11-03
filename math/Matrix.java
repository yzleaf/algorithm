package math;

import  java.util.*;

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

    // 498. Diagonal Traverse
    // 按对角线顺序输出元素，第一次往斜向上，第二次就要斜向下
    public class FindDiagnosalSolution {
        public int[] findDiagonalOrder(int[][] mat) {

            // 对角线上x和y的坐标和是相等的
            // 如果斜向上，x横坐标增大，y纵坐标减小
            // 如果斜向下，x纵坐标减小，y横坐标增大
            int m = mat.length;
            int n = mat[0].length;

            int res[] = new int[m * n];
            int index = 0;
            boolean flag = true; // 斜向上/斜向下

            // i是对角线的元素和
            for (int i = 0; i < m + n - 1; i ++) {
                int r = 0, c = 0;
                if (flag) {
                    r = (i < m) ? i : m - 1; // x要从最大的开始（因为斜向右上输出）
                    c = i - r;
                    while (r >= 0 && c < n) {
                        res[index] = mat[r][c];
                        index ++;
                        r --;
                        c ++;
                    }
                } else {
                    c = (i < n) ? i : n - 1; // y要从最大的开始（因为斜向左下输出）
                    r = i - c;
                    while (r < m && c >= 0) {
                        res[index] = mat[r][c];
                        index ++;
                        r ++;
                        c --;
                    }
                }
                flag = !flag;
            }

            return res;
        }
        // 方法2 遍历数组，用HashMap存(key:对角线左边和，val:具体数字)
        // 然后再按key的值输出
    }

    // 1424. Diagonal Traverse II
    // 按对角线输出元素，斜向下，但是是稀疏矩阵
    // 要用HashMap来存：每个对角线坐标和key --- 对应的元素数值
    public class FindDiagnorlIISolution {
        public int[] findDiagonalOrder(List<List<Integer>> nums) {

            List<List<Integer>> mHash = new ArrayList<>();
            int num = 0; // 记录所有元素个数，方便最后建int数组的大小
            // 遍历整个矩阵数组，把不同元素存入不同的坐标和下
            for (int i = 0; i < nums.size(); i ++) {
                for (int j = 0; j < nums.get(i).size(); j ++) {
                    if (mHash.size() <= i + j) {
                        mHash.add(new ArrayList<>());
                    }
                    mHash.get(i + j).add(nums.get(i).get(j));
                    num ++;
                }
            }

            int[] res = new int[num];
            int index = 0;
            for (int i = 0; i < mHash.size(); i ++) {
                for (int j = mHash.get(i).size() - 1; j >= 0; j --) {
                    res[index] = mHash.get(i).get(j);
                    index ++;
                }
            }

            return res;
        }
    }
}
