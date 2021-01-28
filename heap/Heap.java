package heap;

import datastructure.ListNode;

import java.util.*;

public class Heap {
    // 1. Ugly Number II
    // 设计一个算法，找出只含素因子2，3，5 的第 n 小的数。
    // 符合条件的数如：1, 2, 3, 4, 5, 6, 8, 9, 10, 12...
    public class NthUglyNumberSolution {
        // 遍历效率太低。换个角度思考，如果我们只生成丑数，且生成n个
        // 不难发现生成丑数的规律：如果已知丑数ugly，那么ugly * 2，ugly * 3和ugly * 5也都是丑数
        // 既然求第n小的丑数，可以采用最小堆来解决。每次弹出堆中最小的丑数，然后检查它分别乘以2、3和 5后的数是否生成过，如果是第一次生成，那么就放入堆中。第n个弹出的数即为第n小的丑数。
        public int nthUglyNumber(int n) {
            Queue<Long> minheap = new PriorityQueue<>(); // 默认最小堆
            Set<Long> seen = new HashSet<>();

            minheap.offer(1L); // 添加第一个丑数1
            seen.add(1L);

            int[] factors = new int[]{2, 3, 5};
            long currUgly = 1;
            long newUgly;

            for (int i = 0; i < n; i++) {
                // 每次弹出当前的最小丑数
                currUgly = minheap.poll();
                // 计算新的丑数
                for (int f : factors) {
                    newUgly = currUgly * f;
                    if (!seen.contains(newUgly)) {
                        minheap.offer(newUgly);
                        seen.add(newUgly);
                    }
                }
            }
            return (int)currUgly;
        }
    }

    // 2. Top k largest Number II
    // 实现两个函数：不断add数据，取前K个最大的数
    public class TopKLargestSolution {
        private int maxSize;
        private Queue<Integer> minheap;
        public TopKLargestSolution(int k) {
            minheap = new PriorityQueue<>();
            maxSize = k;
        }

        public void add(int num) {
            // 如果空间够，直接添加
            if (minheap.size() < maxSize) {
                minheap.offer(num);
                return;
            }

            // 如果空间不够，需要删除最小的（因为要求的是top k largest）
            // 此时需要判断加进来的数和最小值得大小
            if (num > minheap.peek()) {
                minheap.poll();
                minheap.offer(num);
            }
        }

        public List<Integer> topK() {
            // 如果用for循环不断往外poll数据的话，heap会变空，topK的函数就无法调用多次了
            Iterator it = minheap.iterator();
            List<Integer> result = new ArrayList<Integer>();

            // 遍历堆，把k个数据加进数组（此时遍历结果可能是乱序的）
            while (it.hasNext()) {
                result.add((Integer) it.next()); // iterator返回Object类型
            }
            Collections.sort(result, Collections.reverseOrder()); // 从大到小排序

            return result;
        }
    }

    // 3. Merge K Sorted Lists
    // 合并k个排序链表，并且返回合并后的排序链表
    public class MergeKListsSolution {
        // 方法1 最小堆
        public ListNode mergeKLists(List<ListNode> lists) {
            if (lists == null || lists.size() == 0) {
                return null;
            }
            // .1 重写comparator
            // Queue<ListNode> minHeap = new PriorityQueue<ListNode>(lists.size(), ListNodeComparator);
            // .2 lamba表达式
            Queue<ListNode> minHeap = new PriorityQueue<ListNode>(lists.size(), (left, right) -> left.val - right.val);

            for (int i = 0; i < lists.size(); i++) {
                if (lists.get(i) != null) {
                    minHeap.add(lists.get(i)); // lists里的第i串链表的head
                }
            }
            
            // 一直比较每条链表的第一个数
            ListNode dummy = new ListNode(0); // 构建新的链表
            ListNode tail = dummy;
            while (!minHeap.isEmpty()) {
                ListNode head = minHeap.poll();
                tail.next = head;
                tail = head; // tail往下走到当前head
                if (head.next != null) {
                    minHeap.offer((head.next));
                }
            }

            return dummy.next;
        }

        // .1 重写comparator（如果用lamba就不需要这个操作了）
        private Comparator<ListNode> ListNodeComparator = new Comparator<ListNode>() {
            public int compare(ListNode left, ListNode right) {
                return left.val - right.val;
            }
        };

        // 方法2 merge two by two
        public ListNode mergeKLists2(List<ListNode> lists) {
            if (lists == null || lists.size() == 0) {
                return null;
            }
            // 不断地进行两两merge
            while (lists.size() > 1) {
                List<ListNode> newLists = new ArrayList<>();
                for (int i = 0; i + 1 < lists.size(); i += 2) {
                    ListNode mergeList = merge(lists.get(i), lists.get(i + 1));
                    newLists.add(mergeList);
                }
                if (lists.size() % 2 == 1) { // 奇数个，有一个没参与排序，直接添加
                    newLists.add(lists.get(lists.size() - 1));
                }
                lists = newLists;
            }

            return lists.get(0);
        }
        private ListNode merge(ListNode a, ListNode b) {
            ListNode dummy = new ListNode(0);
            ListNode tail = dummy;
            while (a != null && b != null) {
                if (a.val < b.val) {
                    tail.next = a;
                    a = a.next;
                } else {
                    tail.next = b;
                    b = b.next;
                }
                tail = tail.next;
            }

            if (a != null) {
                tail.next = a;
            } else {
                tail.next = b;
            }

            return dummy.next;
        }
        // 方法3
    }

