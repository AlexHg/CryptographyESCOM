import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.swing.JOptionPane;

public class Hash {
    public static String hashType(String input,String type) {
        try {
            MessageDigest md = MessageDigest.getInstance(type);
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);

            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static String getHash(String txt, String hashType) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest
                    .getInstance(hashType);
            byte[] array = md.digest(txt.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100)
                        .substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    public static String leerArchivo(String archivo) throws FileNotFoundException, IOException {
      String cadena="";
      String lectura="";
      FileReader f = new FileReader(archivo);
      BufferedReader b = new BufferedReader(f);
      while((cadena = b.readLine())!=null) {
          //System.out.println(cadena);
          lectura+=cadena+"\n";
      }
      b.close();
      return lectura;
}
    
    public static void escribirArchivo(String mensaje){
        FileWriter fichero = null;
        PrintWriter pw = null;
        try{
            fichero = new FileWriter("enviar.txt");
            pw = new PrintWriter(fichero);
            String[] ms=mensaje.split("\n");
            for (String m:ms){
                pw.println(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
           try {
                if (null != fichero)
                fichero.close();
            }catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }
    
    public void generarHash(String archivo, String type) throws IOException{
        String mensaje=leerArchivo(archivo);
        mensaje+=getHash(mensaje,type);
        escribirArchivo(mensaje);
        JOptionPane.showMessageDialog(null,"Correct!");
    }
    
    public static void verificarMensaje(String type) throws IOException{
        String mensaje=leerArchivo("enviar.txt");
        String[] ms=mensaje.split("\n");
        String hashGenerado=ms[ms.length-1];
        String rm="";
        int i=0;
        while(i<ms.length-1){
            rm+=ms[i]+"\n";
            i++;
        }
        String hashRecuperado=getHash(rm,type);
        System.out.println(rm);
        System.out.println(hashGenerado);
        System.out.println(hashRecuperado);
        if(hashGenerado.equals(hashRecuperado))
            JOptionPane.showMessageDialog(null, "Message's correct!");
        else
            JOptionPane.showMessageDialog(null, "Message's incorrect!");
        
            
        
        
    }
    
    public static void main(String[] args) throws IOException {
        verificarMensaje("MD5");
    }
}
