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

//small  : 1 core-1024 ram- 10g storage 
//medium : 2 core-2048 ram- 20g storage
//large  : 4 core-4096 ram- 40g storage 


public class Resources {
 
    String type;
    
    double cpu;
    double memory;
    double storage;
    double bandwidth;

    public Resources(String type,Configuration config) {
        this.type = type;
        
        switch (type) {
            case "Host":
                cpu=8;
                memory=8192;
                storage=100;
                bandwidth=10;
                break;
            case "SmallVM":
                cpu=1;
                memory=1024;
                storage=10;
                bandwidth=1;
                break;
            case "MediumVM":
                cpu=2;
                memory=2024;
                storage=20;
                bandwidth=1;
                break;
            case "LargeVM":
                cpu=4;
                memory=4024;
                storage=40;
                bandwidth=1;
                break;
        }
        
    }

    public String getType() {
        return type;
    }

    public double getCpu() {
        return cpu;
    }

    public double getMemory() {
        return memory;
    }

    public double getStorage() {
        return storage;
    }

    public double getBandwidth() {
        return bandwidth;
    }
    
    
    
    
}
