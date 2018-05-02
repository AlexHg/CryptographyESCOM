import java.awt.Desktop;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
public class MyCipher{
    private static JFrame ventana;
    private static JPanel contenedor;
    private static JLabel etiqueta;
 

    public static void mostrarImagen(String nombre) throws IOException{
        System.out.println("LISTO!!!!!");
        File objetoFile = new File(nombre);
        Desktop.getDesktop().open(objetoFile);
    }
    
    /*
        Modo:   1 - Encrypt
                2 - Decrypt
        Clave:  8 bytes, ejemplo "asegurar"
    */
    public static void DES_ECB(int modo, String clave, String nombreArchivo){
        String salidaArchivo="Error.bmp";
        try{
            SecretKeySpec ks = new SecretKeySpec(clave.getBytes(),"DES");
            try{
                Cipher cifrado = Cipher.getInstance("DES/ECB/PKCS5Padding");
                //ECB CBC OFB CFB
                //Escojo modo cifrado o descifrado segun sea el caso
                if (modo==1){
                    cifrado.init(Cipher.ENCRYPT_MODE, ks);
                    System.out.println("Encrypt");
                    salidaArchivo="c_ECB_"+nombreArchivo;
                }//MODO CIFRAR
                if (modo==2){
                    cifrado.init(Cipher.DECRYPT_MODE, ks);
                    System.out.println("Decrypt ");
                    salidaArchivo="rm_ECB_"+nombreArchivo;
                }
                //Leer fichero
                InputStream archivo = new FileInputStream( nombreArchivo );
                OutputStream fich_out = new FileOutputStream ( salidaArchivo );

                byte[] buffer1 = new byte[54];
                byte[] buffer = new byte[8];
                byte[] bloque_cifrado;
                String textoCifrado = new String();
                String encabezado = new String();
                String terminal = new String();
                int fin_archivo = -1;
                int leidos=0;//numero de bytes leidos
                leidos = archivo.read(buffer1);
                encabezado =new String(buffer1,"ISO-8859-1"); 
                leidos = archivo.read(buffer);          
                System.out.println("leidos "+leidos);
                while( leidos != fin_archivo ) {
                   bloque_cifrado = cifrado.update(buffer,0,leidos);
                   textoCifrado = textoCifrado + new String(bloque_cifrado,"ISO-8859-1"); 
                   leidos = archivo.read(buffer);          
                }
                archivo.close();
                bloque_cifrado = cifrado.doFinal();//textoCifrado.getBytes());
                textoCifrado = textoCifrado + new String(bloque_cifrado,"ISO-8859-1");
                //ISO-8859-1 es ISO-Latin-1
                terminal=encabezado+textoCifrado;
                fich_out.write(terminal.getBytes("ISO-8859-1"));//escribir fichero
                mostrarImagen(salidaArchivo);
            }
            //Inicializacion de cifrado
            catch(javax.crypto.NoSuchPaddingException nspe) {System.out.println("Instancia");} //Instanciacion DES
            catch(javax.crypto.IllegalBlockSizeException ibse) {System.out.println(ibse.toString());}//metodo doFinal
            catch(javax.crypto.BadPaddingException bpe) {System.out.println("Do final");} catch (FileNotFoundException ex) {
                Logger.getLogger(Cipher.class.getName()).log(Level.SEVERE, null, ex);
            }catch (IOException ex) {
                Logger.getLogger(Cipher.class.getName()).log(Level.SEVERE, null, ex);
            }
            //metodo doFinal
            //metodo doFinal
        }
        //pasar clave a la clase SecretKey
        catch(java.security.InvalidKeyException ike) {System.out.println(ike.toString());}
        catch(java.security.NoSuchAlgorithmException nsae) {System.out.println("NoSuchAlgorithm");}
    }
    
