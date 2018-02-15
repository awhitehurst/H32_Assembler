package assembler.parser;

import assembler.ast.Statement;
import assembler.SymbolTable;
import assembler.ast.ALine;
import assembler.ast.Directive;
import assembler.ast.DirectiveDw;
import assembler.ast.DirectiveEnd;
import assembler.ast.DirectiveExtern;
import assembler.ast.DirectiveOrg;
import assembler.ast.DirectivePublic;
import assembler.ast.Mneumonic;
import assembler.ast.Operand;
import assembler.ast.OperandDup;
import assembler.ast.OperandLabel;
import assembler.ast.OperandNumber;
import assembler.ast.OperandString;

/**
 *
 * @author Alan
 */
public class Parser {

   public Parser(SymbolTable symtab) {
      this.symtab = symtab;
   }

   public ALine parse(String source, int srcLine, int relAddr) throws SyntaxError {
      tok = new ParseTokenizer(source, ":+-; \t", true, true);
      this.source = source;
      this.srcLine = srcLine;
      this.relAddr = relAddr;
      String label = null;
      if (hasLabel()) {
         label = parseLabel();
      }
      if (isMneumonic(tok.peekToken())) {
         return parseStatement(label);
      } else {
         return parseDirective(label);
      }
   }

   private String parseLabel() throws SyntaxError {
      String label;
      if (!tok.hasMoreTokens()) {
         return null;
      }
      String token = tok.nextToken();
      if (!isValidLabel(token)) {
         throw new SyntaxError(String.format("Illegal label '%s' at line %d.", token, srcLine));
      }
      if (!tok.hasMoreTokens()) {
         throw new SyntaxError(String.format("Expecting ':' following '%s' at line %d.", token, srcLine));
      }
      String colon = tok.nextToken();
      if (!colon.equals(":")) {
         throw new SyntaxError(String.format("Label must be the first item on line %d", srcLine));
      }
      label = token;
      if (symtab.contains(label)) {
         throw new SyntaxError(String.format("Duplicate label: %s", label));
      }
      symtab.add(label, relAddr);
      return label;
   }

   private Statement parseStatement(String label) throws SyntaxError {
      Statement sLine = new Statement(source, srcLine, relAddr, label);
      parseMnemonic(sLine);
      parseOperands(sLine, Mneumonic.getInstance().getNumops(sLine.getOperator()));
      int limit = 0xffffffff;
      if (sLine.getOperands().size() > 0) {
         for (Operand operand : sLine.getOperands()) {
            if (operand instanceof OperandNumber) {
               OperandNumber oNum = (OperandNumber) operand;
//               if (oNum.getTotal() < 0 || oNum.getTotal() >= limit) {
//                  throw new SyntaxError("Address or operand out of range: " + oNum.getTotal());
//               }
            }
         }
      }
      return sLine;
   }

   private void parseMnemonic(Statement sline) throws SyntaxError {
      String mnemonic = tok.nextToken();
      if (!isMneumonic(mnemonic)) {
         throw new SyntaxError(String.format("expecting assembler mnemonic, saw '%s' at line %d.", mnemonic, srcLine));
      }
      sline.setOperator(mnemonic);
   }

   private void parseOperands(ALine aline, int numOperands) throws SyntaxError {
      if (numOperands == 0 && tok.hasMoreTokens()) {
         throw new SyntaxError("Too many operands.");
      } else if (numOperands == 0) {
         return;
      }
      if (numOperands > 0) {
         for (int i = 0; i < numOperands; ++i) {
            parseOperand(aline);
         }
         if (tok.hasMoreTokens()) {
            throw new SyntaxError("Too many operands.");
         }
      } else {
         while (tok.hasMoreTokens()) {
            if (tok.peekToken().equals(",")) {
               tok.nextToken();
            } else {
               parseOperand(aline);
            }
         }
         if (aline.getOperands().size() == 1 && (aline.getOperands().get(0) instanceof OperandString)) {
            aline.setMacSize(((OperandString) aline.getOperands().get(0)).getSize());
         } else if (aline.getOperands().size() == 3 && aline.getOperands().get(1).getToken().equalsIgnoreCase("dup")) {
            OperandDup dup = new OperandDup(aline.getOperands());
            aline.getOperands().clear();
            aline.getOperands().add(dup);
            aline.setMacSize(dup.getSize());
         }
      }
   }

