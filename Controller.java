package sample;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.Random;

public class Controller {

    private final int BOARD_SIZE = 3;

    private final int AI_DEPTH = 6;

    private boolean playAI = false;

    private ArrayList<Integer> board = new ArrayList<>(BOARD_SIZE*BOARD_SIZE);


    // Buttons
    public Button r1c1 = new Button();
    public Button r1c2 = new Button();
    public Button r1c3 = new Button();

    public Button r2c1 = new Button();
    public Button r2c2 = new Button();
    public Button r2c3 = new Button();

    public Button r3c1 = new Button();
    public Button r3c2 = new Button();
    public Button r3c3 = new Button();

    private Button[][] buttons;

    public void initialize() {
        buttons = new Button[][] {
                {r1c1, r1c2, r1c3},
                {r2c1, r2c2, r2c3},
                {r3c1, r3c2, r3c3}
        };
    }

    // Start/Reset
    public Button resetBtn = new Button();
    public Button startBtn = new Button();
    public Button aiBtn = new Button();

    // Turn Display
    public Label turnDisplay = new Label();

    // Win/Draw Counter
    public Label player1Wins = new Label();
    public Label player2Wins = new Label();
    public Label gameDraws = new Label();

    // track results
    private int p1Wins = 0;
    private int p2Wins = 0;
    private int draws = 0;

    // Start?
    private boolean start = false;

    // Moves left
    private int movesLeft = BOARD_SIZE*BOARD_SIZE-1;

    // track if reset
    private boolean isReset = true;

    // track last move
    private String lastMove = "";

    // Track whose turn it is
    private int playerTurn = 0;

    public void setPlayAI() {
        if (playAI) {
            aiBtn.setText("Play against CPU");
            playAI = false;
        } else {
            aiBtn.setText("Play against human");
            playAI = true;
        }
    }


    public void startGame() {
        // setup board tracker
        for (int i = 0; i < 9; i++) {
            board.add(i, 0);
        }

        // check if the player reset the game
        if (!isReset) {
            turnDisplay.setText("Please reset the game first!");

        } else {
            // Generate random number between 0 and 1 to decide starting player
            Random r = new Random();
            int randStart = r.nextInt(2);

            if (randStart == 1) {
                turnDisplay.setText("Player 1 will start!");
                playerTurn = 1;
                start = true;

            } else {
                turnDisplay.setText("Player 2 will start!");
                playerTurn = -1;

                start = true;

                if (playAI) {
                    aiMakeMove();
                }
            }
        }
    }

