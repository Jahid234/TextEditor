package texteditor;
import java.io.File;
import javax.swing.filechooser.FileFilter;

public final class FileFilterExtends extends FileFilter{
    private String extension;
    private String description;

    public FileFilterExtends(final String ext, final String desc){
        this.setExtension(ext);
        this.setDescription(desc);
    }
    
    @Override
    public boolean accept(File f){
        final String filename = f.getName();
        return f.isDirectory()
            || extension == null
            || filename.toUpperCase()
                .endsWith(extension.toUpperCase());
    }    

    @Override
    public String getDescription(){
        return description;
    }    
    
    public void setDescription(String desc){
        if(desc == null){
            description = "All Files(*.*)";
        } 
        else{
            description = desc;
        }
    }
    
    
    public void setExtension(String ext){
        if (ext == null){
            extension = null;
            return;
        }

        extension = ext.toLowerCase();
        if (!ext.startsWith(".")){
            extension = "." + extension;
        }
    }
}
