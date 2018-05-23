
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidAlgorithmParameterException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
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
            File file, String clave){
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
                try ( //Leer fichero
                    InputStream archivo = new FileInputStream( file )) {
                    byte[] buffer = new byte[8];
                    textoCifrado = new String();
                    int fin_archivo = -1;
                    int leidos=0;//numero de bytes leidos
                    leidos = archivo.read(buffer); 
                    while( leidos != fin_archivo ) {
                        bloque_cifrado = cifrado.update(buffer,0,leidos);
                        textoCifrado = textoCifrado
                                + new String(bloque_cifrado,"ISO-8859-1");
                        leidos = archivo.read(buffer);
                    }
                }
                bloque_cifrado = cifrado.doFinal();//textoCifrado.getBytes());
                textoCifrado = textoCifrado 
                        + new String(bloque_cifrado,"ISO-8859-1"); //Latin-1
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
    
    public static byte[] privateKeyRSAencrypt(PrivateKey privateKey, String message) 
            throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");  
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);  
        return cipher.doFinal(message.getBytes());  
    }
    
    public static byte[] publicKeyRSAencrypt(PublicKey publicKey, String message) 
            throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");  
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);  
        return cipher.doFinal(message.getBytes());  
    }
    
    public static byte[] publicKeyRSAdecrypt(PublicKey publicKey, byte [] encrypted) 
            throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");  
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return cipher.doFinal(encrypted);
    }
    
    public static byte[] privateKeyRSAdecrypt(PrivateKey privateKey, byte [] encrypted) 
            throws Exception {   
        Cipher cipher = Cipher.getInstance("RSA");  
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(encrypted);
    }
    
    
    public static PrivateKey readPrivateKey(File d) 
            throws IOException, NoSuchAlgorithmException, InvalidKeySpecException{    
        String ruta = d.getAbsolutePath();
        String nombre= d.getAbsolutePath();
        Path path = d.toPath();
        byte[] bytes = null;
        bytes = Files.readAllBytes(path);
        PKCS8EncodedKeySpec ks = new PKCS8EncodedKeySpec(bytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PrivateKey pvt = kf.generatePrivate(ks);
        return pvt;
    }
    
    public static PublicKey readPublicKey(File d) 
            throws NoSuchAlgorithmException, InvalidKeySpecException, IOException{
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
    
    public static String leerArchivo(File archivo) 
            throws FileNotFoundException, IOException {
        String cadena="";
        String lectura="";
        FileReader f = new FileReader(archivo);
        BufferedReader b = new BufferedReader(f);
        while((cadena = b.readLine())!=null) {
            lectura+=cadena;
        }
        b.close();
        return lectura;
    }
    
    public static void processEncrypt(String standard, String operationMode, 
            String hashFunction, File privateKeyFile, File publicKeyFile, 
            File originalFile, String keyStandard){
        String data;
        try {
            data = leerArchivo(originalFile);
            String hashGotten=getHash(data, hashFunction);
            PrivateKey privateKey = readPrivateKey(privateKeyFile);
            PublicKey publicKey = readPublicKey(publicKeyFile);
            // 256 Bytes cipherKeyStandardBytes and hash_rsa
            byte[] hash_rsa=privateKeyRSAencrypt(privateKey, hashGotten);
            byte [] cipherKeyStandardBytes=publicKeyRSAencrypt(publicKey,keyStandard);
            String cipherText=AES_DES(standard,1,operationMode, 
                    originalFile , keyStandard);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
            outputStream.write( hash_rsa );
            outputStream.write( cipherKeyStandardBytes );
            outputStream.write( cipherText.getBytes("ISO-8859-1") );
            OutputStream fich_outA = new FileOutputStream("archivoAuxiliar1.txt");
            OutputStream fich_out = new FileOutputStream ( "archivoFirmado.txt" );
            fich_out.write(outputStream.toByteArray());
            fich_outA.write(cipherText.getBytes("ISO-8859-1"));
        } catch (IOException ex) {
            Logger.getLogger(MyCipher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(MyCipher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(MyCipher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(MyCipher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static boolean processDecrypt(String standard, String operationMode, 
            String hashFunction, File privateKeyFile, File publicKeyFile, 
            File originalFile){
        String data;
        try {
            byte[] hashBuffer = new byte[256];
            byte[] cipherKeyBuffer = new byte[256];
            byte[] dataBuffer = new byte[1024];
            String textoCifrado="";
            int fin_archivo = -1;
            PrivateKey privateKey = readPrivateKey(privateKeyFile);
            PublicKey publicKey = readPublicKey(publicKeyFile);
            InputStream archivo = new FileInputStream(originalFile);
            int leidos=0;//numero de bytes leidos
            archivo.read(hashBuffer);
            archivo.read(cipherKeyBuffer);
            
            OutputStream fich_out = new FileOutputStream ( "archivoAuxiliar.txt" );
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
            leidos = archivo.read(dataBuffer);
            while( leidos != fin_archivo ) {
                outputStream.write( dataBuffer );
                leidos = archivo.read(dataBuffer);          
            }
            fich_out.write(outputStream.toByteArray());
            fich_out.close();
            archivo.close();
            String clave=new String(privateKeyRSAdecrypt(privateKey, cipherKeyBuffer));
            System.out.println(clave);
            String hash=new String(publicKeyRSAdecrypt(publicKey, hashBuffer));
            System.out.println(hash);
            String textoOriginal=AES_DES(standard, 2, operationMode, new File("archivoAuxiliar1.txt"), clave);
            System.out.println(textoOriginal);
            String hashObtenido=getHash(textoOriginal, hashFunction);
            System.out.println(hashObtenido);
            if(hashObtenido.equals(hash))
                return true;
            archivo.close();
            
        } catch (IOException ex) {
            Logger.getLogger(MyCipher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(MyCipher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(MyCipher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(MyCipher.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
        
    public static void main(String[] args) throws Exception {
        /*/RSAKeysGenerator();
        PrivateKey privateKey = readPrivateKey(new File("RSAPrivateKey.key"));
        PublicKey pubKey = readPublicKey(new File("RSAPublicKey.key"));
        byte [] encrypted = RSAencrypt(privateKey, "This is a secret message");     
        System.out.println(new String(encrypted));  // <<encrypted message>>
        // decrypt the message
        byte[] secret = RSAdecrypt(pubKey, encrypted);                                 
        System.out.println(new String(secret));     // This is a secret message
        */
        processEncrypt("DES","ECB","MD5",new File("RSAPrivateKey.key"),
                new File("RSAPublicKey.key"),new File("archivo.txt"),"asegurar");
        
        processDecrypt("DES","ECB","MD5",new File("RSAPrivateKey.key"),
                new File("RSAPublicKey.key"),new File("archivoFirmado.txt"));
        
    }
    
 }
