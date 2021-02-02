package other;

import java.util.*;

public class OtherHighFrequency {
    // 1. 矩形重叠 · Rectangle Overlap
    // 给定两个矩形，判断这两个矩形是否有重叠
    // l1代表第一个矩形的左上角 r1代表第一个矩形的右下角
    // l2代表第二个矩形的左上角 r2代表第二个矩形的右下角
    public class RecOverlapSolution {
        class Point {
            public int x, y;
            public Point() {
                x = 0;
                y = 0;
            }
            public Point(int a, int b) {
                x = a;
                y = b;
            }
        }
        // 判断不重叠的情况（一上一下，一左一右）
        // 需要确认一条边重合是否算重叠（细节）
        public boolean doOverlap(Point l1, Point r1, Point l2, Point r2) {
            if (l1.x > r2.x || l2.x > r1.x) { // 一左一右的情况
                return false;
            }
            if (r1.y > l2.y || r2.y > l1.y) { // 一上一下的情况
                return false;
            }

            return true;
        }
    }

    // 2. Check Word Abbreviation
    // 给定一个非空字符串 word 和缩写 abbr，返回字符串是否可以和给定的缩写匹配
    // 数字代表字母个数
    // ["word", "1ord", "w1rd", "wo1d", "wor1", "2rd", "w2d", "wo2", "1o1d", "1or1", "w1r1", "1o2", "2r1", "3d", "w3", "4"]
    public class ValidWordAbbreviationSolution {
        public boolean validWordAbbreviation(String word, String abbr) {
            char[] s = word.toCharArray();
            char[] t = abbr.toCharArray();

            int i = 0, j = 0;
            while (i < s.length && j < t.length) {
                if (Character.isDigit(t[j])) { // 是数字，需要匹配字母个数
                    if (t[j] == '0') {
                        return false;
                    }
                    int val = 0;
                    while (j < t.length && Character.isDigit(t[j])) { // 可能有多位数字
                        val = val * 10 + t[j] - '0';
                        j++;
                    }
                    i += val;
                } else { // 是字母，需要看两个字符串的字母是否相等
                    if (s[i++] != t[j++]) {
                        return false;
                    }
                }
            }

            return i == s.length && j == t.length; // 必须完整遍历，保证结尾相同
        }
    }

    // 3. Words Abbreviation
    // 给出一组 n 个不同的非空字符串，您需要按以下规则为每个单词生成 最小 的缩写
    // .1 从第一个字符开始，然后加上中间缩写掉的字符的长度，后跟最后一个字符。
    // .2 如果有冲突，就是多个单词缩写相同，加长前缀字母保证单词缩写不同。
    // .3 如果缩写不会使单词更短，则不进行缩写，保持原样。
    // 输入: ["like","god","internal","me","internet","interval","intension","face","intrusion"]
    // 输出: ["l2e","god","internal","me","i6t","interval","inte4n","f2e","intr4n"]
    public class WordsAbbreviationSolution {
        public String[] wordsAbbreviation(String[] dict) {
            int len = dict.length;

            String[] result = new String[len];
            int[] prefix = new int[len]; // 每个单词的前缀字母个数
            Map<String, Integer> count = new HashMap<>();

            // .1 先按照基本方法得到缩写
            for (int i = 0; i < len; i++) {
                prefix[i] = 1;
                result[i] = getAbbr(dict[i], 1);
                count.put(result[i], count.getOrDefault(result[i], 0) + 1);
            }

            // .2 检查是否有重复
            while (true) { // 多次for循环数组中所有缩写
                boolean unique = true;
                for (int i = 0; i < len; i++) {
                    if (count.get(result[i]) > 1) {
                        prefix[i] ++;
                        result[i] = getAbbr(dict[i], prefix[i]);
                        count.put(result[i], count.getOrDefault(result[i], 0) + 1);

                        unique = false; // 更新完abbr可能还是不unique
                    }
                }
                if (unique) { // 最后for循环一遍，所有String缩写都已经唯一
                    break;
                }
            }

            return result;
        }

        // 从第p个index开始的缩写
        private String getAbbr(String s, int p) {
            if (p >= s.length() - 2) {
                return s;
            }

            return s.substring(0, p) + (s.length() - 1 - p) + s.charAt(s.length() - 1);
        }
    }
}
