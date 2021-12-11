package com;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

public class Map {
    public static char [][] maphash;
    private LinkedList<Image> map;
    private Image background;
    public static ArrayList<Upgrade> upgrades;
    private LinkedList<Mob> mob;
    public static Point portal;

    public Map() {
        maphash = new char[13][31];
        map = new LinkedList<>();
        upgrades = new ArrayList<>();
        mob = new LinkedList<>();
        try {
            map.add(new Image(getClass().getResource("/Picture/hardWall.png").toURI().toString()));
            map.add(new Image(getClass().getResource("/Picture/softWall.png").toURI().toString()));
            map.add(new Image(getClass().getResource("/Picture/portal.png").toURI().toString()));
            background = new Image(getClass().getResource("/Picture/bg.png").toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public boolean isWinLevel() {
        return mob.size() == 0;
    }

    public void draw(GraphicsContext render) {
        render.drawImage(background, 0, 0);
        for (int i = 0; i < mob.size();) {
            mob.get(i).draw(render);
            if (!mob.get(i).isSurvival()) {
                mob.remove(i);
                continue;
            }
            i++;
        }
        for (int i = 0; i < upgrades.size();) {
            upgrades.get(i).draw(render);
            if (upgrades.get(i).checkPlayer()) {
                upgrades.get(i).effect();
                upgrades.remove(upgrades.get(i));
                continue;
            }
            i++;
        }
        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 31; j++) {
                if (maphash[i][j] == '#') {
                    render.drawImage(map.get(0), j * 32, i * 32);
                }
                if (maphash[i][j] == '*') {
                    render.drawImage(map.get(1), j * 32, i * 32);
                }
            }
        }
        if (mob.size() == 0) {
            render.drawImage(map.get(2), portal.getX(), portal.getY());
            maphash[(int) portal.getY() / 32][(int) portal.getX() / 32] = 'p';

        }
    }

    public void load(String s) {
        //load map from file
        try {
            LinkedList<Point> list = new LinkedList<>();
            File file = new File(getClass().getResource(s).toURI());
            Scanner cin = new Scanner(file);
            //check breakable block
            for (int i = 0; i < 13; i++) {
                String hash = cin.nextLine();
                for (int j = 0; j < 31; j++) {
                    maphash[i][j] = hash.charAt(j);
                    if (hash.charAt(j) == '1') {
                        mob.add(new Balloom(j * 32, i * 32));
                        maphash[i][j] = '-';
                    }
                    if (hash.charAt(j) == '2') {
                        mob.add(new Enemy1(j * 32, i * 32));
                        maphash[i][j] = '-';
                    }
                    if (hash.charAt(j) == '*') {
                        list.add(new Point(j, i));
                    }
                    if (hash.charAt(j) == 'p') {
                        portal = new Point(j * 32, i * 32);
                        maphash[i][j] = '-';
                    }
                }
            }
            //Random item under breakable block
            for (int i = 0; i < 12 && i < list.size(); i++) {
                Random ran = new Random();
                Random random = new Random();
                int numcd = random.nextInt(3);
                Point num = list.get(ran.nextInt(list.size()));
                upgrades.add(new Upgrade(num, numcd));
                list.remove(num);
            }

        } catch (URISyntaxException | FileNotFoundException e) {
            e.printStackTrace();
        }

    }

}
