/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package assembler.ast;

import assembler.Mneumonic;
import assembler.H32OutputStream;
import assembler.SymbolTable;
import java.util.ArrayList;

/**
 *
 * @author Alan
 */
public abstract class ALine {

    public ALine(String source, int srcLine, int prgLine, String label) {
        this.srcLine = srcLine;
        this.relAddr = prgLine;
        this.source = source;
        this.label = label;
        this.macSize = 1;
        operands = new ArrayList<>();
        this.relative = false;
    }

    public String getLabel() {
        return label;
    }

    public int getNumOperands() {
        return operands.size();
    }

    public ArrayList<Operand> getOperands() {
        return operands;
    }

    public void addOperand(Operand operand) {
        operands.add(operand);
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public int getRelAddr() {
        return relAddr;
    }

    public String getSource() {
        return source;
    }

    public int getSrcLine() {
        return srcLine;
    }

    public int getMacSize() {
        return macSize;
    }

    public void setMacSize(int macSize) {
        this.macSize = macSize;
    }

    public int getNextRelAddr() {
        return relAddr + getMacSize();
    }

    public boolean hasOperands() {
        return operands.size() > 0;
    }

    public void setRelative(boolean relative) {
        this.relative = relative;
    }

    public boolean isRelative() {
        return relative;
    }

    public abstract void toHeader(H32OutputStream boos, SymbolTable symtab);

    //TODO: change to abstract once it is implemented in all subclasses
    public void toMac(H32OutputStream boos, SymbolTable symtab) {
        int machineWord = 0;
        if (Mneumonic.getInstance().contains(operator)) {
            machineWord = Mneumonic.getInstance().getOpcode(operator);
            if (Mneumonic.getInstance().getNumops(operator) > 0) {
                for (Operand operand : operands) {
                    machineWord += operand.toMac(symtab)[0];
                }
            }
            boos.write(machineWord);
        }
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        if (label != null) {
            buf.append(String.format("%-8s", label + ":"));
        } else {
            buf.append("        ");
        }
        if (operator != null) {
            buf.append(operator);
            for (Operand operand : operands) {
                buf.append(" ");
                buf.append(operand);
            }
        }
        return buf.toString();
    }

    protected int srcLine;
    protected int relAddr;
    protected String source;
    protected String label;
    protected String operator;
    protected ArrayList<Operand> operands;
    protected boolean relative;
    protected int macSize;
}
