/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.recsys.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 *
 * @author sergio
 */
public class FeaturesCentroid<F> implements Features {

    private final Map<Integer, Map<F, Double>> items;


    public FeaturesCentroid() {
        //Inicializamos el map
        items = new HashMap<>();
    }

    @Override
    public Set getFeatures(int id) {
        return (items.containsKey(id))? items.get(id).keySet():null;
    }

    @Override
    public Double getFeature(int id, Object feature) {
        return (items.containsKey(id) && items.get(id).containsKey(feature) )? 
                items.get(id).get(feature):null;
    }

    @Override
    public void setFeature(int id, Object feature, double value) {
        
        //Si existe el item
        if (items.containsKey(id)) {
            //Si contiene la caracteristica la remplazamos
            if (items.get(id).containsKey(feature)) {
                items.get(id).replace((F) feature, value);
            } else {//Si no contiene la caracteristica la creamos
                items.get(id).put((F) feature, value);
            }
        } else {//Si no existe el item lo creamos y añadimos la caracteristica
            Map<F, Double> aux = new HashMap<>();
            aux.put((F) feature, value);

            items.put(id, aux);
        }

    }

    @Override
    public Set getIDs() {
        return (items.size()>0)? items.keySet():null;
    }
    
}
