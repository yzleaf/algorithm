package bfs;

import datastructure.*;

import java.util.*;

public class BFSOther {

    // 1. Connected Component in Undirected Graph
    // 输入: {1,2,4#2,1,4#3,5#4,1,2#5,3}
    // 输出: [[1,2,4],[3,5]]
    public class ConnectedSetSolution {
        public List<List<Integer>> connectedSet(List<UndirectedGraphNode> nodes) {

            // 将所有nodes置为未被访问过
            Map<UndirectedGraphNode, Boolean> visited = new HashMap<>();
            for (UndirectedGraphNode node : nodes) {
                visited.put(node, false);
            }

            List<List<Integer>> result = new ArrayList<>();

            for (UndirectedGraphNode node : nodes) {
                if (visited.get(node) == false) { // 当前点未被访问过
                    bfs(node, visited, result);
                }
            }

            return result;
        }
        private void bfs(UndirectedGraphNode node, Map<UndirectedGraphNode, Boolean> visited, List<List<Integer>> result) {
            List<Integer> chunk = new ArrayList<>();
            Queue<UndirectedGraphNode> queue = new LinkedList<>();

            queue.offer(node);
            visited.put(node, true); // 置当前点被访问过

            while (!queue.isEmpty()) {
                UndirectedGraphNode u = queue.poll();
                chunk.add(u.label); // 把当前node值加入chunk联通块中
                for (UndirectedGraphNode neighbor : u.neighbors) {
                    if (visited.get(neighbor) == false) {
                        queue.offer(neighbor);
                        visited.put(neighbor, true);
                    }
                }
            }

            Collections.sort(chunk);
            result.add(chunk);
        }
    }

    // 2. Word Ladder
    // 给出两个单词（start和end）和一个字典，找出从start到end的最短转换序列，输出最短序列的长度
    public class WordLadderSolution {
        public int ladderLength(String start, String end, Set<String> dict) {
            if (start == null || end == null || dict == null) {
                return 0;
            }

            dict.add(end);

            Queue<String> queue = new LinkedList<>();
            queue.offer(start);

            Map<String, Integer> distance = new HashMap<>();
            distance.put(start, 1);

            while (!queue.isEmpty()) {
                String word = queue.poll();
                if (word.equals(end)) {
                    return distance.get(word);
                }

                for (String nextWord : getNextWords(word, dict)) { // 变一个字母的下一个单词
                    if (distance.containsKey(nextWord)) {
                        continue;
                    }
                    queue.offer(nextWord);
                    distance.put(nextWord, distance.get(word) + 1);
                }
            }

            return 0;
        }
        // 改变单词中每个字母（26-1种）组成一个列表
        private List<String> getNextWords(String word, Set<String> dict) {
            List<String> words = new ArrayList<>();
            for (int i = 0; i < word.length(); i++) {
                String left = word.substring(0, i); // 从0取到i-1
                String right = word.substring(i + 1); // 从i+1到最后
                // 变化第i位数据
                for (char ch = 'a'; ch <= 'z'; ch++) {
                    if (word.charAt(i) == ch) {
                        continue;
                    }
                    String nextWord = left + ch + right;
                    if (dict.contains(nextWord)) {
                        words.add(nextWord);
                    }
                }
            }
            return words;
        }
    }

    // 3.

}
