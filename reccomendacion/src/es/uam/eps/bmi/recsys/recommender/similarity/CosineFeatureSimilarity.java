/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.recsys.recommender.similarity;

import es.uam.eps.bmi.recsys.data.Features;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author sergio
 */
public class CosineFeatureSimilarity<F> extends FeatureSimilarity<F> {

    public CosineFeatureSimilarity(Features<F> features) {
        super(features);
    }

    @Override
    public double sim(int x, int y) {
        double scoreX = 0, scoreY = 0, parcialScore = 0, finalScore;

        //Creamos un mapa para cada item con los usuarios y sus respectivos scores
        Map<F, Double> mapX = new HashMap<>();
        Map<F, Double> mapY = new HashMap<>();

        //Para el primer item guardamos todos los ratings en el mapa
        for (F current_feature : this.getFeatures().getFeatures(x)) {
            double scoreCurrentUser = this.getFeatures().getFeature(x, current_feature);

            scoreX += Math.pow(scoreCurrentUser, 2);
            mapX.put(current_feature, scoreCurrentUser);
        }

        //Realizamos la misma operacion para el segundo item
        for (F current_feature : this.getFeatures().getFeatures(y)) {
            double scoreCurrentUser = this.getFeatures().getFeature(y, current_feature);

            scoreY += Math.pow(scoreCurrentUser, 2);
            mapY.put(current_feature, scoreCurrentUser);
        }

        //Guardamos en esta variable la suma del producto de los scores de los items que comparten ambos usuarios
        parcialScore = mapX.keySet().stream().filter((current_feature) -> (mapY.containsKey(current_feature))).map((current_feature) -> mapX.get(current_feature) * mapY.get(current_feature)).reduce(parcialScore, (accumulator, _item) -> accumulator + _item);

        //Calculamos la raiz de los vectores de los usuarios
        scoreX = Math.sqrt(scoreX);
        scoreY = Math.sqrt(scoreY);

        //Finalmente, realizamos la operacion de similitud
        finalScore = (double) parcialScore / (scoreX * scoreY);

        return finalScore;

    }

    @Override
    public String toString() {
        return "(cosine on user centroid)";
    }

}
