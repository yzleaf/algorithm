package bfs;

import datastructure.*;

import java.util.*;

public class BFSOther {

    // 1. Connected Component in Undirected Graph
    // 323
    // 找出无向图中所有的连通块，图中的每个节点包含一个label属性和一个邻接点的列表
    // 输入: {1,2,4#2,1,4#3,5#4,1,2#5,3} ------> 1和2/4相连，2和1/4相连，3和5相连...
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

    // 2. Word Ladder（直接看下面一题的解法）
    // 给出两个单词（start和end）和一个字典，找出从start到end的最短转换序列，输出最短序列的长度
    // 输入：start ="hit"，end = "cog"，dict =["hot","dot","dog","lot","log"]
    // 输出：5
    // 解释："hit"->"hot"->"dot"->"dog"->"cog"
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
                if (word.equals(end)) { // 到达终点
                    return distance.get(word);
                }

                for (String nextWord : getNextWords(word, dict)) { // 变一个字母的下一个单词
                    if (distance.containsKey(nextWord)) { // 之前已经处理过
                        continue;
                    }
                    queue.offer(nextWord);
                    distance.put(nextWord, distance.get(word) + 1); // 当前变换次数+1
                }
            }

            return 0;
        }
        // 改变单词中每个字母（26-1=25种）组成一个列表
        // 返回当前字符串经过一次改变后在字典中的字符串
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

    // 2. Word Ladder解法2
    // 127
    public class WordLadderSolution2 {
        public int ladderLength(String start, String end, List<String> dict) {
            if (start == null || end == null || dict == null) {
                return 0;
            }

            Set<String> dictSet = new HashSet<>(); // 把List转为Set更容易操作contains命令
            for (String str : dict) {
                dictSet.add(str);
            }

            Queue<String> queue = new LinkedList<>();
            queue.offer(start);

            Set<String> visited = new HashSet<>(); // 之前是否处理过该String
            int steps = 0;

            while (!queue.isEmpty()) {
                steps ++;
                int size = queue.size();
                for (int i = 0; i < size; i++) {
                    String word = queue.poll();
                    if (word.equals(end)) { // 到达终点
                        return steps;
                    }

                    for (String nextWord: getNextWords(word, dictSet)) { // 变一个字母的下一个单词
                        if (visited.contains(nextWord)) { // 之前已经处理过
                            continue;
                        }
                        queue.offer(nextWord);
                        visited.add(nextWord);
                    }
                }
            }

            return 0;
        }
        // 改变单词中每个字母（26-1=25种）组成一个列表
        // 返回当前字符串经过一次改变后在字典中的字符串
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

    // 752 open the lock
    // 锁的初始数字为 '0000'，每个拨轮可以自由旋转变化1
    // 列表 deadends 是会被锁定无法再转，target 是最终数字
    // 返回最小旋转次数
    class openLockS {
        public int openLock(String[] deadends, String target) {

            Set<String> deadendSet = new HashSet<>();
            for (String s : deadends) {
                deadendSet.add(s);
            }

            Set<String> visited = new HashSet<>();
            int step = 0;

            Queue<String> q = new LinkedList<>();
            q.offer("0000");
            visited.add("0000");

            while (!q.isEmpty()){
                int size = q.size();
                for (int i = 0; i < size; i++) {
                    String curr = q.poll();
                    if (deadendSet.contains(curr)) {
                        continue;
                    }
                    if (curr.equals(target)) {
                        return step;
                    }

                    for (int j = 0; j < 4; j++) {
                        String nextUp = plusOne(curr, j); // 任意一位+1
                        if (!visited.contains(nextUp)) {
                            visited.add(nextUp);
                            q.offer(nextUp);
                        }
                        String nextDown = minusOne(curr, j); // 任意一位-1
                        if (!visited.contains(nextDown)) {
                            visited.add(nextDown);
                            q.offer(nextDown);
                        }

                    }
                }

                step ++;
            }

            return -1; // 解不了锁
        }

        private String plusOne(String s, int position) {
            char[] charArr = s.toCharArray();
            if (charArr[position] == '9') {
                charArr[position] = '0';
            } else {
                charArr[position] += 1;
            }

            return charArr.toString();
        }
        private String minusOne(String s, int position) {
            char[] charArr = s.toCharArray();
            if (charArr[position] == '0') {
                charArr[position] = '9';
            } else {
                charArr[position] -= 1;
            }

            return charArr.toString();
        }
    }
}
