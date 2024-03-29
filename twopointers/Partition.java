package twopointers;

import java.util.*;

public class Partition {
    // 1. Partition Array
    // 给出一个整数数组 nums 和一个整数 k。划分数组使得 所有小于k的元素移到左边，所有大于等于k的元素移到右边
    // 返回数组划分的位置，即数组中第一个位置i，满足 nums[i] 大于等于 k。
    public class PartitionArraySolution {
        public int partitionArray(int[] nums, int k) {
            if (nums == null) {
                return 0;
            }
            int left = 0, right = nums.length - 1;
            while (left <= right) {
                while (left <= right && nums[left] < k) {
                    left ++;
                }
                while (left <= right && nums[right] >= k) {
                    right --;
                }

                if (left <= right) {
                    int temp = nums[left];
                    nums[left] = nums[right];
                    nums[right] = temp;

                    left ++;
                    right --;
                }
            }
            return left;
        }
    }

    // 2. Kth Largest Element
    // 215
    // 在数组中找到第 k 大的元素
    // 时间复杂度为O(n)，空间复杂度为O(1)
    // Input: [3,2,3,1,2,4,5,5,6] and k = 4 （有相同的数据，也是按下标算k，不是不同数的第k个）
    // Output: 4
    public class KthLargestSolution {

        public int findKthLargest(int[] nums, int k) {
            // PriorityQueue<Integer> pq = new PriorityQueue<>();
            // for (int i=0 ; i<nums.length ; i++) {
            //     pq.add(nums[i]);
            //     if (pq.size() > k) {
            //         pq.poll();
            //     }
            // }
            // return pq.peek();
            if (nums == null || nums.length == 0 || k < 1 || k > nums.length) {
                return -1;
            }
            // 第k大，是从小到大第nums.length-k的下标的数
            return partition(nums, 0, nums.length - 1, nums.length - k);
        }

        private int partition(int[] nums, int start, int end, int k) {
            if (start >= end) {
                return nums[k];
            }

            int i = start;     //i左边（不包括i）都是小于pivot的
            int j = start;     //j左边（不包括j）都是已经看过的
            int pivotIndex = (start + end) / 2;
            int pivot = nums[pivotIndex];
            swap(nums, end, pivotIndex); // pivot放到尾部end
            while (j < end) {
                if (nums[j] < pivot) {
                    swap(nums, i, j); // j已经走过大于等于pivot的数，遇到小于pivot的，要和之前i所在位置的数交换
                                      // 以保证i左边（不包括i）都是小于pivot
                    i++;
                    j++;
                } else {
                    j++;
                }
            }
            // 跳出循环的i是第一个大于或等于pivot的数
            swap(nums, i, end); // i一定是pivot的位置

            if (k < i) {
                return partition(nums, start, i-1, k);
            }
            if (k > i) {
                return partition(nums, i+1, end, k);
            }
            return nums[k];
        }

        private void swap(int[] nums, int i, int j) {
            int tmp = nums[i];
            nums[i] = nums[j];
            nums[j] = tmp;
        }

        private int partition2(int[] nums, int start, int end, int k) {
            int pivotIndex = (start + end) / 2;
            int pivot = nums[pivotIndex];
            swap(nums, end, pivotIndex);
            int left = start; // left左边不包括left都是小于pivot的
            int right = end - 1; // right右边不包括right都是大于等于pivot的
            while (left <= right) {
                while (left <= right && nums[left] < pivot) {
                    left++;
                }
                while (left <= right && nums[right] >= pivot) {
                    right--;
                }
                if (left <= right) {
                    int temp = nums[left];
                    nums[left] = nums[right];
                    nums[right] = temp;
                    left++;
                    right--;
                }
            }
            swap(nums, left, end);
            if (k < left) {
                return partition(nums, start, left-1, k);
            }
            if (k > left) {
                return partition(nums, left+1, end, k);
            }
            return nums[left];
        }
        // 模板
        // partition可以不返回任何数，直接设置void
        private int partition3(int[] nums, int start, int end, int k) {
            if (start >= end) {
                return nums[k]; // nums[start]也是可以的
            }

            int left = start, right = end;
            int pivot = nums[(start + end) / 2];

            while (left <= right) {
                while (left <= right && nums[left] < pivot) { // 如果用nums<=pivot，整个数组元素相等时候，左指针会一直走到最后，不平衡
                    left++;
                }
                while (left <= right && nums[right] > pivot) {
                    right--;
                }
                if (left <= right) {
                    swap(nums, left, right);
                    left++;
                    right--;
                }
            }

            if (k <= right) { // 在左半边
                return partition(nums, start, right, k);
            }
            if (k >= left) { // 在右半边
                return partition(nums, left, end, k);
            }
            return nums[k]; // k在right和left之间，k位置一定是最后所在的位置（相遇的时候是相等的数）
        }
    }

