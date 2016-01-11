import java.util.*;
import java.security.*;
import java.io.*;

public class ComputeSHA {
    String filename;

	public static void main (String args[]) {
        
        if (args.length == 0) {
            System.out.println("Please input filename");
            return;
        }
        else {
            ComputeSHA cSHA = new ComputeSHA(args[0]);
            System.out.println(cSHA.go());
        }
	}
    
    public ComputeSHA(String str) {
        filename = str;
    }
    
    public String go() {
        String input = readfile(filename);
        return encryptSHA1(input);
    }
    
    public String readfile(String str) {
        String input = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            StringBuilder sb = new StringBuilder("");
            String line;
            while((line = br.readLine()) != null)
                sb.append(line);
            br.close();
            input = sb.toString();
        } catch(IOException ex) {
            ex.printStackTrace();
        }
        return input;
    }
    
    public String encryptSHA1(String str) {
        String result = "";
        try {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] arr = md.digest(str.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0 ; i < arr.length ; i++) {
            sb.append(String.format("%02x", arr[i]));
        }
        result = sb.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }
}