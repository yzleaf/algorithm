package twopointer;

public class TestRandom {

    private static String plusOne(String s, int pos) {
        char[] charArr = s.toCharArray();
        if (charArr[pos] == '9') {
            charArr[pos] = '0';
        } else {
            charArr[pos] += 1;
        }
        //return charArr.toString();
        return new String(charArr); // String.valueOf(charArr);
    }


    public static void main(String[] args) {
        String a = "1203";
        System.out.println(plusOne(a, 1));
    }
}
