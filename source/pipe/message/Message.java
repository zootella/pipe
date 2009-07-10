package pipe.message;

import base.data.Bay;
import base.data.Data;
import base.data.Outline;
import base.encode.Hash;
import base.exception.ChopException;
import base.exception.MessageException;
import base.internet.name.IpPort;

public class Message {
	
	// "i" Please tell me what my IP address and port number is.
	
	public static Data ipRequest() {
		return sign((new Outline("i")).toData());
	}
	
	public static boolean isIpRequest(Data data) {
		try {
			Data message = check(data);
			Outline o = new Outline(message);
			return o.name.equals("i");
		}
		catch (ChopException e) { return false; }
		catch (MessageException e) { return false; }
	}
	
	// "p" This is what your IP address and port number looks like to me.
	
	public static Data ipResponse(IpPort ipPort) {
		return sign((new Outline("p", ipPort.data())).toData());
	}
	
	public static IpPort isIpResponse(Data data) {
		try {
			Data message = check(data);
			Outline o = new Outline(message);
			if (!o.name.equals("p")) return null;
			return new IpPort(o.getData());
		}
		catch (ChopException e) { return null; }
		catch (MessageException e) { return null; }
	}
	
	// Inside
	
	/**
	 * @param data Starts with a SHA1 hash of the message afterwards
	 * @return     The message afterwards if the hash is good
	 * @throws     ChopException or MessageException
	 */
	private static Data check(Data data) {
		Data hash = data.start(Hash.size);
		Data message = data.after(Hash.size);
		if (!hash.equals(Hash.hash(message))) throw new MessageException("bad hash");
		return message;
	}
	
	/** Put the hash of data at the start of it. */
	private static Data sign(Data data) {
		Bay bay = new Bay();
		bay.add(Hash.hash(data));
		bay.add(data);
		return bay.data();
	}
}
