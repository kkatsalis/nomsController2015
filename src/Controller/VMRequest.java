/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

/**
 *
 * @author kostas
 */
public class VMRequest {
    
    int providerID;
    int requestID;
    String vmType;
    long slotOfArrival;     // long start = System.currentTimeMillis( );
    long slotOfDeparture;
    boolean satisfied;

    public VMRequest(int providerID, int requestID) {
        this.providerID = providerID;
        this.requestID = requestID;
    }

    public int getProviderID() {
        return providerID;
    }

      
    public void setRequestID(int requestID) {
        this.requestID = requestID;
    }

    public String getVmType() {
        return vmType;
    }

    public void setVmType(String vmType) {
        this.vmType = vmType;
    }

    public long getSlotOfArrival() {
        return slotOfArrival;
    }

    public void setSlotOfArrival(long slotOfArrival) {
        this.slotOfArrival = slotOfArrival;
    }

    public long getSlotOfDeparture() {
        return slotOfDeparture;
    }

    public void setSlotOfDeparture(long slotOfDeparture) {
        this.slotOfDeparture = slotOfDeparture;
    }

    public boolean isSatisfied() {
        return satisfied;
    }

    public void setSatisfied(boolean satisfied) {
        this.satisfied = satisfied;
    }

    
    
    

}
