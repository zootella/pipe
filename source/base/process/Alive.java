package base.process;

/** I'm making a note here: HUGE SUCCESS. */
public class Alive {
	
	public static void still() {
		
		Runnable r = new Runnable() {
            public void run() {
                Object o = new Object();
                try {
                    synchronized (o) {
                        o.wait();
                    }
                } catch (InterruptedException ie) {}
            }
        };
        
        Thread t = new Thread(r, "still alive");
        t.setDaemon(false);
        t.start();        			
	}
}
