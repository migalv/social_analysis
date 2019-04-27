/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.recsys.recommender;

import es.uam.eps.bmi.recsys.data.Ratings;
import java.util.Set;

/**
 *
 * @author sergio
 */
public class AverageRecommender extends AbstractRecommender {

    Ratings ratingAver;
    int minim;

    public AverageRecommender(Ratings ratings, int minim) {
        super(ratings);
        this.ratingAver = ratings;
        this.minim = minim;
    }

    @Override
    public double score(int user, int item) {
        Set<Integer> users = ratingAver.getUsers(item);

        //Obtenemos el numero de usuarios que han calificado un item
        int n_ratings = users.size();
        double finalScore = 0;

        //Sumamos todos los ratings de los usuarios del item actual
        if (n_ratings>= this.minim) {
            finalScore = users.stream().map((current_user) -> ratingAver.getRating(current_user, item)).filter((parcialScore) -> (parcialScore != null)).map((parcialScore) -> parcialScore).reduce(finalScore, (accumulator, _item) -> accumulator + _item);
        }

        //Dividimos por el numero de usuarios que han calificado el item
        finalScore /= n_ratings;

        return finalScore;
    }

    @Override
    public String toString() {
        return "average";
    }

}
