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
import Statistics.NetRateStats;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import jsc.distributions.Exponential;
import jsc.distributions.Pareto;
import org.json.JSONException;

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

        startControllerInternalTimer(); // for Statistics updates
 
        try {
            //      _webUtilities.startVM("kostas","node080");
            
            

//       retrieveHostStatistics();
//       retrieveActiveVMStatistics();
//       retrieveClientStatistics();
//       
//       runScheduler();
//       deleteVMs();
//       updateActiveVMsObjectsPerHost();
            for (int i = 0; i < 10; i++) {
                 List<VMRequest> list2Create=translateTheOptSolutionToVMRequets(slot, nodeName);
            }
   
            createVMs();
//       startVMs();
//       updateProviderStats();
//       startWebClients();
            
        } catch (JSONException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    private List<VMRequest> translateTheOptSolutionToVMRequets(int slot, String nodeName){

        List<VMRequest> list2Create=new ArrayList<>();
    
        return list2Create;
    }
    
    
    private void createVM(VMRequest request,String nodeName) throws IOException {
     
        Hashtable vmParameters;
        
        boolean vmCreated=false;
        boolean vmCreateCommandSend=false;
        
       
                vmCreated=false;
        
                System.out.println("provider:"+request.providerID+" - activate: "+request.getRequestID());
                
                //Step 1: Add VM on the Physical node
                vmParameters=Utilities.determineVMparameters(request,"node080");
                
                while(!vmCreated){
                    
                    vmCreateCommandSend=_webUtilities.createVM(vmParameters);
                        
                    if(vmCreateCommandSend){
                        vmCreated=_webUtilities.checkVMListOnHost(nodeName,String.valueOf(vmParameters.get("vmName")));
                    
                    }
                    
                   
                }
                
                //Step 2: Add VM on the Active VMs List on the Host Object
               
                //Step 3: Update provider Statistics
              
     
     
     }
    
    private void deleteVMs() {

       //Step 1: update Statistics Before Remove 
       
        
       //Step 2: remove from List VM

       
       //Step 3: remove VM Object
        
    
    }

    private void startControllerInternalTimer() {
           
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

    private void updateAllHostStatistics() throws IOException, JSONException {
        int statsNumber=0;
        
        for (int i = 0; i < _hosts.length; i++) {
            _hosts[i].getHostStats().add(new HostStats());
            statsNumber=_hosts[i].getHostStats().size();
            
            Hashtable parameters=_webUtilities.retrieveHostStats(_hosts[i].getNodeName(), slot);
            
            
            _hosts[i].getHostStats().get(statsNumber-1).setSlot(Integer.valueOf(String.valueOf(parameters.get("slot"))));
            _hosts[i].getHostStats().get(statsNumber-1).setHostname(String.valueOf(parameters.get("Hostname")));
            _hosts[i].getHostStats().get(statsNumber-1).setTime(String.valueOf(parameters.get("Time")));
            _hosts[i].getHostStats().get(statsNumber-1).setArch(String.valueOf(parameters.get("Arch")));
            _hosts[i].getHostStats().get(statsNumber-1).setPhysical_CPUs(String.valueOf(parameters.get("Physical_CPUs")));
            _hosts[i].getHostStats().get(statsNumber-1).setCount(String.valueOf(parameters.get("Count")));
            _hosts[i].getHostStats().get(statsNumber-1).setRunning(String.valueOf(parameters.get("Running")));
            _hosts[i].getHostStats().get(statsNumber-1).setBlocked(String.valueOf(parameters.get("Blocked")));
            _hosts[i].getHostStats().get(statsNumber-1).setPaused(String.valueOf(parameters.get("Paused")));
            _hosts[i].getHostStats().get(statsNumber-1).setShutdown(String.valueOf(parameters.get("Shutdown")));
            _hosts[i].getHostStats().get(statsNumber-1).setShutoff(String.valueOf(parameters.get("Shutoff")));
            _hosts[i].getHostStats().get(statsNumber-1).setCrashed(String.valueOf(parameters.get("Crashed")));
            _hosts[i].getHostStats().get(statsNumber-1).setActive(String.valueOf(parameters.get("Active")));
            _hosts[i].getHostStats().get(statsNumber-1).setInactive(String.valueOf(parameters.get("Inactive")));
            _hosts[i].getHostStats().get(statsNumber-1).setCPU_percentage(String.valueOf(parameters.get("CPU_percentage")));
            _hosts[i].getHostStats().get(statsNumber-1).setTotal_hardware_memory_KB(String.valueOf(parameters.get("Total_hardware_memory_KB")));
            _hosts[i].getHostStats().get(statsNumber-1).setTotal_memory_KB(String.valueOf(parameters.get("Total_memory_KB")));
            _hosts[i].getHostStats().get(statsNumber-1).setTotal_guest_memory_KB(String.valueOf(parameters.get("Total_guest_memory_KB")));                    
             
            List<NetRateStats> interfaces=(List<NetRateStats>)parameters.get("netRates");
            
            for (int j = 0; j < interfaces.size(); j++) {
                _hosts[i].getHostStats().get(statsNumber-1).getNetRates().add(new NetRateStats());
                
                _hosts[i].getHostStats().get(statsNumber-1).getNetRates().get(0).setInterface(interfaces.get(j).getInterface());
                _hosts[i].getHostStats().get(statsNumber-1).getNetRates().get(0).setTimeStamp(interfaces.get(j).getTimeStamp());
                _hosts[i].getHostStats().get(statsNumber-1).getNetRates().get(0).setKbps_in(interfaces.get(j).getKbps_in());
                _hosts[i].getHostStats().get(statsNumber-1).getNetRates().get(0).setKbps_out(interfaces.get(j).getKbps_out());
            }
          
        }
        
        
    }

    
    class ExecuteControllerTimer extends TimerTask {

            public void run() {
             
                if(_currentInstance<_maxControlInstances){
                    
                    try {
                        
                        updateAllHostStatistics();
                        updateVMStatistics(_currentInstance);
                        
                        _currentInstance++;
                        
                    } catch (IOException ex) {
                        Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (JSONException ex) {
                        Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                    }
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
