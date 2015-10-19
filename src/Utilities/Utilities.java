/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import Controller.Configuration;
import Controller.Host;
import Controller.Slot;
import Controller.VM;
import Controller.VMRequest;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 *
 * @author kostas
 */
public class Utilities {
    
   
    public static int randInt(int min, int max) {

        Random rand=new Random();
    
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
    
    public static String determineVMType(int providerID,Configuration _config) {
        
       //Random Allocation 
      int type=Utilities.randInt(0,_config.getVmTypesNames().size()-1);
     
       String  vmType=_config.getVmTypesNames().get(type); //Small
      
      return vmType;
      
    }
    
    public static String determineVMService(int providerID, Configuration _config) {
        
      int type=Utilities.randInt(0,_config.getServicesNames().size()-1);
      String serviceType=_config.getServicesNames().get(type); 
      
      return serviceType;
    }
 
    public static Hashtable determineVMparameters(VMRequest vmRequest,String hostName) {
    
        Hashtable parameters=new Hashtable();
        int y=vmRequest.getRequestID();
        
        String vmName=hostName+"p"+String.valueOf(vmRequest.getProviderID())+"r"+String.valueOf(vmRequest.getRequestID());
        
        parameters.put("hostName",hostName);
        parameters.put("vmName",vmName);
        parameters.put("OS","precise");
        parameters.put("vmType",vmRequest.getVmType());
        parameters.put("interIP","10.64.98."+String.valueOf(y));
        parameters.put("interMask","255.255.254.0");
        parameters.put("interDefaultGateway","10.64.98.1");
        
        return parameters;
        
        
    }   
    
    public static List<VMRequest> findVMequests2RemoveThisSlot(int slot,Slot[] _slots,Configuration _config){
    
        List<VMRequest> vmRequests2RemoveThisSlot=new ArrayList<>();
       
       // Step 1: Find RequestIDs to remove
       for (int i = 0; i < _config.getProvidersNumber(); i++) {
           for (int j = 0; j < _slots[slot].getVmRequests2Remove()[i].size(); j++) {
               vmRequests2RemoveThisSlot.add(_slots[slot].getVmRequests2Remove()[i].get(j));
           }
       }
    
       return vmRequests2RemoveThisSlot;
    }
    
     public static List<VMRequest> vmRequests2ActivateThisSlot(int slot,Slot[] _slots,Configuration _config){
    
        List<VMRequest> vmRequests2ActivateThisSlot=new ArrayList<>();
       
       // Step 1: Find RequestIDs to remove
       for (int i = 0; i < _config.getProvidersNumber(); i++) {
           for (int j = 0; j < _slots[slot].getVmRequests2Remove()[i].size(); j++) {
               vmRequests2ActivateThisSlot.add(_slots[slot].getVmRequests2Activate()[i].get(j));
           }
       }
    
       return vmRequests2ActivateThisSlot;
    }
     
     public static List<VM> findActiveVMs(int slot,Host[] _hosts){
     
        List<VM> activeVMs=new ArrayList<>();
        List<VM> vms;
        
        
         for (int i = 0; i < _hosts.length; i++) {
             vms=_hosts[i].getVMs();
             
             for (Iterator<VM> iterator = vms.iterator(); iterator.hasNext();) {
                 VM nextVM = iterator.next();
                 if(nextVM.isActive())
                     activeVMs.add(nextVM);
             }
             
         }
 
         return activeVMs;
     
     }
     
     public static List<VM> closedVMs(int slot,Host[] _hosts){
     
        List<VM> closedVMs=new ArrayList<>();
        List<VM> vms;
        
        
         for (int i = 0; i < _hosts.length; i++) {
             vms=_hosts[i].getVMs();
             
             for (Iterator<VM> iterator = vms.iterator(); iterator.hasNext();) {
                 VM nextVM = iterator.next();
                 if(!nextVM.isActive())
                     closedVMs.add(nextVM);
             }
             
         }
 
         return closedVMs;
     
     }
     
     public static int findHostID(Configuration config,String hostName){
     
        int id=-1;
     
         for (int i = 0; i < config.getHostNames().size(); i++) {
             if( config.getHostNames().get(i).equals(hostName))
                 return i; 
         }
        
         return id;
     }
     
     
     public static int[] findRequestPattern(Configuration _config) {

        //Moving Average 
        int[] r = new int [_config.getProvidersNumber()]; // requests per service provider
	
        for (int j=0;j<_config.getProvidersNumber();j++)
            r[j] = 1000000000*(j+1);
                
            return r;
    }
     
      public static String chooseService2Call(Configuration _config,int clientID) {
          
            Random rand=new Random();
            String service=_config.getServicesNames().get(rand.nextInt(_config.getServicesNumber()-1));
            
            return service;
        }
}
