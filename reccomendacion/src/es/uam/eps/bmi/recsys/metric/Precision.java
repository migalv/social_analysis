/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.recsys.metric;

import es.uam.eps.bmi.recsys.Recommendation;
import es.uam.eps.bmi.recsys.data.Ratings;
import es.uam.eps.bmi.recsys.ranking.RankingElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author sergio
 */
public class Precision implements Metric {

    private final Ratings ratings;
    private final double threshold;
    private final int cutoff;

    public Precision(Ratings test, double threshold, int cutoff) {
        this.ratings = test;
        this.threshold = threshold;
        this.cutoff = cutoff;
    }

    @Override
    public double compute(Recommendation rec) {
        double precisionParcial = 0, precisionFinal, userCounter = 0;

        for (int current_user : this.ratings.getUsers()) {
            int counterRefactor = 0, positiveUser = 0;

            if (rec.getRecommendation(current_user).size() > 0) {
                userCounter++;

                for (RankingElement rankingRec : rec.getRecommendation(current_user)) {
                    Double scoreParcial = this.ratings.getRating(current_user, rankingRec.getID());

                    if (scoreParcial != null && scoreParcial >= this.threshold) {
                        positiveUser++;
                    }

                    counterRefactor++;
                    if (counterRefactor == this.cutoff) {
                        break;
                    }
                }
            }

            if (positiveUser > 0) {
                precisionParcial += ((double) positiveUser / this.cutoff);
            }

        }

        precisionFinal = ((double) precisionParcial / userCounter);

        return precisionFinal;

    }

    @Override
    public String toString() {
        return "Precision" + "@" + this.cutoff;
    }

}
