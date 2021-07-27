package math;

import datastructure.*;

public class Other {
    // 1. big integer addition
    // 415
    public class AddStringsSolution {

        public String addStrings(String num1, String num2) {
            int i = num1.length() - 1;
            int j = num2.length() - 1;
            int carry = 0; // 进位
            String result = "";

            while (i >= 0 || j >= 0) {
                int curSum = carry;
                if (i >= 0) {
                    curSum += num1.charAt(i) - '0'; // 得到当前位的数据
                    i--;
                }
                if (j >= 0) {
                    curSum += num2.charAt(j) - '0';
                    j--;
                }
                result = curSum % 10 + result; // 当前位的结果拼上之前算出的String
                carry = curSum / 10; // 进位留给下一位
            }

            if (carry > 0) {
                result = "1" + result;
            }

            return result;
        }
        // 用string builder更快一点, append结果，最后reverse
    }

    // 2. 二进制求和 · Add Binary
    // 67
    // 给定两个二进制字符串，返回他们的和（用二进制表示）
    public class AddBinarySolution {
        public String addBinary(String num1, String num2) {
            int i = num1.length() - 1;
            int j = num2.length() - 1;
            int carry = 0; // 进位
            StringBuilder result = new StringBuilder();

            while (i >= 0 || j >= 0) {
                int curSum = carry;
                if (i >= 0) {
                    curSum += num1.charAt(i) - '0'; // 得到当前位的数据
                    i--;
                }
                if (j >= 0) {
                    curSum += num2.charAt(j) - '0';
                    j--;
                }
                result.insert(0, curSum % 2); // 在最前面加上当前和
                carry = curSum / 2; // 进位留给下一位
            }

            if (carry > 0) {
                result.insert(0, carry); // carry = 1
            }

            return result.toString();
        }
    }

    // 3. 链表求和 · Add Two Numbers
    // Input: 7->1->6->null, 5->9->2->null
    // Output: 2->1->9->null
    // Explanation: 617 + 295 = 912, 912 to list:  2->1->9->null
    public class AddTwoListNodesSolution {
        public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
            ListNode dummy = new ListNode(0);
            ListNode tail = dummy;

            ListNode i = l1;
            ListNode j = l2;
            int carry = 0;

            while (i != null || j != null) {
                int curSum = carry;

                if (i != null) {
                    curSum += i.val;
                    i = i.next;
                }
                if (j != null) {
                    curSum += j.val;
                    j = j.next;
                }

                tail.next = new ListNode(curSum % 10);
                tail = tail.next;
                carry = curSum / 10;
            }

            if (carry > 0) {
                tail.next = new ListNode(carry);
            }

            return dummy.next;
        }
    }


    // 4. big integer multiplication
    // 43
    public class MultiplyStringsSolution {
        public String multiply(String num1, String num2) {
            int len1 = num1.length(), len2 = num2.length();
            int carry = 0;
            int[] num3 = new int[len1 + len2]; // 两个3位数相乘最大为6位数

            // 总长度是i+j+1，所以不要想错后面num3[i]中i所在的位置
            for (int i = len1 - 1; i >= 0; i--) {
                carry = 0; // 被乘数每一轮遍历到新的位，进位应该清零
                for (int j = len2 - 1; j >= 0; j--) {
                    // i和j相乘对应的位是i+j+1
                    int product = carry + (num2.charAt(j) - '0') * (num1.charAt(i) - '0') + num3[i+j+1]; // num3[i+j+1]其他数相乘留下来的
                    num3[i + j + 1] = product % 10;
                    carry = product / 10;
                }
                num3[i] = carry; // 乘数遍历结束，j停在-1（i+j+1=i）。乘数当前位*被乘数积的进位
            }

            StringBuilder result = new StringBuilder();
            // 从左往右，数组中开头为0的数是多出来的（乘积没有大到这个位置），需要跳过
            int index = 0;
            while (index < len1 + len2 - 1 && num3[index] == 0) { // -1是因为避免两个0相乘被跳过
                index ++;
            }

            while (index < len1 + len2) {
                result.append(num3[index]);
                index ++;
            }

            return result.toString();
        }
    }

    // 5. x的n次幂 · Pow(x, n)
    // 50
    // Time: O(nLogn)
    public class MyPowSolution {
        public double myPow(double x, int n) {
            long nL = (long)n; // 避免负数情况，minValue取-n会溢出。或者取相反数时改成-(n+1)最后结果再乘以一个x
            if (nL >= 0) {
                return pow(x, nL);
            } else {
                return 1.0 / pow(x, -nL);
            }
        }
        private double pow(double x, long n) {
            if (n == 0) {
                return 1;
            }

            double y = pow(x, n/2);
            if (n % 2 == 0) {
                return y * y;
            } else { // 奇数次幂，多乘以一个x本身
                return y * y * x;
            }
        }
    }

}
