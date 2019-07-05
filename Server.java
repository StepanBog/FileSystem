package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


import java.util.*;

public class Server {
    private static Socket clientSocket; //сокет для общения
    private static ServerSocket server; // серверсокет
    private static BufferedReader in; // поток чтения из сокета
    private static BufferedWriter out; // поток записи в соке
    private static BufferedReader reader;
    public static String dir = "C:/Users/User/IdeaProjects/FileObmen/src/base/user1/";

    public static void safeDoc()
    {
        try{
        System.out.println("Client want save doc");
        String word = "ready";
        out.write(word + "\n");
        out.flush();
        System.out.println("Ready to receive");
        String name = in.readLine();
        System.out.println(name);
        String text = in.readLine();
        System.out.println(text);
        word = "complited";
        out.write(word + "\n");
        out.flush();
        System.out.println("Message received");
        Doc d = new Doc(name, text);
        try(FileWriter writer = new FileWriter(dir + d.Name +".txt", false))
            {
                // запись всей строки
                writer.write(d.Text);
                writer.flush();
            }
        }catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void delDoc(){
        String name = "";
        try{
            System.out.println("Client want del doc");
            String word = "ready";
            out.write(word + "\n");
            out.flush();
            System.out.println("Ready to receive");
            name = in.readLine();

        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }
        File f1 = findDoc(name);
        if (f1 != null){
            try {
                f1.delete();
                out.write("Document deleted" + "\n");
                out.flush();
                return;
            }
            catch(IOException ex){
                System.out.println(ex.getMessage());
            }
        }
        else
            try {
                out.write("Document not found" + "\n");
                out.flush();
                return;
            } catch(IOException ex){
                System.out.println(ex.getMessage());
            }

    }

    public static void findAll(){
        try {
            System.out.println("Client want to list of doc");
            File f = new File(dir);
            String[] mass = f.list();
            String str = "";
            for (String s : mass)
                str = str + s + "|";
            out.write(str + "\n");
            out.flush();
        } catch (IOException ex){

            System.out.println(ex.getMessage());
        }
    }
    public static void find(){
        String name = "";
        try{
            System.out.println("Client want find doc");
            String word = "ready";
            out.write(word + "\n");
            out.flush();
            System.out.println("Ready to receive");
            name = in.readLine();
            File f = findDoc(name);
            String dir1;
            if (f != null){
                dir1 = dir + f.getName();
                String str = "";
                try(FileReader reader = new FileReader(dir1))
                {
                    int c;
                    while((c=reader.read())!=-1){
                        str+=Character.toString((char)c);
                    }
                }
                out.write("Success" + "\n");
                out.write(str + "\n");
                out.flush();
            } else {
                out.write("Unsuccess" + "\n");
                out.write("Document not found" + "\n");
                out.flush();
            }
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }
    }
    public static File findDoc(String name){
        File f = new File (dir);
        String[] mass = f.list();
        for (int i = 0 ; i < mass.length; i++)
            if ((name + ".txt").equals(mass[i])){
                File f1 = new File (dir + name + ".txt");
                return f1;
            }
        return null;
    }

    public static void main(String[] args) {
        try {
            try  {  System.out.println("Server stated!");
                server = new ServerSocket(8000);
                while(true) {
                    clientSocket = server.accept();

                    try {
                        reader = new BufferedReader(new InputStreamReader(System.in));
                        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        // и отправлять
                        out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                        while (true) {
                            System.out.println("Waiting from client");
                            String word = in.readLine();
                            System.out.println(word);
                            if (word.equals("save")) {
                                safeDoc();
                            }
                            if (word.equals("delete")) {
                                delDoc();
                            }
                            if (word.equals("find")) {
                                find();
                            }
                            if (word.equals("findAll")) {
                                findAll();
                            }
                            if (word.equals("exit")) {
                                System.out.println("Connection close");
                                clientSocket.close();
                                in.close();
                                out.close();
                                break;
                            }
                        }
                    } catch (IOException e) {
                        System.out.println("Connection break");
                        clientSocket.close();
                        in.close();
                        out.close();
                    }
                }

            } finally {
                System.out.println("Server closed!");
                server.close();
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}


class Doc{
    public String Name;
    public String Text;

    public Doc(String a,String b){
        this.Name = a;
        this.Text = b;
    }
    public Doc(){
        Scanner in = new Scanner(System.in);
        System.out.println("Enter name of document");
        this.Name = in.next();
        System.out.println("Enter document location");
        String dir1 = in.next();
        dir1 = Server.dir + "/" + Name + ".txt";
        String str = "";
        try(FileReader reader = new FileReader(dir1))
        {
            int c;
            while((c=reader.read())!=-1){
                str+=Character.toString((char)c);
            }
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }
        this.Text = str;
        System.out.println("Text entered");
    }

}