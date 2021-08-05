import java.awt.*;
import java.util.ArrayList;

public class ChessGame
{

    private boolean isWhitesTurn = true;
    private Board board;

    public ChessGame(boolean playerPlaysWhite)
    {
        board = new Board(playerPlaysWhite);
    }

    public Board getBoard()
    {
        return board;
    }

    public void setBoard(Board board)
    {
        this.board = board;
    }

    public boolean isWhitesTurn()
    {
        return isWhitesTurn;
    }

    public void movePiece(Square squareMovingFrom, Square squareMovingTo)
    {
        isWhitesTurn = !isWhitesTurn;
        board.movePiece(squareMovingFrom, squareMovingTo, false);
    }

    public void render(Graphics graphics)
    {
        board.render(graphics);
    }
}
