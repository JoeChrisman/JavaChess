import java.awt.*;
import java.util.ArrayList;

public class ChessGame
{

    private ArrayList<Board> positionHistory = new ArrayList<>();
    private int historyIndex = 0;

    private boolean isWhitesTurn = true;
    private Board board;

    public ChessGame(boolean playerPlaysWhite)
    {
        board = new Board(playerPlaysWhite);
        positionHistory.add(board.deepClone());
    }

    public Board getBoard()
    {
        return board;
    }

    public boolean isWhitesTurn()
    {
        return isWhitesTurn;
    }

    public void movePiece(Square squareMovingFrom, Square squareMovingTo)
    {
        isWhitesTurn = !isWhitesTurn;
        board.movePiece(squareMovingFrom, squareMovingTo);

        // overwrite history if we are back in time and make a new move
        if (historyIndex < positionHistory.size() - 1)
        {
            positionHistory = new ArrayList<>(positionHistory.subList(0, historyIndex));
        }
        positionHistory.add(board.deepClone());
        historyIndex++;
    }

    public void undo()
    {
        if (historyIndex > 0)
        {
            historyIndex--;
        }
        board = positionHistory.get(historyIndex);
    }

    public void redo()
    {
        if (historyIndex < positionHistory.size() - 1)
        {
            historyIndex++;
        }
        board = positionHistory.get(historyIndex);
    }

    public void render(Graphics graphics)
    {
        board.render(graphics);
    }
}
