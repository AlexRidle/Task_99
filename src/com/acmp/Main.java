package com.acmp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private static byte levelsNum;
    private static byte heightNum;
    private static byte widthNum;
    private static char[][][] levels;
    private static int moveCount = 0;
    private static boolean isQueenNearby = false;
    private static ArrayList<byte[]> currentMoves = new ArrayList<>();
    private static ArrayList<byte[]> possibleMoves = new ArrayList<>();

    public static void main(String[] args) throws FileNotFoundException, CustomException {
        readFileAndDoSetup();
        findQueen();
        writeFile();
    }

    private static void findQueen() {
        while (!isQueenNearby) {
            moveCount++;
            for (byte[] location : currentMoves) {
                checkNearestCells(location[0], location[1], location[2]);
            }
            currentMoves.clear();
            for (int i = 0; i < possibleMoves.size(); i++) {
                currentMoves.add(i, possibleMoves.get(i));
            }
            possibleMoves.clear();
        }
    }

    private static void readFileAndDoSetup() throws FileNotFoundException, CustomException {
        Scanner scanner = new Scanner(new File("INPUT.txt"));
        levelsNum = scanner.nextByte();
        heightNum = scanner.nextByte();
        widthNum = scanner.nextByte();
        checkRules();
        levels = new char[levelsNum][heightNum][widthNum];
        String line;
        char[] cellsInLine;
        for (byte lvlNum = 0; lvlNum < levelsNum; lvlNum++) {
            for (byte lvlColumn = 0; lvlColumn < heightNum; lvlColumn++) {
                line = scanner.next();
                cellsInLine = line.toCharArray();
                for (byte lvlWidth = 0; lvlWidth < widthNum; lvlWidth++) {
                    levels[lvlNum][lvlColumn][lvlWidth] = cellsInLine[lvlWidth];
                    if (cellsInLine[lvlWidth] == '1') {
                        addToCurrentCells(lvlNum, lvlColumn,lvlWidth);
                    }
                }
            }
        }
    }

    private static void checkNearestCells(byte lvl, byte lvlLine, byte lvlWidth) {
        if (lvlLine - 1 >= 0) { //up
            if (levels[lvl][lvlLine - 1][lvlWidth] == '.') {
                levels[lvl][lvlLine - 1][lvlWidth] = 'o';
                addToPossibleCells(lvl, (byte) (lvlLine - 1), lvlWidth);
            }
            checkOnQueen(lvl, (byte) (lvlLine - 1), lvlWidth);
        }

        if (lvlLine + 1 < heightNum) { //down
            if (levels[lvl][lvlLine + 1][lvlWidth] == '.') {
                levels[lvl][lvlLine + 1][lvlWidth] = 'o';
                addToPossibleCells(lvl, (byte) (lvlLine + 1), lvlWidth);
            }
            checkOnQueen(lvl, (byte) (lvlLine + 1), lvlWidth);
        }

        if (lvlWidth + 1 < widthNum) { //right
            if (levels[lvl][lvlLine][lvlWidth + 1] == '.') {
                levels[lvl][lvlLine][lvlWidth + 1] = 'o';
                addToPossibleCells(lvl, (lvlLine), (byte) (lvlWidth + 1));
            }
            checkOnQueen(lvl, lvlLine, (byte) (lvlWidth + 1));
        }

        if (lvlWidth - 1 >= 0) { //left
            if (levels[lvl][lvlLine][lvlWidth - 1] == '.') {
                levels[lvl][lvlLine][lvlWidth - 1] = 'o';
                addToPossibleCells(lvl, (lvlLine), (byte) (lvlWidth - 1));
            }
            checkOnQueen(lvl, lvlLine, (byte) (lvlWidth - 1));
        }

        if (lvl + 1 < levelsNum) { //breakFloor
            if (levels[lvl + 1][lvlLine][lvlWidth] == '.') {
                levels[lvl + 1][lvlLine][lvlWidth] = 'o';
                addToPossibleCells((byte) (lvl + 1), (lvlLine), lvlWidth);
            }
            checkOnQueen((byte) (lvl + 1), lvlLine, lvlWidth);
        }

        levels[lvl][lvlLine][lvlWidth] = 'o';
    }

    private static void addToPossibleCells(byte lvl, byte lvlLine, byte lvlWidth) {
        byte[] freeCell = new byte[]{lvl, lvlLine, lvlWidth};
        possibleMoves.add(freeCell);
    }

    private static void addToCurrentCells(byte lvl, byte lvlLine, byte lvlWidth) {
        byte[] freeCell = new byte[]{lvl, lvlLine, lvlWidth};
        currentMoves.add(freeCell);
    }

    private static void checkOnQueen(byte lvl, byte lvlLine, byte lvlWidth){
        if(levels[lvl][lvlLine][lvlWidth] == '2'){
            isQueenNearby = true;
        }
    }

    private static void checkRules() throws CustomException {
        boolean rightLvl = 2 <= levelsNum &&  50 >= levelsNum;
        boolean rightHeight = 2 <= heightNum &&  50 >= heightNum;
        boolean rightWidth = 2 <= widthNum &&  50 >= widthNum;
        if (!(rightHeight && rightLvl && rightWidth)) {
            throw new CustomException("Please, check INPUT.txt file for right maze settings.");
        }
    }

    private static void writeFile() {
        File file = new File("OUTPUT.txt");
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(file);
            writer.print(moveCount*5);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            writer.flush();
            writer.close();
        }
    }
}

class CustomException extends Exception {

    CustomException(String message) {
        super(message);
    }

}