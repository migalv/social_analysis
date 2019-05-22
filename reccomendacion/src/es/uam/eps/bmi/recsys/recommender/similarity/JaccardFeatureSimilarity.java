/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.recsys.recommender.similarity;

import es.uam.eps.bmi.recsys.data.Features;
import java.util.Set;

/**
 *
 * @author sergio
 */
public class JaccardFeatureSimilarity<F> extends FeatureSimilarity<F> {

    public JaccardFeatureSimilarity(Features<F> features) {
        super(features);
    }

    @Override
    public double sim(int x, int y) {
        int commonFeatures = 0, sizeX, sizeY;
        double finalScore;

        //Primero vemos cuantas caracterisitcas comparten los 2
        commonFeatures = this.getFeatures().getFeatures(x).stream().filter((current_feature) -> (this.getFeatures().getFeatures(y).contains(current_feature))).map((_item) -> 1).reduce(commonFeatures, Integer::sum);

        Set<F> jx=this.getFeatures().getFeatures(x);
                Set<F> jy=this.getFeatures().getFeatures(y);

        
        //Obtenemos el numero de caracteristicas de cada item
        sizeX = this.getFeatures().getFeatures(x).size();
        sizeY = this.getFeatures().getFeatures(y).size();

        //Finalmente calculamos jaccard
        finalScore = (double) commonFeatures / (sizeX + sizeY - commonFeatures);

        return finalScore;
    }
    
    @Override
    public String toString(){
        return "(Jaccard on item feature)";
    }
}
