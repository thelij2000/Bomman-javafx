package com;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class Engine extends Application {
    private GraphicsContext render;
    private Stage stage;
    private Bomberman bomberman;
    private Scene window;
    private Map map;
    private Media audio;
    MediaPlayer mediaPlayer;
    private int level;
    private int limitLv;

    @Override
    public void start(Stage mainStage) throws Exception {
        level = 1;
        limitLv = 2;
        Group group = new Group();
        Canvas canvas = new Canvas(992, 416);
        group.getChildren().add(canvas);
        Scene scene = new Scene(group, 992, 416);
        window = scene;
        mainStage.setScene(scene);
        mainStage.centerOnScreen();
        mainStage.setTitle("Bommanchuicuocdoi");
        mainStage.show();
        stage = mainStage;
        addLoop();
        render = canvas.getGraphicsContext2D();
        map = new Map();
        bomberman = new Bomberman();
        map.load("/map/map" + level + ".txt");
        addEvent();
        audio = new Media(Bomb.class.getResource("/Sound/Bgr.mp3").toURI().toString());
        createAudio();
    }

    void addEvent() {
        bomberman.addEvent(window);
    }

    void playIsPortal() {
        if (map.isWinLevel()) {
            if (bomberman.isInPortal()) {
                level++;
                if (level > limitLv) {
                    try {
                        stage.close();
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                map = new Map();
                bomberman = new Bomberman();
                System.out.println(level);
                map.load("/map/map" + level + ".txt");
                addEvent();
            }
        }
        if (Bomberman.life == 0) {
            try {
                stage.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void createAudio() {
        mediaPlayer = new MediaPlayer(audio);
        mediaPlayer.setOnEndOfMedia(() -> {
            createAudio();
        });
        mediaPlayer.play();
    }

    void addLoop() {
        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                update();
            }
        };
        animationTimer.start();
    }

    void update() {
        playIsPortal();
        render.clearRect(0, 0, window.getWidth(), window.getHeight());
        map.draw(render);
        bomberman.draw(render);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
