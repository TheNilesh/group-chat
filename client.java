import java.net.*;
import java.io.*;

class client
{
client()
{
	//send msg to server
	try
	{
		Socket s=new Socket("127.0.0.1",1005);
		System.out.println("Connection established");
		DataInputStream dis=new DataInputStream(s.getInputStream());
		DataOutputStream dos=new DataOutputStream(s.getOutputStream());
		String temp;
		System.out.println("Chat session created..(bye to exit)");
		do
		{	
			System.out.print("you: ");	
			temp=getString();
			dos.writeUTF(temp);
			System.out.println("Server: " + dis.readUTF());
		}while(!temp.equalsIgnoreCase("bye"));
		s.close();
	}catch(IOException ex) { System.out.println(ex);}
}

public static void main(String args[])
{
	new client();
}

static String getString() throws IOException
 {
   System.in.skip(System.in.available());

   byte arr[] = new byte[512];
   int x;
   x= System.in.read(arr);
   String temp = new String(arr, 0,x-1);
   return temp;
 }

}
