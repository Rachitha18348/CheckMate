import java.util.Arrays;

public class GameCore {

    private char[][] grid;
    private boolean whiteToMove;
    private boolean[] castleFlags;
    private int[] epTarget;

    public GameCore() {
        setupBoard();
        whiteToMove = true;
        castleFlags = new boolean[]{true, true, true, true};
        epTarget = null;
    }

    private void setupBoard() {
        grid = new char[8][8];
        grid[0] = new char[]{'r','n','b','q','k','b','n','r'};
        Arrays.fill(grid[1], 'p');
        for (int r = 2; r <= 5; r++)
            Arrays.fill(grid[r], ' ');
        Arrays.fill(grid[6], 'P');
        grid[7] = new char[]{'R','N','B','Q','K','B','N','R'};
    }

    public boolean move(int sr, int sc, int dr, int dc) {
        if (!canMove(sr, sc, dr, dc)) return false;

        char piece = grid[sr][sc];
        char backup = grid[dr][dc];

        grid[dr][dc] = piece;
        grid[sr][sc] = ' ';

        handleSpecial(piece, sr, sc, dr, dc);

        if (kingInCheck(whiteToMove)) {
            grid[sr][sc] = piece;
            grid[dr][dc] = backup;
            return false;
        }

        whiteToMove = !whiteToMove;
        return true;
    }

    private boolean canMove(int sr, int sc, int dr, int dc) {
        if (!inside(sr, sc) || !inside(dr, dc)) return false;

        char piece = grid[sr][sc];
        if (piece == ' ') return false;
        if (whiteToMove != Character.isUpperCase(piece)) return false;

        return evaluateMove(piece, sr, sc, dr, dc);
    }

    private boolean evaluateMove(char p, int sr, int sc, int dr, int dc) {
        switch (Character.toLowerCase(p)) {
            case 'p': return PieceValidation.validatePawnMove(grid, p, sr, sc, dr, dc);
            case 'r': return PieceValidation.validateRookMove(grid, sr, sc, dr, dc);
            case 'n': return PieceValidation.validateKnightMove(grid, sr, sc, dr, dc);
            case 'b': return PieceValidation.validateBishopMove(grid, sr, sc, dr, dc);
            case 'q': return PieceValidation.validateQueenMove(grid, sr, sc, dr, dc);
            case 'k': return PieceValidation.validateKingMove(grid, sr, sc, dr, dc);
        }
        return false;
    }

    private void handleSpecial(char p, int sr, int sc, int dr, int dc) {
        char lower = Character.toLowerCase(p);

        if (lower == 'k' && Math.abs(dc - sc) == 2)
            SpecialMoves.handleCastling(grid, sr, sc, dr, dc);

        if (lower == 'p' && (dr == 0 || dr == 7))
            SpecialMoves.handlePromotion(grid, dr, dc);

        if (lower == 'p' && sc != dc && grid[dr][dc] == ' ')
            SpecialMoves.handleEnPassant(grid, sr, dr, dc);
    }

    private boolean kingInCheck(boolean whiteKing) {
        int[] kingPos = locateKing(whiteKing);
        if (kingPos == null) return false;

        int kr = kingPos[0], kc = kingPos[1];

        for (int r = 0; r < 8; r++)
            for (int c = 0; c < 8; c++)
                if (grid[r][c] != ' ' && (Character.isUpperCase(grid[r][c]) != whiteKing))
                    if (evaluateMove(grid[r][c], r, c, kr, kc))
                        return true;

        return false;
    }

    private int[] locateKing(boolean whiteKing) {
        char target = whiteKing ? 'K' : 'k';
        for (int r = 0; r < 8; r++)
            for (int c = 0; c < 8; c++)
                if (grid[r][c] == target)
                    return new int[]{r, c};
        return null;
    }

    private boolean inside(int r, int c) {
        return r >= 0 && r < 8 && c >= 0 && c < 8;
    }

    public char[][] getGrid() {
        return grid;
    }

    public boolean whiteTurn() {
        return whiteToMove;
    }
}
