package array;

public class HighFrequency {
    // 1. 字符同构 · Strings Homomorphism
    // 两个字符串是同构的如果 s 中的字符可以被替换得到 t。
    // 所有出现的字符必须用另一个字符代替，同时保留字符串的顺序。 没有两个字符可以映射到同一个字符，但一个字符可以映射到自己。
    // 输入 : s = "paper", t = "title"
    // 输出 : true
    // 说明 : p -> t, a -> i, e -> l, r -> e。
    public class IsIsomorphicSolution {
        public boolean isIsomorphic(String s, String t) {
            char[] sc = s.toCharArray();
            char[] tc = t.toCharArray();

            // 从s到t遍历一遍每个字母，确认映射
            int[] mapStoT = new int[256]; // ASCII范围0-255
            // char字母会直接变成对应ASCII的数字,进而成为数组下标
            for (int i = 0; i < s.length(); i++) {
                if (mapStoT[sc[i]] == 0 ) { // 还未映射过
                    mapStoT[sc[i]] = tc[i];
                } else { // 前面已经映射过，需要比较
                    if (mapStoT[sc[i]] != tc[i]) {
                        return false;
                    }
                }
            }

            // 从t到s遍历一遍每个字母，确认映射
            // 两个方向都要遍历，为了避免aabb -> cccc这种情况第一个方向遍历是查不到问题
            int[] mapTtoS = new int[256];
            for (int i = 0; i < t.length(); i++) {
                if (mapTtoS[tc[i]] == 0) {
                    mapTtoS[tc[i]] = sc[i];
                } else {
                    if (mapTtoS[tc[i]] != sc[i]) {
                        return false;
                    }
                }
            }

            return true;
        }
    }
}
