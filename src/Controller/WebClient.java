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
    String _clientName;
    String _apName;
    
    ABStats abStats;
    VLCStats vlcStats;
    NetRateStats netRateStats;
    
    Host[] _hosts;
    
    
    public WebClient(Configuration config,int id, String clientNodeName, Host[] hosts) {
        
        this._config=config;
        this._clientName=clientNodeName;
        this._apName=String.valueOf(config.getAssociatedAPsPerClient().get("client_"+id+"_ap"));
        this._hosts=hosts;
        
        abStats=new ABStats();
        vlcStats=new VLCStats();
        netRateStats=new NetRateStats();
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
    
    public void RunVLCApp(){ 
    
    
    }
    
}
