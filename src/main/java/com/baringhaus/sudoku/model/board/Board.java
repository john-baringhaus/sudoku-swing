package com.baringhaus.sudoku.model.board;

import com.baringhaus.sudoku.exceptions.IllegalBoardException;
import com.sun.tools.javac.util.Pair;

import java.io.Serializable;
import java.util.*;

public class Board implements Serializable {

    private final Map<Integer, Map<Integer, Square>> squares;
    int size, numCols, numRows;

    public Board(int size){

        this.size = size*size;
        this.numCols = size;
        this.numRows = size;

        squares = new HashMap<>();
        for(int col = 0; col < size; col++) {
            squares.put( col, new HashMap<>());
            for(int row = 0; row < size; row++) {
                squares.get(col).put(row, new Square());
            }
        }
    }

    public Board(Board b) {
        this.size = b.getSize();
        this.numCols = b.getNumCols();
        this.numRows = b.getNumRows();
        this.squares = new HashMap<>();
        for(Map.Entry<Integer, Map<Integer, Square>> c: b.getCols().entrySet()) {
            Map<Integer, Square> temp = new HashMap<>();
            for(Map.Entry<Integer, Square> r: c.getValue().entrySet()) {
                temp.put(r.getKey(), new Square(r.getValue()));
            }
            this.squares.put(c.getKey(), temp);
        }
    }

    public Board(String s) throws IllegalBoardException {
        String[] lines = s.split("\n");
        for(String l : lines) {
            if(l.length() != lines.length)
                throw new IllegalBoardException("Board width does not match height");
        }
        squares = new HashMap<>();
        for(int x = 0; x < lines.length; x++) {
            squares.put(x, new HashMap<>());
            for(int y = 0; y < lines[x].length(); y++) {
                int value = ( lines[x].charAt(y) >=1 && lines[x].charAt(y) <= 9 ? lines[x].charAt(y): 0);

                squares.get(x).put(y, new Square(value) );
            }
        }
    }

    public int getSize() {
        int sqCount = 0;

        for(Map.Entry<Integer, Map<Integer, Square>> e : squares.entrySet()) {
            sqCount += e.getValue().size();
        }
        return sqCount;
    }

    public int getValue(int col, int row) {
        return squares.get(col).get(row).getValue();
    }

    public void setValue(int col, int row, int value){
        squares.get(col).get(row).setValue(value);
    }

    public Map<Integer, Map<Integer, Square>> getCols() {
        return squares;
    }

    public void clear() {
        squares.clear();
    }

    public int getNumCols() {
        return numCols;
    }

    public int getNumRows(){
        return numRows;
    }

    public List<Pair<Integer, Integer>> emptySquares() {
        List<Pair<Integer, Integer>> emptySquares = new ArrayList<>();
        for(int x = 0; x < getNumCols(); x++) {
            for(int y = 0; y < getNumRows(); y++) {
                if(getValue(x,y) == 0)
                    emptySquares.add(new Pair<>(x,y));
            }
        }
        return emptySquares;
    }

    public List<Integer> validValuesForSquare(int col, int row) {
        int subgridsize = (int)Math.sqrt(getNumCols());
        int subgridcol =  col/subgridsize;
        int subgridrow =  row/subgridsize;

        List<Integer> values = new ArrayList<>();

        for(int i = 1; i <= getNumCols(); i++) {
            values.add(i);
        }

        for(int i = 0; i < getNumCols(); i++) {
            values.removeAll(Collections.singleton(getValue(col, i)));
            values.removeAll(Collections.singleton(getValue(i, row)));
            values.removeAll(Collections.singleton(getValue(subgridcol*subgridsize+(i%3), subgridrow*subgridsize+i/3)));
        }
        return values;

    }

    @Override
    public String toString() {
        return "Board{" +
                ", squares=" + squares +
                ", size=" + size +
                '}';
    }
}



