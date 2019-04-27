/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.recsys.recommender.similarity;

import es.uam.eps.bmi.recsys.data.Ratings;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author sergio
 */
public class CosineUserSimilarity implements Similarity {

    private final Ratings ratings;

    public CosineUserSimilarity(Ratings ratings) {
        this.ratings = ratings;
    }

    @Override
    public double sim(int x, int y) {
        double scoreX = 0, scoreY = 0, parcialScore = 0, finalScore;

        //Creamos un mapa para cada usuario con los items y sus respectivos scores
        Map<Integer, Double> mapX = new HashMap<>();
        Map<Integer, Double> mapY = new HashMap<>();

        //Para el primer usuario guardamos todos los ratings en el mapa
        for (int current_item : this.ratings.getItems(x)) {
            double scoreCurrentItem = this.ratings.getRating(x, current_item);

            scoreX += Math.pow(scoreCurrentItem, 2);
            mapX.put(current_item, scoreCurrentItem);
        }

        //Realizamos la misma operacion para el segundo usuario
        for (int current_item : this.ratings.getItems(y)) {
            double scoreCurrentItem = this.ratings.getRating(y, current_item);

            scoreY += Math.pow(scoreCurrentItem, 2);
            mapY.put(current_item, scoreCurrentItem);
        }

        //Guardamos en esta variable la suma del producto de los scores de los items que comparten ambos usuarios
        parcialScore = mapX.keySet().stream().filter((current_item) -> (mapY.containsKey(current_item))).map((current_item) -> mapX.get(current_item) * mapY.get(current_item)).reduce(parcialScore, (accumulator, _item) -> accumulator + _item);

        //Calculamos la raiz de los vectores de los usuarios
        scoreX = Math.sqrt(scoreX);
        scoreY = Math.sqrt(scoreY);

        //Finalmente, realizamos la operacion de similitud
        finalScore = (double) parcialScore / (scoreX * scoreY);

        return finalScore;
    }

    @Override
    public String toString() {
        return "(cosine)";
    }
}
