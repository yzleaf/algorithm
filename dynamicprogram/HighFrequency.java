package dynamicprogram;

public class HighFrequency {
    // 1. 解码方法 · Decode Ways
    // 编码规则 'A' -> 1 'B' -> 2 ... 'Z' -> 26
    // 现在给你一个加密过后的消息，问有几种解码的方式
    public class NumDecodingsSolution {
        // 类似于爬楼梯的解法
        // 状态：dp[i]表示字符串的前i位解码有多少种解码方式
        // 初始化：dp[0] = dp[1] = 1，dp数组其他值均为0，
        // 状态转移方程
        //    若s[i - 1]表示的数是1到9，dp[i] += dp[i - 1]
        //    若s[i - 2]和s[i - 1]表示的数是10到26，dp[i] += dp[i - 2]
        //    若上述两种情况都不满足，直接返回答案0
        // 如果s以 '0' 开头, 则直接返回0
        public int numDecodings(String s) {
            if (s.length() == 0 || s.charAt(0) == '0') { // 以0开头
                return 0;
            }

            // dp[i]表示字符串的前i位解码有多少种解码方式
            int n = s.length();
            int[] dp = new int[n + 1];
            dp[0] = dp[1] = 1;

            for (int i = 2; i <= n; i++) {
                // 若s[i-1]表示的数是1到9
                if (s.charAt(i - 1) != '0') {
                    dp[i] += dp[i - 1];
                }
                // 若s[i-2]和s[i-1]表示的数是10到26
                if (s.charAt(i - 2) != '0' && (s.charAt(i - 2) - '0') * 10 + s.charAt(i - 1) - '0' <= 26) {
                    dp[i] += dp[i - 2];
                }
                // 若上述两种情况都不满足，直接返回答案0（***100这种情况）
                if (dp[i] == 0) {
                    return 0;
                }
            }

            return dp[n];
        }
    }

    // 2. 奇怪的打印机
    // 664
    // 打印机每次只能打印同一个字符序列。每次可以在任意起始和结束位置打印新字符，并且会覆盖掉原来已有的字符
    // 输入: "aba"
    // 输出: 2
    // 解释: 首先打印 "aaa" 然后在第二个位置打印 "b" 覆盖掉原来的字符 'a'。
    public class StrangePrinterSolution {
        public int strangePrinter(String s) {
            int n = s.length();
            if (n == 0) {
                return 0;
            }

            // dp[i][j]表示打印i到j位置最少打印次数
            int[][] dp = new int[n + 1][n + 1];

            // 一个字母最少也要打印一次，dp[i][i]=1，其他情况为0
            for (int i = 0; i < n; i++) {
                dp[i][i] = 1;
            }

            for (int len = 2; len <= n; len++) { // 每次固定一个区间长度遍历
                for (int i = 0; i + len - 1 < n; i++) { // 左端点
                    int j = i + len - 1; // 右端点

                    // .1 先假设第i个字母后这个区间的每一个字母都不相同
                    dp[i][j] = dp[i + 1][j] + 1;
                    // .2 假设从i+1开始到j结束的区间里有字母和区间首部元素i相同
                    for (int k = i + 1; k <= j; k++) {
                        if(s.charAt(k) == s.charAt(i)) { // 打印i的时候直接打印了k
                            dp[i][j] = Math.min(dp[i][j], dp[i][k - 1] + dp[k + 1][j]);
                        }
                    }
                }
            }

            return dp[0][n - 1];
        }
    }
}
