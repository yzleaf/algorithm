package dfs;

import java.util.*;

public class HighFrequency {
    // 1. 电话号码的字母组合 · Letter Combinations of a Phone Number
    // 输入一系列数字串，得到所有它可能代表的字母
    public class LetterCombinationPhoneNumberSolution {
        List<String> result;
        String[] phone = {"", "", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"}; // 九宫格输入法

        public List<String> letterCombinations(String digits) {

            result = new ArrayList<>();
            if (digits.length() == 0) {
                return result;
            }

            dfs(0, digits.length(), "", digits);

            return result;
        }

        // length总长度
        // str当前的字符串内容
        private void dfs(int index, int length, String str, String digits) {

            if (index == length) { // index超出了length范围返回
                result.add(str);
                return;
            }

            int num = digits.charAt(index) - '0'; // 得到当前的数字
            for (char c : phone[num].toCharArray()) { // 这个数字对应的每一个字母
                dfs(index + 1, length, str + c, digits);
            }
            // 增加的c会不断被替换，所以不用想list一样add和remove操作
        }
    }

    // 2. 因式分解 · Factorization
    // 一个非负数可以被视为其因数的乘积。编写一个函数来返回整数 n 的因数所有可能组合
    // 输入：8
    // 输出： [[2,2,2],[2,4]]
    // 解释： 8 = 2 x 2 x 2 = 2 x 4
    public class GetFactorsSolution {
        List<List<Integer>> result = new ArrayList<>();
        List<Integer> path = new ArrayList<>();

        public List<List<Integer>> getFactors(int n) {
            dfs(2, n); // 从2这个因子开始
            return result;
        }

        private void dfs(int startNum, int remain) {
            if (remain == 1) {
                if (path.size() > 1) { // 如果整个path只有自己，是不加入结果的（一开始的情况，8=1*8不符合）
                    result.add(new ArrayList<>(path)); // deep copy
                }
                return;
            }

            // .1 remain继续分解的情况
            for (int i = startNum; i <= remain; i++) {
                if (i > remain / i) { // 避免重复（2,2,3) (2,3,2)，也在保证因子从小到大
                    break; // 这一个判断也可以在for循环条件处改成i <= Math.sqrt(remain)
                }
                if (remain % i == 0) {
                    path.add(i);
                    dfs(i, remain / i); // 因为有for循环，所以每次的下一轮dfs的start会变化
                    path.remove(path.size() - 1);
                }
            }

            // .2 remain不继续分解的情况，即startNum和remain构成的一个答案
            path.add(remain);
            dfs(remain, 1); // 加入remain自己，余数为1
            path.remove(path.size() - 1);
        }
    }

    // 3. 添加运算符 · add operators
    // 给定一个仅包含数字 0 - 9 的字符串和一个目标值
    // 返回在数字之间添加了 二元 运算符(不是一元)+, - 或 * 之后所有能得到目标值的情况
    public class AddOperatorsSolution {
        List<String> result;

        public List<String> addOperators(String num, int target) {
            result = new ArrayList<>();
            dfs(num, target, 0, "", 0, 0);
            return result;
        }

        private void dfs(String num, int target, int startIndex, String curStr, long curCalResult, long lastFactor) {
            if (startIndex == num.length()) { // 遍历结束
                if (curCalResult == target) { // 计算结果相等就添加
                    result.add(curStr);
                }
                return;
            }

            for (int i = startIndex; i < num.length(); i++) {
                long curNum = Long.parseLong(num.substring(startIndex, i)); // 依次将string里的字符不断增加长度遍历，转成long

                if (startIndex == 0) { // 第一个数不能为标点
                    dfs(num, target, i + 1, "" + curNum, curNum, curNum);
                } else {
                    // 如果是*，需要将当前结果减去上一次已经计算过的facotr（还原回去）
                    dfs(num, target, i + 1, curStr + "*" + curNum,
                            curCalResult - lastFactor + lastFactor * curNum, lastFactor * curNum);
                    dfs(num, target, i + 1, curStr + "+" + curNum, curCalResult + curNum, curNum);
                    dfs(num, target, i + 1, curStr + "-" + curNum, curCalResult - curNum, -curNum);
                }

                if (curNum == 0) { // 1006这种情况，遍历到第一个0时，在前面的代码会添加这个0进dfs
                    // 但是在当前层不会再继续往后遍历了（即没有00或者006的情况出现）
                    // break出循环，不执行之后的分类操作
                    break;
                }
            }
        }
    }

    // 4. 单词矩阵 · Word Squares
    // 给出一系列不重复的单词，找出所有用这些单词能构成的 单词矩阵。
    // 一个有效的单词矩阵是指, 如果从第 k 行读出来的单词和第 k 列读出来的单词相同(0 <= k < max(numRows, numColumns))，那么就是一个单词矩阵.
    // b a l l
    // a r e a
    // l e a d
    // l a d y
    // 单词矩阵每一行和每一列，读出来的单词都是相同的。
    public class WordSquaresSolution {
        List<List<String>> result;
        Map<String, List<String>> prefixes; // 前缀->该前缀对应的所有单词

