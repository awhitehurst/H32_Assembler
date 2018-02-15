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
public class OperandNumber extends Operand {
    
    public OperandNumber(String token){
        this(token, 0);
    }

    public OperandNumber(String token, int offset) {
        super(token);
        this.value = Integer.parseInt(token);
        this.offset = offset;
    }

    @Override
    public int[] toMac(SymbolTable symtab) {
        int[] mac = new int[1];
        mac[0] = value + offset;
        return mac;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }  
    
    @Override
    public int getSize(){
        return 1;
    }
    
    public int getTotal(){
        return value + offset;
    }
    
    @Override
    public String toString(){
        if(offset!=0){
            return value + (offset>0?"+":"-") + offset;
        } else {
            return Integer.toString(value);
        }
    }
    
    private int value;
    private int offset;
}
