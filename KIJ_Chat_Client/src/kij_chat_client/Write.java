/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kij_chat_client;

import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
/**
 *
 * @author santen-suru
 */
public class Write implements Runnable {
    
        private String account; 
	private Scanner chat;
        private PrintWriter out;
        boolean keepGoing = true;
        ArrayList<String> log;
	
	public Write(Scanner chat, PrintWriter out, ArrayList<String> log)
	{
		this.chat = chat;
                this.out = out;
                this.log = log;
	}
	
	@Override
	public void run()//INHERIT THE RUN METHOD FROM THE Runnable INTERFACE
	{
		try
		{
			while (keepGoing)//WHILE THE PROGRAM IS RUNNING
			{						
                            String input = chat.nextLine();	//SET NEW VARIABLE input TO THE VALUE OF WHAT THE CLIENT TYPED IN
                                if (input.split(" ")[0].toLowerCase().equals("login") == true) {
                                            String[] vals = input.split(" ");
                                            
                                            Pair paired = new Pair(vals[1], vals[2]);
                                            String user = (String) paired.getFirst();
                                            String pass = (String) paired.getSecond();
                                            String key = user;
                                            this.account = user;
                                            //String cat = value.substring(0, 3);
                                            String keys = StringUtils.padRight(key, 16);
                                            String cipheruser = AES.encrypt(user, keys, keys);
                                            String cipherpass = AES.encrypt(pass, keys, keys);
                                            String send = "login " + cipheruser + " " + cipherpass;
                                            input=send;
                                            //System.out.println(send);
                                           
                                } else if (input.split(" ")[0].toLowerCase().equals("gm") == true) {
                                            String[] vals = input.split(" ");
                                            String[] vals2 = input.split(" ",3);
                                            Pair paired = new Pair(vals[1], vals[2]);
                                            String Group = (String) paired.getFirst();
                                            String message = vals2[2];
                                            String ciphercontent = RC4.encrypt(message,Group);                                            
                                            String send = "gm " + Group + " " + ciphercontent;
                                            input=send;
                                            //System.out.println(Group);
                                           
                                }

                               else if( input.split(" ")[0].toLowerCase().equals("cg")==true){
                                            String[] namaGrup = input.split(" ");
                                            String user = namaGrup[1];
                                            String grup = namaGrup[2];
                                            String kunci = StringUtils.padRight(user, 16);
                                            String cipheruser = AES.encrypt(user, kunci, kunci);
                                            String ciphergrup = AES.encrypt(grup, kunci, kunci);
                                            String kirim = "cg "+cipheruser+" "+ciphergrup;
                                            input = kirim;
                                            
                               } else if (input.split(" ")[0].toLowerCase().equals("pm") == true) {
                                            String[] potong = input.split(" ",4);
                                            String uname_asal = potong[1];
                                            String uname_tujuan = potong[2];
                                            String pesan = potong[3];
                                            
                                            //Pair paired = new Pair(vals[1], vals[2]);
                                            //String user = (String) paired.getFirst();
                                            //String message = vals2[2];
                                            
                                            String key = uname_asal+uname_tujuan;
                                            
                                            String cipherasal = RC4.encrypt(uname_asal,key);
                                            String ciphertujuan = RC4.encrypt(uname_tujuan,key);
                                            String cipherpesan = RC4.encrypt(pesan,key);
                                            
                                            String send = "pm " + cipherasal + " " + ciphertujuan + " " + cipherpesan;
                                            input=send;
                                            //System.out.println(send);
                                }
                                 out.println(input);//SEND IT TO THE SERVER
                                 out.flush();//FLUSH THE STREAK 
				
                                
                                if (input.contains("logout")) {
                                    if (log.contains("true"))
                                        keepGoing = false;
                                    
                                }
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();//MOST LIKELY WONT BE AN ERROR, GOOD PRACTICE TO CATCH THOUGH
		} 
	}

}
