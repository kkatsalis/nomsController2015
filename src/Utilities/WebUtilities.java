/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.io.IOException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 *
 * @author kostas
 */
public class Utilities {
    
    public String createVM(String name,String OS, String type, String interIP, String interMask, String interDefaultGateway, String host) throws IOException{
    
       //http://nitlab3.inf.uth.gr:4100/vm-create/server-john/precise/small/192.168.100.10/255.255.254.0/192.168.100.1/node
        
        String uri="http://nitlab3.inf.uth.gr:4100/vm-create/";
        String methodResponse="";
        
        uri+=name+"/";
        uri+=OS+"/";
        uri+=type+"/";
        uri+=interIP+"/";
        uri+=interMask+"/";
        uri+=interDefaultGateway+"/";   
        uri+=host;
        
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
    
    public String startVM(String name) throws IOException{
    
        //http://nitlab3.inf.uth.gr:4100/vm-start/server-john
        
        String uri="http://nitlab3.inf.uth.gr:4100/vm-start/";
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
    
    public String deleteVM(String name) throws IOException{
      
        String uri="http://nitlab3.inf.uth.gr:4100/vm-destroy/";
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
}
