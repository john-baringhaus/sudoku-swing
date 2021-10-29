package com.baringhaus.sudoku.api;

import com.baringhaus.sudoku.model.board.Board;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SudokuAPI{

    private static final String API_URL = "http://www.cs.utep.edu/cheon/ws/sudoku/new/?size=%d&level=%d";

    private static Board instance;

    public static Board getNewBoard(int size, int level) {
        if(instance == null) {
            instance = getInstance();
        }

        HttpURLConnection con = null;
        try {
            URL url = new URL(String.format(API_URL, size, level));
            con = (HttpURLConnection) url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            Gson g = new Gson();
            return g.fromJson(response.toString(), Board.class);
        } catch (IOException e) {
            //e.printStackTrace();
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
        return null;
    }

    public static Board getInstance(){
        if(instance == null) {
            instance = new Board();
        }
        return instance;
    }
}
