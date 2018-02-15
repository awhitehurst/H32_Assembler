package assembler.ast;

import assembler.H32OutputStream;
import assembler.SymbolTable;

/**
 *
 * @author alan.whitehurst
 */
public class DirectivePublic extends Directive {

   public DirectivePublic(String source, int srcLine, int prgLine, String label) {
      super(source, srcLine, prgLine, label);
   }

   public DirectivePublic(Directive dline) {
      super(dline.source, dline.srcLine, dline.relAddr, dline.label);
      this.operator = dline.operator;
   }

   @Override
   public void toHeader(H32OutputStream boos, SymbolTable symtab) {
      for(Operand operand : getOperands()){
         boos.write((byte) 'P');
         boos.write(symtab.getLineNum(operand.getToken()));
         String label = operand.getToken();
         char[] chars = label.toCharArray();
         for (int i = 0; i < chars.length; ++i) {
            boos.write((byte) chars[i]);
         }
         boos.write((byte) 0);
      }
   }

   @Override
   public int getNextRelAddr() {
      return this.relAddr;
   }

}
