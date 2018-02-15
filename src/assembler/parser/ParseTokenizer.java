/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package assembler.parser;

/**
 *
 * @author Alan
 */
public class ParseTokenizer {
    
    public ParseTokenizer(String str){
        this(str,null);
    }
    
    public ParseTokenizer(String str, String delims){
        this(str,delims,false);
    }
    
    public ParseTokenizer(String str, String delims, boolean rdels){
        this(str, delims, rdels, false);
    }
    
    public ParseTokenizer(String str, String delims, boolean rdels, boolean igwhite){
        // strip off end of line comments
        if(str.contains(";")){
            str = str.substring(0,str.indexOf(";"));
        }
        st = new StringTokenizer(str, delims, rdels, igwhite);
        peek = null;
    }
    
    public ParseTokenizer(ParseTokenizer pt){
        this.st = new StringTokenizer(pt.st);
        this.peek = pt.peek;
    }
    
    public boolean hasMoreTokens(){
        return peek!=null || st.hasMoreTokens();
    }
    
    public String nextToken(){
        if(peek!=null){
            String retValue = peek;
            peek = null;
            return retValue;
        }
        return st.nextToken();
    }
    
    public String peekToken(){
        if(peek==null && st.hasMoreTokens()){
            peek = st.nextToken();
        }
        return peek;
    }
    
    public String concatToken(){
        if(peek==null) return peekToken();
        if(st.hasMoreTokens()){
            peek += st.nextToken();
        }
        String retVal = peek;
        peek = null;
        return retVal;
    }
    
    private StringTokenizer st;
    private String peek;
    
}
