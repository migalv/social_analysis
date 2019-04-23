/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.recsys.metric;

import es.uam.eps.bmi.recsys.Recommendation;
import es.uam.eps.bmi.recsys.RecommendationImpl;
import es.uam.eps.bmi.recsys.data.Ratings;
import es.uam.eps.bmi.recsys.ranking.RankingElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author sergio
 */
public class Recall implements Metric {

    private final Ratings ratings;
    private final double threshold;
    private final int cutoff;

    public Recall(Ratings test, double threshold, int cutoff) {
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

            //Creamos una lista para poder ordenar el mapa
            List<Map.Entry<Integer, Double>> listUser = new ArrayList<>(ratingMap.entrySet());
            listUser.sort(Map.Entry.comparingByValue());

            for (RankingElement rankingRec : rec.getRecommendation(current_user)) {
                ratingRec.put(rankingRec.getID(), rankingRec.getScore());
            }

            int counter = 0, positiveUser = 0, positiveRec = 0;
            for (Map.Entry<Integer, Double> entry : listUser) {

                if (entry.getValue() >= this.threshold) {
                    positiveUser++;
                }

                if (ratingRec.containsKey(entry.getKey())) {
                    if (ratingRec.get(entry.getKey()) >= this.threshold) {
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
