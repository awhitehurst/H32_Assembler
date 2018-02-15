/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package assembler.ast;

import assembler.SymbolTable;
import java.util.ArrayList;

/**
 *
 * @author Alan
 */
public class OperandDup extends Operand {

    public OperandDup(String token) {
        super(token);
    }
    
    public OperandDup(ArrayList<Operand> operands){
        super("dup");
        if((operands.get(0) instanceof OperandNumber)){
            count = ((OperandNumber)operands.get(0)).getTotal();
        } else {
            count = 1;
        }
        if((operands.get(2) instanceof OperandNumber)){
            value = ((OperandNumber)operands.get(2)).getTotal();
        } else {
            value = 0;
        }
    }

    @Override
    public int[] toMac(SymbolTable symtab) {
        int [] result = new int[count];
        for(int i=0;i<count;++i){
            result[i] = value;
        }
        return result;
    }

    @Override
    public int getSize() {
        return count;
    }
    
    public int getValue() {
        return value;
    }
    
    @Override
    public String toString(){
        return String.format("%d dup %d", count, value);
    }
    
    private int count;
    private int value;
    
}
