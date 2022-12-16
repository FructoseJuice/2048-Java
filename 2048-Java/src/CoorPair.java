public record CoorPair(int xCoor, int yCoor) {

    public boolean coorEquals(CoorPair pairToCheck) {
        return (xCoor == pairToCheck.xCoor() &
                yCoor == pairToCheck.yCoor());
    }

    @Override
    public String toString() {
        return "(" + xCoor + ", " + yCoor + ")";
    }

    @Override
    public int hashCode() {
        if (xCoor >= yCoor) {
            return ((xCoor << 1) + xCoor + yCoor);
        } else {
            return (xCoor + (yCoor << 1));
        }
    }
}