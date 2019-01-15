import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Controller implements KeyListener {

    private Player player;
    public boolean left, up, right, down, k;

    public Controller(Player player) {
        this.player = player;
        left = false;
        up = false;
        right = false;
        down = false;
        k = false;
        System.out.println("Controller created.");
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
            left = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
            up = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
            right = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
            down = true;
        }
        switch(e.getKeyCode()) {
            case KeyEvent.VK_K:
                k = true;
                break;
            case KeyEvent.VK_SHIFT:
                player.movementSpeed = 2;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
            left = false;
        }
        if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
            up = false;
        }
        if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
            right = false;
        }
        if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
            down = false;
        }
        switch(e.getKeyCode()) {
            case KeyEvent.VK_K:
                k = false;
                break;
            case KeyEvent.VK_SHIFT:
                player.movementSpeed = player.movementSpeedStart;
                break;
        }
    }
}
