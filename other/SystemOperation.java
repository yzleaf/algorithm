package other;

import java.util.*;

public class SystemOperation {
    // 1. read characters from file multiple calls
    // 接口：int read4(char * buf)一次从文件中读取 4 个字符。
    // 返回值是实际读取的字符数。例如，如果文件中只剩下 3 个字符，则返回 3。
    // 使用read4 接口，实现从文件读取 n 个字符的函数int read(char * buf，int n)。
    // Input:
    // "filetestbuffer"
    // read(6),read(5),read(4),read(3),read(2),read(1),read(10)
    // Output:
    // 6, buf = "filete"
    // 5, buf = "stbuf"
    // 3, buf = "fer"
    // 0, buf = ""
    // 0, buf = ""
    // 0, buf = ""
    // 0, buf = ""
    public class ReadSolution {
        char[] buffer = new char[4];
        int head = 0, tail = 0;

        public int read(char[] buf, int n) {
            int i = 0;
            while (i < n) {
                if (head == tail) { // queue空
                    head = 0;
                    tail = read4(buffer); // 读取4个数，tail自动改
                    if (tail == 0) { // 没有数了
                        break;
                    }
                }
                while (i < n && head < tail) {
                    buf[i] = buffer[head];
                    i++;
                    head++;
                }
            }
            return i;
        }
        private int read4(char[] buffer) {
            return 1;
        }
    }

    // 2. strings serialization
    // 设计一个将字符串列表编码为字符串的算法
    // 请实现 encode 和 decode
    // 输入： ["we", "say", ":", "yes"]
    // 输出： ["we", "say", ":", "yes"]
    // 解释： 一种可能的编码方式为： "we:;say:;:::;yes"
    public class StringsSerializationSolution {
        // 方法1 长度+$
        public String encode1(List<String> strs) {
            StringBuilder result = new StringBuilder();
            for (String str : strs) {
                result.append(String.valueOf(str.length())+ "$");
                result.append(str);
            }
            return result.toString();
        }
        public List<String> decode1(String str) {
            List<String> result = new LinkedList<String>();
            int start = 0;

            while(start < str.length()) {
                int index = str.indexOf('$', start); // 从start开始，第一个返回$的位置，没有的话返回-1
                int size = Integer.parseInt(str.substring(start, index));
                result.add(str.substring(index + 1, index + size + 1));
                start = index + size + 1;
            }
            return result;
        }

        // 方法2 仿照转义字符的表示方法：用":;"表示字符串连接符，"::"表示":"自己本身
        public String encode2(List<String> strs) {
            StringBuilder result = new StringBuilder();
            for (String str : strs) {
                for (char c : str.toCharArray()) {
                    if (c == ':') {
                        result.append("::");
                    } else {
                        result.append(c);
                    }
                }
                result.append(":;");
            }
            return result.toString();
        }
        public List<String> decode2(String str) {
            List<String> result = new LinkedList<String>();
            StringBuilder item = new StringBuilder();

            char[] sc = str.toCharArray();
            int i = 0;
            while (i < str.length()) {
                if (sc[i] == ':') { // 要看转义
                    if (sc[i + 1] == ';') { // 分隔符
                        result.add(item.toString());
                        item = new StringBuilder();
                    } else { // :自己
                        item.append(sc[i + 1]);
                    }
                    i += 2;
                } else { // 普通情况
                    item.append(sc[i]);
                    i += 1;
                }
            }

            return result;
        }
    }

    // 3. 最长绝对文件路径 · system longest file path
    // 假设我们通过以下的方式用字符串来抽象我们的文件系统：
    // 字符串"dir\n\tsubdir1\n\tsubdir2\n\t\tfile.ext"代表了
    // dir
    //     subdir1
    //     subdir2
    //         file.ext
    // 最长的绝对路径是“dir/subdir2/subsubdir2/file2.ext”，其长度为 32 (不包括双引号，\n \t)
    public class LengthLongestPathSolution {
        // 滚动得到每一层深度的最大路径长度
        public int lengthLongestPath(String input) {
            int result = 0;
            int[] pathLength = new int[input.length() + 1];

            for (String str : input.split("\n")) { // 按\n分隔层，split必须按string分隔，所以""，但\n和\t的长度其实只有1，当做char

                String name = str.replaceAll("\t", ""); // 删掉所有的\t
                int depth = str.length() - name.length(); // 可以得到几个\t，就在第几层
                if (name.contains(".")) { // 是文件
                    result = Math.max(result, pathLength[depth] + name.length());
                } else { // 不是文件
                    pathLength[depth + 1] = pathLength[depth] + name.length() + 1; // 当前深度文件夹长度 + 当前文件名长度 + "/"
                }
            }
            return result;
        }
    }

}
