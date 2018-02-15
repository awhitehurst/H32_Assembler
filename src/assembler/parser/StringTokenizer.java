/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package assembler.parser;

/**
 * Returns the tokens in a string. See StringTokenizer
 * for more details.
 * @see java.util.StringTokenizer
 * @author Alan
 */
public class StringTokenizer {

    /**
     * Tokenize the given string, breaking the tokens on
     * whitespace characters. Do not return the delimiters.
     * See Character.isWhitespace for more details. 
     * @param str The String to tokenize
     * @see Character
     */
    public StringTokenizer(String str) {
        this(str, null);
    }

    /**
     * Tokenize the given string, breaking the tokens on
     * the given set of delimiters. Do not return the delimiters.
     * @param str The String to tokenize
     * @param delims The single-character delimiters which define token boundaries
     */
    public StringTokenizer(String str, String delims) {
        this(str, delims, false);
    }

    /**
     * Tokenize the given string, breaking the tokens on the given set of
     * delimiters. If retDelims is true, then return the delimiters as tokens.
     * @param str The String to tokenize
     * @param delims The single-character delimiters which define token boundaries
     * @param retDelims A flag to signal whether or not to return delimiters as tokens
     */
    public StringTokenizer(String str, String delims, boolean retDelims) {
        this(str, delims, retDelims, false);
    }

    /**
     * Tokenize the given string, breaking the tokens on the given set of
     * delimiters. If retDelims is true, then return the delimiters as tokens.
     * @param str The String to tokenize
     * @param delims The single-character delimiters which define token boundaries
     * @param retDelims A flag to signal whether or not to return delimiters as tokens
     * @param ignoreWhitespace A flag to signal whether or not to return whitespace as delimiters
     */
    public StringTokenizer(String str, String delims, boolean retDelims, boolean ignoreWhitespace) {
        this.str = str;
        this.del = delims;
        this.rDels = retDelims;
        this.ignoreWS = ignoreWhitespace;
        this.pos = 0;
        if (ignoreWhitespace) {
            this.str = this.str.trim();
        }
        if (!rDels) {
            skipDelims();
        }
    }

    public StringTokenizer(StringTokenizer st) {
        this(st.str, st.del, st.rDels, st.ignoreWS);
        this.pos = st.pos;
    }

    /**
     * Return true if and only if a succeeding call to nextToken 
     * will return a token.
     * @return true if there are more tokens, false otherwise
     */
    public boolean hasMoreTokens() {
        return pos < str.length();
    }

    /**
     * Return the next token in the string.
     * @return the next token (string) in the original string
     */
    public String nextToken() {
        String retString = null;
        // if there is nothing to return, return a null.
        // this would signal an exception in the real StringToken
        if (pos >= str.length()) {
            return null;
        }
        // if we're supposed to return delimiters, and there
        // is still characters left to return and there is
        // a delimiter at pos, then return it and advance
        // pos to the next character
        while (rDels && pos < str.length() && isDelim(pos)) {
            if (!ignoreWS || !isWhitespace(pos)) {
                retString = str.substring(pos, pos + 1);
                ++pos;
                return retString;
            } else {
                ++pos;
            }
        }
        // Special case: balanced strings that start with
        // either a \' or a \" and end with the same
        // character regardless of what is between
        int endPos;
        if (str.charAt(pos) == '\'') {
            endPos = pos + 1;
            while (endPos < str.length() && 
                    (str.charAt(endPos) != '\'' || str.charAt(endPos-1)=='\\')) {
                ++endPos;
            }
            if(endPos < str.length()){
                ++endPos;
            }
        } else if (str.charAt(pos) == '"') {
            endPos = pos + 1;
            while (endPos < str.length() && 
                    (str.charAt(endPos) != '"' || str.charAt(endPos-1)=='\\')) {
                ++endPos;
            }
            if(endPos < str.length()){
                ++endPos;
            }
        } else {
            // if we got this far, pos is a non-delimiter, so
            // let endPos be the character position one beyond
            // pos; then advance endPos until we reach the
            // end of the string or we encounter a delimiter
            endPos = pos + 1;
            while (endPos < str.length() && !isDelim(endPos)) {
                ++endPos;
            }
        }
        // the token to return will be the substring from
        // pos to endPos, so set pos to that delimiter;
        // remember that token in retString
        retString = str.substring(pos, endPos);
        pos = endPos;
        // finally, if we are not returning delimiters, let's
        // advance pos to the next non-delimiter in preparation
        // for the next call to hasMoreTokens
        if (!rDels) {
            skipDelims();
        } else if (ignoreWS) {
            skipWhitespace();
        }
        // now, return the token
        return retString;
    }

    private void skipDelims() {
        while (pos < str.length() && (isDelim(pos) || (!ignoreWS || isWhitespace(pos)))) {
            pos++;
        }
    }

    private void skipWhitespace() {
        while (pos < str.length() && isWhitespace(pos)) {
            pos++;
        }
    }

    private boolean isWhitespace(int i) {
        return Character.isWhitespace(str.charAt(i));
    }

    private boolean isDelim(int i) {
        if (del == null) {
            return Character.isWhitespace(str.charAt(i));
        } else {
            return del.indexOf(str.charAt(i)) >= 0;
        }
    }
    private String str;
    private String del;
    private boolean rDels;
    private boolean ignoreWS;
    private int pos;
}
