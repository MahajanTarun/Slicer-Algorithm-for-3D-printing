import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;

/**
 * This simple app will launch a JFileChooser window to load an STL file.
 * @author CCHall
 */
public class TestApp {
	private static File askForFile(){
		JFileChooser jfc = new JFileChooser();
		int action = jfc.showOpenDialog(null);
		if(action != JFileChooser.APPROVE_OPTION){
			return null;
		}
		return jfc.getSelectedFile();
	}
	/**
	 * Entry point of program
	 * @param arg ignored
	 */
	public static void main(String[] arg){
		File f = askForFile();
		if(f == null){
			// canceled by user
			Logger.getLogger(STLParser.class.getName()).log(Level.WARNING, "Canceled by user");
			System.exit(0);
		}
		try {
			
			// read file to array of triangles
			List<Triangle> mesh = STLParser.parseSTLFile(f.toPath());
			
			
			// show the results
			mesh.forEach((Triangle t)->{System.out.println(t);});
		} catch (IOException ex) {
			Logger.getLogger(STLParser.class.getName()).log(Level.SEVERE, null, ex);
			System.exit(ex.hashCode());
		}
		System.exit(0);
	}
	
}
