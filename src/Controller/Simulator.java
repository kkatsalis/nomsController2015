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
       int slot=0;
       
       Configuration _config;
       
       List<String> _hostNames;
       List<String> _clientNames;
       List<String> _serviceNames;
       List<String> _vmTypesNames;
       
       Host[] _hosts;
       WebClient[] _clients;
       Slot[] _slots;
       
        Controller _controller;
        
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
        
        public Simulator(){
           
           this._config=new Configuration();
           this._hostNames=_config.getHostNames();
           this._clientNames=_config.getClientNames();
           
           this._hosts=new Host[_config.getHostNames().size()];
           this._clients=new WebClient[_config.getClientNames().size()];
           
           
           initializeRateGenerators(); 
           initializeVmLifetimeGenerators();
          
           initializeHostObjects();
           initializeSlots();
           addVMEvents();
           this._controller=new Controller(_hosts,_clients,_config,_slots); 
           this.initializeClientObjects();
         
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
       



        

    private void initializeHostObjects() {
        
            for (int i = 0; i < _hosts.length; i++) {
                _hosts[i]=new Host(i,_config,_hostNames.get(i));
            }
            
            

    }
    
    private void initializeClientObjects() {
    
        for (int i = 0; i < _clients.length; i++) {
                _clients[i]=new WebClient(_config,i,_clientNames.get(i),_controller);
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
            
        if(lifetime<1)
                lifetime=1;
        
         // Slot calculation
        int slotDistance= calculateSlotsAway(providerID);
        
        slot2AddVM=currentSlot+slotDistance;
        slot2RemoveVM=slot2AddVM+lifetime;
        
        if(slot2AddVM<_config.getNumberOfSlots()){
            
            VMRequest newRequest = new VMRequest(_config,providerID,lifetime);

            newRequest.setVmType(Utilities.determineVMType(providerID,_config));
            newRequest.setService(Utilities.determineVMService(providerID,_config));


            //add vm during this slot
            newRequest.setSlotStart(slot2AddVM);
             _slots[slot2AddVM].getVmRequests2Activate()[providerID].add(newRequest);
             
             //remove vm during this slot
            newRequest.setSlotEnd(slot2RemoveVM);
            if(slot2RemoveVM<_config.getNumberOfSlots())
                _slots[slot2RemoveVM].getVmRequests2Remove()[providerID].add(newRequest); 
        }
        else
            System.out.println("failed to add request" );
         
        
        
         return slot2AddVM;

    }

    
    private int calculateSlotsAway(int providerID) {
     
    		
       int interArrivalTime=-1;

       double min=0;
       double max=0;
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
               min= Double.valueOf(_config.getVmRequestRateConfig()[providerID].get("min").toString());
               max= Double.valueOf(_config.getVmRequestRateConfig()[providerID].get("max").toString());
                
               interArrivalTime = Utilities.randInt((int)min, (int)max);
                break;
           
           
            default:            
                break;
        }
        

        return interArrivalTime;
        
        }
   
     private int calculateVMLifeTime(int providerID) {
     
    		
        int lifetime=-1;

        double min=0;
        double max=0;
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
               min= Double.valueOf(_config.getVmLifeTimeConfig()[providerID].get("min").toString());
               max= Double.valueOf(_config.getVmLifeTimeConfig()[providerID].get("max").toString());
                
               lifetime = Utilities.randInt((int)min, (int)max);
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
