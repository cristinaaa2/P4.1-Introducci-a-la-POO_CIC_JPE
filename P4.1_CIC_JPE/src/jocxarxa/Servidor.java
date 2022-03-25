package jocxarxa;
/**
 * Servidor joc Connecta 4
 * @author Cristina de la Iglesia, Jordi Palomino
 * @version 1.0
 */

import servidor.Connexio;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Servidor {
        static int f = 8;
        static int c = 9;
        static char[][] tbuit = new char[f][c];
        static Socket s1;
        static Socket s2;
        static DataInputStream din1;
        static DataInputStream din2;
        static DataOutputStream dout1;
        static DataOutputStream dout2;
        static ServerSocket ss;
        static Boolean jugador;
        static char peca;
        static boolean fipartida = false;
    static Scanner scan = new Scanner(System.in);

    /**
     * Classe Main del Servidor
     * @param args
     */
    public static void main(String[] args) {

        try {
            //Mostrar IP del Servidor
            Connexio.mostrarip();

            //Inicialitzar connexió amb el 1r Client
            ss = new ServerSocket(5000);
            s1 = Connexio.establirconnexio(ss);
            din1=new DataInputStream(s1.getInputStream());
            dout1=new DataOutputStream(s1.getOutputStream());
            jugador = true;
            Connexio.enviarEspera(dout1, jugador);

            //Inicialitzar connexió amb el 2n Client
            s2 = Connexio.establirconnexio(ss);
            din2=new DataInputStream(s2.getInputStream());
            dout2=new DataOutputStream(s2.getOutputStream());
            jugador = false;
            Connexio.enviarEspera(dout2, jugador);
            dout1.writeBoolean(true);
            dout1.flush();

            //Seleccionem el jugador 1
            jugador = true;

        } catch(IOException e){
            System.out.println(e);
        }
        inicialitzarTaulell();
        procesJoc();
        finalitzarjoc();


    }

    /**
     * Mètode per iniciar l'array Bidimensional del Connecta 4
     */
    private static void inicialitzarTaulell() {
            for (int i = 1; i < f - 1; i++) {
                for (int j = 1; j < c -1; j++) {
                    tbuit[i][j] = '.';
                }
            }
            try {
                dout2.writeUTF("Tens la peça: O");
                dout1.writeUTF("Tens la peça: X");
            } catch (IOException e){
                System.out.println(e);
            }
            mostrarTaulell();
        }

    /**
     * Mètode per mostrar el taulell als 2 clients
     */
        private static void mostrarTaulell() {
            System.out.println();
            int n = 0;
            do {
                n++;
                System.out.printf("%2d ", n);
                String enviar = String.format("%2d ", n);
                try{
                    dout1.writeUTF(enviar);
                    dout1.flush();
                    dout2.writeUTF(enviar);
                    dout2.flush();
                } catch (IOException e){
                    System.out.println(e);
                }


            } while (c-2 != n);
            System.out.println();
            try{
                dout1.writeUTF("\n");
                dout1.flush();
                dout2.writeUTF("\n");
                dout2.flush();
            } catch (IOException e){
                System.out.println(e);
            }
            for (int i =1; i < tbuit.length -1; i++) {
                for (int j = 1; j < tbuit[i].length - 1; j++) {
                    System.out.print(" " + tbuit[i][j] + " ");
                    String enviar = String.format(" " + tbuit[i][j] + " ");
                    try{
                        dout1.writeUTF(enviar);
                        dout1.flush();
                        dout2.writeUTF(enviar);
                        dout2.flush();
                    } catch (IOException e){
                        System.out.println(e);
                    }
                }
                System.out.println();
                String enviar = String.format("\n");
                try{
                    dout1.writeUTF("\n");
                    dout1.flush();
                    dout2.writeUTF("\n");
                    dout2.flush();
                } catch (IOException e){
                    System.out.println(e);
                }
            }
        }

    /**
     * Mètode per demanar al client on vol posar la seva peça
     * @param peca Caràcter que defineix la peça que té el jugador
     * @param din DatainputStream Rebre missatges del client
     * @param dout DataOutputStream Enviar missatges al client
     * @throws IOException
     */
    private static void afegirPeca(char peca, DataInputStream din, DataOutputStream dout) throws IOException {
        int i;
        int cp = 0;
        do {
            dout.writeUTF("En quina columna vols posar la peça " +  peca + " (1-7): ");
            dout.flush();
            cp = din.readInt();
            i = 0;
            if (cp > 0 && cp <= 7) {
                while (tbuit[i + 1][cp] == 46) {
                    ++i;
                }
                if (i != 0) {
                    tbuit[i][cp] = peca;
                    dout.writeBoolean(true);
                    comprovarLinia(i, cp, peca);
                    mostrarTaulell();

                } else {
                    dout.writeBoolean(false);
                    dout.flush();
                    System.out.println("Aquesta columna està plena tria un altre.");
                    dout.writeUTF("Aquesta columna està plena tria un altre.\n");
                    dout.flush();
                }
            } else {
                dout.writeBoolean(false);
                dout.flush();
                System.out.println("El numero a de ser entre 1 i 7.");
                dout.writeUTF("El numero a de ser entre 1 i 7.\n");
                dout.flush();
            }
        } while ((i == 0) || !(cp > 0 && cp <= 7));

    }

    /**
     * Bucle del joc per continuar la partida.
     */
    private static void procesJoc(){
        while (fipartida != true){
            if(jugador == false) {
                peca = 'X';
                try {
                    dout2.writeUTF("Esperi el seu torn...");
                    dout2.flush();
                    afegirPeca(peca, din1, dout1);
                } catch (IOException e){
                    System.out.println(e);
                }
                jugador = true;
            } else {
                peca = 'O';
                try {
                    dout1.writeUTF("Esperi el seu torn...");
                    dout1.flush();
                    afegirPeca(peca, din2, dout2);
                } catch (IOException e){
                    System.out.println(e);
                }
                jugador = false;
            }
        }
    }

    /**
     * Mètode per anar comprovant si ha fet una linia de 4 fitxes
     * @param i Enter que defineix la fila de la peça
     * @param cp Enter que defineix la columna de la peça
     * @param peca La peça que té el jugador per comprovar si ha guanyat
     */
    private static void comprovarLinia(int i, int cp, char peca) {
        if (liniaVertical(i, cp, peca)) {
            try {
                dout1.writeBoolean(true);
                dout2.writeBoolean(true);
                fipartida = true;
                dout2.flush();
                dout1.flush();
            }catch (IOException e){
                System.out.println(e);
            }
                System.out.println("Has fet una linia vertical");
                System.out.println("Has guanyat!");
        } else if (liniaHoritzontal(i, cp, peca)) {
            try {
                dout1.writeBoolean(true);
                dout2.writeBoolean(true);
                fipartida = true;
                dout2.flush();
                dout1.flush();
            }catch (IOException e){
                System.out.println(e);
            }
            System.out.println("Has fet una linia horitzonal");
            System.out.println("Has guanyat!");
        } else if (liniaDiagonalEsquerra(i, cp, peca)) {
            try {
                dout1.writeBoolean(true);
                dout2.writeBoolean(true);
                fipartida = true;
                dout2.flush();
                dout1.flush();
            }catch (IOException e){
                System.out.println(e);
            }
            System.out.println("Has fet una linia diagonal a l'esquerra");
            System.out.println("Has guanyat!");
        } else if (liniaDiagonalDreta(i, cp, peca)) {
            try {
                dout1.writeBoolean(true);
                dout2.writeBoolean(true);
                fipartida = true;
                dout2.flush();
                dout1.flush();
            }catch (IOException e){
                System.out.println(e);
            }
            System.out.println("Has fet una linia diagonal a la dreta");
            System.out.println("Has guanyat!");
        } else {
            try {
                dout1.writeBoolean(false);
                dout2.writeBoolean(false);
                dout2.flush();
                dout1.flush();
            }catch (IOException e){
                System.out.println(e);
            }
        }
    }

    /**
     * Mètode per comprovar la linia vertical per el mètode comprovarLinia
     * @param i Enter que defineix la fila de la peça
     * @param cp Enter que defineix la columna de la peça
     * @param peca Caràcter que identifica el jugador
     * @return True si hi ha linia o False si no
     */
    private static boolean liniaVertical(int i, int cp, char peca) {
        int l = 0;
        while (tbuit[i][cp] == peca) {
            l++;
            i++;
        }
        return l >= 4;
    }
    /**
     * Mètode per comprovar la horitzontal per el mètode comprovarLinia
     * @param i Enter que defineix la fila de la peça
     * @param cp Enter que defineix la columna de la peça
     * @param peca Caràcter que identifica el jugador
     * @return True si hi ha linia o False si no
     */
    private static boolean liniaHoritzontal(int i, int cp, char peca) {
        int l = 0;
        while (tbuit[i][cp] == peca) {
            cp--;
        }
        while (tbuit[i][cp + 1] == peca) {
            l++;
            cp++;
        }
        return l >= 4;
    }
    /**
     * Mètode per comprovar la linia diagonal esquerra per el mètode comprovarLinia
     * @param i Enter que defineix la fila de la peça
     * @param cp Enter que defineix la columna de la peça
     * @param peca Caràcter que identifica el jugador
     * @return True si hi ha linia o False si no
     */
    private static boolean liniaDiagonalEsquerra(int i, int cp, char peca) {
        int l = 0;
        while (tbuit[i][cp] == peca) {
            i--;
            cp--;
        }
        while (tbuit[i + 1][cp + 1] == peca) {
            l++;
            cp++;
            i++;
        }
        return l >= 4;
    }
    /**
     * Mètode per comprovar la linia diagonal dret per el mètode comprovarLinia
     * @param i Enter que defineix la fila de la peça
     * @param cp Enter que defineix la columna de la peça
     * @param peca Caràcter que identifica el jugador
     * @return True si hi ha linia o False si no
     */
    private static boolean liniaDiagonalDreta(int i, int cp, char peca) {
        int l = 0;
        while (tbuit[i][cp] == peca) {
            i--;
            cp++;
        }
        while (tbuit[i + 1][cp - 1] == peca) {
            l++;
            cp--;
            i++;
        }
        return l >= 4;
    }

    /**
     * Mètode per finalitzar el joc
     */
    private static void finalitzarjoc(){
        try{
            ss.close();
        } catch(IOException e){
            System.out.println(e);
        }
    }
}
