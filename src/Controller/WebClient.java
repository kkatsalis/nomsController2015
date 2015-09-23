/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Statistics.ABStats;
import Statistics.NetRateStats;
import Statistics.VLCStats;

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

    public WebClient(Configuration config, String nodeName) {
        
        this._config=config;
        this._nodeName=nodeName;
        
        abStats=new ABStats();
        vlcStats=new VLCStats();
        netRateStats=new NetRateStats("wlan0");
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
