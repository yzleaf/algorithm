package dfs;

import java.util.*;

public class HighFrequency {
    // 1. 电话号码的字母组合 · Letter Combinations of a Phone Number
    // 17
    // 输入一系列数字串，得到所有它可能代表的字母
    public class LetterCombinationPhoneNumberSolution {
        List<String> result;
        String[] phone = {"", "", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"}; // 九宫格输入法

        public List<String> letterCombinations(String digits) {

            result = new ArrayList<>();
            if (digits.length() == 0) {
                return result;
            }

            dfs(digits, 0, "");

            return result;
        }

        // str当前的字符串内容
        private void dfs(String digits, int startIndex, String str) {

            if (startIndex == digits.length()) { // index超出了length范围返回
                result.add(str);
                return;
            }

            int num = digits.charAt(startIndex) - '0'; // 得到当前的数字
            for (char c : phone[num].toCharArray()) { // 这个数字对应的每一个字母
                dfs(digits, startIndex + 1, str + c);
            }
            // 增加的c会不断被替换，所以不用想list一样add和remove操作
        }
    }

    // 2. 因式分解 · Factorization
    // 254
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

    // 24点游戏
    // 你有 4 张写有 1 到 9 数字的牌。你需要判断是否能通过 *，/，+，-，(，) 的运算得到 24。
    // 679
    public class Computer24Solution {
        public boolean compute24(int[] nums) {
            // 转换成double类型来判断（有除法）
            double[] numsDouble = new double[nums.length];
            for (int i = 0; i < nums.length; i ++) {
                numsDouble[i] = (double)nums[i];
            }
            return compute(numsDouble, 4);
        }
        // 枚举任意两个数，即i和j，然后将这两个数进行运算，每次将当前的num[i]替换成两数运算后的新数字，并将数字规模减一
        // 每次搜索完成后，需要将num[i]和num[j]还原。
        public boolean compute(double[] nums, int n) {
            if (n == 1) { // 还剩一个数，判断是否为24
                if (Math.abs(nums[0] - 24) < 1e-6) {
                    return true;
                }
            }

            for (int i = 0; i < n; i ++) { // 取第一个数
                for (int j = i + 1; j < n; j++) { // 取第二个数 （两层for循环已经可以把所有情况都遍历过了），因为有一个总数n，所以哪怕在这一层没有取到第一个数，在下一层dfs也会取到
                    double a = nums[i];
                    double b = nums[j];
                    nums[j] = nums[n - 1]; // 把这个已经用完的第j个数替换成最后一个数（在后续DFS中就抹掉最后一个数了）

                    nums[i] = a + b;
                    if (compute(nums, n - 1)) {
                        return true;
                    }

                    nums[i] = a - b;
                    if (compute(nums, n - 1)) {
                        return true;
                    }

                    nums[i] = b - a;
                    if (compute(nums, n - 1)) {
                        return true;
                    }

                    nums[i] = a * b;
                    if (compute(nums, n - 1)) {
                        return true;
                    }

                    // a / b or b / a
                    if (b != 0) {
                        nums[i] = a / b;
                        if (compute(nums, n - 1)) {
                            return true;
                        }
                    }

                    if (a != 0) {
                        nums[i] = b / a;
                        if (compute(nums, n - 1)) {
                            return true;
                        }
                    }

                    // 回溯，要把这两个数还原
                    nums[i] = a;
                    nums[j] = b;
                }
            }

            return false;
        }
    }

    // 递增子序列
    // 491
    // 找到所有该数组的递增子序列，递增子序列的长度至少是 2
    // 输入：[4, 6, 7, 7]
    // 输出：[[4, 6], [4, 7], [4, 6, 7], [4, 6, 7, 7], [6, 7], [6, 7, 7], [7,7], [4,7,7]]
    class IncreaseSubSequencesSolution {
        List<List<Integer>> results;
        public List<List<Integer>> findSubsequences(int[] nums) {
            results = new ArrayList<>();

            dfs(nums, 0, Integer.MIN_VALUE, new ArrayList<Integer>());
            return results;
        }
        // lastNum是上一层加入的最后一个数，当前层加入的只要比这个数大，就是递增
        private void dfs(int[] nums, int startIndex, int lastNum, List<Integer> subSequence) {
            if (subSequence.size() >= 2) { // 大于2的序列才能返回
                results.add(new ArrayList<Integer>(subSequence));
            }


            Set<Integer> hash = new HashSet<>();
            for (int i = startIndex; i < nums.length; i++) {
                if (nums[i] < lastNum) { // 比上一个数小，这个序列就是递增序列，直接取消这一层的操作
                    continue;
                }

                if (hash.contains(nums[i])) { // 因为序列是乱序的，不能按照常规方法判断nums[i] != nums[i-1]
                    continue;
                }
                hash.add(nums[i]);

                subSequence.add(nums[i]);
                dfs(nums, i + 1, nums[i], subSequence);
                subSequence.remove(subSequence.size() - 1);
            }

        }
        // 方法2
    }

    // Zuma Game
    // 488
    // 球被涂上了红（R）、黄（Y）、蓝（B）、绿（G）和白（W）这么几种颜色，同时你也拥有几个球。
    // 每一次，从你拥有的球当中拿出一个，插入到当前行当中（包括最左边和最右边）。然后，如果有三个或更多的同色球挨在一起，然后就消除这几个球。一直这么做直到没有更多的球可以消除。
    // 找到最少的需要插入的球的数量，使得所有的球都可以被删除。如果不能删除所有的球，那么就返回-1。
    // 输入: "WWRRBBWW", "WRBRW"
    // 输出: 2
    // 解释: WWRRBBWW -> WWRR[R]BBWW -> WWBBWW -> WWBB[B]WW -> WWWW -> empty
    public class ZumaGameSolution {
        public int findMinStep(String board, String hand) {
            int count[] = new int[128]; // 记录手上有的各种球的个数
            for (char c : hand.toCharArray()) {
                count[c]++;
            }

            return aux(board, count);
        }
        private int aux(String s, int[] count) { // 消除s所需要的最小长度
            if ("".equals(s)) {
                return 0;
            }
            int result = Integer.MAX_VALUE;
            for (int i = 0; i < s.length();) { // 依次遍历每一段有相同元素的区间，然后递归到子串
                int j = i; // j保存起点
                while (i < s.length() && s.charAt(i) == s.charAt(j)) { // 得到j->i这段区间
                    i ++;
                }

                int needed = 3 - (i - j); // 需要几个球能够消掉这段区间
                if (count[s.charAt(j)] >= needed) { // 手上的球足够消掉当前区间的球，可进入下一层子串递归
                    int used = needed <= 0 ? 0 : needed; // 区间本身长度超过3，就不需要手上的球加入来消除了
                    count[s.charAt(j)] -= used; // 用掉了这些球

                    int subResult = aux(s.substring(0, j) + s.substring(i), count); // 消除j->i当前段以后进入子串递归
                    if (subResult >= 0) {
                        result = Math.min(result, used + subResult);
                    }

                    count[s.charAt(j)] += used; // 还原用掉的球
                }
            }

            return result == Integer.MAX_VALUE ? -1 : result;
        }
    }
    // 测试用例一："WWRRGGRRWWRRGGRRWW", "GG"
    // 无论怎么插入，都无法完全消除，结果应是-1。
    // 测试用例二："RRWWRRBBRR", "WB"
    // "R(B)RWWRRBBRR" -> "R(B)RWW(W)RRBBRR" -> ""
    // 结果应是2。
    // 这两个测试用例用这种方法过不了，用例1可以不断地判断当前串是否能消来做，用例2不知道怎么做

}
