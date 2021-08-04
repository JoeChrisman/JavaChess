import java.awt.*;
import java.util.ArrayList;

public class Board {

    private ArrayList<Piece> pieces;
    private Square[][] squares;
    private Piece mostRecentPieceMoved;

    private boolean isFromWhitesPerspective;

    public Board(boolean isFromWhitesPerspective) {
        squares = new Square[Constants.SQUARES_WIDE][Constants.SQUARES_WIDE];
        pieces = new ArrayList<>();

        for (int row = 0; row < squares.length; row++) {
            for (int col = 0; col < squares.length; col++) {
                squares[row][col] = new Square(row, col);
                if (row == 1) {
                    addPiece(Constants.PieceType.PAWN, !isFromWhitesPerspective, row, col);
                }
                if (row == 6) {
                    addPiece(Constants.PieceType.PAWN, isFromWhitesPerspective, row, col);
                }
            }
        }

        for (int row = 0; row < squares.length; row += 7)
        {
            boolean pieceIsWhite = isFromWhitesPerspective == (row == 7);
            addPiece(Constants.PieceType.ROOK, pieceIsWhite, row, 0);
            addPiece(Constants.PieceType.ROOK, pieceIsWhite, row, 7);
            addPiece(Constants.PieceType.BISHOP, pieceIsWhite, row, 2);
            addPiece(Constants.PieceType.BISHOP, pieceIsWhite, row, 5);
            addPiece(Constants.PieceType.KNIGHT, pieceIsWhite, row, 1);
            addPiece(Constants.PieceType.KNIGHT, pieceIsWhite, row, 6);

            addPiece(Constants.PieceType.KING, pieceIsWhite, row, isFromWhitesPerspective ? 4 : 3);
            addPiece(Constants.PieceType.QUEEN, pieceIsWhite, row, isFromWhitesPerspective ? 3 : 4);
        }

        this.isFromWhitesPerspective = isFromWhitesPerspective;

        // set up the board for pawn battle
        // only kings and pawns
    }

    public void movePiece(Square squareMovingFrom, Square squareMovingTo)
    {
        Piece pieceMoving = squareMovingFrom.getPiece();
        Piece pieceCaptured = squareMovingTo.getPiece();
        assert pieceMoving != null;

        pieceMoving.moveTo(squareMovingTo);
        setMostRecentPieceMoved(pieceMoving);

        // if a pawn was moved using en passant
        if (pieceMoving instanceof Pawn && pieceCaptured == null && squareMovingFrom.getCol() != squareMovingTo.getCol())
        {
            // find the pawn captured by en passant and remove it
            int oppositePawnDirection = pieceMoving.isWhite() == isFromWhitesPerspective ? 1 : -1;
            Square enPassantCaptureSquare = squares[squareMovingTo.getRow() + oppositePawnDirection][squareMovingTo.getCol()];
            pieceCaptured = enPassantCaptureSquare.getPiece();
            enPassantCaptureSquare.setPiece(null);
        }

        squareMovingFrom.setPiece(null);
        if (pieceCaptured != null)
        {
            pieces.remove(pieceCaptured);
        }
    }

    public void addPiece(Constants.PieceType pieceType, boolean isWhite, int row, int col) {
        Square square = squares[row][col];
        Piece pieceToAdd = null;
        switch(pieceType) {
            case KING: pieceToAdd = new King(square, isWhite); break;
            case PAWN: pieceToAdd = new Pawn(square, isWhite); break;
            case ROOK: pieceToAdd = new Rook(square, isWhite); break;
            case BISHOP: pieceToAdd = new Bishop(square, isWhite); break;
            case KNIGHT: pieceToAdd = new Knight(square, isWhite); break;
            case QUEEN: pieceToAdd = new Queen(square, isWhite); break;
        }
        assert pieceToAdd != null;
        pieces.add(pieceToAdd);
    }

    public Square[][] getSquares() {
        return squares;
    }

    public boolean isFromWhitesPerspective() {
        return isFromWhitesPerspective;
    }

    public void render(Graphics graphics) {
        for (Square[] row : squares) {
            for (Square square : row) {
                square.render(graphics);
            }
        }
    }

    public Square getSquareClicked(Point mouseLocation) {
        for (Square[] row : squares) {
            for (Square square : row) {
                if (square.contains(mouseLocation)) {
                    return square;
                }
            }
        }
        return null;
    }

    public void setMostRecentPieceMoved(Piece pieceMoved)
    {
        mostRecentPieceMoved = pieceMoved;
    }

    public Piece getMostRecentPieceMoved()
    {
        return mostRecentPieceMoved;
    }
}
