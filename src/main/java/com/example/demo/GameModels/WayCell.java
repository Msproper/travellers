package com.example.demo.GameModels;

import com.example.demo.Enums.Colors;
import com.example.demo.Enums.StatusText;

public class WayCell extends Cell {

    private boolean wall = false;

    public boolean isWall() {
        return !wall;
    }

    public WayCell(int x, int y, Game game) {
        super(x, y, game);
        applyStyle(Colors.WHITE);
    }


    private boolean isPossibleWay(){
        return game.getPlayerWhoDoTurn().getPossibleWays().contains(this);
    }

    @Override
    public void onMouseEntered() {
        if (!game.isChoiced()) {
            checkStyle(true);
            currentNeighbor.checkStyle(true);
        }
        else if(isPossibleWay()) applyStyle(Colors.GRAY);
    }

    @Override
    public void onMouseExited() {
        if (!game.isChoiced()) {
            checkStyle(false);
            currentNeighbor.checkStyle(false);
        }

    }

    @Override
    public void OnMouseClicked() {
        game.checkWin();

        if (isPossibleWay() && game.isChoiced()) {
            game.setChoiced(false);
            game.move(this);
            return;
        }


        if (!currentNeighbor.getClass().isAssignableFrom(getClass())
                || game.isChoiced()
                || wall
                || ((WayCell) currentNeighbor).wall
        ) {
            game.setStatus(StatusText.WALL_COLLISION, Colors.RED);return;}

        if (game.getPlayerWhoDoTurn().getNumberOfWalls() == 0) {
            game.setStatus(StatusText.DONT_HAVE_WALLS, Colors.RED); return;}

        game.changeMatrix(x, y, (byte)1);
        game.changeMatrix(currentNeighbor.getX(), currentNeighbor.getY(), (byte)1);

        if ( game.getBluePlayer().checkLogic() && game.getGreenPlayer().checkLogic()){
            wall = true;
            ((WayCell) currentNeighbor).wall = true;
            game.getPlayerWhoDoTurn().useWall();

            game.checkPossibleWaysForBoth();
            game.switchTurn();
        }
        else {
            game.changeMatrix(x, y, (byte)0);
            game.changeMatrix(currentNeighbor.getX(), currentNeighbor.getY(), (byte)0);
            game.setStatus(StatusText.NOWAY, Colors.RED);
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
