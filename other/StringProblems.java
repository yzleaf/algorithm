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
            int[] sum = new int[256];
            for (char c : p.toCharArray()) { // 统计P的字母数
                sum[c] ++;
            }

            int start = 0, end = 0, match = 0;
            while (end < sLength) { // end结尾依次往后扫S数组
                if (sum[s.charAt(end)] >= 1) { // S中找到与p匹配的一个字母
                    match ++;
                }
                sum[s.charAt(end)] --; // S中扫过的字母都应该有记录，个数减一个1（匹配就个数减1，不匹配变成-1再往下减）
                end ++;

                // 不断移动sliding window，还原start的数据
                if (end - start == pLength) {
                    if (match == pLength) { // match个数达到长度了
                        result.add(start);
                    }

                    if (sum[s.charAt(start)] >= 0) { // 有过匹配的数据，match要减回去，负数表示本来就没有匹配上的数据
                        match --;
                    }
                    sum[s.charAt(start)] ++; // 之前通过end扫过的字母记录过，现在还原回去

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
}
