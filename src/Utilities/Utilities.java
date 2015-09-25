/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import Controller.Configuration;
import Controller.VMRequest;
import java.util.Hashtable;
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
        
        String vmName="host_"+hostName+"_vm_"+String.valueOf(vmRequest.getProviderID())+"_"+String.valueOf(vmRequest.getRequestID());
        
        parameters.put("hostName",hostName);
        parameters.put("vmName",vmName);
        parameters.put("OS","precise");
        parameters.put("vmType",vmRequest.getVmType());
        parameters.put("interIP","10.64.98."+String.valueOf(y));
        parameters.put("interMask","255.255.254.0");
        parameters.put("interDefaultGateway","10.64.98.1");
        
        return parameters;
        
        
    }    
}
