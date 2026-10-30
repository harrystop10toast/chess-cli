package org.example;

import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Main {
    private static final Pattern CASTLE = Pattern.compile("^(O-O-O|O-O|0-0-0|0-0)[+#]?$");
    private static final Pattern PIECE  = Pattern.compile("^([KQRBN])([a-h])?([1-8])?(x)?([a-h][1-8])[+#]?$");
    private static final Pattern PAWN   = Pattern.compile("^(?:([a-h])x)?([a-h][1-8])(?:=([QRBN]))?[+#]?$");

    private static final String BLACK = "♜♞♝♛♚♟";
    private static final String WHITE = "♖♘♗♕♔♙";
    public static void main(String[] args){
        while (true){

            //a loop is a game
            //create init board

            String[][] game = new String[9][9];
            game[0][0]="/";game[1][0]="8";game[2][0]="7";game[3][0]="6";game[4][0]="5";game[5][0]="4";game[6][0]="3";game[7][0]="2";game[8][0]="1";
            game[0][1]="a";game[0][2]="b";game[0][3]="c";game[0][4]="d";game[0][5]="e";game[0][6]="f";game[0][7]="g";game[0][8]="h";
            String B = "\u001B[90m";
            String W = "\u001B[97m";
            String R = "\u001B[0m";
            game[1][1] = B + "♜" + R;
            game[1][2] = B + "♞" + R;
            game[1][3] = B + "♝" + R;
            game[1][4] = B + "♛" + R;
            game[1][5] = B + "♚" + R;
            game[1][6] = B + "♝" + R;
            game[1][7] = B + "♞" + R;
            game[1][8] = B + "♜" + R;

            game[2][1] = B + "♟" + R;
            game[2][2] = B + "♟" + R;
            game[2][3] = B + "♟" + R;
            game[2][4] = B + "♟" + R;
            game[2][5] = B + "♟" + R;
            game[2][6] = B + "♟" + R;
            game[2][7] = B + "♟" + R;
            game[2][8] = B + "♟" + R;

            game[3][1] = " ";
            game[3][2] = " ";
            game[3][3] = " ";
            game[3][4] = " ";
            game[3][5] = " ";
            game[3][6] = " ";
            game[3][7] = " ";
            game[3][8] = " ";

            game[4][1] = " ";
            game[4][2] = " ";
            game[4][3] = " ";
            game[4][4] = " ";
            game[4][5] = " ";
            game[4][6] = " ";
            game[4][7] = " ";
            game[4][8] = " ";

            game[5][1] = " ";
            game[5][2] = " ";
            game[5][3] = " ";
            game[5][4] = " ";
            game[5][5] = " ";
            game[5][6] = " ";
            game[5][7] = " ";
            game[5][8] = " ";

            game[6][1] = " ";
            game[6][2] = " ";
            game[6][3] = " ";
            game[6][4] = " ";
            game[6][5] = " ";
            game[6][6] = " ";
            game[6][7] = " ";
            game[6][8] = " ";

            game[7][1] = W + "♙" + R;
            game[7][2] = W + "♙" + R;
            game[7][3] = W + "♙" + R;
            game[7][4] = W + "♙" + R;
            game[7][5] = W + "♙" + R;
            game[7][6] = W + "♙" + R;
            game[7][7] = W + "♙" + R;
            game[7][8] = W + "♙" + R;

            game[8][1] = W + "♖" + R;
            game[8][2] = W + "♘" + R;
            game[8][3] = W + "♗" + R;
            game[8][4] = W + "♕" + R;
            game[8][5] = W + "♔" + R;
            game[8][6] = W + "♗" + R;
            game[8][7] = W + "♘" + R;
            game[8][8] = W + "♖" + R;
            Scanner sc=new Scanner(System.in);
            boolean win=false;
            int turn=0; //turn=0 is white turn=1 is black
            while (!win){
                for (String[] array : game) {
                    System.out.println(Arrays.toString(array));
                }
                System.out.println((turn==0?"player1":"player2") + "'s turn!");
                String notation=sc.nextLine().trim();
                if (isValid(notation, game, turn)){
                    //change board if the move is valid
                    Matcher cm = CASTLE.matcher(notation);
                    if (cm.matches()){
                        boolean queenSide = cm.group(1).equals("O-O-O") || cm.group(1).equals("0-0-0");
                        int row = turn==0 ? 8 : 1;
                        int rookCol = queenSide ? 1 : 8;
                        String king = game[row][5], rook = game[row][rookCol];
                        game[row][5] = " ";
                        game[row][rookCol] = " ";
                        game[row][queenSide ? 3 : 7] = king;
                        game[row][queenSide ? 4 : 6] = rook;
                    } else {
                        int[] s=findStart(notation, game, turn);
                        String sq, promo = null;
                        Matcher pm = PIECE.matcher(notation);
                        if (pm.matches()) {
                            sq = pm.group(5);
                        } else {
                            pm = PAWN.matcher(notation);
                            pm.matches();
                            sq = pm.group(2);
                            promo = pm.group(3);
                        }
                        int tr = rowOf(sq.charAt(1)), tc = colOf(sq.charAt(0));
                        if (typeAt(game, tr, tc) == 'K') win = true; //king captured
                        game[tr][tc] = game[s[0]][s[1]];
                        game[s[0]][s[1]] = " ";
                        if (promo != null) {
                            int i = "QRBN".indexOf(promo);
                            game[tr][tc] = turn==0 ? W + "♕♖♗♘".charAt(i) + R : B + "♛♜♝♞".charAt(i) + R;
                        }
                    }
                    if (!win) turn=1-turn;
                } else{
                    System.out.println("Invalid notation");
                }
            }
            System.out.println((turn==0?"player1":"player2")+" won the game!");
            //temporary break for no spammy spammy
            break;
        }
    }
    //generous code donated by claude checking if a notation is valid
    public static boolean isValid(String notation, String[][] game, int turn) {
        if (notation == null || game == null) return false;
        notation = notation.trim();
        char me = (turn == 0) ? 'W' : 'B';

        Matcher m = CASTLE.matcher(notation);
        if (m.matches()) {
            boolean queenSide = m.group(1).equals("O-O-O") || m.group(1).equals("0-0-0");
            return validCastle(game, queenSide, me);
        }
        m = PIECE.matcher(notation);
        if (m.matches()) return validPieceMove(game, m, me);

        m = PAWN.matcher(notation);
        if (m.matches()) return validPawnMove(game, m, me);

        return false;
    }

    private static char pieceChar(String[][] g, int r, int c) {
        String s = g[r][c];
        if (s == null) return 0;
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (BLACK.indexOf(ch) >= 0 || WHITE.indexOf(ch) >= 0) return ch;
        }
        return 0;
    }

    private static char colorAt(String[][] g, int r, int c) {
        char ch = pieceChar(g, r, c);
        if (ch == 0) return ' ';
        return BLACK.indexOf(ch) >= 0 ? 'B' : 'W';
    }

    private static char typeAt(String[][] g, int r, int c) {
        switch (pieceChar(g, r, c)) {
            case '♜': case '♖': return 'R';
            case '♞': case '♘': return 'N';
            case '♝': case '♗': return 'B';
            case '♛': case '♕': return 'Q';
            case '♚': case '♔': return 'K';
            case '♟': case '♙': return 'P';
            default: return ' ';
        }
    }

    private static boolean empty(String[][] g, int r, int c) { return colorAt(g, r, c) == ' '; }
    private static int colOf(char file) { return file - 'a' + 1; }
    private static int rowOf(char rank) { return 9 - (rank - '0'); }

    private static boolean pathClear(String[][] g, int fr, int fc, int tr, int tc) {
        int dr = Integer.signum(tr - fr), dc = Integer.signum(tc - fc);
        int r = fr + dr, c = fc + dc;
        while (r != tr || c != tc) {
            if (!empty(g, r, c)) return false;
            r += dr; c += dc;
        }
        return true;
    }

    private static boolean canReach(String[][] g, char type, int fr, int fc, int tr, int tc) {
        int dr = Math.abs(tr - fr), dc = Math.abs(tc - fc);
        if (dr == 0 && dc == 0) return false;
        switch (type) {
            case 'N': return dr * dc == 2;
            case 'K': return Math.max(dr, dc) == 1;
            case 'R': return (dr == 0 || dc == 0) && pathClear(g, fr, fc, tr, tc);
            case 'B': return dr == dc && pathClear(g, fr, fc, tr, tc);
            case 'Q': return (dr == 0 || dc == 0 || dr == dc) && pathClear(g, fr, fc, tr, tc);
            default:  return false;
        }
    }

    private static boolean validCastle(String[][] g, boolean queenSide, char me) {
        int row = (me == 'W') ? 8 : 1;
        if (typeAt(g, row, 5) != 'K' || colorAt(g, row, 5) != me) return false;
        int rookCol = queenSide ? 1 : 8;
        if (typeAt(g, row, rookCol) != 'R' || colorAt(g, row, rookCol) != me) return false;
        return pathClear(g, row, 5, row, rookCol);
    }

    private static boolean validPieceMove(String[][] g, Matcher m, char me) {
        char type = m.group(1).charAt(0);
        String fromFile = m.group(2), fromRank = m.group(3);
        boolean capture = m.group(4) != null;
        String sq = m.group(5);
        int tr = rowOf(sq.charAt(1)), tc = colOf(sq.charAt(0));

        if (capture) {
            if (empty(g, tr, tc) || colorAt(g, tr, tc) == me) return false;
        } else if (!empty(g, tr, tc)) {
            return false;
        }

        int candidates = 0;
        for (int r = 1; r <= 8; r++) {
            for (int c = 1; c <= 8; c++) {
                if (colorAt(g, r, c) != me || typeAt(g, r, c) != type) continue;
                if (fromFile != null && colOf(fromFile.charAt(0)) != c) continue;
                if (fromRank != null && rowOf(fromRank.charAt(0)) != r) continue;
                if (canReach(g, type, r, c, tr, tc)) candidates++;
            }
        }
        return candidates == 1;
    }

    private static boolean validPawnMove(String[][] g, Matcher m, char me) {
        String fromFile = m.group(1);
        String sq = m.group(2);
        String promo = m.group(3);
        int tr = rowOf(sq.charAt(1)), tc = colOf(sq.charAt(0));
        int dir = (me == 'W') ? -1 : 1;
        int startRow = (me == 'W') ? 7 : 2;
        int lastRow = (me == 'W') ? 1 : 8;

        if ((tr == lastRow) != (promo != null)) return false;

        if (fromFile != null) {
            int fc = colOf(fromFile.charAt(0));
            if (Math.abs(fc - tc) != 1) return false;
            int fr = tr - dir;
            if (fr < 1 || fr > 8) return false;
            return typeAt(g, fr, fc) == 'P' && colorAt(g, fr, fc) == me
                    && !empty(g, tr, tc) && colorAt(g, tr, tc) != me;
        }

        if (!empty(g, tr, tc)) return false;
        int one = tr - dir;
        if (one >= 1 && one <= 8 && typeAt(g, one, tc) == 'P' && colorAt(g, one, tc) == me) return true;
        int two = tr - 2 * dir;
        return two == startRow && typeAt(g, two, tc) == 'P' && colorAt(g, two, tc) == me
                && empty(g, one, tc);
    }
    public static int[] findStart(String notation, String[][] g, int turn) {
        char me = turn == 0 ? 'W' : 'B';
        Matcher m = PIECE.matcher(notation.trim());
        if (m.matches()) {
            char type = m.group(1).charAt(0);
            int tr = rowOf(m.group(5).charAt(1)), tc = colOf(m.group(5).charAt(0));
            int[] found = null;
            for (int r = 1; r <= 8; r++)
                for (int c = 1; c <= 8; c++) {
                    if (colorAt(g, r, c) != me || typeAt(g, r, c) != type) continue;
                    if (m.group(2) != null && colOf(m.group(2).charAt(0)) != c) continue;
                    if (m.group(3) != null && rowOf(m.group(3).charAt(0)) != r) continue;
                    if (!canReach(g, type, r, c, tr, tc)) continue;
                    if (found != null) return null; // ambiguous
                    found = new int[]{r, c};
                }
            return found;
        }
        m = PAWN.matcher(notation.trim());
        if (m.matches()) {
            int tr = rowOf(m.group(2).charAt(1)), tc = colOf(m.group(2).charAt(0));
            int dir = me == 'W' ? -1 : 1;
            int fc = m.group(1) != null ? colOf(m.group(1).charAt(0)) : tc;
            for (int k = 1; k <= 2; k++) {
                int fr = tr - dir * k;
                if (fr < 1 || fr > 8) break;
                if (typeAt(g, fr, fc) == 'P' && colorAt(g, fr, fc) == me) return new int[]{fr, fc};
                if (m.group(1) != null || !empty(g, fr, fc)) break; // captures only look 1 back
            }
        }
        return null;
    }
}