/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.recsys.recommender;

import es.uam.eps.bmi.recsys.data.Ratings;
import es.uam.eps.bmi.recsys.ranking.Ranking;
import es.uam.eps.bmi.recsys.ranking.RankingElement;
import es.uam.eps.bmi.recsys.ranking.RankingImpl;
import es.uam.eps.bmi.recsys.recommender.similarity.Similarity;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author sergio
 */
public class UserKNNRecommender extends AbstractRecommender {

    private final Similarity similiarity;
    private final Map<Integer, Ranking> userSimilarity;

    public UserKNNRecommender(Ratings ratings, Similarity sim, int k) {
        super(ratings);
        this.similiarity = sim;

        this.userSimilarity = new HashMap<>();

        //Por cada usuario creamos un ranking añadiendo la similitud entre los vecinos
        this.ratings.getUsers().forEach((current_user) -> {
            Ranking ranking = new RankingImpl(k);

            //Realizamos la similitud con cada usuario excepto con el mismo
            this.ratings.getUsers().stream().filter((sim_user) -> (!Objects.equals(current_user, sim_user))).forEachOrdered((sim_user) -> {
                double similarityScore = this.similiarity.sim(current_user, sim_user);

                //Lo añadimos al ranking de usuario
                ranking.add(sim_user, similarityScore);
            });

            //Finalmente metemos el ranking que se ha generado por usuario en el mapa
            userSimilarity.put(current_user, ranking);
        });

    }

    @Override
    public double score(int user, int item) {
        double parcialScore = 0;

        for (RankingElement current_ranking : this.userSimilarity.get(user)) {
            //Obtenemos el score del usuario vecino del item
            Double userScore = this.ratings.getRating(current_ranking.getID(), item);

            if (userScore != null && userScore > 0) {
                //Lo multiplicamos por la similitud entre el usuario y el vecino del usuario
                double finalUserScore = userScore * current_ranking.getScore();

                //Sumamos los scores parciales de cada vecino
                parcialScore += finalUserScore;
            }

        }

        return parcialScore;

    }
    
    @Override
    public String toString(){
        return "user-based kNN " + this.similiarity;
    }

}
