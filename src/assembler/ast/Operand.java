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
public abstract class Operand {
    
    public Operand(String token){
        this.token = token;
    }
    
    public String getToken(){
        return token;
    }
    
    public abstract int [] toMac(SymbolTable symtab);
    public abstract int getSize();
    
    protected String token;
    
}
