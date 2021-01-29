package heap;

import java.util.*;

public class TreeMapProblems {
    // 1. The Skyline Problem
    // 218
    // 每个建筑物的几何信息由数组 buildings 表示，其中三元组 buildings[i] = [lefti, righti, heighti] 表示
    // 起始位置，终止位置，高度
    // 输出天际线
    public class GetSkylineSolution {
        public List<List<Integer>> getSkyline(int[][] buildings) {
            // 使用扫描线，从左至右扫过。如果遇到左端点，将高度入堆，如果遇到右端点，则将高度从堆中删除。
            // 使用 last 变量记录上一个转折点

            // minHeap记录（当前位置，高度）
            // 按当前位置从小到大排序，如果位置相同按高度排序（都是起始位置，高度大的先进；都是终止位置，高度小的先进）
            // 高度小于0表示起始位置，高度大于0表示终止位置（用正负进行区分）
            PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> a[0] != b[0] ? a[0] - b[0] : a[1] - b[1]);
            for (int[] building : buildings) {
                minHeap.offer(new int[]{building[0], -building[2]});
                minHeap.offer(new int[]{building[1], building[2]});
            }

            List<List<Integer>> result = new ArrayList<>();

            // map记录（高度，对应个数）
            // 出现一个起始位置，这个高度的个数加一；出现一个终止位置，这个高度的个数减一
            TreeMap<Integer, Integer> heights = new TreeMap<>((a, b) -> b - a); // a和b指的是两次进入的key（排序方法）
            heights.put(0, 1); // 避免到最后的时候出现空指针
            int left = 0, preHeight = 0;
            while (!minHeap.isEmpty()) {
                int[] arr = minHeap.poll();
                if (arr[1] < 0) { // 起始位置
                    heights.put(-arr[1], heights.getOrDefault(-arr[1], 0) + 1);
                } else { // 终止位置
                    heights.put(arr[1], heights.get(arr[1]) - 1);
                    if (heights.get(arr[1]) == 0) {
                        heights.remove(arr[1]);
                    }
                }
                int curMaxHeight = heights.firstKey();;
                if (curMaxHeight != preHeight) {
                    left = arr[0];
                    preHeight = curMaxHeight;
                    result.add(Arrays.asList(left, preHeight));
                }
            }

            return result;
        }
    }

    // 2. Top K Frequent Words
    // 给一个单词列表，求出这个列表中出现频次最高的K个单词
    // 越高词频排在越前面，如果两个单词出现的次数相同，则词典序小的排在前面
    public class TopKFrequentWordsSolution {
        class Pair {
            String key; // 列表
            int value; // 出现次数

            Pair(String key, int value) {
                this.key = key;
                this.value = value;
            }
        }
        // 时间O(nlogk) 空间O(n+k)
        // 使用哈希表记录每个单词出现的次数, 然后需要将单词按照 次数(主要关键词)和字典序(次要关键字) 排序, 取出前k个即可
        public String[] topKFrequentWords(String[] words, int k) {
            if (k == 0) {
                return new String[0];
            }

            Map<String, Integer> counter = new HashMap<>(); // string和对应出现的次数
            for (String word : words) {
                if (counter.containsKey(word)) {
                    counter.put(word, counter.get(word) + 1);
                } else {
                    counter.put(word, 1);
                }
            }
            // 最小堆，value小的先淘汰，留大的
            // 字典序靠后的先淘汰，留字典序靠前的
            PriorityQueue<Pair> minHeap = new PriorityQueue<>(k, (a, b) -> a.value != b.value
                                                                         ? a.value - b.value
                                                                         : b.key.compareTo(a.key));
            for (String word : counter.keySet()) {
                minHeap.offer(new Pair(word, counter.get(word)));
                if (minHeap.size() > k) {
                    minHeap.poll();
                }
            }

            // 输出
            String[] result = new String[k];
            int index = k - 1;
            while (!minHeap.isEmpty()) {
                result[index] = minHeap.poll().key;
                index --;
            }
            return result;
        }

        // 最优方法 quick select
        // Time: O(n + klogk)
        // Space: O(n) HashMap
        // 用String[]数组存所有的key，用O(n)快速选择出前k个大的，对k个String排序（k logk)
    }
}
