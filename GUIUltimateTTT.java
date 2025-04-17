import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUIUltimateTTT extends JFrame {
    private JButton[][] buttons = new JButton[9][9];
    private char[][] board = new char[9][9];
    private char[] macroBoard = new char[9];
    private char currentPlayer = 'X';
    private int activeBoard = -1;
    private JLabel statusLabel;

    public GUIUltimateTTT() {
        setTitle("Ultimate Tic Tac Toe");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 650);
        setLayout(new BorderLayout());

        JPanel boardPanel = new JPanel(new GridLayout(3, 3, 5, 5));
        boardPanel.setBackground(Color.BLACK);

        for (int macro = 0; macro < 9; macro++) {
            JPanel miniBoard = new JPanel(new GridLayout(3, 3));
            miniBoard.setBackground(Color.GRAY);
            for (int i = 0; i < 9; i++) {
                int row = (macro / 3) * 3 + (i / 3);
                int col = (macro % 3) * 3 + (i % 3);
                JButton button = new JButton("");
                button.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
                final int r = row, c = col;
                button.addActionListener(e -> handleMove(r, c));
                buttons[row][col] = button;
                miniBoard.add(button);
            }
            boardPanel.add(miniBoard);
        }

        statusLabel = new JLabel("Player X's turn", SwingConstants.CENTER);
        statusLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));

        add(statusLabel, BorderLayout.NORTH);
        add(boardPanel, BorderLayout.CENTER);

        highlightActiveBoard();
        setVisible(true);
    }

    private void handleMove(int row, int col) {
        int macroIndex = (row / 3) * 3 + (col / 3);
        int cellIndex = (row % 3) * 3 + (col % 3);

        if (board[row][col] != '\0' || (activeBoard != -1 && macroIndex != activeBoard) || macroBoard[macroIndex] != '\0') {
            return;
        }

        board[row][col] = currentPlayer;
        buttons[row][col].setText(String.valueOf(currentPlayer));
        buttons[row][col].setEnabled(false);

        if (checkWin(row / 3 * 3, col / 3 * 3)) {
            macroBoard[macroIndex] = currentPlayer;
            highlightMiniBoard(macroIndex, currentPlayer);
            if (checkMacroWin()) {
                JOptionPane.showMessageDialog(this, "Player " + currentPlayer + " wins the game!");
                resetGame();
                return;
            }
        }

        activeBoard = cellIndex;
        if (macroBoard[activeBoard] != '\0') activeBoard = -1;

        currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
        statusLabel.setText("Player " + currentPlayer + "'s turn");
        highlightActiveBoard();
    }

    private boolean checkWin(int baseRow, int baseCol) {
        for (int i = 0; i < 3; i++) {
            if (board[baseRow + i][baseCol] == currentPlayer &&
                board[baseRow + i][baseCol + 1] == currentPlayer &&
                board[baseRow + i][baseCol + 2] == currentPlayer) return true;

            if (board[baseRow][baseCol + i] == currentPlayer &&
                board[baseRow + 1][baseCol + i] == currentPlayer &&
                board[baseRow + 2][baseCol + i] == currentPlayer) return true;
        }

        if (board[baseRow][baseCol] == currentPlayer &&
            board[baseRow + 1][baseCol + 1] == currentPlayer &&
            board[baseRow + 2][baseCol + 2] == currentPlayer) return true;

        if (board[baseRow][baseCol + 2] == currentPlayer &&
            board[baseRow + 1][baseCol + 1] == currentPlayer &&
            board[baseRow + 2][baseCol] == currentPlayer) return true;

        return false;
    }

    private boolean checkMacroWin() {
        for (int i = 0; i < 3; i++) {
            if (macroBoard[i * 3] == currentPlayer && macroBoard[i * 3 + 1] == currentPlayer && macroBoard[i * 3 + 2] == currentPlayer)
                return true;
            if (macroBoard[i] == currentPlayer && macroBoard[i + 3] == currentPlayer && macroBoard[i + 6] == currentPlayer)
                return true;
        }
        if (macroBoard[0] == currentPlayer && macroBoard[4] == currentPlayer && macroBoard[8] == currentPlayer)
            return true;
        if (macroBoard[2] == currentPlayer && macroBoard[4] == currentPlayer && macroBoard[6] == currentPlayer)
            return true;
        return false;
    }

    private void highlightMiniBoard(int macroIndex, char winner) {
        int startRow = (macroIndex / 3) * 3;
        int startCol = (macroIndex % 3) * 3;
        for (int r = startRow; r < startRow + 3; r++) {
            for (int c = startCol; c < startCol + 3; c++) {
                buttons[r][c].setBackground(winner == 'X' ? Color.CYAN : Color.PINK);
                buttons[r][c].setEnabled(false);
            }
        }
    }

    private void highlightActiveBoard() {
        for (int i = 0; i < 9; i++) {
            int startRow = (i / 3) * 3;
            int startCol = (i % 3) * 3;
            Color highlightColor = (activeBoard == -1 || activeBoard == i) ? new Color(200, 255, 200) : Color.WHITE;
            for (int r = startRow; r < startRow + 3; r++) {
                for (int c = startCol; c < startCol + 3; c++) {
                    if (buttons[r][c].getBackground() != Color.CYAN && buttons[r][c].getBackground() != Color.PINK) {
                        buttons[r][c].setBackground(highlightColor);
                    }
                }
            }
        }
    }

    private void resetGame() {
        for (int i = 0; i < 9; i++) {
            macroBoard[i] = '\0';
            for (int j = 0; j < 9; j++) {
                board[i][j] = '\0';
                buttons[i][j].setText("");
                buttons[i][j].setEnabled(true);
                buttons[i][j].setBackground(Color.WHITE);
            }
        }
        currentPlayer = 'X';
        activeBoard = -1;
        statusLabel.setText("Player X's turn");
        highlightActiveBoard();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUIUltimateTTT::new);
    }
}
