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
import java.util.Iterator;
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
    int[][][][] requestMatrix;
    int[][][][] allocationMatrix;
  
    int vmIDs=0;
    
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
       
        requestMatrix=new int[_config.getVmTypesNumber()][_config.getServicesNumber()][_config.getProvidersNumber()][_config.getHostsNumber()];//: # of allocated VMs of type v for service s of provider j at AP i
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

         deleteVMs(slot);  
 
//       runScheduler();
       
       
            // Start New VMs
            List<VMRequest> list2Create=null;
            VMRequest request=null;
            
            for (int i = 0; i < _config.getHostsNumber(); i++) {
                 list2Create=CplexSolution2VMRequets(slot, _hosts[i].getHostID());
                                  
                 for (Iterator iterator = list2Create.iterator(); iterator.hasNext();) {
                    request = (VMRequest) iterator.next();
                    
                    LoadVM loadVM=new LoadVM(slot,request,_hosts[i].getNodeName());
                           loadVM.start();
                     
                }
                 
            }
   
            

//       startWebClients();
            
        } catch (Exception ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    private List<VMRequest> CplexSolution2VMRequets(int slot, int hostID){

        List<VMRequest> vmList2Create=new ArrayList<>();
    
        return vmList2Create;
    }
    
    
    
    private void deleteVMs(int slot) throws IOException {

     
       int vms2DeleteNumber=0; 
       List<Integer> requestID2RemoveThisSlot=new ArrayList<>();
       
       // Step 1: Find RequestIDs to remove
       for (int i = 0; i < _config.getProvidersNumber(); i++) {
            
           vms2DeleteNumber=_slots[slot].getVmRequests2Remove()[i].size();
           
           for (int j = 0; j < vms2DeleteNumber; j++) {
               requestID2RemoveThisSlot.add(_slots[slot].getVmRequests2Remove()[i].get(j).getRequestID());
           }
        }
       
        // Step 2: Find the VMs based on the requestID to remove and Update Host object
        
        for (int i = 0; i < _config.getHostsNumber(); i++) {
            for (int j = 0; j < _hosts[i].getVMs().size(); j++) {
                if(requestID2RemoveThisSlot.contains(_hosts[i].getVMs().get(j).getVmReuestId())){
                    _hosts[i].getVMs().get(j).setSlotDeactivated(slot);
                    _hosts[i].getVMs().get(j).setActive(false);
                }
            }
        }    
       
       String hostName="";
       String vmName="";
       
       //Step 3: Delete the VM
       for (int i = 0; i < _config.getHostsNumber(); i++) {
            hostName=_hosts[i].getNodeName();
            
            for (int j = 0; j < _hosts[i].getVMs().size(); j++) {
                
                if(requestID2RemoveThisSlot.contains(_hosts[i].getVMs().get(j).getVmReuestId())&_hosts[i].getVMs().get(j).isActive()){
                    vmName=_hosts[i].getVMs().get(j).getName();
                    
                    DeleteVM deleter=new DeleteVM(hostName,vmName);
                    deleter.start();
                    
            }   
           }
       }
    
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

    private void updateProviderStatistics() {
        
        int[][][][] requestMatrix;
        int[][][][] allocationMatrix;
        
    }

    private void updateActiveVMStatistics(int currentInstance) throws IOException {
           
        System.out.println("instance: "+currentInstance);
           
        for (int i = 0; i < _hosts.length; i++) {
            List<VMStats> _allVMStats=_webUtilities.retrieveVMStatsPerHost(_hosts[i].getNodeName(), slot, currentInstance);
            
            for (Iterator<VMStats> iterator = _allVMStats.iterator(); iterator.hasNext();) {
                VMStats next = iterator.next();
                
               //Find the VM and add Stats
                for (int j = 0; j < _hosts[i].getVMs().size(); j++) {
                    if(_hosts[i].getVMs().get(j).getName().equals(next.getDomain_name()))
                        _hosts[i].getVMs().get(j).getStats().add(next);
                }
                
            }
            
        }
           
    }
    
    class ExecuteControllerTimer extends TimerTask {

            public void run() {
             
                if(_currentInstance<_maxControlInstances){
                    
                    try {
                        
                        updateAllHostStatistics();
                        updateActiveVMStatistics(_currentInstance);
                        
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

        
    }
    
    class DeleteVM implements Runnable{
        
        private Thread _thread;
        private String threadName;
        private String hostName;
        private String vmName;
        
        public boolean deleted=false;
        
        DeleteVM(String hostName,String vmName){
        
           this.hostName=hostName;
           this.vmName=vmName;
           threadName = hostName+"-"+vmName;
           
           System.out.println("Creating " +  threadName );
        }
           public void run() {
              
              try {
                
                    System.out.println("Delete Thread: " + threadName + " started");
                    
                    _webUtilities.deleteVM(hostName, vmName);
                    
                    // Let the thread sleep for a while.
                    Thread.sleep(0);
                 
             } catch (Exception e) {
                 System.out.println("Thread " +  threadName + " interrupted.");
             }
            
              System.out.println("Delete Thread " +  threadName + " finished.");
             deleted=true;
           }

           public void start ()
           {
              System.out.println("Starting " +  threadName );
              if (_thread == null)
              {
                 _thread = new Thread (this, threadName);
                 _thread.start ();
              }
           }

        public boolean isDeleted() {
            return deleted;
        }
           
           

    
    }
    
    class LoadVM implements Runnable{
        
        private Thread _thread;
        private String threadName;
        private String hostName;
        VMRequest request;
        
        public boolean loaded=false;
        
        LoadVM(int slot,VMRequest request,String hostName){
        
           this.hostName=hostName;
           this.request=request;
           
           threadName = hostName+"-"+request.getRequestID();
           
           System.out.println("Creating " +  threadName );
        }
           public void run() {
              
              try {
                
                    System.out.println("Load VM Thread: " + threadName + " started");
                    
                     createAndStartVM(slot,request,hostName); 
                    // Let the thread sleep for a while.
                    Thread.sleep(0);
                 
             } catch (Exception e) {
                 System.out.println("Thread " +  threadName + " interrupted.");
             }
            
              System.out.println("Delete Thread " +  threadName + " finished.");
             loaded=true;
           }

           public void start ()
           {
              System.out.println("Starting " +  threadName );
              if (_thread == null)
              {
                 _thread = new Thread (this, threadName);
                 _thread.start ();
              }
           }

        public boolean isLoaded() {
            return loaded;
        }
           
        private boolean createAndStartVM(int slot,VMRequest request,String nodeName) throws IOException {
     
            Hashtable vmParameters;

            boolean vmCreated=false;
            boolean vmCreateCommandSend=false;
        
       
            vmCreated=false;

            System.out.println("provider:"+request.providerID+" - activate: "+request.getRequestID());

            //Step 1: Add VM on the Physical node
            vmParameters=Utilities.determineVMparameters(request,nodeName);

            while(!vmCreated){

                vmCreateCommandSend=_webUtilities.createVM(vmParameters);

                if(vmCreateCommandSend){
                    vmCreated=_webUtilities.checkVMListOnHost(nodeName,String.valueOf(vmParameters.get("vmName")));

                }

            }


            String vmName=String.valueOf(vmParameters.get("vmName"));
            boolean started=_webUtilities.startVM(vmName,nodeName);

            //Step 2: Add VM on the Active VMs List on the Host Object
            if(started){
                for (int i = 0; i < _hosts.length; i++) {
                    if(_hosts[i].getNodeName().equals(nodeName))
                    {
                        _hosts[i].getVMs().add(new VM(vmParameters,request,slot,vmIDs,_hosts[i].getNodeName()));
                        vmIDs++;
                    }
                }



            }

            //Step 3: Update provider Statistics
           updateProviderStatistics();


            if(started)
                return true;
            else 
                return false;
     
     
     }
       

    
    }
    
}
