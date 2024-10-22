package com.example.demo;

public class WayCell extends Cell{

    private boolean wall = false;

    public boolean isWall() {
        return wall;
    }

    public WayCell(int x, int y, Board board) {
        super(x, y, board);
        applyStyle(Colors.WHITE);
    }


    private boolean isPossibleWay(){
        return board.getPlayerWhoDoTurn().getPossibleWays().contains(this);
    }

    @Override
    public void onMouseEntered() {
        if (!board.isChoiced()) {
            checkStyle(true);
            currentNeighbor.checkStyle(true);
        }
        else if(isPossibleWay()) applyStyle(Colors.GRAY);
    }

    @Override
    public void onMouseExited() {
        if (!board.isChoiced()) {
            checkStyle(false);
            currentNeighbor.checkStyle(false);
        }

    }

    @Override
    public void OnMouseClicked() {
        if (isPossibleWay() && board.isChoiced()) {
            board.setChoiced(false);
            board.move(this);
            return;
        }

        if (!currentNeighbor.getClass().isAssignableFrom(getClass())
                || board.isChoiced()
                || wall
                || ((WayCell) currentNeighbor).wall
        ) return;


        board.changeMatrix(x, y, (byte)1);
        board.changeMatrix(currentNeighbor.getX(), currentNeighbor.getY(), (byte)1);

        if ( board.getBluePlayer().checkLogic() && board.getGreenPlayer().checkLogic()){
            wall = true;
            ((WayCell) currentNeighbor).wall = true;

            board.checkPossibleWaysForBoth();
            board.switchTurn();
        }
        else {
            board.changeMatrix(x, y, (byte)0);
            board.changeMatrix(currentNeighbor.getX(), currentNeighbor.getY(), (byte)0);
        }

    }

    @Override
    public void checkStyle(boolean mouseInOrOut){
        if (mouseInOrOut) applyStyle(wall ? Colors.RED : Colors.BLACK);
        else applyStyle(wall ? Colors.BLACK : Colors.WHITE);
    }

    @Override
    public String toString() {
        return "WayCell" +
                "coords=" + getX() +
                " " + getY() +
                "\n";
    }
}
