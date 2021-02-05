package other;

public class Number {
    // 1. 罗马数字转整数 · Roman to Integer
    public class RomanToIntSolution {
        public int romanToInt(String s) {
            int result = 0;
            char[] sc = s.toCharArray();

            result = toInt(sc[0]);
            for (int i = 1; i < s.length(); i++) {
                result += toInt(sc[i]); // 结果加上当前位的数据
                if (toInt(sc[i - 1]) < toInt(sc[i])) { // 小的数在左边，应该要减去小的数
                    result -= toInt(sc[i - 1]) * 2; // 因为在上一次遍历的时候先加了，这一次要多减回去
                }
            }

            return result;
        }
        private int toInt(char s) {
            switch (s) {
                case 'I': return 1;
                case 'V': return 5;
                case 'X': return 10;
                case 'L': return 50;
                case 'C': return 100;
                case 'D': return 500;
                case 'M': return 1000;
            }
            return 0;
        }
    }

    // 2. 整数转罗马数字 · Integer to Roman
    public class IntToRomanSolution {
        public String intToRoman(int n) {
            if (n <= 0 || n > 4000) {
                return "";
            }
            String M[] = {"", "M", "MM", "MMM"};
            String C[] = {"", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM"};
            String X[] = {"", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC"};
            String I[] = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"};

            return M[n/1000] + C[(n/100) % 10] + X[(n/10) % 10] + I[(n % 10)];
        }

    }
}
