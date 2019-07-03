package main;
import java.util.*;
import java.io.*;
import java.net.Socket;

public class Controler {
	private static Socket clientSocket; 
    private static BufferedReader reader; 
    private static BufferedReader in; 
    private static BufferedWriter out; 
	private static void saveOnServer(Doc d){
		try {
            try {
            
                clientSocket = new Socket("localhost", 4004);
                
                reader = new BufferedReader(new InputStreamReader(System.in));
            
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                String word = reader.readLine();
                word = "save";
                out.write(word + "\n"); 
                out.flush();

                String ans = in.readLine(); 
 				System.out.println(ans); 
                
                	out.write(d.Name + "\n");
                	out.write(d.Name + "\n");
                	out.flush();
                	ans = in.readLine();
                	System.out.println(ans);
                
            } finally { 
                System.out.println("Client closed...");
                clientSocket.close();
                in.close();
                out.close();
            }
        } catch (IOException e) {
            System.err.println(e);
        }

	}
	private static void findOnServer(String name){
		try {
            try {
			clientSocket = new Socket("localhost", 8080);

			reader = new BufferedReader(new InputStreamReader(System.in));
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	           
	        out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

	        out.write(name);
	        String word = in.readLine();
	        Doc d = new Doc();
	    }
	        finally { 
                System.out.println("Клиент был закрыт...");
                clientSocket.close();
                in.close();
                out.close();
            }
        } catch (IOException e) {
            System.err.println(e);
        }

	}

 	public static void main(String[] args) {
 		Scanner in = new Scanner(System.in);
 		while(true){
	    	System.out.println("\n\n-------------------------------------------\nEnter the option: \n 1. Save document\n 2. Find document \n 3. Delete document \n 4. Close programm");
	    	String option = in.next();
	    	switch (option){
	    		case "1":{
	    			Doc ND = new Doc();
	    			saveOnServer(ND);
	    			break;
	    		}
	    		case "2":{
	    			System.out.println("Enter name of document");
	    			String name = in.next();
	    			findOnServer(name);
	    			break;
	    		}
	    		case "3":{
	    	/*		System.out.println("Enter name of document");
	    			String name = in.next();
	    			boolean flag = base.delDoc(name);
	    			
	    			break;*/
	    		}
	    		case "4":{
	    			return;
	    		}
	    		default :{
	    			System.out.println("Please, enter correct nomer of option");
	    		}
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
		Scanner in = new Scanner(System.in);
		System.out.println("Enter name of document");
		this.Name = in.next();
		System.out.println("Enter document location");
		String dir = in.next();
		dir = dir + "/" + Name + ".txt";
		String str = "";
		 try(FileReader reader = new FileReader(dir))
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

	public void readDoc(){
		System.out.println(Text);
	}

}