        public List<List<String>> wordSquares(String[] words) {
            result = new ArrayList<>();
            if (words.length == 0) {
                return result;
            }

            int size = words[0].length(); // 题目设定所有单词长度一致

            prefixes = new HashMap<>();
            getPrefixes(words);

            List<String> curWords = new ArrayList<>();
            for (int i = 0; i < words.length; i++) {
                curWords.add(words[i]);
                dfs(curWords, size);
                curWords.remove(curWords.size() - 1);
            }

            return result;
        }
        private void dfs(List<String> curWords, int size) {
            if (curWords.size() == size) { // 单词长度和单词数量一致为结果，否则就无法行和列都相同了
                result.add(new ArrayList<>(curWords));
                return;
            }

            // 待添加单词所在行
            int rowNum = curWords.size();
            // 待添加单词的前缀
            StringBuilder prefix = new StringBuilder();
            for (int i = 0; i < rowNum; i++) {
                // 假设需要添加第3行的单词，我们需要得到第一个单词的第3列，第二个单词的第三列
                prefix.append(curWords.get(i).charAt(rowNum));
            }
            String prefixStr = prefix.toString();

            if (!prefixes.containsKey(prefixStr)) { // 不存在这个前缀，肯定不会有单词了
                return;
            }
            for (String word : prefixes.get(prefixStr)) {
                curWords.add(word);
                dfs(curWords, size);
                curWords.remove(curWords.size() - 1);
            }
        }
        // 得到 前缀->该前缀对应的所有单词
        private void getPrefixes(String[] words) {
            for (String word : words) {
                for (int i = 0; i < word.length() - 1; i++) {
                    String curPrefix = word.substring(0, i + 1); // 随着for循环依次得到第一个字母；前两个字母；前三个字母
                    prefixes.putIfAbsent(curPrefix, new ArrayList<>());
                    prefixes.get(curPrefix).add(word);
                }
            }
        }
    }

    // 5. 单词拆分II · Word Break II
    // 给一字串s和单词的字典dict,在字串中增加空格来构建一个句子，并且所有单词都来自字典。
    // 返回所有可能的句子。
    // s = "catsanddog", dict = "cat", "cats", "and", "sand", "dog".
    // A solution is "cats and dog", "cat sand dog".
    public class WordBreakSolution {
        List<String> result;
        public List<String> wordBreak(String s, List<String> wordDict) {
            result = new ArrayList<>();
            if (s == null || s.length() == 0 || wordDict == null || wordDict.size() == 0) {
                return result;
            }

            dfs(s, wordDict, 0, "");
            return result;
        }

        private void dfs(String s, List<String> dict, int startIndex, String curStr) {
            if (startIndex == s.length()) {
                result.add(curStr);
                return;
            }

            for (int i = startIndex; i < s.length(); i++) {
                String curWord = s.substring(startIndex, i + 1); // 从startIndex往后依次取单词

                if (!dict.contains(curWord)) {
                    continue;
                }

                String nextStr;
                if (curStr == "") {
                    nextStr = curWord;
                } else {
                    nextStr = curStr + " " + curWord;
                }

                // 当前单词取到了i位置
                dfs(s, dict, i + 1, nextStr);
            }
        }
    }
    public class WordBreakSolution2DP {
        List<String> result = new ArrayList<>();
        Map<Integer, List<Integer>> hash = new HashMap<>(); // i往后是否能够拆分及拆分位置(起始位置->可以拆成单词终止位置)，要保证后面的都可以拆分

        public List<String> wordBreak(String s, List<String> dict) {
            int n = s.length();

            for (int i = 0; i < n; ++i) {
                hash.put(i, new ArrayList<>());
            }

            for (int i = n - 1; i >= 0; i--) {
                for (int j = i + 1; j <= n; ++j) {
                    if (dict.contains(s.substring(i, j))) { // 从i开始到j-1的单词
                        if (j == n || hash.get(j).size() > 0) { // j已经结束或者j后面还可以拆分
                                                                // 如果j后面不能拆分，那这个位置也就没有意义了
                            hash.get(i).add(j);
                        }
                    }
                }
            }

            dfs(s, 0, "");
            return result;
        }
        void dfs(String s, int startIndex, String curStr) {
            if (startIndex == s.length()) {
                result.add(curStr);
                return;
            }

            if (startIndex > 0) { // 非第一个字母，前面先加上一个空格
                curStr += " ";
            }

            for (int nextIndex : hash.get(startIndex)) { // 以startIndex开始的后面可以拆分封的位置
                dfs(s, nextIndex, curStr + s.substring(startIndex, nextIndex));
            }
        }
    }

}
