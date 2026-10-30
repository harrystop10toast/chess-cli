package org.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Main {
    private static final Pattern CASTLE = Pattern.compile("^(O-O-O|O-O|0-0-0|0-0)[+#]?$");
    private static final Pattern PIECE  = Pattern.compile("^([KQRBN])([a-h])?([1-8])?(x)?([a-h][1-8])[+#]?$");
    private static final Pattern PAWN   = Pattern.compile("^(?:([a-h])x)?([a-h][1-8])(?:=([QRBN]))?[+#]?$");

    private static final String C_BLACK = "\u001B[90m";
    private static final String C_WHITE = "\u001B[97m";
    private static final String C_RESET = "\u001B[0m";

    private static final String BLACK = "♜♞♝♛♚♟";
    private static final String WHITE = "♖♘♗♕♔♙";
    private static final List<String> moveHistory = new ArrayList<>();

    public static void main(String[] args){
        while (true){
            moveHistory.clear();
            //a loop is a game
            //create init board
            String[][] game = new String[9][9];
            game[0][0]="/";game[1][0]="8";game[2][0]="7";game[3][0]="6";game[4][0]="5";game[5][0]="4";game[6][0]="3";game[7][0]="2";game[8][0]="1";
            game[0][1]="a";game[0][2]="b";game[0][3]="c";game[0][4]="d";game[0][5]="e";game[0][6]="f";game[0][7]="g";game[0][8]="h";
            String B = C_BLACK;
            String W = C_WHITE;
            String R = C_RESET;
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
            int mode = chooseMode(sc); //1 = bot vs player, 2 = player vs player
            int botTurn = mode == 1 ? new Random().nextInt(2) : -1; //random side for the bot, -1 = no bot
            if (mode == 1) System.out.println("You are " + (botTurn == 0 ? "black" : "white") + ", the bot is " + (botTurn == 0 ? "white" : "black"));
            boolean win=false;
            int turn=0; //turn=0 is white turn=1 is black
            while (!win){
                for (String[] array : game) {
                    System.out.println(Arrays.toString(array));
                }
                System.out.println(nameOf(turn, botTurn) + "'s turn!");
                String notation = turn == botTurn ? getBotMove(game, turn) : sc.nextLine().trim();
                if (isValid(notation, game, turn)){
                    moveHistory.add(toUciMove(notation, game, turn));
                    //change board if the move is valid
                    win = applyMove(notation, game, turn);
                    if (!win) turn=1-turn;
                } else{
                    System.out.println(turn == botTurn ? "Bot gave an invalid notation" : "Invalid notation");
                }
            }
            System.out.println(nameOf(turn, botTurn)+" won the game!");
            //temporary break for no spammy spammy
            break;
        }

    }
    // ---------- game mode / bot ----------

    // asks until the player enters 1 (bot vs player) or 2 (player vs player)
    private static int chooseMode(Scanner sc) {
        while (true) {
            System.out.println("Enter 1 for bot vs player, 2 for player vs player:");
            String in = sc.nextLine().trim();
            if (in.equals("1")) return 1;
            if (in.equals("2")) return 2;
            System.out.println("Please enter 1 or 2");
        }
    }

    // display name of the side whose turn it is
    private static String nameOf(int turn, int botTurn) {
        if (turn == botTurn) return "bot";
        return botTurn == -1 ? (turn == 0 ? "player1" : "player2") : "player";
    }


    private static String getBotMove(String[][] game, int turn) {
        String executable = System.getenv().getOrDefault("STOCKFISH_EXECUTABLE", "stockfish");
        Process process;
        try {
            process = new ProcessBuilder(executable).redirectErrorStream(true).start();
        } catch (IOException e) {
            throw new IllegalStateException("Could not start Stockfish. Set STOCKFISH_EXECUTABLE to its executable path.", e);
        }

        try (BufferedWriter input = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
             BufferedReader output = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            sendUciCommand(input, "uci");
            readUntil(output, "uciok");
            sendUciCommand(input, "setoption name Skill Level value 20");
            sendUciCommand(input, "setoption name UCI_LimitStrength value false");
            sendUciCommand(input, "setoption name Threads value " + Runtime.getRuntime().availableProcessors());
            sendUciCommand(input, "setoption name Hash value 128");
            sendUciCommand(input, "isready");
            readUntil(output, "readyok");
            sendUciCommand(input, "position startpos moves " + String.join(" ", moveHistory));
            sendUciCommand(input, "go movetime 2000");

            String bestMove = null;
            String line;
            while ((line = output.readLine()) != null) {
                if (line.startsWith("bestmove ")) {
                    bestMove = line.split("\\s+")[1];
                    break;
                }
            }
            if (bestMove == null || bestMove.equals("(none)") || bestMove.equals("0000")) {
                throw new IllegalStateException("Stockfish did not return a move; the position may be over.");
            }
            return toNotation(bestMove, game, turn);
        } catch (IOException e) {
            throw new IllegalStateException("Could not communicate with Stockfish.", e);
        } finally {
            process.destroy();
        }
    }

    private static void sendUciCommand(BufferedWriter input, String command) throws IOException {
        input.write(command);
        input.newLine();
        input.flush();
    }

    private static void readUntil(BufferedReader output, String expected) throws IOException {
        String line;
        while ((line = output.readLine()) != null) {
            if (line.equals(expected)) return;
        }
        throw new IOException("Stockfish exited before returning " + expected + ".");
    }

    private static String toUciMove(String notation, String[][] game, int turn) {
        Matcher castle = CASTLE.matcher(notation);
        if (castle.matches()) {
            boolean queenSide = castle.group(1).equals("O-O-O") || castle.group(1).equals("0-0-0");
            return turn == 0 ? (queenSide ? "e1c1" : "e1g1") : (queenSide ? "e8c8" : "e8g8");
        }

        int[] start = findStart(notation, game, turn);
        if (start == null) throw new IllegalArgumentException("Could not determine the starting square for " + notation);
        Matcher piece = PIECE.matcher(notation);
        Matcher pawn = PAWN.matcher(notation);
        String destination;
        String promotion = "";
        if (piece.matches()) {
            destination = piece.group(5);
        } else if (pawn.matches()) {
            destination = pawn.group(2);
            if (pawn.group(3) != null) promotion = pawn.group(3).toLowerCase(Locale.ROOT);
        } else {
            throw new IllegalArgumentException("Unsupported move notation: " + notation);
        }
        return squareName(start[0], start[1]) + destination + promotion;
    }

    private static String toNotation(String uciMove, String[][] game, int turn) {
        int fromCol = colOf(uciMove.charAt(0)), fromRow = rowOf(uciMove.charAt(1));
        int toCol = colOf(uciMove.charAt(2)), toRow = rowOf(uciMove.charAt(3));
        char type = typeAt(game, fromRow, fromCol);
        if (type == 'K' && Math.abs(fromCol - toCol) == 2) {
            return toCol < fromCol ? "O-O-O" : "O-O";
        }

        String target = squareName(toRow, toCol);
        boolean capture = !empty(game, toRow, toCol) || (type == 'P' && fromCol != toCol);
        if (type == 'P') {
            String notation = capture ? uciMove.substring(0, 1) + "x" + target : target;
            return uciMove.length() > 4 ? notation + "=" + Character.toUpperCase(uciMove.charAt(4)) : notation;
        }

        StringBuilder notation = new StringBuilder().append(type);
        boolean sameFileCandidate = false, sameRankCandidate = false, otherCandidate = false;
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                if ((row == fromRow && col == fromCol) || colorAt(game, row, col) != (turn == 0 ? 'W' : 'B')
                        || typeAt(game, row, col) != type || !canReach(game, type, row, col, toRow, toCol)) continue;
                otherCandidate = true;
                if (col == fromCol) sameFileCandidate = true;
                if (row == fromRow) sameRankCandidate = true;
            }
        }
        if (otherCandidate) {
            if (!sameFileCandidate) notation.append((char) ('a' + fromCol - 1));
            else if (!sameRankCandidate) notation.append((char) ('0' + 9 - fromRow));
            else notation.append((char) ('a' + fromCol - 1)).append((char) ('0' + 9 - fromRow));
        }
        if (capture) notation.append('x');
        notation.append(target);
        return notation.toString();
    }

    private static String squareName(int row, int col) {
        return "" + (char) ('a' + col - 1) + (char) ('0' + 9 - row);
    }

    // ---------- applying a validated move to the board ----------

    // applies a move that already passed isValid; returns true if a king was captured
    private static boolean applyMove(String notation, String[][] game, int turn) {
        Matcher cm = CASTLE.matcher(notation);
        if (cm.matches()) {
            boolean queenSide = cm.group(1).equals("O-O-O") || cm.group(1).equals("0-0-0");
            castle(game, turn, queenSide);
            return false;
        }
        return movePiece(notation, game, turn);
    }

    // moves the king and the rook for castling
    private static void castle(String[][] game, int turn, boolean queenSide) {
        int row = turn == 0 ? 8 : 1;
        int rookCol = queenSide ? 1 : 8;
        String king = game[row][5], rook = game[row][rookCol];
        game[row][5] = " ";
        game[row][rookCol] = " ";
        game[row][queenSide ? 3 : 7] = king;
        game[row][queenSide ? 4 : 6] = rook;
    }

    // moves a normal piece or pawn (including promotion); returns true if a king was captured
    private static boolean movePiece(String notation, String[][] game, int turn) {
        int[] s = findStart(notation, game, turn);
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
        boolean kingCaptured = typeAt(game, tr, tc) == 'K';
        boolean enPassant = typeAt(game, s[0], s[1]) == 'P' && tc != s[1] && empty(game, tr, tc);
        game[tr][tc] = game[s[0]][s[1]];
        game[s[0]][s[1]] = " ";
        if (enPassant) game[s[0]][tc] = " ";
        if (promo != null) promote(game, turn, tr, tc, promo);
        return kingCaptured;
    }

    // replaces the pawn on (tr, tc) with the chosen promotion piece
    private static void promote(String[][] game, int turn, int tr, int tc, String promo) {
        int i = "QRBN".indexOf(promo);
        game[tr][tc] = turn == 0 ? C_WHITE + "♕♖♗♘".charAt(i) + C_RESET
                : C_BLACK + "♛♜♝♞".charAt(i) + C_RESET;
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
            if (typeAt(g, fr, fc) != 'P' || colorAt(g, fr, fc) != me) return false;
            return (!empty(g, tr, tc) && colorAt(g, tr, tc) != me)
                    || isEnPassantCapture(g, fr, fc, tr, tc, me);
        }

        if (!empty(g, tr, tc)) return false;
        int one = tr - dir;
        if (one >= 1 && one <= 8 && typeAt(g, one, tc) == 'P' && colorAt(g, one, tc) == me) return true;
        int two = tr - 2 * dir;
        return two == startRow && typeAt(g, two, tc) == 'P' && colorAt(g, two, tc) == me
                && empty(g, one, tc);
    }

    private static boolean isEnPassantCapture(String[][] g, int fromRow, int fromCol, int toRow, int toCol, char me) {
        if (!empty(g, toRow, toCol) || moveHistory.isEmpty()) return false;
        String lastMove = moveHistory.get(moveHistory.size() - 1);
        if (lastMove.length() < 4) return false;
        int lastFromRow = rowOf(lastMove.charAt(1)), lastToRow = rowOf(lastMove.charAt(3));
        int lastToCol = colOf(lastMove.charAt(2));
        int opponentStartRow = me == 'W' ? 2 : 7;
        int opponentEndRow = me == 'W' ? 4 : 5;
        int dir = me == 'W' ? -1 : 1;
        return lastFromRow == opponentStartRow && lastToRow == opponentEndRow
                && lastToRow == fromRow && lastToCol == toCol && toRow == fromRow + dir
                && typeAt(g, lastToRow, lastToCol) == 'P' && colorAt(g, lastToRow, lastToCol) != me;
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