   private void parseOperand(ALine aline) throws SyntaxError {
      if (!tok.hasMoreTokens()) {
         throw new SyntaxError("Missing required operand.");
      }
      String operand = tok.peekToken();
      if (operand.startsWith("\"") || operand.startsWith("'")) {
         parseStrExpr(aline);
      } else if (operand.equals("+") || operand.equals("-")) {
         operand = tok.concatToken();
         if (isValidNumber(operand)) {
            parseNumExpr(aline, operand);
         }
      } else if (isValidNumber(operand)) {
         parseNumExpr(aline);
      } else if (isValidLabel(operand)) {
         parseLabelExpr(aline);
      } else if (operand.startsWith("*")) {
         parseHereExpr(aline);
      } else {
         throw new SyntaxError("Illegal or unidentified operand '" + operand + "'.");
      }
   }

   private void parseLabelExpr(ALine aline) throws SyntaxError {
      String theLabel = "";
      theLabel += tok.nextToken();
      OperandLabel operand = new OperandLabel(theLabel);
      // optional offset
      String theOffset = "";
      if (tok.hasMoreTokens() && (tok.peekToken().equals("+") || tok.peekToken().equals("-"))) {
         theOffset += tok.nextToken();
         if (!tok.hasMoreTokens()) {
            throw new SyntaxError("Expecting number following +/- sign.");
         }
         theOffset += tok.nextToken();
         int offset = Integer.parseInt(theOffset);
         operand.setOffset(offset);
      }
      aline.setRelative(true);
      aline.addOperand(operand);
   }

   private void parseNumExpr(ALine aline) throws SyntaxError {
      // optional + or - at beginning of number
      String theNumber = "";
      if (tok.hasMoreTokens() && (tok.peekToken().equals("+") || tok.peekToken().equals("-"))) {
         theNumber += tok.nextToken();
      }
      if (!tok.hasMoreTokens() || !Character.isDigit(tok.peekToken().charAt(0))) {
         throw new SyntaxError("Expecting number following +/- sign.");
      }
      // main number
      theNumber += tok.nextToken();
      parseNumExpr(aline, theNumber);
   }

   private void parseNumExpr(ALine aline, String theNumber) throws SyntaxError {
      OperandNumber operand = new OperandNumber(theNumber);
      // optional offset
      String theOffset = "";
      if (tok.hasMoreTokens() && (tok.peekToken().equals("+") || tok.peekToken().equals("-"))) {
         theOffset += tok.nextToken();
         if (!tok.hasMoreTokens()) {
            throw new SyntaxError("Expecting number following +/- sign.");
         }
         theOffset += tok.nextToken();
         int offset = Integer.parseInt(theOffset);
         operand.setOffset(offset);
      }
      aline.addOperand(operand);
   }

   private void parseStrExpr(ALine aline) throws SyntaxError {
      String str = tok.nextToken();
      if (str.startsWith("'")) {
         if (!str.endsWith("'") || str.endsWith("\\'")) {
            throw new SyntaxError(String.format("unterminated string at line %d.", srcLine));
         }
      } else if (str.startsWith("\"")) {
         if (!str.endsWith("\"") || str.endsWith("\\\"")) {
            throw new SyntaxError(String.format("unterminated string at line %d.", srcLine));
         }
      }
      aline.addOperand(new OperandString(str));
   }

   private void parseHereExpr(ALine aline) throws SyntaxError {
      String str = tok.nextToken();
      if (!str.equals("*")) {
         throw new SyntaxError(String.format("expecting '*' on line %d.", srcLine));
      }
      // main number
      String theNumber = Integer.toString(aline.getRelAddr());
      OperandNumber operand = new OperandNumber(theNumber);
      // optional offset
      String theOffset = "";
      if ((tok.peekToken().equals("+") || tok.peekToken().equals("-"))) {
         theOffset += tok.nextToken();
         if (!tok.hasMoreTokens()) {
            throw new SyntaxError("Expecting number following +/- sign.");
         }
         theOffset += tok.nextToken();
         int offset = Integer.parseInt(theOffset);
         operand.setOffset(offset);
      }
      aline.addOperand(operand);
   }

   private Directive parseDirective(String label) throws SyntaxError {
      Directive dline = new Directive(source, srcLine, relAddr, label);
      dline = parseDirect(dline);
      return dline;
   }

