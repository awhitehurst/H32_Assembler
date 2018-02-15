/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package assembler;

import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author Alan
 */
public class ByteOrderingOutputStream extends OutputStream {
    
    public ByteOrderingOutputStream(OutputStream out){
        this.out = out;
    }
    
    public void write(byte b){
        byte [] ba = new byte[1];
        ba[0] = b;
        try {
            out.write(ba);
        } catch (IOException ex){
            System.err.println(ex.getMessage());
        }
    }

    @Override
    public void write(int i) {
        byte [] b = new byte[2];
        b[1] = (byte)(i >> 8);
        b[0] = (byte) i;
        try {
            out.write(b);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }
    
    public void write(int [] i) {
        for(int j=0;j<i.length;++j){
            write(i[j]);
        }
    }
    
    @Override
    public void flush() throws IOException{
        out.flush();
    }
    
    @Override
    public void close() throws IOException {
        out.close();
    }
    
    private OutputStream out;
    
}
