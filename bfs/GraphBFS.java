package bfs;

import datastructure.*;

import java.util.*;

public class GraphBFS {

    // 1. 图是否是树 Graph Valid Tree
    // n个node, edges无向边列表
    // 输入: n = 5   edges = [[0, 1], [0, 2], [0, 3], [1, 4]]
    // 输出: true.
    public class GraphValidTreeSolution {

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

    // 2. Clone Graph

    public class CloneGraphSolution {

        public UndirectedGraphNode cloneGraph(UndirectedGraphNode node) {
            if (node == null) {
                return null;
            }

            // BFS得到所有点nodes
            ArrayList<UndirectedGraphNode> nodes = getNodes(node);

            // copy nodes. store the old->new mapping information in a hash map
            HashMap<UndirectedGraphNode, UndirectedGraphNode> mapping = new HashMap<>();
            for (UndirectedGraphNode n : nodes) {
                mapping.put(n, new UndirectedGraphNode(n.label));
            }

            // copy neighbors(edges).
            for (UndirectedGraphNode n : nodes) {
                UndirectedGraphNode newNode = mapping.get(n); // 获得新的node(只有val，neighbor为空)
                for (UndirectedGraphNode neighbor : n.neighbors) { // 遍历当前node的所有邻居
                    UndirectedGraphNode newNeighbor = mapping.get(neighbor);
                    newNode.neighbors.add(newNeighbor); // 当前node的邻居里添加新的邻居
                }
            }

            return mapping.get(node);
        }

        private ArrayList<UndirectedGraphNode> getNodes(UndirectedGraphNode node) {
            Queue<UndirectedGraphNode> queue = new LinkedList<UndirectedGraphNode>();
            HashSet<UndirectedGraphNode> set = new HashSet<>();

            queue.offer(node);
            set.add(node);
            while (!queue.isEmpty()) {
                UndirectedGraphNode head = queue.poll();
                for (UndirectedGraphNode neighbor : head.neighbors) {
                    if (!set.contains(neighbor)) {
                        queue.offer(neighbor);
                        set.add(neighbor);
                    }
                }
            }

            return new ArrayList<UndirectedGraphNode>(set); // 构造包含指定collection元素
        }
    }

    // 3. 搜索图中节点 Search Graph Nodes
    // 给定一张无向图，一个节点以及一个目标值，返回距离这个节点最近且值为目标值的节点
    public class SearchNodeSolution {
        /**
         * @param graph a list of Undirected graph node
         * @param values a hash mapping, <UndirectedGraphNode, (int)value>
         * @param node an Undirected graph node
         * @param target an integer
         * @return the a node
         */
        public UndirectedGraphNode searchNode(ArrayList<UndirectedGraphNode> graph,
                                              Map<UndirectedGraphNode, Integer> values,
                                              UndirectedGraphNode node,
                                              int target) {
            if (graph == null || values == null || node == null) {
                return null;
            }

            // BFS分层遍历，找到即为最近的
            Queue<UndirectedGraphNode> queue = new LinkedList<UndirectedGraphNode>();
            Set<UndirectedGraphNode> set = new HashSet<UndirectedGraphNode>();

            queue.offer(node);
            set.add(node);

            while (!queue.isEmpty()) {
                UndirectedGraphNode head = queue.poll();
                if (values.get(head) == target) { // 在map中获取对应值
                    return head;
                }
                for (UndirectedGraphNode neighbor : head.neighbors) {
                    if (!set.contains(neighbor)) {
                        queue.offer(neighbor);
                        set.add(neighbor);
                    }
                }
            }
            // 没找到
            return null;
        }
    }

    // 4. 拓扑排序 Topological Sorting
    // 对于图中的每一条有向边 A -> B , 在拓扑排序中A一定在B之前.
    public class topSortSolution {

        // 步骤1.从队列中获取一个入度为0的顶点
        // 步骤2.获取该顶点边，将边的另一端顶点入度减一，如果为0，也入队列
        // 当B的入度为0时，表示B前面的所有点都输出了
        public ArrayList<DirectedGraphNode> topSort(ArrayList<DirectedGraphNode> graph) {
            ArrayList<DirectedGraphNode> order = new ArrayList<>();

            Map<DirectedGraphNode, Integer> inDegree = new HashMap<>(); // 记录每个点入度
            // 统计入度inDegree
            for (DirectedGraphNode node : graph) {
                for (DirectedGraphNode neighbor : node.neighbors) {
                    if (inDegree.containsKey(neighbor)) { // 如果已存在，inDegree + 1
                        inDegree.put(neighbor, inDegree.get(neighbor) + 1);
                    } else { // 如果不存在，inDegree设置为1
                        inDegree.put(neighbor, 1);
                    }
                }
            }

            Queue<DirectedGraphNode> queue = new LinkedList<>();
            for (DirectedGraphNode node : graph) {
                if (!inDegree.containsKey(node)) { // 不存在，入度为0
                    queue.offer(node);
                    order.add(node);
                }
            }

            while (!queue.isEmpty()) {
                DirectedGraphNode node = queue.poll();
                for (DirectedGraphNode neighbor : node.neighbors) {
                    inDegree.put(neighbor, inDegree.get(neighbor) - 1); // node已输出，邻居入度可以-1
                    if (inDegree.get(neighbor) == 0) {
                        queue.offer(neighbor);
                        order.add(neighbor);
                    }
                }
            }

            return order;
        }
    }



}
