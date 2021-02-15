package dfs;

import java.util.*;

public class GraphDFS {

    // 2. Word Ladder II
    // 给出两个单词（start和end）和一个字典，找出所有从start到end的最短转换序列
    // 输出所有序列
    public class WordLadder2Solution {

        // .1 从end到start做一次BFS，并且把离end的距离都保存在distance中
        // .2 在从start到end做一次DFS，每走一步必须确保离end的distance越来越近

        List<List<String>> ladders;

        public List<List<String>> findLadders(String start, String end, Set<String> dict) {
            ladders = new ArrayList<List<String>>();
            if (dict == null || dict.size() == 0) {
                return ladders;
            }

            // 每个String -> 对应所有变换一个字母的string列表
            Map<String, List<String>> map = new HashMap<String, List<String>>();
            // 每个String -> 离end的距离
            Map<String, Integer> distance = new HashMap<String, Integer>();

            dict.add(start);
            dict.add(end);

            // end -> start的bfs，存储到end的最短距离
            bfs(map, distance, end, start, dict);

            List<String> path = new ArrayList<>(); // 每一条路径
            // start -> end的dfs，输出
            dfs(map, distance, start, end, path);

            return ladders;
        }

        private void dfs(Map<String, List<String>> map, Map<String, Integer> distance,
                         String current, String end, List<String> path) {

            path.add(current);

            if (current.equals(end)) { // 已到终点
                ladders.add(new ArrayList<String>(path));
            } else {
                for (String next : map.get(current)) { // 当前点邻居满足条件的所有都可以再递归dfs
                    if (distance.containsKey(next) && // BFS中distance和map是同步更新的，可以删除这行
                            distance.get(current) == distance.get(next) + 1) { // 保证是最短路径
                        dfs(map, distance, next, end, path);
                    }
                }
            }

            path.remove(path.size() - 1); // 和add配套，必须都要执行（相当于要回到上一层parent节点进行下一个next操作）

        }

        private void bfs(Map<String, List<String>> map, Map<String, Integer> distance,
                         String start, String end, Set<String> dict) {

            Queue<String> queue = new LinkedList<String>();
            queue.offer(start);
            distance.put(start, 0);
            for (String s : dict) {
                map.put(s, new ArrayList<String>());
            }

            while (!queue.isEmpty()) {
                String current = queue.poll();

                List<String> nextList = expand(current, dict); // 找到经过一次变换得到的string列表
                for (String next : nextList) {
                    map.get(next).add(current); // 所有next的列表中都要加入current
                    if (!distance.containsKey(next)) {
                        distance.put(next, distance.get(current) + 1); // next = current离String start(即target end点)的距离+1
                        // 先从current=0开始
                        queue.offer(next);
                    }
                    // 如果是contain情况，说明之前节点BFS处理过（距离肯定更短），不用再管
                }
            } // while
        }

        private List<String> expand(String s, Set<String> dict) { // 经过一次变换得到的string列表
            List<String> expansions = new ArrayList<String>();

            for (int i = 0; i < s.length(); i++) {
                for (char ch = 'a'; ch <= 'z'; ch++) {
                    if (ch != s.charAt(i)) { // 变换当前字母
                        String expansion = s.substring(0, i) + ch + s.substring(i + 1);
                        if (dict.contains(expansion)) {
                            expansions.add(expansion);
                        }
                    }
                } // for char变换
            } // for String 每一位

            return expansions;
        }
    }
}
