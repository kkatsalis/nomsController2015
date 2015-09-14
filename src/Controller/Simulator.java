/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author kostas
 */
public class Simulator {
    /**
     * @param args the command line arguments
     */
    
       Configuration config;
       List<String> nodes;
       Controller controller;
        
       List<String>[] _vmAllocations;    // A list with VMs per Host Machine;
       
       Simulator(){
           
           config=new Configuration();
           config.loadProperties();
           nodes=new ArrayList<String>();
           
           addHostNodes();
           controller=new Controller(nodes,config);
           _vmAllocations=new List[nodes.size()];     // A list with VMs per Host Machine;
           
       }
      
        
       public void addHostNodes(){
       
           nodes.add("node050");
           nodes.add("node055");
       }
        

}
