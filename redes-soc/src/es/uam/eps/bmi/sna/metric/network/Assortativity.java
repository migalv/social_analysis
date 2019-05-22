/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.sna.metric.network;

import es.uam.eps.bmi.sna.metric.GlobalMetric;
import es.uam.eps.bmi.sna.structure.Edge;
import es.uam.eps.bmi.sna.structure.EdgeImpl;
import es.uam.eps.bmi.sna.structure.UndirectedSocialNetwork;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author migal
 * @param <U>
 */
public class Assortativity <U extends Comparable<U>> implements GlobalMetric {

    
    public Assortativity(){
        
    }
    
    @Override
    public double compute(UndirectedSocialNetwork network) {
        int m = network.nEdges();
        Set<U> users = network.getUsers();
        Set<Edge<U>> edges = new HashSet<>();
        double degreeSum = 0;
        double result;
        double squared = 0.0, cubic = 0.0;
        
        // Recuperamos un set de todos los arcos de la red social
        for(U user : users){
            Set<U> contacts = network.getContacts(user);
            for(U contact : contacts){
                if(network.connected(user, contact))
                    edges.add(new EdgeImpl(user, contact));
            }
        }
        
        // Calculamos el sumatorio de los arcos del grafo
        for(Edge edge : edges){
            degreeSum += network.getContacts(edge.getFirst()).size() * network.getContacts(edge.getSecond()).size();
        }
        
        // 4 * m * Sumatorio de los arcos del grafo
        result = 4 * m * degreeSum;
        
        // Calculamos el sumatorio del grado de los nodos al cuadrado y al cubo
        for(U user : users){
            squared += Math.pow(network.getContacts(user).size(), 2);
            cubic += Math.pow(network.getContacts(user).size(), 3);
        }
        // Sumatorio al cuadrado
        squared = Math.pow(squared, 2);
        
        // 2 * m * por el sumatorio de los grados al cubo
        degreeSum = 2 * m * cubic;

        // Aplicamos la formula
        result = (result - squared) / (degreeSum - squared);
        
        return result;
    }
    
    
    @Override
    public String toString(){
        return "Assortativity";
    }
}
