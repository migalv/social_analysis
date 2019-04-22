/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.recsys.recommender;

import es.uam.eps.bmi.recsys.Recommendation;
import es.uam.eps.bmi.recsys.RecommendationImpl;
import es.uam.eps.bmi.recsys.data.Ratings;
import es.uam.eps.bmi.recsys.ranking.Ranking;
import es.uam.eps.bmi.recsys.ranking.RankingImpl;



/**
 *
 * @author sergio
 */
abstract class AbstractRecommender implements Recommender {

    private final Ratings ratings;

    public AbstractRecommender(Ratings ratings) {
        this.ratings = ratings;
    }

    @Override
    public Recommendation recommend(int cutoff) {
        Recommendation recommend = new RecommendationImpl();

        //Recorremos todos los usuarios
        ratings.getUsers().forEach((current_user) -> {
            Ranking rank= new RankingImpl(cutoff);
            
            //Por cada usuario recomendamos los mejores items que no ha calificado
            ratings.getItems().forEach((current_item) -> {
                if (!ratings.getItems(current_user).contains(current_item)) {
                    //Obtenemos score y lo añadimos al ranking
                    double finalScore=this.score(current_item, current_item);
                    rank.add(current_item, finalScore);
                }
            });
            
            //Finalmente obtenemos los mejores scores de cada usuario y lo añadimos a la recomendacion
            recommend.add(current_user, rank);
        });
        
        return recommend;
    }

    @Override
    public abstract double score(int user, int item);

}
