package assembler.ast;

import assembler.H32OutputStream;
import assembler.SymbolTable;

/**
 *
 * @author Alan
 */
public class Directive extends ALine {

    public Directive(String source, int srcLine, int prgLine, String label) {
        super(source, srcLine, prgLine, label);
        this.setMacSize(0);
    }
    
    public void toHeader(H32OutputStream boos, SymbolTable symtab) {
        
    }
    
    public void toMac(H32OutputStream boos, SymbolTable symtab) {
        
    }
    
}
