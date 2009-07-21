package base.process;

/** I'm making a note here: HUGE SUCCESS. */
public class Alive {
	
	/** Keep Java from exiting the process so Update objects can get future Swing events. */
	public static void still() {
		
		Runnable r = new Runnable() {
            public void run() {
                Object o = new Object();
                try {
                    synchronized (o) {
                        o.wait();
                    }
                } catch (InterruptedException e) { Mistake.ignore(e); }
            }
        };
        
        Thread t = new Thread(r, "still alive");
        t.setDaemon(false);
        t.start();        			
	}
}
