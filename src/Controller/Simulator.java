/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Enumerators.EGeneratorType;
import Enumerators.ESlotDurationMetric;
import Utilities.Utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Queue;
import java.util.Random;
import jsc.distributions.Exponential;
import jsc.distributions.Pareto;

import java.awt.Toolkit;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author kostas
 */
public class Simulator {
    /**
     * @param args the command line arguments
     */
       static int slot=0;
       
       Configuration _config;
       
       List<String> _hostNames;
       List<String> _clientNames;
       List<String> _serviceNames;
       List<String> _vmTypesNames;
       
       Host[] _hosts;
       WebClient[] _clients;
       Slot[] _slots;
       
       Controller _controller;
       int[] requestIDs;
       // How to create requests per Service Domain, one per provider
        String[] _rateGeneratorType; 
        Exponential[] _rateExponentialGenerator;
        Pareto[] _rateParetoGenerator;
        
        // Lifetime of VM one per provider
        String[] _lifetimeGeneratorType; 
        Exponential[] _lifetimeExponentialGenerator;
        Pareto[] _lifetimeParetoGenerator;

        
        Random rand;
        
        Timer timer;
  
        long experimentStart;
        long experimentStop;
        
        public Simulator(List<String> hostNames,List<String> clientNames,List<String> servicesNames, List<String> vmTypesNames){
           
           this._config=new Configuration(hostNames,clientNames,servicesNames,vmTypesNames);
           this._hostNames=hostNames;
    
           this._hosts=new Host[hostNames.size()];
           this._clients=new WebClient[clientNames.size()];
           
           this.requestIDs=new int[_config.getProvidersNumber()];
            for (int i = 0; i < _config.getProvidersNumber(); i++) {
                requestIDs[i]=0;
            }
           
           initializeRateGenerators(); 
           initializeVmLifetimeGenerators();
          
           initializeNodeObjects();
           initializeSlots();
           addVMEvents();
           this._controller=new Controller(_hosts,_clients,_config,_slots); 
         
       }
      

       

       
        private void initializeRateGenerators()
        {
             _rateGeneratorType=new String[_config.getProvidersNumber()];

            for (int i = 0; i < _config.getProvidersNumber(); i++) {
                _rateGeneratorType[i]=_config.getVmRequestRateConfig()[i].get("vmRate_type").toString();
            }

            // 2. Build the array of generators
            double lamda;
            double location;
            double shape;

            this._rateExponentialGenerator=new Exponential[_config.getProvidersNumber()];
            this._rateParetoGenerator=new Pareto[_config.getProvidersNumber()];

                for (int i = 0; i < _config.getProvidersNumber(); i++) {

                    if(_rateGeneratorType[i].equals(EGeneratorType.Exponential.toString())){

                        lamda=((Double)_config.getVmRequestRateConfig()[i].get("mean"));
                        this._rateExponentialGenerator[i]=new Exponential(lamda);
                        this._rateParetoGenerator[i]=null;
                    }
                    else if(_rateGeneratorType[i].equals(EGeneratorType.Pareto.toString())){

                      _rateExponentialGenerator[i]=null;  
                      location=((Double)_config.getVmRequestRateConfig()[i].get("location"));
                      shape =((Double)_config.getVmRequestRateConfig()[i].get("shape"));

                      this._rateParetoGenerator[i]=new Pareto(location, shape);
                      double mean=_rateParetoGenerator[i].mean();
                      System.out.print(mean);
                    }

                }      

        }
        
        private void initializeVmLifetimeGenerators()
        {
             _lifetimeGeneratorType=new String[_config.getProvidersNumber()];

            for (int i = 0; i < _config.getProvidersNumber(); i++) {
                _lifetimeGeneratorType[i]=_config.getVmRequestRateConfig()[i].get("vmRate_type").toString();
            }

            // 2. Build the array of generators
            double lamda;
            double location;
            double shape;

            this._lifetimeExponentialGenerator=new Exponential[_config.getProvidersNumber()];
            this._lifetimeParetoGenerator=new Pareto[_config.getProvidersNumber()];

                for (int i = 0; i < _config.getProvidersNumber(); i++) {

                    if(_lifetimeGeneratorType[i].equals(EGeneratorType.Exponential.toString())){

                        lamda=((Double)_config.getVmRequestRateConfig()[i].get("mean"));
                        this._lifetimeExponentialGenerator[i]=new Exponential(lamda);
                        this._lifetimeParetoGenerator[i]=null;
                    }
                    else if(_lifetimeGeneratorType[i].equals(EGeneratorType.Pareto.toString())){

                      _lifetimeExponentialGenerator[i]=null;  
                      location=((Double)_config.getVmRequestRateConfig()[i].get("location"));
                      shape =((Double)_config.getVmRequestRateConfig()[i].get("shape"));

                      this._lifetimeParetoGenerator[i]=new Pareto(location, shape);
                      double mean=_lifetimeParetoGenerator[i].mean();
                      System.out.print(mean);
                    }

                }      

        }
       



        

