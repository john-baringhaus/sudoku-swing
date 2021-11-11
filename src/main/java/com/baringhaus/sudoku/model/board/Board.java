package com.baringhaus.sudoku.model.board;

import com.baringhaus.sudoku.exceptions.IllegalBoardException;
import com.baringhaus.sudoku.model.Pair;
import com.baringhaus.sudoku.model.Triplet;

import java.io.Serializable;
import java.util.*;

public class Board implements Serializable {

    private final Square[][] squares;
    int size, numCols, numRows;

    public Board(int size){

        this.size = size*size;
        this.numCols = size;
        this.numRows = size;

        squares = new Square[size][size];
        clear();
        int a =0;
    }

    public Board(Board b) {
        this.size = b.getSize();
        this.numCols = b.getNumCols();
        this.numRows = b.getNumRows();
        this.squares = new Square[b.getNumCols()][b.getNumRows()];
        for(int x = 0; x < squares.length; x++) {
            for(int y = 0; y < squares.length; y++) {

                squares[x][y] = new Square(b.getValue(x,y));
            }
        }
    }

    public Board(String s) throws IllegalBoardException {
        String[] lines = s.split("\n");
        for(String l : lines) {
            if(l.length() != lines.length)
                throw new IllegalBoardException("Board width does not match height");
        }
        squares = new Square[lines.length][lines.length];
        for(int x = 0; x < lines.length; x++) {
            for(int y = 0; y < lines[x].length(); y++) {
                int value = ( lines[x].charAt(y) >=1 && lines[x].charAt(y) <= 9 ? lines[x].charAt(y): 0);
                squares[x][y].setValue(value);
            }
        }
    }

    public int getSize() {
        return squares.length * squares[0].length;
    }

    public int getValue(int col, int row) {
        return squares[col][row].getValue();
    }

    public void setValue(int col, int row, int value){
        squares[col][row].setValue(value);
    }

    public Square[][] getCols() {
        return squares;
    }

    public void clear() {
        for(int x = 0; x < squares.length; x++) {
            for(int y = 0; y < squares[0].length; y++) {
                squares[x][y] = new Square();
            }
        }
    }

    public int getNumCols() {
        return numCols;
    }

    public int getNumRows(){
        return numRows;
    }

    public List<Pair<Integer, Integer>> emptySquaresSorted() {
        List<Triplet<Integer, Integer, Integer>> emptySquares = new ArrayList<>();
        for(int x = 0; x < getNumCols(); x++) {
            for(int y = 0; y < getNumRows(); y++) {
                if(getValue(x,y) == 0)
                    emptySquares.add(new Triplet<>(x,y,getValue(x,y)));
            }
        }
        Collections.sort(emptySquares);
        List<Pair<Integer, Integer>> pairs = new ArrayList<>();
        for(Triplet<Integer, Integer, Integer> t : emptySquares) {
            pairs.add(Pair.of(t.fst, t.snd));
        }
        return pairs;
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
                ", squares=" + Arrays.deepToString(squares) +
                ", size=" + size +
                '}';
    }
}