   private Directive parseDirect(Directive dline) throws SyntaxError {
      String direct = tok.nextToken();
      dline.setOperator(direct);
      switch (direct) {
         case "dw":
            dline = parseDwDirective(dline);
            break;
         case "org":
            dline = parseOrgDirective(dline);
            break;
         case "end":
            dline = parseEndDirective(dline);
            break;
         case "extern":
            dline = parseExternDirective(dline);
            break;
         case "public":
            dline = parsePublicDirective(dline);
            break;
         default:
            throw new SyntaxError(String.format("unrecognized symbol %s at line %d.", direct, srcLine));
      }
      return dline;
   }

   private Directive parseDwDirective(Directive dline) throws SyntaxError {
      Directive direct = new DirectiveDw(dline);
      parseOperands(direct, -1);
      if (direct.getOperands().size() == 1) {
         if (direct.getOperands().get(0) instanceof OperandNumber) {
            int total = ((OperandNumber) direct.getOperands().get(0)).getTotal();
//            if (total > 65535 || total < -32767) {
//               throw new SyntaxError("Address or operand out of range: " + direct.getOperands().get(0));
//            }
         } else if (direct.getOperands().get(0) instanceof OperandDup) {
            int total = ((OperandDup) direct.getOperands().get(0)).getValue();
//            if (total > 65535 || total < -32767) {
//               throw new SyntaxError("Address or operand out of range: " + direct.getOperands().get(0));
//            }
         }
      }
      return direct;
   }

   private Directive parseOrgDirective(Directive dline) throws SyntaxError {
      Directive direct = new DirectiveOrg(dline);
      parseOperands(direct, 1);
      if (direct.getOperands().size() == 1) {
         if (direct.getOperands().get(0) instanceof OperandNumber) {
            int total = ((OperandNumber) direct.getOperands().get(0)).getTotal();
//            if (total > 65535 || total < -32767) {
//               throw new SyntaxError("Address or operand out of range: " + direct.getOperands().get(0));
//            }
         } else {
            throw new SyntaxError("Org directive requires a single numeric argument.");
         }
      }
      return direct;
   }

   private Directive parseEndDirective(Directive dline) throws SyntaxError {
      Directive direct = new DirectiveEnd(dline);
      parseOperands(direct, 1);
      if (direct.getOperands().size() == 1) {
         if (direct.getOperands().get(0) instanceof OperandNumber) {
            int total = ((OperandNumber) direct.getOperands().get(0)).getTotal();
//            if (total > 65535 || total < -32767) {
//               throw new SyntaxError("Address or operand out of range: " + direct.getOperands().get(0));
//            }
         }
      }
      return direct;
   }

   private Directive parseExternDirective(Directive dline) throws SyntaxError {
      Directive direct = new DirectiveExtern(dline);
      parseOperands(direct, -1);
      if (direct.getOperands().isEmpty()) {
         throw new SyntaxError("Extern directive without associated label.");
      }
      for (Operand operand : direct.getOperands()) {
         if (operand instanceof OperandLabel) {
            if (!symtab.contains(operand.getToken())) {
               symtab.add(operand.getToken(), 0);
            }
            symtab.setExtern(operand.getToken(), true);
            symtab.setMob(true);
         } else {
            throw new SyntaxError("Expecting label with 'extern' directive: " + operand.getToken());
         }
      }
      return direct;
   }

   private Directive parsePublicDirective(Directive dline) throws SyntaxError {
      Directive direct = new DirectivePublic(dline);
      parseOperands(direct, -1);
      if (direct.getOperands().isEmpty()) {
         throw new SyntaxError("Public directive without associated label.");
      }
      for (Operand operand : direct.getOperands()) {
         if (operand instanceof OperandLabel) {
            if (!symtab.contains(operand.getToken())) {
               throw new SyntaxError("Public directive references unrecognized label: " + operand.getToken());
            }
            symtab.setMob(true);
         } else {
            throw new SyntaxError("Expecting label with 'public' directive: " + operand.getToken());
         }
      }
      return direct;
   }

   private boolean isMneumonic(String str) {
      return Mneumonic.getInstance().contains(str);
   }

   private boolean hasLabel() {
      return source.contains(":");
   }

   private boolean isValidNumber(String str) {
      final String numberPattern = "[+-]?[0-9]+";
      return str.matches(numberPattern);
   }

   private boolean isValidLabel(String str) {
      final String labelPattern = "[@$_A-Za-z][@$_0-9A-Za-z]*";
      return str.matches(labelPattern);
   }

   private String filename;
   private final SymbolTable symtab;
   private ParseTokenizer tok;
   private String source;
   private int srcLine;
   private int relAddr;

}
