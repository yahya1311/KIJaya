/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kij_chat_client;

/*import java.net.Socket;*/
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author santen-suru
 */
public class Read implements Runnable {
        
        private Scanner in;//MAKE SOCKET INSTANCE VARIABLE
        String input;
        boolean keepGoing = true;
        ArrayList<String> log;
	
	public Read(Scanner in, ArrayList<String> log)
	{
		this.in = in;
                this.log = log;
	}
    
        @Override
	public void run()//INHERIT THE RUN METHOD FROM THE Runnable INTERFACE
	{
		try
		{
			while (keepGoing)//WHILE THE PROGRAM IS RUNNING
			{						
				if(this.in.hasNext()) {
                                        input = this.in.nextLine();
                                        
                                        if (input.split(" ")[0].toLowerCase().equals("success")) {
                                            System.out.println(input);//PRINT IT OUT
                                            if (input.split(" ")[1].toLowerCase().equals("logout")) {
                                                keepGoing = false;
                                            } else if (input.split(" ")[1].toLowerCase().equals("login")) {
                                                log.clear();
                                                log.add("true");
                                            }
                                        }
                                        else if (input.split(" ")[0].toLowerCase().equals("fail")) {
                                            System.out.println(input);//PRINT IT OUT
                                        }
                                        else if (input.split(" ")[0].toLowerCase().equals("pm")) {
                                            String[] potong = input.split(" ",4);
                                            String uname_asal = potong[1];
                                            String uname_tujuan = potong[2];
                                            String pesan = potong[3];
                                            
                                            String key = uname_asal+uname_tujuan;
                                            
                                            String plainpesan = RC4.decrypt(pesan,key);
                                            
                                            String receive = "from " + uname_asal + " :  " + plainpesan;
                                            
                                            System.out.println(receive);
                                        }
                                        else if (input.split(" ")[0].toLowerCase().equals("gm")) {
                                            String[] hasil = input.split(" ");
                                            String from = hasil[1];
                                            String group = hasil[3];
                                            String pesan = hasil[5];
                                            String semua_pesan = RC4.decrypt(pesan,group);
                                            String receive2 = from + " @ " + group + " group: " + semua_pesan;
                                            System.out.println(receive2);
                                         } 
                                }
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();//MOST LIKELY WONT BE AN ERROR, GOOD PRACTICE TO CATCH THOUGH
		} 
	}
}
