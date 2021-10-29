package com.baringhaus.sudoku.exceptions;

public class IllegalMoveException extends Exception{

    public IllegalMoveException(String message){
        super(message);
    }
}
