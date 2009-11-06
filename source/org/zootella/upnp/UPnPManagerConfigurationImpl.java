package org.zootella.upnp;

public class UPnPManagerConfigurationImpl implements UPnPManagerConfiguration {
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
    	this.enabled = enabled;
    }

    public String getClientID() {
    	return "0123456789";
    }
    
    
    private boolean enabled = true;
}
