package clients;
/**
 * Client joc Connecta 4
 * @author Cristina de la Iglesia, Jordi Palomino
 * @version 1.0
 */
import java.io.*;
import java.net.Socket;
import java.util.Scanner;


public class Client {
    static boolean comprovarguanyador;
    public static void main(String[] args){
        String connexio = obtenirConnexio();
        int port = obtenirPort(connexio);
        connexio = separarPortdeIP(connexio);
        try {
            Scanner scan = new Scanner(System.in);
            Socket s = new Socket(connexio, port);
            DataInputStream din = new DataInputStream(s.getInputStream());
            DataOutputStream dout = new DataOutputStream(s.getOutputStream());
            Boolean jugador = esperarJugador(din);
            System.out.println(din.readUTF());
            String str="",str2="";
            while(true) {
                for (int i = 0; i <= 55; ++i) {
                    str = din.readUTF();
                    System.out.print(str);
                }

                if (jugador == false) {
                    System.out.print(din.readUTF());
                    comprovarguanyador = din.readBoolean();
                    if(comprovarguanyador == true){
                        
                        for (int i = 0; i <= 55; ++i) {
                            str = din.readUTF();
                            System.out.print(str);
                        }
                        System.out.println("Has perdut");
                        s.close();
                        return;
                    }
                    jugador = true;
                }

                else {
                    boolean comprovapeca = false;
                    do {
                        System.out.print(din.readUTF());
                        dout.writeInt(scan.nextInt());
                        scan.nextLine();
                        dout.flush();
                        comprovapeca = din.readBoolean();
                        if(comprovapeca == false){
                            System.out.println(din.readUTF());
                        }
                    }while (comprovapeca != true);
                    comprovarguanyador = din.readBoolean();
                    if(comprovarguanyador == true){
                        for (int i = 0; i <= 55; ++i) {
                            str = din.readUTF();
                            System.out.print(str);
                        }
                        System.out.println("Has guanyat");
                        s.close();
                        return;
                    }
                    jugador = false;

                }
                System.out.println();
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * Mètode per escriure la connexió del servidor
     * @return String de la IP + port
     */
    private static String obtenirConnexio(){
        String connexio;
        Scanner scan = new Scanner(System.in);
        System.out.print("Digues connexió al servidor: ");
        connexio = scan.nextLine();
        return connexio;
    }

    /**
     * Mètode per obtenir el port de la connexió
     * @param connexio
     * @return
     */
    private static int obtenirPort(String connexio) {
        int delimiter = connexio.indexOf(':');
        String port = connexio.substring(delimiter + 1);
        return Integer.parseInt(port);
    }

    /**
     * Mètode peer treure el port de la variable Connexió
     * @param connexio String amb la IP i el port
     * @return String només amb la IP
     */
    private static String separarPortdeIP(String connexio){
        int delimiter = connexio.indexOf(':');
        connexio = connexio.substring(0,delimiter);
        return connexio;
    }

    /**
     * Mètode per fer l'espera del 2n jugador
     * @param din DataInputStream per rebre missatges del servidor
     * @return booleà de l'espera que identifica al jugador.
     */
    private static boolean esperarJugador(DataInputStream din) {
        try {
            System.out.println(din.readUTF());
            Boolean espera = din.readBoolean();
            if (espera == false) din.readBoolean();
            return espera;
        } catch (IOException e){
            System.out.println(e);
            return true;
        }
    }


}
