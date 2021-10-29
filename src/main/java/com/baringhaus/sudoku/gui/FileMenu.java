package com.baringhaus.sudoku.gui;

import com.baringhaus.sudoku.gamelogic.GameLogic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class FileMenu extends JMenu {

    JMenu fileMenu;

    public FileMenu(GameLogic gameLogic) {
        this.fileMenu=new JMenu("File");
        fileMenu.setFont(new Font("Sansserif", Font.BOLD, 16));
        fileMenu.setOpaque(true);
        fileMenu.setBorder(BorderFactory.createCompoundBorder(null,BorderFactory.createEmptyBorder(3, 3, 3, 3)));

        JMenuItem i1=new JMenuItem("New Game");
        i1.addActionListener( (event) -> {
//            board.newGame
            repaint();
        });

        i1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        i1.setBorder(BorderFactory.createEmptyBorder(3, 0, 3, 0));

        JMenuItem i2= new JMenuItem("Load Last");
        i2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        i2.setBorder(BorderFactory.createEmptyBorder(3, 0, 3, 0));

        JMenuItem i3 = new JMenuItem("Quit");
        i3.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
        i3.setBorder(BorderFactory.createEmptyBorder(3, 0, 3, 0));
        i3.addActionListener(ev -> System.exit(0));

        fileMenu.add(i1);
        fileMenu.add(i2);
        fileMenu.addSeparator();
        fileMenu.add(i3);
    }
}
