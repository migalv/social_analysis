/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.recsys.recommender;

import es.uam.eps.bmi.recsys.data.Ratings;
import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author sergio
 */
public class AverageRecommender extends AbstractRecommender {
    Ratings ratingAver;

    public AverageRecommender(Ratings ratings) {
        super(ratings);
        this.ratingAver = ratings;
    }

    @Override
    public double score(int user, int item) {
        Set<Integer> users= ratingAver.getUsers(item);

        //Obtenemos el numero de usuarios que han calificado un item
        int n_ratings= users.size();
        double finalScore=0;
        
        //Sumamos todos los ratings de los usuarios del item actual
        finalScore = users.stream().map((current_user) -> ratingAver.getRating(current_user, item)).reduce(finalScore, (accumulator, _item) -> accumulator + _item);
        
        /*for(int current_user: users){
            finalScore+=ratingAver.getRating(current_user, item);
        }*/
        
        //Dividimos por el numero de usuarios que han calificado el item
        finalScore/=n_ratings;
                
        return finalScore;
    }

}
