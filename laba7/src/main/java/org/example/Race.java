package org.example;

import java.util.ArrayList;
import java.util.Arrays;

public class Race {
    private ArrayList<Stage> stages;  //этапы
    public ArrayList<Stage> getStages (){
        return stages;
    }
    public Race (Stage... stages){
        this.stages = new ArrayList<>(Arrays.asList(stages));
    }
}