package twopointer;

import java.io.*;
import java.net.*;
import java.util.*;

public class Test {

    private static final String ADDR = "http://bholt.org/ssh/short.txt";

    public static void main(String[] args) throws IOException {

//        List<String> list = new ArrayList<>();
//        list.add("abe");
//        list.add("bca");
//        list.add("abdu");
//
//        System.out.println(list);
//        System.out.println("------");
//        Collections.sort(list);
//        System.out.println(list);
//
//        int[] a = new int[]{1,2,3};
//        System.out.println(a);

//        URL url = new URL(ADDR);
//        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
//        String s;
//        while ((s = reader.readLine()) != null) {
//            System.out.println(s);
//        }
//        reader.close();
        String addr = "http://bholt.org/ssh/short.txt";
        System.out.println(readURL(addr));

        //System.out.println(readDevice("D:/test.txt"));
    }

    public static String readURL(String addr) throws IOException {
        URL url = new URL(addr);
        BufferedReader buffer = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream())); // url.openStream()

        StringBuffer strRes = new StringBuffer();
        String readLineStr = null;
        while ((readLineStr = buffer.readLine()) != null) {
            strRes.append(readLineStr).append("\r\n"); // 换行
        }

        buffer.close();

        return  strRes.toString();
    }

    public static String readDevice(String addr) throws IOException {

//        String s = null;
//        try {
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        BufferedReader buffer = new BufferedReader(new FileReader(addr));

        StringBuffer strRes = new StringBuffer();
        String readLineStr = null;
        while ((readLineStr = buffer.readLine()) != null) {
            strRes.append(readLineStr).append("\r\n"); // 换行
        }

        buffer.close();

        return strRes.toString();

    }
}
