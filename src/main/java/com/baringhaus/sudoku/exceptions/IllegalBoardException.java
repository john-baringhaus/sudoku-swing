package com.baringhaus.sudoku.exceptions;

public class IllegalBoardException extends Exception{

    public IllegalBoardException(String message) {
        super(message);
    }
    public  IllegalBoardException() {
        super("Board could not be created");
    }
}
