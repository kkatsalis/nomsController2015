/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Statistics.ABStats;
import Statistics.NetRateStats;
import Statistics.VLCStats;
import java.util.List;

/**
 *
 * @author kostas
 */
public class WebClient {
    
    Configuration _config;
    String _nodeName;
    
    ABStats abStats;
    VLCStats vlcStats;
    NetRateStats netRateStats;
    
    Host[] _hosts;
    
    public WebClient(Configuration config, String nodeName, Host[] hosts) {
        
        this._config=config;
        this._nodeName=nodeName;
        this._hosts=hosts;
        abStats=new ABStats();
        vlcStats=new VLCStats();
        netRateStats=new NetRateStats();
    }

    public String getNodeName() {
        return _nodeName;
    }

    public ABStats getAbStats() {
        return abStats;
    }

    public VLCStats getVlcStats() {
        return vlcStats;
    }

    public NetRateStats getNetRateStats() {
        return netRateStats;
    }
    
    
    
}
