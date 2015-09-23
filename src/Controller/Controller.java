/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import static Controller.Simulator.slot;
import Enumerators.ESlotDurationMetric;
import Utilities.WebUtilities;
import Statistics.VMStats;
import Statistics.HostStats;
import Statistics.ProviderStats;
import Utilities.Utilities;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import jsc.distributions.Exponential;
import jsc.distributions.Pareto;

/**
 *
 * @author kostas
 */
public class Controller {

    Configuration _config;
    Slot[] _slots;
            
    Host[] _hosts;
    WebClient[] _clients;
    
   
    int _numberOfHosts=0;
    
    ProviderStats[] _providerStats;
    HostStats[] _hostStats;
    List<VMStats> _activeVMStats;
    
    WebUtilities _webUtilities;
    
    Timer _controllerTimer;
    int _maxControlInstances=0; 
    int _currentInstance=0; 
    int[][][][] allocationMatrix;
    
    Controller(Host[] hosts,WebClient[] clients, Configuration config, Slot[] slots) {
        
        this._config=config;
        this._slots=slots;
        this._hosts=hosts;
        this._clients=clients;
        this._numberOfHosts=hosts.length;
        
        this._providerStats=new ProviderStats[config.getProvidersNumber()];
        this._webUtilities=new WebUtilities(config);
       
        
        this._hostStats=new HostStats[_numberOfHosts];
        this._activeVMStats=new ArrayList<>();
        this._maxControlInstances=_config.getStatsUpdatesPerSlot();
        
        
        allocationMatrix=new int[_config.getVmTypesNumber()][_config.getServicesNumber()][_config.getProvidersNumber()][_config.getHostsNumber()];//: # of allocated VMs of type v for service s of provider j at AP i
    }

    void Run(int slot) throws IOException {

        System.out.println("------- Slot:"+slot);

        startControllerTimer();
        
          

        
        
        
 
  //      _webUtilities.startVM("kostas","node080");
          
         _webUtilities.updateHostStats("node080", slot, slot);
        
        
//       deleteVMs();       
//       retrieveHostStatistics();
//       retrieveActiveVMStatistics();
//       retrieveClientStatistics();
//       
//       runScheduler();

//       updateActiveVMsObjectsPerHost();
//       createVMs();
//       startVMs();
//       updateProviderStats();
//       startWebClients();
        
    }

     private void createVMs() throws IOException {
     
        VMRequest request;
        
        for (int i = 0; i < _config.getProvidersNumber(); i++) {
            for (int j = 0; j < _slots[slot].getVmRequests2Activate()[i].size(); i++) {
                request=_slots[slot].getVmRequests2Activate()[i].get(j);

                System.out.println("provider:"+i+" - activate: "+request.getRequestID());
                _webUtilities.createVM(Utilities.determineVMparameters(request,"node080"));

            }    
        }
     
     
     }
    
    private void deleteVMs() {

       //Step 1: update Statistics Before Remove 
       
        
       //Step 2: remove from List VM

       
       //Step 3: remove VM Object
        
    
    }

    private void startControllerTimer() {
           
        int statsUpdateInterval=_config.getSlotDuration()/_config.getStatsUpdatesPerSlot();

            _controllerTimer = new Timer();
            _currentInstance=0;
            
            if(_config.getSlotDurationMetric().equals(ESlotDurationMetric.milliseconds.toString()))
               _controllerTimer.scheduleAtFixedRate(new ExecuteControllerTimer(),0 ,statsUpdateInterval);
            else if(_config.getSlotDurationMetric().equals(ESlotDurationMetric.seconds.toString()))
               _controllerTimer.scheduleAtFixedRate(new ExecuteControllerTimer(),0 ,statsUpdateInterval*1000);
            else if(_config.getSlotDurationMetric().equals(ESlotDurationMetric.minutes.toString()))
               _controllerTimer.scheduleAtFixedRate(new ExecuteControllerTimer(),0 ,60*statsUpdateInterval*1000);
            else if(_config.getSlotDurationMetric().equals(ESlotDurationMetric.hours.toString()))
               _controllerTimer.scheduleAtFixedRate(new ExecuteControllerTimer(),0 ,3600*statsUpdateInterval*1000);
    }

   
    
    
    class ExecuteControllerTimer extends TimerTask {

            public void run() {
             
                if(_currentInstance<_maxControlInstances){
                    updateVMStatistics(_currentInstance);
                 
                   _currentInstance++;
              }
              else{
                _controllerTimer.cancel();
              }
              
            }

        private void updateVMStatistics(int currentInstance) {
           System.out.println("instance: "+currentInstance);
        }
    }
    
    
}
