package org.zootella.upnp;

// TODO move to net
/**
 * Configuration for UPnPManager
 */
public interface UPnPManagerConfiguration {
    boolean isEnabled();
    void setEnabled(boolean enabled);
    String getClientID();
}
