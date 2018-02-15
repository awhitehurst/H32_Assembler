/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package assembler;

import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author Alan
 */
public class SymbolTable {

   private HashMap<String, STE> symtab;
   private boolean mob;

   public SymbolTable() {
      symtab = new HashMap<String, STE>();
      mob = false;
   }

   public void add(String symbol, int lineNum) {
      symtab.put(symbol, new STE(symbol, lineNum));
   }

   public boolean contains(String symbol) {
      return symtab.containsKey(symbol);
   }

   public int getLineNum(String symbol) {
      if (contains(symbol)) {
         return symtab.get(symbol).lineNum;
      } else {
         return -1;
      }
   }

   public boolean isExtern(String symbol) {
      if (contains(symbol)) {
         return symtab.get(symbol).extern;
      } else {
         return false;
      }
   }

   public void setExtern(String symbol, boolean flag) {
      if (contains(symbol)) {
         symtab.get(symbol).extern = flag;
      }
   }
   
   public boolean isMob(){
      return this.mob;
   }
   
   public void setMob(boolean flag){
      this.mob = flag;
   }

   @Override
   public String toString() {
      StringBuilder buf = new StringBuilder();
      buf.append("SymbolTable:\n");
      Set<String> keys = symtab.keySet();
      for (String key : keys) {
         buf.append("\t");
         buf.append(key);
         buf.append(", ");
         buf.append(getLineNum(key));
         buf.append("\n");
      }
      buf.append("-----------\n");
      return buf.toString();
   }

   class STE {

      public STE(String symbol, int lineNum) {
         this(symbol, lineNum, false);
      }

      public STE(String symbol, int lineNum, boolean extern) {
         this.symbol = symbol;
         this.lineNum = lineNum;
         this.extern = extern;
      }

      @Override
      public boolean equals(Object obj) {
         if (obj == null) {
            return false;
         }
         if (getClass() != obj.getClass()) {
            return false;
         }
         final STE other = (STE) obj;
         if ((this.symbol == null) ? (other.symbol != null) : !this.symbol.equals(other.symbol)) {
            return false;
         }
         return true;
      }

      @Override
      public int hashCode() {
         int hash = 7;
         hash = 31 * hash + (this.symbol != null ? this.symbol.hashCode() : 0);
         return hash;
      }

      String symbol;
      int lineNum;
      boolean extern;
   }

}
