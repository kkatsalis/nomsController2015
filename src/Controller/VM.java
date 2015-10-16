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
    VMStats stats;
    Resources resources;
    Configuration config;
    
    
    public VM(Hashtable vmParameters, VMRequest request,int vmID, int slot,String hostName,Configuration config) {
        
        this.config=config;
        
        this.hostname=hostName;
        
        this.vmID=vmID;
        this.name=(String)vmParameters.get("vmName");
        this.ip=(String)vmParameters.get("interIP");
        this.netmask=(String)vmParameters.get("interMask");
        
        this.service = request.getServiceType();
        this.vmType = request.getVmType();
        this.providerID = request.getProviderID();
        this.vmReuestId=request.getRequestID();
        
        this.slotActivated=slot;
        
        this.resources=new Resources();
        this.active=true;
        this.stats=new VMStats();
        
        loadResourcesSpecification();
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

    public VMStats getStats() {
        return stats;
    }

    public void setStats(VMStats stats) {
        this.stats = stats;
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

    private void loadResourcesSpecification() {
    
        switch(vmType){
            
            case "small": 
                resources.setCpu(config.getCpu_VM()[0]);
                resources.setMemory(config.getMemory_VM()[0]);
            case "medium": 
                resources.setStorage(config.getStorage_VM()[1]);    
            case "large": 
                resources.setBandwidth(config.getBandwidth_VM()[0]);
        
        
        }
    }

    
 
    
    
}
