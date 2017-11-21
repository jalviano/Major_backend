package analysis.runners;

import java.io.OutputStream;
import java.security.DigestOutputStream;
import java.security.MessageDigest;

public class DeadEndDigestOutputStream extends DigestOutputStream {

    private static class DeadEndStream extends OutputStream {
        public void write(int b) {}
        public DeadEndStream() {}
    }

    public DeadEndDigestOutputStream() {
        super(new DeadEndStream(), null);
        try {
            setMessageDigest(MessageDigest.getInstance("SHA-1"));
        }
        catch (java.security.NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.out.println("According to http://docs.oracle.com/javase/7/docs/api/java/security/MessageDigest.html , java.security.MessageDigest *must* provide SHA-1. What happened?");
            System.exit(1);
        }
    }

    public String getDigestString() {
        // Gives the hash as a typical hex string.
        byte[] digestBytes = getMessageDigest().digest();
        StringBuffer sb = new StringBuffer();
        for (byte b : digestBytes) {
            sb.append(String.format("%02x", b & 0xff));
        }
        return sb.toString();
    }
}