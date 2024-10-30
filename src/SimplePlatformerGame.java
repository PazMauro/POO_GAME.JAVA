    import org.lwjgl.*;
    import org.lwjgl.glfw.*;
    import org.lwjgl.opengl.*;
    import org.lwjgl.system.*;
    import org.lwjgl.stb.STBImage;
    import java.nio.ByteBuffer;
    import java.nio.IntBuffer;
    import javax.sound.sampled.AudioInputStream;
    import javax.sound.sampled.AudioSystem;
    import javax.sound.sampled.Clip;
    import javax.sound.sampled.LineUnavailableException;
    import javax.sound.sampled.UnsupportedAudioFileException;
    import java.io.File;
    import java.io.IOException;
    import org.lwjgl.stb.STBTTFontinfo;
    import org.lwjgl.stb.STBTruetype;
    import org.lwjgl.system.MemoryStack;

    import java.nio.ByteBuffer;
    import java.nio.FloatBuffer;

    
    import static org.lwjgl.stb.STBTruetype.*;
    import static org.lwjgl.system.MemoryUtil.*;

    import static org.lwjgl.openal.ALC10.*;
    import static org.lwjgl.stb.STBVorbis.*;
    import static org.lwjgl.system.MemoryStack.*;

    import static org.lwjgl.opengl.GL12.*;  



    import java.nio.*;
    import static org.lwjgl.glfw.GLFW.*;
    
    import static org.lwjgl.system.MemoryUtil.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;


    public class SimplePlatformerGame {

    private long window;
    private List<Enemy> enemies = new ArrayList<>();
    private int enemyTexture;
    private boolean scoreChanged = false;
    private int score = 0;
    private Clip attackSound;
    private Clip walkSound;
    private Clip coinSound;
    private int floorTexture;
    private float playerX = 1.5f, playerZ = 1.5f;  
    private float playerAngle = 0.0f;             
    private float moveSpeed = 0.1f;              
    private float turnSpeed = 1.5f;      
    private float playerPitch = 0.0f;  
    private int attackTexture; 
    private boolean isAttacking = false; 
    private long attackStartTime = 0; 
    private long attackDuration = 250; 
    private float hudOscillationPhase = 0.0f;
    private float hudOscillationAmplitude = 5.0f; 
    private float hudOscillationSpeed = 0.1f;     
    private boolean isWalking = false;            
    private float playerY = 0.0f;  
    private float jumpSpeed = 0.15f;  
    private float gravity = 0.01f;  
    private boolean isJumping = false;  
    private float currentJumpSpeed = 0.0f; 
    private Clip backgroundMusic;
    private int wallTexture; 
    private int coinTexture;
    private int playerTexture; 
    private int textTexture;
    



    
        private int[][] maze = {
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1},
        {1, 0, 0, 1, 0, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 2, 0, 1},
        {1, 0, 0, 2, 0, 1, 0, 1, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1},
        {1, 0, 1, 1, 0, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 2, 1, 1, 1},
        {1, 0, 1, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 1, 0, 1},
        {1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 1},
        {1, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1},
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 3, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 1},
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1},
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 2, 0, 0, 0, 2, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1},
        {1, 0, 0, 1, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1},
        {1, 0, 0, 0, 0, 1, 0, 0, 2, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 2, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 3, 0, 1},
        {1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        };
        private void cleanupAudio() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
            backgroundMusic.close();
        }
        if (attackSound != null && attackSound.isRunning()) {
            attackSound.stop();
            attackSound.close();
        }
        if (walkSound != null && walkSound.isRunning()) {
            walkSound.stop();
            walkSound.close();
        }
        if (coinSound != null && coinSound.isRunning()) {
            coinSound.stop();
            coinSound.close();
        }
    }
    private void stopWalking() {
        walkSound.stop();
            walkSound.close();
    }

        public void run() {
           
            init();
            loop();

            
            glfwDestroyWindow(window);
            glfwTerminate();
            glfwSetErrorCallback(null).free();
            cleanupAudio();
        
        }
    private void loadAndPlayMusic(String filePath) {
        try {
        
            File audioFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);


            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioStream);

            
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
            backgroundMusic.start();

        } catch (UnsupportedAudioFileException e) {
        System.out.println("El archivo de audio no es compatible: " + e.getMessage());
        e.printStackTrace();
    } catch (IOException e) {
        System.out.println("Error de entrada/salida al cargar el archivo de audio: " + e.getMessage());
        e.printStackTrace();
    } catch (LineUnavailableException e) {
        System.out.println("No se puede acceder al dispositivo de audio: " + e.getMessage());
        e.printStackTrace();
    }
    }
    class Enemy {
    float x, z;
    int texture;
    
    public Enemy(float x, float z, int texture) {
        this.x = x;
        this.z = z;
        this.texture = texture;
    }

    
    public void moveTowards(float playerX, float playerZ, float speed) {
        float dx = playerX - this.x;
        float dz = playerZ - this.z;
        float distance = (float) Math.sqrt(dx * dx + dz * dz);

      
        if (distance > 0.01f) { 
            this.x += (dx / distance) * speed;
            this.z += (dz / distance) * speed;
        }
    }
}

    private void loadAttackSound(String filePath) {
        try {
            File soundFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            attackSound = AudioSystem.getClip();
            attackSound.open(audioStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
    private void playAttackSound() {
        if (attackSound != null) {
            if (attackSound.isRunning()) {
                attackSound.stop(); 
            }
            attackSound.setFramePosition(0); 
            attackSound.start();
        }
    }
    private void loadCoinSound(String filePath) {
        try {
            File soundFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            coinSound = AudioSystem.getClip();
            coinSound.open(audioStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
    private void playCoinSound() {
        if (coinSound != null) {
            if (coinSound.isRunning()) {
                coinSound.stop(); 
            }
            coinSound.setFramePosition(0); 
            coinSound.start();
        }
    }
    private void loadWalkSound(String filePath) {
    try {
        File soundFile = new File(filePath);
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
        walkSound = AudioSystem.getClip();
        walkSound.open(audioStream);
    } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
        e.printStackTrace();
    }
}
 private void playWalkSound() {
    if (walkSound != null) {
        if (isWalking) {
            if (!walkSound.isRunning()) {
                walkSound.setFramePosition(0); 
                walkSound.start();
            }
        } else {
            if (walkSound.isRunning()) {
                walkSound.stop();
            }
        }
    }
}
        private void init() {
        loadAndPlayMusic("/home/santiago-larrosa/Descargas/My3dPlatform/src/Quest-of-Light.wav");
    loadAttackSound("/home/santiago-larrosa/Descargas/My3dPlatform/src/attack.wav");
    loadWalkSound("/home/santiago-larrosa/Descargas/My3dPlatform/src/Pasos.wav");
    loadCoinSound("/home/santiago-larrosa/Descargas/My3dPlatform/src/moneda.wav");
  


            GLFWErrorCallback.createPrint(System.err).set();
            if (!glfwInit()) {
                throw new IllegalStateException("No GLFW");
            }

        
            glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
            glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
            glfwWindowHint(GLFW_DEPTH_BITS, 24);  
            window = glfwCreateWindow(800, 600, "Juego 3D de DOOM KNIGHT", NULL, NULL);
            if (window == NULL) {
                throw new RuntimeException("No se pudo crear la ventana");
            }

            
            glfwMakeContextCurrent(window);
            glfwSwapInterval(1); 
            glfwShowWindow(window);

            GL.createCapabilities();

    
            glViewport(0, 0, 800, 600);

            
            glMatrixMode(GL_PROJECTION);
            glLoadIdentity();
            gluPerspective(60.0f, 800f / 600f, 0.1f, 100.0f); 
            glMatrixMode(GL_MODELVIEW);

            
            glEnable(GL_BLEND);
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);


            glEnable(GL_DEPTH_TEST);
            glDepthFunc(GL_LESS);
    
    glEnable(GL_LIGHTING);
    glEnable(GL_LIGHT0);

    float[] lightPosition = {1.0f, 1.0f, 1.0f, 0.0f}; 
    glLightfv(GL_LIGHT0, GL_POSITION, lightPosition);


    float[] lightAmbient = {1.5f, 1.5f, 1.5f, 1.5f};
    float[] lightDiffuse = {4.5f, 3.5f, 3.5f, 7.7f};  
    glLightfv(GL_LIGHT0, GL_AMBIENT, lightAmbient);
    glLightfv(GL_LIGHT0, GL_DIFFUSE, lightDiffuse);


    glEnable(GL_COLOR_MATERIAL);
    glColorMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE);

    playerTexture = loadTexture("/home/santiago-larrosa/Descargas/My3dPlatform/src/manos1.png");
    attackTexture = loadTexture("/home/santiago-larrosa/Descargas/My3dPlatform/src/manos2.png");
    wallTexture = loadTexture("/home/santiago-larrosa/Descargas/My3dPlatform/src/fondinho2.png");
    floorTexture = loadTexture("/home/santiago-larrosa/Descargas/My3dPlatform/src/fondinho3.png");
    coinTexture = loadTexture("/home/santiago-larrosa/Descargas/My3dPlatform/src/monedinha.png");
     enemyTexture = loadTexture("/home/santiago-larrosa/Descargas/My3dPlatform/src/enemigo.png");
     System.out.println("Textura del enemigo cargada: " + enemyTexture);




        }
        private int loadTexture(String path) {
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);

        
    
        System.out.println("Directorio de trabajo actual: " + System.getProperty("user.dir"));
        
    ByteBuffer image = STBImage.stbi_load(path, width, height, channels, 4);
        if (image == null) {
            String errorMessage = STBImage.stbi_failure_reason();
            throw new RuntimeException("Error al cargar imagen: " + path + ". Razón: " + errorMessage);
            
        }

    
        int textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);
    
        
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
        
    
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

    
        STBImage.stbi_image_free(image);

        return textureID;
    }


    
        private void gluPerspective(float fov, float aspect, float near, float far) {
            float y_scale = (float) (1.0f / Math.tan(Math.toRadians(fov / 2)));
            float x_scale = y_scale / aspect;
            float frustum_length = far - near;

            FloatBuffer perspective = BufferUtils.createFloatBuffer(16);
            perspective.put(new float[]{
                x_scale, 0, 0, 0,
                0, y_scale, 0, 0,
                0, 0, -((far + near) / frustum_length), -1,
                0, 0, -((2 * near * far) / frustum_length), 0
            });
            perspective.flip();

            glLoadMatrixf(perspective);
        }

        private void loop() {
        
            glClearColor(0.75f, 0.7f, 0.7f, 0.7f);

            while (!glfwWindowShouldClose(window)) {
                
                
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            
                handleInput();

           
             
        
                renderFloor();

                
                renderMaze();
                
               if (isWalking) {
            hudOscillationPhase += hudOscillationSpeed; // Incrementa la fase de oscilación
        }
            updateEnemies();
        renderEnemies(); 
                renderPlayerHUD();
              
                updatePlayer();
                

               /*  if (!(glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS) && !(glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS)){
            stopWalking();
        }*/

            
                glfwSwapBuffers(window);
                glfwPollEvents();
            }
        }
    private void updatePlayer() {
        
        if (isJumping) {
            
            playerY += currentJumpSpeed;
            
            
            currentJumpSpeed -= gravity;

            
            if (playerY <= 0.0f) {
                playerY = 0.0f;  
                isJumping = false;
                currentJumpSpeed = 0.0f;  
            }
        }
        checkCoinPickup();
    }
    private int createTextTexture(String text) {
    
    BufferedImage bufferedImage = new BufferedImage(256, 64, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g = bufferedImage.createGraphics();
    
   
    g.setFont(new Font("Arial", Font.BOLD, 24));
    g.setColor(new Color(255, 255, 255, 0)); 
    g.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight()); 
    g.setColor(Color.YELLOW); 
    g.drawString(text, 10, 40); 

    g.dispose(); 

    int textureID = glGenTextures();
    glBindTexture(GL_TEXTURE_2D, textureID);
    
  
    ByteBuffer buffer = ByteBuffer.allocateDirect(bufferedImage.getWidth() * bufferedImage.getHeight() * 4);
    for (int y = 0; y < bufferedImage.getHeight(); y++) {
        for (int x = 0; x < bufferedImage.getWidth(); x++) {
            int pixel = bufferedImage.getRGB(x, y);
            buffer.put((byte) ((pixel >> 16) & 0xFF)); 
            buffer.put((byte) ((pixel >> 8) & 0xFF));  
            buffer.put((byte) (pixel & 0xFF));           
            buffer.put((byte) ((pixel >> 24) & 0xFF)); 
        }
    }
    buffer.flip();

    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, bufferedImage.getWidth(), bufferedImage.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
    
   
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

    return textureID;
}
  private void checkCoinPickup() {
    int mazeX = (int) Math.floor(playerX + 0.5f);
    int mazeZ = (int) Math.floor(playerZ + 0.5f);

   
    if (maze[mazeZ][mazeX] == 2) {
        maze[mazeZ][mazeX] = 0;
        playCoinSound(); 
        score += 10; 
        scoreChanged = true; 
        System.out.println("Moneda recogida en (" + mazeX + ", " + mazeZ + ")");
    }
}


 private void handleInput() {
    boolean isMoving = false; 

    // Manejo de la rotación
    if (glfwGetKey(window, GLFW_KEY_LEFT) == GLFW_PRESS) {
        playerAngle += turnSpeed;
        isMoving = true;
    }

    if (glfwGetKey(window, GLFW_KEY_RIGHT) == GLFW_PRESS) {
        playerAngle -= turnSpeed;
        isMoving = true;
    }

    if (glfwGetKey(window, GLFW_KEY_UP) == GLFW_PRESS) {
        playerPitch -= turnSpeed;
        if (playerPitch < -89.0f) { 
            playerPitch = -89.0f;
        }
        isMoving = true;
    }

    if (glfwGetKey(window, GLFW_KEY_DOWN) == GLFW_PRESS) {
        playerPitch += turnSpeed;  
        if (playerPitch > 89.0f) { 
            playerPitch = 89.0f;
        }
        isMoving = true;
    }

    // Movimiento hacia adelante
    if (glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS) {
        float newX = playerX + (float) Math.sin(Math.toRadians(playerAngle)) * moveSpeed;
        float newZ = playerZ - (float) Math.cos(Math.toRadians(playerAngle)) * moveSpeed;

        if (!checkCollision(newX, newZ)) {
            playerX = newX;
            playerZ = newZ;
            isMoving = true; // El jugador se está moviendo
        }
    }

    // Movimiento hacia atrás
    if (glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS) {
        float newX = playerX - (float) Math.sin(Math.toRadians(playerAngle)) * moveSpeed;
        float newZ = playerZ + (float) Math.cos(Math.toRadians(playerAngle)) * moveSpeed;

        if (!checkCollision(newX, newZ)) {
            playerX = newX;
            playerZ = newZ;
            isMoving = true; // El jugador se está moviendo
        }
    }

    // Actualiza la variable isWalking
    isWalking = isMoving;

    // Reproducir o detener el sonido de caminar
    playWalkSound();

    // Manejo de ataque
    if (glfwGetKey(window, GLFW_KEY_X) == GLFW_PRESS && !isAttacking) {
        isAttacking = true;
        removeNearbyEnemies();
        attackStartTime = System.currentTimeMillis();
        playAttackSound();
    }

    // Manejo de salto
    if (glfwGetKey(window, GLFW_KEY_SPACE) == GLFW_PRESS && !isJumping) {
        isJumping = true;
        currentJumpSpeed = jumpSpeed;
    }
}

        
        private boolean checkCollision(float x, float z) {
            int mazeX = (int) Math.floor(x + 0.5f);
            int mazeZ = (int) Math.floor(z + 0.5f);

            
            if (mazeZ < 0 || mazeZ >= maze.length || mazeX < 0 || mazeX >= maze[0].length) {
                return true; 
            }

            return maze[mazeZ][mazeX] == 1;
        }

private void removeNearbyEnemies() {
    float thresholdDistance = 1.0f; 
    score += 100; 
        scoreChanged = true; 
    enemies.removeIf(enemy -> {
        float dx = playerX - enemy.x;
        float dz = playerZ - enemy.z;
        
        float distance = (float) Math.sqrt(dx * dx + dz * dz);
        return distance < thresholdDistance; 
    });
}
        
    private void renderMaze() {
        glLoadIdentity();

       
        glTranslatef(0.0f, -playerY, 0.0f);

       
        glRotatef(playerPitch, 1.0f, 0.0f, 0.0f); 
        glRotatef(playerAngle, 0.0f, 1.0f, 0.0f);  
        glTranslatef(-playerX, -1.0f, -playerZ);    

        for (int z = 0; z < maze.length; z++) {
            for (int x = 0; x < maze[0].length; x++) {
                   if (maze[z][x] == 3) {
                enemies.add(new Enemy(x + 0.5f, z + 0.5f, enemyTexture));
                maze[z][x] = 0; 
            }
                renderFloorTile(x, z);
                if (maze[z][x] == 1) {
                    renderWall(x, z);
                } else if (maze[z][x] == 2) {
                    renderCoin(x, z);
                }
            }
        }
    }
private void updateEnemies() {
    for (Enemy enemy : enemies) {
        
        float newX = enemy.x;
        float newZ = enemy.z;

        float dx = playerX - enemy.x;
        float dz = playerZ - enemy.z;
        float distance = (float) Math.sqrt(dx * dx + dz * dz);

        
        if (distance > 0.01f) { 
            newX += (dx / distance) * 0.02f; 
            newZ += (dz / distance) * 0.02f; 
        }

      
        if (!checkCollision(newX, newZ)) {
            enemy.x = newX;
            enemy.z = newZ;
        }
    }
}


private void renderPlayerHUD() {
    glPushMatrix();
    
    glMatrixMode(GL_PROJECTION);
    glPushMatrix();
    glLoadIdentity();
    glOrtho(0, 750, 350, 0, -1, 1);
    
    glMatrixMode(GL_MODELVIEW);
    glLoadIdentity();
    glEnable(GL_BLEND);
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    
    glEnable(GL_TEXTURE_2D);
    glBindTexture(GL_TEXTURE_2D, playerTexture);
    
    if (textTexture == 0 || scoreChanged) {
        textTexture = createTextTexture("Score: " + score); 
        scoreChanged = false;
    }
    
    glBindTexture(GL_TEXTURE_2D, textTexture);
    
    // Oscilación del HUD
    float oscillationOffset = (float) Math.sin(hudOscillationPhase) * hudOscillationAmplitude;
    float scoreXPos = 10; 
    float scoreYPos = 20 + oscillationOffset; // Aplicar la oscilación en la posición Y
    float scoreWidth = 200; 
    float scoreHeight = 60; 

    glBegin(GL_QUADS);
    glTexCoord2f(0, 0); glVertex2f(scoreXPos, scoreYPos);
    glTexCoord2f(1, 0); glVertex2f(scoreXPos + scoreWidth, scoreYPos);
    glTexCoord2f(1, 1); glVertex2f(scoreXPos + scoreWidth, scoreYPos + scoreHeight);
    glTexCoord2f(0, 1); glVertex2f(scoreXPos, scoreYPos + scoreHeight);
    glEnd();
    
    long currentTime = System.currentTimeMillis();
    if (isAttacking) {
        glBindTexture(GL_TEXTURE_2D, attackTexture); 
        if (currentTime - attackStartTime > attackDuration) {
            isAttacking = false; 
        }
    } else {
        glBindTexture(GL_TEXTURE_2D, playerTexture); 
    }

    float xPos = 10; 
    float yPos = 110 + oscillationOffset; // Aplicar la oscilación en la posición Y
    float width = 750; 
    float height = 250; 

    glBegin(GL_QUADS);
    glTexCoord2f(0, 0); glVertex2f(xPos, yPos);
    glTexCoord2f(1, 0); glVertex2f(xPos + width, yPos);
    glTexCoord2f(1, 1); glVertex2f(xPos + width, yPos + height);
    glTexCoord2f(0, 1); glVertex2f(xPos, yPos + height);
    glEnd();

    glDisable(GL_TEXTURE_2D);
    glDisable(GL_BLEND);
    
    glMatrixMode(GL_PROJECTION);
    glPopMatrix();
    
    glMatrixMode(GL_MODELVIEW);
    glPopMatrix();
}

    
    private void renderWall(int x, int z) {
        glPushMatrix();
        glTranslatef(x, 0.0f, z);
        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, wallTexture);
        glBegin(GL_QUADS);

       
        float repeatFactor = 2.0f;

        
        glNormal3f(0.0f, 0.0f, -1.0f); 
        glTexCoord2f(0.0f, 0.0f); glVertex3f(-0.5f, 0.0f, -0.5f);
        glTexCoord2f(repeatFactor, 0.0f); glVertex3f(0.5f, 0.0f, -0.5f);
        glTexCoord2f(repeatFactor, repeatFactor); glVertex3f(0.5f, 3.0f, -0.5f);
        glTexCoord2f(0.0f, repeatFactor); glVertex3f(-0.5f, 3.0f, -0.5f);

       
        glNormal3f(0.0f, 0.0f, 1.0f); 
        glTexCoord2f(0.0f, 0.0f); glVertex3f(-0.5f, 0.0f, 0.5f);
        glTexCoord2f(repeatFactor, 0.0f); glVertex3f(0.5f, 0.0f, 0.5f);
        glTexCoord2f(repeatFactor, repeatFactor); glVertex3f(0.5f, 3.0f, 0.5f);
        glTexCoord2f(0.0f, repeatFactor); glVertex3f(-0.5f, 3.0f, 0.5f);

       
        glNormal3f(-1.0f, 0.0f, 0.0f);  
        glTexCoord2f(0.0f, 0.0f); glVertex3f(-0.5f, 0.0f, 0.5f);
        glTexCoord2f(repeatFactor, 0.0f); glVertex3f(-0.5f, 0.0f, -0.5f);
        glTexCoord2f(repeatFactor, repeatFactor); glVertex3f(-0.5f, 3.0f, -0.5f);
        glTexCoord2f(0.0f, repeatFactor); glVertex3f(-0.5f, 3.0f, 0.5f);

      
        glNormal3f(1.0f, 0.0f, 0.0f); 
        glTexCoord2f(0.0f, 0.0f); glVertex3f(0.5f, 0.0f, -0.5f);
        glTexCoord2f(repeatFactor, 0.0f); glVertex3f(0.5f, 0.0f, 0.5f);
        glTexCoord2f(repeatFactor, repeatFactor); glVertex3f(0.5f, 3.0f, 0.5f);
        glTexCoord2f(0.0f, repeatFactor); glVertex3f(0.5f, 3.0f, -0.5f);

        glEnd();
        glDisable(GL_TEXTURE_2D);
        glPopMatrix();
    }
    private void renderFloorTile(int x, int z) {
        glEnable(GL_TEXTURE_2D); 
        glBindTexture(GL_TEXTURE_2D, floorTexture);  

        glBegin(GL_QUADS);
        
        glTexCoord2f(0, 0); glVertex3f(x, 0, z);
        glTexCoord2f(1, 0); glVertex3f(x + 1, 0, z);
        glTexCoord2f(1, 1); glVertex3f(x + 1, 0, z + 1);
        glTexCoord2f(0, 1); glVertex3f(x, 0, z + 1);
        
        glEnd();
        
        glDisable(GL_TEXTURE_2D); 
    }

    private void renderCoin(int x, int z) {
        
        glPushMatrix();
        glTranslatef(x, 0.5f, z);  
        
       
        float angleToPlayer = (float) Math.toDegrees(Math.atan2(playerX - x, playerZ - z));
        glRotatef(angleToPlayer, 0.0f, 1.0f, 0.0f); 
        
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        
        glBindTexture(GL_TEXTURE_2D, coinTexture);

        glBegin(GL_QUADS);
        
       
        glTexCoord2f(0.0f, 1.0f); glVertex3f(-0.25f, -0.25f, 0.0f);
        glTexCoord2f(1.0f, 1.0f); glVertex3f(0.25f, -0.25f, 0.0f);
        glTexCoord2f(1.0f, 0.0f); glVertex3f(0.25f, 0.25f, 0.0f);
        glTexCoord2f(0.0f, 0.0f); glVertex3f(-0.25f, 0.25f, 0.0f);
        
        glEnd();
        
        glDisable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glPopMatrix();
    }



        
        private void renderFloor() {
            glPushMatrix();
            glTranslatef(0.0f, -1.0f, 0.0f); 
            glBegin(GL_QUADS);
            
        

            
            glVertex3f(-4.0f, 0.0f, -4.0f);
            glVertex3f(4.0f, 0.0f, -4.0f);
            glVertex3f(4.0f, 0.0f, 4.0f);
            glVertex3f(-4.0f, 0.0f, 4.0f);

            glEnd();
            glPopMatrix();
        }
            
