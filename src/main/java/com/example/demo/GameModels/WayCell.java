package com.example.demo.GameModels;

import com.example.demo.Enums.Colors;
import com.example.demo.Enums.StatusText;

public class WayCell extends Cell {

    private boolean wall = false;

    public boolean isWall() {
        return !wall;
    }

    public WayCell(int x, int y, Board board, GameModel gameModel) {
        super(x, y, board, gameModel);
        applyStyle(Colors.WHITE);
    }


    private boolean isPossibleWay(){
        return gameModel.getPlayerWhoDoTurn().getPossibleWays().contains(this);
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
        gameModel.checkWin();

        if (isPossibleWay() && board.isChoiced()) {
            board.setChoiced(false);
            gameModel.doMove(x, y);
            return;
        }


        if (!currentNeighbor.getClass().isAssignableFrom(getClass())
                || board.isChoiced()
                || wall
                || ((WayCell) currentNeighbor).wall
        ) {
            gameModel.setStatus(StatusText.WALL_COLLISION, Colors.RED);return;}

        if (gameModel.getPlayerWhoDoTurn().getNumberOfWalls() == 0) {
            gameModel.setStatus(StatusText.DONT_HAVE_WALLS, Colors.RED); return;}

        board.changeMatrix(x, y, (byte)1);
        board.changeMatrix(currentNeighbor.getX(), currentNeighbor.getY(), (byte)1);

        if ( board.getBluePlayer().checkLogic() && board.getGreenPlayer().checkLogic()){
            wall = true;
            ((WayCell) currentNeighbor).wall = true;
            gameModel.getPlayerWhoDoTurn().useWall();

            gameModel.checkPossibleWaysForBoth();
            gameModel.switchTurn();
        }
        else {
            board.changeMatrix(x, y, (byte)0);
            board.changeMatrix(currentNeighbor.getX(), currentNeighbor.getY(), (byte)0);
            gameModel.setStatus(StatusText.NOWAY, Colors.RED);
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
