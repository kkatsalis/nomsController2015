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

import ilog.concert.*;
import ilog.cplex.*;


public class Scheduler {
	static class Data {
		int N; // # of APs
		int P; // # of service providers
		int S; // # of services
		int V; // # of VM types
		double[] r; // requests per service provider
		int R; // # of physical resources per physical machine
		double[] phi; // fairness weight per provider
		double[][] m; // m[v][k]: amount of each resource k for each VM type v
		double[][] p; // p[i][k]: capacity of each resource k at each AP i
		double[][] pen; // pen[j][s] penalty for not satisfying locally a request for service s of provider j
		double[][][] A; // A[j][v][s]: # of new requests for VMs of type v for service s of provider j
		double[][][][] D; // D[i][j][v][s]: # of removed VMs of type v for service s of provider j from AP i
		double[][][][] n; // n[i][j][v][s]: # of allocated VMs of type v for service s of provider j at AP i
		final double Omega = 100;



		double ksi(int s, int j, int v)
		{
			return 100*(v+1);
		}

		double[] f(double r, int s, int j)
		{
			double[] unitVector = new double[V];
			unitVector[s] = 1;

			unitVector[s] = (((int)r/ksi(s,j,s))+1)*unitVector[s];

			return unitVector;
		}



		public Data(int S, int P, int V, int N, int R, double[] r, double[][] m, double [][] p, double [][] pen, double[][][] A, double[][][][] D, double[][][][] n, double[] phi)
		{
			this.S = S; 
			this.P = P; 
			this.V = V;
			this.N = N;
			this.R = R;
			this.r = r;
			this.m = m;
			this.p = p;
			this.pen = pen;
			this.A = A;
			this.D = D;
			this.n = n;
			this.phi = phi;
		}
	}
	static void buildModelByRow(IloModeler    model,
			Data          data,
			IloNumVar[][][][]   a,
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




	public static void main(String[] args) {

		int S = 3;
		int P = 5;
		int V = 3;
		int N = 3;
		int R = 2;
		int capacity = 1000;

		double[] phi = new double[P];
		for (int j=0;j<P;j++)
			phi[j] = 1;

		double[] r = new double [P];
		double[][] m = new double[V][R];

		for (int v=0;v<V;v++)
			for (int k=0;k<R;k++)
				m[v][k] = 2*(v+1);

		for (int j=0;j<P;j++)
			r[j] = 100*(j+1);

		double[][] p = new double[N][R];
		for (int i=0;i<N;i++)
			for (int k=0;k<R;k++)
				p[i][k] = capacity;

		double[][] pen = new double[P][S];
		for (int j=0;j<P;j++)
			for (int s=0;s<S;s++)
				pen[j][s] = 10*(s+1);

		double[][][] A = new double[P][V][S];

		for (int j=0;j<P;j++)
			for (int v=0;v<V;v++)
				for (int s=0;s<S;s++)
					A[j][v][s] = 5;


		double[][][][] n = new double [N][P][V][S];
		double[][][][] D = new double [N][P][V][S];

		for (int i=0;i<N;i++)
			for (int j=0;j<P;j++)
				for (int v=0;v<V;v++)
					for (int s=0;s<S;s++)
					{
						n[i][j][v][s] = 2;
						D[i][j][v][s] = 1;
					}


		try {
			Data data = new Data(S, P, V, N, R,  r, m, p, pen, A, D, n, phi);

			// Build model
			IloCplex     cplex = new IloCplex();
			IloNumVar[][][][]  a   = new IloNumVar[data.N][data.P][data.V][data.S];

			IloNumVarType   varType   = IloNumVarType.Float;

			buildModelByRow   (cplex, data, a, varType);

			//System.out.println(cplex.toString());

			// Solve model

			if ( cplex.solve() ) { 
				System.out.println();
				System.out.println("Solution status = " + cplex.getStatus());
				System.out.println();
				System.out.println(" cost = " + cplex.getObjValue());
				for (int i = 0; i < data.N; i++) {
					for (int j=0;j < data.P; j++)
						for (int v=0;v < data.V; v++)
							for (int s=0;s < data.S; s++)
								System.out.println(" a[" + i + "],["+j+"]["+v+"]["+s+"] = " + cplex.getValue(a[i][j][v][s]));
				}
				System.out.println();
			}
			cplex.end();
		}
		catch (IloException ex) {
			System.out.println("Concert Error: " + ex);
		}

	}

	static void usage() {
		System.out.println(" ");
		System.out.println("usage: java Lyapunov ");
		System.out.println(" ");
	}
}



