import java.net.*;
import java.io.*;

class server
{
server()
{
	Socket s;
	int count,clcnt;
	try
	{
	  	ServerSocket srv=new ServerSocket(1005);
		count=0;
		while(count<10)
		{
			System.out.println("ONLINE");
			s=srv.accept();
			clcnt =Thread.activeCount();
			System.out.println("Currently Serving " + clcnt + " clients.");
			System.out.println("Total request accepted = " + ++count );
			System.out.println("Got a request from ");
			new processor(s);	//It will serve him
		}
		System.out.println("I am away for routine check-up!");
		srv.close();
	}
	catch(IOException ex) { System.out.println(ex);}
}

public static void main(String args[])
	{
	  new server();
	}
}//server

class processor extends Thread
{
	Socket s;
	processor(Socket t)
	{
		s=t; //this socket corresponds to client, serve it
		start();
		System.out.println("Just now I allocated one thread to serve this request. Bye");
	}
public void run()
{
	try
	{
		DataInputStream dis=new DataInputStream(s.getInputStream());
		DataOutputStream dos=new DataOutputStream(s.getOutputStream());
		String temp;
		do
		{
			temp=dis.readUTF();
//			System.out.println("Client: " + temp);
			dos.writeUTF(temp.toUpperCase());
		}while(!temp.equalsIgnoreCase("bye"));
		System.out.println("Somebody left.");
		s.close();
	}
	catch(IOException ex)
	{System.out.println(ex);}
}
}
