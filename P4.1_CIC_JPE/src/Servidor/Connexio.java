package Servidor;

import jocxarxa.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Connexio {
    public static void mostrarip() throws IOException {
        String ip;
        Socket s = new Socket("www.google.com",80);
        ip = s.getLocalAddress().getHostAddress();
        System.out.println("La ip del servidor per realitzar la connexió és: " + ip + ":5000");
    }
    public static Socket establirconnexio(ServerSocket ss){
        try {
            return ss.accept();
        }
        catch (IOException e) {
            System.out.println(e);
            return null;
        }
    }
    public static void enviarEspera(){
        
    }
}
