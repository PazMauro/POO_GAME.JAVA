    import org.lwjgl.*;
    import org.lwjgl.glfw.*;
    import org.lwjgl.opengl.*;
    import org.lwjgl.system.*;
    import org.lwjgl.stb.STBImage;
    import java.nio.ByteBuffer;
    import java.nio.IntBuffer;
    import javax.sound.sampled.FloatControl;
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
    private int exitButtonTexture;
    private int victoryTexture;
    private boolean teleportedTo300 = false; 
private boolean teleportedTo600 = false;
    private int enemyTexture;
    private int playerLives = 3; 
    private float timeNearEnemy = 0.0f; 
    private final float timeThreshold = 1000.0f; 
    private boolean livesChanged = false;
    private boolean scoreChanged = false;
    private int score = 0;
    private Clip enemySound; 
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
    

      

//MAPAS
//0 VACíO
//1 PARED
//2 MONEDA
//3 ENEMIGO
    
        private int[][] maze = {
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
{1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
{1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 1},
{1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
{1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 1},
{1, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 1, 0, 1},
{1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 1},
{1, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1},
{1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 1},
{1, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
{1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
{1, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1},
{1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1},
{1, 0, 2, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1},
{1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1},
{1, 0, 0, 1, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1},
{1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1},
{1, 0, 2, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 3, 0, 1},
{1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        };
        private int[][] maze2 = {
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
        private int[][] maze3 = {
{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
{1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1},
{1, 0, 1, 1, 1, 0, 1, 0, 3, 0, 0, 0, 1, 0, 1, 1, 1, 0, 1, 1},
{1, 0, 0, 2, 0, 0, 1, 0, 0, 0, 0, 3, 0, 0, 0, 2, 0, 0, 0, 1},
{1, 0, 0, 1, 1, 0, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 1},
{1, 0, 3, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 3, 0, 0, 1},
{1, 1, 1, 0, 1, 1, 1, 0, 0, 0, 3, 0, 1, 3, 1, 0, 0, 1, 1, 1},
{1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1},
{1, 0, 0, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 0, 1},
{1, 0, 0, 0, 1, 0, 0, 2, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1},
{1, 3, 0, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1},
{1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1},
{1, 0, 0, 1, 1, 0, 3, 0, 0, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1},
{1, 0, 0, 3, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 1},
{1, 1, 0, 0, 1, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 1, 1, 1, 1},
{1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 1, 0, 0, 1},
{1, 0, 1, 1, 1, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 1},
{1, 0, 0, 2, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 2, 1, 0, 0, 1},
{1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1},
{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},

        };
        //CERRAR AUDIO
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
    //TERMINA EL AUDIO DE CAMINAR
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

    private Clip lifeLostSound;

//MUSICA DE FONDO
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
//CREO LA CLASE DE LOS ENEMIGOS
class Enemy {
    float x, z;
    int texture;
    boolean isAlive; 
    Clip enemySound;
    public Enemy(float x, float z, int texture, Clip enemySound) {
        this.x = x;
        this.z = z;
        this.texture = texture;
        this.isAlive = true;
        this.enemySound = enemySound; 
    }
    //SE PUEDE MOVER
    public void moveTowards(float playerX, float playerZ, float speed) {
        float dx = playerX - this.x;
        float dz = playerZ - this.z;
        float distance = (float) Math.sqrt(dx * dx + dz * dz);

        if (distance > 0.01f) { 
            this.x += (dx / distance) * speed;
            this.z += (dz / distance) * speed;
        }
    }

    public float getAngleToPlayer(float playerX, float playerZ) {
        return (float) Math.toDegrees(Math.atan2(playerX - this.x, playerZ - this.z));
    }
}


//NUEVA CLASE PARA RESTAR VIDA POR EL ENEMIGO
class EnemyState {
    Enemy enemy;
    float timeNear = 0.0f; 
    public EnemyState(Enemy enemy) {
        this.enemy = enemy;
    }
}
private List<EnemyState> enemies = new ArrayList<>();
private void loadEnemySound(String filePath) {
    try {
        File soundFile = new File(filePath);
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
        enemySound = AudioSystem.getClip();
        enemySound.open(audioStream);
    } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
        e.printStackTrace();
    }
}
//CARGAR SONIDOS
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
    private void loadLifeLostSound(String filePath) {
    try {
        File soundFile = new File(filePath);
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
        lifeLostSound = AudioSystem.getClip();
        lifeLostSound.open(audioStream);
        System.out.println("Sonido de vida perdida cargado correctamente.");
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
            //LLAMO A LAS CARGAS
        loadAndPlayMusic("/home/santiago-larrosa/Descargas/My3dPlatform/src/Caminos-Oscuros.wav");
    loadAttackSound("/home/santiago-larrosa/Descargas/My3dPlatform/src/attack.wav");
    loadWalkSound("/home/santiago-larrosa/Descargas/My3dPlatform/src/Pasos.wav");
    loadCoinSound("/home/santiago-larrosa/Descargas/My3dPlatform/src/moneda1.wav");
    loadEnemySound("/home/santiago-larrosa/Descargas/My3dPlatform/src/monster.wav");
    loadLifeLostSound("/home/santiago-larrosa/Descargas/My3dPlatform/src/hit.wav");
  


            GLFWErrorCallback.createPrint(System.err).set();
            if (!glfwInit()) {
                throw new IllegalStateException("No GLFW");
            }
//VENTANA
        
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

    //SHADER
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


    float[] lightAmbient = {0.2f, 0.2f, 0.2f, 1.0f}; 
float[] lightDiffuse = {1.0f, 1.0f, 1.0f, 1.0f}; 
    glLightfv(GL_LIGHT0, GL_AMBIENT, lightAmbient);
    glLightfv(GL_LIGHT0, GL_DIFFUSE, lightDiffuse);


    glEnable(GL_COLOR_MATERIAL);
    glColorMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE);
//CARGA DE TEXTURAS
    playerTexture = loadTexture("/home/santiago-larrosa/Descargas/My3dPlatform/src/manos1.png");
    attackTexture = loadTexture("/home/santiago-larrosa/Descargas/My3dPlatform/src/manos2.png");
    wallTexture = loadTexture("/home/santiago-larrosa/Descargas/My3dPlatform/src/fondinho2.png");
    floorTexture = loadTexture("/home/santiago-larrosa/Descargas/My3dPlatform/src/fondinho3.png");
    coinTexture = loadTexture("/home/santiago-larrosa/Descargas/My3dPlatform/src/monedinha.png");
     enemyTexture = loadTexture("/home/santiago-larrosa/Descargas/My3dPlatform/src/enemy1.png");
     victoryTexture = loadTexture("/home/santiago-larrosa/Descargas/My3dPlatform/src/win.png");
         exitButtonTexture = loadTexture("/home/santiago-larrosa/Descargas/My3dPlatform/src/quit.png");
    




        }
        private int loadTexture(String path) {
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);

        
    
        
        
    ByteBuffer image = STBImage.stbi_load(path, width, height, channels, 4);


    
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

//MATRIZ DE RENDERIZADO 3D
    
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
        //LOOP PRINCIPAL DEL JUEGO

        private void loop() {
            
            glClearColor(1.0f, 0.0f, 0.0f, 0.3f);

            
            while (!glfwWindowShouldClose(window)) {
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
                handleInput();
                renderFloor();
                renderMaze();
                
               if (isWalking) {
                hudOscillationPhase += hudOscillationSpeed;
                }
                updateEnemies();
                renderEnemies(); 
                renderPlayerHUD();
                updatePlayer();
                glfwSwapBuffers(window);
                glfwPollEvents();
            }
        }
        //FUNCION DE ACTUALIZAR AL JUGADOR Y ESATDOS
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
    //CREAR A LOS TEXTOS EN EL HUD
   private int createTextTexture(String text) {

    BufferedImage bufferedImage = new BufferedImage(640, 64, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g = bufferedImage.createGraphics();
    
    g.setFont(new Font("Arial", Font.BOLD, 48));
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
//FUNCION DE PERDER VIDA
private void loseLife() {
    playerLives--;
    livesChanged = true; 
    System.out.println("¡Has perdido una vida! Vidas restantes: " + playerLives);
    

    
        lifeLostSound.setFramePosition(0); 
        lifeLostSound.start(); 
        System.out.println("Sonido de vida perdida reproducido.");
   

   
    if (playerLives <= 0) {
        System.out.println("¡Game Over! Reiniciando el juego...");
        resetGame(); 
    }
}
//RECOGER MONEDA
  private void checkCoinPickup() {
    int mazeX = (int) Math.floor(playerX + 0.5f);
    int mazeZ = (int) Math.floor(playerZ + 0.5f);

    if (score<300){
    if (maze[mazeZ][mazeX] == 2) {
        maze[mazeZ][mazeX] = 0;
        playCoinSound(); 
        score += 10; 
        scoreChanged = true; 
        System.out.println("Moneda recogida en (" + mazeX + ", " + mazeZ + ")");
    }}
    else if (300 < score &&  score < 600) {
        if(maze2[mazeZ][mazeX] == 2) {
        maze2[mazeZ][mazeX] = 0;
        playCoinSound(); 
        score += 10; 
        scoreChanged = true; 
        System.out.println("Moneda recogida en (" + mazeX + ", " + mazeZ + ")");
    }
    
    }
    else {
        if(maze3[mazeZ][mazeX] == 2) {
        maze3[mazeZ][mazeX] = 0;
        playCoinSound(); 
        score += 10; 
        scoreChanged = true; 
        System.out.println("Moneda recogida en (" + mazeX + ", " + mazeZ + ")");
    }
   if (score >= 300 && score < 600 && !teleportedTo300) {
        playerX = 1.5f; 
        playerZ = 1.5f; 
        teleportedTo300 = true; 
        System.out.println("¡Teletransportado a (1, 1) por alcanzar 300 puntos!");
    } else if (score >= 600 && !teleportedTo600) {
        playerX = 1.5f; 
        playerZ = 1.5f; 
        teleportedTo600 = true; 
        System.out.println("¡Teletransportado a (1, 1) por alcanzar 600 puntos!");
    }
}
//MANEJAR ENTRADAS
  }
 private void handleInput() {
    boolean isMoving = false; 

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

 
    if (glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS) {
        float newX = playerX + (float) Math.sin(Math.toRadians(playerAngle)) * moveSpeed;
        float newZ = playerZ - (float) Math.cos(Math.toRadians(playerAngle)) * moveSpeed;

        if (!checkCollision(newX, newZ)) {
            playerX = newX;
            playerZ = newZ;
            isMoving = true; 
        }
    }

    
    if (glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS) {
        float newX = playerX - (float) Math.sin(Math.toRadians(playerAngle)) * moveSpeed;
        float newZ = playerZ + (float) Math.cos(Math.toRadians(playerAngle)) * moveSpeed;

        if (!checkCollision(newX, newZ)) {
            playerX = newX;
            playerZ = newZ;
            isMoving = true; 
        }
    }

    isWalking = isMoving;

   
    playWalkSound();

    if (glfwGetKey(window, GLFW_KEY_X) == GLFW_PRESS && !isAttacking) {
        isAttacking = true;
        removeNearbyEnemies();
        attackStartTime = System.currentTimeMillis();
        playAttackSound();
    }

    if (glfwGetKey(window, GLFW_KEY_SPACE) == GLFW_PRESS && !isJumping) {
        isJumping = true;
        currentJumpSpeed = jumpSpeed;
    }
    if (score >= 900) {
        if (glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_LEFT) == GLFW_PRESS) {
            double[] xPos = new double[1];
            double[] yPos = new double[1];
            glfwGetCursorPos(window, xPos, yPos);
            
        
            if (xPos[0] >= 350 && xPos[0] <= 450 && yPos[0] >= 370 && yPos[0] <= 420) {
                System.out.println("¡Botón de salir presionado! Cerrando el juego...");
                glfwSetWindowShouldClose(window, true); 
            }
        }
    }
}

        
        private boolean checkCollision(float x, float z) {
            if (score<300){
            int mazeX = (int) Math.floor(x + 0.5f);
            int mazeZ = (int) Math.floor(z + 0.5f);

            
            if (mazeZ < 0 || mazeZ >= maze.length || mazeX < 0 || mazeX >= maze[0].length) {
                return true; 
            }

            return maze[mazeZ][mazeX] == 1;}
            else if (300 < score &&  score < 600){
                int mazeX = (int) Math.floor(x + 0.5f);
            int mazeZ = (int) Math.floor(z + 0.5f);

            
            if (mazeZ < 0 || mazeZ >= maze2.length || mazeX < 0 || mazeX >= maze2[0].length) {
                return true; 
            }

            return maze2[mazeZ][mazeX] == 1;
            }
            else {
                int mazeX = (int) Math.floor(x + 0.5f);
            int mazeZ = (int) Math.floor(z + 0.5f);

            
            if (mazeZ < 0 || mazeZ >= maze3.length || mazeX < 0 || mazeX >= maze3[0].length) {
                return true; 
            }

            return maze3[mazeZ][mazeX] == 1;
            }
        }
        //MATAR A LOS ENEMIGOS SI ESTAN CERCA
private void removeNearbyEnemies() {
    float thresholdDistance = 1.0f; 

    enemies.removeIf(enemyState -> { 
        Enemy enemy = enemyState.enemy; 
        float dx = playerX - enemy.x; 
        float dz = playerZ - enemy.z;
        
        float distance = (float) Math.sqrt(dx * dx + dz * dz);
        if (distance < thresholdDistance) {
            score += 100; 
            scoreChanged = true;
            enemy.isAlive = false;
            
            
            if (enemy.enemySound != null) {
                enemy.enemySound.stop(); 
                enemy.enemySound.close(); 
            }
        }
        return !enemy.isAlive; 
    });
}
        //CARGAR MAPA
    private void renderMaze() {
        glLoadIdentity();

       
        glTranslatef(0.0f, -playerY, 0.0f);

       
        glRotatef(playerPitch, 1.0f, 0.0f, 0.0f); 
        glRotatef(playerAngle, 0.0f, 1.0f, 0.0f);  
        glTranslatef(-playerX, -1.0f, -playerZ);    
 if (score < 300) {
        for (int z = 0; z < maze.length; z++) {
            for (int x = 0; x < maze[0].length; x++) {
                if (maze[z][x] == 3) {
                    Clip newEnemySound = null; 

                  
                    try {
                        newEnemySound = AudioSystem.getClip();
                        newEnemySound.open(AudioSystem.getAudioInputStream(new File("/home/santiago-larrosa/Descargas/My3dPlatform/src/monster.wav")));
                    } catch (LineUnavailableException e) {
                        System.out.println("No se puede acceder al dispositivo de audio: " + e.getMessage());
                        return; 
                    } catch (UnsupportedAudioFileException e) {
                        System.out.println("El archivo de audio no es compatible: " + e.getMessage());
                        return; 
                    } catch (IOException e) {
                        System.out.println("Error de entrada/salida al cargar el archivo de audio: " + e.getMessage());
                        return; 
                    }

                    
                    if (newEnemySound != null) {
                        Enemy newEnemy = new Enemy(x + 0.5f, z + 0.5f, enemyTexture, newEnemySound);
                        enemies.add(new EnemyState(newEnemy));
                        maze[z][x] = 0; 
                    }
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
        else if (300 < score &&  score < 600){
for (int z = 0; z < maze2.length; z++) {
            for (int x = 0; x < maze2[0].length; x++) {
                   if (maze2[z][x] == 3) {
    Clip newEnemySound = null;


    try {
        newEnemySound = AudioSystem.getClip();
        newEnemySound.open(AudioSystem.getAudioInputStream(new File("/home/santiago-larrosa/Descargas/My3dPlatform/src/monster.wav")));
    } catch (LineUnavailableException e) {
        System.out.println("No se puede acceder al dispositivo de audio: " + e.getMessage());
        e.printStackTrace();
    } catch (UnsupportedAudioFileException e) {
        System.out.println("El archivo de audio no es compatible: " + e.getMessage());
        e.printStackTrace();
    } catch (IOException e) {
        System.out.println("Error de entrada/salida al cargar el archivo de audio: " + e.getMessage());
        e.printStackTrace();
    }

    if (newEnemySound != null) {
        Enemy newEnemy = new Enemy(x + 0.5f, z + 0.5f, enemyTexture, newEnemySound);
        enemies.add(new EnemyState(newEnemy));
        maze2[z][x] = 0; 
    }
}
                renderFloorTile(x, z);
                if (maze2[z][x] == 1) {
                    renderWall(x, z);
                } else if (maze2[z][x] == 2) {
                    renderCoin(x, z);
                }
            }
        }
        }
        else {
            for (int z = 0; z < maze3.length; z++) {
            for (int x = 0; x < maze3[0].length; x++) {
                   if (maze3[z][x] == 3) {
    Clip newEnemySound = null; 

    try {
        newEnemySound = AudioSystem.getClip();
        newEnemySound.open(AudioSystem.getAudioInputStream(new File("/home/santiago-larrosa/Descargas/My3dPlatform/src/monster.wav")));
    } catch (LineUnavailableException e) {
        System.out.println("No se puede acceder al dispositivo de audio: " + e.getMessage());
        e.printStackTrace();
    } catch (UnsupportedAudioFileException e) {
        System.out.println("El archivo de audio no es compatible: " + e.getMessage());
        e.printStackTrace();
    } catch (IOException e) {
        System.out.println("Error de entrada/salida al cargar el archivo de audio: " + e.getMessage());
        e.printStackTrace();
    }

    if (newEnemySound != null) {
        Enemy newEnemy = new Enemy(x + 0.5f, z + 0.5f, enemyTexture, newEnemySound);
        enemies.add(new EnemyState(newEnemy));
        maze3[z][x] = 0; 
    }
}
                renderFloorTile(x, z);
                if (maze3[z][x] == 1) {
                    renderWall(x, z);
                } else if (maze3[z][x] == 2) {
                    renderCoin(x, z);
                }
            }
        }
        }
    }
    //RESETEAR VALORES AL MORIR
private void resetGame() {
    playerLives = 3; 
    score = 0; 
    playerY = 0.0f; 
    isJumping = false;
    currentJumpSpeed = 0.0f; 
    enemies.clear(); 

    if (score < 300) {
        findEmptyPosition(maze);
    } else if (score < 600) {
        findEmptyPosition(maze2);
    } else {
        findEmptyPosition(maze3);
    }
}

private void findEmptyPosition(int[][] maze) {
   
}
// MODIFICAR VALORES DE ENEMIGOS
private void updateEnemies() {
    List<EnemyState> enemiesToRemove = new ArrayList<>(); 
    float maxDistance = 5.0f; 
    float minVolume = 0.0f; 
    float maxVolume = 1.0f; 
    float enemySpeed = 0.02f;
    for (EnemyState enemyState : enemies) {
         
      Enemy enemy = enemyState.enemy;
        float dx = playerX - enemy.x;
        float dz = playerZ - enemy.z;
        float distance = (float) Math.sqrt(dx * dx + dz * dz);
        float volume = maxVolume - (distance / maxDistance) * maxVolume;
        volume = Math.max(minVolume, Math.min(volume, maxVolume));

       
        if (enemy.isAlive && enemy.enemySound != null) {
            if (!enemy.enemySound.isRunning()) {
                enemy.enemySound.setFramePosition(0); 
                enemy.enemySound.loop(Clip.LOOP_CONTINUOUSLY);
            }
           
            FloatControl volumeControl = (FloatControl) enemy.enemySound.getControl(FloatControl.Type.MASTER_GAIN);
            volumeControl.setValue(20f * (float) Math.log10(volume));
        }
        float newX = enemy.x;
        float newZ = enemy.z;

    

        
        if (distance < 1.0f) { 
            enemyState.timeNear += 16.67f; 
            if (enemyState.timeNear >= timeThreshold) {
                loseLife();
                livesChanged = true; 
                System.out.println("¡Has perdido una vida! Vidas restantes: " + playerLives);
                enemyState.timeNear = 0.0f; 

                
                if (playerLives <= 0) {
                    System.out.println("¡Game Over! Reiniciando el juego...");
                    resetGame(); 
                }
            }
        } else {
            enemyState.timeNear = 0.0f;
        }

        if (distance > 0.01f) { 
            newX += (dx / distance) * 0.03f; 
            newZ += (dz / distance) * 0.03f; 
        }

        if (!checkCollision(newX, newZ)) {
            enemy.x = newX;
            enemy.z = newZ;
        } else {
            
        }
    }

 
    enemies.removeAll(enemiesToRemove);
}
//RENDERIZAR EL HUD
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
if (score >= 900) {
        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, victoryTexture);
        
        
        float victoryWidth = 400; 
        float victoryHeight = 200; 
        float xPos = (750 - victoryWidth) / 2; 
        float yPos = ((600 - victoryHeight) / 4)-100; 
        
        glBegin(GL_QUADS);
        glTexCoord2f(0, 0); glVertex2f(xPos, yPos); 
        glTexCoord2f(1, 0); glVertex2f(xPos + victoryWidth, yPos); 
        glTexCoord2f(1, 1); glVertex2f(xPos + victoryWidth, yPos + victoryHeight); 
        glTexCoord2f(0, 1); glVertex2f(xPos, yPos + victoryHeight); 
        glEnd();
        
        
        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, exitButtonTexture);
        
        
        float buttonWidth = 100; 
        float buttonHeight = 50; 
        float buttonXPos = (750 - buttonWidth) / 2; 
        float buttonYPos = yPos + victoryHeight + 20; 

        
        glBegin(GL_QUADS);
        glTexCoord2f(0, 0); glVertex2f(buttonXPos, buttonYPos);
        glTexCoord2f(1, 0); glVertex2f(buttonXPos + buttonWidth, buttonYPos); 
        glTexCoord2f(1, 1); glVertex2f(buttonXPos + buttonWidth, buttonYPos + buttonHeight); 
        glTexCoord2f(0, 1); glVertex2f(buttonXPos, buttonYPos + buttonHeight); 
        glEnd();
        
        glDisable(GL_TEXTURE_2D);
    } else {
    
     String hudText = "Score: " + score + "            Lives: " + playerLives;
    if (textTexture == 0 || scoreChanged || livesChanged) { 
        textTexture = createTextTexture(hudText); 
        scoreChanged = false; 
        livesChanged = false; 
    }
    
    glBindTexture(GL_TEXTURE_2D, textTexture);
    
    
    float oscillationOffset = (float) Math.sin(hudOscillationPhase) * hudOscillationAmplitude;
    float scoreXPos = 10; 
    float scoreYPos = 20 + oscillationOffset; 
    float scoreWidth = 400; 
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
    float yPos = 110 + oscillationOffset; 
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
}
    glMatrixMode(GL_PROJECTION);
    glPopMatrix();
    
    glMatrixMode(GL_MODELVIEW);
    glPopMatrix();
}
private void renderExitButton() {
    glEnable(GL_TEXTURE_2D);
    glBindTexture(GL_TEXTURE_2D, exitButtonTexture);
    
    
    float buttonWidth = 100; 
    float buttonHeight = 50; 
    float buttonXPos = (750 - buttonWidth) / 2; 
    float buttonYPos = 500; 

    glBegin(GL_QUADS);
    glTexCoord2f(0, 0); glVertex2f(buttonXPos, buttonYPos); 
    glTexCoord2f(1, 0); glVertex2f(buttonXPos + buttonWidth, buttonYPos); 
    glTexCoord2f(1, 1); glVertex2f(buttonXPos + buttonWidth, buttonYPos + buttonHeight); 
    glTexCoord2f(0, 1); glVertex2f(buttonXPos, buttonYPos + buttonHeight); 
    glEnd();
    
    glDisable(GL_TEXTURE_2D);
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
    for (EnemyState enemyState : enemies) {
        Enemy enemy = enemyState.enemy;
        glPushMatrix();
        
        glTranslatef(enemy.x, 0.5f, enemy.z); 

        float angle = enemy.getAngleToPlayer(playerX, playerZ);
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
