/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.recsys.metric;

import es.uam.eps.bmi.recsys.Recommendation;
import es.uam.eps.bmi.recsys.data.Ratings;
import es.uam.eps.bmi.recsys.ranking.Ranking;
import es.uam.eps.bmi.recsys.ranking.RankingElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author sergio
 */
public class Precision implements Metric {

    private final Ratings ratings;
    private final double threshold;
    private final int cutoff;

    public Precision(Ratings test, double threshold, int cutoff) {
        this.ratings = test;
        this.threshold = threshold;
        this.cutoff = cutoff;
    }

        @Override
    public double compute(Recommendation rec) {
        double precisionParcial = 0, precisionFinal = 0;

        for (int current_user : this.ratings.getUsers()) {
            //Creamos un mapa por usuario para guardar los ratings de todos los items
            Map<Integer, Double> ratingMap = new HashMap<>();
            Map<Integer, Double> ratingRec = new HashMap<>();

            //Añadimos al mapa todos los ratings de los items
            this.ratings.getItems(current_user).forEach((current_item) -> {
                double rating = this.ratings.getRating(current_user, current_item);
                ratingMap.put(current_item, rating);
            });

            for (RankingElement rankingRec : rec.getRecommendation(current_user)) {
                ratingRec.put(rankingRec.getID(), rankingRec.getScore());
            }

            //Creamos una lista para poder ordenar el mapa
            List<Map.Entry<Integer, Double>> listRec = new ArrayList<>(ratingRec.entrySet());
            listRec.sort(Map.Entry.comparingByValue());

            int counter = 0, positiveUser = 0, positiveRec = 0;
            for (Map.Entry<Integer, Double> entry : listRec) {

                if (entry.getValue() >= this.threshold) {
                    positiveUser++;
                }

                if (ratingMap.containsKey(entry.getKey())) {
                    if (ratingMap.get(entry.getKey()) >= this.threshold) {
                        positiveRec++;
                    }
                }

                counter++;
                if (counter == this.cutoff) {
                    precisionParcial += (positiveUser / positiveRec);
                    break;
                }
            }

        }

        precisionFinal = (precisionParcial / this.ratings.getUsers().size());

        return precisionFinal;

    }

}
