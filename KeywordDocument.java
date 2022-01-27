package texteditor;

import java.util.ArrayList;
import java.util.List;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;

public class KeywordDocument extends DefaultStyledDocument{
    
    private Style _defaultStyle;
    private Style _kwStyle;

    public KeywordDocument(Style defaultStyle, Style kwStyle){
        _defaultStyle = defaultStyle;
        _kwStyle = kwStyle;
    }

    @Override
    public void insertString(int offset, String str, AttributeSet a) throws BadLocationException{
        super.insertString(offset, str, a);
        refreshDocument();
    }

    @Override
    public void remove(int offs, int len) throws BadLocationException{
        super.remove(offs, len);        
        refreshDocument();
    }

    private synchronized void refreshDocument() throws BadLocationException{
        String text = getText(0, getLength());
        final List<HiliteWord> list = processWords(text);

        setCharacterAttributes(0, text.length(), _defaultStyle, true);
        for (HiliteWord word : list){
            int p0 = word._position;            
            setCharacterAttributes(p0, word._word.length(), _kwStyle, true);
        }
    }

    private static List<HiliteWord> processWords(String content){ 
        
        content += " ";
        List<HiliteWord> hiliteWords = new ArrayList<HiliteWord>();
        int lastWhitespacePosition = 0;
        String word = "";
        char[] data = content.toCharArray();

        for (int index = 0; index < data.length; index++){
            char ch = data[index];
            System.out.println(ch);
            System.out.println(word);
            
            if (!(Character.isLetter(ch) || Character.isDigit(ch) || ch == '_'||ch=='<'||ch=='>'||ch=='/')) {
                lastWhitespacePosition = index;
                if (word.length() > 0){                     
                    Language lang = JPad.getLang();
                    boolean cpptrue = lang.cpptrue;
                    boolean htmltrue = lang.htmltrue;
                    if (cpptrue) {
                        if (iscppReservedWord(word)){
                            hiliteWords.add(new HiliteWord(word, (lastWhitespacePosition - word.length())));
                        }
                    } else if (htmltrue){
                        if (ishtmlReservedWord(word)){
                            hiliteWords.add(new HiliteWord(word, (lastWhitespacePosition - word.length())));
                        }
                    }
                    word = "";
                }
            } 
            else {
                word += ch;
            }
        }
        return hiliteWords;
    }

    private static boolean iscppReservedWord(String word) {

        return (word.toUpperCase().trim().equals("ASM")
                || word.toUpperCase().trim().equals("AUTO")
                || word.toUpperCase().trim().equals("PRINTF")
                || word.toUpperCase().trim().equals("SCANF")
                || word.toUpperCase().trim().equals("BOOL")
                || word.toUpperCase().trim().equals("BREAK")
                || word.toUpperCase().trim().equals("CASE")
                || word.toUpperCase().trim().equals("CATCH")
                || word.toUpperCase().trim().equals("CHAR")
                || word.toUpperCase().trim().equals("CLASS")
                || word.toUpperCase().trim().equals("CONST")
                || word.toUpperCase().trim().equals("CONST_CAST")
                || word.toUpperCase().trim().equals("CONTINUE")
                || word.toUpperCase().trim().equals("DEFAULT")
                || word.toUpperCase().trim().equals("DELETE")
                || word.toUpperCase().trim().equals("DO")
                || word.toUpperCase().trim().equals("DOUBLE")
                || word.toUpperCase().trim().equals("DYNAMIC_CAST")
                || word.toUpperCase().trim().equals("ELSE")
                || word.toUpperCase().trim().equals("ENUM")
                || word.toUpperCase().trim().equals("EXPLICIT")
                || word.toUpperCase().trim().equals("EXPORT")
                || word.toUpperCase().trim().equals("EXTERN")
                || word.toUpperCase().trim().equals("FALSE")
                || word.toUpperCase().trim().equals("FLOAT")
                || word.toUpperCase().trim().equals("FOR")
                || word.toUpperCase().trim().equals("FRIEND")
                || word.toUpperCase().trim().equals("GOTO")
                || word.toUpperCase().trim().equals("IF")
                || word.toUpperCase().trim().equals("INLINE")
                || word.toUpperCase().trim().equals("INT")
                || word.toUpperCase().trim().equals("LONG")
                || word.toUpperCase().trim().equals("MUTABLE")
                || word.toUpperCase().trim().equals("MAIN")
                || word.toUpperCase().trim().equals("NAMESPACE")
                || word.toUpperCase().trim().equals("NEW")
                || word.toUpperCase().trim().equals("OPERATOR")
                || word.toUpperCase().trim().equals("PRIVATE")
                || word.toUpperCase().trim().equals("PROTECTED")
                || word.toUpperCase().trim().equals("PUBLIC")
                || word.toUpperCase().trim().equals("RETURN")
                || word.toUpperCase().trim().equals("SHORT")
                || word.toUpperCase().trim().equals("SIGNED")
                || word.toUpperCase().trim().equals("SIGEOF")
                || word.toUpperCase().trim().equals("STATIC")
                || word.toUpperCase().trim().equals("STRUCT")
                || word.toUpperCase().trim().equals("SWITCH")
                || word.toUpperCase().trim().equals("TEMPLATE")
                || word.toUpperCase().trim().equals("THIS")
                || word.toUpperCase().trim().equals("THROW")
                || word.toUpperCase().trim().equals("TRUE")
                || word.toUpperCase().trim().equals("TRY")
                || word.toUpperCase().trim().equals("TYPEDEF")
                || word.toUpperCase().trim().equals("UNION")
                || word.toUpperCase().trim().equals("UNSIGNED")
                || word.toUpperCase().trim().equals("USING")
                || word.toUpperCase().trim().equals("VIRTUAL")
                || word.toUpperCase().trim().equals("VOID")
                || word.toUpperCase().trim().equals("WHILE"));
    }

