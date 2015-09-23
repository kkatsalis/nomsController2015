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

    Configuration _config;
    String _nodeName;
    
    List<VM> _activeVMs;    // A list with VMs per Host Machine;
    List<VM> _closedVMs;    // A list with VMs per Host Machine;
    
    List<HostStats> _hostStats;
    
    public Host(Configuration config,String nodeName) {
        
        this._config=config;
        this._nodeName=nodeName;
        
        this._activeVMs=new ArrayList<>();     // A list with VMs per Host Machine;
        this._closedVMs=new ArrayList<>();
        this._hostStats=new ArrayList<>();
    }

    public String getNodeName() {
        return _nodeName;
    }

    
}
