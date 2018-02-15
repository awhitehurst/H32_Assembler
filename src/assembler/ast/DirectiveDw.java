package assembler.ast;

import assembler.H32OutputStream;
import assembler.SymbolTable;

/**
 *
 * @author alan.whitehurst
 */
public class DirectiveDw extends Directive {

    public DirectiveDw(String source, int srcLine, int prgLine, String label) {
        super(source, srcLine, prgLine, label);
    }
    
    public DirectiveDw(Directive dline) {
        super(dline.source, dline.srcLine, dline.relAddr, dline.label);
        this.operator = dline.operator;
    }
    
    public void toHeader(H32OutputStream boos, SymbolTable symtab) {
        
    }
    
    public void toMac(H32OutputStream boos, SymbolTable symtab) {
        for(Operand operand : operands){
            int [] results = operand.toMac(symtab);
            boos.write(results);
        }
    }
    
    public int getMacSize(){
       if(operands.isEmpty()){
          return 1;
       } else {
          return operands.get(0).getSize();
       }
    }

}
