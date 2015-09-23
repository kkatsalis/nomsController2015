/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import Controller.Configuration;
import Controller.VMRequest;
import Enumerators.EServiceType;
import Enumerators.ESlotDurationMetric;
import Enumerators.EVMType;
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
    
    public static String determineVMType(int providerID) {
        
      int type=Utilities.randInt(0,2);
      String vmType="";
      if(type==0)
          vmType=EVMType.Small.toString(); 
      else if(type==1)
          vmType=EVMType.Medium.toString();
      else if(type==2)
          vmType=EVMType.Large.toString();
      
      return vmType;
      
    }
    
    public static String determineVMService(int providerID) {
        
      int type=Utilities.randInt(0,1);
      String service=""; 
      
      if(type==0)
          service=EServiceType.AB.toString(); 
      else if(type==1)
          service=EServiceType.VLC.toString();
      
      return service;
    }
 
    public static Hashtable determineVMparameters(VMRequest vmRequest,String host) {
    
        Hashtable parameters=new Hashtable();
    
        String vmName="host_"+host+"_vm_"+String.valueOf(vmRequest.getProviderID())+"_"+String.valueOf(vmRequest.getRequestID());
        
        parameters.put("hostName",host);
        parameters.put("vmName",vmName);
        parameters.put("OS","precise");
        parameters.put("vmType",vmRequest.getVmType());
        parameters.put("interIP","192.168.XXX.YYY");
        parameters.put("interMask","255.255.254.0");
        parameters.put("interDefaultGateway","192.168.XXX.YYY");
        
        return parameters;
        
        
    }    
}
