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
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sergio
 */
public class RatingsImpl implements Ratings {

    private String dataPath;
    private String separator;
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

    public RatingsImpl() {

        //Inicializamos los diferentes maps
        users = new HashMap<>();
        items = new HashMap<>();
        ratings = new HashMap<>();

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
                    if (itemsReader.length > 2) {
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
        return (ratings.containsKey(user))? ratings.get(user).get(item):null;
    }

    @Override
    public Set<Integer> getUsers(int item) {
        return(items.containsKey(item))? items.get(item):null;
    }

    @Override
    public Set<Integer> getItems(int user) {
        return (users.containsKey(user))? users.get(user):null;
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
        int nRatings=0;
        
        nRatings = this.getUsers().stream().map((current_user) -> this.getItems(current_user).size()).reduce(nRatings, Integer::sum);
        
        return nRatings;
    }

    @Override
    public Ratings[] randomSplit(double ratio) {
        Ratings folds[];
        folds = new Ratings[2];
        Random randomNumber = new Random();

        folds[0] = new RatingsImpl();
        folds[1] = new RatingsImpl();

        //Recorremos toda la lista de ratings
        this.ratings.forEach((user, rates) -> {
            rates.forEach((item, finalRate) -> {

                //Si el random supera el ratio lo metemos en la segunda lista de test
                if (randomNumber.nextFloat() > ratio) {
                    folds[1].rate(user, item, finalRate);
                } else {//En caso contrario lo añadimos a la primera lista de train
                    folds[0].rate(user, item, finalRate);
                }
            });
        });

        return folds;

    }

}
