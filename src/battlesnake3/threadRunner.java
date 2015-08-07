/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battlesnake3;

/**
 *
 * @author johanwendt
 */
public class threadRunner implements Runnable{
    private Player player;
    private int gameSpeed;

   
            
    public threadRunner(Player player, int gameSpeed) {
        
    }
    @Override
    public void run() {
        try {
            while (player.isAlive()) {
                player.movePlayer();
                Thread.sleep(1000/gameSpeed);
            }    
        }
        catch (InterruptedException ex) {
        }
    }
}

    

