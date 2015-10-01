/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Statistics.VMStats;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 *
 * @author kostas
 */
public class VM {
    
    int vmID;
    int providerID;
    String hostname;
    
    String name;
    String service;
    String vmType;
    String ip;
    String netmask;
    
    int slotActivated;
    int slotDeactivated;
    int vmReuestId;
    boolean active;
    List<VMStats> stats;
    
    
    public VM(Hashtable vmParameters, VMRequest request,int vmID, int slot,String nodeName) {
        this.name=(String)vmParameters.get("vmName");
        this.service = request.getServiceType();
        this.vmType = request.getVmType();
        this.providerID = request.getProviderID();
        this.vmID=vmID;
        this.ip=(String)vmParameters.get("interIP");
        this.netmask=(String)vmParameters.get("interMask");
        this.slotActivated=slot;
        this.vmReuestId=request.getRequestID();
        this.hostname=nodeName;
        
        active=true;
        
        stats=new ArrayList<>();
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

    public List<VMStats> getStats() {
        return stats;
    }

    public int getSlotActivated() {
        return slotActivated;
    }

    public void setSlotActivated(int slotActivated) {
        this.slotActivated = slotActivated;
    }

    public int getSlotDeactivated() {
        return slotDeactivated;
    }

    public void setSlotDeactivated(int slotDeactivated) {
        this.slotDeactivated = slotDeactivated;
    }

    public int getVmReuestId() {
        return vmReuestId;
    }

    public void setActive(boolean activeStatus) {
        this.active = activeStatus;
    }

    public String getHostname() {
        return hostname;
    }

    public boolean isActive() {
        return active;
    }

    
 
    
    
}
