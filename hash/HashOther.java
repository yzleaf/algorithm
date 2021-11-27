package hash;

import java.util.*;

public class HashOther {
    // 1. Anagrams
    // 给出一个字符串数组S，找到其中所有的乱序字符串(Anagram)
    // 如果是乱序字符串：那么他存在一个字母集合相同，但顺序不同的字符串也在S中
    // 输入:["lint", "intl", "inlt", "code"]
    // 输出:["lint", "inlt", "intl"]
    public class AnagramsSolution {
        // 方法1 排序+Hashmap
        public List<String> anagrams(String[] strs) {
            List<String> result = new ArrayList<>();
            if (strs == null || strs.length == 0) {
                return result;
            }

            // Map存放：排序后的String -> 所有拥有这些字母的原始String
            Map<String, ArrayList<String>> map = new HashMap<>();
            for (int i = 0; i < strs.length; i++) {
                // string转化为char array并按字母排序
                char[] arr = strs[i].toCharArray();
                Arrays.sort(arr);
                String s = String.valueOf(arr);

                // 所有拥有相同字母的string排序后就一致了
                map.putIfAbsent(s, new ArrayList<String>()); // 如果没有包括这个排序的string 需要新建
                // 找到String对应的value并且添加当前字符串（拥有相同的排序string，即乱序字符串）
                map.get(s).add(strs[i]);
            }

            for (Map.Entry<String, ArrayList<String>> entry : map.entrySet()) {
                if (entry.getValue().size() >= 2) {
                    result.addAll(entry.getValue());
                }
            }

            return result;
        }

        // 方法2 Hash
        public ArrayList<String> anagrams2(String[] strs) {
            ArrayList<String> result = new ArrayList<>();
            if (strs == null || strs.length == 0) {
                return result;
            }

            // Map存放String每个字母的hash -> 所有拥有这些字母的String
            Map<Integer, ArrayList<String>> map = new HashMap<>();

            for (String str : strs) {
                int[] count = new int[26]; // 记录每个字母出现的次数，判断是不是相同的乱序字符串
                for (int i = 0; i < str.length(); i++) {
                    count[str.charAt(i) - 'a'] ++;
                }

                int hash = getHash(count);

                map.putIfAbsent(hash, new ArrayList<String>()); // 没包含，要新建
                map.get(hash).add(str);
            }

            for (ArrayList<String> temp : map.values()) {
                if (temp.size() >= 2) {
                    result.addAll(temp);
                }
            }

            return result;
        }
        private int getHash(int[] count) {
            int hash = 0;
            int a = 378551;
            int b = 63689;
            for (int num : count) {
                hash = hash * a + num;
                a = a * b;
            }
            return hash;
        }
    }

    // 2. Longest Consecutive Sequence
    // 128
    // 给定一个未排序的整数数组，找出最长连续序列的长度（每个相差1）
    // 输入 : [100, 4, 200, 1, 3, 2]
    // 输出 : 4
    // 解释 : 这个最长的连续序列是 [1, 2, 3, 4]. 返回所求长度 4
    public class LongestConsecutiveSolution {

        public int longestConsecutive(int[] nums) {
            HashSet<Integer> hash = new HashSet<>();
            for (int i = 0; i < nums.length; i++) {
                hash.add(nums[i]);
            }

            int longest = 0;
            for (int i = 0; i < nums.length; i++) {
                if (hash.contains(nums[i])){
                    hash.remove(nums[i]);

                    // 因为找连续的序列，所以只需要看是否存在+1 和 -1
                    int down = nums[i] - 1;
                    while (hash.contains(down)) {
                        hash.remove(down); // 避免i遍历到其他位置的时候有这个相邻的数重复计算
                        down--;
                    }
                    int up = nums[i] + 1;
                    while (hash.contains(up)) {
                        hash.remove(up);
                        up++;
                    }

                    longest = Math.max(longest, up - down - 1); // 最后跳出循环后的数up+1 down-1，所以个数为up-down-1
                }
            }
            return longest;
        }
    }

    // 715. Range Module
    // 查询，加入，删除[left, right)区间的数
    public class RangeModuleSolution {
        // [begin, end)
        TreeMap<Integer, Integer> interval;
        public RangeModule() {
            interval = new TreeMap<>();
        }

        public void addRange(int left, int right) {
            // 用Integer属性是因为如果没有小于left的最大数的话会返回null
            Integer start = interval.floorKey(left);
            Integer end = interval.floorKey(right);
            if (start != null && interval.get(start) >= left) {
                left = start;
            }
            if (end != null && interval.get(end) > right) {
                right = interval.get(end); // 注意要用get(end)
            }
            interval.put(left, right);
            // 删除相交的多余数组
            // left已经覆盖了原来的start，所以不能删除left
            interval.subMap(left, false, right, true).clear();
            // clear是删除
        }

        public boolean queryRange(int left, int right) {
            Integer start = interval.floorKey(left);
            if (start != null && interval.get(start) >= right) {
                return true;
            }
            return false;
        }

        public void removeRange(int left, int right) {
            Integer start = interval.floorKey(left);
            Integer end = interval.floorKey(right);

            // 这里先判断end是因为如果start和end相等的话，先判断start会直接修改了interval里对应的value
            if (end != null && interval.get(end) > right) {
                interval.put(right, interval.get(end)); // 注意要用get(end)
            }
            if (start != null && interval.get(start) > left) {
                interval.put(start, left);
            }
            // 并不像add中left和right会发生变化，这里不变
            // 从left开始>=它的，到<right的（不能取等号，因为right是右边开区间）
            interval.subMap(left, true, right, false).clear();
        }
    }
}
