import java.io.*;
import java.net.*;

class client
{
	Socket s;

	public static void main(String args[])
	{
		try
		{
			int port=Integer.parseInt(args[1]);
			System.out.println("Console Group chat https://github.com/TheniL/group-chat");
			new client(args[0],port);
		}catch(ArrayIndexOutOfBoundsException e)
		{
			System.out.println("java client <Server-IP> <Server-Port>");
		}
	}

	client(String srv,int prt)
	{
		try
		{
			s=new Socket(srv,prt);
			System.out.println("Connected.");
			DataInputStream dis=new DataInputStream(s.getInputStream());
			DataOutputStream dos=new DataOutputStream(s.getOutputStream());
			Thread t1,t2;
			t1=new Thread(new sender(dos),"send");
			t2=new Thread(new receiver(dis),"receive");
			t2.start();
			t1.start();
		}catch(IOException ex)
		{ System.out.println(ex);
		}
	}

}

class sender extends Thread
{
	DataOutputStream dos;
	sender(DataOutputStream d)
	{
		dos=d;
	}
	public void run()
	{
		send();
	}
	void send()
	{
		String temp;
		try  //Get String again
		{
			do
			{
				System.out.print("you : ");
				temp=getString();
				dos.writeUTF(temp);
			}while(!temp.equalsIgnoreCase("bye"));
		}catch(IOException ex) { System.out.println(ex);}
	}

	static String getString()
	 {
		String s;
		try{
			BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
			s = bufferRead.readLine();
			//int count = 1; 
			//System.out.print(String.format("\033[%dA",count)); // Move up
			//System.out.print("\033[2K"); // Erase line content
		}	
		catch(IOException e)
		{e.printStackTrace(); s="Error";}
		return s;
	}

}

class receiver extends Thread
{
	DataInputStream dis;
	receiver(DataInputStream d)
	{
		dis=d;
	}
	public void run()
	{
		receive();
	}
	void receive()
	{
		String temp;
		try
		{
			while(true)
			{
				temp=dis.readUTF();
				System.out.print("\r");
				System.out.println(temp);
				System.out.print("you : ");
			}
		}catch(IOException ex) { System.err.println(ex);}
	}
}
