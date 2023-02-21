import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Scanner;

public class LinkShortener {
    private static HashMap<String, String> map = new HashMap<>();
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int BASE = ALPHABET.length();

    private static String encode(BigInteger num) {
        StringBuilder str = new StringBuilder();
        while (num.compareTo(BigInteger.ZERO) > 0) {
            int rem = num.mod(BigInteger.valueOf(BASE)).intValue();
            str.insert(0, ALPHABET.charAt(rem));
            num = num.divide(BigInteger.valueOf(BASE));
        }
        return str.toString();
    }

    private static BigInteger decode(String str) {
        BigInteger num = BigInteger.ZERO;
        for (int i = 0; i < str.length(); i++) {
            num = num.multiply(BigInteger.valueOf(BASE));
            num = num.add(BigInteger.valueOf(ALPHABET.indexOf(str.charAt(i))));
        }
        return num;
    }

    public static String getMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String shortenURL(String longURL) {
        String hash = getMD5(longURL);
        if (map.containsKey(hash)) {
            return map.get(hash);
        } else {
            String shortURL = encode(decode(hash).mod(BigInteger.valueOf((long) Math.pow(BASE, 6))));
            map.put(hash, shortURL);
            return shortURL;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the long URL: ");
        String longURL = sc.nextLine();
        String shortURL = shortenURL(longURL);
        System.out.println("Short URL: " + shortURL);
        sc.close();
    }
}
