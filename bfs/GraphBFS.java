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
                for (Integer neighbor: graph.get(node)) { // 取到当前node的所有相邻节点
                    if (hash.contains(neighbor)) { // hash表里包含，表示之前访问过
                        continue;
                    }
                    hash.add(neighbor);
                    queue.offer(neighbor);
                }
            }

            return (hash.size() == n); // 是否n个点都进入hash，即所有点都联通了
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
    // 返回一个经过深度拷贝的新图. 新图和原图具有同样的结构, 并且对新图的任何改动不会对原图造成任何影响
    public class CloneGraphSolution {

        public UndirectedGraphNode cloneGraph(UndirectedGraphNode node) {
            if (node == null) {
                return null;
            }

            // BFS得到所有点nodes
            ArrayList<UndirectedGraphNode> nodes = getNodes(node);

            // copy nodes. store the old->new mapping information in a hash map
            HashMap<UndirectedGraphNode, UndirectedGraphNode> mapping = new HashMap<>();
            for (UndirectedGraphNode n : nodes) { // 遍历原始表的所有点
                mapping.put(n, new UndirectedGraphNode(n.label));
            }

            // copy neighbors(edges).
            for (UndirectedGraphNode n : nodes) { // 遍历原始表的所有点
                UndirectedGraphNode newNode = mapping.get(n); // 根据原始node获得新的node(只有val，neighbor为空)

                for (UndirectedGraphNode neighbor : n.neighbors) { // 遍历当前node的所有邻居
                    UndirectedGraphNode newNeighbor = mapping.get(neighbor); // 根据原始neighbor获得新的neighbor
                    newNode.neighbors.add(newNeighbor); // 新的node的邻居里添加新的邻居
                }
            }

            return mapping.get(node);
        }

        private ArrayList<UndirectedGraphNode> getNodes(UndirectedGraphNode node) {
            Queue<UndirectedGraphNode> queue = new LinkedList<UndirectedGraphNode>();
            HashSet<UndirectedGraphNode> hash = new HashSet<>();

            queue.offer(node);
            hash.add(node);

            while (!queue.isEmpty()) {
                UndirectedGraphNode head = queue.poll();
                for (UndirectedGraphNode neighbor : head.neighbors) {
                    if (!hash.contains(neighbor)) {
                        queue.offer(neighbor);
                        hash.add(neighbor);
                    }
                }
            }

            return new ArrayList<UndirectedGraphNode>(hash); // 构造包含指定collection元素
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
            Set<UndirectedGraphNode> hash = new HashSet<UndirectedGraphNode>();

            queue.offer(node);
            hash.add(node);

            while (!queue.isEmpty()) {
                UndirectedGraphNode head = queue.poll();
                if (values.get(head) == target) { // 在map中获取对应值
                    return head;
                }
                for (UndirectedGraphNode neighbor : head.neighbors) {
                    if (!hash.contains(neighbor)) {
                        queue.offer(neighbor);
                        hash.add(neighbor);
                    }
                }
            }
            // 没找到
            return null;
        }
    }

    // 4. 拓扑排序 Topological Sorting
    // 针对给定的有向图找到任意一种拓扑排序的顺序
    // 对于图中的每一条有向边 A -> B , 在拓扑排序中A一定在B之前.
    public class TopSortSolution {

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

    // 5. 课程表 Course Schedule
    // n门课需要选，记为0到n-1. 要学习课程0你需要先学习课程1，表示为[0,1]
    // 给定n门课以及他们的先决条件，判断是否可能完成所有课程？
    public class CourseScheduleSolution {
        
        public boolean canFinish(int numCourses, int[][] prerequisites) {
            List[] edges = new ArrayList[numCourses];
            int[] inDegree = new int[numCourses];

            for (int i = 0; i < numCourses; i++) {
                edges[i] = new ArrayList<Integer>();
            }

            // 建立图
            for (int i = 0; i < prerequisites.length; i++) {
                inDegree[prerequisites[i][0]]++; // 第一个点为原始课程，要修这门需要先修其他的，所以多一门其他课程这门课入度+1
                edges[prerequisites[i][1]].add(prerequisites[i][0]); // 在其他的先修课程列表中添加了这门课（即其他先修课修完可以修这门课）
            }

            Queue<Integer> queue = new LinkedList<>();
            for (int i = 0; i < inDegree.length; i++) { // 入度为0，没有先修课的课程，先进队列
                if (inDegree[i] == 0) {
                    queue.offer(i);
                }
            }

            int countCourses = 0;
            while (!queue.isEmpty()) {
                int course = queue.poll();
                countCourses++; // 修完course这门课，count+1
                int n = edges[course].size(); // course是先修课，修完可以修其他哪些课（这些课的indgree减一）
                for (int i = 0; i < n; i++) {
                    int pointer = (int) edges[course].get(i); // 可以修的下一门课
                    inDegree[pointer]--;
                    if (inDegree[pointer] == 0) {
                        queue.offer(pointer);
                    }
                }
            }

            return countCourses == numCourses; // 是否全部修完
        }
    }

    // 6. 安排课程 Course Schedule II
    // 给你课程的总数量和一些前置课程的需求，返回你为了学完所有课程所安排的学习顺序
    public class CourseSchedule2Solution {
        /**
         * @param numCourses a total of n courses
         * @param prerequisites a list of prerequisite pairs
         * @return the course order
         */
        public int[] findOrder(int numCourses, int[][] prerequisites) {
            List[] edges = new ArrayList[numCourses];
            int[] inDegree = new int[numCourses];

            for (int i = 0; i < numCourses; i++) {
                edges[i] = new ArrayList<Integer>();
            }

            for (int i = 0; i < prerequisites.length; i++) {
                inDegree[prerequisites[i][0]]++;
                edges[prerequisites[i][1]].add(prerequisites[i][0]);
            }

            Queue<Integer> queue = new LinkedList<>();
            for (int i = 0; i < inDegree.length; i++) { // 入度为0，没有先修课的课程，先进队列
                if (inDegree[i] == 0) {
                    queue.offer(i);
                }
            }

            int countCourses = 0;
            int[] order = new int[numCourses];

            while (!queue.isEmpty()) {
                int course = queue.poll();
                order[countCourses] = course;
                countCourses++; // 修完course这门课，count+1

                int n = edges[course].size(); // course是先修课，修完可以修其他哪些课
                for (int i = 0; i < n; i++) {
                    int pointer = (int) edges[course].get(i); // 可以修的下一门课
                    inDegree[pointer]--;
                    if (inDegree[pointer] == 0) {
                        queue.add(pointer);
                    }
                }
            }

            if (countCourses == numCourses) {
                return order;
            }

            return new int[0];
        }
    }

    // 7. 序列重构 Sequence Reconstruction
    // 444
    // 判断是否序列org能唯一地由seqs重构得出
    // 重构：一个最短的序列使得所有seqs里的序列都是它的子序列
    // 序列seqs [1,2], [1,3], [2,3] 可以唯一重构出 org[1,2,3]
    // 序列seqs [1,2], [1,3] 不能唯一重构[1,2,3]，因为还有[1,3,2]
    public class SequenceReconstructionSolution {
        /**
         * @param org: a permutation of the integers from 1 to n
         * @param seqs: a list of sequences
         * @return true if it can be reconstructed only one or false
         */
        public boolean sequenceReconstruction(int[] org, int[][] seqs) {
            // 先构建出唯一的序列，再比较是否和给定的org相同
            Map<Integer, Set<Integer>> graph = buildGraph(seqs);
            List<Integer> topoOrder = getTopoOrder(graph);

            if (topoOrder == null || topoOrder.size() != org.length) {
                return false;
            }

            for (int i = 0; i < org.length; i++) { // 比较每一位数据
                if (org[i] != topoOrder.get(i)) {
                    return false;
                }
            }
            return true;
        }

        // Integer A -> 在它后面的所有数集合
        private Map<Integer, Set<Integer>> buildGraph(int[][] seqs) {
            Map<Integer, Set<Integer>> graph = new HashMap<>();
            for (int[] seq : seqs) { // 把所有seqs中的点都放入Map
                for (int i = 0; i < seq.length; i++) {
                    if (!graph.containsKey(seq[i])) { // 如果不包含当前点就添加进graph
                        graph.put(seq[i], new HashSet<Integer>());
                    }
                }
            }
            for (int[] seq : seqs) {
                for (int i = 1; i < seq.length; i++) {
                    graph.get(seq[i - 1]).add(seq[i]); // 取到基准点，把相邻的后一个点添加进去
                }
            }
            return graph;
        }
        private Map<Integer, Integer> getInDegrees(Map<Integer, Set<Integer>> graph) {
            Map<Integer, Integer> inDegrees = new HashMap<>();

            for (Integer node : graph.keySet()) { // 遍历所有key
                inDegrees.put(node, 0); // 设置每个点的入度为0
            }
            for (Integer node : graph.keySet()) {
                for (Integer neighbor : graph.get(node)) { // 遍历在node点后面的所有邻居
                    inDegrees.put(neighbor, inDegrees.get(neighbor) + 1); // 邻居inDegree+1，表示node在邻居前面
                }
            }
            return inDegrees;
        }

        private List<Integer> getTopoOrder(Map<Integer, Set<Integer>> graph) {
            Map<Integer, Integer> inDegrees = getInDegrees(graph);
            Queue<Integer> queue = new LinkedList<>();
            List<Integer> topoOrder = new ArrayList<>();

            for (Integer node : graph.keySet()) { // 遍历所有key
                if (inDegrees.get(node) == 0) { // 入度为0，可直接添加至队列
                    queue.offer(node);
                    topoOrder.add(node);
                }
            }

            while (!queue.isEmpty()) {
                if (queue.size() > 1) { // 队列里同时存在2个以上元素，重构序列（拓扑排序）就不唯一了
                                        // 因为不知道要出哪一个（这两个没有先后）
                    return null;
                }

                Integer node = queue.poll();
                for (Integer neighbor : graph.get(node)) { // node进队列后，邻居入度-1
                    inDegrees.put(neighbor, inDegrees.get(neighbor) - 1);
                    if (inDegrees.get(neighbor) == 0) {
                        queue.offer(neighbor);
                        topoOrder.add(neighbor);
                    }
                }
            }

//            if (graph.size() == topoOrder.size()) {
//                return topoOrder;
//            }
//
//            return null;
            return topoOrder;
        }

    }

}
