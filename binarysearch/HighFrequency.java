package binarysearch;

public class HighFrequency {
    // 1. 猜数游戏 · Guess Number Game
    // 我从 1 到 n 选择一个数字。 你需要猜我选择了哪个号码。
    // 每次你猜错了，我会告诉你这个数字与你猜测的数字相比是大还是小。
    // 预定义的接口 guess(int num)，它会返回 3 个可能的结果(-1，1或0):
    //     -1代表这个数字小于你猜测的数
    //     1代表这个数字大于你猜测的数
    //     0代表这个数字等于你猜测的数
    public class GuessNumberSolution {
        public int guessNumber(int n) {
            int start = 1, end = n;

            while (start + 1 < end) {
                int mid = start + (end - start) / 2;
                if (guess(mid) == 0) {
                    return mid;
                } else if (guess(mid) == 1) {
                    start = mid;
                } else { // guess(mid) == -1
                    end = mid;
                }
            }

            if (guess(start) == 0) {
                return start;
            } else { // guess(end) == 0)
                return end;
            }
        }
        private int guess(int nums) {
            return 0;
        }
    }

}
