/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.recsys.metric;

import es.uam.eps.bmi.recsys.Recommendation;
import es.uam.eps.bmi.recsys.data.Ratings;

/**
 *
 * @author sergio
 */
public class Recall implements Metric{

    private final Ratings ratings;
    private final int threshold;
    private final int cutoff;
    
    public Recall(Ratings test, int threshold, int cutoff) {
        this.ratings=test;
        this.threshold=threshold;
        this.cutoff=cutoff;
    }

    @Override
    public double compute(Recommendation rec) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
