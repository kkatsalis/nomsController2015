/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Enumerators.EMachineTypes;
import Enumerators.ESlotDurationMetric;
import Statistics.DBClass;
import Utilities.WebUtilities;
import Statistics.VMStats;
import Statistics.HostStats;
import Statistics.NetRateStats;
import Statistics.ProviderStats;
import Utilities.Scheduler;
import Utilities.SchedulerData;
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
import omlBasePackage.OMLMPFieldDef;
import omlBasePackage.OMLTypes;
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
    
   
    
    ProviderStats[] _providerStats;
    HostStats[] _hostStats;
    List<VMStats> _activeVMStats;
    
    WebUtilities _webUtilities;
    DBClass _db;
    
    Timer _machineStatsTimer;
    int maxUpdateInstance=0; 
    int _currentInstance=0; 
    int vmIDs=0;
    
    SchedulerData cplexData;
    Scheduler scheduler;
    
    Controller(Host[] hosts,WebClient[] clients, Configuration config, Slot[] slots, DBClass db) {
        
        this._config=config;
        this._slots=slots;
        this._hosts=hosts;
        this._clients=clients;
        this._db=db;
        this._providerStats=new ProviderStats[config.getProvidersNumber()];
        this._webUtilities=new WebUtilities(config);
       
        
        this._hostStats=new HostStats[_config.getHostsNumber()];
        this._activeVMStats=new ArrayList<>();
        this.maxUpdateInstance=_config.getNumberOfMachineStatsPerSlot();
       

    }

    void Run(int slot) throws IOException {

        System.out.println("------- Slot:"+slot);

        startStatsUpdateTimer(slot); // for Statistics updates
 
        try {

            
//            double[][][] vmRequestMatrix=loadVMRequestMatrix(slot);             //requestMatrix[v][s][p]
//            double[][][][] vmDeactivationMatrix=loadVmDeactivationMatrix(slot);
//            
//            deleteVMs(slot); 
//            double[] requestPattern=Utilities.findRequestPattern(_config);
//        
//                    
//          cplexData=new SchedulerData(_config,requestPattern, vmRequestMatrix, vmDeactivationMatrix);
//          scheduler=new Scheduler(_config);
//            
//          int[][][][] activationMatrix=scheduler.Run(cplexData);
          
        

//            int[][][][] activationMatrix =tempScheduler(vmRequestMatrix); // activationMatrix[i][j][v][s]: # of allocated VMs of v v for service s of provider j at AP i
            
            // Create the VMs
//            VMRequest request=null;
//            LoadVM loadObject;
//            Thread thread;
//            
//            
//            for (int i = 0; i < _config.getHostsNumber(); i++) {
//                 List<VMRequest> vm2CreatePerHost=cplexSolution2VMRequets(slot, _hosts[i].getHostID(),activationMatrix);
//
//                 System.out.println("Bring up: "+vm2CreatePerHost.size()+"VMs");
//                 
//                 for (Iterator iterator = vm2CreatePerHost.iterator(); iterator.hasNext();) {
//                    request = (VMRequest) iterator.next();
//
//                    loadObject=new LoadVM(slot,request,_hosts[i].getNodeName());
//                    thread = new Thread(loadObject);
//                    thread.start();
//                    Thread.sleep(5000);
//
//                }
//
//            }
   

//       startWebClients();
            
        } catch (Exception ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    // n[i][j][v][s]: # of allocated VMs of v v for service s of provider j at AP i
    private List<VMRequest> cplexSolution2VMRequets(int slot, int hostID, int[][][][] activationMatrix){

        int vms2Load=-1;
                
        List<VMRequest> vmList2Create=new ArrayList<>();
        List<VMRequest> listOfRequestedVMs;

        String _vmType="";
        String _serviceType="";
        int totalVms2Load=0;
        
        for (int p = 0; p < _config.getProvidersNumber(); p++) {
            
            listOfRequestedVMs=_slots[slot].getVmRequests2Activate()[p];
            
            for (int v = 0; v < _config.getVmTypesNumber(); v++) {
                
                if(v==0)
                    _vmType=EMachineTypes.small.toString();
                else if(v==1)
                    _vmType=EMachineTypes.medium.toString();
                else if(v==2)
                    _vmType=EMachineTypes.large.toString();
               
                for (int s = 0; s < _config.getServicesNumber(); s++) {
                    
                    if(s==0)
                        _serviceType="AB";
                    else if(s==1)
                        _serviceType="VLC";
                      
                    vms2Load=activationMatrix[hostID][p][v][s];
                    totalVms2Load+=vms2Load;
                    
                    for(Iterator iterator = listOfRequestedVMs.iterator(); iterator.hasNext();) {
                       VMRequest nextRequest = (VMRequest)iterator.next();
         
                       if(nextRequest.getServiceType().equals(_serviceType)&nextRequest.getVmType().equals(_vmType))
                       {
                           if(vms2Load>0){
                                vmList2Create.add(nextRequest);
                                vms2Load--;
                           }
                       }
                           
                    }
                }
           }
    
        }
   
        // Returns A list of VMs to activate for a specific host
        return vmList2Create;
    }
    
    private void deleteVMs(int slot) throws IOException, InterruptedException {

       System.out.println("Delete VMs Called - slot:"+slot);
       
       List<Integer> requestID2RemoveThisSlot=new ArrayList<>();
       
       // Step 1: Find RequestIDs to remove
       for (int i = 0; i < _config.getProvidersNumber(); i++) {
           for (int j = 0; j < _slots[slot].getVmRequests2Remove()[i].size(); j++) {
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
      
       Thread thread; 
       //Step 3: Delete the VM
       for (int i = 0; i < _config.getHostsNumber(); i++) {
            hostName=_hosts[i].getNodeName();
            
            for (int j = 0; j < _hosts[i].getVMs().size(); j++) {
                
                if(requestID2RemoveThisSlot.contains(_hosts[i].getVMs().get(j).getVmReuestId())&!_hosts[i].getVMs().get(j).isActive()){
                    vmName=_hosts[i].getVMs().get(j).getName();
                    
                    DeleteVM deleter=new DeleteVM(vmName,hostName);
                    thread=new Thread(deleter);
                    thread.start();
                    Thread.sleep(5000);
                    
            }   
           }
       }
    
    }

   
    private double[][][] loadVMRequestMatrix(int slot) {
        
        double[][][] requestMatrix=new double[_config.getVmTypesNumber()][_config.getServicesNumber()][_config.getProvidersNumber()];//: # of allocated VMs of v v for service s of provider j at AP i
        List<VMRequest> listOfRequestedVMs=null;
        int v=-1;
        int s=-1;
        
        for (int p = 0; p < _config.getProvidersNumber(); p++) {
            
            listOfRequestedVMs=_slots[slot].getVmRequests2Activate()[p];
            
            for (VMRequest nextRequest : listOfRequestedVMs) {
                if(EMachineTypes.small.toString().equals(nextRequest.getVmType()))
                    v=0;
                else if(EMachineTypes.medium.toString().equals(nextRequest.getVmType()))
                    v=1;
                else if(EMachineTypes.large.toString().equals(nextRequest.getVmType()))
                    v=2;
                
                if("AB".equals(nextRequest.getServiceType()))
                    s=0;
                else if("VLC".equals(nextRequest.getServiceType()))
                    s=1;
                
                requestMatrix[v][s][p]++;
            }     
            
        }
        
        return requestMatrix;
    }

    

    
    
    
    private double[][][][] loadVmDeactivationMatrix(int slot) {
       
       int N=_config.getHostsNumber();
       int P=_config.getProvidersNumber();
       int V=_config.getVmTypesNumber();
       int S=_config.getServicesNumber();
       
       List<VMRequest> vmRequests2RemoveThisSlot=Utilities.findVMequests2RemoveThisSlot(slot,_slots,_config);
       List<VM> activeVMsThisSlot=Utilities.findActiveVMs(slot, _hosts);
      
       
       double[][][][] deactivationMatrix = new double [N][P][V][S]; // D[i][j][v][s]: # of removed VMs of v v for service s of provider j from AP i 
        
       for (int i=0;i<N;i++)
          for (int j=0;j<P;j++)
            for (int v=0;v<V;v++)
                for (int s=0;s<S;s++)
		{
                    deactivationMatrix[i][j][v][s]=0;
		}
       
       
        int hostID=-1;
        int providerID=-1;
        int vmTypeID=-1;
        int serviceID=-1;
        
        for (Iterator iterator = vmRequests2RemoveThisSlot.iterator(); iterator.hasNext();) {
            VMRequest next = (VMRequest)iterator.next();
            
            for (Iterator iterator1 = activeVMsThisSlot.iterator(); iterator1.hasNext();) {
                VM nextVM = (VM)iterator1.next();
                
                if(nextVM.getVmReuestId()==next.getRequestID()){ // then this Vm must be deactivated
                
                    hostID=Utilities.findHostID(_config, nextVM.getHostname());
                    providerID=nextVM.getProviderID();
                    
                    switch (next.getVmType()){
                        case "small":
                            vmTypeID=0;
                        case "medium":
                            vmTypeID=1;
                        case "large":
                            vmTypeID=2;  
                    }
            
                    switch (next.getServiceType()){
                        case "AB":
                            serviceID=0;
                        case "VLC":
                            serviceID=1;
                    }
                    
                    deactivationMatrix[hostID][providerID][vmTypeID][serviceID]++;
                }
                    
            }
            
          
         
            
            
        }
        
        return deactivationMatrix;
    }

    private int[][][][] tempScheduler(double[][][] vmRequestMatrix) {
        // activationMatrix[i][j][v][s]: # of allocated VMs of v v for service s of provider j at AP i
         //requestMatrix[v][s][p]
        
        int[][][][] activationMatrix=new int[_config.getHostsNumber()][_config.getProvidersNumber()][_config.getVmTypesNumber()][_config.getServicesNumber()];
        
        for (int j = 0; j < _config.getProvidersNumber(); j++) {
            for (int v = 0; v < _config.getVmTypesNumber(); v++) {
                for (int s = 0; s < _config.getServicesNumber(); s++) {
                    activationMatrix[0][j][v][s]=(int)vmRequestMatrix[v][s][j];
                   
                }
            }
   
        }
        
        return activationMatrix;
        
        
        
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

          
        public boolean isDeleted() {
            return deleted;
        }
           
           

    
    }
    
    public class LoadVM implements Runnable{
        
        private Thread _thread;
        private final String threadName;
        private final String hostName;
        VMRequest request;
        
        public boolean loaded=false;
        int slot;
        Hashtable hostVMpair;
         
        LoadVM(int slot,VMRequest request,String hostName){
        
           this.slot=slot;
           this.hostName=hostName;
           this.request=request;
           this.threadName = hostName+"-"+request.getRequestID();
           this. hostVMpair=new Hashtable();
           
           System.out.println("Creating " +  threadName );
        }
        
        public void run() {
              
            try {
                
                    System.out.println("Load VM Thread: " + threadName + " started");
                    
                    createVM(slot,request,hostName); 
                    startVM(request,hostName);
                    createVMobject(slot,request,hostName);
                    
                    Thread.sleep(0);
                 
             } catch (Exception e) {
                 System.out.println("Thread " +  threadName + " interrupted.");
             }
            
              System.out.println("Delete Thread " +  threadName + " finished.");
             loaded=true;
           }

        

       
        private boolean createVM(int slot,VMRequest request,String nodeName) throws IOException {
     
            Hashtable vmParameters;

            boolean vmCreated=false;
            boolean vmCreateCommandSend=false;
        
       
            System.out.println("provider:"+request.providerID+" - activate: "+request.getRequestID());

            //Step 1: Add VM on the Physical node
            vmParameters=Utilities.determineVMparameters(request,nodeName);
            vmCreateCommandSend=_webUtilities.createVM(vmParameters);

 
            return vmCreateCommandSend;
     
     
    }

        private void startVM(VMRequest request, String hostName) throws IOException {
          
            Boolean vmCreated=false;
            Hashtable  vmParameters=Utilities.determineVMparameters(request,hostName);
            int counter=0;
            
            while(!vmCreated){
                
                vmCreated=_webUtilities.checkVMListOnHost(hostName,String.valueOf(vmParameters.get("vmName")));
                System.out.println("Bring VM up attempt:"+ counter+"-requestID:"+request.getRequestID());
                
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
    // handle the exception...        
    // For example consider calling Thread.currentThread().interrupt(); here.
}
                counter++;
            }

            String vmName=String.valueOf(vmParameters.get("vmName"));
            _webUtilities.startVM(vmName,hostName);
            
        }

        private void createVMobject(int slot, VMRequest request, String hostName) {
           
            Hashtable  vmParameters=Utilities.determineVMparameters(request,hostName);
            
            for (int i = 0; i < _hosts.length; i++) {
                    if(_hosts[i].getNodeName().equals(hostName))
                    {
                        _hosts[i].getVMs().add(new VM(vmParameters,request,slot,vmIDs,_hosts[i].getNodeName()));
                        vmIDs++;
                    }
                }
        }
        
  }
    
    class MachineStatsTimer extends TimerTask {

         int slot;
         
         MachineStatsTimer(int slot){
            this.slot=slot;
         }
         
         public void run() {
             
            if(_currentInstance<maxUpdateInstance){
                    
                try {
                        
                    updateAllHostStatistics(slot,_currentInstance);
                    updateAllHostStatisticsDB(slot,_currentInstance);
                    updateAllHostStatisticsInterfacesDB(slot,_currentInstance);
                 //   updateActiveVMStatistics(slot,_currentInstance);
                        
                        _currentInstance++;
                        
                } catch (IOException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                } catch (JSONException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                }
              }
              else{
                _machineStatsTimer.cancel();
              }
              
            }

        
    }
    
    private void startStatsUpdateTimer(int slot) {
           
        int statsUpdateInterval=_config.getSlotDuration()/_config.getNumberOfMachineStatsPerSlot();

            _machineStatsTimer = new Timer();
            _currentInstance=0;
            
            if(_config.getSlotDurationMetric().equals(ESlotDurationMetric.milliseconds.toString()))
               _machineStatsTimer.scheduleAtFixedRate(new MachineStatsTimer(slot),0 ,statsUpdateInterval);
            else if(_config.getSlotDurationMetric().equals(ESlotDurationMetric.seconds.toString()))
               _machineStatsTimer.scheduleAtFixedRate(new MachineStatsTimer(slot),0 ,statsUpdateInterval*1000);
            else if(_config.getSlotDurationMetric().equals(ESlotDurationMetric.minutes.toString()))
               _machineStatsTimer.scheduleAtFixedRate(new MachineStatsTimer(slot),0 ,60*statsUpdateInterval*1000);
            else if(_config.getSlotDurationMetric().equals(ESlotDurationMetric.hours.toString()))
               _machineStatsTimer.scheduleAtFixedRate(new MachineStatsTimer(slot),0 ,3600*statsUpdateInterval*1000);
    }

  
    //************************************
    //         Statistics Update
    //************************************
    private void updateAllHostStatistics(int slot, int measurement) throws IOException, JSONException {
       
        int statsNumber=0;
        
        for (int i = 0; i < _hosts.length; i++) {
            _hosts[i].createNewStatsObject();
           
            
            Hashtable parameters=_webUtilities.retrieveHostStats(_hosts[i].getNodeName(), slot,measurement);
            
            if(parameters!=null){
                _hosts[i].getHostStats().setSlot(String.valueOf(parameters.get("slot")));
                _hosts[i].getHostStats().setMeasurement(String.valueOf(parameters.get("measurement")));
                _hosts[i].getHostStats().setHostname(String.valueOf(parameters.get("Hostname")));
                _hosts[i].getHostStats().setTime(String.valueOf(parameters.get("Time")));
                _hosts[i].getHostStats().setArch(String.valueOf(parameters.get("Arch")));
                _hosts[i].getHostStats().setPhysical_CPUs(String.valueOf(parameters.get("Physical_CPUs")));
                _hosts[i].getHostStats().setCount(String.valueOf(parameters.get("Count")));
                _hosts[i].getHostStats().setRunning(String.valueOf(parameters.get("Running")));
                _hosts[i].getHostStats().setBlocked(String.valueOf(parameters.get("Blocked")));
                _hosts[i].getHostStats().setPaused(String.valueOf(parameters.get("Paused")));
                _hosts[i].getHostStats().setShutdown(String.valueOf(parameters.get("Shutdown")));
                _hosts[i].getHostStats().setShutoff(String.valueOf(parameters.get("Shutoff")));
                _hosts[i].getHostStats().setCrashed(String.valueOf(parameters.get("Crashed")));
                _hosts[i].getHostStats().setActive(String.valueOf(parameters.get("Active")));
                _hosts[i].getHostStats().setInactive(String.valueOf(parameters.get("Inactive")));
                _hosts[i].getHostStats().setCPU_percentage(String.valueOf(parameters.get("CPU_percentage")));
                _hosts[i].getHostStats().setTotal_hardware_memory_KB(String.valueOf(parameters.get("Total_hardware_memory_KB")));
                _hosts[i].getHostStats().setTotal_memory_KB(String.valueOf(parameters.get("Total_memory_KB")));
                _hosts[i].getHostStats().setTotal_guest_memory_KB(String.valueOf(parameters.get("Total_guest_memory_KB")));                    

                List<NetRateStats> interfaces=(List<NetRateStats>)parameters.get("netRates");

                for (int j = 0; j < interfaces.size(); j++) {
                    _hosts[i].getHostStats().getInterfacesRates().add(new NetRateStats());

                    _hosts[i].getHostStats().getInterfacesRates().get(j).setInterface(interfaces.get(j).getInterface());
                    _hosts[i].getHostStats().getInterfacesRates().get(j).setTimeStamp(interfaces.get(j).getTimeStamp());
                    _hosts[i].getHostStats().getInterfacesRates().get(j).setKbps_in(interfaces.get(j).getKbps_in());
                    _hosts[i].getHostStats().getInterfacesRates().get(j).setKbps_out(interfaces.get(j).getKbps_out());
                }
            }
            
        }
        
        
    }
    private void updateAllHostStatisticsDB(int slot,int _currentInstance){
        
        for (int i = 0; i < _hosts.length; i++) {
              
            String[] data = { 
                String.valueOf(slot),
                String.valueOf(_currentInstance),
                _hosts[i].getHostStats().getTime(),
                _hosts[i].getHostStats().getHostName(),
                _hosts[i].getHostStats().getArch(),
                _hosts[i].getHostStats().getPhysical_CPUs(),
                _hosts[i].getHostStats().getCount(),
                _hosts[i].getHostStats().getRunning(),
                _hosts[i].getHostStats().getBlocked(),
                _hosts[i].getHostStats().getPaused(),
                _hosts[i].getHostStats().getShutdown(),
                _hosts[i].getHostStats().getShutoff(),
                _hosts[i].getHostStats().getCrashed(),
                _hosts[i].getHostStats().getActive(),
                _hosts[i].getHostStats().getInactive(),
                _hosts[i].getHostStats().getCPU_percentage(),
                _hosts[i].getHostStats().getTotal_hardware_memory_KB(),
                _hosts[i].getHostStats().getTotal_memory_KB(),
                _hosts[i].getHostStats().getTotal_guest_memory_KB()
              
             };
            
            _db.getMp_hostStats().inject(data);
    
        }
    
    
    
    }
    private void updateAllHostStatisticsInterfacesDB(int slot, int measurement){
    
        for (int i = 0; i < _hosts.length; i++) {
            
            List<NetRateStats> interfaces=_hosts[i].getHostStats().getInterfacesRates();

                for (int j = 0; j < interfaces.size(); j++) {
                    
                    String[] data = { 
                    String.valueOf(slot),
                    String.valueOf(_hosts[i].getNodeName()),    
                    _hosts[i].getHostStats().getInterfacesRates().get(j).getInterface(),
                    _hosts[i].getHostStats().getInterfacesRates().get(j).getTimeStamp(),
                    String.valueOf(_hosts[i].getHostStats().getInterfacesRates().get(j).getKbps_in()),
                    String.valueOf(_hosts[i].getHostStats().getInterfacesRates().get(j).getKbps_out())
                    };
                }
     
    
        }
    
    }
    private void updateActiveVMStatistics(int slot, int currentInstance) throws IOException {
           
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

    
    
    private void updateProviderStatistics() {
        
        
    }
}
