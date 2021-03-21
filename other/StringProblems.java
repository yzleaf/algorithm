package other;

import java.util.*;

public class StringProblems {
    // 1. First Position Unique Character
    // 给出一个字符串。找到字符串中第一个不重复的字符然后返回它的下标。如果不存在这样的字符，返回 -1
    public class FirstUniqCharSolution {
        // 先将所有字符扫一遍，保存出现过的字符，然后再扫一遍，遇到第一个出现一次的字符就是答案
        public int firstUniqChar(String s) {
            int[] count = new int[256];

            for (int i = 0; i < s.length(); i++) {
                count[s.charAt(i)] ++;
            }

            for (int i = 0; i < s.length(); i++) {
                if (count[s.charAt(i)] == 1) {
                    return i;
                }
            }

            return -1;
        }
    }

    // 2. substring anagrams
    // Input: s: "cbaebabacd" p: "abc"
    // Output: [0, 6]
    // Explanation:
    // The substring with start index = 0 is "cba", which is an anagram of "abc".
    // The substring with start index = 6 is "bac", which is an anagram of "abc".
    public class FindAnagramsSolution {
        public List<Integer> findAnagrams(String s, String p) {
            List<Integer> result = new ArrayList<>();
            if (s.length() < p.length()) {
                return result;
            }

            int sLength = s.length();
            int pLength = p.length();
            int[] cnt = new int[256]; // 出现的次数
            for (char c : p.toCharArray()) { // 统计P的字母数
                cnt[c] ++;
            }

            int start = 0, end = 0, match = 0;
            while (end < sLength) { // end结尾依次往后+1扫描S数组
                if (cnt[s.charAt(end)] >= 1) { // S中找到与p匹配的一个字母
                    match ++;
                }
                cnt[s.charAt(end)] --; // S中扫过的字母都应该有记录，个数减一个1（匹配过一个就个数减1，不匹配变成-1再往下减）
                end ++;

                // 不断移动sliding window，还原start的数据
                if (end - start == pLength) {
                    if (match == pLength) { // match个数达到长度了
                        result.add(start);
                    }

                    if (cnt[s.charAt(start)] >= 0) { // 有过匹配的数据（可能从1减到0，也有可能从其他数减1但还没到0），match要减回去
                                                     // 没有匹配上的数据在之前做过操作会是0-1=-1，就不会进这个判断了
                        match --;
                    }
                    cnt[s.charAt(start)] ++; // 之前通过end扫过的字母记录过，现在还原回去

                    start ++;
                }
            }

            return result;
        }
    }

    // 3. word abbreviation set
    // 假设你有一个字典和给你一个单词，判断这个单词的缩写在字典中是否是唯一的。
    // 当字典中的其他单词的缩写均与它不同的时候， 这个单词的缩写是唯一的.
    // a) it                      --> it    (没有缩写)
    // b) d|o|g                   --> d1g
    // c) i|nternationalizatio|n  --> i18n
    // d) l|ocalizatio|n          --> l10n
    public class ValidWordAbbr {
        // 注意：如果字典中有两个一样的单词，他们缩写是两个一样的，但他们是唯一的（如果字典里没有重复的单词，其实只要直接判断abbr是不是1即可）
        // 判断待查找的单词在字典中出现的次数是否和缩写在缩写字典中出现的次数一样
        Map<String, Integer> dict = new HashMap<>();
        Map<String, Integer> abbr = new HashMap<>();

        public ValidWordAbbr(String[] dictionary) {
            for (String str : dictionary) {
                dict.put(str, dict.getOrDefault(str, 0) + 1);

                String abbrStr = getAbbr(str);
                abbr.put(abbrStr, abbr.getOrDefault(abbrStr, 0) + 1);
            }
        }

        public boolean isUnique(String word) {
            String abbrStr = getAbbr(word);
            return dict.get(word) == abbr.get(abbrStr);
        }

        private String getAbbr(String str) {
            if (str.length() <= 2) {
                return str;
            }
            return str.charAt(0) + (str.length() - 2) + str.charAt(str.length() - 1) + "";
        }
    }

