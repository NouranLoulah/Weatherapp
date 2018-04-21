package com.example.nouran.weatherapp;

/**
 * Created by Nouran on 3/3/2018.
 */

public class Event {
    public long date;
    int min;
    int max;
    String main;
    String name;
    int icon;
    double day;

    public Event(long date, int min, int max, String main, String name, int icon, double day) {
        this.date = date;
        this.min = min;
        this.max = max;
        this.main = main;
        this.name = name;
        this.icon = icon;
        this.day = day;
    }

    public long getDate() {


        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getMin() {
        min = min - 273;
//        int n= (int)min;
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public double getMax() {
        max = max - 273;
//        int m= (int)max;
        return max;
    }

    public void setMax(int max) {
        this.max = max;

    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public double getDay() {
        return day;
    }

    public void setDay(double day) {
        this.day = day;
    }
}
