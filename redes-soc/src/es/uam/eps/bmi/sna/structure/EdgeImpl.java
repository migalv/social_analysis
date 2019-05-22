/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.sna.structure;

/**
 *
 * @author migal
 */
public class EdgeImpl extends Edge {
    
    public EdgeImpl(Comparable u, Comparable v) {
        super(u, v);
    }
    
    @Override
    public boolean equals(Object obj){
        if(obj instanceof Edge){
            if(this.first.compareTo(((Edge) obj).first) == 0){
                if(this.second.compareTo(((Edge) obj).second) == 0)
                    return true;
            }
            else if(this.first.compareTo(((Edge) obj).second) == 0){
                if(this.second.compareTo(((Edge) obj).first) == 0)
                    return true;
            }
        }
        
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }
}
