package com.baringhaus.sudoku;

import java.awt.*;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.prefs.Preferences;
import javax.swing.*;

import com.baringhaus.sudoku.gamelogic.GameLogic;
import com.baringhaus.sudoku.gui.SudokuBoardDisplay;

public class Sudoku extends JFrame {

    GameLogic board;
    private final Preferences prefs = Preferences.userNodeForPackage(com.baringhaus.sudoku.Sudoku.class);

    public Sudoku() {

        board = new GameLogic(9);

        SudokuBoardDisplay _sudokuBoard = new SudokuBoardDisplay(board);


        MenuBar mb = new MenuBar();
        mb.add(createFileMenu());
        mb.add(createGameMenu());
        mb.setBorder(BorderFactory.createRaisedBevelBorder());


        JPanel sep = new JPanel();
        sep.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));


        JPanel content = new JPanel();
        content.setLayout(new BorderLayout());


        content.add(mb, BorderLayout.NORTH);
        content.add(sep);
        content.add(_sudokuBoard, BorderLayout.CENTER);


        setContentPane(content);
        setTitle("Sudoku");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
    }

    public static class MenuBar extends JMenuBar {
        Color bgColor = Color.WHITE;

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(bgColor);
            g2d.fillRect(0, 0, getWidth() - 1, getHeight() - 1);

        }
    }

    private JMenu createFileMenu() {

        //Create menu
        JMenu fileMenu = new JMenu("Sudoku");
        fileMenu.setFont(new Font("SanSerif", Font.BOLD, 16));
        fileMenu.setOpaque(true);
        fileMenu.setBorder(BorderFactory.createCompoundBorder(null, BorderFactory.createEmptyBorder(3, 4, 3, 4)));

        JMenuItem i1 = new JMenuItem("New Game");
        i1.addActionListener((event) -> {
            int level = (prefs.getInt("difficulty", 1));
            board.newGame(level, 9);
            repaint();
        });

        i1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        i1.setBorder(BorderFactory.createEmptyBorder(3, 0, 3, 0));

        JMenuItem i2 = new JMenuItem("Load Last");
        i2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        i2.setBorder(BorderFactory.createEmptyBorder(3, 0, 3, 0));

        JMenuItem i3 = new JMenuItem("Quit");
        i3.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        i3.setBorder(BorderFactory.createEmptyBorder(3, 0, 3, 0));
        i3.addActionListener(ev -> System.exit(0));

        fileMenu.add(i1);
        fileMenu.add(i2);
        fileMenu.addSeparator();
        fileMenu.add(i3);

        return fileMenu;
    }

    private JMenu createGameMenu() {

        ////////////////////////////
        //////////Undo Move/////////
        ////////////////////////////

        JMenuItem undo = new JMenuItem("Undo Move");
        undo.setBorder(BorderFactory.createEmptyBorder(3, 6, 3, 3));
        undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        undo.addActionListener(e -> {
            board.undoMove();
            if (board.turnsSize() == 0) {
                undo.setEnabled(false);
            }
            repaint();
        });

        ////////////////////////////
        //////////Redo Move/////////
        ////////////////////////////

        JMenuItem redo = new JMenuItem("Redo Move");
        redo.setBorder(BorderFactory.createEmptyBorder(3, 6, 3, 3));
        redo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() + InputEvent.SHIFT_MASK));
        redo.addActionListener(e -> {
            board.redoMove();
            if (board.redoSize() == 0) {
                redo.setEnabled(false);
            }
            repaint();
        });

        ////////////////////////////
        /////Difficulty Submenu/////
        ////////////////////////////

        JMenu difficulty = new JMenu("Difficulty");
        ButtonGroup difficultyGroup = new ButtonGroup();
        difficulty.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 6));

        JMenuItem hard = new JRadioButtonMenuItem("Hard");
        hard.setBorder(BorderFactory.createEmptyBorder(3, 6, 3, 3));
        hard.setSelected(prefs.getInt("difficulty", 1) == 3);
        hard.addActionListener(e -> prefs.putInt("difficulty", 3));


        JMenuItem med = new JRadioButtonMenuItem("Medium");
        med.setBorder(BorderFactory.createEmptyBorder(3, 6, 3, 3));
        med.setSelected(prefs.getInt("difficulty", 1) == 2);
        med.addActionListener(e -> prefs.putInt("difficulty", 2));


        JMenuItem easy = new JRadioButtonMenuItem("Easy");
        easy.setBorder(BorderFactory.createEmptyBorder(3, 6, 3, 3));
        easy.setSelected(prefs.getInt("difficulty", 1) == 1);
        easy.addActionListener(e -> prefs.putInt("difficulty", 1));

        difficultyGroup.add(easy);
        difficultyGroup.add(med);
        difficultyGroup.add(hard);

        difficulty.add(easy);
        difficulty.add(med);
        difficulty.add(hard);

        ////////////////////////////
        ////////Size Submenu////////
        ////////////////////////////

        JMenu size = new JMenu("Size");
        ButtonGroup sizeGroup = new ButtonGroup();
        size.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 6));

        JMenuItem small = new JRadioButtonMenuItem("Small");
        small.setBorder(BorderFactory.createEmptyBorder(3, 6, 3, 3));
        small.setSelected(prefs.getInt("size", 4) == 4);
        small.addActionListener(e -> prefs.putInt("size", 4));

        JMenuItem large = new JRadioButtonMenuItem("Large");
        small.setBorder(BorderFactory.createEmptyBorder(3, 6, 3, 3));
        small.setSelected(prefs.getInt("size", 9) == 4);
        small.addActionListener(e -> prefs.putInt("size", 9));

        sizeGroup.add(small);
        sizeGroup.add(large);

        size.add(small);
        size.add(large);

        ////////////////////////////
        //////////Hint Item/////////
        ////////////////////////////

        JMenuItem hint = new JMenuItem("Hint");
        hint.setBorder(BorderFactory.createEmptyBorder(3, 6, 3, 3));
        hint.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        hint.addActionListener(e -> {
            board.getHint();
            repaint();
        });

        ////////////////////////////
        ///////////Check////////////
        ////////////////////////////

        ButtonGroup checkGroup = new ButtonGroup();
        JMenuItem check = new JCheckBoxMenuItem("Check");
        check.setBorder(BorderFactory.createEmptyBorder(3, 6, 3, 3));
        check.setSelected(SudokuBoardDisplay.check);
        check.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        check.addActionListener(e -> {
            SudokuBoardDisplay.check = !SudokuBoardDisplay.check;
            repaint();
        });
        checkGroup.add(check);

        ////////////////////////////
        ////////Menu Header/////////
        ////////////////////////////
        JMenu menu = new JMenu("Game");
        menu.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                undo.setEnabled(board.turnsSize() > 0);
                redo.setEnabled(board.redoSize() > 0);
            }

            @Override
            public void mousePressed(MouseEvent e) {
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
        });

        menu.setFont(new Font("SanSerif", Font.PLAIN, 14));
        menu.setOpaque(true);
        menu.setBorder(BorderFactory.createCompoundBorder(null, BorderFactory.createEmptyBorder(3, 4, 3, 4)));

        menu.add(undo);
        menu.add(redo);
        menu.addSeparator();
        menu.add(difficulty);
//        menu.add(size);
        menu.addSeparator();
        menu.add(hint);
        menu.add(check);

        return menu;
    }

    public static void main(String[] args) {
        new Sudoku().setVisible(true);
    }

}