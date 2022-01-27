package texteditor;

import java.io.*;
import java.util.Date;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class JPad extends JFrame implements ActionListener, ManueCreate{

    JFrame fram;
    JTextPane textarea;
    JLabel statusBar;

    private String fileName = "Untitled";
    private boolean saved = true;
    String applicationName ="JPad";

    String searchString, replaceString;
    int lastSearchIndex;

    Operation fileHandler;
    FontClass fontDialog = null;    
    JColorChooser bcolorChooser = null;
    JColorChooser fcolorChooser = null;
    JDialog backgroundDialog = null;
    JDialog foregroundDialog = null;
    JMenuItem cutItem, copyItem, deleteItem, findItem, findNextItem, replaceItem, gotoItem, selectAllItem;
    public static Language lang = new Language();

    JPad(){
        StyleContext styleContext = new StyleContext();
        Style defaultStyle = styleContext.getStyle(StyleContext.DEFAULT_STYLE);
        Style kwStyle = styleContext.addStyle("ConstantWidth", null);
        StyleConstants.setForeground(kwStyle, Color.BLUE);
        StyleConstants.setBold(kwStyle, true);

        fram = new JFrame(fileName + " - " + applicationName);
        textarea = new JTextPane(new KeywordDocument(defaultStyle, kwStyle));
        textarea.setFont(new Font("Courier New", Font.PLAIN, 16));

        fram.setSize(500, 500);

       
        fram.add(new JScrollPane(textarea), BorderLayout.CENTER);
        
        fram.add(new JLabel("  "), BorderLayout.EAST);
        fram.add(new JLabel("  "), BorderLayout.WEST);
        createMenuBar(fram);
        fram.setLocation(100, 50);
        fram.setVisible(true);
        fram.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        fileHandler = new Operation(this);

        DocumentListener myListener = new DocumentListener(){
            public void changedUpdate(DocumentEvent e){
                fileHandler.saved = false;
            }

            public void removeUpdate(DocumentEvent e){
                fileHandler.saved = false;
            }

            public void insertUpdate(DocumentEvent e){
                fileHandler.saved = false;
            }
        };
        textarea.getDocument().addDocumentListener(myListener);

        WindowListener frameClose = new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                if (fileHandler.confirmSave()) {
                    System.exit(0);
                }
            }
        };
        fram.addWindowListener(frameClose);
    }

    public void actionPerformed(ActionEvent ev){
        String cmdText = ev.getActionCommand();
        if (cmdText.equals(fileNew)) {
            fileHandler.newFile();
        } 
        else if (cmdText.equals(fileOpen)){
            fileHandler.openFile2();
        } 
        else if (cmdText.equals(fileSave)){
            fileHandler.saveThisFile();
        } 
        else if (cmdText.equals(fileSaveAs)){
            fileHandler.saveAsFile();
        } 
        else if (cmdText.equals(fileExit)){
            if (fileHandler.confirmSave()){
                System.exit(0);
            }
        } 
        else if (cmdText.equals(filePrint)){
            JOptionPane.showMessageDialog(JPad.this.fram,
                    "Get your printer repaired first! It seems you don't have one!",
                    "Bad Printer",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } 
        else if (cmdText.equals(editCut)){
            textarea.cut();
        } 
        else if (cmdText.equals(editCopy)){
            textarea.copy();
        } 
        else if (cmdText.equals(editPaste)){
            textarea.paste();
        } 
        else if (cmdText.equals(editDelete)){
            textarea.replaceSelection("");
        } 
        else if (cmdText.equals(editSelectAll)){
            textarea.selectAll();
        } 
        else if (cmdText.equals(formatWordWrap)){

        } 
        else if (cmdText.equals(formatFont)) {
            if (fontDialog == null) {
                fontDialog = new FontClass(textarea.getFont());
            }

            if (fontDialog.showDialog(JPad.this.fram, "Choose a font")){
                JPad.this.textarea.setFont(fontDialog.createFont());
            }
        } 
        else if (cmdText.equals(formatForeground)){
            showForegroundColorDialog();
        } 
        else if (cmdText.equals(formatBackground)){
            showBackgroundColorDialog();
        } 
        else if (cmdText.equals(viewStatusBar)){
            JCheckBoxMenuItem temp = (JCheckBoxMenuItem) ev.getSource();
            statusBar.setVisible(temp.isSelected());
        } 
        else if (cmdText.equals(helpAboutNotepad)){
            JOptionPane.showMessageDialog(JPad.this.fram, aboutText, "Dedicated to you!", JOptionPane.INFORMATION_MESSAGE);
        } 
        else if (cmdText.equals(cpp)){
            lang.cpptrue = true;
            lang.htmltrue = false;
        } 
        else if (cmdText.equals(html)){
            lang.htmltrue = true;
            lang.cpptrue = false;
        } 
        else{
            
        }
    }

    //action Performed  
    ////////////////////////////////////

    void showBackgroundColorDialog(){
        if (bcolorChooser == null){
            bcolorChooser = new JColorChooser();
        }
        if (backgroundDialog == null){
            backgroundDialog = JColorChooser.createDialog(JPad.this.fram,
                    formatBackground,
                    false,
                    bcolorChooser,
                    new ActionListener(){
                public void actionPerformed(ActionEvent evvv){
                    JPad.this.textarea.setBackground(bcolorChooser.getColor());
                }
            },
                    null);
        }

        backgroundDialog.setVisible(true);
    }


    void showForegroundColorDialog(){
        if (fcolorChooser == null){
            fcolorChooser = new JColorChooser();
        }
        if (foregroundDialog == null){
            foregroundDialog = JColorChooser.createDialog(JPad.this.fram,
                formatForeground,
                false,
                fcolorChooser,
                new ActionListener(){
                    public void actionPerformed(ActionEvent evvv){
                        JPad.this.textarea.setForeground(fcolorChooser.getColor());
                    }
                },
            null);
        }
        foregroundDialog.setVisible(true);
    }

    JMenuItem createMenuItem(String s, int key, JMenu toMenu, ActionListener al) {
        JMenuItem temp = new JMenuItem(s, key);
        temp.addActionListener(al);
        toMenu.add(temp);

        return temp;
    }

    JMenuItem createMenuItem(String s, int key, JMenu toMenu, int aclKey, ActionListener al) {
        JMenuItem temp = new JMenuItem(s, key);
        temp.addActionListener(al);
        temp.setAccelerator(KeyStroke.getKeyStroke(aclKey, ActionEvent.CTRL_MASK));
        toMenu.add(temp);

        return temp;
    }

    JCheckBoxMenuItem createCheckBoxMenuItem(String s, int key, JMenu toMenu, ActionListener al) {
        JCheckBoxMenuItem temp = new JCheckBoxMenuItem(s);
        temp.setMnemonic(key);
        temp.addActionListener(al);
        temp.setSelected(false);
        toMenu.add(temp);

        return temp;
    }

    JMenu createMenu(String s, int key, JMenuBar toMenuBar){
        JMenu temp = new JMenu(s);
        temp.setMnemonic(key);
        toMenuBar.add(temp);
        return temp;
    }

    /**
     * ******************************
     */
    void createMenuBar(JFrame f) {
        JMenuBar mb = new JMenuBar();
        JMenuItem temp;

        JMenu fileMenu = createMenu(fileText, KeyEvent.VK_F, mb);
        JMenu editMenu = createMenu(editText, KeyEvent.VK_E, mb);
        JMenu formatMenu = createMenu(formatText, KeyEvent.VK_O, mb);        
        JMenu helpMenu = createMenu(helpText, KeyEvent.VK_H, mb);
        JMenu languageMenu = createMenu(language, KeyEvent.VK_L, mb);

        createMenuItem(fileNew, KeyEvent.VK_N, fileMenu, KeyEvent.VK_N, this);
        createMenuItem(fileOpen, KeyEvent.VK_O, fileMenu, KeyEvent.VK_O, this);
        createMenuItem(fileSave, KeyEvent.VK_S, fileMenu, KeyEvent.VK_S, this);
        createMenuItem(fileSaveAs, KeyEvent.VK_A, fileMenu, this);
        fileMenu.addSeparator();
        temp = createMenuItem(filePageSetup, KeyEvent.VK_U, fileMenu, this);
        temp.setEnabled(false);
        createMenuItem(filePrint, KeyEvent.VK_P, fileMenu, KeyEvent.VK_P, this);
        fileMenu.addSeparator();
        createMenuItem(fileExit, KeyEvent.VK_X, fileMenu, this);
        
        cutItem = createMenuItem(editCut, KeyEvent.VK_X, editMenu, KeyEvent.VK_X, this);
        copyItem = createMenuItem(editCopy, KeyEvent.VK_C, editMenu, KeyEvent.VK_C, this);
        createMenuItem(editPaste, KeyEvent.VK_P, editMenu, KeyEvent.VK_V, this);
        deleteItem = createMenuItem(editDelete, KeyEvent.VK_L, editMenu, this);
        deleteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        editMenu.addSeparator();
        selectAllItem = createMenuItem(editSelectAll, KeyEvent.VK_A, editMenu, KeyEvent.VK_A, this);

        createCheckBoxMenuItem(formatWordWrap, KeyEvent.VK_W, formatMenu, this);

        createMenuItem(formatFont, KeyEvent.VK_F, formatMenu, this);
        formatMenu.addSeparator();
        createMenuItem(formatForeground, KeyEvent.VK_T, formatMenu, this);
        createMenuItem(formatBackground, KeyEvent.VK_P, formatMenu, this);
        

        temp = createMenuItem(helpHelpTopic, KeyEvent.VK_H, helpMenu, this);
        temp.setEnabled(false);
        helpMenu.addSeparator();
        createMenuItem(helpAboutNotepad, KeyEvent.VK_A, helpMenu, this);
        createMenuItem(cpp, KeyEvent.VK_A, languageMenu, this);
        languageMenu.addSeparator();
        createMenuItem(html, KeyEvent.VK_A, languageMenu, this);

        MenuListener editMenuListener = new MenuListener() {
            public void menuSelected(MenuEvent evvvv) {
                if (JPad.this.textarea.getText().length() == 0) {
                    selectAllItem.setEnabled(false);
                    
                } 
                else{
                    selectAllItem.setEnabled(true);
                    
                }
                if (JPad.this.textarea.getSelectionStart() == textarea.getSelectionEnd()){
                    cutItem.setEnabled(false);
                    copyItem.setEnabled(false);
                    deleteItem.setEnabled(false);
                } 
                else {
                    cutItem.setEnabled(true);
                    copyItem.setEnabled(true);
                    deleteItem.setEnabled(true);
                }
            }

            public void menuDeselected(MenuEvent evvvv)
            {
            
            }

            public void menuCanceled(MenuEvent evvvv) 
            {
            
            }
        };
        editMenu.addMenuListener(editMenuListener);
        f.setJMenuBar(mb);
    }

    public static void main(String[] args) {
        new JPad();
    }

    public static Language getLang() {
        return lang;
    }
}
