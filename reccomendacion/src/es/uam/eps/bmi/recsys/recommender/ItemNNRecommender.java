/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.recsys.recommender;

import es.uam.eps.bmi.recsys.data.Ratings;

/**
 *
 * @author sergio
 */
public class ItemNNRecommender extends AbstractRecommender{

    public ItemNNRecommender(Ratings ratings) {
        super(ratings);
    }

    @Override
    public double score(int user, int item) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public String toString(){
        return "item-based NN  " ;
    }
    
}
