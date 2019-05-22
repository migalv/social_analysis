/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.sna.metric.user;

import es.uam.eps.bmi.sna.metric.LocalMetric;
import es.uam.eps.bmi.sna.ranking.Ranking;
import es.uam.eps.bmi.sna.ranking.RankingImpl;
import es.uam.eps.bmi.sna.structure.UndirectedSocialNetwork;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author migal
 * @param <U>
 */
public class UserClusteringCoefficient<U extends Comparable<U>> implements LocalMetric {

    int topK;
    
    public UserClusteringCoefficient(int topK){
        this.topK = topK;
    }
    
    public UserClusteringCoefficient(){
        topK = -1;
    }
    
    @Override
    public Ranking compute(UndirectedSocialNetwork network) {
        Set<U> users = network.getUsers();
        Ranking<U> ranking;
        if(topK != -1)
            ranking = new RankingImpl<>(topK);
        else
            ranking = new RankingImpl<>();
        
        for(U user : users){
            ranking.add(user, this.compute(network, user));
        }
        
        return ranking;
    }

    @Override
    public double compute(UndirectedSocialNetwork network, Comparable element) {
        // Numero de conexiones entre vecinos del vertice
        int connectionsBetweenNeighbors = 0;
        List<U> contacts = new ArrayList<>(network.getContacts(element));

        if (contacts.size() <= 1) {
            return 0.0;
        }

        // Calculamos el numero de conexiones entre vecinos
        for (int i = 0; i < contacts.size(); i++) {
            // Mientras no sea el último vecino de la lista
            if (i != contacts.size() - 1) {
                for (int j = i + 1; j < contacts.size(); j++) {
                    // Si existe un arco entre los vertices entonces sumamos una
                    if (network.connected(contacts.get(i), contacts.get(j))) {
                        connectionsBetweenNeighbors++;
                    }
                }
            }
        }

        // Numero de posibles conexiones entre los vecinos del vertice
        int pcbn = (contacts.size() * (contacts.size() - 1)) / 2;

        return (double) connectionsBetweenNeighbors / pcbn;
    }

    @Override
    public String toString(){
        return "UserClusteringCoefficient";
    }
}
