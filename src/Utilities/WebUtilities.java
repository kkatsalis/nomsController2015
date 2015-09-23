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
    
    public String createVM(Hashtable parameters) throws IOException{
    
       //http://nitlab3.inf.uth.gr:4100/vm-create/server-john/precise/small/192.168.100.10/255.255.254.0/192.168.100.1/node
        
        String uri="http://"+_config.getNitosServer()+".inf.uth.gr:4100/vm-create/";
        String methodResponse="";
        
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
        
        return methodResponse;
    }
    
    public String startVM(String vmName, String hostName) throws IOException{
    
        //http://nitlab3.inf.uth.gr:4100/vm-start/server-john
        
        String uri="http://"+_config.getNitosServer()+".inf.uth.gr:4100/vm-start/";
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
    
    public String deleteVM(String name) throws IOException{
      
        String uri="http://"+_config.getNitosServer()+".inf.uth.gr:4100/vm-destroy/";
        String methodResponse="";
        
        uri+=name;
        
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
    
    public List<VMStats> updateVMStats(String hostName,int instance, int slot) throws IOException{
    
        List<VMStats> statsList=new ArrayList<>();
     
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

                statsList.add(new VMStats());

                statsList.get(i).setSlot(slot);
                statsList.get(i).setSlot(instance);
                statsList.get(i).setDomain_ID(vm.getString("Domain_ID"));
                statsList.get(i).setDomain_name(vm.getString("Domain_name"));
                statsList.get(i).setCPU_ns(vm.getString("CPU_ns"));
                statsList.get(i).setCPU_percentage(vm.getString("CPU_percentage"));
                statsList.get(i).setMem_bytes(vm.getString("Mem_bytes"));  
                statsList.get(i).setMem_percentage(vm.getString("Mem_percentage")); 
                statsList.get(i).setBlock_RDRQ(vm.getString("Block_RDRQ"));
                statsList.get(i).setBlock_WRRQ(vm.getString("Block_WRRQ"));
                statsList.get(i).setNet_RXBY(vm.getString("Net_RXBY"));
                statsList.get(i).setNet_TXBY(vm.getString("Net_TXBY"));
                statsList.get(i).getNetRates().setKbps_in(vm.getDouble("Kbps in"));
                statsList.get(i).getNetRates().setKbps_out(vm.getDouble("Kbps out"));
                statsList.get(i).getNetRates().setInterface(vm.getString("interface"));
                statsList.get(i).getNetRates().setTimeStamp(vm.getString("timestamp"));
                
                
                
                
            }
        } 
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        finally {
            response.close();
        }
    
        return statsList;
    }
    
     public void updateHostStats(String hostName,int instance, int slot) throws IOException{
    
        
        String uri="http://"+_config.getNitosServer()+".inf.uth.gr:4100/host/";
        uri+=hostName;
        
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(uri);
        CloseableHttpResponse response = httpclient.execute(httpget);

        try {

            String json="";
            String output;
            BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
            
             while ((output = br.readLine()) != null) {
               json+=output;
            }
             
            JSONObject body=new JSONObject(json);
             
            System.out.println(response.getProtocolVersion());
            System.out.println(response.getStatusLine().getStatusCode());
            System.out.println(response.getStatusLine().getReasonPhrase());
            System.out.println(response.getStatusLine().toString());

        } 
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        finally {
            response.close();
        }
    
     
    }
    
     
    public void checkVMListOnHost(String hostName){
    
        String uri="http://"+_config.getNitosServer()+".inf.uth.gr:4100/virsh_list_all/";
        uri+=hostName;
    
    
    
    }
    
    
    
    
}
