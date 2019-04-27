/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.recsys.metric;

import es.uam.eps.bmi.recsys.Recommendation;
import es.uam.eps.bmi.recsys.data.Ratings;
import es.uam.eps.bmi.recsys.ranking.RankingElement;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author sergio
 */
public class Rmse implements Metric {

    private final Ratings ratings;

    public Rmse(Ratings test) {
        this.ratings = test;
    }

    @Override
    public double compute(Recommendation rec) {
        double scoreParcial = 0, finalScore;
        int num_test = 0;
        Map <Integer, Double> rankingRatings;

        //Recorremos todos los usuarios
        for (int current_user : this.ratings.getUsers()) {
            
            //Por cada usuario creamos un hashmap con todas las recomendaciones para acceder a ellas
            rankingRatings= new HashMap<>();
            for(RankingElement userRank: rec.getRecommendation(current_user)){
                rankingRatings.put(userRank.getID(),userRank.getScore());
            }
            
            //Por cada item de cada usuario 
            for (int current_item : this.ratings.getItems(current_user)) {
                
                //Si existe la recomendacion del item del usuario actual
                if(rankingRatings.containsKey(current_item)){
                    //Por un lado obtenemos la recomendacion del item del usuario actual
                    double recommendedRating= rankingRatings.get(current_item);
                    
                    //Por otro lado obtenemos el rating actual del item y del usuario actual
                    double rating= this.ratings.getRating(current_user, current_item);
                    
                    //Finalmente aplicamos RMSE y se lo sumamos al score parcial
                    double squareResult= Math.pow(recommendedRating-rating,2);
                    scoreParcial+= squareResult;
                    
                    //Sumamos uno al numero de test validos
                    num_test++;
                }
            }
        }
        
        //Por ultimo dividimos por el numero de tests y aplicamos la raiz cuadrada
        finalScore=Math.sqrt(scoreParcial/num_test);

        return finalScore;
    }
    
    @Override
    public String toString(){
        return "Rmse";
    }

}
