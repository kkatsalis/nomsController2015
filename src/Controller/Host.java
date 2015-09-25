/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Statistics.HostStats;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kostas
 */
public class Host {

    int hostID;
    Configuration _config;
    String _nodeName;
    
    List<VM> _VMs;    // A list with VMs per Host Machine;
    
    List<HostStats> _hostStats; // A number of measurements is taken for every Host per Slot
    
    public Host(int hostId,Configuration config,String nodeName) {
        
        this.hostID=hostId;
        this._config=config;
        this._nodeName=nodeName;
        
        this._VMs=new ArrayList<>();     // A list with VMs per Host Machine;
        this._hostStats=new ArrayList<>();
    }

    public String getNodeName() {
        return _nodeName;
    }

    public List<HostStats> getHostStats() {
        return _hostStats;
    }

    public List<VM> getVMs() {
        return _VMs;
    }

    public int getHostID() {
        return hostID;
    }

    
    
    
}
