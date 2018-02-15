/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package assembler.ast;

import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author Alan
 */
public class Mneumonic {

   private static final Mneumonic opcodeTable;

   static {
      opcodeTable = new Mneumonic();
      opcodeTable.add("ld", 0x00000000, 1);
      opcodeTable.add("st", 0x01000000, 1);
      opcodeTable.add("add", 0x02000000, 1);
      opcodeTable.add("sub", 0x03000000, 1);
      opcodeTable.add("ldr", 0x04000000, 1);
      opcodeTable.add("str", 0x05000000, 1);
      opcodeTable.add("addr", 0x06000000, 1);
      opcodeTable.add("subr", 0x07000000, 1);
      opcodeTable.add("ldc", 0x08000000, 1);
      opcodeTable.add("ja", 0x09000000, 1);
      opcodeTable.add("jzop", 0x0A000000, 1);
      opcodeTable.add("jn", 0x0B000000, 1);
      opcodeTable.add("jz", 0x0C000000, 1);
      opcodeTable.add("jnz", 0x0D000000, 1);
      opcodeTable.add("call", 0x0E000000, 1);
      opcodeTable.add("ret", 0x0F000000, 0);
      opcodeTable.add("ldi", 0x10000000, 0);
      opcodeTable.add("sti", 0x11000000, 0);
      opcodeTable.add("push", 0x12000000, 0);
      opcodeTable.add("pop", 0x13000000, 0);
      opcodeTable.add("aloc", 0x14000000, 1);
      opcodeTable.add("dloc", 0x15000000, 1);
      opcodeTable.add("swap", 0x16000000, 0);
      opcodeTable.add("addc", 0x17000000, 1);
      opcodeTable.add("subc", 0x18000000, 1);
      opcodeTable.add("esba", 0x19000000, 0);
      opcodeTable.add("reba", 0x1a000000, 0);
      opcodeTable.add("cora", 0x1b000000, 0);
      opcodeTable.add("scmp", 0x1c000000, 0);
      opcodeTable.add("ucmp", 0x1d000000, 0);
      opcodeTable.add("shll", 0x1e000000, 1);
      opcodeTable.add("shrl", 0x1f000000, 1);
      opcodeTable.add("shra", 0x20000000, 1);
      opcodeTable.add("muls", 0x21000000, 1);
      opcodeTable.add("mult", 0x22000000, 1);
      opcodeTable.add("div", 0x23000000, 1);
      opcodeTable.add("rem", 0x24000000, 1);
      opcodeTable.add("addy", 0x25000000, 1);
      opcodeTable.add("or", 0x26000000, 1);
      opcodeTable.add("xor", 0x27000000, 1);
      opcodeTable.add("and", 0x28000000, 1);
      opcodeTable.add("flip", 0x29000000, 0);
      opcodeTable.add("cali", 0x2a000000, 0);
      opcodeTable.add("sect", 0x2b000000, 1);
      opcodeTable.add("dect", 0x2c000000, 0);
      opcodeTable.add("sodd", 0x2d000000, 0);
      opcodeTable.add("bpbp", 0x2e000000, 0);
      opcodeTable.add("pobp", 0x2f000000, 0);
      opcodeTable.add("pubp", 0x30000000, 0);
      opcodeTable.add("bcpy", 0x31000000, 0);
      opcodeTable.add("sysc", 0x32000000, 1);
      opcodeTable.add("exit", 0x33000000, 1);
      opcodeTable.add("ittb", 0x34000000, 1);
      opcodeTable.add("iitb", 0x35000000, 1);
      opcodeTable.add("enbi", 0x36000000, 0);
      opcodeTable.add("disi", 0x37000000, 1);
      opcodeTable.add("uout", 0xF5000000, 0);
      opcodeTable.add("sin", 0xF6000000, 0);
      opcodeTable.add("sout", 0xF7000000, 0);
      opcodeTable.add("hin", 0xF8000000, 0);
      opcodeTable.add("hout", 0xF9000000, 0);
      opcodeTable.add("ain", 0xFA000000, 0);
      opcodeTable.add("aout", 0xFB000000, 0);
      opcodeTable.add("din", 0xFC000000, 0);
      opcodeTable.add("dout", 0xFD000000, 0);
      opcodeTable.add("bkpt", 0xFE000000, 0);
      opcodeTable.add("halt", 0xFF000000, 0);
   }

   private Mneumonic() {
      optab = new HashMap<String, OTE>();
   }

   private void add(String mnemonic, int opcode, int numops) {
      optab.put(mnemonic, new OTE(opcode, numops));
   }

   public static Mneumonic getInstance() {
      return opcodeTable;
   }

   public boolean contains(String mnemonic) {
      return optab.containsKey(mnemonic);
   }

   public int getOpcode(String mnemonic) {
      return optab.get(mnemonic).opcode;
   }

   public int getNumops(String mnemonic) {
      return optab.get(mnemonic).numops;
   }


   public Set<String> getKeySet() {
      return optab.keySet();
   }
   //
   private HashMap<String, OTE> optab;

   class OTE {

      public OTE(int opcode, int numops) {
         this.opcode = opcode;
         this.numops = numops;
      }

      int opcode;
      int numops;
   }
}
