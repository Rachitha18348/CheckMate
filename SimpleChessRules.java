public class SimpleChessRules {

    public static boolean clear(char[][] b, int sr, int sc, int dr, int dc) {
        int rs = Integer.compare(dr, sr), cs = Integer.compare(dc, sc);
        int r = sr + rs, c = sc + cs;
        while (r != dr || c != dc) {
            if (b[r][c] != ' ') return false;
            r += rs; c += cs;
        }
        return true;
    }

    public static boolean pawn(char[][] b, char p, int sr, int sc, int dr, int dc) {
        boolean w = Character.isUpperCase(p);
        int d = w ? -1 : 1;
        int start = w ? 6 : 1;

        if (sc == dc && dr == sr + d && b[dr][dc] == ' ') return true;
        if (sr == start && sc == dc && dr == sr + 2*d &&
            b[sr+d][sc]==' ' && b[dr][dc]==' ') return true;

        if (Math.abs(dc - sc) == 1 && dr == sr + d) {
            char t = b[dr][dc];
            if (t!=' ' && Character.isUpperCase(t)!=w) return true;
        }
        return false;
    }

    public static boolean rook(char[][] b, int sr, int sc, int dr, int dc) {
        if (sr!=dr && sc!=dc) return false;
        return clear(b, sr, sc, dr, dc);
    }

    public static boolean knight(int sr, int sc, int dr, int dc) {
        int r=Math.abs(dr-sr), c=Math.abs(dc-sc);
        return (r==2&&c==1)||(r==1&&c==2);
    }

    public static boolean bishop(char[][] b, int sr, int sc, int dr, int dc) {
        if (Math.abs(dr-sr)!=Math.abs(dc-sc)) return false;
        return clear(b, sr, sc, dr, dc);
    }

    public static boolean queen(char[][] b, int sr, int sc, int dr, int dc) {
        if (sr==dr||sc==dc) return rook(b,sr,sc,dr,dc);
        if (Math.abs(dr-sr)==Math.abs(dc-sc)) return bishop(b,sr,sc,dr,dc);
        return false;
    }

    public static boolean king(int sr, int sc, int dr, int dc) {
        int r=Math.abs(dr-sr), c=Math.abs(dc-sc);
        return r<=1 && c<=1;
    }

    public static void castle(char[][] b, int sr, int sc, int dr, int dc) {
        boolean ks = dc > sc;
        int rs = ks ? 7 : 0;
        int re = ks ? dc-1 : dc+1;
        char rook = b[sr][rs];
        b[sr][rs] = ' ';
        b[sr][re] = rook;
    }

    public static void promote(char[][] b, int dr, int dc) {
        boolean w = b[dr][dc] == 'P';
        b[dr][dc] = w ? 'Q' : 'q';
    }

    public static void enPassant(char[][] b, int sr, int dr, int dc) {
        b[sr][dc] = ' ';
    }

    public static boolean inCheck(char[][] b, boolean white) {
        int[] k = findKing(b, white);
        if (k == null) return false;
        int kr = k[0], kc = k[1];

        for (int r=0;r<8;r++)
            for (int c=0;c<8;c++) {
                char p=b[r][c];
                if (p!=' ' && Character.isUpperCase(p)!=white)
                    if (valid(b,p,r,c,kr,kc)) return true;
            }
        return false;
    }

    public static boolean valid(char[][] b, char p, int sr, int sc, int dr, int dc) {
        switch (Character.toLowerCase(p)) {
            case 'p': return pawn(b,p,sr,sc,dr,dc);
            case 'r': return rook(b,sr,sc,dr,dc);
            case 'n': return knight(sr,sc,dr,dc);
            case 'b': return bishop(b,sr,sc,dr,dc);
            case 'q': return queen(b,sr,sc,dr,dc);
            case 'k': return king(sr,sc,dr,dc);
        }
        return false;
    }

    public static int[] findKing(char[][] b, boolean white) {
        char k = white ? 'K' : 'k';
        for (int r=0;r<8;r++)
            for (int c=0;c<8;c++)
                if (b[r][c]==k) return new int[]{r,c};
        return null;
    }
}
