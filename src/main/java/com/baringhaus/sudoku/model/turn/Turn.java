package com.baringhaus.sudoku.model.turn;

import com.baringhaus.sudoku.model.Pair;

public class Turn {

    private final Pair<Integer, Integer> location, values;

    public Turn(int x, int y, int newValue, int oldValue){
        location = new Pair<>(x,y);
        values = new Pair<>(newValue, oldValue);
    }

    public int getX(){
        return location.fst;
    }

    public int getY(){
        return location.snd;
    }

    public int getNewValue(){return values.fst;}

    public int getOldValue(){ return values.snd;}

}
