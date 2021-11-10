package com.baringhaus.sudoku.gui;

import com.baringhaus.sudoku.gamelogic.GameLogic;
import com.baringhaus.sudoku.model.Pair;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;

public class SudokuBoardDisplay extends JComponent {
    //================================================================ constants
    private static final int CELL_PIXELS = 50;  // Size of each cell.
    private static final int PUZZLE_SIZE = 9;   // Number of rows/cols
    private static final int SUBSQUARE = (int) Math.sqrt(PUZZLE_SIZE);   // Size of subsquare.
    private static final int BOARD_PIXELS = CELL_PIXELS * PUZZLE_SIZE;
    private static final int TEXT_OFFSET = 15;  // Fine-tuning placement of text.
    private static final Font TEXT_FONT = new Font("Sansserif", Font.BOLD, 24);
    private static final int GAME_MARGIN = 20;

    public static boolean check;

    GameLogic board;

    private Pair<Integer, Integer> _activeSquare;

    public SudokuBoardDisplay(GameLogic b) {
        setPreferredSize(new Dimension(BOARD_PIXELS + 2 + GAME_MARGIN * 2, BOARD_PIXELS + 2 + GAME_MARGIN * 2));
        setBackground(new Color(151, 158, 115));
        GridListener gridListener = new GridListener();
        addMouseListener(gridListener);
        addKeyListener(gridListener);
        setFocusable(true);
        board = b;

        _activeSquare = Pair.of(-1, -1);
    }

    public void updateBoard(GameLogic b) {
        this.board = b;
        repaint();

    }

    @Override
    public void paintComponent(Graphics g) {
        //... Draw background.
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(new Color(253, 240, 232, 255));
        g.fillRect(GAME_MARGIN, GAME_MARGIN, CELL_PIXELS * board.getBoard().getNumCols(), CELL_PIXELS * board.getBoard().getNumRows());
        if (_activeSquare.fst != -1) {
            g.setColor(new Color(117, 152, 159));
            g.fillRect((int) _activeSquare.fst * CELL_PIXELS + GAME_MARGIN, (int) _activeSquare.snd * CELL_PIXELS + GAME_MARGIN, CELL_PIXELS, CELL_PIXELS);
        }
        g.setColor(new Color(138, 87, 64));

        drawCellValues(g);
        drawGridLines(g);
    }

    private void drawGridLines(Graphics g) {

        //... Draw grid lines.  Terminates on <= to get final line.
        for (int i = 0; i <= board.getBoard().getNumCols(); i++) {
            int acrossOrDown = i * CELL_PIXELS;
            g.drawLine(acrossOrDown + GAME_MARGIN, GAME_MARGIN, acrossOrDown + GAME_MARGIN, BOARD_PIXELS + GAME_MARGIN);
            g.drawLine(GAME_MARGIN, acrossOrDown + GAME_MARGIN, BOARD_PIXELS + GAME_MARGIN, acrossOrDown + GAME_MARGIN);

            if (i % SUBSQUARE == 0) {
                acrossOrDown++;  // Move one pixel and redraw as above
                g.drawLine(acrossOrDown + GAME_MARGIN, GAME_MARGIN, acrossOrDown + GAME_MARGIN, BOARD_PIXELS + GAME_MARGIN);
                g.drawLine(GAME_MARGIN, acrossOrDown + GAME_MARGIN, BOARD_PIXELS + GAME_MARGIN, acrossOrDown + GAME_MARGIN);
            }
        }
    }

    private void drawCellValues(Graphics g) {
        g.setFont(TEXT_FONT);
        for (int i = 0; i < PUZZLE_SIZE; i++) {
            int xDisplacement = (i) * CELL_PIXELS + TEXT_OFFSET;
            for (int j = 0; j < PUZZLE_SIZE; j++) {
                if (board.getValue(i, j) != 0) {
                    int yDisplacement = (j + 1) * CELL_PIXELS - TEXT_OFFSET;
                    if (check && board.getValue(i, j) != board.getSolutionValue(i, j)) {
                        if (i == _activeSquare.fst && j == _activeSquare.snd) {
                            g.setColor(new Color(117, 152, 159));
                            g.fillRect(i * CELL_PIXELS + GAME_MARGIN, j * CELL_PIXELS + GAME_MARGIN, CELL_PIXELS, CELL_PIXELS);
                            g.setColor(new Color(158, 115, 117));
                        } else {
                            g.setColor(new Color(158, 115, 117));
                            g.fillRect(i * CELL_PIXELS + GAME_MARGIN, j * CELL_PIXELS + GAME_MARGIN, CELL_PIXELS, CELL_PIXELS);
                            g.setColor(new Color(138, 87, 64));
                        }
                    } else {
                        if (_activeSquare.fst != -1 && board.getValue(i, j) == board.getValue(_activeSquare.fst, _activeSquare.snd)) {
                            g.setColor(new Color(117, 152, 159, 128));
                            g.fillRect(i * CELL_PIXELS + GAME_MARGIN, j * CELL_PIXELS + GAME_MARGIN, CELL_PIXELS, CELL_PIXELS);
                        }
                        g.setColor(new Color(138, 87, 64));
                    }

                    g.drawString("" + board.getValue(i, j), xDisplacement + GAME_MARGIN, yDisplacement + GAME_MARGIN);

                }
            }
        }
    }

    public class GridListener implements MouseListener, KeyListener {

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            requestFocusInWindow();

            if (e.getX() >= GAME_MARGIN && e.getX() <= PUZZLE_SIZE * CELL_PIXELS + GAME_MARGIN && e.getY() >= GAME_MARGIN
                    && e.getY() <= PUZZLE_SIZE * CELL_PIXELS + GAME_MARGIN) {

                if ((int) _activeSquare.fst == (e.getX() - GAME_MARGIN) / CELL_PIXELS && (int) _activeSquare.snd == (e.getY() - GAME_MARGIN) / CELL_PIXELS) {
                    _activeSquare = Pair.of(-1, -1);
                } else {
                    _activeSquare = Pair.of((e.getX() - GAME_MARGIN) / CELL_PIXELS, (e.getY() - GAME_MARGIN) / CELL_PIXELS);
                }
            } else {
                _activeSquare = Pair.of(-1, -1);
            }

            repaint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {

        }

        @Override
        public void keyReleased(KeyEvent e) {

            int activeX = (int) _activeSquare.fst;
            int activeY = (int) _activeSquare.snd;
            int keyValue = e.getKeyChar() - 48;

            if (activeX != -1) {
                if ((keyValue >= 1 && keyValue <= 9) || e.getKeyCode() == 8) {
                    try {
                        if (board.isLegalUserMove(activeX, activeY, (e.getKeyCode() == 8 ? 0 : keyValue))) {
                            board.takeTurn(activeX, activeY, (e.getKeyCode() == 8 ? 0 : keyValue));
                            repaint();
                            if (board.gameComplete())
                                JOptionPane.showMessageDialog(null, "Good job", "Game Over", JOptionPane.INFORMATION_MESSAGE, UIManager.getIcon("OptionPane.informationIcon"));

                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage(), "Illegal move", JOptionPane.ERROR_MESSAGE, UIManager.getIcon("OptionPane.errorIcon"));
                    }
                } else if (e.getKeyCode() == 27) {
                    _activeSquare = Pair.of(-1, -1);
                    repaint();
                }
            }
        }
    }
}