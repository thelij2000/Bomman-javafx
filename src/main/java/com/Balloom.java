package com;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Balloom extends Mob {
    private static List<Image> balloomImg = null;
    private boolean canMoveL;
    private boolean canMoveR;
    private boolean canMoveT;
    private boolean canMoveB;
    private double speed = 1;
    private double moveH;
    private double moveV;
    private int type;


    public static void init() {
        if (balloomImg == null) {
            balloomImg = new LinkedList<>();
            try {
                balloomImg.add(new Image((Balloom.class.getResource("/Picture/Ballom/mob1.png").toURI().toString())));
                balloomImg.add(new Image((Balloom.class.getResource("/Picture/Ballom/mob2.png").toURI().toString())));
                balloomImg.add(new Image((Balloom.class.getResource("/Picture/Ballom/mob3.png").toURI().toString())));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    public Balloom(int x, int y) {
        init();
        setX(x);
        setY(y);
        setSurvival(true);
        moveH = 0;
        moveV = 0;
        Random r = new Random();
        type = r.nextInt(3);
    }

    @Override
    public void draw(GraphicsContext render) {
        move();
        render.drawImage(balloomImg.get(type), getX(), getY());
        if (isPlayer(Bomberman.x, Bomberman.y)) {
            Bomberman.ishasbeenslain = true;
        }
        checkiskicked();
    }

    private void move() {
        int i = (int) getY() / 32;
        int j = (int) getX() / 32;
        if (i * 32 == getY() && j * 32 == getX()) {
            canMoveT = checkCanMove(i - 1, j);
            canMoveB = checkCanMove(i + 1, j);
            canMoveR = checkCanMove(i, j + 1);
            canMoveL = checkCanMove(i, j - 1);
            Random ran = new Random();
            int num;
            while(canMoveT || canMoveR || canMoveL || canMoveB) {
                num = ran.nextInt(4) + 1;
                if (num == 1 && canMoveR) {
                    moveH = speed;
                    moveV = 0;
                    break;
                }
                if (num == 2 && canMoveL) {
                    moveH = -speed;
                    moveV = 0;
                    break;
                }
                if (num == 3 && canMoveT) {
                    moveV = -speed;
                    moveH = 0;
                    break;
                }
                if (num == 4 && canMoveB) {
                    moveV = speed;
                    moveH = 0;
                    break;
                }
            }
        }
        setX(getX() + moveH);
        setY(getY() + moveV);
    }

    boolean checkCanMove(int i, int j) {
        return Map.maphash[i][j] == '-' || Map.maphash[i][j] == '.';
    }

    private boolean isPlayer(int xP, int yP) {
        //top left
        int i1 = (yP + 16);
        int j1 = (xP + 0);
        //bottom left
        int i2 = (yP + 48);
        int j2 = (xP + 0);
        //top right
        int i3 = (yP + 16);
        int j3 = (xP + 32);
        //bottom right
        int i4 = (yP + 48);
        int j4 = (xP + 32);

        int xM = (int) getX();
        int yM = (int) getY();

        //top left
        int iM1 = (yM);
        int jM1 = (xM);

        //bottom right
        int iM4 = (yM + 32);
        int jM4 = (xM + 32);

        //check top left
        if(j1 > jM1 && j1 < jM4 && i1 > iM1 && i1 < iM4) {
            return true;
        }
        //check top right
        if(j3 > jM1 && j3 < jM4 && i3 > iM1 && i3 < iM4) {
            return true;
        }
        //check bot left
        if(j2 > jM1 && j2 < jM4 && i2 > iM1 && i2 < iM4) {
            return true;
        }
        //check bot right
        if(j4 > jM1 && j4 < jM4 && i4 > iM1 && i4 < iM4) {
            return true;
        }

        return false;
    }

    public void checkiskicked() {
        if (Map.maphash[(int)(getY() + 16) / 32][(int)(getX() + 16) / 32] == '.') {
           setSurvival(false);
        }
    }
}
