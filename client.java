import java.net.*;
import java.io.*;

class client
{
client()
{
	//send msg to server
	try{
	Socket s=new Socket("127.0.0.1",1005);
	DataInputStream dis=new DataInputStream(s.getInputStream());
	DataOutputStream dos=new DataOutputStream(s.getOutputStream());
	dos.writeUTF("Who are You ?");
	System.out.println("Server says: " + dis.readUTF());
	}catch(IOException ex) { System.out.println(ex);}
}
public static void main(String args[])
{
	new client();
}
}
