/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.recsys.recommender;

import es.uam.eps.bmi.recsys.data.Ratings;
import es.uam.eps.bmi.recsys.recommender.similarity.Similarity;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author sergio
 */
public class ItemNNRecommender extends AbstractRecommender {

    private final Similarity similarity;
    private final Map<Integer, Map<Integer, Double>> itemSimilarity;

    public ItemNNRecommender(Ratings ratings, Similarity similarity) {
        super(ratings);
        this.similarity = similarity;

        this.itemSimilarity = new HashMap<>();

        //Por cada usuario creamos un ranking añadiendo la similitud entre los vecinos
        for (int current_item : this.ratings.getItems()) {
            for (int sim_item : this.ratings.getItems()) {
                if (current_item != sim_item) {
                    double similarityScore = this.similarity.sim(current_item, sim_item);

                    Map<Integer, Double> mapSimilarity = new HashMap<>();
                    mapSimilarity.put(sim_item, similarityScore);

                    //Lo añadimos al map de items
                    itemSimilarity.put(current_item, mapSimilarity);

                }
            }

        }
    }

    @Override
    public double score(int user, int item) {
        double scoreSimilarity = 0, scoreParcial = 0, finalScore;

        //Sumamos el global de sim entre los items y el item principal
        for (int current_item : itemSimilarity.get(item).keySet()) {
            if (this.ratings.getItems(user).contains(current_item)) {
                //Obtenemos el sim entre el item actual y el item a calcular
                Double scoreSim = itemSimilarity.get(item).get(current_item);

                if(scoreSim != null && !scoreSim.isNaN() && scoreSim > 0){
                    //Sumamos el rating del usuario con el item actual multiplicado por el valor anterior
                    scoreParcial += (this.ratings.getRating(user, current_item) * scoreSim);

                    //Sumamos los valores del sim entre el item actual y el item a calcular
                    scoreSimilarity += scoreSim;
                }
            }

        }

        //Obtenemos el score final a recomendar
        if (scoreSimilarity != 0) {
            finalScore = (double) scoreParcial / scoreSimilarity;
        }else{
            finalScore=0;
        }

        return finalScore;
    }

    @Override
    public String toString() {
        return "item-based NN " + this.similarity;
    }

}
