/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import Controller.Configuration;
import Controller.VMRequest;
import java.util.List;

/**
 *
 * @author kostas
 */
public class SchedulerData {

       public int N; // # of APs
       public int P; // # of service providers
       public int S; // # of services
       public int V; // # of VM types
       public double[] r; // requests per service provider
       public int R; // # of physical resources per physical machine
       public double[] phi; // fairness weight per provider
       public double[][] m; // m[vmtype][res]: amount of each res res for each VM vmtype vmtype
       public double[][] p; // p[host][res]: capacity of each res res at each AP host
       public double[][] pen; // pen[j][s] penalty for not satisfying locally a request for service s of provider j
       public double[][][] A; // A[j][vmtype][s]: # of new requests for VMs of vmtype vmtype for service s of provider j
       public double[][][][] D; // D[host][j][vmtype][s]: # of removed VMs of vmtype vmtype for service s of provider j from AP host
       public double[][][][] n; // n[host][j][vmtype][s]: # of allocated VMs of vmtype vmtype for service s of provider j at AP host
       
       public double Omega = 100;
    
        Configuration config;

        public SchedulerData(Configuration config,double[] r, double[][][] A, double[][][][] D, double[][][][] n)
        {
                this.config=config;
                this.S = config.getServicesNumber(); 
                this.P = config.getProvidersNumber(); 
                this.V = config.getVmTypesNumber();
                this.N = config.getHostsNumber();
                this.R = config.getMachineResourcesNumber();
                this.r = r;
                this.pen = pen;
                this.A = A;
                this.D = D;
                this.n = n;
                this.Omega=config.getOmega();
                
                initializeArrays();
        }
        
        
        private void initializeArrays(){
        
                // 1 - Fairness Weight
                phi = new double[P]; // fairness weight per provider
		for (int j=0;j<P;j++)
			phi[j] = config.getPhiWeight()[j];
                
                // 2 - amount of each res res for each VM vmtype vmtype 
                // R0:cpu, R1:memory, R3:storage,R4:bandwidth, v0:small,v1:medium,v2:large 
                m = new double[V][R];  
                for (int vmtype=0;vmtype<V;vmtype++)
			for (int res=0;res<R;res++){
                            if(res==0) 
				m[vmtype][res] = config.getCpu_VM()[vmtype];
                            else if(res==1) 
				m[vmtype][res] = config.getMemory_VM()[vmtype];
                            else if(res==2) 
				m[vmtype][res] = config.getStorage_VM()[vmtype];
                            else if(res==3) 
				m[vmtype][res] = config.getBandwidth_VM()[vmtype];
                        
                        }
                // 3- p[host][res]: capacity of each res res at each AP host 
                p = new double[N][R]; 
		
                for (int host=0;host<N;host++)
			for (int res=0;res<R;res++){
                            
                            if(res==0) 
				p[host][res] = config.getCpu_host();
                            else if(res==1) 
				p[host][res] = config.getMemory_host();
                            else if(res==2) 
				p[host][res] = config.getStorage_host();
                            else if(res==3)
				p[host][res] = config.getBandwidth_host();
                        
                        }
                
                 // 4- pen[j][s] penalty for not satisfying locally a request for service s of provider j
                pen = new double[P][S];
		for (int j=0;j<P;j++)
			for (int s=0;s<S;s++)
				pen[j][s] = config.getPenalty()[j][s];
                
             
                
        }
        
        
        
        public double ksi(int s, int j, int v) //helper function for denoting capacity of a VM type for the client requests for a service s of a provider j
        {
                return 100*(v+1);
        }

        public double[] f(double r, int s, int j) // Function for finding the number of requests for VMs of a certain type based on the client requests r for service s of provider j 
        {
                double[] unitVector = new double[V];
                unitVector[s] = 1;

                unitVector[s] = (((int)r/ksi(s,j,s))+1)*unitVector[s];

                return unitVector;
        }
}

