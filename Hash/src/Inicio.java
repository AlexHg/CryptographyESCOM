import javax.swing.JFileChooser;
import java.net.*;
import java.io.*;
public class Inicio {
        public static void main(String[] args) {
            Inter ui = new Inter();
        
        
        new FileDrop( System.out, ui.tArea1, /*dragBorder,*/ new FileDrop.Listener()
        {   public void filesDropped( java.io.File[] files )
            {
                for( int i = 0; i < files.length; i++ )
                {   ui.agregarDoc(files[i]);
                    System.out.println("aqui");
                    // end try
                }   // end for: through each dropped file
             
            }   // end filesDropped
        }); 

       
        ui.setVisible(true);
        
    }
    
}
