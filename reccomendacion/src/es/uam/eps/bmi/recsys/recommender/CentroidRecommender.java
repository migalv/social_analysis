/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.recsys.recommender;

import es.uam.eps.bmi.recsys.data.Features;
import es.uam.eps.bmi.recsys.data.FeaturesCentroid;
import es.uam.eps.bmi.recsys.data.Ratings;
import es.uam.eps.bmi.recsys.recommender.similarity.CosineFeatureSimilarity;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author sergio
 */
public class CentroidRecommender<F> extends AbstractRecommender {

    private final CosineFeatureSimilarity<F> features;

    public CentroidRecommender(Ratings ratings, CosineFeatureSimilarity<F> features) {
        super(ratings);
        this.features = features;
    }

    @Override
    public double score(int user, int item) {
        double finalScore, scoreFunction = 0;
        Map<F, Double> itemFeature = new HashMap<>();
        Features featuresCentroid = new FeaturesCentroid();

        //Por cada item que un usuario haya realizado un rating
        for (int current_item : this.ratings.getItems(user)) {
            for (F current_feature : this.features.getFeatures().getFeatures(current_item)) {
                //Calculamos el valor entre el value del item-feature multiplicado con el rating del user-item
                double featureScore = this.features.getFeatures().getFeature(current_item, current_feature);
                scoreFunction = featureScore * this.ratings.getRating(user, current_item);

                //Si no existe la caracteristica en nuestro mapa lo creamos
                if (!itemFeature.containsKey(current_feature)) {
                    itemFeature.put(current_feature, scoreFunction);
                } else {//Si existe obtenemos el score parcial y se lo sumamos al valor obtenido
                    itemFeature.replace(current_feature, itemFeature.get(current_feature) + scoreFunction);
                }
            }
        }
        
        //Actualizamos el valor final de cada u[f] dividiendolo por el numero de rates del usuario
        itemFeature.keySet().forEach((current_feature) -> {
            int tamUserItem=this.ratings.getItems(user).size();
            double finalValue=(double) itemFeature.get(current_feature)/tamUserItem;
            
            itemFeature.replace(current_feature, finalValue);
            
            //Finalmente añadimos un feature a la clase FeaturesCentroid
            featuresCentroid.setFeature(user, current_feature, finalValue);
        });
        
        //Añadimos la funcion usuario[caracteristica]
        this.features.setXFeatures(featuresCentroid);

        //Finalmente calculamos la similitud entre el usuario y el item
        finalScore = this.features.sim(user, item);
        
        return finalScore;
    }

    @Override
    public String toString() {
        return "centroid-based  " + this.features;
    }

}
