package com;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.net.URISyntaxException;
import java.util.ArrayList;

public class Upgrade {
    private Point point;
    private ArrayList<Image> images;
    private int type;

    public Upgrade(Point point, int type) {
        try {
            this.type = type;
            this.point = point;
            images = new ArrayList<>();
            images.add(new Image(getClass().getResource("/Picture/power_bomb.png").toURI().toString()));
            images.add(new Image(getClass().getResource("/Picture/power_fireup.png").toURI().toString()));
            images.add(new Image(getClass().getResource("/Picture/power_speed.png").toURI().toString()));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void draw(GraphicsContext render) {
        render.drawImage(images.get(type), point.getX() * 32, point.getY() * 32);
    }

    public void effect() {
        if (type == 0) {
            Bomberman.bombCount ++;
        }
        if (type == 1) {
            Bomberman.bombPower ++;
        }
        if (type == 2) {
            Bomberman.speed += 1;
        }
    }

    public boolean checkPlayer() {
        int j = point.getX();
        int i = point.getY();
        int jp = (Bomberman.x + 16) / 32;
        int ip = (Bomberman.y + 32) / 32;
        return i == ip && j == jp;
    }

    public int getType() {
        return type;
    }

    public Point getPoint() {
        return point;
    }
}
