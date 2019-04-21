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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sergio
 */
public class RatingsImpl implements Ratings {

    private final String dataPath;
    private final String separator;
    private final Map<Integer, Set<Integer>> users;
    private final Map<Integer, Set<Integer>> items;
    private final Map<Integer, Map<Integer, Double>> ratings;

    public RatingsImpl(String dataPath, String separator) {
        this.dataPath = dataPath;
        this.separator = separator;

        //Inicializamos los diferentes maps
        users = new HashMap<>();
        items = new HashMap<>();
        ratings = new HashMap<>();

        //Por ultimo leemos los datos de todos los elementos
        this.getAllRatings();
    }

    private void getAllRatings() {
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
                        this.rate(Integer.parseInt(itemsReader[0]), Integer.parseInt(itemsReader[1]), Double.parseDouble(itemsReader[2]));
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
    public void rate(int user, int item, Double rating) {

        //Comprobamos que el user se encuentra en el mapa de usuarios: si no, lo añadimos
        if (!users.containsKey(user)) {
            users.put(user, new HashSet<>());
        }

        //Comprobamos que el elemento se encuentra en el mapa de elementos: si no, lo añadimos
        if (!items.containsKey(item)) {
            items.put(item, new HashSet<>());
        }

        //Comprobamos que el user se encuentra en el mapa de ratings: si no, lo añadimos
        if (!ratings.containsKey(user)) {
            ratings.put(user, new HashMap<>());
        }

        //Por ultimo añadimos los nuevos rates que nos han mandado
        users.get(user).add(item);
        items.get(item).add(user);
        ratings.get(user).put(item, rating);
    }

    @Override
    public Double getRating(int user, int item) {
        if (ratings.containsKey(user)) {
            return ratings.get(user).get(item);
        }
        return null;
    }

    @Override
    public Set<Integer> getUsers(int item) {
        if (items.containsKey(item)) {
            return items.get(item);
        }
        return null;
    }

    @Override
    public Set<Integer> getItems(int user) {
        if (users.containsKey(user)) {
            return users.get(user);
        }
        return null;
    }

    @Override
    public Set<Integer> getUsers() {
        return this.users.keySet();
    }

    @Override
    public Set<Integer> getItems() {
        return this.items.keySet();
    }

    @Override
    public int nRatings() {
        return this.ratings.size();
    }

    @Override
    public Ratings[] randomSplit(double ratio) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