    public static void DES_(int modo,String metodo, String clave, String nombreArchivo){
    String salidaArchivo="Error.bmp";
        try{
            SecretKeySpec ks = new SecretKeySpec(clave.getBytes(),"DES");
            try{
                Cipher cifrado = Cipher.getInstance("DES/"+metodo+"/PKCS5Padding");
                //ECB CBC OFB CFB
                //Escojo modo cifrado o descifrado segun sea el caso
                if (modo==1){
                    byte[] iv = {1,2,3,4,5,6,7,8};
                    IvParameterSpec ivspec = new IvParameterSpec(iv);
                    cifrado.init(Cipher.ENCRYPT_MODE, ks, ivspec);
                    System.out.println("Encrypt");
                    salidaArchivo="c_"+metodo+"_"+nombreArchivo;
                }//MODO CIFRAR
                if (modo==2){
                    byte[] iv = {1,2,3,4,5,6,7,8};
                    IvParameterSpec ivspec = new IvParameterSpec(iv);
                    cifrado.init(Cipher.DECRYPT_MODE, ks, ivspec);
                    System.out.println("Decrypt ");
                    salidaArchivo="rm_"+metodo+"_"+nombreArchivo;
                }
                //Leer fichero
                InputStream archivo = new FileInputStream( nombreArchivo );
                OutputStream fich_out = new FileOutputStream ( salidaArchivo );

                byte[] buffer1 = new byte[54];
                byte[] buffer = new byte[8];
                byte[] bloque_cifrado;
                String textoCifrado = new String();
                String encabezado = new String();
                String terminal = new String();
                int fin_archivo = -1;
                int leidos=0;//numero de bytes leidos
                leidos = archivo.read(buffer1);
                encabezado =new String(buffer1,"ISO-8859-1"); 
                leidos = archivo.read(buffer);          
                System.out.println("leidos "+leidos);
                while( leidos != fin_archivo ) {
                   bloque_cifrado = cifrado.update(buffer,0,leidos);
                   textoCifrado = textoCifrado + new String(bloque_cifrado,"ISO-8859-1"); 
                   leidos = archivo.read(buffer);          
                }
                archivo.close();
                bloque_cifrado = cifrado.doFinal();//textoCifrado.getBytes());
                textoCifrado = textoCifrado + new String(bloque_cifrado,"ISO-8859-1");
                //ISO-8859-1 es ISO-Latin-1
                terminal=encabezado+textoCifrado;
                fich_out.write(terminal.getBytes("ISO-8859-1"));//escribir fichero
                fich_out.close();
                mostrarImagen(salidaArchivo);
            }
            //Inicializacion de cifrado
            catch(javax.crypto.NoSuchPaddingException nspe) {System.out.println("Instancia");} //Instanciacion DES
            catch(javax.crypto.IllegalBlockSizeException ibse) {System.out.println(ibse.toString());}//metodo doFinal
            catch(javax.crypto.BadPaddingException bpe) {System.out.println("Do final");} catch (FileNotFoundException ex) {
                Logger.getLogger(Cipher.class.getName()).log(Level.SEVERE, null, ex);
            }catch (IOException ex) {
                Logger.getLogger(Cipher.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvalidAlgorithmParameterException ex) {
            Logger.getLogger(Cipher.class.getName()).log(Level.SEVERE, null, ex);
        }
            //metodo doFinal
            //metodo doFinal
        }
        //pasar clave a la clase SecretKey
        catch(java.security.InvalidKeyException ike) {System.out.println(ike.toString());}
        catch(java.security.NoSuchAlgorithmException nsae) {System.out.println("NoSuchAlgorithm");}  
    }
    
    public static void AES_ECB(int modo, String clave, String nombreArchivo){
        String salidaArchivo="Error.bmp";
        try{
            SecretKeySpec ks = new SecretKeySpec(clave.getBytes(),"AES");
            try{
                Cipher cifrado = Cipher.getInstance("AES/ECB/PKCS5Padding");
                //ECB CBC OFB CFB
                //Escojo modo cifrado o descifrado segun sea el caso
                if (modo==1){
                    cifrado.init(Cipher.ENCRYPT_MODE, ks);
                    System.out.println("Encrypt");
                    salidaArchivo="c_ECB_"+nombreArchivo;
                }//MODO CIFRAR
                if (modo==2){
                    cifrado.init(Cipher.DECRYPT_MODE, ks);
                    System.out.println("Decrypt ");
                    salidaArchivo="rm_ECB_"+nombreArchivo;
                }
                //Leer fichero
                InputStream archivo = new FileInputStream( nombreArchivo );
                OutputStream fich_out = new FileOutputStream ( salidaArchivo );

                byte[] buffer1 = new byte[54];
                byte[] buffer = new byte[8];
                byte[] bloque_cifrado;
                String textoCifrado = new String();
                String encabezado = new String();
                String terminal = new String();
                int fin_archivo = -1;
                int leidos=0;//numero de bytes leidos
                leidos = archivo.read(buffer1);
                encabezado =new String(buffer1,"ISO-8859-1"); 
                leidos = archivo.read(buffer);          
                System.out.println("leidos "+leidos);
                while( leidos != fin_archivo ) {
                   bloque_cifrado = cifrado.update(buffer,0,leidos);
                   textoCifrado = textoCifrado + new String(bloque_cifrado,"ISO-8859-1"); 
                   leidos = archivo.read(buffer);          
                }
                archivo.close();
                bloque_cifrado = cifrado.doFinal();//textoCifrado.getBytes());
                textoCifrado = textoCifrado + new String(bloque_cifrado,"ISO-8859-1");
                //ISO-8859-1 es ISO-Latin-1
                terminal=encabezado+textoCifrado;
                fich_out.write(terminal.getBytes("ISO-8859-1"));//escribir fichero
                mostrarImagen(salidaArchivo);
            }
            //Inicializacion de cifrado
            catch(javax.crypto.NoSuchPaddingException nspe) {System.out.println("Instancia");} //Instanciacion DES
            catch(javax.crypto.IllegalBlockSizeException ibse) {System.out.println(ibse.toString());}//metodo doFinal
            catch(javax.crypto.BadPaddingException bpe) {System.out.println("Do final");} catch (FileNotFoundException ex) {
                Logger.getLogger(Cipher.class.getName()).log(Level.SEVERE, null, ex);
            }catch (IOException ex) {
                Logger.getLogger(Cipher.class.getName()).log(Level.SEVERE, null, ex);
            }
            //metodo doFinal
            //metodo doFinal
        }
        //pasar clave a la clase SecretKey
        catch(java.security.InvalidKeyException ike) {System.out.println(ike.toString());}
        catch(java.security.NoSuchAlgorithmException nsae) {System.out.println("NoSuchAlgorithm");}
    }
    
    public static void AES_(int modo,String metodo, String clave, String nombreArchivo){
    String salidaArchivo="Error.bmp";
        try{
            SecretKeySpec ks = new SecretKeySpec(clave.getBytes(),"AES");
            try{
                Cipher cifrado = Cipher.getInstance("AES/"+metodo+"/PKCS5Padding");
                //ECB CBC OFB CFB
                //Escojo modo cifrado o descifrado segun sea el caso
                if (modo==1){
                    byte[] iv = {1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6};
                    IvParameterSpec ivspec = new IvParameterSpec(iv);
                    cifrado.init(Cipher.ENCRYPT_MODE, ks, ivspec);
                    System.out.println("Encrypt");
                    salidaArchivo="c_"+metodo+"_"+nombreArchivo;
                }//MODO CIFRAR
                if (modo==2){
                    byte[] iv = {1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6};
                    IvParameterSpec ivspec = new IvParameterSpec(iv);
                    cifrado.init(Cipher.DECRYPT_MODE, ks, ivspec);
                    System.out.println("Decrypt ");
                    salidaArchivo="rm_"+metodo+"_"+nombreArchivo;
                }
                //Leer fichero
                InputStream archivo = new FileInputStream( nombreArchivo );
                OutputStream fich_out = new FileOutputStream ( salidaArchivo );

                byte[] buffer1 = new byte[54];
                byte[] buffer = new byte[8];
                byte[] bloque_cifrado;
                String textoCifrado = new String();
                String encabezado = new String();
                String terminal = new String();
                int fin_archivo = -1;
                int leidos=0;//numero de bytes leidos
                leidos = archivo.read(buffer1);
                encabezado =new String(buffer1,"ISO-8859-1"); 
                leidos = archivo.read(buffer);          
                System.out.println("leidos "+leidos);
                while( leidos != fin_archivo ) {
                   bloque_cifrado = cifrado.update(buffer,0,leidos);
                   textoCifrado = textoCifrado + new String(bloque_cifrado,"ISO-8859-1"); 
                   leidos = archivo.read(buffer);          
                }
                archivo.close();
                bloque_cifrado = cifrado.doFinal();//textoCifrado.getBytes());
                textoCifrado = textoCifrado + new String(bloque_cifrado,"ISO-8859-1");
                //ISO-8859-1 es ISO-Latin-1
                terminal=encabezado+textoCifrado;
                fich_out.write(terminal.getBytes("ISO-8859-1"));//escribir fichero
                fich_out.close();
                mostrarImagen(salidaArchivo);
            }
            //Inicializacion de cifrado
            catch(javax.crypto.NoSuchPaddingException nspe) {System.out.println("Instancia");} //Instanciacion DES
            catch(javax.crypto.IllegalBlockSizeException ibse) {System.out.println(ibse.toString());}//metodo doFinal
            catch(javax.crypto.BadPaddingException bpe) {System.out.println("Do final");} catch (FileNotFoundException ex) {
                Logger.getLogger(Cipher.class.getName()).log(Level.SEVERE, null, ex);
            }catch (IOException ex) {
                Logger.getLogger(Cipher.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvalidAlgorithmParameterException ex) {
            Logger.getLogger(Cipher.class.getName()).log(Level.SEVERE, null, ex);
        }
            //metodo doFinal
            //metodo doFinal
        }
        //pasar clave a la clase SecretKey
        catch(java.security.InvalidKeyException ike) {System.out.println(ike.toString());}
        catch(java.security.NoSuchAlgorithmException nsae) {System.out.println("NoSuchAlgorithm");}  
    }
    
    public static void main(String [] args) {
       //ECB CBC OFB CFB
        //String llave=JOptionPane.showInputDialog(null, "Key=");
        AES_(1,"OFB","asegurarasegurar","bandera.bmp");
        AES_(2,"OFB","asegurarasegurar","c_OFB_bandera.bmp");
    }
}