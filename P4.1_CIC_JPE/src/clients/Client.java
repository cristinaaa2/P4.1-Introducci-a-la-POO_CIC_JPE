package clients;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;


public class Client {
    public static void main(String[] args){
        String connexio = obtenirConnexio();
        int port = obtenirPort(connexio);
        connexio = separarPortdeIP(connexio);
        try {
            Socket s = new Socket(connexio, port);
            DataInputStream din = new DataInputStream(s.getInputStream());
            DataOutputStream dout = new DataOutputStream(s.getOutputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            Boolean jugador = esperarJugador(din);
            String str="",str2="";
            while(true) {
                //True és el jugador 2
                if (jugador == true) {
                }
                //False és el jugador 1
                else {
                    for (int i = 0; i <= 42; ++i) {
                        str = din.readUTF();
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static String obtenirConnexio(){
        String connexio;
        Scanner scan = new Scanner(System.in);
        System.out.print("Digues connexió al servidor: ");
        connexio = scan.nextLine();
        return connexio;
    }
    public static int obtenirPort(String connexio) {
        int delimiter = connexio.indexOf(':');
        String port = connexio.substring(delimiter + 1);
        return Integer.parseInt(port);
    }

    public static String separarPortdeIP(String connexio){
        int delimiter = connexio.indexOf(':');
        connexio = connexio.substring(0,delimiter);
        return connexio;
    }

    public static boolean esperarJugador(DataInputStream din) {
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
