/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kostas
 */
public class Configuration {
    
    Configuration _config=this;
      
    int simulationID;
    long simulationTime;
    String server;
    long slotDuration;
    int providers;
    
    Properties property = new Properties();
    InputStream input = null;    
        
    HashMap[] _requestArrivalRateConfig;

    public Configuration() {
   
    }
   
    
    public void loadProperties(){
    
        try{    
            String filename = "simulation.properties";
            input = Configuration.class.getClassLoader().getResourceAsStream(filename);

            // load a properties file
            property.load(input);

            simulationID=Integer.valueOf(property.getProperty("simulationID"));
            providers=Integer.valueOf(property.getProperty("providers"));
            simulationTime=Long.valueOf(property.getProperty("simulationTime"));
            server=String.valueOf(property.getProperty("server"));
            slotDuration=Long.valueOf(property.getProperty("slot"));
  

            }
             catch (Exception e) {

            }
    
    }

     public void loadRequestRatesParameters() {

      
        Properties property = new Properties();
	InputStream input = null;    
            
       
 
    	String filename = "simulation.properties";
    	input = Configuration.class.getClassLoader().getResourceAsStream(filename);
            
        
        try {
            // load a properties file
            property.load(input);
        } catch (IOException ex) {
            Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
        }

        String parameter="";
        String arrType="";
        double value=-1;
        
        _requestArrivalRateConfig=new HashMap[providers];
        
        // InterArrival Time
        for (int i = 0; i < providers; i++) {
            parameter="arrType_"+i;
            arrType=String.valueOf((String)property.getProperty(parameter));
            
            if(EGeneratorType.Exponential.toString().equals(arrType)){
                parameter="lamda"+i;
                value=Double.valueOf((String)property.getProperty(parameter));
                value=(double)1/value;
                _requestArrivalRateConfig[i].put("arrType",EGeneratorType.Exponential.toString());
                _requestArrivalRateConfig[i].put("mean",value);
            }
            else if(EGeneratorType.Pareto.toString().equals(arrType)){
                _requestArrivalRateConfig[i].put("arrType",EGeneratorType.Pareto.toString());
                
                parameter="location"+i;
                value=Double.valueOf((String)property.getProperty(parameter));
                _requestArrivalRateConfig[i].put("location",value);
                
                parameter="shape"+i;
                value=Double.valueOf((String)property.getProperty(parameter));
                _requestArrivalRateConfig[i].put("shape",value);
            
            }
            else if(EGeneratorType.Random.toString().equals(arrType)){
                  _requestArrivalRateConfig[i].put("arrType",EGeneratorType.Random.toString());
                
                parameter="min"+i;
                value=Double.valueOf((String)property.getProperty(parameter));
                _requestArrivalRateConfig[i].put("min",value);
                
                parameter="max"+i;
                value=Double.valueOf((String)property.getProperty(parameter));
                _requestArrivalRateConfig[i].put("max",value);
                
                
                
                
             }
            
         }
        
        
    }

    public int getProviders() {
        return providers;
    }
    
    
    
    public int getSimulationID() {
        return simulationID;
    }

    public long getSimulationTime() {
        return simulationTime;
    }
  
    
    public String getServer() {
        return server;
    }

    public long getSlotDuration() {
        return slotDuration;
    }

    public HashMap[] getRequestArrivalRateConfig() {
        return _requestArrivalRateConfig;
    }
    
    
    
}