    public void resetGame() {
        turnDisplay.setText("The game has been reset!");

        // Reset buttons
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                for (int row = 0; row < BOARD_SIZE; row++) {
                    buttons[col][row].setText("");
                }
            }
            for (int row = 0; row < BOARD_SIZE; row++) {
                for (int col = 0; col < BOARD_SIZE; col++) {
                    buttons[col][row].setText("");
                }
            }
        }

        // reset board tracker
        board.clear();

        movesLeft = BOARD_SIZE*BOARD_SIZE-1;

        isReset = true;
    }

    // check for a win
    public boolean checkWin(ArrayList<Integer> board, int playerTurn) {
        ArrayList<Integer> wayToWin = new ArrayList<>();

        // check rows
        for (int i = 0; i < 9; i+=3) {
            wayToWin.clear();
            for (int j = i; j < i+3; j++) {
                if (board.get(j) == playerTurn) {
                    wayToWin.add(j);
                }
            }
            if (wayToWin.size() == BOARD_SIZE) {
                return true;
            }
        }
        wayToWin.clear();

        // check columns
        for (int i = 0; i < 3; i++) {
            wayToWin.clear();
            for (int j = i; j < i+7; j+=3) {
                if(board.get(j) == playerTurn) {
                    wayToWin.add(j);
                }
            }
            if (wayToWin.size() == BOARD_SIZE) {
                return true;
            }
        }
        wayToWin.clear();

        // check top-left to bot-right diagonal
        for (int i = 0; i < 9; i+=4) {
            if (board.get(i) == playerTurn) {
                wayToWin.add(i);
            }
        }
        if (wayToWin.size() == BOARD_SIZE) {
            // System.out.println("true");
            // System.out.println("Current: " + wayToWin);
            return true;
        }
        wayToWin.clear();

        // check top-right to bot-left diagonal
        for (int i = 2; i < 7; i+=2) {
            if (board.get(i) == playerTurn) {
                wayToWin.add(i);
            }
        }
        if (wayToWin.size() == BOARD_SIZE) {
            // System.out.println("true");
            // System.out.println("Current: " + wayToWin);
            return true;
        }
        return false;
    }

    // process moves
    public void processMove(ActionEvent evt) {
        // System.out.println(board); --> RULED OUT
        Button movePos = ((Button) evt.getSource());
        lastMove = movePos.getId();
        board.set(getFieldNumber(lastMove), playerTurn);

        if (!start) {
            if (!isReset) {
                turnDisplay.setText("Please reset the game first!");
            } else {
                turnDisplay.setText("Please start the game first!");
            }

        }  else if (!(movePos.getText().equals(""))) {
            turnDisplay.setText("Invalid move!");

        } else if (playerTurn == 1) {
            movePos.setText("X");

            if (checkWin(board, playerTurn)) {
                p1Wins += 1;
                player1Wins.setText("Player 1 Wins: " + p1Wins);
                turnDisplay.setText("Player 1 wins!");
                start = false;
                isReset = false;
            } else if (movesLeft == 0) {
                draws += 1;
                turnDisplay.setText("It's a tie!");
                gameDraws.setText("Draws: " + draws);
                start = false;
                isReset = false;
            } else {
                playerTurn = -1;
                turnDisplay.setText("Player 2's turn");
                movesLeft -= 1;

                if (playAI) {
                    aiMakeMove();

                }
            }

        } else {
            movePos.setText("O");

            if (checkWin(board, playerTurn)) {
                // System.out.println(board); --> RULED OUT
                p2Wins += 1;
                player2Wins.setText("Player 2 Wins: " + p2Wins);
                turnDisplay.setText("Player 2 wins!");
                start = false;
                isReset = false;

            } else if (movesLeft == 0) {
                draws += 1;
                turnDisplay.setText("It's a tie!");
                gameDraws.setText("Draws: " + draws);
                start = false;
                isReset = false;

            } else {
                playerTurn = 1;
                turnDisplay.setText("Player 1's turn");

                movesLeft -= 1;
            }
            System.out.println(movesLeft);
        }
    }

    private void aiMakeMove() {
        Button moveToMake = getFieldButton(bestMove(board));
        moveToMake.setText("O");
        board.set(getFieldNumber(moveToMake.getId()), -1);


        if (checkWin(board, -1)) {
            // System.out.println(board); --> RULED OUT
            p2Wins += 1;
            player2Wins.setText("Player 2 Wins: " + p2Wins);
            turnDisplay.setText("Player 2 wins!");
            start = false;
            isReset = false;

        } else if (movesLeft == 0) {
            draws += 1;
            turnDisplay.setText("It's a tie!");
            gameDraws.setText("Draws: " + draws);
            start = false;
            isReset = false;

        } else {
            playerTurn = 1;
            turnDisplay.setText("Player 1's turn");

            movesLeft -= 1;
        }
    }

    private Integer getFieldNumber(String fieldId) {
        return switch (fieldId) {
            case "r1c1" -> 0;
            case "r1c2" -> 1;
            case "r1c3" -> 2;

            case "r2c1" -> 3;
            case "r2c2" -> 4;
            case "r2c3" -> 5;

            case "r3c1" -> 6;
            case "r3c2" -> 7;
            case "r3c3" -> 8;
            default -> 99;
        };

    }

    private Button getFieldButton(int fieldNumber) {
        return switch (fieldNumber) {
            case 0 -> r1c1;
            case 1 -> r1c2;
            case 2 -> r1c3;

            case 3 -> r2c1;
            case 4 -> r2c2;
            case 5 -> r2c3;

            case 6 -> r3c1;
            case 7 -> r3c2;
            case 8 -> r3c3;

            default -> throw new IllegalStateException("Unexpected value: " + fieldNumber);
        };

    }


    // return free spots
    public ArrayList<Integer> getEmptyFields(ArrayList<Integer> board) {
        ArrayList<Integer> emptyFields = new ArrayList<>();

        for (int i = 0; i < board.size(); i++) {
            if (board.get(i) == 0) {
                emptyFields.add(i);
            }
        }
        return emptyFields;
    }

    // evaluate any given board from the perspective of player 2 (cpu)
    public int evaluateBoard(ArrayList<Integer> board, int depth) {
        if (checkWin(board, -1)) {
            // System.out.println(board + "Value is 10");
            return 10 + depth;
        } else if (checkWin(board, 1)) {
            // System.out.println(board + "Value is -10");
            return -10 - depth;
        } else {
            // System.out.println(board + "Value is 0");
            return 0;
        }
    }

    // minimax algorithm
    public int miniMax(ArrayList<Integer> board, int depth, int alpha, int beta, boolean isMax) {
        ArrayList<Integer> newBoard = new ArrayList<>(board);
        int boardValue = evaluateBoard(newBoard, depth);
        ArrayList<Integer> emptyFields = getEmptyFields(newBoard);

        // if a terminal node is reached, return the current board value
        if (Math.abs(boardValue) > 0 || depth == 0 || getEmptyFields(newBoard).size() == 0) {
            // System.out.println("Value: " + boardValue + ", newBoard: " + newBoard);
            return boardValue;

        }

        if (isMax) {
            // maximising cpu
            int highestValue = Integer.MIN_VALUE;

            for (Integer emptyField : emptyFields) {
                newBoard.set(emptyField, -1);
                highestValue = Math.max(highestValue, miniMax(newBoard, depth - 1, alpha, beta, false));
                newBoard.set(emptyField, 0);

                alpha = Math.max(alpha, highestValue);
                if (alpha >= beta) {
                    return highestValue;
                }
            }
            return highestValue;

        } else {
            // minimising player
            int lowestValue = Integer.MAX_VALUE;

            for (Integer emptyField : emptyFields) {
                newBoard.set(emptyField, 1);

                lowestValue = Math.min(lowestValue, miniMax(newBoard, depth - 1, alpha, beta,true));
                newBoard.set(emptyField, 0);

                beta = Math.min(beta, lowestValue);
                if (beta <= alpha) {
                    return lowestValue;
                }
            }
            return lowestValue;
        }
    }

    public int bestMove(ArrayList<Integer> board) {
        int bestMove = -1;
        int bestValue = Integer.MIN_VALUE;

        ArrayList<Integer> newBoard = new ArrayList<>(board);

        for (int i = 0; i < getEmptyFields(board).size(); i++) {
            newBoard.set(getEmptyFields(board).get(i), -1);
            int moveValue = miniMax(newBoard, AI_DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, false);

            newBoard = new ArrayList<>(board);

            if (moveValue > bestValue) {
                bestMove = getEmptyFields(board).get(i);
                bestValue = moveValue;
            }
        }
        // System.out.println("Best Move: " + bestMove + ", Move Value: " + bestValue);
        return bestMove;
    }
}


