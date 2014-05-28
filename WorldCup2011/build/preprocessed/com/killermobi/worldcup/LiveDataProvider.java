/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.killermobi.worldcup;

import org.kalmeo.kuix.core.Kuix;
import org.kalmeo.kuix.core.model.DataProvider;

/**
 *
 * @author rishabh
 */
public class LiveDataProvider extends DataProvider {

    // Declare static values to identify the provided data
    private static final String MATCH_PROPERTY = "match";
    private static final String SCORE_PROPERTY = "score";
    private static final String ID_PROPERTY = "id";

    // create a value variable

    private String score = "unknown score";
    private String match = "unknown match";
    private String id = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        dispatchUpdateEvent(ID_PROPERTY);
    }

    public String getMatch() {
        return match;
    }

    public String getScore() {
        return score;
    }
   

    public void setMatch(String match) {
        this.match = match;
        dispatchUpdateEvent(MATCH_PROPERTY);
    }

    // create an accessor to dispatch an event when value is set
    public void setScore(String score) {
        this.score = score;
        dispatchUpdateEvent(SCORE_PROPERTY);
    }

    // override the parent method to handle user defined value
    protected Object getUserDefinedValue(String property) {

        // handle custom properties requests
        if (MATCH_PROPERTY.equals(property)) {
            //return Kuix.getCanvas().getPlatformName();        
            return this.match;
        }
        if (SCORE_PROPERTY.equals(property)) {
            return this.score;
        }

        if (ID_PROPERTY.equals(property)) {
            return this.id;
        }
        // default behavior if the property has not been found
        return null;
    }
    
}