    // 4. Valid Parentheses
    // 给定一个字符串所表示的括号序列，包含以下字符： '(', ')', '{', '}', '[' and ']'， 判定是否是有效的括号序列。
    // 括号必须依照 "()" 顺序表示， "()[]{}" 是有效的括号，但 "([)]" 则是无效的括号
    public class isValidParenthesesSolution {
        // 遇到左括号进stack，遇到右括号出stack看是否匹配
        public boolean isValidParentheses(String s) {
            Stack<Character> stack = new Stack<>();
            for (char c : s.toCharArray()) {
                if (c == '(' || c == '[' || c == '{') {
                    stack.push(c);
                }

                if (c == ')') {
                    if (stack.isEmpty() || stack.pop() != '(') { // 另一种方法是如果提前先push一个空格到stack里，这里就不用再判断是否empty了
                        return false;
                    }
                }
                if (c == ']') {
                    if (stack.isEmpty() || stack.pop() != '[') {
                        return false;
                    }
                }
                if (c == '}') {
                    if (stack.isEmpty() || stack.pop() != '{') {
                        return false;
                    }
                }
            }
            return stack.isEmpty();
        }
    }

    // 5. 负载均衡器 · Load Balancer
    // 为网站实现一个负载均衡器，提供如下的 3 个功能：
    //     添加一台新的服务器到整个集群中 => add(server_id)。
    //     从集群中删除一个服务器 => remove(server_id)。
    //     在集群中随机（等概率）选择一个有效的服务器 => pick()。
    // 最开始时，集群中一台服务器都没有。每次 pick() 调用你需要在集群中随机返回一个 server_id
    public class LoadBalancerSolution {
        // pick(): 数组中随机选取一个元素可以直接使用随机函数得到一个 [0, 数组大小-1] 的整数即可.
        // add(server_id): 在数组末尾添加这个server_id, 并在哈希表中添加 server_id -> 数组下标 的键值映射
        // remove(server_id): 利用哈希表得到 server_id 的数组下标, 在数组中将它和最末尾的元素交换位置, 然后删除, 并将修改同步到哈希表

        Map<Integer, Integer> position = new HashMap<>(); // service对应的数组所在index
        List<Integer> loadBalancers = new ArrayList<>(); // 存的是service_id

        public void add(int service_id) {
            int m = loadBalancers.size();

            if (!position.containsKey(service_id)) {
                loadBalancers.add(service_id);

                position.put(service_id, m);
            }
        }

        public void remove(int service_id) {
            int m = loadBalancers.size();

            if (position.containsKey(service_id)) {
                int lastItemServiceId = loadBalancers.get(m - 1); // 最后一个load balancer
                int removeIndex = position.get(service_id);

                position.put(lastItemServiceId, removeIndex); // 最后一个元素的位置改成要删除的当前元素的位置
                loadBalancers.set(removeIndex, lastItemServiceId); // 最后一个元素放替换了当前要删除元素

                position.remove(service_id);
                loadBalancers.remove(m - 1); // 删除最后一个元素

            }
        }

        public int pick() {
            Random rand = new Random();
            int m = loadBalancers.size();
            return loadBalancers.get(rand.nextInt(m));
        }
    }

    // 6 字符串解码
    // 394
    // 编码规则为: k[encoded_string]，表示其中方括号内部的 encoded_string 正好重复 k 次。注意 k 保证为正整数。
    // 输入：s = "3[a2[c]]"
    // 输出："accaccacc"
    public class DecodeStringSolution {
        public String decodeString(String s) {
            StringBuilder res = new StringBuilder();
            int number = 0;
            Stack<String> stackStr = new Stack<>();
            Stack<Integer> stackNum = new Stack<>();

            for (char c : s.toCharArray()) {
                if (c == '[') { // 遇到左括号，把之前存的临时数据添加进栈里
                    stackStr.push(res.toString());
                    stackNum.push(number);
                    // 存完需要新建和置0
                    res = new StringBuilder();
                    number = 0;
                } else if (c == ']') { // 计算当前层的结果
                    StringBuilder temp = new StringBuilder();
                    int curCount = stackNum.pop();
                    for (int i = 0; i < curCount; i ++) { // 添加n次当前的String
                        temp.append(res.toString());
                    }
                    res = new StringBuilder(stackStr.pop() + temp.toString()); // 加上之前存进stack里的字符串构成新的字符串

                } else if (c <= '9' && c >= '0') { // 数字 存临时变量
                    number = number * 10 + c - '0';
                } else { // 字母 存临时变量
                    res.append(c);
                }
            }

            return res.toString();
        }
    }
}
