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
public class OperandLabel extends Operand {

    public OperandLabel(String token) {
        this(token, 0);
    }

    public OperandLabel(String token, int offset) {
        super(token);
        this.offset = offset;
    }
    
    public int[] toHeader(SymbolTable symtab) {
        int[] mac = new int[1];
        if (symtab.contains(token)) {
            int addr = symtab.getLineNum(token);
            addr += offset;
            mac[0] = addr;
        } else {
            System.err.println("Undefined label in operand field: " + token);
        }
        return mac;
    }

    @Override
    public int[] toMac(SymbolTable symtab) {
        int[] mac = new int[1];
        if (symtab.contains(token)) {
            int addr = symtab.getLineNum(token);
            addr += offset;
            mac[0] = addr;
        } else {
            System.err.println("Undefined label in operand field: " + token);
        }
        return mac;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    @Override
    public int getSize() {
        return 1;
    }

    @Override
    public String toString() {
        return token;
    }

    private int offset;
}
