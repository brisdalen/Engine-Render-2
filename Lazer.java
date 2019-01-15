import java.awt.*;
import java.util.Random;

public class Lazer {

    private int x, y, width, height, startX, startY, direction, speed;
    private Random randomer;

    public Lazer(int x, int y, int width, int height, int direction) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        startX = x;
        startY = y;
        this.direction = direction;
        randomer = new Random();
        speed = randomer.nextInt(5)+1;

        if(direction == 0 || direction == 2) {
            roamX(100);
        } else {
            roamY(100);
        }
    }

    public void roamX(int distance) {
        if(direction == 0) {
            if(x <= startX) {
                direction = 2;
            } else {
                stepDirection();
            }
        }
        if(direction == 2) {
            if(x >= startX + distance) {
                direction = 0;
            } else {
                stepDirection();
            }
        }
    }
    public void roamY(int distance) {
        if(direction == 1) {
            if(y <= startY) {
                direction = 3;
            } else {
                stepDirection();
            }
        }
        if(direction == 3) {
            if(y >= startY + distance) {
                direction = 1;
            } else {
                stepDirection();
            }
        }
    }

    public void stepDirection() {
        if(direction == 0) {
            x -= speed;
        }
        if(direction == 1) {
            y -= speed;
        }
        if(direction == 2) {
            x += speed;
        }
        if(direction == 3) {
            y += speed;
        }
    }

    public void checkCollision(Player player) {
        if(player.x <= x + width &&
                player.x + player.width >= x &&
                player.y <= y + height &&
                player.y + player.height >= y) {
            player.takeDamage(4);
        }
    }

    public Rectangle getClip(int offset) {

        int clipX = x - offset*3;
        int clipY = y - offset*3;
        int clipWidth = width + offset*2;
        int clipHeight = height + offset*2;

        return new Rectangle(clipX, clipY, clipWidth, clipHeight);
    }

    public int getDirection() {
        return this.direction;
    }

    public void paint(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(x-2, y-2, width+4, height+4);
        g.setColor(Color.red);
        g.fillRect(x,y,width,height);
    }
}
