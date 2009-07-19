package pipe.main;

public class Snippet {

	public static void snippet(Program program) {
		

		// what should the api be for sockets?
		
		//send
		// you have a domain or ip and port, and want to make a new socket connection
		// you set the time you're willing to wait
		// when it connects, you'll chuck it into a flow chain
		
//		Socket socket = new Socket(update, new Address("site.com:1234"), 10 * Time.second);
		// it starts trying to connect
		// within 10 seconds, your update will get it and it'll be done
		// this is actually all a fancy connect task
		// do it from an ephemeral port, that's good in this case
		
		//basically, you just need to improve ConnectTask by adding the following things:
		//takes a Site, not an IpPort
		//times out in 4 seconds
		//call this object Connect
		
		
		

		//receive
		// you want to look at a new socket when it connects
		// you want to know when it connected
		// you want to see the data it sent
		// you then either grab it or discard it
		
		//call this object Accept
		
		
		//have Packets take a Site
		
		
		
		/*
		//Timeout
		//what if you had a timeout object, an egg timer
		
		egg = new Egg(receive, 4 * Time.second);
		
		close(egg); // you have to close it
		
		// and then in receive
		
		egg.check(); // this throws TimeException
		*/
		
		

	}
}
