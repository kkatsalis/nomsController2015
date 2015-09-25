/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import Controller.Configuration;
import Statistics.NetRateStats;
import Statistics.VMStats;
import com.google.gson.JsonArray;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
/**
 *
 * @author kostas
 */
public class WebUtilities {
    
    Configuration _config;
    
    public WebUtilities(Configuration config){
    
        this._config=config;
            
    }
    
    public boolean createVM(Hashtable parameters) throws IOException{
    
       //http://nitlab3.inf.uth.gr:4100/vm-create/server-john/precise/small/192.168.100.10/255.255.254.0/192.168.100.1/node
        
        String uri="http://"+_config.getNitosServer()+".inf.uth.gr:4100/vm-create/";
        boolean methodResponse=false;
        
        String vmName=String.valueOf(parameters.get("vmName"));
        String OS=String.valueOf(parameters.get("OS")); 
        String vmType=String.valueOf(parameters.get("vmType")); 
        String interIP=String.valueOf(parameters.get("interIP")); 
        String interMask=String.valueOf(parameters.get("interMask")); 
        String interDefaultGateway=String.valueOf(parameters.get("interDefaultGateway")); 
        String hostName=String.valueOf(parameters.get("hostName"));
                
        uri+=vmName+"/";
        uri+=OS+"/";
        uri+=vmType+"/";
        uri+=interIP+"/";
        uri+=interMask+"/";
        uri+=interDefaultGateway+"/";   
        uri+=hostName;
        
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(uri);
        CloseableHttpResponse response = httpclient.execute(httpget);

        try {

            System.out.println(response.getProtocolVersion());
            System.out.println(response.getStatusLine().getStatusCode());
            System.out.println(response.getStatusLine().getReasonPhrase());
            System.out.println(response.getStatusLine().toString());

        } 
        finally {
            response.close();
        }

        if(response.getStatusLine().toString()=="200")
            methodResponse=true;
        
        return methodResponse;
    }
    
    public boolean startVM(String vmName, String hostName) throws IOException{
    
        //http://nitlab3.inf.uth.gr:4100/vm-start/server-john
        
        String uri="http://"+_config.getNitosServer()+".inf.uth.gr:4100/vm-start/";
        String methodResponse="";
        
        uri+=vmName;
        uri+="/"+hostName;
        
        
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(uri);
        CloseableHttpResponse response = httpclient.execute(httpget);

        try {

//            System.out.println(response.getProtocolVersion());
//            System.out.println(response.getStatusLine().getStatusCode());
//            System.out.println(response.getStatusLine().getReasonPhrase());
//            System.out.println(response.getStatusLine().toString());
//            
            if(response.getStatusLine().toString().contains("200"))
                return true;

        } 
        finally {
            response.close();
        }
        
        return false;
    }
    
    public String deleteVM(String vmName,String hostName) throws IOException{
      
        String uri="http://"+_config.getNitosServer()+".inf.uth.gr:4100/vm-destroy/";
        String methodResponse="";
        
        uri+=vmName;
        uri+="/"+hostName;
        
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(uri);
        
        CloseableHttpResponse response = httpclient.execute(httpget);

        try {

            System.out.println(response.getProtocolVersion());
            System.out.println(response.getStatusLine().getStatusCode());
            System.out.println(response.getStatusLine().getReasonPhrase());
            System.out.println(response.getStatusLine().toString());

        } 
        finally {
            response.close();
        }
        
        return methodResponse;
    }
    
