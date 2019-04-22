/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.recsys.data;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sergio
 * @param <F>
 */
public class FeaturesImpl<F> implements Features<F> {

    private final Map<Integer, Map<F, Double>> items;
    private final String dataPath;
    private final String separator;
    private final Parser<F> featureParser;

    public FeaturesImpl(String dataPath, String separator, Parser<F> featureParser) {
        this.dataPath = dataPath;
        this.separator = separator;
        this.featureParser = featureParser;

        //Inicializamos el map
        items = new HashMap<>();

        //Por ultimo leemos los datos de todos los elementos
        this.getItems();
    }

    private void getItems() {
        String line;
        String itemsReader[];
        BufferedReader buffer;
        try {
            buffer = new BufferedReader(new FileReader(this.dataPath));
            try {
                //Leemos todas las lineas del fichero
                while ((line = buffer.readLine()) != null) {
                    //Lo separamos a traves del separador que nos han enviado
                    itemsReader = line.split(this.separator);

                    //Comprobamos que haya 3 elementos
                    if (itemsReader.length == 3) {
                        //Añadimos una nueva caracteristica al elemento
                        this.setFeature(Integer.parseInt(itemsReader[0]), this.featureParser.parse(itemsReader[1]), Double.parseDouble(itemsReader[2]));
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(FeaturesImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FeaturesImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

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
