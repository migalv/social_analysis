/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.sna.metric.edge;

import es.uam.eps.bmi.sna.metric.LocalMetric;
import es.uam.eps.bmi.sna.ranking.Ranking;
import es.uam.eps.bmi.sna.ranking.RankingImpl;
import es.uam.eps.bmi.sna.structure.Edge;
import es.uam.eps.bmi.sna.structure.EdgeImpl;
import es.uam.eps.bmi.sna.structure.UndirectedSocialNetwork;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author migal
 * @param <U>
 */
public class Embeddedness <U extends Comparable<U>> implements LocalMetric {

    int topK;
    
    public Embeddedness(int topK){
        this.topK = topK;
    }
    
    @Override
    public Ranking compute(UndirectedSocialNetwork network) {
        Set<EdgeImpl> edges = new HashSet<>();
        List<U> users = new ArrayList<>(network.getUsers());
        Ranking<Edge<U>> ranking = new RankingImpl<>(topK);
        
        // Recuperamos un set de todos los arcos de la red social
        for(U user : users){
            Set<U> contacts = network.getContacts(user);
            for(U contact : contacts){
                if(network.connected(user, contact))
                    edges.add(new EdgeImpl(user, contact));
            }
        }
        
        // Creamos el ranking
        for(Edge edge : edges){
            ranking.add(edge, this.compute(network, edge));
        }
        
        return ranking;
    }

    @Override
    public double compute(UndirectedSocialNetwork network, Comparable element) {
        Edge<U> edge = (Edge<U>) element;
        Set<U> firstNeighbors =  network.getContacts(edge.getFirst());
        Set<U> secondNeighbors = network.getContacts(edge.getSecond());
        Set<U> sameNeighbors = new HashSet<>();
        
        // Nos quedamos todos los que coinciden
        for(U user : firstNeighbors){
            if(secondNeighbors.contains(user))
                sameNeighbors.add(user);
        }

        if (secondNeighbors.isEmpty()) {
            return 0.0;
        }
        
        return (double) sameNeighbors.size() / ((firstNeighbors.size() - 1) + (secondNeighbors.size() - 1) - sameNeighbors.size());

    }
    
    @Override
    public String toString(){
        return "Embeddedness";
    }
}