    public List<VMStats> retrieveVMStatsPerHost(String hostName, int slot,int instance) throws IOException{
    
        List<VMStats> vmStatsList=new ArrayList<>();
     
        String uri="http://"+_config.getNitosServer()+".inf.uth.gr:4000/vm/";
        uri+=hostName;
        
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(uri);
        CloseableHttpResponse response = httpclient.execute(httpget);

        try {

            JSONObject body=new JSONObject(response);

            JSONArray vmStatsArray=body.getJSONArray("VMs");
            int vap_number=vmStatsArray.length();

            JSONObject vm ;

            for (int i = 0; i < vap_number; ++i) {
                vm = vmStatsArray.getJSONObject(i);

                vmStatsList.add(new VMStats());

                vmStatsList.get(i).setSlot(slot);
                vmStatsList.get(i).setInstance(instance);
                vmStatsList.get(i).setHostName(hostName);
                vmStatsList.get(i).setDomain_ID(vm.getString("Domain_ID"));
                vmStatsList.get(i).setDomain_name(vm.getString("Domain_name"));
                vmStatsList.get(i).setCPU_ns(vm.getString("CPU_ns"));
                vmStatsList.get(i).setCPU_percentage(vm.getString("CPU_percentage"));
                vmStatsList.get(i).setMem_bytes(vm.getString("Mem_bytes"));  
                vmStatsList.get(i).setMem_percentage(vm.getString("Mem_percentage")); 
                vmStatsList.get(i).setBlock_RDRQ(vm.getString("Block_RDRQ"));
                vmStatsList.get(i).setBlock_WRRQ(vm.getString("Block_WRRQ"));
                vmStatsList.get(i).setNet_RXBY(vm.getString("Net_RXBY"));
                vmStatsList.get(i).setNet_TXBY(vm.getString("Net_TXBY"));
                vmStatsList.get(i).getNetRates().setKbps_in(vm.getDouble("Kbps in"));
                vmStatsList.get(i).getNetRates().setKbps_out(vm.getDouble("Kbps out"));
                vmStatsList.get(i).getNetRates().setInterface(vm.getString("interface"));
                vmStatsList.get(i).getNetRates().setTimeStamp(vm.getString("timestamp"));
                
            }
        } 
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        finally {
            response.close();
        }
    
        return vmStatsList;
    }
    
    public Hashtable retrieveHostStats(String hostName, int slot) throws IOException, JSONException{
    
        List<NetRateStats> netRates=new ArrayList<>();
        Hashtable parameters=new Hashtable();
        
        String uri="http://"+_config.getNitosServer()+".inf.uth.gr:4100/host/";
        uri+=hostName;
        
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(uri);
        CloseableHttpResponse response = httpclient.execute(httpget);
        
        
        System.out.println(response.getProtocolVersion());
        System.out.println(response.getStatusLine().getStatusCode());
        System.out.println(response.getStatusLine().getReasonPhrase());
        System.out.println(response.getStatusLine().toString());
  
        try {

            String json="";
            String output;
            BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
            
             while ((output = br.readLine()) != null) {
               json+=output;
            }
             
            JSONObject body=new JSONObject(json);
             
            body.getString("hw_mode");
                                 
            JSONArray interfacesArray=body.getJSONArray("net rate");
           
            int interfacesNumber=interfacesArray.length();
             
            JSONObject interfaceStats ;
            
            for (int i = 0; i < interfacesNumber; ++i) {
                interfaceStats = interfacesArray.getJSONObject(i);
            
                netRates.add(new NetRateStats());
           
                netRates.get(i).setInterface(interfaceStats.getString("interface"));
                netRates.get(i).setTimeStamp(interfaceStats.getString("timestamp"));
                netRates.get(i).setKbps_in(interfaceStats.getDouble("Kbps in"));
                netRates.get(i).setKbps_out(interfaceStats.getDouble("Kbps out"));
            }
            
            
            parameters.put("slot",slot);
            parameters.put("Hostname", hostName);
            parameters.put("Time",body.getString("Time") );
            parameters.put("Arch",body.getString("Arch") );
            parameters.put("Physical_CPUs",body.getString("Physical_CPUs") );
            parameters.put("Count",body.getString("Count") );
            parameters.put("Running",body.getString("Running") );
            parameters.put("Blocked",body.getString("Blocked") );
            parameters.put("Paused",body.getString("Paused") );
            parameters.put("Shutdown",body.getString("Shutdown") );
            parameters.put("Shutoff",body.getString("Shutoff") );
            parameters.put("Crashed",body.getString("Crashed") );
            parameters.put("Active",body.getString("Active") );
            parameters.put("Inactive",body.getString("Inactive") );
            parameters.put("CPU_percentage",body.getString("CPU_percentage") );
            parameters.put("Total_hardware_memory_KB",body.getString("Total_hardware_memory_KB") );
            parameters.put("Total_memory_KB",body.getString("Total_memory_KB") );
            parameters.put("Total_guest_memory_KB",body.getString("Total_guest_memory_KB") );
            parameters.put("netRates",netRates);
            
            return parameters;
        } 
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        finally {
            response.close();
        }
        
       return null;
     
    }
    
    public boolean checkVMListOnHost(String hostName, String vmName) throws IOException{
    
         
            String uri="http://"+_config.getNitosServer()+".inf.uth.gr:4100/virsh_list_all/";
            uri+=hostName;
            
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpGet httpget = new HttpGet(uri);
            CloseableHttpResponse response = httpclient.execute(httpget);
            
            String json="";
            String output;
            BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
            
            while ((output = br.readLine()) != null) {
                if(output.contains(vmName))
                return true;
            }
            
            return false;
        
    }
    
    
    
    
}
