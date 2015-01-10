import java.net.*;
import java.io.*;
import java.util.LinkedList;

class server
{
	public static LinkedList<member> group;

	public static void main(String args[])
	{
	  new server(1025);
	}

	server(int port)
	{
		Socket s;
		group=new LinkedList<member>();
		try
		{
		  	ServerSocket srv=new ServerSocket(port);
			int count=0;
			while(count<10)
			{
				System.out.println("ONLINE");
				s=srv.accept();
				System.out.println("Connected : " + s);
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
		System.out.println("Members : " + group.size());
	}
}//server

class member
{
	String name;
	Socket sck;
	member(Socket s)
	{
		sck=s; 
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
			temp=dis.readUTF();
			System.out.println(m.name + " :" + temp);
			if(temp.equals("ping"))
			{
				dos.writeUTF(server.group.size() + " peoples here");
			}
			new announcer(m,temp).start();
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
