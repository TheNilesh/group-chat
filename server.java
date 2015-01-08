import java.net.*;
import java.io.*;

class server
{
server()
 {
  //create Server
	try
	{
	ServerSocket srv=new ServerSocket(1005);
	Socket s=srv.accept();
	DataInputStream dis=new DataInputStream(s.getInputStream());
	DataOutputStream dos=new DataOutputStream(s.getOutputStream());
	String temp=dis.readUTF();	
	dos.writeUTF("OK");
	System.out.println("Client: " + temp);
	}
	catch(IOException ex)
	{System.out.println(ex);}
 }
public static void main(String args[])
	{
	  new server();
	}
}
