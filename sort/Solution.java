package sort;

import java.util.*;

public class Solution {

    // Q1/215 find k-th largest element in the array
    public int findKthLargestSort(int[] nums, int k) { // O(NlogN) O(1)
        Arrays.sort(nums);
        return nums[nums.length - k];
    }

    public int findKthLargestHeap(int[] nums, int k) { // O(NlogK) O(K)
        PriorityQueue<Integer> pq = new PriorityQueue<>(); // min heap
        for (int val : nums) {
            pq.add(val);
            // remove the smallest, remain k largest elements finally
            if (pq.size() > k) {
                pq.poll(); // get and remove the head of the queue
            }
        }
        return pq.peek(); // get but not remove the head of the queue
    }


    private int partition(int[] a, int l, int r) {
        int i = l;
        int j = r + 1;
        while (true) {
            while (a[++i] < a[l] && i < r) ; // find the element >= a[l]
            while (a[--j] > a[l] && j > l) ; // find the element <= a[l]
            if (i >= j) {
                break;
            }
            swap(a, i, j); // swap to ensure left part <= a[l], right part >= a[l]
        }
        swap(a, l, j); // swap the pivot and j element
        return j;
    }
    private void swap(int[] a, int i, int j) {
        int t = a[i];
        a[i] = a[j];
        a[j] = t;
    }
    public int findKthLargestQuick(int[] nums, int k) { // O(N) O(1)
        k = nums.length - k;
        int l = 0;
        int r = nums.length - 1;
        while (l < r) {
            int j = partition(nums, l, r);
            if (j == k) {
                break;
            }
            else if (j < k) {
                l = j + 1;
            }
            else { // j>k
                r = j - 1;
            }
        }
        return nums[k];
    }


    // Q2/347 find top k largest frequency elements
    // bucket sort
    // every bucket store elements with same frequency
    // bucket 1: 1 time, bucket 2: 2 times, bucket 3: 3 times ...
    public int[] topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> frequencyForNum = new HashMap<Integer, Integer>();
        for (int num: nums) {
            // get value of num, if not find, get default 0
            int value = frequencyForNum.getOrDefault(num, 0) + 1;
            // put (key, value)
            frequencyForNum.put(num, value);
        }

        List<Integer>[] buckets = new ArrayList[nums.length + 1];

        return new int[] {-1};
    }

}
