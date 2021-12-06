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
        public int nthUglyNumber(int n) { // 第n小
            Queue<Long> minheap = new PriorityQueue<>(); // 默认最小堆
            Set<Long> seen = new HashSet<>(); // 当前丑数是否生成过

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
    public class TopKLargestSolution2 {
        private int maxSize;
        private Queue<Integer> minheap;
        public TopKLargestSolution2(int k) {
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
                    minHeap.offer(lists.get(i)); // lists里的第i串链表的head
                }
            }
            
            // 一直比较每条链表的第一个数
            ListNode dummy = new ListNode(0); // 构建新的链表
            ListNode tail = dummy;
            while (!minHeap.isEmpty()) {
                ListNode curr = minHeap.poll();
                tail.next = curr;
                tail = curr; // tail往下走到当前head
                if (curr.next != null) {
                    minHeap.offer(curr.next);
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

        // 方法3 divide and conquer
        // 不断向下二分lists，最后是不断向上merge两个lists
        public ListNode mergeKLists3(List<ListNode> lists) {
            if (lists.size() == 0) {
                return null;
            }
            return mergeHelper(lists, 0, lists.size() - 1);
        }
        private ListNode mergeHelper(List<ListNode> lists, int start, int end) {
            if (start == end) {
                return lists.get(start);
            }

            int mid = start + (end - start) / 2;
            ListNode left = mergeHelper(lists, start, mid);
            ListNode right = mergeHelper(lists, mid + 1, end);
            return merge(left, right);
        }
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
//                if (!hash.containsKey(r.id)) {
//                    hash.put(r.id, new PriorityQueue<Integer>()); // 每个学生ID对应的成绩组成一个堆
//                }
                hash.putIfAbsent(r.id, new PriorityQueue<Integer>()); // 每个学生ID对应的成绩组成一个堆

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
                    if (diff == 0) { // 距离相等，取x近的（所以最大堆大的进）（diff此时为距离差）
                        diff = b.x - a.x;
                    }
                    if (diff == 0) { // 距离相等且x也相等，取y近的（diff此时为x值差）
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
            for (int i = 0; i < arrays.length; i++) { // 行数
                if (arrays[i].length > 0) { // 每行的列数
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

    // 7. 数据流中位数 · data stream median
    // 295
    // 数字是不断进入数组的，在每次添加一个新的数进入数组的同时返回当前新数组的中位数
    // 不同于数学的中位数，这里中位数是排序后数组的中间值，如果有数组中有n个数，则中位数为A[(n-1)/2]
    public class DataStreamMedianSolution {
        // 用 maxheap 保存左半部分的数，用 minheap 保存右半部分的数
        // 把所有的数一左一右的加入到每个部分。左边部分最大的数就一直都是 median
        private PriorityQueue<Integer> maxHeap, minHeap;
        private int numOfElements = 0;// 判断奇偶性 确认在哪个堆中加入数

        public int[] median(int[] nums) {
            int cnt = nums.length;
            maxHeap = new PriorityQueue<>(cnt, Collections.reverseOrder());
            minHeap = new PriorityQueue<>(cnt);
            int[] ans = new int[cnt];
            for (int i = 0; i < cnt; i++) {
                addNumber(nums[i]);
                ans[i] = getMedian();
            }
            return ans;
        }
        private void addNumber(int value) {
            maxHeap.offer(value);
            if (numOfElements % 2 == 0) { // 每两次add就会判断左右两边堆顶元素大小
                if (minHeap.isEmpty()) {
                    numOfElements++;
                    return;
                }
                else if (maxHeap.peek() > minHeap.peek()) { // 为了保证右边（最小堆：放大的数）的元素比左边（最大堆：放小的数）大
                    Integer maxHeapRoot = maxHeap.poll();
                    Integer minHeapRoot = minHeap.poll();
                    maxHeap.offer(minHeapRoot);
                    minHeap.offer(maxHeapRoot);
                }
            } else { // 如果是奇数，说明原来不平衡了，max多一个，此时数应该先进minHeap（在下一轮的时候判断两边谁大确认最终位置）
                minHeap.offer(maxHeap.poll());
            }
            numOfElements ++;
        }
        private int getMedian() {
            return maxHeap.peek();
        }
    }

    // 8. 前K大数 · Top k Largest Numbers
    // 在一个数组中找到前K大的数
    public class TopkLargestSolution1 {
        // 方法1 堆排序
        public int[] topK(int[] nums, int k) {
            PriorityQueue<Integer> minHeap = new PriorityQueue<>(k+1);

            for (int i = 0; i < nums.length; i++) {
                minHeap.offer(nums[i]);
                if (minHeap.size() > k) {
                    minHeap.poll();
                }
            }

            int[] result = new int[k];
            for (int i = 0; i < k; i++) {
                result[k - i - 1] = minHeap.poll(); // 从后往前
            }

            return result;
        }

        // 方法2 quick select
        public int[] topK2(int[] nums, int k) {
            // 第k大，是从小到大第nums.length-k的下标的数
            quickSelect(nums, 0, nums.length - 1, nums.length - k);

            int[] result = new int[k];
            for (int i = nums.length - 1, j = 0; j < k; i--, j++) { // 输出后半部分大于k的数
                result[j] = nums[i];
            }

            return result;
        }
        private void quickSelect(int[] nums, int start, int end, int k) {
            if (start >= end) {
                return;
            }

            int left = start, right = end;
            int pivot = nums[start + (end - start)/2];

            while (left <= right) {
                // 如果数组里的数是一样的，nums[left]<=pivot的话，left和right不会换，之后会一直反复调用这个函数没有出口
                while (left <= right && nums[left] < pivot) {
                    left ++;
                }
                while (left <= right && nums[right] > pivot) {
                    right --;
                }
                if (left <= right) {
                    int temp = nums[left];
                    nums[left] = nums[right];
                    nums[right] = temp;

                    left++;
                    right--;
                }
            }

            if (k <= right) {
                quickSelect(nums, start, right, k);
            }
            if (k >= left) {
                quickSelect(nums, left, end, k);
            }
            return;
        }
    }

    // 9. Kth Smallest Number in Sorted Matrix
    // 378
    // 在一个排序矩阵中找从小到大的第 k 个整数
    // 排序矩阵的定义为：每一行递增，每一列也递增
    public class kthSmallestInMatrixSolution {
        class Pair {
            public int x, y, val;
            public Pair(int x, int y, int val) {
                this.x = x;
                this.y = y;
                this.val = val;
            }
        }
        // 方法1 最小堆
        // 定义一个小根堆, 起始仅仅放入第一行第一列[0][0]的元素.
        // 循环k次, 每一次取出一个元素, 然后把该元素右方以及下方的元素放入堆中, 第k次取出的元素即为答案
        public int kthSmallest(int[][] matrix, int k) {
            // 向右和向下两个坐标
            int[] dx = new int[]{0, 1};
            int[] dy = new int[]{1, 0};

            int row = matrix.length;
            int col = matrix[0].length;
            boolean[][] visited = new boolean[row][col];
            PriorityQueue<Pair> minHeap = new PriorityQueue<>(k, (a, b) -> a.val - b.val);

            minHeap.offer(new Pair(0, 0, matrix[0][0])); // 添加第一个元素

            for (int i = 0; i < k - 1; i++) { // 取出k-1个元素后，堆顶就是第k个
                Pair curr = minHeap.poll();
                for (int j = 0; j < 2; j++) { // 向右和向下两个坐标
                    int nextX = curr.x + dx[j];
                    int nextY = curr.y + dy[j];
                    Pair next = new Pair(nextX, nextY, 0); // 判断以后再赋值，避免出现越界为空的情况
                    if (nextX < row && nextY < col && !visited[nextX][nextY]) {
                        visited[nextX][nextY] = true;
                        next.val = matrix[nextX][nextY];
                        minHeap.offer(next);
                    }
                }
            }

            return minHeap.peek().val;
        }

        // 方法2  二分
        // 起始数区间是[0][0] - [n][m]，二分查找中间的数mid排多少位
        // 从左下角开始，目标值更大->往右找，目标值更小->往上找
        // 每次查看小于中间数mid的个数有多少
        //    如果个数k<mid，mid应该右移，以获得更多的小于它的数(start = mid)
        //    如果个数k>mid，mid应该左移，以获得更少的小于它的数(end = mid)
        public int kthSmallest2(int[][] matrix, int k) {
            int m = matrix.length;
            int n = matrix[0].length;
            int start = matrix[0][0];
            int end = matrix[m - 1][n - 1];

//            while (start < end) {
//                // 每次循环都保证第K小的数在start~end之间，当start==end，第k小的数就是start
//                int mid = start + (end - start) / 2;
//                // 找二维矩阵中<mid的元素总个数
//                int count = findNotBiggerThanMid(matrix, mid, m, n);
//                if (count < k) {
//                    // 第k小的数在右半部分，且不包含mid
//                    start = mid + 1;
//                } else if (count > k) {
//                    // 第k小的数在左半部分，可能包含mid（下面的=）
//                    end = mid;
//                } else {
//                    end = mid;
//                }
//            }
//            return end;

            while (start + 1 < end) {
                // 每次循环都保证第K小的数在start~end之间，当start==end，第k小的数就是start
                int mid = start + (end - start) / 2;
                // 找二维矩阵中<mid的元素总个数
                int count = findNotBiggerThanMid(matrix, mid, m, n);
                if (count < k) {
                    // 第k小的数在右半部分，且不包含mid
                    start = mid;
                } else { // >=
                    // 第k小的数在左半部分，可能包含mid
                    end = mid;
                }
            }
            // 先看start，如果大于k的话，可能会有相等的元素
            if (findNotBiggerThanMid(matrix, start, m, n) >= k) {
                return start;
            }
            if (findNotBiggerThanMid(matrix, end, m, n) >= k) {
                return end;
            }
            return 0;
        }
        private int findNotBiggerThanMid(int[][] matrix, int mid, int row, int col) {
            // 以列为单位找，找到每一列最后一个<=mid的数即知道每一列有多少个数<=mid
            // 从左下角开始找
            int i = row - 1;
            int j = 0;
            int count = 0;
            while (i >= 0 && j < col) {
                if (matrix[i][j] <= mid) {
                    // 第j列有i+1个元素<=mid
                    count += i + 1;
                    j ++; // 向右移动
                } else {
                    // 第j列目前的数大于mid，需要继续在当前列往上找
                    i --;
                }
            }
            return count;
        }
    }

    // 239. 滑动窗口最大值
    // 整数数组nums，有一个大小为k的滑动窗口从数组的最左侧移动到数组的最右侧。
    // 滑动窗口每次只向右移动一位，返回滑动窗口中的最大值
    public class maxSlidingWindowS {
        // 方法1 用最大堆记(value, index)，每次向右滑都添加新元素进堆，如果当前堆里最大数的index不在窗口内就不断poll直到最大值的index在窗口内
        // 时间nlogn（元素单调递增，所有元素都在heap里）
        // 空间n

        // 方法2 双向队列，让数据从大到小排列。每次滑动窗口，新数从队尾last进入，把队尾小于它的数挤掉；队首的数是最大的，判断是不是在窗口内
        // 双向队列里存的是index（还没有被移除的下标）
        // 在队列中，这些下标按照从小到大的顺序被存储，并且它们在数组nums中对应的值是严格单调递减的。（所以队首index对应的元素肯定是最大的）
        // 因为如果队列中有两个相邻的下标，它们对应的值相等或者递增，那么令前者为i，后者为j，即 nums[i] <= nums[j] 在上一步中应该被移除，这就产生了矛盾。
        // 时间n
        // 空间k
        public int[] maxSlidingWindow(int[] nums, int k) {
            int n = nums.length;
            int[] result = new int[n - k + 1];
            Deque<Integer> deque = new LinkedList<>();

            for (int i = 0; i < n; i++) {
                while (!deque.isEmpty() && nums[deque.peekLast()] <= nums[i]) { // 队尾的数值如果小的话要丢掉，直到数值大于它
                    deque.pollLast();
                }
                deque.offerLast(i);

                // 判断队首是否在windows里（画图确认index范围）
                while (deque.peekFirst() < i - k + 1) {
                    deque.pollFirst();
                }

                // 开头k个值窗口占满了才开始保存
                if (i >= k - 1) {
                    result[i - k + 1] = nums[deque.peekFirst()];
                }
            }
            return result;
        }
    }

}
