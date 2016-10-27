package ben.work.lipuhossain.productivitymanager.models;

/**
 * Created by lipuhossain on 10/9/16.
 */

public class History {
    private String date = null;
    private String target_productivity = null;
    private String actual_productivity = null;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTarget_productivity() {
        return target_productivity;
    }

    public void setTarget_productivity(String target_productivity) {
        this.target_productivity = target_productivity;
    }

    public String getActual_productivity() {
        return actual_productivity;
    }

    public void setActual_productivity(String actual_productivity) {
        this.actual_productivity = actual_productivity;
    }
}
