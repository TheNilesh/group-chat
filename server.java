import java.net.*;
import java.io.*;
import java.util.LinkedList;

class server
{
	public static LinkedList<member> group;
	final int MAX_MEMBERS=20;

	public static void main(String args[])
	{
		try
	  	{
			new server(Integer.parseInt(args[0]));
		}
		catch(ArrayIndexOutOfBoundsException ex)
		{
			System.out.println("Chat Server by niLesh\njava server <port-to-use>");
		}
	}//main

	server(int port)
	{
		Socket s;
		group=new LinkedList<member>();
		int memberCount=0;
		try
		{
		  	ServerSocket srv=new ServerSocket(port);
			while(memberCount <= MAX_MEMBERS)
			{
				memberCount=Thread.activeCount()-1;
				System.out.println("ONLINE at port " + port);
				s=srv.accept();
				System.out.println("Connection from: " + s);
				group.add(new member(s));
			}
			srv.close();
		}
		catch(IOException ex) { System.out.println(ex);}
	}

	public static boolean searchMember(String x)
	{
		for(int i=0;i<group.size();i++)
			if(x.equalsIgnoreCase((group.get(i)).name)==true)
			{	return true;}
		return false;
	}
	public static void removeMember(member x)
	{
		group.remove(group.indexOf(x));
	}
}//server

class member
{
	String name;
	Socket sck;
	member(Socket s)
	{
		sck=s;
		name="unnamed";
		new listener(this).start();	//start member to listen to this member
	}
}

class listener extends Thread
{
	member m; 
	listener(member x)
	{
		m=x;
	}

	public void run()
	{
		listen();
	}

	void listen()
	{
	Socket sck=m.sck;
	try{
		DataInputStream dis=new DataInputStream(sck.getInputStream());
		DataOutputStream dos=new DataOutputStream(sck.getOutputStream());
		String temp;
		do
		{
			dos.writeUTF("Your name? (should not be already taken) :");
			temp=dis.readUTF();
		}while(server.searchMember(temp)==true);
		m.name=temp;	//set nickname
		new announcer(temp + " joined.").start();

		do
		{
			temp=dis.readUTF(); //get msg from client
			if(!temp.equals("ping"))
			{
				new announcer(m,temp).start();		//announce to all		
			}
			else
			{
				int t=server.group.size();
				temp="";
				for(int i=0;i<t;i++)
					temp=temp + "<" + server.group.get(i).name + ">  ";
				temp=temp + "\nTotal Members = " + t;
				dos.writeUTF(temp);
			}
		}while(!temp.equalsIgnoreCase("bye"));

		server.removeMember(m);
		m.sck.close();
		new announcer(m.name + " left group chat.").start();

		}catch(IOException ex){System.out.println(ex);}
	}

}

class announcer extends Thread
{
	String message;
	announcer(member x,String msg)
	{
		message= x.name + " : " + msg ;
	}
	announcer(String notif)
	{
		message = "[" + notif + "]";
	}
	public void run()
	{
		for(int i=0;i<server.group.size();i++)
			send(server.group.get(i).sck);
	}
	void send(Socket sck)//sends message to membr
	{
		try{
		DataOutputStream dos=new DataOutputStream(sck.getOutputStream());
		dos.writeUTF(message);
		}catch(IOException ex){System.out.println(ex);}
	}
}
