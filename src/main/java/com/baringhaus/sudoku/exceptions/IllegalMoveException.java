package com.baringhaus.sudoku.exceptions;

public class IllegalMoveException extends Exception{

    public IllegalMoveException(String message, Throwable err){
        super(message, err);
    }
    public IllegalMoveException(String message){
        super(message);
    }
}
