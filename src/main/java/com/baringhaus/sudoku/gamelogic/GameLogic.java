package com.baringhaus.sudoku.gamelogic;

import com.baringhaus.sudoku.model.board.Square;
import com.baringhaus.sudoku.model.turn.Turn;
import com.baringhaus.sudoku.model.board.Board;
import com.baringhaus.sudoku.exceptions.IllegalMoveException;
import com.baringhaus.sudoku.model.Pair;


import java.util.*;

public class GameLogic {

    private Board board;
    private Board solution;
    private final List<Turn> turns = new ArrayList<>();
    private final List<Turn> redo = new ArrayList<>();


    public GameLogic(int size) {
        this.board = new Board(size);
    }

    public GameLogic(Board b) {
        this.board = new Board(b);
    }

    public void loadFile(List<String> board) {
        this.board = new Board(board.size());
        int x =0;
        for(String line: board) {
            int y = 0;
            for(char c : line.toCharArray()) {
                this.board.setValue(x,y, c - '0');
                y++;
            }
            x++;
        }

        sudoku();

        x = 0;
        for(String line: board) {
            int y = 0;
            for(char c : line.toCharArray()) {
                this.board.setValue(x,y, (c-'0'==0 ? 0 :this.board.getValue(x,y)));
                y++;
            }
            x++;
        }
    }

    public boolean isLegalUserMove(int col, int row, int val) throws IllegalMoveException {

        //Check to make sure col and row are valid positions on the board
        if (!(col < board.getNumCols() && col >= 0))
            throw new IllegalMoveException(String.format("Column %d is out of bounds", col));

        if (!(row < board.getNumRows() && row >= 0))
            throw new IllegalMoveException(String.format("Row %d is out of bounds", row));

        if (val == 0)
            return true;
        //Check row
        if (valAlreadyInCol(col, val))
            throw new IllegalMoveException(String.format("The number %d already exists in this column", val));
        //Check column
        if (valAlreadyInRow(row, val))
            throw new IllegalMoveException(String.format("The number %d already exists in this row", val));
        //Check sub grid
        if (valAlreadyInSubgrid(col, row, val))
            throw new IllegalMoveException(String.format("Number already exists\n in this %dx%d grid", board.getNumCols(), board.getNumRows()));

        return true;
    }

    private boolean valAlreadyInRow(int row, int val) {

        for (int x = 0; x < board.getNumCols(); x++) {
            if (board.getValue(x, row) == val) {
                return true;
            }
        }
        return false;
    }

    private boolean valAlreadyInCol(int col, int val) {

        for (int y = 0; y < board.getNumCols(); y++) {
            if (board.getValue(col, y) == val)
                return true;
        }
        return false;
    }

    private boolean valAlreadyInSubgrid(int col, int row, int val) {

        int subGridSize = (int) Math.sqrt(board.getNumCols());
        int startX = col / 3;
        int startY = row / 3;
        for (int x = 0; x < subGridSize; x++) {
            for (int y = 0; y < subGridSize; y++) {
                if (board.getValue(startX * subGridSize + x, startY * subGridSize + y) == val) {
                    return true;
                }
            }
        }
        return false;
    }

    public Pair<Integer, Integer> unassigned() {
        for (int x = 0; x < board.getNumCols(); x++) {
            for (int y = 0; y < board.getNumRows(); y++) {
                if (board.getValue(x, y) == 0) {
                    return new Pair<>(x, y);
                }
            }
        }
        return new Pair<>(-1, -1);
    }

    public boolean sudoku() {
        Pair<Integer, Integer> ra = unassigned();
        if (ra.fst == -1) {
            solution = new Board(board);
            return true;
        }
        int col = ra.fst;
        int row = ra.snd;

        for (int val : board.validValuesForSquare(col, row)) {
            board.setValue(col, row, val);
            if (sudoku()) {
                return true;
            }
            board.setValue(col, row, 0);
        }
        return false;
    }


    public void clear() {
        turns.clear();
        redo.clear();
        board.clear();
    }

