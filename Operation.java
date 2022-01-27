package texteditor;
import java.awt.event.*;
import java.io.File;
import java.io.*;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

class Operation{
    JPad npd;

    boolean saved;
    boolean newFileFlag;
    String fileName;
    String applicationTitle = "Jpad";

    File fileRef;
    JFileChooser chooser;

    boolean isSave(){
        return saved;
    }

    void setSave(boolean saved){
        this.saved = saved;
    }

    String getFileName(){
        return new String(fileName);
    }

    void setFileName(String fileName){
        this.fileName = new String(fileName);
    }

    Operation(JPad npd){
        this.npd = npd;

        saved = true;
        newFileFlag = true;
        fileName = new String("Untitled");
        fileRef = new File(fileName);
        this.npd.fram.setTitle(fileName + " - " + applicationTitle);

        chooser = new JFileChooser();
        chooser.addChoosableFileFilter(new FileFilterExtends(".cpp", "C++ Source Files(*.cpp)"));
        chooser.addChoosableFileFilter(new FileFilterExtends(".html", "Html Source Files(*.html)"));
        chooser.addChoosableFileFilter(new FileFilterExtends(".txt", "Text Files(*.txt)"));
        chooser.setCurrentDirectory(new File("E:\\New folder"));

    }
    
    boolean fileSave(File temp){
        FileWriter fout = null;
        try {
            fout = new FileWriter(temp);
            fout.write(npd.textarea.getText());
        } catch (IOException ioe) {
            updateStatus(temp, false);
            return false;
        } finally{
            try{
                fout.close();
            }catch (IOException excp){
            }
        }
        updateStatus(temp, true);
        return true;
    }

    boolean saveThisFile(){

        if (!newFileFlag){
            return fileSave(fileRef);
        }

        return saveAsFile();
    }

////////////////////////////////////

    boolean saveAsFile(){
        File temp = null;
        chooser.setDialogTitle("Save As...");
        chooser.setApproveButtonText("Save Now");
        chooser.setApproveButtonMnemonic(KeyEvent.VK_S);
        chooser.setApproveButtonToolTipText("Click me to save!");

        do {
            if (chooser.showSaveDialog(this.npd.fram) != JFileChooser.APPROVE_OPTION){
                return false;
            }
            temp = chooser.getSelectedFile();
            if (!temp.exists()){
                break;
            }
            if (JOptionPane.showConfirmDialog(this.npd.fram, "<html>" + temp.getPath() + " already exists.<br>Do you want to replace it?<html>",
                    "Save As", JOptionPane.YES_NO_OPTION
            ) == JOptionPane.YES_OPTION){
                break;
            }
        } while (true);
                  
        return fileSave(temp);
    }

    ////////////////////////
    boolean openFile1(File temp){
        FileInputStream fin = null;
        BufferedReader br = null;

        try {
            fin = new FileInputStream(temp);
            br = new BufferedReader(new InputStreamReader(fin));
            String str = " ";
            while (str != null) {
                str = br.readLine();
                if (str == null) {
                    break;
                }
               
            }

        } catch (IOException ioe) {
            updateStatus(temp, false);
            return false;
        } finally {
            try {
                br.close();
                fin.close();
            } catch (IOException excp) {
            }
        }
        updateStatus(temp, true);
        this.npd.textarea.setCaretPosition(0);
        return true;
    }
///////////////////////

    void openFile2() {
        if (!confirmSave()) {
            return;
        }
        chooser.setDialogTitle("Open File...");
        chooser.setApproveButtonText("Open this");
        chooser.setApproveButtonMnemonic(KeyEvent.VK_O);
        chooser.setApproveButtonToolTipText("Click me to open the selected file.!");

        File temp = null;
        do {
            if (chooser.showOpenDialog(this.npd.fram) != JFileChooser.APPROVE_OPTION) {
                return;
            }
            temp = chooser.getSelectedFile();

            if (temp.exists()) {
                break;
            }

            JOptionPane.showMessageDialog(this.npd.fram,
                    "<html>" + temp.getName() + "<br>file not found.<br>"
                    + "Please verify the correct file name was given.<html>",
                    "Open", JOptionPane.INFORMATION_MESSAGE);

        } while (true);

        this.npd.textarea.setText("");

        if (!openFile1(temp)) {
            fileName = "Untitled";
            saved = true;
            this.npd.fram.setTitle(fileName + " - " + applicationTitle);
        }
        if (!temp.canWrite()) {
            newFileFlag = true;
        }

    }


    void updateStatus(File temp, boolean saved){
        if (saved){
            this.saved = true;
            fileName = new String(temp.getName());
            if (!temp.canWrite()){
                fileName += "(Read only)";
                newFileFlag = true;
            }
            fileRef = temp;
            npd.fram.setTitle(fileName + " - " + applicationTitle);
            npd.statusBar.setText("File : " + temp.getPath() + " saved/opened successfully.");
            newFileFlag = false;
        } else {
            npd.statusBar.setText("Failed to save/open : " + temp.getPath());
        }
    }
///////////////////////

    boolean confirmSave() {
        String strMsg = "<html>The text in the " + fileName + " file has been changed.<br>"
                + "Do you want to save the changes?<html>";
        if (!saved) {
            int x = JOptionPane.showConfirmDialog(this.npd.fram, strMsg, applicationTitle, JOptionPane.YES_NO_CANCEL_OPTION);

            if (x == JOptionPane.CANCEL_OPTION) {
                return false;
            }
            if (x == JOptionPane.YES_OPTION && !saveAsFile()) {
                return false;
            }
        }
        return true;
    }
///////////////////////////////////////

    void newFile() {
        if (!confirmSave()){
            return;
        }

        this.npd.textarea.setText("");
        fileName = new String("Untitled");
        fileRef = new File(fileName);
        saved = true;
        newFileFlag = true;
        this.npd.fram.setTitle(fileName + " - " + applicationTitle);
    }
//////////////////////////////////////

}


