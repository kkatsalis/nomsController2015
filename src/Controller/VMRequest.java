/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Enumerators.EVMType;
import Utilities.Utilities;
import Enumerators.EServiceType;
import java.util.Random;

/**
 *
 * @author kostas
 */
public class VMRequest {
    
    int providerID;
    int requestID=0;
    int slotStart;
    int slotEnd;
    
    String vmType;
    String service;
    int lifetime;     //in Slots
   
    public VMRequest(int providerID,int requestID, int lifetime) {
        
        this.providerID = providerID;
        this.lifetime=lifetime;
        this.requestID=requestID;
        
    
    }

    
    
    
    public int getProviderID() {
        return providerID;
    }

    public int getRequestID() {
        return requestID;
    }

      
    

    public String getVmType() {
        return vmType;
    }

    public void setVmType(String vmType) {
        this.vmType = vmType;
    }

    public int getSlotStart() {
        return slotStart;
    }

    public void setSlotStart(int slotStart) {
        this.slotStart = slotStart;
    }

    public int getSlotEnd() {
        return slotEnd;
    }

    public void setSlotEnd(int slotEnd) {
        this.slotEnd = slotEnd;
    }

   

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
    
    
   

    

    
    
    

}
