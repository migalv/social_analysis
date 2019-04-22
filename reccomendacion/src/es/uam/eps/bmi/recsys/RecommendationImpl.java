/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.recsys;

import es.uam.eps.bmi.recsys.ranking.Ranking;
import es.uam.eps.bmi.recsys.ranking.RankingElement;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author sergio
 */
public class RecommendationImpl implements Recommendation {

    Map<Integer, Ranking> recommendations;

    public RecommendationImpl() {
        this.recommendations = new HashMap<>();
    }

    @Override
    public Set<Integer> getUsers() {
        return recommendations.keySet();
    }

    @Override
    public Ranking getRecommendation(int user) {
        return (recommendations.containsKey(user)) ? recommendations.get(user) : null;
    }

    @Override
    public void add(int user, Ranking ranking) {
        recommendations.put(user, ranking);
    }

    @Override
    public void print(PrintStream out) {
        this.getUsers().forEach((current_user) -> {
            for (RankingElement current_ranking : this.getRecommendation(current_user)) {
                out.println(current_ranking.getID() + "\t" + current_ranking.getScore());
            }
        });
    }

    @Override
    public void print(PrintStream out, int userCutoff, int itemCutoff) {
        int user_n = 0;
        Set<Integer> users = this.getUsers();

        while (user_n < userCutoff) {
            int item_n = 0;

            Iterator<Integer> current_user = users.iterator();
            if (current_user.hasNext()) {
                for (RankingElement current_ranking : this.getRecommendation(current_user.next())) {
                    if(item_n < itemCutoff){
                        out.println(current_ranking.getID() + "\t" + current_ranking.getScore());
                        item_n++;
                    }else{
                        break;
                    }
                }
            }

            user_n++;
        }

    }

}
