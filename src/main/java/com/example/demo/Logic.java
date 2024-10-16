package com.example.demo;

import javafx.util.Pair;

import java.util.*;

class Logic {

    public static boolean bfs(byte[][] matrix, byte[] firstPlayerPos, byte rowWin) {

        int rows = matrix.length;

        Set<String> visited = new HashSet<>();
        Queue<int[]> queue = new LinkedList<>();

        queue.offer(new int[]{firstPlayerPos[0], firstPlayerPos[1], 0});
        visited.add(firstPlayerPos[0] + "," + firstPlayerPos[1]);

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        while (!queue.isEmpty()) {
            int[] current = queue.poll();

            int col = current[0];
            int row = current[1];
            int distance = current[2];


            if ( row == rowWin) {

                return true;
            }

            for (int[] dir : directions) {
                int newRow = row + dir[0];
                int newCol = col + dir[1];

                if (isValid(newRow, newCol, matrix) && !visited.contains(newCol + "," + newRow)) {
                    queue.offer(new int[]{newCol, newRow, distance + 1});
                    visited.add(newCol + "," + newRow);
                }
            }
        }

        return false;
    }

    private static boolean isValid(int row, int col, byte[][] matrix) {
        return row >= 0 && row < matrix.length && col >= 0 && col < matrix[row].length && matrix[row][col] != 1;
    }
}

