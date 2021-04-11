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

    // 重新安排行程
    // 332
    // 给定一个机票的字符串二维数组 [from, to]，子数组中的两个成员分别表示飞机出发和降落的机场地点，对该行程进行重新规划排序。
    // 所有这些机票都属于一个从 JFK 出发的先生，所以该行程必须从 JFK 开始。
    public class FindItinerarySolution {
        // 欧拉通路，题目给出的测试例肯定会构成这个通路，我们只用找出即可

        // 由于题目中说必然存在一条有效路径(至少是半欧拉图)，所以算法不需要回溯（加入到结果集里的元素不需要删除）
        // 整个图最多存在一个死胡同(出度和入度相差1），且这个死胡同一定是最后一个访问到的，否则无法完成一笔画。
        // DFS的调用其实是一个拆边的过程（既每次递归调用删除一条边，所有子递归都返回后，再将当前节点加入结果集保证了结果集的逆序输出），一定是递归到这个死胡同（没有子递归可以调用）后递归函数开始返回。所以死胡同是第一个加入结果集的元素。

        Map<String, PriorityQueue<String>> hash = new HashMap<String, PriorityQueue<String>>(); // 出发 -> 可到达的所有点
        List<String> itinerary = new LinkedList<>();

        public List<String> findItinerary(List<List<String>> tickets) {

            for (List<String> ticket : tickets) {
                String src = ticket.get(0); // 出发地
                String dst = ticket.get(1); // 目的地
                hash.putIfAbsent(src, new PriorityQueue<String>()); // 目的地构成的最小堆（按字典序排列）
                hash.get(src).offer(dst);
            }

            dfs("JFK");
            Collections.reverse(itinerary); // 先添加进去的是死胡同，最后应该逆序
            return itinerary;
        }
        private void dfs(String curr) {
            while (hash.containsKey(curr) && hash.get(curr).size() > 0) { // 还存在路径
                String temp = hash.get(curr).poll(); // poll出来（表示已经用了这张票了，需要删除）
                dfs(temp);
            }
            itinerary.add(curr); // 当curr这个点没有新的dst可以前往的时候，它就是终点，可以添加进数组
        }
    }
}