        private void initializeNodeObjects() {
        
            for (int i = 0; i < _hosts.length; i++) {
                _hosts[i]=new Host(_config,_hostNames.get(i));
            }
            
            for (int i = 0; i < _clients.length; i++) {
                _clients[i]=new WebClient(_config,_clientNames.get(i));
            }
            
            

        }

        
        // Slot 
        private void initializeSlots() {
        
            _slots=new Slot[_config.getNumberOfSlots()];
            
            for (int i = 0; i < _config.getNumberOfSlots(); i++) {
                _slots[i]=new Slot(i,_config);
                
            }
        
        
        }
        
        
        private void addVMEvents() {
         
            int runningSlot=0;
            
            for (int i = 0; i < _config.getProvidersNumber(); i++) {
                
                runningSlot=0;
                
                while(runningSlot<_config.getNumberOfSlots()){
                    
                    runningSlot=CreateNewVMRequest(i,runningSlot);

                }
            }
            
        }
    
    //Returns the new running slot (this can be also 0)    
    private int CreateNewVMRequest(int providerID,int currentSlot)
    {
        
        
        int slot2AddVM=0;
        int slot2RemoveVM=0;
        
        int lifetime=calculateVMLifeTime(providerID);
       
         // Slot calculation
        int slotDistance= calculateNextSlotTime(providerID);
        
        slot2AddVM=currentSlot+slotDistance;
        slot2RemoveVM=slot2AddVM+lifetime;
        
        if(slot2AddVM<_config.getNumberOfSlots()){
            requestIDs[providerID]++;
            VMRequest newRequest = new VMRequest(providerID,requestIDs[providerID],lifetime);

            newRequest.setVmType(Utilities.determineVMType(providerID));
            newRequest.setService(Utilities.determineVMService(providerID));


            //add vm during this slot
            newRequest.setSlotStart(slot2AddVM);
             //remove vm during this slot
            newRequest.setSlotEnd(slot2RemoveVM);

            //Update Slot Lists
            
                _slots[slot2AddVM].getVmRequests2Activate()[providerID].add(newRequest);
        
            if(slot2RemoveVM<_config.getNumberOfSlots())
                _slots[slot2RemoveVM].getVmRequests2Remove()[providerID].add(newRequest); 
        }
        else
            System.out.println("failed to add: "+requestIDs[providerID] );
         
        
        
         return slot2AddVM;

    }

    
    private int calculateNextSlotTime(int providerID) {
     
    		
       int interArrivalTime=-1;

       int min=0;
       int max=0;
        Double value;
        switch (EGeneratorType.valueOf(this._rateGeneratorType[providerID])){
            case Exponential:
                value=_rateExponentialGenerator[providerID].random();
                interArrivalTime = value.intValue();
                break;
            
            case Pareto:
                value=_rateParetoGenerator[providerID].random();
                interArrivalTime=value.intValue();
                                
                break;
            
            
            case Random: 
               min= Integer.valueOf(_config.getVmRequestRateConfig()[providerID].get("min").toString());
               max= Integer.valueOf(_config.getVmRequestRateConfig()[providerID].get("max").toString());
                
               interArrivalTime = Utilities.randInt(min, max);
                break;
           
           
            default:            
                break;
        }
        

        return interArrivalTime;
        
        }
   
     private int calculateVMLifeTime(int providerID) {
     
    		
        int lifetime=-1;

        int min=0;
        int max=0;
        Double value;
        
        switch (EGeneratorType.valueOf(this._lifetimeGeneratorType[providerID])){
            case Exponential:
                value=_lifetimeExponentialGenerator[providerID].random();
                lifetime = value.intValue();
                break;
            
            case Pareto:
                value=_lifetimeParetoGenerator[providerID].random();
                lifetime=value.intValue();
                                
                break;
            
            
            case Random: 
               min= Integer.valueOf(_config.getVmLifeTimeConfig()[providerID].get("min").toString());
               max= Integer.valueOf(_config.getVmLifeTimeConfig()[providerID].get("max").toString());
                
               lifetime = Utilities.randInt(min, max);
                break;
           
           
            default:            
                break;
        }
        
 
        if(lifetime!=0)
            return lifetime;
        else 
            return 3;
        
    }
        
        
     public final void StartExperiment() {

            int duration=_config.getSlotDuration();

            experimentStart=System.currentTimeMillis();
            System.out.println("start: " +experimentStart);

            timer = new Timer();
            
            if(_config.getSlotDurationMetric().equals(ESlotDurationMetric.milliseconds.toString()))
               timer.scheduleAtFixedRate(new ExecuteSlot(),0 ,duration);
            else if(_config.getSlotDurationMetric().equals(ESlotDurationMetric.seconds.toString()))
               timer.scheduleAtFixedRate(new ExecuteSlot(),0 ,duration*1000);
            else if(_config.getSlotDurationMetric().equals(ESlotDurationMetric.minutes.toString()))
               timer.scheduleAtFixedRate(new ExecuteSlot(),0 ,60*duration*1000);
            else if(_config.getSlotDurationMetric().equals(ESlotDurationMetric.hours.toString()))
               timer.scheduleAtFixedRate(new ExecuteSlot(),0 ,3600*duration*1000);
            
        }

        
        
    class ExecuteSlot extends TimerTask {

            public void run() {
             
                try {
                    if(slot<_config.getNumberOfSlots()){

                        _controller.Run(slot);

                        slot++; 
                    
                    }else{
                        experimentStop=System.currentTimeMillis();
                        timer.cancel();
                        System.exit(0);
                    }
                } 
                catch (IOException ex) {
                    Logger.getLogger(Simulator.class.getName()).log(Level.SEVERE, null, ex);
                }
              
            }
  }
}
