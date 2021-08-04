import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Main extends JPanel
{

    public static void main(String[] args)
    {
        new Main();
    }

    private static Board board = new Board(true);
    private static JFrame frame = new JFrame("Chess");
    private MouseInputHandler mouseHandler = new MouseInputHandler();

    public Main()
    {
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(Constants.WINDOW_SIZE, Constants.WINDOW_SIZE + Constants.FRAME_INSET);
        frame.addMouseListener(mouseHandler);
        frame.addMouseMotionListener(mouseHandler);
        frame.add(this);

    }

    public static Frame getFrame()
    {
        return frame;
    }

    @Override
    public void paintComponent(Graphics graphics)
    {
        board.render(graphics);

        Piece pieceDragging = mouseHandler.getPieceDragging();
        if (pieceDragging != null)
        {
            pieceDragging.render(graphics);
        }

    }

    private class MouseInputHandler implements MouseListener, MouseMotionListener
    {

        private Piece pieceDragging;
        private Square squareMovedFrom;

        public Piece getPieceDragging()
        {
            return pieceDragging;
        }

        public Point getCoordinateAdjustedForFrameInsets(Point coordinate)
        {
            return new Point(coordinate.x, coordinate.y - Constants.FRAME_INSET);
        }

        public Point getPieceLocationOffset(Point pieceLocation)
        {
            // offset dragged piece to render from center instead of corner
            return getCoordinateAdjustedForFrameInsets(new Point(
                    pieceLocation.x - Constants.SQUARE_SIZE / 2,
                    pieceLocation.y - Constants.SQUARE_SIZE / 2
            ));
        }

        @Override
        public void mousePressed(MouseEvent mouseEvent)
        {
            Point mouseLocation = mouseEvent.getPoint();
            Square squareClicked = getSquareClicked(mouseLocation);
            if (squareClicked != null)
            {
                Piece pieceOnSquare = squareClicked.getPiece();
                if (pieceOnSquare != null)
                {
                    pieceDragging = pieceOnSquare;
                    pieceDragging.dragTo(getPieceLocationOffset(mouseLocation));
                    squareClicked.setPiece(null);
                    squareMovedFrom = squareClicked;
                }
                repaint();
            }
        }

        @Override
        public void mouseReleased(MouseEvent mouseEvent)
        {
            if (pieceDragging != null)
            {
                Square targetSquare = getSquareClicked(mouseEvent.getPoint());
                if (pieceDragging.getLegalMoves(board).contains(targetSquare))
                {
                    pieceDragging.moveTo(targetSquare);
                    board.setMostRecentPieceMoved(pieceDragging);
                } else
                {
                    squareMovedFrom.setPiece(pieceDragging);
                }
                pieceDragging.resetDrag();
                pieceDragging = null;
                repaint();
            }
        }

        @Override
        public void mouseDragged(MouseEvent mouseEvent)
        {
            if (pieceDragging != null)
            {
                pieceDragging.dragTo(getPieceLocationOffset(mouseEvent.getPoint()));
                repaint();
            }
        }

        public Square getSquareClicked(Point mouseLocation)
        {
            return board.getSquareClicked(getCoordinateAdjustedForFrameInsets(mouseLocation));
        }

        @Override
        public void mouseMoved(MouseEvent e) {}
        @Override
        public void mouseEntered(MouseEvent e) {}
        @Override
        public void mouseExited(MouseEvent e) {}
        @Override
        public void mouseClicked(MouseEvent e) {}
    }
}
