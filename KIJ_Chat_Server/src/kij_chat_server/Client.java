package kij_chat_server;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import javax.crypto.BadPaddingException;

/** original ->http://www.dreamincode.net/forums/topic/262304-simple-client-and-server-chat-program/
 * 
 * @author santen-suru
 */


public class Client implements Runnable{

	private Socket socket;//SOCKET INSTANCE VARIABLE
        private String username;
        private boolean login = false;
        
        private ArrayList<Pair<Socket,String>> _loginlist;
        private ArrayList<Pair<String,String>> _userlist;
        private ArrayList<Pair<String,String>> _grouplist;
	
	public Client(Socket s, ArrayList<Pair<Socket,String>> _loginlist, ArrayList<Pair<String,String>> _userlist, ArrayList<Pair<String,String>> _grouplist)
	{
		socket = s;//INSTANTIATE THE SOCKET)
                this._loginlist = _loginlist;
                this._userlist = _userlist;
                this._grouplist = _grouplist;
	}
	
	@Override
	public void run() //(IMPLEMENTED FROM THE RUNNABLE INTERFACE)
	{
		try //HAVE TO HAVE THIS FOR THE in AND out VARIABLES
		{
			Scanner in = new Scanner(socket.getInputStream());//GET THE SOCKETS INPUT STREAM (THE STREAM THAT YOU WILL GET WHAT THEY TYPE FROM)
			PrintWriter out = new PrintWriter(socket.getOutputStream());//GET THE SOCKETS OUTPUT STREAM (THE STREAM YOU WILL SEND INFORMATION TO THEM FROM)
			
			while (true)//WHILE THE PROGRAM IS RUNNING
			{		
				if (in.hasNext())
				{
					String input = in.nextLine();//IF THERE IS INPUT THEN MAKE A NEW VARIABLE input AND READ WHAT THEY TYPED
//					System.out.println("Client Said: " + input);//PRINT IT OUT TO THE SCREEN
//					out.println("You Said: " + input);//RESEND IT TO THE CLIENT
//					out.flush();//FLUSH THE STREAM
                                        
                                        // param LOGIN <userName> <pass>
                                        /*if (input.split(" ")[0].toLowerCase().equals("login") == true) {
                                            String[] vals = input.split(" ");
                                            
                                            Pair paired = new Pair(vals[1], vals[2]);
                                            String user = (String) paired.getFirst();
                                            String pass = (String) paired.getSecond();
                                            String key = user;
                                            String keys = StringUtils.padRight(key, 16);
                                            String cipher = AES.decrypt(pass, keys);
                                            
                                            if (this._userlist.contains(new Pair(user, cipher)) == true) {
                                                if (this.login == false) {
                                                    this._loginlist.add(new Pair(this.socket, user));
                                                    this.username = user;
                                                    this.login = true;
                                                    System.out.println("Users count: " + this._loginlist.size());
                                                    out.println("SUCCESS login");
                                                    out.flush();
                                                } else {
                                                    out.println("FAIL login");
                                                    out.flush();
                                                }
                                            } else {
                                                out.println("FAIL login");
                                                out.flush();
                                            }
                                        }*/
                                        
                                        // param enrcypted LOGIN <userName> <pass>
                                        if (input.split(" ")[0].toLowerCase().equals("login") == true) {
                                            String[] vals = input.split(" ");
                                            
                                            Pair paired = new Pair(vals[1], vals[2]);
                                            String cipheruser = (String) paired.getFirst();
                                            String cipherpass = (String) paired.getSecond();
                                            
                                            boolean exist = false;
                                            
                                            for(Pair<String, String> cur : _userlist) {
                                                String user = (String) cur.getFirst();
                                                String pass = (String) cur.getSecond();
                                                String key = user;
                                                String keys = StringUtils.padRight(key, 16);
                                                
                                                try 
                                                {
                                                    String plainuser = AES.decrypt(cipheruser, keys, keys);
                                                    String plainpass = AES.decrypt(cipherpass, keys, keys);
                                                    
                                                    if(this.login == false)
                                                    {
                                                        if (user.equals(plainuser) && pass.equals(plainpass)) {
                                                            this._loginlist.add(new Pair(this.socket, user));
                                                            this.username = user;
                                                            this.login = true;
                                                            exist = true;
                                                            System.out.println("Users count: " + this._loginlist.size());
                                                            out.println("SUCCESS login");
                                                            out.flush();
                                                        }
                                                    }
                                                    
                                                }
                                                catch(BadPaddingException e)
                                                {
                                                    continue;
                                                }
                                            }
                                            
                                            if (exist == false){
                                                out.println("FAIL login");
                                                out.flush();
                                            }
                                        }
                                        
                                        // param LOGOUT
                                        if (input.split(" ")[0].toLowerCase().equals("logout") == true) {
                                            String[] vals = input.split(" ");
                                            
                                            if (this._loginlist.contains(new Pair(this.socket, this.username)) == true) {
                                                this._loginlist.remove(new Pair(this.socket, this.username));
                                                System.out.println(this._loginlist.size());
                                                out.println("SUCCESS logout");
                                                out.flush();
                                                this.socket.close();
                                                break;
                                            } else {
                                                out.println("FAIL logout");
                                                out.flush();
                                            }
                                        }
                                        
                                        // param PM <userName dst> <message>                                        
                                        if (input.split(" ")[0].toLowerCase().equals("pm") == true) {
                                            String[] vals = input.split(" ",4);
                                            boolean exist = false;
                                            
                                            for(Pair<Socket, String> cur : _loginlist) {
                                               String user = (String) cur.getSecond();
                                               String key = this.username+user;
                                               String uname_tujuan = RC4.decrypt(vals[2],key);
                                                if (cur.getSecond().equals(uname_tujuan)) {
                                                    PrintWriter outDest = new PrintWriter(cur.getFirst().getOutputStream());                                                        
                                                    //System.out.println("pm " + this.username + " " + uname_tujuan + " " + vals[3]);
                                                    String send = "pm " + this.username + " " + uname_tujuan + " " + vals[3];
                                                    outDest.println(send);
                                                    outDest.flush();
                                                    exist = true;
                                                }
                                                
                                           
                                            }
                                            
                                            if (exist == false) {
                                                System.out.println("pm failed.");
                                                out.println("FAIL pm");
                                                out.flush();
                                            }
                                        }
                                        
                                        // param CG <groupName>
                                        if (input.split(" ")[0].toLowerCase().equals("cg") == true) {
                                            String[] vals = input.split(" ");
                                            
                                            String cipherNamaGrup = vals[2];
                                            String Key = StringUtils.padRight(this.username, 16);
                                            String namaGrup = AES.decrypt(cipherNamaGrup, Key, Key);
                                            boolean exist = false;
                                            
                                            for(Pair<String, String> selGroup : _grouplist) {
                                                if (selGroup.getFirst().equals(namaGrup)) {
                                                    exist = true;
                                                }
                                            }
                                            
                                            if(exist == false) {
                                                Group group = new Group();
                                                int total = group.updateGroup(namaGrup, this.username, _grouplist);
                                                System.out.println("total group: " + total);
                                                System.out.println("cg " + namaGrup + " by " + this.username + " successed.");
                                                out.println("SUCCESS cg");
                                                out.flush();
                                            } else {
                                                System.out.println("cg " + namaGrup + " by " + this.username + " failed.");
                                                out.println("FAIL cg");
                                                out.flush();
                                            }
                                        }
                                        
                                        // param GM <groupName> <message>
                                        if (input.split(" ")[0].toLowerCase().equals("gm") == true) {
                                            String[] vals = input.split(" ");                                            
                                            boolean exist = false;
                                            for(Pair<String, String> selGroup : _grouplist) {
                                                if (selGroup.getSecond().equals(this.username)) {
                                                    exist = true;                                                    
                                                }
                                            }
                                            
                                            if (exist == true) {
                                                for(Pair<String, String> selGroup : _grouplist) {
                                                    if (selGroup.getFirst().equals(vals[1])) {
                                                        for(Pair<Socket, String> cur : _loginlist) {
                                                            if (cur.getSecond().equals(selGroup.getSecond()) && !cur.getFirst().equals(socket)) {
                                                                PrintWriter outDest = new PrintWriter(cur.getFirst().getOutputStream());
                                                                String messageOut = "";
                                                                for (int j = 2; j<vals.length; j++) {
                                                                    messageOut += vals[j] + " ";
                                                                }      
                                                               
                                                                //System.out.println(this.username + " to " + vals[1] + " group: " + messageOut);
                                                                outDest.println("gm " + this.username + " @ " + vals[1] + " group: " + messageOut);
                                                                outDest.flush();
                                                            }
                                                        }
                                                    }
                                                }
                                            } else {
                                                System.out.println("gm to " + vals[1] + " by " + this.username + " failed.");
                                                out.println("FAIL gm");
                                                out.flush();
                                            }
                                        }
                                        
                                        // param BM <message>
                                        if (input.split(" ")[0].toLowerCase().equals("bm") == true) {
                                            String[] vals = input.split(" ");
                                            
                                            for(Pair<Socket, String> cur : _loginlist) {
                                                if (!cur.getFirst().equals(socket)) {
                                                    PrintWriter outDest = new PrintWriter(cur.getFirst().getOutputStream());
                                                    String messageOut = "";
                                                    for (int j = 1; j<vals.length; j++) {
                                                        messageOut += vals[j] + " ";
                                                    }
                                                    System.out.println(this.username + " to alls: " + messageOut);
                                                    outDest.println(this.username + " <BROADCAST>: " + messageOut);
                                                    outDest.flush();
                                                }
                                            }
                                        }
				}
			}
		} 
		catch (Exception e)
		{
			e.printStackTrace();//MOST LIKELY THERE WONT BE AN ERROR BUT ITS GOOD TO CATCH
		}	
	}

}


