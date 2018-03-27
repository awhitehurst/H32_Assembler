package assembler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alan.whitehurst
 */
public class H32OutputStream extends OutputStream {

    private OutputStream out;

    public H32OutputStream(OutputStream out) {
        this.out = out;
    }

    @Override
    public void write(int i) {
        try {
            out.write(i >> 24);
            out.write(i >> 16);
            out.write(i >> 8);
            out.write(i);
        } catch (IOException ex) {
            System.err.println("H32OutputStream IO Error in write((int)" + i + ")");
        }
    }

    public void write(short i) {
        try {
            out.write(i >> 8);
            out.write(i);
        } catch (IOException ex) {
            System.err.println("H32OutputStream IO Error in write((short)" + i + ")");
        }
    }

    public void write(byte b) {
        try {
            out.write(b);
        } catch (IOException ex) {
            System.err.println("H32OutputStream IO Error in write((byte)" + b + ")");
        }
    }

    public void write(char c) {
        write((short) c);
    }

    public void write(int[] a) {
        for (int i = 0; i < a.length; ++i) {
            write(a[i]);
        }
    }

    public void write(char[] a) {
        for (int i = 0; i < a.length; ++i) {
            write((short)a[i]);
        }
    }
    
    public void write(byte[] a){
        for (int i = 0; i < a.length; ++i) {
            write(a[i]);
        }
    }

    @Override
    public void flush() throws IOException {
        out.flush();
    }

    @Override
    public void close() throws IOException {
        out.close();
    }

}
