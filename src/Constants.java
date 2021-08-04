
public class Constants
{

    public static final int WINDOW_SIZE = 800;
    public static final int SQUARES_WIDE = 8;
    public static final int SQUARE_SIZE = WINDOW_SIZE / SQUARES_WIDE;
    public static final int FRAME_INSET = Main.getFrame().getInsets().top;

    public enum PieceType
    {
        PAWN,
        KING,
        QUEEN,
        ROOK,
        BISHOP,
        KNIGHT
    }

    public enum Direction
    {
        NORTH,
        NORTH_EAST,
        EAST,
        SOUTH_EAST,
        SOUTH,
        SOUTH_WEST,
        WEST,
        NORTH_WEST;

        public static Direction[] getCardinalDirections()
        {
            return getDirectionSubset(true);
        }

        public static Direction[] getOrdinalDirections()
        {
            return getDirectionSubset(false);
        }

        // get an array either cardinal directions or ordinal directions
        private static Direction[] getDirectionSubset(boolean isCardinal)
        {
            Direction[] subset = new Direction[4];
            int subsetIndex = 0;
            Direction[] directions = Direction.values();
            for (int index = isCardinal ? 0 : 1; index < directions.length; index += 2)
            {
                subset[subsetIndex++] = directions[index];
            }
            return subset;
        }
    }
}
