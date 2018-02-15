/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package assembler.parser;

/**
 *
 * @author Alan
 */
public class SyntaxError extends Exception {

    public SyntaxError(Throwable cause) {
        super(cause);
    }

    public SyntaxError(String message, Throwable cause) {
        super("syntax error: " + message, cause);
    }

    public SyntaxError(String message) {
        super("syntax error: " + message);
    }

    public SyntaxError() {
        super("sytnax error");
    }
    
}
