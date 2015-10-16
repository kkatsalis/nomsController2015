/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cplex;

/**
 *
 * @author kostas
 */

import Controller.Configuration;
import ilog.concert.*;
import ilog.cplex.*;
import java.util.Hashtable;


public class Scheduler {
	
        Configuration config;
      
        double[][][][] n;
        
        public Scheduler(Configuration config){
            
            this.config=config;
            
          
            int N=config.getHostsNumber();
            int P=config.getProvidersNumber();
            int V=config.getVmTypesNumber();
            int S=config.getServicesNumber();
            
            this.n = new double [N][P][V][S];
            
            for (int i=0;i<N;i++)
			for (int j=0;j<P;j++)
				for (int v=0;v<V;v++)
					for (int s=0;s<S;s++)
					{
						n[i][j][v][s] = 0;
						
					}
            
        }
        
        static void buildModelByRow(IloModeler    model,
			SchedulerData          data,
			IloNumVar[][][][]   a,
			IloNumVarType type) throws IloException {
		
		
	}

	public int[][][][] Run(SchedulerData data) {

		int S = config.getServicesNumber();
		int P = config.getProvidersNumber();
		int V = config.getVmTypesNumber();
		int N = config.getHostsNumber();
		
                int[][][][] activationMatrix=new int[N][P][V][S];
               

		try {
			
			// Build model
			IloCplex     cplex = new IloCplex();
			IloNumVar[][][][]  a   = new IloNumVar[config.getHostsNumber()][config.getProvidersNumber()][config.getVmTypesNumber()][config.getServicesNumber()];

			IloNumVarType   varType   = IloNumVarType.Float;

			buildModelByRow   (cplex, data, a, varType);

			//System.out.println(cplex.toString());

			// Solve model

			if ( cplex.solve() ) { 
				System.out.println();
				System.out.println("Solution status = " + cplex.getStatus());
				System.out.println();
				System.out.println(" cost = " + cplex.getObjValue());
				for (int i = 0; i < config.getHostsNumber(); i++) {
					for (int j=0;j < config.getProvidersNumber(); j++)
						for (int v=0;v < config.getVmTypesNumber(); v++)
							for (int s=0;s < config.getServicesNumber(); s++)
								System.out.println(" a[" + i + "],["+j+"]["+v+"]["+s+"] = " + cplex.getValue(a[i][j][v][s]));
				}
				System.out.println();
			}
			cplex.end();
                        
                    
		}
		catch (IloException ex) {
			System.out.println("Concert Error: " + ex);
		}

                               
                return activationMatrix;
	}

	
}



