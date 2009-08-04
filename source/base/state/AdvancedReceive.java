package base.state;

public interface AdvancedReceive {

	public boolean outClosed();
	
	public void receive() throws Exception;
}
