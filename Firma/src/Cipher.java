
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class Cipher {
     
    
//------------------------------------------------------------------Hash-----------------------------------    
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
//---------------------------------------------------------------------DES/AES------------------------------------
      public static String AES_DES (String opc,int modo,String metodo, String nombreArchivo, String clave){
          
      //  String salidaArchivo="Error.bmp";
        try{
            SecretKeySpec ks = new SecretKeySpec(clave.getBytes(),opc);
            try{
                javax.crypto.Cipher cifrado = javax.crypto.Cipher.getInstance(opc+metodo+"/PKCS5Padding");
                //ECB CBC OFB CFB
                //Escojo modo cifrado o descifrado segun sea el caso
                if (modo==1){
                    byte[] iv = {1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6};
                    IvParameterSpec ivspec = new IvParameterSpec(iv);
                    cifrado.init(javax.crypto.Cipher.ENCRYPT_MODE, ks, ivspec);
                    System.out.println("Encrypt");
                 //   salidaArchivo="c_"+metodo+"_"+nombreArchivo;
                }//MODO CIFRAR
                if (modo==2){
                    byte[] iv = {1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6};
                    IvParameterSpec ivspec = new IvParameterSpec(iv);
                    cifrado.init(javax.crypto.Cipher.DECRYPT_MODE, ks, ivspec);
                    System.out.println("Decrypt ");
                 //   salidaArchivo="rm_"+metodo+"_"+nombreArchivo;
                }
                //Leer fichero
                DataInputStream archivo = new DataInputStream(new FileInputStream( nombreArchivo));
                //InputStream archivo = new FileInputStream( nombreArchivo );
                //OutputStream fich_out = new FileOutputStream ( salidaArchivo );

                byte[] buffer = new byte[8];
                byte[] bloque_cifrado;
                String textoCifrado = new String();
                //String encabezado = new String();
                String terminal = new String();
                int fin_archivo = -1;
                int leidos=0;//numero de bytes leidos
                //leidos = archivo.read(buffer1);
                //encabezado =new String(buffer1,"ISO-8859-1"); 
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
                terminal=textoCifrado;
                //fich_out.write(terminal.getBytes("ISO-8859-1"));//escribir fichero
                //fich_out.close();
                return textoCifrado;
                
            }
            //Inicializacion de cifrado
            catch(javax.crypto.NoSuchPaddingException nspe) {System.out.println("Instancia");} //Instanciacion DES
            catch(javax.crypto.IllegalBlockSizeException ibse) {System.out.println(ibse.toString());}//metodo doFinal
            catch(javax.crypto.BadPaddingException bpe) {System.out.println("Do final");} catch (FileNotFoundException ex) {
                Logger.getLogger(javax.crypto.Cipher.class.getName()).log(Level.SEVERE, null, ex);
            }catch (IOException ex) {
                Logger.getLogger(javax.crypto.Cipher.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvalidAlgorithmParameterException ex) {
            Logger.getLogger(javax.crypto.Cipher.class.getName()).log(Level.SEVERE, null, ex);
        }
            //metodo doFinal
            //metodo doFinal
        }
        //pasar clave a la clase SecretKey
        catch(java.security.InvalidKeyException ike) {System.out.println(ike.toString());}
        catch(java.security.NoSuchAlgorithmException nsae) {System.out.println("NoSuchAlgorithm");}  
        return null;
    }
      //---------------------------------------------------String opc,int modo,String metodo, String nombreArchivo
     public static String AES_DES_ECB(String opc,int modo, String nombreArchivo, String clave){
        
        try{
            SecretKeySpec ks = new SecretKeySpec(clave.getBytes(),opc);
            try{
                javax.crypto.Cipher cifrado = javax.crypto.Cipher.getInstance(opc+"/ECB/PKCS5Padding");
                //ECB CBC OFB CFB
                //Escojo modo cifrado o descifrado segun sea el caso
                if (modo==1){
                    cifrado.init(javax.crypto.Cipher.ENCRYPT_MODE, ks);
                    System.out.println("Encrypt");
                    //salidaArchivo="c_ECB_"+nombreArchivo;
                }//MODO CIFRAR
                if (modo==2){
                    cifrado.init(javax.crypto.Cipher.DECRYPT_MODE, ks);
                    System.out.println("Decrypt ");
                    //salidaArchivo="rm_ECB_"+nombreArchivo;
                }
                //Leer fichero
                DataInputStream archivo = new DataInputStream(new FileInputStream( nombreArchivo));
                //InputStream archivo = new FileInputStream( nombreArchivo );
                //OutputStream fich_out = new FileOutputStream ( salidaArchivo );

                //byte[] buffer1 = new byte[54];
                byte[] buffer = new byte[8];
                byte[] bloque_cifrado;
                String textoCifrado = new String();
                //String encabezado = new String();
                String terminal = new String();
                int fin_archivo = -1;
                int leidos=0;//numero de bytes leidos
                //leidos = archivo.read(buffer1);
                //encabezado =new String(buffer1,"ISO-8859-1"); 
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
                terminal=textoCifrado;
                return terminal;
                //fich_out.write(terminal.getBytes("ISO-8859-1"));//escribir fichero

            }
            //Inicializacion de cifrado
            catch(javax.crypto.NoSuchPaddingException nspe) {System.out.println("Instancia");} //Instanciacion DES
            catch(javax.crypto.IllegalBlockSizeException ibse) {System.out.println(ibse.toString());}//metodo doFinal
            catch(javax.crypto.BadPaddingException bpe) {System.out.println("Do final");} catch (FileNotFoundException ex) {
                Logger.getLogger(javax.crypto.Cipher.class.getName()).log(Level.SEVERE, null, ex);
            }catch (IOException ex) {
                Logger.getLogger(javax.crypto.Cipher.class.getName()).log(Level.SEVERE, null, ex);
            }
            //metodo doFinal
            //metodo doFinal
        }
        //pasar clave a la clase SecretKey
        catch(java.security.InvalidKeyException ike) {System.out.println(ike.toString());}
        catch(java.security.NoSuchAlgorithmException nsae) {System.out.println("NoSuchAlgorithm");}
        return null;
    }
    
    
      
//----------------------------------------------------------------------------------RSA----------------------------------------------------
      public static String RSAencrypt(String plainText, PublicKey publicKey) throws Exception {
        javax.crypto.Cipher encryptCipher = javax.crypto.Cipher.getInstance("RSA");
        encryptCipher.init(javax.crypto.Cipher.ENCRYPT_MODE, publicKey);

        byte[] cipherText = encryptCipher.doFinal(plainText.getBytes(UTF_8));

        return Base64.getEncoder().encodeToString(cipherText);
    }
      public static String RSAdecrypt(String cipherText, PrivateKey privateKey) throws Exception {
        byte[] bytes = Base64.getDecoder().decode(cipherText);

        javax.crypto.Cipher decriptCipher = javax.crypto.Cipher.getInstance("RSA");
        decriptCipher.init(javax.crypto.Cipher.DECRYPT_MODE, privateKey);

        return new String(decriptCipher.doFinal(bytes), UTF_8);
    }
    
    public static String sign(String plainText, PrivateKey privateKey) throws Exception {
//        Signature privateSignature = Signature.getInstance("SHA256withRSA");
//        privateSignature.initSign(privateKey);
//        privateSignature.update(plainText.getBytes(UTF_8));
//
//        byte[] signature = privateSignature.sign();
//
//        return Base64.getEncoder().encodeToString(signature);
        javax.crypto.Cipher encryptCipher = javax.crypto.Cipher.getInstance("RSA");
        encryptCipher.init(javax.crypto.Cipher.ENCRYPT_MODE, privateKey);

        byte[] cipherText = encryptCipher.doFinal(plainText.getBytes(UTF_8));

        return Base64.getEncoder().encodeToString(cipherText);
    }

    public static boolean verify(String plainText, String signature, PublicKey publicKey) throws Exception {
        Signature publicSignature = Signature.getInstance("SHA256withRSA");
        publicSignature.initVerify(publicKey);
        publicSignature.update(plainText.getBytes(UTF_8));

        byte[] signatureBytes = Base64.getDecoder().decode(signature);

        return publicSignature.verify(signatureBytes);
    }
     //------------------------------abrir key RSA
//      public static PublicKey (File d){
//        String ruta = d.getAbsolutePath();
//        String nombre= d.getAbsolutePath();
//        Path path = d.toPath();
//        
//        byte[] bytes = Files.readAllBytes(path);
//      //  byte[] bytes = Files.readAllBytes(d);
//
//        /* Generate public key. */
//        X509EncodedKeySpec ks = new X509EncodedKeySpec(bytes);
//        KeyFactory kf = KeyFactory.getInstance("RSA");
//        PublicKey pub = kf.generatePublic(ks);
//        return pub;
//      }
      
   
      //---------------------Docum¿ento a cifrar, AES/DES, Cifrar,Decifrar, METDOD OFB;TC, HASH
      public static void Firmar(File d, String opc,int modo,String metodo, String hashType, File KeyRSA, File KPRec ){
        String ruta = d.getAbsolutePath();
        String nombre= d.getAbsolutePath();
        String txt = new String();
        String digesto= new String();
        String firma= new String();
        String c=new String ();
        String kDAES= new String ();
        long tam=0, leidos=0, n=0;
        
        try {
            DataInputStream archivo = new DataInputStream(new FileInputStream( ruta));
            tam=d.length();
            while(leidos< tam){
                    byte[] b = new byte[(int)tam];
                try {
                    n = archivo.read(b);
                } catch (IOException ex) {
                    System.out.println("Error al leer archivo \n");
                    Logger.getLogger(Cipher.class.getName()).log(Level.SEVERE, null, ex);
                }
                    txt=txt+ b.toString();
                    leidos= leidos + n;
            }
        //-----------digesto HASH
            digesto= getHash(txt,hashType);
            
            byte[] bytes = Files.readAllBytes(KeyRSA.toPath());
            byte[] bytes2 = Files.readAllBytes(KPRec.toPath());
            /* Generate public key. */
            PrivateKey priv;
            PublicKey pub;
            X509EncodedKeySpec ks = new X509EncodedKeySpec(bytes);
            KeyFactory kf = null;
            X509EncodedKeySpec ks2 = new X509EncodedKeySpec(bytes2);
            KeyFactory kf2 = null;
            try {
                kf = KeyFactory.getInstance("RSA");
                kf2 = KeyFactory.getInstance("RSA");
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(Cipher.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (modo==1){//modo 1 firmar 
                priv=kf.generatePrivate(ks);
                pub = kf2.generatePublic(ks2);
            }else {
                pub = kf.generatePublic(ks);
                priv=kf2.generatePrivate(ks2);
            }
    //-----------------------firmar-------        
            firma=sign (digesto, priv);
            
    //-----------------------------DES o AES
        //--------------------Clave
        SecureRandom sr = new SecureRandom();
              int x=0;
             if(opc.equals("AES")){
                  x=128;
             }else {
                  x=64;
             }
           sr.nextBytes(new byte[x]);
           String clave= sr.toString();
           sr.setSeed(System.currentTimeMillis());


           if(metodo.equals("ECB")){
               c = AES_DES_ECB(opc, modo,ruta , clave);
           }else{
                 c = AES_DES(opc,modo,metodo,ruta, clave);
           }
           
           kDAES= RSAencrypt(clave, pub);
           
        
        
        
        
            //PublicKey pub = kf.generatePublic(ks);
            //PrivateKey priv=kf.generatePrivate(ks);
            
            
        } catch (FileNotFoundException ex) {
            System.out.println("Error al abrir el archivo a frimar \n");
            Logger.getLogger(Cipher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Cipher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(Cipher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            System.out.println("Error firmar");
            Logger.getLogger(Cipher.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
      }
      
    
      
}
