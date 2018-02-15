/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package assembler.ast;

import assembler.SymbolTable;

/**
 *
 * @author Alan
 */
public class OperandString extends Operand {

    public OperandString(String token) {
        super(token);
        nullTerminated = token.startsWith("\"");
    }

    public boolean isNullTerminated() {
        return nullTerminated;
    }

    @Override
    public int[] toMac(SymbolTable symtab) {
        int size = getSize();
        int[] mac = new int[size];
        int i=0;
        for (int p = 1; p < token.length() - 1; ++p) {
            if (token.charAt(p) != '\\') {
                mac[i++] = (int) token.charAt(p);
            } else if (token.charAt(p + 1) == '\\') {
                mac[i++] = (int) '\\';
                p += 2;
            } else if (token.charAt(p + 1) == 'n') {
                mac[i++] = (int) '\n';
                p += 2;
            } else if (token.charAt(p + 1) == 'r') {
                mac[i++] = (int) '\r';
                p += 2;
            } else if (token.charAt(p + 1) == 't') {
                mac[i++] = (int) '\t';
                p += 2;
            } else {
                mac[i++] = (int) token.charAt(p + 1);
                p+=2;
            }
        }
        if (token.charAt(0) == '"') {
            mac[size - 1] = 0;
        }
        return mac;
    }

    @Override
    public int getSize() {
        int size = 0;
        char quoteChar = token.charAt(0);
        for (int i = 1; i < token.length() - 1; ++i) {
            if (token.charAt(i) != '\\') {
                ++size;
            } else if (token.charAt(i + 1) == '\\') {
                i += 2;
                ++size;
            }
        }
        if (quoteChar == '"') {
            ++size;
        }
        return size;
    }
    
    public String toString(){
        return token;
    }
    
    private boolean nullTerminated;
}
