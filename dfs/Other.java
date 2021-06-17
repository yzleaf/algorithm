package dfs;

import java.util.*;

public class Other {
    // 473 Matchsticks to Square
    // 输入为小女孩拥有火柴的数目，每根火柴用其长度表示。
    // 输出即为是否能用所有的火柴拼成正方形
    public class MakesquareSolution {
        public boolean makesquare(int[] matchsticks) {
            int total = 0;
            for (int val : matchsticks) { // 所有火柴长度
                total += val;
            }

            if (total == 0 || total % 4 != 0) { // 不是正方形
                return false;
            }

            Arrays.sort(matchsticks); // 升序，保证先取大的火柴（因为后面递归currIndex是减小的）。因为不是Integer，不能用Collections.reverseOrder
            return dfs(matchsticks, matchsticks.length - 1, total / 4, new int[4]);

        }
        // 用数组当前火柴依次放在四条边看结果
        // currIndex当前访问的火柴位置
        // target正方形最终的边长
        // sides当前正方形四边的长度（长度为4的数组）
        public boolean dfs(int[] nums, int currIndex, int target, int[] sides) {
            if (currIndex < 0) { // 所有火柴访问完
                return sides[0] == sides[1] && sides[1] == sides[2] && sides[2] == sides[3]; // 四边是否相等（true or false）
            }

            for (int i = 0; i < sides.length; i++) {
                // 当前火柴放到sides[i]这个边上
                // sides[i] == sides[i - 1]即上一个分支的值和当前分支的一样，上一个分支没有成功，说明这个分支也不会成功，直接跳过即可。
                if (sides[i] + nums[currIndex] > target || (i > 0 && sides[i] == sides[i-1])) {
                    continue;
                }
                sides[i] += nums[currIndex];
                if (dfs(nums, currIndex - 1, target, sides)) {
                    return true;
                }
                sides[i] -= nums[currIndex];
            }

            return false;
        }
    }

    // 22. Generate Parentheses
    // n 代表生成括号的对数，返回有效的括号组合
    // 输入：n = 3
    // 输出：["((()))","(()())","(())()","()(())","()()()"]
    public class GenerateParenthesisSolution {
        // 左括号的个数大于右括号，再添加是有效的
        // 不断递归添加括号
        public List<String> generateParenthesis(int n) {
            List<String> result = new ArrayList<>();
            if (n == 0) {
                return result;
            }
            dfs(result, "", 0, 0, n);
            return result;
        }
        private void dfs(List<String> res, String curr, int left, int right, int n) {
            if (left == n && right == n) {
                res.add(curr);
                return;
            }

            if (left < right) { // 右括号多，不合法，直接返回
                return;
            }

            if (left < n) {
                dfs(res, curr + "(", left + 1, right, n);
            }
            if (right < n) {
                dfs(res, curr + ")", left, right + 1, n);
            }
        }
    }

}
