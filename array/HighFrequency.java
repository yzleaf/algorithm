package array;

public class HighFrequency {
    // 1. 字符同构 · Strings Homomorphism
    // 两个字符串是同构的如果 s 中的字符可以被替换得到 t。
    // 所有出现的字符必须用另一个字符代替，同时保留字符串的顺序。 没有两个字符可以映射到同一个字符，但一个字符可以映射到自己。
    // 输入 : s = "paper", t = "title"
    // 输出 : true
    // 说明 : p -> t, a -> i, e -> l, r -> e。
    public class IsIsomorphicSolution {
        public boolean isIsomorphic(String s, String t) {
            char[] sc = s.toCharArray();
            char[] tc = t.toCharArray();

            // 从s到t遍历一遍每个字母，确认映射
            int[] mapStoT = new int[256]; // ASCII范围0-255
            // char字母会直接变成对应ASCII的数字,进而成为数组下标
            for (int i = 0; i < s.length(); i++) {
                if (mapStoT[sc[i]] == 0 ) { // 还未映射过
                    mapStoT[sc[i]] = tc[i];
                } else { // 前面已经映射过，需要比较
                    if (mapStoT[sc[i]] != tc[i]) {
                        return false;
                    }
                }
            }

            // 从t到s遍历一遍每个字母，确认映射
            // 两个方向都要遍历，为了避免aabb -> cccc这种情况第一个方向遍历是查不到问题
            int[] mapTtoS = new int[256];
            for (int i = 0; i < t.length(); i++) {
                if (mapTtoS[tc[i]] == 0) {
                    mapTtoS[tc[i]] = sc[i];
                } else {
                    if (mapTtoS[tc[i]] != sc[i]) {
                        return false;
                    }
                }
            }

            return true;
        }
    }

    // 65. Valid Number
    // 字符串模拟题
    // Valid: ["2", "0089", "-0.1", "+3.14", "4.", "-.9", "2e10", "-90E3", "3e+7", "+6e-1", "53.5e93", "-123.456e789"]
    // Invalid: ["abc", "1a", "1e", "e3", "99e2.5", "--6", "-+3", "95a54e53"]
    class IsNumberSolution {
        public boolean isNumber(String s) {
            // 按e/E分开，左侧是整数/浮点，右侧整数
            // 判断是整数或者浮点数
            //    +/-只能出现在开头
            //    .最多出现一次
            //    至少有一个digit
            int n = s.length();
            char[] arr = s.toCharArray();
            int eIndex = -1;
            for (int i = 0; i < n; i ++) {
                if (arr[i] == 'e' || arr[i] == 'E') {
                    if (eIndex == -1) {
                        eIndex = i;
                    } else { //  != -1，之前出现过了e/E记录了index
                        return false;
                    }
                }
            }

            boolean res = true;
            if (eIndex == -1) {
                res &= check(arr, 0, n - 1, false);
            } else { // 有e
                res &= check(arr, 0, eIndex - 1, false);
                res &= check(arr, eIndex + 1, n - 1, true); // 右半部分必须为整数
            }

            return res;
        }
        private boolean check(char[] arr, int start, int end, boolean mustInt) {
            if (start > end) { // 避免 e3 这样的数字出现
                return false;
            }
            boolean seeDigit = false;
            boolean seeDot = false;
            if (arr[start] == '+' || arr[start] == '-') {
                start ++;
            }
            for (int i = start; i <= end; i ++) {
                if (arr[i] == '.') {
                    if (mustInt || seeDot) {
                        return false;
                    }
                    seeDot = true;
                } else if (arr[i] >= '0' || arr[i] <= '9') {
                    seeDigit = true;
                } else {
                    return false;
                }
            }
            return seeDigit;
        }
    }
}
