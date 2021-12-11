package com;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URISyntaxException;
import java.util.ArrayList;

public class Bomb {
    private int x;
    private int y;
    private Image bomb;
    private int timeAnimation;
    private int status;
    private int firestatus;
    public static ArrayList<Bomb> listBomb = new ArrayList<>();
    private Image bombfire;
    private boolean boomed;
    private int up;
    private int down;
    private int left;
    private int right;
    private Media audioClip = null;

    public Bomb(int x, int y) {
        left = down = up = right = Bomberman.bombPower;
        firestatus = 0;
        double X = x + 16;
        double Y = y + 32;
        int xx = (int) X / 32;
        int yy = (int) Y / 32;
        timeAnimation = 0;
        status = 0;
        boomed = false;
        try {
            bomb = new Image(getClass().getResource("/Picture/bomb.png").toURI().toString());
            bombfire = new Image(getClass().getResource("/Picture/explosion.png").toURI().toString());
            audioClip = new Media(Bomb.class.getResource("/Sound/Bomb.wav").toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        this.x = xx * 32;
        this.y = yy * 32;
        listBomb.add(this);
    }

    public void draw(GraphicsContext render) {
        if (!boomed) {
            //check if bomman leave this stage
            if (!Bomberman.checkinbomb(this)) {
                Map.maphash[y / 32][x / 32] = '+';
            }
            timeAnimation++;
            if (timeAnimation % 5 == 0) {
                if (status <= 14) {
                    status ++;
                } else {
                    status = 0;
                }
            }
            if (timeAnimation > 150) {
                boomed = true;
                MediaPlayer mediaPlayer = new MediaPlayer(audioClip);
                mediaPlayer.play();
                timeAnimation = 0;
            }
            render.drawImage(bomb, status * 32, 0, 32, 32, x, y, 32, 32);
        } else {
            timeAnimation++;
            if (timeAnimation % 5 == 0) {
                if (firestatus < 9) {
                    firestatus++;
                } else {
                    firestatus = 0;
                }
            }
            if (timeAnimation > 45) {
                Map.maphash[y / 32][x / 32] = '-';
                for (int i = 1; i <= right; i++) {
                    Map.maphash[y / 32][(x / 32) + i] = '-';
                }
                for (int i = 1; i <= left; i++) {
                    Map.maphash[y / 32][(x / 32) - i] = '-';
                }
                for (int i = 1; i <= up; i++) {
                    Map.maphash[(y / 32) - i][x / 32] = '-';
                }
                for (int i = 1; i <= down; i++) {
                    Map.maphash[(y / 32) + i][x / 32] = '-';
                }
                listBomb.remove(this);
            }
            //check right
            for (int i = x / 32; i <= (x / 32) + right && i < 31; i++) {
                if (Map.maphash[y / 32][i] != '-') {
                    if (Map.maphash[y / 32][i] == '*') {
                        Map.maphash[y / 32][i] = '-';
                        right = i - x / 32 - 1;
                        break;
                    } else if (Map.maphash[y / 32][i] == '#') {
                        right = i - x / 32 - 1;
                        break;
                    }
                }
            }
            //check left
            for (int i = x / 32; i >= (x / 32) - left && i >= 0; i--) {
                if (Map.maphash[y / 32][i] != '-') {
                    if (Map.maphash[y / 32][i] == '*') {
                        Map.maphash[y / 32][i] = '-';
                        left = x / 32 - i - 1;
                        break;
                    } else if (Map.maphash[y / 32][i] == '#') {
                        left = x / 32 - i - 1;
                        break;
                    }
                }
            }
            //check up
            for (int i = y / 32; i >= (y / 32) - up && i >= 0; i--) {
                if (Map.maphash[i][x / 32] != '-') {
                    if (Map.maphash[i][x / 32] == '*') {
                        Map.maphash[i][x / 32] = '-';
                        up = y / 32 - i - 1;
                        break;
                    } else if (Map.maphash[i][x / 32] == '#') {
                        up = y / 32 - i - 1;
                        break;
                    }
                }
            }
            //check down
            for (int i = y / 32; i <= (y / 32) + down && i < 13; i++) {
                if (Map.maphash[i][x / 32] != '-') {
                    if (Map.maphash[i][x / 32] == '*') {
                        Map.maphash[i][x / 32] = '-';
                        down = i - y / 32 - 1;
                        break;
                    } else if (Map.maphash[i][x / 32] == '#') {
                        down = i - y / 32 - 1;
                        break;
                    }
                }
            }
            if (timeAnimation < 45) {
                //comment
                Map.maphash[y / 32][x / 32] = '.';
                for (int i = 1; i <= right; i++) {
                    Map.maphash[y / 32][(x / 32) + i] = '.';
                }
                for (int i = 1; i <= left; i++) {
                    Map.maphash[y / 32][(x / 32) - i] = '.';
                }
                for (int i = 1; i <= up; i++) {
                    Map.maphash[(y / 32) - i][x / 32] = '.';
                }
                for (int i = 1; i <= down; i++) {
                    Map.maphash[(y / 32) + i][x / 32] = '.';
                }
            }

            render.drawImage(bombfire, 0 + firestatus * 32, 0, 32, 32, x, y, 32, 32);
            for (int i = 1; i <= right; i++) {
                render.drawImage(bombfire, 0 + firestatus * 32, 32, 32, 32, x + i * 32, y, 32, 32);
            }
            for (int i = 1; i <= left; i++) {
                render.drawImage(bombfire, 0 + firestatus * 32, 32, 32, 32, x - i * 32, y, 32, 32);
            }
            for (int i = 1; i <= up; i++) {
                render.drawImage(bombfire, 0 + firestatus * 32, 64, 32, 32, x, y - i * 32, 32 ,32);
            }
            for (int i = 1; i <= down; i++) {
                render.drawImage(bombfire, 0 + firestatus * 32, 64, 32, 32, x, y + i * 32, 32, 32);
            }
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Image getBomb() {
        return bomb;
    }
}
