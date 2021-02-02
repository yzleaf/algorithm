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
}
