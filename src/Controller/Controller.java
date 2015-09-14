/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.util.List;
import java.util.Queue;
import java.util.Random;
import jsc.distributions.Exponential;
import jsc.distributions.Pareto;

/**
 *
 * @author kostas
 */
public class Controller {

    Configuration _config;
    List<String> _nodes;
    Queue<VMRequest>[] _queue;
   
    int _numberOfHosts=0;
    int _numberOfProviders=0;
    
   // How to create requests per Service Domain
    String[] _generatorType; 
    Exponential[] _exponentialArrivalGenerator;
    Pareto[] _paretoArrivalGenerator;
    
    Random _arrivalTimeRandom;
    Random rand;
    
    
    Controller(List<String> nodes, Configuration config) {
        this._config=config;
        this._nodes=nodes;
        _numberOfHosts=nodes.size();
    }
    
    public void initializeController(){
    
        
        
        _queue=new Queue[_numberOfProviders];        // A queue with VM requests per Provider
        
        //Initialize generators
        _generatorType=new String[_numberOfProviders];
        _exponentialArrivalGenerator=new Exponential[_numberOfProviders];
        _paretoArrivalGenerator=new Pareto[_numberOfProviders];
    
        double lamda=0;
        double location=0;
        double shape=0;
        
        for (int i = 0; i < _numberOfProviders; i++) {
                
                if(_generatorType[i].equals(EGeneratorType.Exponential.toString())){
                
                    lamda=((Double)_config.getRequestArrivalRateConfig()[i].get("mean"));
                    this._exponentialArrivalGenerator[i]=new Exponential(lamda);
                    this._paretoArrivalGenerator[i]=null;
                }
                else if(_generatorType[i].equals(EGeneratorType.Pareto.toString())){
                    
                  _exponentialArrivalGenerator[i]=null;  
                  location=((Double)_config.getRequestArrivalRateConfig()[i].get("location"));
                  shape =((Double)_config.getRequestArrivalRateConfig()[i].get("shape"));
                  
                  this._paretoArrivalGenerator[i]=new Pareto(location, shape);
                  double mean=_paretoArrivalGenerator[i].mean();
                  System.out.print(mean);
                }
                 else if(_generatorType[i].equals(EGeneratorType.Random.toString())){
                 //missing
                 }
       
            }      
    }
    
}
