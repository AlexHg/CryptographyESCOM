
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class MyCipher {
    //----Docum¿ento a cifrar, AES/DES, Cifrar,Decifrar, METDOD OFB;TC, HASH----
    
    //-----------------------------DES/AES--------------------------------------
    public static String AES_DES(String standard,int modo,String metodo,
            String nombreArchivo, String clave){
        /*
            RETURN CipherText
            MODO:   1 - ENCRYPT
                    2 - DECRYPT
            STANDARD:   AES
                        DES
            METODO: ECB
                    CBC
                    OFB
                    CFB
        */
        try{
            SecretKeySpec ks = new SecretKeySpec(clave.getBytes(),standard);
            try{
                Cipher cifrado = Cipher.getInstance(standard+"/"+metodo+"/PKCS5Padding");
                //Escojo modo cifrado o descifrado segun sea el caso
                byte[] ivDES = {1,2,3,4,5,6,7,8};
                byte[] ivAES = {1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6};
                //MODO 1 ENCRYPT
                if (modo==1){
                    if(metodo.equals("ECB"))
                        cifrado.init(Cipher.ENCRYPT_MODE, ks);
                    else{
                        if(standard.equals("DES")){
                            IvParameterSpec ivspec = new IvParameterSpec(ivDES);
                            cifrado.init(Cipher.ENCRYPT_MODE, ks, ivspec);
                            System.out.println("Encrypt");
                        }else if(standard.equals("AES")){
                            IvParameterSpec ivspec = new IvParameterSpec(ivAES);
                            cifrado.init(Cipher.ENCRYPT_MODE, ks, ivspec);
                            System.out.println("Encrypt");
                        }
                    }
                }
                //MODO = 2 DECRYPT
                else if (modo==2){
                    if(metodo.equals("ECB"))
                        cifrado.init(Cipher.DECRYPT_MODE, ks);
                    else{
                        if(standard.equals("DES")){
                            IvParameterSpec ivspec = new IvParameterSpec(ivDES);
                            cifrado.init(Cipher.DECRYPT_MODE, ks, ivspec);
                            System.out.println("Decrypt");
                        }else if(standard.equals("AES")){
                            IvParameterSpec ivspec = new IvParameterSpec(ivAES);
                            cifrado.init(Cipher.DECRYPT_MODE, ks, ivspec);
                            System.out.println("Decrypt");
                        }
                    }
                }
                byte[] bloque_cifrado;
                String textoCifrado;
                //OutputStream fich_out = new FileOutputStream( salidaArchivo );
                try ( //Leer fichero
                    InputStream archivo = new FileInputStream( nombreArchivo )) {
                    //OutputStream fich_out = new FileOutputStream( salidaArchivo );
                    byte[] buffer = new byte[8];
                    textoCifrado = new String();
                    int fin_archivo = -1;
                    int leidos=0;//numero de bytes leidos
                    leidos = archivo.read(buffer); 
                    System.out.println("leidos "+leidos);
                    while( leidos != fin_archivo ) {
                        bloque_cifrado = cifrado.update(buffer,0,leidos);
                        textoCifrado = textoCifrado
                                + new String(bloque_cifrado,"ISO-8859-1");
                        leidos = archivo.read(buffer);
                        System.out.println("leidos "+leidos);
                    }
                }
                bloque_cifrado = cifrado.doFinal();//textoCifrado.getBytes());
                textoCifrado = textoCifrado 
                        + new String(bloque_cifrado,"ISO-8859-1");
                //ISO-8859-1 es ISO-Latin-1
                return textoCifrado;
            }
            //Inicializacion de cifrado
            catch(javax.crypto.NoSuchPaddingException nspe)
                {System.out.println("Instancia");} //Instanciacion DES
            catch(javax.crypto.IllegalBlockSizeException ibse) 
                {System.out.println(ibse.toString());}//metodo doFinal
            catch(javax.crypto.BadPaddingException bpe)
                {System.out.println("Do final");} 
            catch(FileNotFoundException ex)
                {Logger.getLogger(javax.crypto.Cipher.class.getName())
                        .log(Level.SEVERE, null, ex);}
            catch (IOException | InvalidAlgorithmParameterException ex)
                {Logger.getLogger(javax.crypto.Cipher.class.getName())
                        .log(Level.SEVERE, null, ex);}
        }
        //pasar clave a la clase SecretKey
        catch(java.security.InvalidKeyException ike)
            {System.out.println(ike.toString());}
        catch(java.security.NoSuchAlgorithmException nsae)
            {System.out.println("NoSuchAlgorithm");}  
        return null;
    }
    
    //-------------------------- HASH (MD5 - SHA1) -----------------------------
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
    
    //--------------------------------RSA---------------------------------------
    public static void RSAKeysGenerator(){
	try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048);
            KeyPair kp = kpg.generateKeyPair();
            Base64.Encoder encoder = Base64.getEncoder();
            Key pub = kp.getPublic();
            Key pvt = kp.getPrivate();
            FileOutputStream out = new FileOutputStream("RSAPrivateKey" + ".key");
            out.write(pvt.getEncoded());
            out.close();

            out = new FileOutputStream("RSAPublicKey" + ".key");
            out.write(pub.getEncoded());
            out.close();
            // prints "Private key format: PKCS#8" on my machine
            System.err.println("Private key format: " + pvt.getFormat());
            // prints "Private key format: X.509" on my machine
            System.err.println("Public key format: " + pub.getFormat());
        } catch (IOException | NoSuchAlgorithmException ex) {
            Logger.getLogger(MyCipher.class.getName()).log(Level.SEVERE,null,ex);
        }
    }
    
    public static byte[] RSAencrypt(PrivateKey privateKey, String message) 
            throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");  
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);  
        return cipher.doFinal(message.getBytes());  
    }
    
    public static byte[] RSAdecrypt(PublicKey publicKey, byte [] encrypted) 
            throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");  
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return cipher.doFinal(encrypted);
    }
    
    public static PrivateKey readPrivateKey(String keyFile){    
        try {
            /* Read all bytes from the private key file */
            Path path = Paths.get(keyFile);
            byte[] bytes = Files.readAllBytes(path);
            /* Generate private key. */
            PKCS8EncodedKeySpec ks = new PKCS8EncodedKeySpec(bytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PrivateKey pvt = kf.generatePrivate(ks);
            return pvt;
        } catch (NoSuchAlgorithmException | IOException ex) {
            Logger.getLogger(MyCipher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(MyCipher.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static PublicKey readPublicKey(String name) 
            throws NoSuchAlgorithmException, InvalidKeySpecException, IOException{
        File d = new File(name);
        String ruta = d.getAbsolutePath();
        String nombre= d.getAbsolutePath();
        Path path = d.toPath();
        byte[] bytes = null;
        bytes = Files.readAllBytes(path);
        //byte[] bytes = Files.readAllBytes(d);
        /* Generate public key. */
        X509EncodedKeySpec ks = new X509EncodedKeySpec(bytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey pub = kf.generatePublic(ks);
        return pub;
      }
    
    public static void main(String[] args) throws Exception {
        RSAKeysGenerator();
        PrivateKey privateKey = readPrivateKey("RSAPrivateKey.key");
        PublicKey pubKey = readPublicKey("RSAPublicKey.key");
        byte [] encrypted = RSAencrypt(privateKey, "This is a secret message");     
        System.out.println(new String(encrypted));  // <<encrypted message>>
        // decrypt the message
        byte[] secret = RSAdecrypt(pubKey, encrypted);                                 
        System.out.println(new String(secret));     // This is a secret message
    }
    
    //---------PRUEBAS LISTA---------
    //AES - DES 
    //HASH 
    //RSA KEY GENERATOR
    //RSA ENCRYPT-DECRYPT
}
