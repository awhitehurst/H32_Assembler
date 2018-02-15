package assembler.ast;

/**
 *
 * @author alan.whitehurst
 */
public class DirectiveExtern extends Directive {
    
    public DirectiveExtern(String source, int srcLine, int prgLine, String label) {
        super(source, srcLine, prgLine, label);
    }

    public DirectiveExtern(Directive dline) {
        super(dline.source, dline.srcLine, dline.relAddr, dline.label);
        this.operator = dline.operator;
    }
    
    @Override
    public int getNextRelAddr() {
        return this.relAddr;
    }
    
}
