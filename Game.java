package basicWorldGame;



import java.awt.BorderLayout;

import java.awt.Canvas;

import java.awt.Color;

import java.awt.Dimension;

import java.awt.Graphics;

import java.awt.image.BufferStrategy;

import java.awt.image.BufferedImage;

import java.awt.image.DataBufferInt;

import java.io.File;

import java.io.IOException;



import javax.imageio.ImageIO;

import javax.swing.JFrame;

import javax.swing.JOptionPane;



public class Game extends Canvas implements Runnable {



   private static final long serialVersionUID = 1L;



   public static final int WIDTH = 300;

   public static final int HEIGHT = (WIDTH / 12 * 9) - 50;

   public static final int SCALE = 3;

   public static final String NAME = "Game";



   private boolean distanceHasBeenChecked = false;



   private int timesNeededXFour;

   private int timesNeededYFour;



   private int timesNeededXFive;

   private int timesNeededYFive;



   private int timesDoneXFour = 0;

   private int timesDoneYFour = 0;



   private int timesDoneXFive = 0;

   private int timesDoneYFive = 0;



   private int velX = 0;

   private int velY = 0;

   private boolean pointFound = false;

   private int xValue;

   private int yValue;



   double minPointAmount = 100.0;

   double maxPointAmount = 250.0;



   private int x = 0;

   private int y = 20;



   private JFrame frame;



   public boolean running = false;

   public int tickCount = 0;



   private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

   private BufferedImage blockImage = null;

   private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();



   public Game() {
   
      setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
   
      setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
   
      setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
   
   
   
      frame = new JFrame(NAME);
   
   
   
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   
      frame.setLayout(new BorderLayout());
   
   
   
      frame.add(this, BorderLayout.CENTER);
   
      frame.pack();
   
      frame.setResizable(false);
   
      frame.setLocationRelativeTo(null);
   
      frame.setVisible(true);
   
   
      try {
      
         blockImage = ImageIO.read(new File("/blockImage.png"));
      
      }
      
      catch (IOException e) {
      
      
      }
   
   }



   public synchronized void start() {
   
      running = true;
   
      new Thread(this).start();
   
   
   
   }



   public synchronized void stop() {
   
      running = false;
   
   }



   public void run() {
   
      long lastTime = System.nanoTime();
   
      double nsPerTick = 1000000000D / 60D;
   
   
   
      int ticks = 0;
   
      int frames = 0;
   
   
   
      long lastTimer = System.currentTimeMillis();
   
      double delta = 0;
   
   
   
      while (running) {
      
         long now = System.nanoTime();
      
         delta += (now - lastTime) / nsPerTick;
      
      
      
         lastTime = now;
      
         boolean shouldRender = true;
      
      
      
         while (delta >= 1) {
         
            ticks++;
         
            tick();
         
            delta -= 1;
         
            shouldRender = true;
         
         }
      
         try {
         
            Thread.sleep(2);
         
         } 
         catch (InterruptedException e) {
         
            e.printStackTrace();
         
         }
      
         if (shouldRender) {
         
            frames++;
         
            render();
         
         }
      
      
      
         if (System.currentTimeMillis() - lastTimer >= 1000) {
         
            lastTimer += 1000;
         
            System.out.println(ticks + " ticks, " + frames + " frames");
         
            System.out.println(x + "¡, " + y + "¡");
         
            frames = 0;
         
            ticks = 0;
         
         }
      
      }
   
   }



   public void tick() {
   
      tickCount++;
   
      for (int i = 0; i < pixels.length; i++) {
      
         pixels[i] = i + tickCount;
      
      }
   
   }



