package base.state;

import java.util.HashSet;
import java.util.Set;

import javax.swing.SwingUtilities;

import base.process.Mistake;

public class AdvancedUpdate {
	
	public AdvancedUpdate(AdvancedReceive receive) {
		list = new HashSet<AdvancedReceive>();
		list.add(receive);
		spin = new Spin();
		send();
	}
	
	public void add(AdvancedReceive receive) {
		list.add(receive);
	}
	
	private final Set<AdvancedReceive> list;
	private final Spin spin;
	private boolean set;
	
	public void send() {
		if (set) return;
		set = true;
		SwingUtilities.invokeLater(new MyRunnable());
	}

	private class MyRunnable implements Runnable {
		public void run() {
			try {
				set = false;
				spin.count();
				
				for (AdvancedReceive r : new HashSet<AdvancedReceive>(list)) {
					if (r.outClosed())
						list.remove(r);
					else
						r.receive();
				}
			} catch (Exception e) { Mistake.stop(e); }
		}
	}
}
