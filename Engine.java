import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Engine extends Timer {

    public Window.DrawPanel drawPanel;
    public Player player;
    public ArrayList<Lazer> lazers;
    public ArrayList<HealPod> healPods;
    public int currentWindowWidth;
    public int currentWindowHeight;
    public Random randomer;
    public int startTime = -20;
    public int totalTime = startTime;
    private final double UPDATE_CAP = 1.0 / 60.0;

    public boolean running = false;
    public boolean hasStarted = false;
    public boolean hasLost = false;
    public boolean resetPrompt = false;

    public Engine(Window.DrawPanel drawPanel, Player player) {
        this.drawPanel = drawPanel;
        this.currentWindowWidth = drawPanel.getWidth();
        this.currentWindowHeight = drawPanel.getHeight();
        this.randomer = new Random();

        this.player = player;

        this.lazers = new ArrayList<>();
        this.healPods = new ArrayList<>();
    }

    public void init() {
        System.out.println("Engine init");
        running = true;

        boolean render = false;
        double firstTime = 0;
        double lastTime = System.nanoTime() / 1000000000.0;
        double passedTime = 0;
        double unproccestedTime = 0;

        double frameTime = 0;
        int frames = 0;
        int fps = 0;

        spawnNewHealpod(currentWindowWidth/2-20, currentWindowHeight/2-20);

        while(running) {
            render = false;

            firstTime = System.nanoTime() / 1000000000.0;
            passedTime = firstTime - lastTime;
            lastTime = firstTime;

            unproccestedTime += passedTime;
            frameTime += passedTime;

            while(unproccestedTime >= UPDATE_CAP) {
                unproccestedTime -= UPDATE_CAP;
                render = true;

                //Update game and get inputs    -   -   -   -   -   -   -   -   -   -
                // Let the player move before the game has started
                if (!hasLost && !hasStarted) {
                    checkPlayer(player, drawPanel);
                }

                // Prompt a reset when you lose and reset the game
                if(hasLost && !resetPrompt) {
                    drawPanel.updateParentFrame();
                    resetPrompt = true;
                    reset();
                }

                // The main movement loop
                if(!hasLost) {
                    checkPlayer(player, drawPanel);
                    // Put all lazers in motion
                    for (Lazer l : lazers) {
                        if (l.getDirection() == 0 || l.getDirection() == 2) {
                            l.roamX(100);
                        } else {
                            l.roamY(100);
                        }
                    }

                    checkLazerCollision(lazers, drawPanel);
                    checkHealPodCollision(healPods, drawPanel);
                }
                //  -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -

                if(frameTime >= 1.0) {
                    frameTime = 0;
                    fps = frames;
                    frames = 0;
                }
            }

            if(render) {
                System.out.println("Render!");
                //Clear the screen and render again
                renderLazers(lazers, drawPanel);
                renderHealPods(healPods, drawPanel);

                frames++;
            } else {
                try {
                    Thread.sleep(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void start() {
        hasStarted = true;
        /*
        scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //If you lost, and resetPrompt has not been displayed yet
                if(hasLost && !resetPrompt) {
                    drawPanel.updateParentFrame();
                    resetPrompt = true;
                    reset();
                }
                if(!hasLost) {
                    checkPlayer(player, drawPanel);
                    //Sett alle lasere i bevegelse
                    for (Lazer l : lazers) {
                        if (l.getDirection() == 0 || l.getDirection() == 2) {
                            l.roamX(100);
                        } else {
                            l.roamY(100);
                        }
                    }

                    //checkAllLazers(lazers, drawPanel);
                    //checkAllHealpods(healPods, drawPanel);
                }
            }
        }, 0, 40);
        //A timer with 1 second initial delay, followed by 1 second ticks
        scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(!hasLost) {
                    System.out.println(totalTime);
                    totalTime++;

                    if (totalTime == -13) {
                        spawnNewLazer();
                    }
                    if (totalTime == -8) {
                        spawnManyLazers(3);
                    }
                    if (totalTime == -6) {
                        spawnManyLazers(6);
                    }
                    if (totalTime == -3) {
                        System.out.println("CLEARED");
                        clearLazers();
                    }
                    if (totalTime == 0) {
                        spawnManyLazers(25);
                    }
                    if (totalTime % 10 == 0 && totalTime >= 0) {
                        clearLazers();
                        spawnManyLazers(25);
                    }
                    if (totalTime % 20 == 0 && totalTime >= 0) {
                        clearHealPods();
                        spawnManyHealpods(3);
                    }
                }

            }
        }, 1000, 1000);
        */
    }

    public void reset() {
        /**TODO: Add code to reset engine
         *  - reset timer to startTime
         *  - clear all healpods and lazers
         *  - set player location to the centre
         *  - spawn 1 healpod in the centre
         *  - reset hasLost
         */
        totalTime = startTime;
        clearHealPods();
        clearLazers();
        player.reset();

        spawnNewHealpod(currentWindowWidth/2-20, currentWindowHeight/2-20);

        resetPrompt = false;
    }

    public void stop() {
        hasLost = true;
        hasStarted = false;
    }

    private void checkPlayer(Player player, Window.DrawPanel drawPanel) {
        player.getInputs();
        if (player.x <= 0) {
            player.x = 0;
        }
        if (player.x + player.width >= currentWindowWidth) {
            player.x = currentWindowWidth - player.width - 1;
        }
        if (player.y <= 0) {
            player.y = 0;
        }
        if (player.y + player.height >= currentWindowHeight) {
            player.y = currentWindowHeight - player.height - 1;
        }
        drawPanel.repaint(getBigClipX1(), getBigClipY1(),
                getBigClipX2(), getBigClipY2());
    }
    private void checkLazerCollision(ArrayList<Lazer> lazers, Window.DrawPanel drawPanel) {
        for(Lazer l : lazers) {
            l.checkCollision(player);
        }
    }
    private void renderLazers(ArrayList<Lazer> lazers, Window.DrawPanel drawPanel) {
        for(Lazer l : lazers) {
            drawPanel.repaint(l.getClip(3));
        }
    }

    private void checkHealPodCollision(ArrayList<HealPod> healPods, Window.DrawPanel drawPanel) {
        for(HealPod hp : healPods) {
            hp.checkCollision(player);
        }
    }
    private void renderHealPods(ArrayList<HealPod> healPods, Window.DrawPanel drawPanel) {
        for(HealPod hp : healPods) {
            drawPanel.repaint(hp.getClip(3));
        }
    }

    public void clearHealPods() {
        healPods.clear();
        drawPanel.repaint();
    }

    public void spawnManyHealpods(int amount) {
        for(int i = 0; i < amount; i++) {
            spawnNewHealpod();
        }
    }
    public void spawnNewHealpod() {
        int width = 40;
        int height = 40;
        int x = randomer.nextInt(currentWindowWidth-width);
        int y = randomer.nextInt(currentWindowHeight-width);
        healPods.add(new HealPod(x,y,width,height));
    }
    public void spawnNewHealpod(int posX, int posY) {
        int width = 40;
        int height = 40;
        int x = posX;
        int y = posY;
        healPods.add(new HealPod(x,y,width,height));
    }
    public void clearLazers() {
        lazers.clear();
        drawPanel.repaint();
    }

    public void spawnManyLazers(int amount) {
        for(int i = 0; i < amount; i++) {
            spawnNewLazer();
        }
    }

    public void spawnNewLazer() {
        int type = randomer.nextInt(4);
        int width = 0;
        int height = 0;
        int x = 0;
        int y = 0;
        switch(type) {
            case 0:
                width = 100;
                height = 20;
                x = randomer.nextInt(currentWindowWidth-width);
                y = randomer.nextInt(currentWindowHeight-height);
                lazers.add(new Lazer(x, y, width, height,
                        getNewDirection(x, y, width, height)));
                break;
            case 1:
                width = 20;
                height = 100;
                x = randomer.nextInt(currentWindowWidth-width);
                y = randomer.nextInt(currentWindowHeight-height);
                lazers.add(new Lazer(x, y, width, height,
                        getNewDirection(x, y, width, height)));
                break;
            case 2:
                width = 80;
                height = 140;
                x = randomer.nextInt(currentWindowWidth-width);
                y = randomer.nextInt(currentWindowHeight-height);
                lazers.add(new Lazer(x, y, width, height,
                        getNewDirection(x, y, width, height)));
                break;
            case 3:
                width = 140;
                height = 80;
                x = randomer.nextInt(currentWindowWidth-width);
                y = randomer.nextInt(currentWindowHeight-height);
                lazers.add(new Lazer(x, y, width, height,
                        getNewDirection(x, y, width, height)));
                break;
        }
    }

    public int getNewDirection(int x, int y, int width, int height) {
        int direction = 0;
        //Typ 1 og 2
        if(height > width) {
            if(x - width < currentWindowWidth/2) {
                direction = 2;
            } else {
                direction = 0;
            }
        //Typ 0
        } else {
            if(y - height < currentWindowHeight/2) {
                direction = 3;
            } else {
                direction = 1;
            }
        }

        return direction;
    }

    public void win() {
        //TODO: Well.. theres no winning in this game..?
    	System.out.println("You have won!");
    }

    public Player getPlayer() {
        return player;
    }
    public ArrayList<Lazer> getLazers() {
        return lazers;
    }
    public ArrayList<HealPod> getHealPods() {
        return healPods;
    }

    public int getBigClipX1() {
        return player.x - player.width*2;
    }
    public int getBigClipX2() {
        return player.width*6;
    }
    public int getBigClipY1() {
        return player.y - player.height*2;
    }
    public int getBigClipY2() {
        return player.height*6;
    }
}
