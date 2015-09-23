/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Statistics.VMStats;
import java.util.List;

/**
 *
 * @author kostas
 */
public class VM {
    
    int vmID;
    int providerID;
    
    String name;
    String service;
    String vmType;
    String ip;
    String netmask;
    
    int slotActivated;
    int slotDeactivated;
    
    boolean activeStatus;
    VMStats[] stats;
    
    
    public VM(String service, String vmType, int providerID, int vmID, int slotAdded, int slotToBeRemoved,int controlInstances) {
        
        this.service = service;
        this.vmType = vmType;
        this.providerID = providerID;
        activeStatus=true;
        
        stats=new VMStats[controlInstances];
    }

    public String getName() {
        return name;
    }

    public String getService() {
        return service;
    }

    public String getVmType() {
        return vmType;
    }

    public String getIp() {
        return ip;
    }

    public String getNetmask() {
        return netmask;
    }

    public int getProviderID() {
        return providerID;
    }
 
    
    
}
