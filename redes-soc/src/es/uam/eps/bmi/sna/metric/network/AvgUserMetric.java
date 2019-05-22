/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.sna.metric.network;

import es.uam.eps.bmi.sna.metric.GlobalMetric;
import es.uam.eps.bmi.sna.metric.LocalMetric;
import es.uam.eps.bmi.sna.ranking.Ranking;
import es.uam.eps.bmi.sna.ranking.RankingElement;
import es.uam.eps.bmi.sna.ranking.RankingImpl;
import es.uam.eps.bmi.sna.structure.Edge;
import es.uam.eps.bmi.sna.structure.UndirectedSocialNetwork;

/**
 *
 * @author migal
 * @param <U>
 */
public class AvgUserMetric <U extends Comparable<U>> implements GlobalMetric {

    LocalMetric<U, Edge<U>> localMetric;
    
    public AvgUserMetric(LocalMetric<U, Edge<U>> localMetric){
        this.localMetric = localMetric;
    }
    
    @Override
    public double compute(UndirectedSocialNetwork network) {
        Ranking<U> ranking = new RankingImpl<>();
        // Suma parcial de los scores de la metrica local de la red social
        double sumParcialScores = 0.0;
        
        ranking = localMetric.compute(network);
        
        for(RankingElement<U> element : ranking){
            sumParcialScores += element.getScore();
        }
        
        return sumParcialScores / ranking.size();
    }
    
    public String toString(){
        return "Avg " + "(" + localMetric.toString() + ")";
    }
}