    public boolean gameComplete() {
        int count = 0;
        for (int x = 0; x < board.getNumCols(); x++) {
            for(int y = 0; y < board.getNumRows(); y++) {
                count += (board.getValue(x,y)==0?0:1);
            }
        }
        return count == board.getNumCols() * board.getNumRows();
    }

    public int[][] makeSubgrid() {
        int[][] sub = new int[][]{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
        List<Integer> values = new LinkedList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
        Collections.shuffle(values);

        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                sub[x][y] = values.get(0);
                values.remove(0);
            }
        }
        return sub;
    }

    public void copySubgridToGrid(int row, int col, int[][] sub) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board.setValue(col * 3 + i, row * 3 + j, sub[i][j]);
            }
        }
    }

    public int getValue(int col, int row) {
        return board.getValue(col, row);
    }

    public int getSolutionValue(int col, int row) {
        return solution.getValue(col, row);
    }

    public void takeTurn(int col, int row, int val) throws IllegalMoveException {
        if (isLegalUserMove(col, row, val)) {
            turns.add(new Turn(col, row, val, board.getValue(col, row)));
            board.setValue(col, row, val);
            redo.clear();
        }
    }

    public Board getBoard() {
        return board;
    }

    //TODO Implement board size

    public void newGame(int level, int size) {
        clear();

        board = new Board(9);

        copySubgridToGrid(0, 0, makeSubgrid());
        copySubgridToGrid(1, 1, makeSubgrid());
        copySubgridToGrid(2, 2, makeSubgrid());

        sudoku();

        while (board.emptySquares().size() < 20 + level * 12 - level + 1) {
            int x;
            int y;
            int val;
            Random r = new Random();
            do {
                x = r.nextInt(board.getNumCols());
                y = r.nextInt(board.getNumRows());
                val = board.getValue(x, y);
            } while (val == 0);
            board.setValue(x, y, 0);
            if (numberOfSolutions(new Board(board)) > 1) {
                board.setValue(x, y, val);
            }
        }
    }

    private int numberOfSolutions(Board b) {

        if (b.emptySquares().isEmpty()) {
            return 1;
        }

        int col = b.emptySquares().get(0).fst;
        int row = b.emptySquares().get(0).snd;

        int count = 0;

        List<Integer> validValues = b.validValuesForSquare(col, row);
        Collections.shuffle(validValues);

        for (int num : validValues) {
            b.setValue(col, row, num);
            count += numberOfSolutions(b);
            b.setValue(col, row, 0);
        }
        return count;
    }


    public void getHint() {
        Map<Integer, Integer> values = new HashMap<>();
        for (int x = 0; x < board.getNumCols(); x++) {
            for (int y = 0; y < board.getNumRows(); y++) {
                if (board.getValue(x,y) != 0)
                    values.put(board.getValue(x,y), values.getOrDefault(board.getValue(x,y), 0) + 1);
            }
        }


        int emptyPairs = board.emptySquares().size();
        Random r = new Random();
        Pair<Integer, Integer> s;
        s = board.emptySquares().get(r.nextInt(emptyPairs));
        try {
            takeTurn(s.fst, s.snd, solution.getValue(s.fst, s.snd));
        }
        catch (IllegalMoveException m){
            System.out.println("Hint failed?");
        }
    }

    public void undoMove() {
        if(turns.size()==0)
            return;
        Turn t = turns.get(turns.size()-1);
        board.setValue(t.getX(), t.getY(), t.getOldValue());
        redo.add(t);
        turns.remove(turns.size()-1);
    }

    public void redoMove() {
        if(redo.size()==0)
            return;
        Turn t = redo.get(redo.size()-1);
        board.setValue(t.getX(), t.getY(), t.getNewValue());
        turns.add(t);
        redo.remove(redo.size()-1);
    }

    public int redoSize(){
        return redo.size();
    }

    public int turnsSize() {
        return turns.size();
    }

    public List<String> boardToFile() {
        List<String> output = new ArrayList<>();
        for(int x = 0; x < board.getNumCols(); x++) {
            StringBuilder line = new StringBuilder();
            for(int y = 0; y < board.getNumRows(); y++) {
                line.append(board.getValue(x, y));
            }
            output.add(line.toString());
        }
        return output;
    }
}
