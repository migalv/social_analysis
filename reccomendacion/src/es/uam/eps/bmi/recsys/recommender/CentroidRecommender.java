/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.recsys.recommender;

import es.uam.eps.bmi.recsys.data.Ratings;
import es.uam.eps.bmi.recsys.recommender.similarity.CosineFeatureSimilarity;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author sergio
 */
public class CentroidRecommender<F> extends AbstractRecommender{
    private final CosineFeatureSimilarity<F> features;


    public CentroidRecommender(Ratings ratings, CosineFeatureSimilarity<F> features) {
        super(ratings);
        this.features= features;
        
    }

    @Override
    public double score(int user, int item) {
        Map<Integer, Double> userSimilarity;
        Map<F, Double> featureSimilarity;
        
        userSimilarity= new HashMap<>();
        featureSimilarity= new HashMap<>();

        //Guardamos en un mapa los ratings de todos los items del usuario user
        this.ratings.getUsers(user).forEach((current_item) -> {
            double scoreUser = this.ratings.getRating(user, current_item);
            userSimilarity.put(current_item, scoreUser);
        });
        
        //Por otro lado guardamos todas las frecuencias de los features
        /*for(int current_featureID: this.features.getFeatures().getIDs()){
            for(F current_feature: this.features.getFeatures().getFeatures(current_featureID)){
                featureSimilarity.put(current_feature, Double.NaN)
            }
        }*/
             
return 2.0;
        
        
    }
    
    @Override
    public String toString(){
        return "centroid-based  " + this.features;
    }
    
}
