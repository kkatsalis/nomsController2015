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
public class ServiceRequestsPerProvider {
    
    int providerID;
    int[] numberOfExpectedrequestsPerService;

    public ServiceRequestsPerProvider(int numberOfServices) {
        
      
        numberOfExpectedrequestsPerService=new int[numberOfServices];
        
        for (int i = 0; i < numberOfServices; i++) {
            numberOfExpectedrequestsPerService[i]=1000;
        }
        
    }

    
    public int getProviderID() {
        return providerID;
    }

    public void setProviderID(int providerID) {
        this.providerID = providerID;
    }

   
    public int[] getNumberOfExpectedrequestsPerService() {
        return numberOfExpectedrequestsPerService;
    }

    
    
    
    
    
}