private void renderEnemies() {
    for (Enemy enemy : enemies) {
        glPushMatrix();
        
      
        glTranslatef(enemy.x, 0.5f, enemy.z);

       
        float angle = (float) Math.toDegrees(Math.atan2(playerX - enemy.x, playerZ - enemy.z));
        glRotatef(-angle, 0, 1, 0);
glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_TEXTURE_2D);
        

        glBindTexture(GL_TEXTURE_2D, enemyTexture);
        glBegin(GL_QUADS);

        
        glTexCoord2f(0.0f, 1.0f); glVertex3f(-0.25f, 0.0f, 0.25f); 
        glTexCoord2f(1.0f, 1.0f); glVertex3f(0.25f, 0.0f, 0.25f); 
        glTexCoord2f(1.0f, 0.0f); glVertex3f(0.25f, 1.0f, 0.25f); 
        glTexCoord2f(0.0f, 0.0f); glVertex3f(-0.25f, 1.0f, 0.25f); 
        glEnd();
        
        glDisable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glPopMatrix();
    }
}
        public static void main(String[] args) {
            new SimplePlatformerGame().run();
        }
    }
    //javac -cp "lib/*" -d build src/SimplePlatformerGame.java
    //java -cp "build:lib/*" -Djava.library.path=native/linux SimplePlatformerGame
