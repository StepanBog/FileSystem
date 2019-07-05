package client;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Controler {
    private static Socket clientSocket;
    private static BufferedReader reader;
    private static BufferedReader in;
    private static BufferedWriter out;

    private static void exit(){
        try {
            String word = "exit";
            out.write(word + "\n");
            out.flush();
            clientSocket.close();
            in.close();
            out.close();
        }catch (IOException e) {
            System.err.println(e);
        }
    }
    private static boolean saveOnServer(Doc d){
        try {
            try {
              //  String word = reader.readLine();
                String word = "save";
                out.write(word + "\n");
                out.flush();

                String ans = in.readLine();
                if (ans.equals("ready")) {
                    System.out.println("Server ready to message");
                    out.write(d.Name + "\n");
                    out.flush();
                    out.write(d.Text + "\n");
                    out.flush();
                    System.out.println("Message sent");
                    ans = in.readLine();
                    if (ans.equals("complited")) {
                        System.out.println("Message received");
                        return true;
                    }

                }
                return false;
            } finally {

            }
        } catch (IOException e) {
            System.err.println(e);
            return false;
        }
    }
    private static boolean delFromServer(String name){
        try {
            try {
                out.write("delete" + "\n");
                //out.write(name);
                out.flush();
                String ans = in.readLine();
                if (ans.equals("ready")){
                    System.out.println("Server ready to message");
                    out.write(name + "\n");
                    out.flush();
                    System.out.println("Message sent");
                    ans = in.readLine();
                    System.out.println((ans));
                    return true;

                }
                return false;
            }
            finally {

            }
        } catch (IOException e) {
            System.err.println(e);
            return false;
        }

    }
    private static boolean findOnServer(String name) {
        try {
            try {
                out.write("find" + "\n");
                //out.write(name);
                out.flush();
                String ans = in.readLine();
                if (ans.equals("ready")){
                    System.out.println("Server ready to message");
                    out.write(name + "\n" + "");
                    out.flush();
                    System.out.println("Message sent");
                    ans = in.readLine();
                    if (ans.equals("Success")) {
                        System.out.println((ans));
                        try{
                            Doc d = new Doc(name,in.readLine());
                            System.out.println(d.Text);
                            return true;

                        }catch (IOException e) {
                            System.err.println(e);
                            return false;
                        }
                    }
                    else {
                        try{
                            System.out.println(in.readLine());
                        }catch (IOException e) {
                            System.err.println(e);
                            return false;
                        }
                    }
                }
                return true;
            }
            finally {

            }
        } catch (IOException e) {
            System.err.println(e);
            return false;
        }
    }
    protected static List<String> parseList(String str){
        List<String> list = new ArrayList<String>();
        String s = "",s1;
        int i = 0;

        while (str.length() != i){
            s1 = Character.toString(str.charAt(i));
            if (s1.equals("|")) {
                list.add(s);
                s = "";
            } else {
                s += s1;
            }
        i++;
        }
        list.add(s);
        return list;
    }
    protected static boolean findAll(){
        try {
            out.write("findAll\n");
            out.flush();
            String str = in.readLine();
            List<String> list = parseList(str);
            for (String i : list){
                System.out.println(i);
            }
            return true;
        } catch (IOException e) {
            System.err.println(e);
            return false;
        }
    }
    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);
        while(true){
                do {
                    try {
                        clientSocket = null;
                        clientSocket = new Socket("localhost", 8000);
                    } catch (IOException ex) {
                        System.out.println("Server not available ");
                        //System.out.println(ex.getMessage());
                        try {
                            TimeUnit.SECONDS.sleep(2);
                        } catch (InterruptedException e) {
                            // e.printStackTrace();
                            System.out.println(ex.getMessage());
                        }

                    }
                }
                while (clientSocket == null);
                try {
                    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
                boolean second_flag = true;
                while (second_flag) {
                    System.out.println("\n\n-------------------------------------------" +
                            "\nEnter the option: " +
                            "\n 1. Save document" +
                            "\n 2. Delete document" +
                            " \n 3. Find document " +
                            "\n 4. Show list of documents " +
                            "\n 5. Close programm");
                    String option = reader.next();
                    boolean flag;
                    switch (option) {
                        case "1": {
                            Doc ND = new Doc();
                            flag = saveOnServer(ND);
                            if (!flag)
                                second_flag = !second_flag;
                            break;
                        }
                        case "2": {
                            System.out.println("Enter name of document");
                            String name = reader.next();
                            flag = delFromServer(name);
                            if (!flag)
                                second_flag = !second_flag;
                            break;
                        }
                        case "3": {
                            System.out.println("Enter name of document");
                            String name = reader.next();
                            flag = findOnServer(name);
                            if (!flag)
                                second_flag = !second_flag;
                            break;
                        }
                        case "4": {
                            flag = findAll();
                            if (!flag)
                                second_flag = !second_flag;
                            break;
                        }
                        case "5": {
                            exit();
                            return;
                        }
                        default: {
                            System.out.println("Please, enter correct nomer of option");
                        }
                    }
                }
                try {
                    clientSocket.close();
                    in.close();
                    out.close();
                }catch (IOException e) {
                    System.err.println(e);
                }
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
        String str;
        while(true) {
            Scanner in = new Scanner(System.in);
            System.out.println("Enter name of document");
            this.Name = in.next();
            System.out.println("Enter document location");
            String dir = in.next();
            dir = dir + "/" + Name + ".txt";str = "";
            try (FileReader reader = new FileReader(dir)) {
                int c;
                while ((c = reader.read()) != -1) {
                    str += Character.toString((char) c);
                }
                break;
            } catch (IOException ex) {

                System.out.println("Wrong name or location");
            }
        }
        this.Text = str;
        System.out.println("Text entered");
    }

}