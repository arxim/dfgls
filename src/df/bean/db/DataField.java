/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package df.bean.db;

import java.sql.Types;
import java.text.NumberFormat;

/**
 *
 * @author Pong
 */
public class DataField {
    protected String name;
    protected int type;
    protected String value;
    protected boolean pk;
    
    public DataField() {
        this("", Types.VARCHAR, "");
    }
    
    public DataField(String name, int type, String value) {
        this(name, type, value, false);
    }
    
    public DataField(String name, int type, String value, boolean pk) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.pk = pk;
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getType() {
        return this.type;
    }
    
    public String getValue() {
        return this.value == null ? "" : this.value;
    }
    
    public String getValueToDisplay() {
        switch (this.type) {
            case Types.VARCHAR :
                return this.value == null || this.value.equalsIgnoreCase("null") ? "" : this.value;
            case Types.DOUBLE :
                return NumberFormat.getNumberInstance().format(Double.parseDouble(this.value));
        }
        return this.value;
    }
}
