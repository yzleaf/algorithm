package binarysearch;

public class HighFrequency {
    // 1. 猜数游戏 · Guess Number Game
    // 374
    // 我从 1 到 n 选择一个数字。 你需要猜我选择了哪个号码。
    // 每次你猜错了，我会告诉你这个数字与你猜测的数字相比是大还是小。
    // 预定义的接口 guess(int num)，它会返回 3 个可能的结果(-1，1或0):
    //     -1代表这个数字小于你猜测的数 pick(target) < num
    //     1代表这个数字大于你猜测的数 pick(target) > num
    //     0代表这个数字等于你猜测的数
    public class GuessNumberSolution {
        public final int EQUAL = 0;
        public final int GT = 1; // greater than
        public final int LT = -1; // less than

        public int guessNumber(int n) {
            int start = 1, end = n;

            while (start + 1 < end) {
                int mid = start + (end - start) / 2;
                if (guess(mid) == EQUAL) {
                    return mid;
                } else if (guess(mid) == GT) { // target > mid
                    start = mid;
                } else { // guess(mid) == LT (-1)
                    end = mid;
                }
            }

            if (guess(start) == EQUAL) {
                return start;
            } else { // guess(end) == 0)
                return end;
            }
        }
        private int guess(int nums) {
            return 0;
        }
    }

    // 以下是答案二分法的题目

    // 875. 爱吃香蕉的珂珂
    // N堆香蕉，第i堆中有piles[i]根香蕉
    // 返回她可以在H小时内吃掉所有香蕉的最小速度 K
    // 输入: piles = [3,6,7,11], H = 8
    // 输出: 4 （1 + 2 + 2 + 3小时）
    public class MinEatingSpeedSolution {
        public int minEatingSpeed(int[] piles, int h) {
            // K速度能吃完，K-1, K-2...都不不能吃完，K+1，K+2...都能吃完
            // 二分查找问题变为第一个能吃完的速度K
            int start = 0;
            int end = 1_000_000_000;
            while (start + 1 < end) {
                int midSpeed = (start + end) / 2;
                if (possible(piles, h, midSpeed)) { // 能吃完
                    end = midSpeed;
                } else {
                    start = midSpeed;
                }
            }

            if (possible(piles, h, start)) {
                return start;
            } else {
                return end;
            }

        }
        private boolean possible(int[] piles, int h, int k) {
            int time = 0;
            for (int num : piles) {
                time += ((num - 1)/ k + 1);
            }
            // 可以简化成return time <= h;
            if (time <= h) {
                return true;
            } else {
                return false;
            }
        }
    }

}
