/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Statistics;

import Controller.Configuration;
import Controller.VMRequest;
import java.util.List;

/**
 *
 * @author kostas
 */
public class ProviderStats {
   
    Configuration _config;
    
    int smallVMRequests=0;
    int mediumVMRequests=0;
    int largeVMRequests=0;
    
    int smallVMRequestsSatisfied=0;    //small  VM Requests Per Provider
    int mediumVMRequestsSatisfied=0;    //small  VM Requests Per Provider
    int largeVMRequestsSatisfied=0;    //small  VM Requests Per Provider
    
    int smallVMRequestsLocalyMigrated=0;    //small  VM Requests Per Provider
    int mediumVMRequestsLocalyMigrated=0;    //small  VM Requests Per Provider
    int largeVMRequestsLocalyMigrated=0;    //small  VM Requests Per Provider
    
    int smallVMRequests2Cloud=0;    //small  VM Requests Per Provider
    int mediumVMRequests2Cloud=0;    //small  VM Requests Per Provider
    int largeVMRequests2Cloud=0;    //small  VM Requests Per Provider
    
    public ProviderStats(Configuration config){
    
        this._config=config;
       
     
    
    }

    public Configuration getConfig() {
        return _config;
    }

    public void setConfig(Configuration _config) {
        this._config = _config;
    }

    public int getSmallVMRequests() {
        return smallVMRequests;
    }

    public void setSmallVMRequests(int smallVMRequests) {
        this.smallVMRequests = smallVMRequests;
    }

    public int getMediumVMRequests() {
        return mediumVMRequests;
    }

    public void setMediumVMRequests(int mediumVMRequests) {
        this.mediumVMRequests = mediumVMRequests;
    }

    public int getLargeVMRequests() {
        return largeVMRequests;
    }

    public void setLargeVMRequests(int largeVMRequests) {
        this.largeVMRequests = largeVMRequests;
    }

    public int getSmallVMRequestsSatisfied() {
        return smallVMRequestsSatisfied;
    }

    public void setSmallVMRequestsSatisfied(int smallVMRequestsSatisfied) {
        this.smallVMRequestsSatisfied = smallVMRequestsSatisfied;
    }

    public int getMediumVMRequestsSatisfied() {
        return mediumVMRequestsSatisfied;
    }

    public void setMediumVMRequestsSatisfied(int mediumVMRequestsSatisfied) {
        this.mediumVMRequestsSatisfied = mediumVMRequestsSatisfied;
    }

    public int getLargeVMRequestsSatisfied() {
        return largeVMRequestsSatisfied;
    }

    public void setLargeVMRequestsSatisfied(int largeVMRequestsSatisfied) {
        this.largeVMRequestsSatisfied = largeVMRequestsSatisfied;
    }

    public int getSmallVMRequestsLocalyMigrated() {
        return smallVMRequestsLocalyMigrated;
    }

    public void setSmallVMRequestsLocalyMigrated(int smallVMRequestsLocalyMigrated) {
        this.smallVMRequestsLocalyMigrated = smallVMRequestsLocalyMigrated;
    }

    public int getMediumVMRequestsLocalyMigrated() {
        return mediumVMRequestsLocalyMigrated;
    }

    public void setMediumVMRequestsLocalyMigrated(int mediumVMRequestsLocalyMigrated) {
        this.mediumVMRequestsLocalyMigrated = mediumVMRequestsLocalyMigrated;
    }

    public int getLargeVMRequestsLocalyMigrated() {
        return largeVMRequestsLocalyMigrated;
    }

    public void setLargeVMRequestsLocalyMigrated(int largeVMRequestsLocalyMigrated) {
        this.largeVMRequestsLocalyMigrated = largeVMRequestsLocalyMigrated;
    }

    public int getSmallVMRequests2Cloud() {
        return smallVMRequests2Cloud;
    }

    public void setSmallVMRequests2Cloud(int smallVMRequests2Cloud) {
        this.smallVMRequests2Cloud = smallVMRequests2Cloud;
    }

    public int getMediumVMRequests2Cloud() {
        return mediumVMRequests2Cloud;
    }

    public void setMediumVMRequests2Cloud(int mediumVMRequests2Cloud) {
        this.mediumVMRequests2Cloud = mediumVMRequests2Cloud;
    }

    public int getLargeVMRequests2Cloud() {
        return largeVMRequests2Cloud;
    }

    public void setLargeVMRequests2Cloud(int largeVMRequests2Cloud) {
        this.largeVMRequests2Cloud = largeVMRequests2Cloud;
    }

    
    
    
}
