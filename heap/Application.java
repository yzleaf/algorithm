package heap;

import java.util.*;

public class Application {

    // 1383. Maximum Performance of a Team
    // 有n个工程师，speed[i] 和 efficiency[i] 分别代表第i位工程师的速度和效率
    // 返回一个团队中k个「所有工程师速度的和」乘以他们「效率值中的最小值」，最后需要对 10^9 + 7取余后的结果
    public class MaxPerformanceS {
        public int maxPerformance(int n, int[] speed, int[] efficiency, int k) {
            final int MODULO = 1000000007;
            int[][] engineer = new int[n][2];
            for (int i = 0; i < n; i++) {
                engineer[i][0] = speed[i]; // speed
                engineer[i][1] = efficiency[i]; // efficiency;
            }
            Arrays.sort(engineer, new Comparator<int[]>() {
                @Override
                public int compare(int[] o1, int[] o2) {
                    return o2[1] - o1[1]; // efficiency从高到低排
                }
            });

            PriorityQueue<Integer> minHeap = new PriorityQueue<>(); // 存k-1个工程师的speed，最小的要淘汰
            long result = 0, speedSum = 0;
            for (int i = 0; i < n; i++) {
                if (minHeap.size() > k-1) { // 最小堆只能留k-1个，第k个是当前的engineer，heap里之前存的efficiency肯定比当前的大（因为降序排列）
                    speedSum -= minHeap.poll(); // 最小的先淘汰
                }

                long curr = (speedSum + engineer[i][0]) * engineer[i][1];
                result = Math.max(result, curr);

                minHeap.offer(engineer[i][0]); // speed
                speedSum += engineer[i][0];
            }

            return (int)(result % MODULO);

        }
    }
}
