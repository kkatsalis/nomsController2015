/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

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
        SchedulerData data;
        Hashtable results;
        
        public Scheduler(Configuration config,SchedulerData data){
            
            this.config=config;
            this.data=data;
            this.results=new Hashtable();
        }
        
	private void buildModelByRow(IloModeler model,
			SchedulerData  data,
			IloNumVar[][][][] a,
			IloNumVarType type) throws IloException {

		for (int i = 0; i < data.N; i++)
			for (int j = 0; j < data.P; j++)
				for (int v = 0; v < data.V; v++)
					for (int s = 0; s < data.S; s++)
						a[i][j][v][s] = model.numVar(0, 1, type);


		// build y[i]s
		IloNumExpr y_sum = model.numExpr();
		IloNumExpr[][] y =  new IloNumExpr[data.N][data.R];
		for (int i=0;i<data.N;i++)
		{
			IloNumExpr ksum = model.numExpr();
			for (int k=0;k<data.R;k++)
			{
				IloNumExpr ssum = model.numExpr();

				for (int s=0; s<data.S; s++)
				{
					IloNumExpr jsum = model.numExpr();
					for (int j=0; j<data.P; j++)
					{
						IloNumExpr vsum = model.numExpr();
						for (int v=0; v<data.V; v++)
						{
							IloNumExpr expr = model.numExpr();
							expr = model.sum(expr, model.prod(a[i][j][v][s], data.A[j][v][s]));
							expr = model.sum(expr, data.n[i][j][v][s]);
							expr = model.sum(expr, -data.D[i][j][v][s]);
							expr = model.prod(expr, data.m[v][k]);
							expr = model.sum(expr, -data.p[i][k]);
							vsum = model.sum(vsum, expr);
						}
						jsum = model.sum(jsum,vsum);
					}
					ssum = model.sum(ssum, jsum);
				}
				y[i][k] = ssum;

				ksum = model.sum(ksum, model.prod(model.max(y[i][k], 0), y[i][k]));
			}
			y_sum = model.sum(y_sum, ksum);
		}

		// build pr penalty expression
		IloNumExpr pr_expr = model.numExpr();
		for(int s=0;s<data.S;s++)
		{
			IloNumExpr jsum = model.numExpr();
			for(int j=0;j<data.P;j++)
			{
				IloNumExpr vsum = model.numExpr();
				for(int v=0;v<data.V;v++)
				{ 
					IloNumExpr expr = model.numExpr();
					for (int i=0;i<data.N;i++)
					{
						expr = model.sum(expr,model.prod(a[i][j][v][s], data.A[j][v][s]));
						expr = model.sum(expr, data.n[i][j][v][s]-data.D[i][j][v][s]);						
					}
					expr = model.prod(expr, data.ksi(s,j,v));
					expr = model.diff(data.f(data.r[j], s,j)[v],expr);

					vsum = model.sum(vsum, expr);
				}
				vsum = model.prod(vsum, data.pen[j][s]);
				jsum = model.sum(jsum, vsum);
			}
			pr_expr = model.sum(pr_expr, jsum);
		}

		// build fr fairness expression
		double n_sum = 0;

		for(int i=0;i<data.N;i++)  
			for(int j=0;j<data.P;j++)
				for(int v=0;v<data.V;v++) 
					for (int s=0;s<data.S;s++)
						n_sum += data.n[i][j][v][s];


		IloNumExpr fr_expr = model.numExpr();

		for (int k=0;k<data.P;k++)
		{
			IloNumExpr isum = model.numExpr();
			for(int i=0;i<data.N;i++)
			{
				IloNumExpr vsum = model.numExpr();
				for(int v=0;v<data.V;v++)
				{ 
					IloNumExpr expr = model.numExpr();
					for (int s=0;s<data.S;s++)
					{
						expr = model.sum(expr,model.prod(a[i][k][v][s], data.A[k][v][s]));
						expr = model.sum(expr, data.n[i][k][v][s]-data.D[i][k][v][s]);
					}
					vsum = model.sum(vsum, expr);
				}
				isum = model.sum(isum, vsum);
			}
			isum = model.prod(isum,1/n_sum);
			isum = model.diff(isum, 1/data.P);
			isum = model.prod(isum, data.phi[k]);
			fr_expr = model.sum(fr_expr, isum);
		}

		// constraint for sum of a[][][][] variables
		for (int j=0; j<data.P; j++)
			for (int s=0; s<data.S; s++){
				IloNumExpr isum = model.numExpr();
				for (int i=0;i<data.N;i++) { 
					IloNumExpr expr = model.numExpr();
					for (int v=0;v<data.V;v++) 
						expr = model.sum(expr, a[i][j][v][s]);
					isum = model.sum(isum, expr);
				}
				model.addLe(isum, 1);  
			}

		// constraint for y[i][k]
		for (int i=0;i<data.N;i++)
			for (int k=0;k<data.R;k++)
				model.addLe(y[i][k], 0);

		// minimization problem

		//start of debugging
		//fr_expr = model.numExpr();
		//y_sum = model.numExpr();
		//end of debugging
		IloNumExpr problem = model.numExpr();
		problem = model.sum(problem,model.sum(model.prod(model.sum(pr_expr, fr_expr), data.Omega), y_sum));
		model.addMinimize(problem);

	}


	public Hashtable Run() {

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

                results.put("activationMatrix",activationMatrix);
                results.put("nMatrix",data.n);
                
                return results;
	}

	
}



