package other;

import java.util.*;

public class UnionFindProblems {
    class UnionFind {
        int[] parent;
        int[] rank;

        public UnionFind(int n) {
            parent = new int[n];
            for (int i = 0; i < n; i ++) {
                parent[i] = i;
            }
            rank = new int[n];
        }

        public int find(int index) {
            if (parent[index] != index) {
                parent[index] = find(parent[index]);
            }
            return parent[index];
        }
        public boolean union(int index1, int index2) {
            // 如果不判断深度，就用这个最简单的方法
            // parent[find(index2)] = find(index1);

            int root1 = find(index1);
            int root2 = find(index2);
            if (root1 == root2) { // 本来就在一个地方，拥有同样的parent
                return false;
            }

            // root1 != root2
            if (rank[root1] > rank[root2]) {
                parent[root2] = root1;
            } else if (rank[root1] < rank[root2]) {
                parent[root1] = root2;
            } else { // ==
                parent[root2] = root1;
                rank[root1] ++;
            }

            return true;
        }
    }

    // 721 Accounts Merge
    // 输入数组[name, email, email, email, ...]
    // 合并相同的email到同一个账户
    public class AccountsMergeSolution {
        public List<List<String>> accountsMerge(List<List<String>> accounts) {

            // 1. 建立email -> index和email -> name的Hash图
            // 有index是因为要建立并查集
            Map<String, Integer> emailIndex = new HashMap<>();
            Map<String, String> emailName = new HashMap<>();
            int emailNum = 0;
            for (List<String> account : accounts) {
                String name = account.get(0);

                for (int i = 1; i < account.size(); i ++) {
                    String email = account.get(i);
                    if (!emailIndex.containsKey(email)) {
                        emailIndex.put(email, emailNum);
                        emailNum ++;
                        emailName.put(email, name);
                    }
                }
            }

            // 2. 同一个name下的email需要连接起来（在并查集中）
            UnionFind uf = new UnionFind(emailNum);
            for (List<String> account : accounts) {
                String firstEmail = account.get(1);
                int firstEmailIndex = emailIndex.get(firstEmail);

                for (int i = 2; i < account.size(); i ++) {
                    String nextEmail = account.get(i);
                    int nextEmailIndex = emailIndex.get(nextEmail);
                    uf.union(firstEmailIndex, nextEmailIndex);
                }
            }

            // 3. 以root为基准，把所有连接的email添加进同一个root来
            Map<Integer, List<String>> indexAllEmail = new HashMap<>();
            for (String email : emailIndex.keySet()) {
                // 遍历每一个email，找到根index
                int currIndex = emailIndex.get(email);
                int currRootIndex = uf.find(currIndex);
                indexAllEmail.putIfAbsent(currRootIndex, new ArrayList<String>());
                indexAllEmail.get(currRootIndex).add(email);
            }

            // 4. 构造答案
            List<List<String>> res = new ArrayList<>();
            for (List<String> allEmail : indexAllEmail.values()) {
                Collections.sort(allEmail);
                String name = emailName.get(allEmail.get(0));
                List<String> account = new ArrayList<>();
                account.add(name);
                account.addAll(allEmail);

                res.add(account);
            }

            return res;
        }
    }
}
