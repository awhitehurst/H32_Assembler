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
          int word = ((byte)'P') << 24;
          word = word + symtab.getLineNum(operand.getToken());
          boos.write(word);
//         boos.write((byte) 'P');
//         boos.write(symtab.getLineNum(operand.getToken()));
         String label = operand.getToken() + "\0";
         while(label.length()%2!=0){
             label = label + "\0";
         }
         char[] chars = label.toCharArray();
         boos.write(chars);
//         for (int i = 0; i < chars.length; ++i) {
//            boos.write((byte) chars[i]);
//         }
//         boos.write((byte) 0);
      }
   }

   @Override
   public int getNextRelAddr() {
      return this.relAddr;
   }

}
