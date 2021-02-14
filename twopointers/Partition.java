package twopointers;

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
    // 在数组中找到第 k 大的元素
    // 时间复杂度为O(n)，空间复杂度为O(1)
    // Input: [3,2,3,1,2,4,5,5,6] and k = 4 （有相同的数据，也是按下标算k，不是不同数的第k个）
    // Output: 4
    public class KthLargestSolution {
//        public int kthLargestElement(int k, int[] nums) {
//            if (nums == null || nums.length == 0 || k < 1 || k > nums.length) {
//                return -1;
//            }
//            return partition(nums, 0, nums.length - 1, ***);
//        }
//        private int pertition(int[] nums, int start, int end, int k) {
//
//        }

        public int findKthLargest(int[] nums, int k) {
            // PriorityQueue<Integer> pq = new PriorityQueue<>();
            // for (int i=0 ; i<nums.length ; i++) {
            //     pq.add(nums[i]);
            //     if (pq.size() > k) {
            //         pq.poll();
            //     }
            // }
            // return pq.peek();
            if (nums == null || nums.length == 0 || k < 1 || k > nums.length){
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

        private int partition3(int[] nums, int start, int end, int k) {
            if (start >= end) {
                return nums[k];
            }

            int left = start, right = end;
            int pivot = nums[(start + end) / 2];

            while (left <= right) {
                while (left <= right && nums[left] < pivot) { // 如果用nums<=pivot，整个数组元素相等时候，不交换，最后一直递归
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

            if (k <= right) {
                return partition(nums, start, right, k);
            }
            if (k >= left) {
                return partition(nums, left, end, k);
            }
            return nums[k]; // k在right和left之间，k位置一定是最后所在的位置
        }
    }

    // 3. Partition Array by Odd and Even
    // 分割一个整数数组，使得奇数在前偶数在后
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

            // left和right，分别指示0-1边界和1-2边界。left左边（不含left）全为0，right右边（不含right）全为2
            // 第三个指针mid从left起向right移动，边扫描边实时更新两个边界
            while (mid <= right) { // mid > right时停止遍历
                if (nums[mid] == 0) {
                    swap(nums, left, mid);
                    left ++;
                    mid ++; // 因为从左往右扫，mid扫过的，现在交换过来的数肯定为1，所以可以继续往后走一位
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

                if (colors[index] <= 0) { // 当前已经计数的位，直接跳过
                    index++;
                } else { // 当前是color，需要计数
                    if (colors[temp] <= 0) { // 对应的位置已经计数
                        colors[temp]--;
//                        colors[index] = 0;
                        index++;
                    } else { // 对应的位置还没用来计数，需要保存和交换这个位置的数
                        swap(colors, index, temp);
                        colors[temp] = -1;
                    }
                }
            }

            // 倒着输出
            int i = len - 1; // i是输出位置的下标
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
}
