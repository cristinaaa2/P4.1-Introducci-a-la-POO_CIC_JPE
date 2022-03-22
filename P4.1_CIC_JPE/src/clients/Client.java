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
            esperarJugador(din);
            String str="",str2="";
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

    public static void esperarJugador(DataInputStream din) {
        try {
            System.out.println(din.readUTF());
            Boolean espera = din.readBoolean();
            if (espera == false) din.readBoolean();
        } catch (IOException e){
            System.out.println(e);
        }
    }


}
