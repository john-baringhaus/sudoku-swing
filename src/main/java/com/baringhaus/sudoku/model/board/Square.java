package com.baringhaus.sudoku.model.board;

public class Square {

    private int value;

    public Square(){
        value = 0;
    }
    public Square(Square sq) {
        this.value = sq.getValue();
    }
    public Square(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
    }


}