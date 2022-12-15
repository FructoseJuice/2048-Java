public record CoorPair(double xCoor, double yCoor) {

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
            return (int) (Math.pow(xCoor, 2) + xCoor + yCoor);
        } else {
            return (int) (xCoor + Math.pow(yCoor, 2));
        }
    }

}