    private static boolean ishtmlReservedWord(String word) {
        return (word.toUpperCase().trim().equals("<HTML>")
                ||word.toUpperCase().trim().equals("</HTML>")
                || word.toUpperCase().trim().equals("<HEAD>")
                || word.toUpperCase().trim().equals("<B>")
                || word.toUpperCase().trim().equals("<BIG")
                || word.toUpperCase().trim().equals("<BODY>")
                || word.toUpperCase().trim().equals("<BR>")
                || word.toUpperCase().trim().equals("<CENTER>")
                || word.toUpperCase().trim().equals("<DD>")
                || word.toUpperCase().trim().equals("<DL>")
                || word.toUpperCase().trim().equals("<DT>")
                || word.toUpperCase().trim().equals("<EM>")
                || word.toUpperCase().trim().equals("<EMBED>")
                || word.toUpperCase().trim().equals("<FONT>")
                || word.toUpperCase().trim().equals("<H1>")
                || word.toUpperCase().trim().equals("<H2>")
                || word.toUpperCase().trim().equals("<H3>")
                || word.toUpperCase().trim().equals("<H4>")
                || word.toUpperCase().trim().equals("<H5>")
                || word.toUpperCase().trim().equals("<H6>")
                || word.toUpperCase().trim().equals("<HR>")
                || word.toUpperCase().trim().equals("<I>")
                || word.toUpperCase().trim().equals("<IMG>")
                || word.toUpperCase().trim().equals("<INPUT>")
                || word.toUpperCase().trim().equals("<LI>")
                || word.toUpperCase().trim().equals("<LINK>")
                || word.toUpperCase().trim().equals("<MARQUEE>")
                || word.toUpperCase().trim().equals("<MENUE>")
                || word.toUpperCase().trim().equals("<META>")
                || word.toUpperCase().trim().equals("<OL>")
                || word.toUpperCase().trim().equals("<OPTION>")
                || word.toUpperCase().trim().equals("<P>")
                || word.toUpperCase().trim().equals("<SMALL>")
                || word.toUpperCase().trim().equals("<STRIKE>")
                || word.toUpperCase().trim().equals("<STRONG>")
                || word.toUpperCase().trim().equals("<TABLE>")
                || word.toUpperCase().trim().equals("<TD>")
                || word.toUpperCase().trim().equals("<TH>")
                || word.toUpperCase().trim().equals("<TITLE>")
                || word.toUpperCase().trim().equals("<TR>")
                || word.toUpperCase().trim().equals("<TT>")
                || word.toUpperCase().trim().equals("<U>")
                || word.toUpperCase().trim().equals("<UL>")
                || word.toUpperCase().trim().equals("</HEAD>")
                || word.toUpperCase().trim().equals("</B>")
                || word.toUpperCase().trim().equals("</BIG")
                || word.toUpperCase().trim().equals("</BODY>")
                || word.toUpperCase().trim().equals("</BR>")
                || word.toUpperCase().trim().equals("</CENTER>")
                || word.toUpperCase().trim().equals("</DD>")
                || word.toUpperCase().trim().equals("</DL>")
                || word.toUpperCase().trim().equals("</DT>")
                || word.toUpperCase().trim().equals("</EM>")
                || word.toUpperCase().trim().equals("</EMBED>")
                || word.toUpperCase().trim().equals("</FONT>")
                || word.toUpperCase().trim().equals("</H1>")
                || word.toUpperCase().trim().equals("</H2>")
                || word.toUpperCase().trim().equals("</H3>")
                || word.toUpperCase().trim().equals("</H4>")
                || word.toUpperCase().trim().equals("</H5>")
                || word.toUpperCase().trim().equals("</H6>")
                || word.toUpperCase().trim().equals("</HR>")
                || word.toUpperCase().trim().equals("</I>")
                || word.toUpperCase().trim().equals("</IMG>")
                || word.toUpperCase().trim().equals("</INPUT>")
                || word.toUpperCase().trim().equals("</LI>")
                || word.toUpperCase().trim().equals("</LINK>")
                || word.toUpperCase().trim().equals("</MARQUEE>")
                || word.toUpperCase().trim().equals("</MENUE>")
                || word.toUpperCase().trim().equals("</META>")
                || word.toUpperCase().trim().equals("</OL>")
                || word.toUpperCase().trim().equals("</OPTION>")
                || word.toUpperCase().trim().equals("</P>")
                || word.toUpperCase().trim().equals("</SMALL>")
                || word.toUpperCase().trim().equals("</STRIKE>")
                || word.toUpperCase().trim().equals("</STRONG>")
                || word.toUpperCase().trim().equals("</TABLE>")
                || word.toUpperCase().trim().equals("</TD>")
                || word.toUpperCase().trim().equals("</TH>")
                || word.toUpperCase().trim().equals("</TITLE>")
                || word.toUpperCase().trim().equals("</TR>")
                || word.toUpperCase().trim().equals("</TT>")
                || word.toUpperCase().trim().equals("</U>")
                || word.toUpperCase().trim().equals("</UL>"));
    }
}


