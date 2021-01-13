package bfs;

import java.util.*;

public class GraphBFS {

    // 1. 图是否是树 Graph Valid Tree
    // n个node, edges无向边列表
    // 输入: n = 5   edges = [[0, 1], [0, 2], [0, 3], [1, 4]]
    // 输出: true.
    public class GraphValidTree {

        public boolean validTree(int n, int[][] edges) {
            if (n == 0) {
                return false;
            }

            if (edges.length != n - 1) { // 树的边数一定为n-1
                return false;
            }

            Map<Integer, Set<Integer>> graph = initializeGraph(n, edges); // 每个点 <-> 所有相邻点

            Queue<Integer> queue = new LinkedList<>();
            Set<Integer> hash = new HashSet<>();

            queue.offer(0); // 0号节点先进入
            hash.add(0);

            while (!queue.isEmpty()) {
                int node = queue.poll();
                for (Integer neighbor: graph.get(node)) { // 取到node的所有相邻节点
                    if (hash.contains(neighbor)) {
                        continue;
                    }
                    hash.add(neighbor);
                    queue.offer(neighbor);
                }
            }

            return (hash.size() == n); // 是否所有点都联通了
            // 如果n个点都联通，且edge为n-1，就是树
        }
    }

    private Map<Integer, Set<Integer>> initializeGraph(int n, int[][] edges) {
        Map<Integer, Set<Integer>> graph = new HashMap<>();
        for (int i = 0; i < n; i++) {
            graph.put(i, new HashSet<Integer>());
        }

        for (int i = 0; i < edges.length; i++) {
            int u = edges[i][0];
            int v = edges[i][1];
            graph.get(u).add(v);
            graph.get(v).add(u);
        }

        return graph;
    }

}
