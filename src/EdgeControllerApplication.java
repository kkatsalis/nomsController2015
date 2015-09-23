/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import Controller.Simulator;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kostas
 */
public class EdgeControllerApplication {
    
     public static void main(String[] args) {
     
        List<String> clients=new ArrayList<>();
        List<String> hosts=new ArrayList<>();
        List<String> services=new ArrayList<>();
        List<String> vmTypes=new ArrayList<>();
        
        addHostNodes(hosts);
        addClientNodes(clients);
        addServices(services);
        addVmTypes(vmTypes);
        
        Simulator simulator=new Simulator(hosts,clients,services,vmTypes);
        simulator.StartExperiment();
      
     
     }
     
      private static void addHostNodes(List<String> hosts){
            hosts.add("node050");
            hosts.add("node055");
        }

        private static void addClientNodes(List<String> clients) {
            clients.add("node058");
            clients.add("node070");
        }

        private static void addServices(List<String> services) {
           services.add("ab");
           services.add("vlc");

        }
    
        private static void addVmTypes(List<String> vmTypes) {
           vmTypes.add("Small");
           vmTypes.add("Medium");
           vmTypes.add("Large");

        }
}