    // 4. 优秀成绩 · High Five
    // 每个学生有两个属性 id 和 scores。找到每个学生最高的5个分数的平均值
    public class HighFiveSolution {
        class Record {
            public int id, score;
            public Record(int id, int score) {
                this.id = id;
                this.score = score;
            }
        }
        public Map<Integer, Double> highFive(Record[] results) {
            Map<Integer, Double> answer = new HashMap<>(); // 每个学生 -> 最高5门课的平均成绩
            Map<Integer, PriorityQueue<Integer>> hash = new HashMap<>(); // 每个学生 -> 所有课的成绩

            for (Record r : results) {
                if (!hash.containsKey(r.id)) {
                    hash.put(r.id, new PriorityQueue<Integer>()); // 每个学生ID对应的成绩组成一个堆
                }
                PriorityQueue<Integer> minHeap = hash.get(r.id);
                if (minHeap.size() < 5) {
                    minHeap.offer(r.score);
                } else { // >5的话要换掉目前存在的最小值
                    if (r.score > minHeap.peek()) {
                        minHeap.poll();
                        minHeap.offer(r.score);
                    }
                }
            }

            for (Map.Entry<Integer, PriorityQueue<Integer>> entry : hash.entrySet()) {
                int id = entry.getKey();
                PriorityQueue<Integer> scores = entry.getValue();
                double average = 0;
                for (int i = 0; i < 5; i++) {
                    average += scores.poll();
                }
                average /= 5.0;
                answer.put(id, average);
            }

            return answer;
        }
    }

    // 5. K个最近的点 · K Closest Points
    // 给定一些 points 和一个 origin，从 points 中找到 k 个离 origin 最近的点
    // 按照距离由小到大返回。如果两个点有相同距离，则按照x值来排序；若x值也相同，就再按照y值排序
    public class kClosestSolution {
        class Point {
            int x;
            int y;
            Point() { x = 0; y = 0; }
            Point(int a, int b) { x = a; y = b; }
        }
        // 基于 PriorityQueue 的方法 PriorityQueue 里从远到近排序。当 PQ 里超过 k 个的时候，就 pop 掉一个
        // 时间复杂度 O(nlogk)
        public Point[] kClosest(Point[] points, Point origin, int k) {

            PriorityQueue<Point> maxHeap = new PriorityQueue<>(k, new Comparator<Point>() {
                @Override
                public int compare(Point a, Point b) {
                    int diff = getDistance(b, origin) - getDistance(a, origin);
                    if (diff == 0) { // 距离相等，取x近的（所以最大堆大的进）
                        diff = b.x - a.x;
                    }
                    if (diff == 0) { // 距离相等，x也相等，取y近的
                        diff = b.y - a.y;
                    }
                    return diff;
                }
            });

            for (int i = 0; i < points.length; i++) {
                maxHeap.offer(points[i]);
                if (maxHeap.size() > k) { // 如果size过大，把最大的数取走
                    maxHeap.poll();
                }
            }

            Point[] result = new Point[k];
            while (!maxHeap.isEmpty()) {
                k--;
                result[k] = maxHeap.poll();
            }

            return result;
        }

        private int getDistance(Point a, Point b) {
            return (a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y);
        }
    }

    // 6. Merge K Sorted Arrays
    // 将 k 个有序数组合并为一个大的有序数组
    // O(n logk)
    public class MergekSortedArraysSolution {
        class Element {
            public int row, col, val;
            Element(int row, int col, int val) {
                this.row = row;
                this.col = col;
                this.val = val;
            }
        }
        // 初始将所有数组的首个元素入堆, 并记录入堆的元素是属于哪个数组的.
        // 每次取出堆顶元素, 并放入该元素所在数组的下一个元素
        public int[] mergekSortedArrays(int[][] arrays) {
            if (arrays == null) {
                return new int[]{0};
            }

            int totalSize = 0; // 记录总的数据个数
            PriorityQueue<Element> minHeap = new PriorityQueue<>(arrays.length,
                                                                 (a, b) -> (a.val - b.val));
            // 每一行的第一个数（最小值）进heap
            for (int i = 0; i < arrays.length; i++) {
                if (arrays[i].length > 0) {
                    Element ele = new Element(i, 0, arrays[i][0]);
                    minHeap.offer(ele);
                    totalSize += arrays[i].length; // 每一行的总个数相加
                }
            }

            int[] result = new int[totalSize];
            int index = 0;
            while (!minHeap.isEmpty()) {
                Element ele = minHeap.poll();
                result[index] = ele.val;
                index++;
                // 所在行的下一个元素进堆
                if (ele.col + 1 < arrays[ele.row].length) { // 所在行的元素个数
                    ele.col++;
                    ele.val = arrays[ele.row][ele.col];
                    minHeap.offer(ele);
                }
            }

            return result;
        }
    }
}
