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
		try{
		x.sck.close();}catch(IOException ex) { System.err.println(ex);}
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
		new listener(this).start();	//start listening to this member
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
			//dos.writeUTF("Your name?");
			temp=dis.readUTF();
		}while(server.searchMember(temp)==true);
		m.name=temp;	//set nickname

		new announcer(temp + " joined.").start();

		do
		{
			temp=dis.readUTF(); //get msg from client
			if(!isCommand(temp,dos))
			{
				new announcer(m,temp).start();		//announce to all		
			}
		}while(!temp.equalsIgnoreCase("bye"));

		server.removeMember(m);
		new announcer(m.name + " left group chat.").start();

		}catch(IOException ex){System.out.println(ex);}
	}

	boolean isCommand(String msg, DataOutputStream dos) throws IOException
	{//return False if invalid command
		String part[]=msg.split(" ");
		String temp="";
		switch(part[0])
		{
		case "members":
			{
				int t=server.group.size();
				for(int i=0;i<t;i++)
					temp=temp + "<" + server.group.get(i).name + ">  ";
				temp=temp + "\nTotal Members = " + t;
				break;
			}
		case "kick":
			{
				temp="There's nobody called " + part[1];
				for(int i=0;i<server.group.size();i++)
					if(part[1].equals(server.group.get(i).name))
						{
							server.removeMember(server.group.get(i));
							new announcer(m.name + " removed " + part[1]).start();
							break;
						}
				break;
			}
		default:
			return false;
		}//switch
		
		dos.writeUTF(temp);
		return true;
	}//fun

}

class announcer extends Thread
{
	String message;
	member senderMember;
	announcer(member x,String msg)
	{
		senderMember=x;
		message= x.name + " : " + msg ;
	}
	announcer(String notification)
	{
		message = "[" + notification + "]";
	}
	public void run()
	{
		for(int i=0;i<server.group.size();i++)
			if(server.group.get(i)!=senderMember)
				send(server.group.get(i).sck);
	}
	void send(Socket sck)//sends message to membr sck
	{
		try{
		DataOutputStream dos=new DataOutputStream(sck.getOutputStream());
		dos.writeUTF(message);
		}catch(IOException ex){System.out.println(ex);}
	}
}
