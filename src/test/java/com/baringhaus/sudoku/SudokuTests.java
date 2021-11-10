package com.baringhaus.sudoku;

import com.baringhaus.sudoku.exceptions.IllegalMoveException;
import com.baringhaus.sudoku.model.board.Board;
import com.baringhaus.sudoku.model.board.Square;
import com.baringhaus.sudoku.exceptions.IllegalBoardException;
import com.baringhaus.sudoku.gamelogic.GameLogic;
import com.sun.tools.javac.util.Pair;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class SudokuTests {

    GameLogic gameLogic;

    @Before
    public void init() {
        gameLogic = new GameLogic(9);
    }

    @Test
    public void testTest() {
        assertTrue(true);
    }

    @Test
    public void boardConstructorWorksCorrectlyTest() {
        Board b = new Board(9);
        int count = 0;
        for (Square[] col : b.getCols()) {
            count += col.length;
        }

        assertEquals("Board is not 9x9", 81, count);
    }

    @Test
    public void getUnassignedTest() {

        assertEquals("Bad first unassigned", new Pair<>(0, 0), gameLogic.unassigned());
        try {
            for (int x = 0; x < gameLogic.getBoard().getCols().length; x++) {
                gameLogic.takeTurn(x, 0, 2);
            }
        } catch (Exception ignored) {
        }
        assertEquals("Bad first unassigned", new Pair<>(0, 1), gameLogic.unassigned());
    }

    @Test
    public void getValueReturnsCorrectValueTest() {
        try {
            gameLogic.takeTurn(2, 2, 8);
            assertEquals("Value not set correctly", 8, gameLogic.getValue(2, 2));
        }
        catch(IllegalMoveException e) {
            fail("Take turn threw exception: " + e.getMessage());
        }
    }

    @Test
    public void boardFromStringTest() {
        String s = "1234\n1234\n1234\n1234";
        try {
            gameLogic = new GameLogic(new Board(s));
        } catch (IllegalBoardException ex) {
            fail("Board threw exception");
        }
        int bSize = gameLogic.getBoard().getSize();
        assertEquals(String.format("Board size should be 16, but is %d", bSize), 16, gameLogic.getBoard().getSize());
    }

    @Test
    public void isSolvableTest() {
        gameLogic.copySubgridToGrid(0, 0, gameLogic.makeSubgrid());
        gameLogic.copySubgridToGrid(1, 1, gameLogic.makeSubgrid());
        gameLogic.copySubgridToGrid(2, 2, gameLogic.makeSubgrid());
        assertTrue("Unable to solve", gameLogic.sudoku());
    }

    @Test
    public void newGameEasyTest() {
        gameLogic = new GameLogic(9);
        gameLogic.newGame(1,9);
        assertEquals("Should be 32", 32, gameLogic.getBoard().emptySquares().size());

    }

    @Test
    public void newGameMediumTest() {
        gameLogic = new GameLogic(9);
        gameLogic.newGame(2, 9);
        assertEquals("Should be 43", 43, gameLogic.getBoard().emptySquares().size());
    }

    @Test
    public void newGameHardTest() {
        gameLogic = new GameLogic(9);
        gameLogic.newGame(3, 9);
        assertEquals("Should be 54", 54, gameLogic.getBoard().emptySquares().size());
    }

    @Test
    public void makeSubgridReturnsValidSubgrid() {

    }

    @Test

    public void valAlreadyInRowTest() {

    }

    @Test
    public void isValidPuzzle() {

    }

    @Test
    public void validValuesForGivenPositionTest() {

    }

    @Test
    public void minPuzzle() {

    }
}