    // 347. Top K Frequent Elements
    // 找到数组中出现频率最高的k个数
    public class TopKFreqSolution {
        // 方法1 堆
        public int[] topKFrequent1(int[] nums, int k) {
            Map<Integer, Integer> numCount = new HashMap<>();
            for (int num : nums) {
                numCount.put(num, numCount.getOrDefault(num, 0) + 1);
            }

            // 第一个数是number，第二个数是出现频率
            PriorityQueue<int[]> minHeap = new PriorityQueue<>(new Comparator<int[]>() {
                @Override
                public int compare(int[] o1, int[] o2) {
                    return o1[1] - o2[1];
                }
            });

            for (Map.Entry<Integer, Integer> entry : numCount.entrySet()) {
                int num = entry.getKey();
                int count = entry.getValue();
                minHeap.offer(new int[] {num, count});
                if (minHeap.size() > k) {
                    minHeap.poll();
                }
            }

            int[] result = new int[k];
            for (int i = 0; i < k; i++) {
                result[i] = minHeap.poll()[0];
            }

            return result;
        }

        // 方法2 双指针partition，类似于K Largest
        public int[] topKFrequent2(int[] nums, int k) {
            Map<Integer, Integer> numCount = new HashMap<>();
            for (int num : nums) {
                numCount.put(num, numCount.getOrDefault(num, 0) + 1);
            }

            List<int[]> values = new ArrayList<>();
            for (Map.Entry<Integer, Integer> entry : numCount.entrySet()) {
                int num = entry.getKey();
                int count = entry.getValue();
                values.add(new int[]{num, count});
            }

            int len = values.size();
            int index = partition(values, 0, len - 1, len - k);

            int[] result = new int[k];
            for (int i = 0; i < k; i ++) {
                result[i] = values.get(i + index)[0];
            }

            return result;
        }
        private int partition(List<int[]> values, int start, int end, int index) {
            if (start >= end) {
                return index;
            }

            int left = start;
            int right = end;
            int pivot = values.get((left + right) / 2)[1];

            while (left <= right) {
                while (left <= right && values.get(left)[1] < pivot) {
                    left ++;
                }
                while (left <= right && values.get(right)[1] > pivot) {
                    right --;
                }
                if (left <= right) {
                    Collections.swap(values, left, right);
                    left ++;
                    right --;
                }
            }

            if (index <= right) {
                return partition(values, start, right, index);
            }
            if (index >= left) {
                return partition(values, left, end, index);
            }
            return index;
        }

    }

    // 3. Partition Array by Odd and Even
    // 分割一个整数数组，使得奇数在前 偶数在后
    public class PartitionArrayOddEvenSolution {
        // 头指针定位到从前到后的第一个偶数，尾指针定位到从后到前的第一个奇数，两者交换即可
        // 直到尾指针在头之前前面
        public void partitionArray(int[] nums) {
            int start = 0, end = nums.length - 1;
            while (start < end) {
                while (start < end && nums[start] % 2 == 1) { // 奇数，继续往后
                    start ++;
                }
                while (start < end && nums[end] % 2 == 0) { // 偶数，继续往前
                    end --;
                }
                if (start < end) {
                    int temp = nums[start];
                    nums[start] = nums[end];
                    nums[end] = nums[start];
                    start ++;
                    end --;
                }
            }
        }
    }

