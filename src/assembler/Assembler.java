package assembler;

import assembler.parser.Parser;
import assembler.parser.SyntaxError;
import assembler.ast.ALine;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This is an object-oriented implementation of the assembler for the H1
 * standard assembly language. This assembler uses a recursive-descent parser to
 * create an in-memory abstract syntax tree representation of the source
 * program, and then implements operations in that abstract tree to produce a
 * machine-code representation of the program.
 *
 * @author Alan
 */
public class Assembler {

    private final String filename;
    private ArrayList<ALine> prog;
    private final SymbolTable symtab;

    public Assembler(String filename) {
        this.filename = filename.trim();
        symtab = new SymbolTable();
    }

    private void assemble() {
        BufferedReader reader = null;
        H32OutputStream boos = null;
        try {
            reader = new BufferedReader(new FileReader(filename + ".as"));

            parseSource(reader);
            System.out.println("Opening " + outputFilename() + " for writing.");
            boos = new H32OutputStream(new FileOutputStream(outputFilename()));
            passOne(boos);
            boos.write((int) 'T');
            passTwo(boos);
        } catch (FileNotFoundException ex) {
            System.err.println(ex.getMessage());
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (boos != null) {
                    boos.flush();
                    boos.close();
                }
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }

    private void parseSource(BufferedReader reader) {
        try {
            int lineCount = 0;
            int relAddr = 0;
            prog = new ArrayList<ALine>();
            Parser parser = new Parser(symtab);
            while (true) {
                String inputLine = reader.readLine();
                if (inputLine != null && inputLine.contains(";")) {
                    inputLine = inputLine.substring(0, inputLine.indexOf(";"));
                }
                if (inputLine != null) {
                    ++lineCount;
                    if (inputLine.trim().length() > 0) {
                        try {
                            ALine aLine = parser.parse(inputLine, lineCount, relAddr);
                            if (aLine != null) {
                                relAddr = aLine.getNextRelAddr();
                                if (relAddr > 4095) {
                                    errorExit(lineCount, filename, inputLine, "Program too big.");
                                }
                                prog.add(aLine);
                            }
                        } catch (SyntaxError ex) {
                            this.errorExit(lineCount, filename, inputLine, ex.getMessage());
                        }
                    }
                } else {
                    break;
                }
            }
            dumpProgram();
            System.out.println(symtab);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            System.exit(1);
        }
    }

    private void passOne(H32OutputStream boos) {
        System.out.println("Starting pass one.");
        for (ALine aline : prog) {
            aline.toHeader(boos, symtab);
        }
    }

    private void passTwo(H32OutputStream boos) {
        System.out.println("Starting pass two.");
        for (ALine aline : prog) {
            aline.toMac(boos, symtab);
        }
    }

    private void dumpProgram() {
        System.out.println("Dumping prog:");
        for (ALine line : prog) {
            System.out.println(line);
        }
    }

    private String outputFilename() {
        String ext = symtab.isMob() ? ".mob" : ".mac";
        if (filename.endsWith(".as")) {
            return filename.substring(0, filename.length() - 4) + ext;
        } else {
            return filename + ext;
        }
    }

    private void errorExit(int lineNum, String filename, String source, String msg) {
        StringBuilder buf = new StringBuilder();
        buf.append(String.format("ERROR on line %d of %s\n", lineNum, filename));
        buf.append(source);
        buf.append("\n");
        buf.append(msg);
        buf.append("\n");
        System.err.print(buf.toString());
        System.exit(1);
    }

    /**
     * Main driver for the assembler.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String fname;
        if (args.length == 0) {
            System.out.println("Enter input file name, or hit ENTER to quit.");
            fname = new Scanner(System.in).nextLine();
            if (fname.isEmpty()) {
                System.exit(0);
            }
        } else {
            fname = args[0];
        }
        if (fname.contains(".as")) {
            fname = fname.substring(0, fname.indexOf("."));
        }
        Assembler mas = new Assembler(fname);
        mas.assemble();
    }

}
