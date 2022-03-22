package jocxarxa;

import Servidor.Connexio;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class joc {
        int f = 7;
        int c = 8;
        String[][] tbuit = new String[f][c];

    public static void main(String[] args) {
        try {
            Connexio.mostrarip();
            ServerSocket ss = new ServerSocket(5000);
            Socket s1 = Connexio.establirconnexio(ss);
            DataInputStream din=new DataInputStream(s1.getInputStream());
            DataOutputStream dout=new DataOutputStream(s1.getOutputStream());
            Connexio.enviarEspera();
            Socket s2 = Connexio.establirconnexio(ss);
            DataInputStream din2=new DataInputStream(s2.getInputStream());
            DataOutputStream dout2=new DataOutputStream(s2.getOutputStream());
            BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
        } catch(IOException e){
            System.out.println(e);
        }
    }

    public void inicialitzarTaulell() {
        for (int i = 1; i < f - 1; i++) {
            for (int j = 1; j < c -1; j++) {
                tbuit[i][j] = ".";
            }
        }


    }

}
