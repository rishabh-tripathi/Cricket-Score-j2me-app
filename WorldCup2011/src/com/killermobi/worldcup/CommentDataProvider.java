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
public class CommentDataProvider extends DataProvider {

    // Declare static values to identify the provided data
    private static final String NAME_PROPERTY = "name";
    private static final String ON_PROPERTY = "on";
    private static final String COMMENT_PROPERTY = "comment";

    // create a value variable

    private String name = "unknown";
    private String on = "unknown";
    private String comment = "unknown";

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
        dispatchUpdateEvent(COMMENT_PROPERTY);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        dispatchUpdateEvent(NAME_PROPERTY);
    }

    public String getOn() {
        return on;
    }

    public void setOn(String on) {
        this.on = on;
        dispatchUpdateEvent(ON_PROPERTY);
    }
    
    // override the parent method to handle user defined value
    protected Object getUserDefinedValue(String property) {

        // handle custom properties requests
        if (NAME_PROPERTY.equals(property)) {
            //return Kuix.getCanvas().getPlatformName();
            return this.name;
        }
        if (ON_PROPERTY.equals(property)) {
            return this.on;
        }

        if (COMMENT_PROPERTY.equals(property)) {
            return this.comment;
        }
        // default behavior if the property has not been found
        return null;
    }

}
