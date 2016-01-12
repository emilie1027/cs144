import java.util.*;
import java.security.*;
import java.io.*;

public class ComputeSHA {
    String filename;

	public static void main (String args[]) {
        try {
        if (args.length == 0) {
            System.out.println("Please input filename");
            return;
        }
        else {
            ComputeSHA cSHA = new ComputeSHA(args[0]);
            System.out.println(cSHA.go());
        }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
	}
    
    public ComputeSHA(String str) {
        filename = str;
    }
    
    public String go() throws Exception{
        byte[] arr = readfile(filename);
        return encryptSHA1(arr);
    }
    
    public byte[] readfile(String str) throws Exception{
            InputStream is = new FileInputStream(filename);
            byte[] buffer = new byte[1024];
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            int numRead;
            while ((numRead = is.read(buffer)) != -1) {
                md.update(buffer, 0, numRead);
            }
            is.close();
            return md.digest();
        /*
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
         */
    }
    
    public String encryptSHA1(byte[] arr) {
        String result = "";
        try {
            //byte[] arr = md.digest(str.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0 ; i < arr.length ; i++) {
                //sb.append(String.format("%1$02X", arr[i]));
                sb.append(Integer.toString((arr[i] & 0xff) + 0x100, 16).substring(1));
            }
            result = sb.toString();
             
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }
}