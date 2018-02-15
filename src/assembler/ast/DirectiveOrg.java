package assembler.ast;

import assembler.H32OutputStream;
import assembler.SymbolTable;

/**
 *
 * @author alan.whitehurst
 */
public class DirectiveOrg extends Directive {

    public DirectiveOrg(String source, int srcLine, int prgLine, String label) {
        super(source, srcLine, prgLine, label);
    }

    public DirectiveOrg(Directive dline) {
        super(dline.source, dline.srcLine, dline.relAddr, dline.label);
        this.operator = dline.operator;
    }

    @Override
    public void toMac(H32OutputStream boos, SymbolTable symtab) {
        
    }

    @Override
    public void toHeader(H32OutputStream boos, SymbolTable symtab) {
        
    }
    
    @Override
    public int getNextRelAddr() {
        return ((OperandNumber)operands.get(0)).getTotal();
    }
}