    // 4. interleaving positive and negative integers
    // 输入 : [-1, -2, -3, 4, 5, 6]
    // 输出 : [-1, 5, -2, 4, -3, 6]
    // 给出一个含有正整数和负整数的数组，重新排列成一个正负数交错的数组
    public class InterleavingIntegersSolution {
        public void partitionArray(int[] nums) {
            // .1. 分别算出正数的个数和负数的个数（原因：如果正数多，则正数在前，负数在后）
            int positiveL = 0;
            int negativeL = 0;
            for (int i = 0; i < nums.length; i++) {
                if (nums[i] > 0) {
                    positiveL ++;
                } else if (nums[i] < 0) {
                    negativeL ++;
                } else { // 不能有0
                    return;
                }
            }

            // .2. partition，把正负数放到左右两半边
            if (positiveL == negativeL) { // 个数相等，正负数随便哪个在左半边
                                          // partition之后的形态是：XXXXOOOO
                partitionArray(nums, 0);

            } else if (positiveL - negativeL == 1) { // 正数多，放左半边X
                                                     // partition之后的形态是：XXXXXOOOO
                partitionArray(nums, 0);

            } else if (negativeL - positiveL == 1) { // 负数多，放左半边
                partitionArray(nums, 0);

            } else { // 不能正负数个数相差大于1
                return;
            }

            // .3. 左右指针向中间走，交换数据
            // 如果正负数个数相等，第一个和最后一个数不变，从left第二个位置和right倒数第二个位置开始swap
            // 如果正负数个数不等，从left第二个位置和right倒数第一个位置开始swap
            // 每次在左指针的奇数位置交换（每次走2步）
            int left = 0, right = nums.length - 1;
            if (nums.length % 2 == 0) { // 正负个数相等
                left ++;
                right --;
                while (left < right) {
                    swap(nums, left, right);
                    left += 2;
                    right -= 2;
                }

            } else { // 正负个数不相等
                left ++;
                while (left < right) {
                    swap(nums, left, right);
                    left += 2;
                    right -= 2;
                }
            }
        }
        private void partitionArray(int[] nums, int pivot) {
        }
        private void swap(int[] nums, int i, int j) {
        }
    }

    // 5. Sort Letters by Case
    // 给定一个只包含字母的字符串，按照先小写字母后大写字母的顺序进行排序
    public class SortLettersSolution {
        public void sortLetters(char[] chars) {
            int start = 0, end = chars.length - 1;
            while (start < end) {
                while (start < end && Character.isLowerCase(chars[start])) {
                    start ++;
                }
                while (start < end && Character.isUpperCase(chars[end])) {
                    end --;
                }
                if (start < end) {
                    char temp = chars[start];
                    chars[start] = chars[end];
                    chars[end] = temp;
                    start ++;
                    end --;
                }
            }
        }
    }

    // 6. Sort Colors
    // 75
    // 给定一个包含红，白，蓝且长度为 n 的数组，将数组元素进行分类使相同颜色的元素相邻，并按照红、白、蓝的顺序进行排序。
    // 使用整数 0，1 和 2 分别代表红，白，蓝。
    public class SortColorsSolution {
        // 三种思路
        // 1 最直观的方式是对数组进行排序，如快速排序，时间复杂度是O(nlogn)，但这种做法没有利用元素值在有限范围内的特性。
        // 2 计数排序，先扫描一遍记录0、1和2的个数，然后再扫描一遍对数组进行赋值。时间复杂度是O(n)，但是依旧没有利用元素只有3种的特性。
        // 3 采用双指针的方法，一遍扫描即可。时间复杂度为O(n)，只需常量空间。
        public void sortColors(int[] nums) {
            int left = 0;
            int right = nums.length - 1;
            int mid = 0;

            // left和right，分别指示0-1边界和1-2边界。left左边（不含left）全为0，right右边（不含right）全为2。即left指向第一个为1的数，right指向最后一个为1或0的数
            // 第三个指针mid从left起向right移动，边扫描边实时更新两个边界
            while (mid <= right) { // mid > right时停止遍历
                if (nums[mid] == 0) {
                    swap(nums, left, mid);
                    left ++;
                    mid ++; // 因为从左往右扫，mid扫过的，现在交换过来的数肯定为1，所以可以继续往后走一位
                            // 如果这里mid不加。一个例子，刚开始mid和left坐标相等，对应数值相等都为0，只有left往后走，后面会把当前的mid 0和left的其他数交换
                } else if (nums[mid] == 2) {
                    swap(nums, mid, right);
                    right --;
                } else { // ==1
                    mid ++;
                }
            }
        }
        private void swap(int[] a, int i, int j) {
            int temp = a[i];
            a[i] = a[j];
            a[j] = temp;
        }
    }

