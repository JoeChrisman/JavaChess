import java.io.Serializable;
import java.util.ArrayList;

public class Pieces implements Serializable {

    private ArrayList<Piece> blackPieces = new ArrayList<>();
    private ArrayList<Piece> whitePieces = new ArrayList<>();
    private Piece whiteKing;
    private Piece blackKing;

    // still unused...
    public ArrayList<Piece> getPieces()
    {
        ArrayList<Piece> allPieces = new ArrayList<>();
        allPieces.addAll(whitePieces);
        allPieces.addAll(blackPieces);
        return allPieces;
    }

    public ArrayList<Piece> getPieces(boolean getWhitePieces)
    {
        return getWhitePieces ? whitePieces : blackPieces;
    }

    public Piece getKing(boolean isKingWhite)
    {
        return isKingWhite ? whiteKing : blackKing;
    }

    public void removePiece(Piece piece)
    {
        getPieces(piece.isWhite()).remove(piece);
    }

    public void addPiece(Piece piece)
    {
        if (piece instanceof King)
        {
            if (piece.isWhite())
            {
                whiteKing = piece;
            }
            else
            {
                blackKing = piece;
            }
        }
        getPieces(piece.isWhite()).add(piece);
    }

}
