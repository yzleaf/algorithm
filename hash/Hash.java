package hash;

import datastructure.*;

public class Hash {

    // 1. Rehashing
    // 哈希表容量的大小在一开始是不确定的。如果哈希表存储的元素太多（如超过容量的十分之一），我们应该将哈希表容量扩大一倍，并将所有的哈希值重新安排。
    // [null, 21, 14, null]
    //       ↓    ↓
    //       9   null
    //       ↓
    //      null
    // hash函数：key % capacity;
    public class RehashingSolution {
        public ListNode[] rehashing(ListNode[] hashTable) {
            if (hashTable.length <= 0) {
                return hashTable;
            }

            int newCapacity = 2 * hashTable.length;
            ListNode[] newHashTable = new ListNode[newCapacity];

            for (int i = 0; i < hashTable.length; i++) {
                while (hashTable[i] != null) { // 这个链表数组的一个位置有数据
                    // Java中如果直接计算-4 % 3，会得到-1。可以应用函数：a % b = (a % b + b) % b得到一个非负整数。
                    int newIndex = (hashTable[i].val % newCapacity + newCapacity) % newCapacity;
                    if (newHashTable[newIndex] == null) {
                        newHashTable[newIndex] = new ListNode(hashTable[i].val);
                    } else { // 已经存在数据，需要在这个链表上往后加新的数据
                        ListNode dummy = newHashTable[newIndex];
                        while (dummy.next != null) {
                            dummy = dummy.next;
                        }
                        dummy.next = new ListNode(hashTable[i].val);
                    }
                    hashTable[i] = hashTable[i].next; // 遍历原始哈希数组中这个位置当前链表的下一个数
                }
            }

            return newHashTable;
        }
    }


}
