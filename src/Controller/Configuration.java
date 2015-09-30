/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Enumerators.EGeneratorType;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    int numberOfSlots;
    int providersNumber;
    int hostsNumber;
    int clientsNumber;
    int servicesNumber;
    int vmTypesNumber;
    int machineResourcesNumber;
    
    String nitosServer;
    int slotDuration;
    String slotDurationMetric;
    int statsUpdatesPerSlot;
    
    double[] phiWeight;
    HashMap[] _vmRequestRateConfig;
    HashMap[] _vmLifeTimeConfig;
 
    List<String> clientNames=new ArrayList<>();
    List<String> hostNames=new ArrayList<>();
    List<String> servicesNames=new ArrayList<>();
    List<String> vmTypesNames=new ArrayList<>();
    
    double cpu_host;
    double memory_host;
    double storage_host;
    double bandwidth_host;
    double[] cpu_VM; //One per VM Type
    double[] memory_VM; //One per VM Type
    double[] storage_VM; //One per VM Type
    double[] bandwidth_VM; //One per VM Type
    
    double[][] penalty; //[i][k] provider i, service k
    
    int abRequestsNumber=1000;
    int abBatchRequestsNumber=100;
    HashMap associatedAPsPerClient;
            
    public Configuration() {
  
        this.clientNames=new ArrayList<>();
        this.hostNames=new ArrayList<>();
        this.servicesNames=new ArrayList<>();
        this.vmTypesNames=new ArrayList<>();
        this.associatedAPsPerClient=new HashMap();
                
        this.addHostNodes();        
        this.addClientNodes();
        this.addServices();
        this.addVmTypes();
        
        this.loadProperties();
        this.loadRequestRatesParameters();
        this.loadVmLifetimeParameters();
        this.loadFairnessWeights();
        this.loadResources();
        this.loadPenalties();
        
     
    }
   
        private void addHostNodes(){
            
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

            hostsNumber=Integer.valueOf(property.getProperty("hostsNumber"));

            String parameter="";
            String hostName="";

            // InterArrival Time
            for (int i = 0; i < hostsNumber; i++) {
                parameter="host_"+i;
                hostName=String.valueOf((String)property.getProperty(parameter));
                hostNames.add(hostName);
            }
        
        }
    
        
        
        
        
        
        private void addClientNodes() {
            
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

            clientsNumber=Integer.valueOf(property.getProperty("clientsNumber"));

            String parameter="";
            String clientName="";
            String associatedAP;
            
            // InterArrival Time
            for (int i = 0; i < clientsNumber; i++) {
                parameter="client_"+i;
                clientName=String.valueOf((String)property.getProperty(parameter));
                clientNames.add(clientName);
                
                parameter="client_"+i+"_ap";
                associatedAP=String.valueOf((String)property.getProperty(parameter));
                associatedAPsPerClient.put(parameter, associatedAP);
                
            }
          
        }

        private void addServices() {
         
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

            servicesNumber=Integer.valueOf(property.getProperty("servicesNumber"));

            String parameter="";
            String serviceName="";

            // InterArrival Time
            for (int i = 0; i < servicesNumber; i++) {
                parameter="service_"+i;
                serviceName=String.valueOf((String)property.getProperty(parameter));
                servicesNames.add(serviceName);
            }

        }
    
        private void addVmTypes() {

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

            vmTypesNumber=Integer.valueOf(property.getProperty("vmTypesNumber"));

            String parameter="";
            String vmTypeName="";

            // InterArrival Time
            for (int i = 0; i < vmTypesNumber; i++) {
                parameter="vmType_"+i;
                vmTypeName=String.valueOf((String)property.getProperty(parameter));
                vmTypesNames.add(vmTypeName);
            }


        }
        
        
    private void loadProperties(){
    
        Properties property = new Properties();
        
        try{    
            String filename = "simulation.properties";
            InputStream input = Configuration.class.getClassLoader().getResourceAsStream(filename);

            // load a properties file
            property.load(input);

            simulationID=Integer.valueOf(property.getProperty("simulationID"));
            providersNumber=Integer.valueOf(property.getProperty("providers"));
            numberOfSlots=Integer.valueOf(property.getProperty("slots"));
            nitosServer=String.valueOf(property.getProperty("server"));
            slotDuration=Integer.valueOf(property.getProperty("slotDuration"));
            slotDurationMetric=String.valueOf(property.getProperty("slotDurationMetric"));
            statsUpdatesPerSlot=Integer.valueOf(property.getProperty("statsUpdatesPerSlot"));
            machineResourcesNumber=Integer.valueOf(property.getProperty("machineResourcesNumber"));
            abRequestsNumber=Integer.valueOf(property.getProperty("abRequestsNumber"));
            abBatchRequestsNumber=Integer.valueOf(property.getProperty("abBatchRequestsNumber"));
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
    
    }

    private void loadFairnessWeights(){
    
        phiWeight=new double[providersNumber];
    
        Properties property = new Properties();
        InputStream input = null;    
        String filename = "simulation.properties";
        String parameter="";
        input = Configuration.class.getClassLoader().getResourceAsStream(filename);

        try {
                // load a properties file
                property.load(input);
        }
        catch (IOException ex) {
                Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
         }
            
        for (int i = 0; i < providersNumber; i++) {
            parameter="phiWeight_"+i;
            phiWeight[i]=Double.valueOf((String)property.getProperty(parameter));
        }
    
    }
    
      private void loadResources() {
        
        cpu_VM=new double [vmTypesNumber];
        memory_VM=new double [vmTypesNumber];
        storage_VM=new double [vmTypesNumber];
        bandwidth_VM=new double [vmTypesNumber];
        
        Properties property = new Properties();
        InputStream input = null;    
        String filename = "simulation.properties";
        String parameter="";
        input = Configuration.class.getClassLoader().getResourceAsStream(filename);

        try {
                // load a properties file
                property.load(input);
        }
        catch (IOException ex) {
                Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
         }
        
        //HOST Resources
        parameter="cpu_host";
        cpu_host=Double.valueOf((String)property.getProperty(parameter));
        parameter="memory_host";
        memory_host=Double.valueOf((String)property.getProperty(parameter));
        parameter="storage_host";
        storage_host=Double.valueOf((String)property.getProperty(parameter));
        parameter="bandwidth_host";
        bandwidth_host=Double.valueOf((String)property.getProperty(parameter));

        // VM Resources
        // CPU
        parameter="cpu_SmallVM";
        cpu_VM[0]=Double.valueOf((String)property.getProperty(parameter));
        parameter="cpu_MediumVM";
        cpu_VM[1]=Double.valueOf((String)property.getProperty(parameter));
        parameter="cpu_LargeVM";
        cpu_VM[2]=Double.valueOf((String)property.getProperty(parameter));
        
        // MEMORY
        parameter="memory_SmallVM";
        memory_VM[0]=Double.valueOf((String)property.getProperty(parameter));
        parameter="memory_MediumVM";
        memory_VM[1]=Double.valueOf((String)property.getProperty(parameter));
        parameter="memory_LargeVM";
        memory_VM[2]=Double.valueOf((String)property.getProperty(parameter));
              
        // Storage
        parameter="storage_SmallVM";
        storage_VM[0]=Double.valueOf((String)property.getProperty(parameter));
        parameter="storage_MediumVM";
        storage_VM[1]=Double.valueOf((String)property.getProperty(parameter));
        parameter="storage_LargeVM";
        storage_VM[2]=Double.valueOf((String)property.getProperty(parameter));
        
         // Bandwidth
        parameter="bandwidth_SmallVM";
        bandwidth_VM[0]=Double.valueOf((String)property.getProperty(parameter));
        parameter="bandwidth_MediumVM";
        bandwidth_VM[1]=Double.valueOf((String)property.getProperty(parameter));
        parameter="bandwidth_LargeVM";
        bandwidth_VM[2]=Double.valueOf((String)property.getProperty(parameter));
        
        
    }
    
    private void loadPenalties() {
        
        Properties property = new Properties();
        InputStream input = null;    
        String filename = "simulation.properties";

        input = Configuration.class.getClassLoader().getResourceAsStream(filename);

        penalty=new double[providersNumber][servicesNumber];
        
        try {
                property.load(input);
        } catch (IOException ex) {
                Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
        }

            String parameter="";
        
            for (int i = 0; i < providersNumber; i++) {
                for (int j = 0; j < servicesNumber; j++) {
                    parameter="penalty_p"+i+"_s"+j;
                    penalty[i][j]=Double.valueOf((String)property.getProperty(parameter));
                }
               
         
            }
    }
    
    
    private void loadRequestRatesParameters() {

      
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
        String rateType="";
        double value=-1;
        
        _vmRequestRateConfig=new HashMap[providersNumber];
        
        // InterArrival Time
        for (int i = 0; i < providersNumber; i++) {
            
            _vmRequestRateConfig[i]=new HashMap();
            
            parameter="vmRate_type_"+i;
            rateType=String.valueOf((String)property.getProperty(parameter));
            
            if(EGeneratorType.Exponential.toString().equals(rateType)){
                parameter="vmRate_lamda_"+i;
                value=Double.valueOf((String)property.getProperty(parameter));
                value=(double)1/value;
                _vmRequestRateConfig[i].put("vmRate_type",EGeneratorType.Exponential.toString());
                _vmRequestRateConfig[i].put("mean",value);
            }
            else if(EGeneratorType.Pareto.toString().equals(rateType)){
                _vmRequestRateConfig[i].put("vmRate_type",EGeneratorType.Pareto.toString());
                
                parameter="vmRate_location_"+i;
                value=Double.valueOf((String)property.getProperty(parameter));
                _vmRequestRateConfig[i].put("location",value);
                
                parameter="vmRate_shape_"+i;
                value=Double.valueOf((String)property.getProperty(parameter));
                _vmRequestRateConfig[i].put("shape",value);
            
            }
            else if(EGeneratorType.Random.toString().equals(rateType)){
                  _vmRequestRateConfig[i].put("vmRate_type",EGeneratorType.Random.toString());
                
                parameter="min_rate_"+i;
                value=Double.valueOf((String)property.getProperty(parameter));
                _vmRequestRateConfig[i].put("min",value);
                
                parameter="max_rate_"+i;
                value=Double.valueOf((String)property.getProperty(parameter));
                _vmRequestRateConfig[i].put("max",value);
                
                
                
                
             }
            
         }
        
        
    }

    private void loadVmLifetimeParameters() {

      
        Properties property = new Properties();
	InputStream input = null;    
     	String filename = "simulation.properties";
        String parameter="";
        String rateType="";
        double value=-1;
        
        input = Configuration.class.getClassLoader().getResourceAsStream(filename);
                
        try {
            // load a properties file
            property.load(input);
       
            _vmLifeTimeConfig=new HashMap[providersNumber];
        
        // InterArrival Time
        for (int i = 0; i < providersNumber; i++) {
            
            _vmLifeTimeConfig[i]=new HashMap();
            
            parameter="lifetime_type_"+i;
            rateType=String.valueOf((String)property.getProperty(parameter));
            
            if(EGeneratorType.Exponential.toString().equals(rateType)){
                parameter="lifetime_lamda_"+i;
                value=Double.valueOf((String)property.getProperty(parameter));
                value=(double)1/value;
                _vmLifeTimeConfig[i].put("lifetime_type",EGeneratorType.Exponential.toString());
                _vmLifeTimeConfig[i].put("mean",value);
            }
            else if(EGeneratorType.Pareto.toString().equals(rateType)){
                _vmLifeTimeConfig[i].put("lifetime_type",EGeneratorType.Pareto.toString());
                
                parameter="lifetime_location_"+i;
                value=Double.valueOf((String)property.getProperty(parameter));
                _vmLifeTimeConfig[i].put("location",value);
                
                parameter="lifetime_shape_"+i;
                value=Double.valueOf((String)property.getProperty(parameter));
                _vmLifeTimeConfig[i].put("shape",value);
            
            }
            else if(EGeneratorType.Random.toString().equals(rateType)){
                  _vmLifeTimeConfig[i].put("lifetime_type",EGeneratorType.Random.toString());
                
                parameter="lifetime_min_"+i;
                value=Double.valueOf((String)property.getProperty(parameter));
                _vmLifeTimeConfig[i].put("min",value);
                
                parameter="lifetime_max_"+i;
                value=Double.valueOf((String)property.getProperty(parameter));
                _vmLifeTimeConfig[i].put("max",value);
            }
            
         }
         } catch (IOException ex) {
            Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    public int getProvidersNumber() {
        return providersNumber;
    }
    
    public int getSimulationID() {
        return simulationID;
    }

    public int getNumberOfSlots() {
        return numberOfSlots;
    }
  
    public String getNitosServer() {
        return nitosServer;
    }

    public int getSlotDuration() {
        return slotDuration;
    }

    public HashMap[] getVmRequestRateConfig() {
        return _vmRequestRateConfig;
    }

    public HashMap[] getVmLifeTimeConfig() {
        return _vmLifeTimeConfig;
    }
    
    public String getSlotDurationMetric() {
        return slotDurationMetric;
    }

    public int getStatsUpdatesPerSlot() {
        return statsUpdatesPerSlot;
    }

    public int getHostsNumber() {
        return hostsNumber;
    }

    public void setHostsNumber(int hostsNumber) {
        this.hostsNumber = hostsNumber;
    }

    public int getClientsNumber() {
        return clientsNumber;
    }

    public void setClientsNumber(int clientsNumber) {
        this.clientsNumber = clientsNumber;
    }

    public int getServicesNumber() {
        return servicesNumber;
    }

    public int getVmTypesNumber() {
        return vmTypesNumber;
    }

    public List<String> getClientNames() {
        return clientNames;
    }

    public List<String> getHostNames() {
        return hostNames;
    }

    public List<String> getServicesNames() {
        return servicesNames;
    }

    public List<String> getVmTypesNames() {
        return vmTypesNames;
    }

    public int getMachineResourcesNumber() {
        return machineResourcesNumber;
    }

    public double[] getPhiWeight() {
        return phiWeight;
    }

    public double[] getCpu_VM() {
        return cpu_VM;
    }

    public double[] getMemory_VM() {
        return memory_VM;
    }

    public double[] getStorage_VM() {
        return storage_VM;
    }

    public double[] getBandwidth_VM() {
        return bandwidth_VM;
    }

    public double getCpu_host() {
        return cpu_host;
    }

    public void setCpu_host(double cpu_host) {
        this.cpu_host = cpu_host;
    }

    public double getMemory_host() {
        return memory_host;
    }

    public void setMemory_host(double memory_host) {
        this.memory_host = memory_host;
    }

    public double getStorage_host() {
        return storage_host;
    }

    public void setStorage_host(double storage_host) {
        this.storage_host = storage_host;
    }

    public double getBandwidth_host() {
        return bandwidth_host;
    }

    public void setBandwidth_host(double bandwidth_host) {
        this.bandwidth_host = bandwidth_host;
    }

    public double[][] getPenalty() {
        return penalty;
    }

    public int getAbRequestsNumber() {
        return abRequestsNumber;
    }

    public int getAbBatchRequestsNumber() {
        return abBatchRequestsNumber;
    }

    public HashMap getAssociatedAPsPerClient() {
        return associatedAPsPerClient;
    }

    
    
    
}
