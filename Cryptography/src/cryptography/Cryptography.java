
package cryptography;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Esli
 */
public class Cryptography {
    private int alpha, betha, ncaracter, caux, invmul;
    private char[] letra;
    private String[] palabra;
    private String nlinea="";
    private int[] euclides;
    String caracter="";
    public static void main(String[] args) {
        // TODO code application logic here
    }

    public Cryptography() {
    }
    
    public void encrypt(int alpha, int betha) {
        euclides=euclidesExtendido(alpha, 26);
        if(mcd(alpha,26)!=1) {
            JOptionPane.showMessageDialog(null, "Incorrect Alpha");
        }
        else{
            FileReader fr=null;
            try {
                File archivo=new File("m.txt");
                BufferedWriter salida = new BufferedWriter(
                    new FileWriter(new File("c.txt")));
                fr = new FileReader (archivo);
                BufferedReader br= new BufferedReader(fr);
                String linea;
                while((linea=br.readLine())!=null){
                    palabra=linea.split(" ");

                    for(String pal: palabra){

                        letra=pal.toCharArray();
                        for (char c : letra) {

                            caux= (int)c;
                            ncaracter=((alpha*(caux-97)+betha)%26)+65;
                            caracter=String.valueOf(Character.toChars(ncaracter));
                            nlinea+=caracter;
                            //System.out.println("+++++++++++++++++++++++++++++++++++++"+caux+"/////"+ncaracter+"##############"+caracter);
                            //System.out.println(nlinea);
                        }
                    }
                }
                //System.out.println("salir"+nlinea);
                salida.write(nlinea);
                salida.close();
                fr.close();
                 JOptionPane.showMessageDialog(null, "Oki <3");

            } catch (FileNotFoundException ex) {
                Logger.getLogger(Cryptography.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Cryptography.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    fr.close();
                } catch (IOException ex) {
                    Logger.getLogger(Cryptography.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
    }
    
    public void decrypt(int alpha, int betha){
        nlinea="";
        euclides=euclidesExtendido(alpha, 26);
        if(mcd(alpha,26)!=1) {
            JOptionPane.showMessageDialog(null, "Incorrect Alpha");
        }
        else{
            FileReader fr=null;
            try {
                File archivo=new File("c.txt");
                BufferedWriter salida = new BufferedWriter(
                    new FileWriter(new File("rm.txt")));
                fr = new FileReader (archivo);
                BufferedReader br= new BufferedReader(fr);
                String linea;
                while((linea=br.readLine())!=null){
                   palabra=linea.split(" ");

                    for(String pal: palabra){

                        letra=pal.toCharArray();


                        for (char c : letra) {
                            caux= (int)c;
                            if((euclides[1])<0) invmul=invadd(Math.abs(euclides[1]));
                            else  {
                                invmul=euclides[1];
                            }
                            System.out.println("aqui andamos checando"+invmul+"    "+invadd(betha));
                            ncaracter=((invmul*((caux-65)+invadd(betha)))%26)+97;
                            caracter=String.valueOf(Character.toChars(ncaracter));
                            nlinea+=caracter;
                            System.out.println("+++++++++++++++++++++++++++++++++++++"+caux+"/////"+ncaracter+"##############"+caracter);
                        }
                    }
                }
                System.out.println(nlinea);
                salida.write(nlinea);
                salida.close();
                fr.close();
                JOptionPane.showMessageDialog(null, "Oki <3");
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Cryptography.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Cryptography.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    fr.close();
                } catch (IOException ex) {
                    Logger.getLogger(Cryptography.class.getName()).log(Level.SEVERE, null, ex);
                }
            }   
        }
    }
    // inverso aditivo
    public int invadd(int betha){
        int inverso=0;
      if (betha==0) inverso=0;
      else {
          inverso=26-betha;
      }
      return inverso;
    }    
    //mcd usando el euclides simple
public static int mcd(int a, int b){ 
    int r;
    while(b!=0){
        r = a%b; 
        a = b;
        b = r;
       // System.out.println("a"+a+"b"+b);
    }   
return a;
} 
/* procedimiento que calcula el valor del maximo común divisor
* de a y b siguiendo el algoritmo de euclides extendido,
* devolviendo en un arreglo la siguiente estructura [d,x,y]
* donde:
*    mcd(a,b) = d = a*x + b*y
**/ 
public static int[] euclidesExtendido(int a, int b) {
    int[] resp = new int[3];
    int x=0,y=0,d=0;
    if(b==0){
        resp[0] = a; resp[1] = 1; resp[2] = 0;
    }  
    else{
        int x2 = 1, x1 = 0, y2 = 0, y1 = 1;
        int q = 0, r = 0;
        while(b>0){
            q = (a/b);
            r = a - q*b;
            x = x2-q*x1;
            y = y2 - q*y1;
            a = b;
            b = r;
            x2 = x1;
            x1 = x;
            y2 = y1;
            y1 = y;
        }
        resp[0] = a;
        resp[1] = x2;
        resp[2] = y2;
       }
        return resp;  
}   
}
