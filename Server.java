
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
    private static String dir = "C:/Gradle/FileEditor/src/base/user1/";

    public static void safeDoc(Doc d){
			try(FileWriter writer = new FileWriter(dir + d.Name +".txt", false))
        {
           // запись всей строки
            writer.write(d.Text);
            writer.flush();
        }
        catch(IOException ex){
             
            System.out.println(ex.getMessage());
        } 
	}

	public static boolean delDoc(String name){
		File f = new File (dir);
		String[] mass = f.list();
		for (int i = 0 ; i < mass.length; i++)
			if ((name + ".txt").equals(mass[i])){
				File f1 = new File (dir + name + ".txt");
				f1.delete();
				return true;
			}
		return false;
	}

	public Doc findDoc(String name){
		return null;
	//	return "";
	}

 	public static void main(String[] args) {
 		//Scanner in = new Scanner(System.in);
 		try {
            try  {
                server = new ServerSocket(4004); 
                System.out.println("Server stated!"); 
                clientSocket = server.accept(); 
                
                try { 
                	reader = new BufferedReader(new InputStreamReader(System.in));
                    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    // и отправлять
                    out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                    String word = in.readLine(); 
                  	System.out.println(word);
                    if (word.equals("save")){
                    	
                    	word = reader.readLine();
                    	word = "ready"; 
                    	out.write(word + "\n");
                    	
                    	String name = in.readLine();
 						System.out.println(name);
                    	String text = in.readLine();
                    	System.out.println(text);
                    	word = "complited";
                    	out.write(word + "\n");
                    	Doc d = new Doc(name,text);
                    	safeDoc(d);
                    	
                    }
                   
                  //  out.write("Hi : " + word + "\n");
                    out.flush();

                } finally { 
                    System.out.println("dfjkhgkdf");
                    clientSocket.close();
                
                    in.close();
                    out.close();
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

}