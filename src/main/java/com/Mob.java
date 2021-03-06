package com;

import javafx.scene.canvas.GraphicsContext;

public class Mob {
    enum Way {
        left,
        right,
        top,
        bottom
    }
    private double x;
    private double y;
    private boolean survival;

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public boolean isSurvival() {
        return survival;
    }

    public void setSurvival(boolean survival) {
        this.survival = survival;
    }

    public void draw(GraphicsContext render) {}
}
