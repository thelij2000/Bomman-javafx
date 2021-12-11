package com;

import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

public class Bomberman {
    public static int x;
    public static int y;
    public static int speed;
    private Image player;
    private HashMap<KeyCode, Integer> key;
    private static int status = 3;
    public static int bombCount;
    public static int bombPower;
    public static ArrayList<Bomb> bomb;
    private int timeAnimation;
    private int frame;
    public static boolean ismove;
    public static boolean ishasbeenslain;
    public static int diestatus;
    public static int life = 1;

    public Bomberman() {
        diestatus = 0;
        ishasbeenslain = false;
        this.x = 32;
        this.y = 16;
        bomb = new ArrayList<>();
        bombPower = 2;
        bombCount = 5;
        speed = 2;
        try {
            player = new Image(getClass().getResource("/Picture/bomber1.png").toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        key = new HashMap<>();
        key.put(KeyCode.RIGHT, 0);
        key.put(KeyCode.LEFT, 0);
        key.put(KeyCode.UP, 0);
        key.put(KeyCode.DOWN, 0);
        timeAnimation = 0;
        frame = 0;
    }

    public void addEvent(Scene scene) {
        scene.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            ismove = true;
            key.put(e.getCode(), speed);
            if (e.getCode() == KeyCode.UP) {
                status = 0;
            }
            if (e.getCode() == KeyCode.DOWN) {
                status = 1;
            }
            if (e.getCode() == KeyCode.LEFT) {
                status = 2;
            }
            if (e.getCode() == KeyCode.RIGHT) {
                status = 3;
            }
            if (e.getCode() == KeyCode.SPACE && Bomb.listBomb.size() < bombCount) {
                if (!checkhavebomb()) {
                    Bomb temp = new Bomb(this.x, this.y);
                }
            }
        });
        scene.addEventHandler(KeyEvent.KEY_RELEASED, e -> {
            key.put(e.getCode(), 0);
            ismove = false;
            timeAnimation = 0;
            frame = 0;
        });
    }

    public void draw(GraphicsContext render) {
        checkiskicked();
        for (int i = 0; i < Bomb.listBomb.size(); i++) {
            Bomb.listBomb.get(i).draw(render);
        }
        if(!ishasbeenslain) {
            if (ismove) {
                timeAnimation++;
                if (timeAnimation % 5 == 0) {
                    if (frame < 3) {
                        frame++;
                    } else {
                        frame = 0;
                    }
                }
            }
            render.drawImage(player, 0 + 32 * frame, 0 + 48 * status, 32, 48, x, y, 32, 48);
            if (checkMove(x + speed, y)) {
                x += key.get(KeyCode.RIGHT);
            }
            if (checkMove(x, y - speed)) {
                y -= key.get(KeyCode.UP);
            }
            if (checkMove(x - speed, y)) {
                x -= key.get(KeyCode.LEFT);
            }
            if (checkMove(x, y + speed)) {
                y += key.get(KeyCode.DOWN);
            }
        } else {
            timeAnimation++;
            if (timeAnimation % 25 == 0) {
                if (diestatus <= 3) {
                    diestatus++;
                } else {
                    life = 0;
                }
            }
            render.drawImage(player, 0 + 32 * diestatus, 48 * 4, 32, 48, x, y, 32, 48);
        }
    }

    public boolean checkMove(int xP, int yP) {
        //top left
        int i1 = (yP + 16 + 6) / 32;
        int j1 = (xP + 0 + 6) / 32;
        //bottom left
        int i2 = (yP + 48 - 6) / 32;
        int j2 = (xP + 0 + 6) / 32;
        //top right
        int i3 = (yP + 16 + 6) / 32;
        int j3 = (xP + 32 - 6) / 32;
        //bottom right
        int i4 = (yP + 48 - 6) / 32;
        int j4 = (xP + 32 - 6) / 32;

        return (checkCanMove(i1, j1)
                && checkCanMove(i2, j2)
                && checkCanMove(i3, j3)
                && checkCanMove(i4, j4));

    }

    private boolean checkCanMove(int i, int j) {
        return Map.maphash[i][j] == '-' || Map.maphash[i][j] == '.'  || Map.maphash[i][j] == 'p';
    }

    public static boolean checkinbomb(Bomb bomb) {
        //top left
        int i1 = (y + 16) / 32;
        int j1 = (x + 0) / 32;
        //bottom left
        int i2 = (y + 48) / 32;
        int j2 = (x + 0) / 32;
        //top right
        int i3 = (y + 16) / 32;
        int j3 = (x + 32) / 32;
        //bottom right
        int i4 = (y + 48) / 32;
        int j4 = (x + 32) / 32;

        int k1 = bomb.getX() / 32;
        int k2 = bomb.getY() / 32;
        return k1 == j1 && k2 == i1
                || k1 == j2 && k2 == i2
                || k1 == j3 && k2 == i3
                || k1 == j4 && k2 == i4 ;
    }

    public static boolean checkhavebomb() {
        for (Bomb b : Bomb.listBomb) {
            if ((x + 16) / 32 == b.getX() / 32 && (y + 32) / 32 == b.getY() / 32) {
                return true;
            }
        }
        return false;
    }

    public static void checkiskicked() {
        if (Map.maphash[(y + 32) / 32][(x + 16) / 32] == '.') {
            ishasbeenslain = true;
        }
    }

    public boolean isInPortal() {
        return Map.maphash[(y + 32) / 32][(x + 16) / 32] == 'p';
    }
}