    // 7. Sort Colors II
    // 给定一个有n个对象（包括k种不同的颜色，并按照1到k进行编号）的数组，将对象进行分类使相同颜色的对象相邻，并按照1,2，...k的顺序进行排序
    // k <= n
    public class SortColors2Solution {
        // 方法1 计数排序
        // 普通的计数排序，会花费额外的O(k)空间

        // 用改进的方法，不花额外空间
        // 用负数代表数字出现的次数，例如colors[i]=-cnt表示数字i出现了cnt次
        // .1 从左往右遍历colors数组
        // 若colors[i]>0 且 colors[colors[i]] < 0，那么colors[colors[i]] -= 1 （计数，-1）
        // 若colors[i]>0 且 colors[colors[i]] > 0，这个位置之前没有被计数过，先要存下当前的值给临时变量，再赋-1计数，再把临时变量换回i去再计数
        // 若colors[i]<0，跳过
        // .2 倒着输出每种颜色
        // 另外注意数组下标是从0开始，为了避免n==k导致数组越界的情况，本题中colors[i]对应的计数位为colors[colors[i] - 1]
        public void sortColors2(int[] colors, int k) {
            int len = colors.length;
            if (len <= 0) {
                return;
            }

            int index = 0; // 遍历的下标
            while (index < len) {
                int temp = colors[index] - 1; // 因为数组下标从0开始，所以需要-1

                if (colors[index] <= 0) { // 当前是用来计数的位，直接跳过
                    index++;
                } else { // 当前是color，需要计数
                    if (colors[temp] <= 0) { // 对应的位置已经计数
                        colors[temp]--;
//                        colors[index] = 0;
                        index++;
                    } else { // 对应的位置还没用来计数，需要保存和交换这个位置的数
                        swap(colors, index, temp);
                        colors[temp] = -1;
                        // index没有变化，下次还是看这个index的数据
                    }
                }
            }

            // 倒着输出
            int i = len - 1; // i是输出颜色个数的颜色下标
            while (k > 0) { // k个数，k肯定在最后面
                for (int j = 0; j > colors[k - 1]; j--) {
                    colors[i] = k;
                    i--;
                }

                k--;
            }
        }
        public void swap(int[] colors, int a, int b) {
            int temp = colors[a];
            colors[a] = colors[b];
            colors[b] = temp;
        }

        // 方法2 分治法，快排
        public void sortColors2_2(int[] colors, int k) {
            if (colors == null || colors.length < 2) {
                return;
            }
            sort(colors, 0, colors.length - 1, 1, k);
        }
        private void sort(int[] colors, int start, int end, int colorFrom, int colorTo) {
            //若处理区间长度为小于等于1或颜色区间长度为1，则不需要再进行处理
            if (start >= end || colorFrom == colorTo) {
                return;
            }

            int left = start, right = end;
            int colorMid = colorFrom + (colorTo - colorFrom) / 2;
            while (left <= right) {
                while (left <= right && colors[left] <= colorMid) { // 左边放小于等于colorMid的
                    left ++;
                }
                while (left <= right && colors[right] > colorMid) { // 右边放大于colorMid的
                    right --;
                }
                if (left <= right) {
                    int temp = colors[left];
                    colors[left] = colors[right];
                    colors[right] = temp;
                }
            }

            // 递归处理左右两半
            sort(colors, start, right, colorFrom, colorMid);
            sort(colors, left, end, colorMid + 1, colorTo);
        }
    }

    // 345. Reverse Vowels of a String
    // 交换String里的元音字母
    // Input: s = "hello"
    // Output: "holle"
    public class ReverseVowelsSolution {
        public String reverseVowels(String s) {
            if (s == null || s.length() == 0) {
                return null;
            }

            int left = 0, right = s.length() - 1;
            Set<Character> hashVowels = new HashSet<>(Arrays.asList('a','e','i','o','u','A','E','I','O','U'));
            char[] charArr = s.toCharArray();

            while(left <= right) {
                while (left <= right && !hashVowels.contains(charArr[left])) { // left <= right判断条件！！！
                    left ++;
                }
                while (left <= right && !hashVowels.contains(charArr[right])) {
                    right --;
                }
                if (left <= right) {
                    swap(charArr, left, right);
                    left ++;
                    right --; // 换完以后要变指针！！！
                }
            }
            return String.valueOf(charArr);
        }
        private char[] swap(char[] charArr, int i, int j) {
            char temp = charArr[i];
            charArr[i] = charArr[j];
            charArr[j] = temp;
            return charArr;
        }
    }
}