   public void render() {
   
      BufferStrategy bs = getBufferStrategy();
   
      if (bs == null) {
      
         createBufferStrategy(3);
      
         return;
      
      }
   
   
   
      Graphics g = bs.getDrawGraphics();
   
   
   
      g.setColor(Color.BLACK);
   
      g.fillRect(0, 0, getWidth(), getHeight());
   
      g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
   
   
   
   // For Testing Purposes to make sure that the box stays within the field
   
   // of movement
   
   // checkDirection();
   
   
      if (!distanceHasBeenChecked){
      
         checkDistance();
      
      }
   
   
   
      if (timesDoneXFour != timesNeededXFour) {
      
         velX = 4;
      
      } 
      else if (timesDoneXFive != timesNeededXFive) {
      
         velX = 5;
      
      } 
      else {
      
         velX = 0;
      
      }
   
   
   
      if (timesDoneYFour != timesNeededYFour) {
      
         velY = 4;
      
      } 
      else if (timesDoneYFive != timesNeededYFive) {
      
         velY = 5;
      
      } 
      else {
      
         velY = 0;
      
      }
   
   
   
      x += velX;
   
      y += velY;
   
   
   
      if (velX == 4) {
      
         timesDoneXFour += 1;
      
      } 
      else if (velX == 5) {
      
         timesDoneXFive += 1;
      
      }
   
   
   
      if (velY == 4) {
      
         timesDoneYFour += 1;
      
      } 
      else if (velY == 5) {
      
         timesDoneYFive += 1;
      
      }
   
   
   
      g.setColor(Color.BLACK);
   
      g.fillRect(0,0,getWidth(),20);
   
      g.setColor(Color.WHITE);
   
      g.drawString("The Box is at the x-coordinate of " + x + "¡, and at the y coordinate of " + y + "¡", 225, 15);
   
   
      g.setColor(Color.WHITE);
   
      g.fillRect(50, 60, 50, 50);
   
   
   
      g.setColor(Color.RED);
   
      g.fillRect(x, y, 50, 50);
   
   
   
   // g.setColor(Color.);
   
   // g.fillRect(120,100, 20, 20);
   
   
      g.dispose();
   
      bs.show();
   
   }



   private void checkDistance() {
   
      if (!pointFound) {
      
         xValue = getNumberInput("Please enter a value between 100 and 850", 100, 850);
      
         yValue = getNumberInput("Please enter a value between 75 and 475", 75, 475);
      
         pointFound = true;
      
      }
   
      int distanceX = xValue - x;
   
      int distanceY = yValue - y;
   
   
   
      calculateXTimes(distanceX);
   
      calculateYtimes(distanceY);
   
   
      distanceHasBeenChecked = true;
   
   }



   private int getNumberInput(String message, Integer minValue, Integer maxValue) {
   
      for (;;) {
      
         String input = JOptionPane.showInputDialog(message, JOptionPane.OK_OPTION);
      
         if (input == null || input.length() == 0) {
         
         // keep looping until we get some input!
         
            continue;
         
         }
      
         try {
         
            int val = Integer.parseInt(input);
         
            if (minValue != null && val < minValue.intValue() || maxValue != null && val > maxValue.intValue()) {
            
               continue;
            
            }
         
            return val;
         
         } 
         catch (NumberFormatException nfe) {
         
            input = null;
         
         }
      
      }
   
   }



   private void calculateXTimes(int distanceX) {
   
   
   
      if (distanceX % 4 == 0) {
      
         timesNeededXFour = distanceX / 4;
      
         timesNeededXFive = 0;
      
      } 
      else if (distanceX % 4 == 1) {
      
         timesNeededXFour = (distanceX - 5) / 4;
      
         timesNeededXFive = 1;
      
      } 
      else if (distanceX % 4 == 2) {
      
         timesNeededXFour = (distanceX - 10) / 4;
      
         timesNeededXFive = 2;
      
      } 
      else if (distanceX % 4 == 3) {
      
         timesNeededXFour = (distanceX - 15) / 4;
      
         timesNeededXFive = 3;
      
      }
   
   }



   private void calculateYtimes(int distanceY) {
   
      if (distanceY % 4 == 0) {
      
         timesNeededYFour = distanceY / 4;
      
         timesNeededYFive = 0;
      
      } 
      else if (distanceY % 4 == 1) {
      
         timesNeededYFour = (distanceY - 5) / 4;
      
         timesNeededYFive = 1;
      
      } 
      else if (distanceY % 4 == 2) {
      
         timesNeededYFour = (distanceY - 10) / 4;
      
         timesNeededYFive = 2;
      
      } 
      else if (distanceY % 4 == 3) {
      
         timesNeededYFour = (distanceY - 15) / 4;
      
         timesNeededYFive = 3;
      
      }
   
   }



   public void checkDirection() {
   
      if (x < 0 || x > (getWidth() - 50)) {
      
         velX = -velX;
      
      }
   
      if (y < 0 || y > (getHeight() - 50)) {
      
         velY = -velY;
      
      }
   
   }



   public static void main(String args[]) {
   
   
   
      new Game().start();
   
   
   
   }

}