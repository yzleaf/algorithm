package hash;

import java.util.*;

public class LRU {

    // 2. LRU Cache
    // 为最近最少使用（LRU）缓存策略设计一个数据结构
    // 它应该支持以下操作：获取数据和写入数据。
    //     get(key) 获取数据：如果缓存中存在key，则获取其数据值（通常是正数），否则返回-1。
    //     set(key, value) 写入数据：如果key还没有在缓存中，则写入其数据值。当缓存达到上限，它应该在写入新数据之前删除最近最少使用的数据用来腾出空闲位置
    // 返回每次 get 的数据

    // .1 单链表方法
    public class LRUCacheSolution {

        class ListNode {
            public int key, val;
            public ListNode next;

            public ListNode(int key, int val) {
                this.key = key;
                this.val = val;
                this.next = null;
            }
        }

        private int capacity, size;
        private ListNode dummy, tail;
        private Map<Integer, ListNode> keyToPrev; // map中当前key位置存的是前驱结点

        public LRUCacheSolution(int capacity) {
            this.size = 0;
            this.capacity = capacity;
            this.keyToPrev = new HashMap<Integer, ListNode>(); // key -> key对应node的前驱节点

            this.dummy = new ListNode(0, 0);
            this.tail = this.dummy;
        }


        public int get(int key) {
            if (!keyToPrev.containsKey(key)) { // 不存在
                return -1;
            }

            moveToTail(key); // 如果存在这个node，就把key对应的node移到队尾（因为已经使用过了）
            return tail.val;
        }

        public void set(int key, int value) {
            // .1 key已经在缓存中，通过get把key移到尾部，更新node的value值
            if (get(key) != -1) { // get method will move the key to the end of the linked list
                ListNode prev = keyToPrev.get(key);
                prev.next.val = value;
                return;
            }

            // .2 key不在缓存中，要新增这个key
            //     .2.1 内存没满，直接新增数据放在尾部
            if (size < capacity) {
                size ++;
                ListNode curr = new ListNode(key, value);
                tail.next = curr; // 刚开始的时候，dummy和tail是一致的，所以这里dummy也就指向了第一个点
                keyToPrev.put(key, tail); // 当前的key，以及对应节点的前驱节点

                tail = curr; // tail指向当前元素（即尾部元素）
                return;
            }

            //     .2.2 如果内存满了，直接更新头部元素的value值并移到尾部（相当于删了头部元素，添加了尾部元素）
            ListNode first = dummy.next;
            keyToPrev.remove(first.key); // 删除hash表中的头部元素

            first.key = key;
            first.val = value;
            keyToPrev.put(first.key, dummy); // key和前驱节点

            moveToTail(key);
        }

        private void moveToTail(int key) { // 把key对应的节点放到尾部（最近使用的位置）
            ListNode prev = keyToPrev.get(key);
            ListNode curr = prev.next;

            if (tail == curr) {
                return;
            }

            // 移到尾部操作
            prev.next = curr.next;
            tail.next = curr;
            curr.next = null;

            // 更改这次变换了的两个节点对应的key的前驱节点，在hash表中
            keyToPrev.put(prev.next.key, prev);
            keyToPrev.put(curr.key, tail);

            tail = curr;
        }
    }

    // .2. 双链表方法
    public class LRUCacheSolution2 {
        private class Node{
            Node prev;
            Node next;
            int key;
            int value;

            public Node(int key, int value) {
                this.key = key;
                this.value = value;
                this.prev = null;
                this.next = null;
            }
        }

        private int capacity;
        private Map<Integer, Node> hash;
        private Node head;
        private Node tail;

        public LRUCacheSolution2(int capacity) {
            this.capacity = capacity;
            this.hash = new HashMap<Integer, Node>();
            this.head = new Node(-1, -1);
            this.tail = new Node(-1, -1);

            tail.prev = head;
            head.next = tail;
        }

        public int get(int key) {
            if (!hash.containsKey(key)) {
                return -1;
            }

            // 如果存在
            // 先删除当前节点
            Node curr = hash.get(key);
            curr.prev.next = curr.next;
            curr.next.prev = curr.prev;

            // 再当前节点添加到末尾（因为最近使用）
            addToTail(curr);

            return curr.value;
        }

        public void set(int key, int value) {
            if (get(key) != -1) { // .1 缓存中存在node，直接移到末尾（get函数可以实现移到末尾）
                hash.get(key).value = value; // 更改value值
                return;
            }

            // .2 缓存中不存在node，要新建且放入队尾
            if (hash.size() == capacity) { // 存储空间已满，需要先删除头节点，再添加尾节点（后面一起操作）
                hash.remove(head.next.key);
                head.next = head.next.next;
                head.next.prev = head;
            }

            // 添加新节点至末尾
            Node newInsert = new Node(key, value);
            hash.put(key, newInsert);
            addToTail(newInsert);

            // 添加和删除要同时维护hash表和链表结构
        }


        private void addToTail(Node curr) { // 把当前节点添加到末尾
            // current和tail的prev连起来
            curr.prev = tail.prev;
            curr.prev.next = curr;
            // current和tail连起来
            tail.prev = curr;
            curr.next = tail;
        }
    }
}
