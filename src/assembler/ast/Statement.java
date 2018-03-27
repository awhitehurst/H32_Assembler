package assembler.ast;

import assembler.H32OutputStream;
import assembler.SymbolTable;

/**
 *
 * @author Alan
 */
public class Statement extends ALine {

    public Statement(String source, int srcLine, int prgLine, String label) {
        super(source, srcLine, prgLine, label);
    }

    @Override
    public void toHeader(H32OutputStream boos, SymbolTable symtab) {
        if (isRelative()) {
            if (operands.size() == 1 && (operands.get(0) instanceof OperandLabel) && symtab.isExtern(operands.get(0).getToken())) {
                boos.write((byte) 'E');
                boos.write(getRelAddr());
                String label = operands.get(0).getToken();
                char [] chars = label.toCharArray();
                for(int i=0;i<chars.length;++i){
                    boos.write((byte)chars[i]);
                }
                boos.write((byte)0);
            } else {
                // output relative address entries to the header
                int word = ((byte)'R') << 24;
                word = word + getRelAddr();
                boos.write(word);
            }
        }
    }

}
