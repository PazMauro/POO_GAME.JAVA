import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;
import org.lwjgl.stb.STBImage;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import static org.lwjgl.opengl.GL12.*;  // Importar GL_CLAMP_TO_EDGE



import java.nio.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public class SimplePlatformerGame {

    private long window;

    // Variables de la cámara
    private float playerX = 1.5f, playerZ = 1.5f;  // Posición inicial en una celda libre
    private float playerAngle = 0.0f;               // Ángulo de la cámara (rotación)
    private float moveSpeed = 0.1f;                // Velocidad de movimiento
    private float turnSpeed = 1.5f;      
    private float playerPitch = 0.0f;  // Ángulo de inclinación de la cámara (rotación en el eje X)
           // Velocidad de giro
           private int playerTexture; // Declaración de la variable de textura del jugador


    // Laberinto básico
    private int[][] maze = {
    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
    {1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1},
    {1, 0, 0, 1, 0, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1},
    {1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1},
    {1, 0, 1, 1, 0, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1},
    {1, 0, 1, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 1, 0, 1},
    {1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 0, 1},
    {1, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1},
    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
    {1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1},
    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1},
    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1},
    {1, 0, 0, 1, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1},
    {1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1},
    {1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
    {1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
    };

    public void run() {
        System.out.println("holas");
        init();
        loop();

        // Limpiar recursos
        glfwDestroyWindow(window);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        // Inicializar GLFW
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit()) {
            throw new IllegalStateException("No GLFW");
        }

        // Configurar la ventana
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_DEPTH_BITS, 24);  // Habilitar el buffer de profundidad
        window = glfwCreateWindow(800, 600, "Juego 3D de DOOM KNIGHT", NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("No se pudo crear la ventana");
        }

        // Contexto OpenGL
        glfwMakeContextCurrent(window);
        glfwSwapInterval(1); // Sincronización vertical
        glfwShowWindow(window);

        // Crear capacidades de OpenGL
        GL.createCapabilities();

        // Configurar viewport
        glViewport(0, 0, 800, 600);

        // Configurar proyección en perspectiva
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        gluPerspective(60.0f, 800f / 600f, 0.1f, 100.0f); // Campo de visión de 60 grados
        glMatrixMode(GL_MODELVIEW);

        // Habilitar el buffer de profundidad
        glEnable(GL_BLEND);
glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);


        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LESS);
        // Habilitar iluminación y definir una fuente de luz
glEnable(GL_LIGHTING);
glEnable(GL_LIGHT0);

// Definir la posición de la luz
float[] lightPosition = {1.0f, 1.0f, 1.0f, 0.0f};  // Posición de la luz
glLightfv(GL_LIGHT0, GL_POSITION, lightPosition);

// Definir las propiedades de la luz
float[] lightAmbient = {0.2f, 0.2f, 0.2f, 1.0f};  // Luz ambiental
float[] lightDiffuse = {0.8f, 0.8f, 0.8f, 1.0f};  // Luz difusa
glLightfv(GL_LIGHT0, GL_AMBIENT, lightAmbient);
glLightfv(GL_LIGHT0, GL_DIFFUSE, lightDiffuse);

// Habilitar iluminación por material
glEnable(GL_COLOR_MATERIAL);
glColorMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE);

playerTexture = loadTexture("/home/santiago-larrosa/Descargas/My3dPlatform/src/HubFromDoom2.png");

    }
    private int loadTexture(String path) {
    IntBuffer width = BufferUtils.createIntBuffer(1);
    IntBuffer height = BufferUtils.createIntBuffer(1);
    IntBuffer channels = BufferUtils.createIntBuffer(1);

    // Cargar la imagen
   
    System.out.println("Directorio de trabajo actual: " + System.getProperty("user.dir"));
    
 ByteBuffer image = STBImage.stbi_load(path, width, height, channels, 4); // 4: forzar RGBA
    if (image == null) {
        String errorMessage = STBImage.stbi_failure_reason();
        throw new RuntimeException("Error al cargar imagen: " + path + ". Razón: " + errorMessage);
        
    }

    // Crear una nueva textura en OpenGL
    int textureID = glGenTextures();
    glBindTexture(GL_TEXTURE_2D, textureID);
   
    // Subir la imagen a la textura
    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
     
    // Configurar los parámetros de la textura (filtrado y repetición)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

    // Liberar la imagen de la memoria una vez que se ha subido a la GPU
    STBImage.stbi_image_free(image);

    return textureID;
}


    // Implementación de gluPerspective
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
        // Color de fondo claro para mejor visibilidad
        glClearColor(0.5f, 0.5f, 0.5f, 0.0f);

        while (!glfwWindowShouldClose(window)) {
            // Limpiar buffers
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            // Procesar entrada del usuario
            handleInput();

            // Renderizar el suelo
            renderFloor();

            // Renderizar el laberinto
            renderMaze();
            renderPlayerHUD();

            // Intercambiar buffers
            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

   private void handleInput() {
    // Rotación hacia la izquierda (flecha izquierda)
    if (glfwGetKey(window, GLFW_KEY_LEFT) == GLFW_PRESS) {
        playerAngle += turnSpeed;
    }

    // Rotación hacia la derecha (flecha derecha)
    if (glfwGetKey(window, GLFW_KEY_RIGHT) == GLFW_PRESS) {
        playerAngle -= turnSpeed;
    }

    // Rotación hacia arriba (flecha arriba)
    if (glfwGetKey(window, GLFW_KEY_UP) == GLFW_PRESS) {
        playerPitch -= turnSpeed;  // Disminuir el pitch para inclinar hacia arriba
        if (playerPitch < -89.0f) {  // Limitar la rotación para no exceder la vertical
            playerPitch = -89.0f;
        }
    }

    // Rotación hacia abajo (flecha abajo)
    if (glfwGetKey(window, GLFW_KEY_DOWN) == GLFW_PRESS) {
        playerPitch += turnSpeed;  // Aumentar el pitch para inclinar hacia abajo
        if (playerPitch > 89.0f) {  // Limitar la rotación para no exceder la vertical
            playerPitch = 89.0f;
        }
    }

    // Movimiento hacia adelante (tecla W)
    if (glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS) {
        float newX = playerX + (float) Math.sin(Math.toRadians(playerAngle)) * moveSpeed;
        float newZ = playerZ - (float) Math.cos(Math.toRadians(playerAngle)) * moveSpeed;

        if (!checkCollision(newX, newZ)) {
            playerX = newX;
            playerZ = newZ;
        }
    }

    // Movimiento hacia atrás (tecla S)
    if (glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS) {
        float newX = playerX - (float) Math.sin(Math.toRadians(playerAngle)) * moveSpeed;
        float newZ = playerZ + (float) Math.cos(Math.toRadians(playerAngle)) * moveSpeed;

        if (!checkCollision(newX, newZ)) {
            playerX = newX;
            playerZ = newZ;
        }
    }
}

    // Verifica si hay colisiones con las paredes del laberinto
    private boolean checkCollision(float x, float z) {
        int mazeX = (int) Math.floor(x + 0.5f);
        int mazeZ = (int) Math.floor(z + 0.5f);

        // Verificar límites del laberinto
        if (mazeZ < 0 || mazeZ >= maze.length || mazeX < 0 || mazeX >= maze[0].length) {
            return true; // Considerar fuera de los límites como colisión
        }

        return maze[mazeZ][mazeX] == 1;
    }

    // Renderiza el laberinto
   private void renderMaze() {
    glLoadIdentity();

    // Ajustar la cámara
    glRotatef(playerPitch, 1.0f, 0.0f, 0.0f);  // Rotar en el eje X (pitch)
    glRotatef(playerAngle, 0.0f, 1.0f, 0.0f);  // Rotar en el eje Y (yaw)
    glTranslatef(-playerX, -1.0f, -playerZ);    // Mover la cámara

    // Dibujar las paredes del laberinto
    for (int z = 0; z < maze.length; z++) {
        for (int x = 0; x < maze[0].length; x++) {
            if (maze[z][x] == 1) {
                renderWall(x, z);
            }
        }
    }
}
private void renderPlayerHUD() {
    glPushMatrix();
    
    // Desactivar la matriz de proyección 3D (ya que estamos dibujando en 2D)
    glMatrixMode(GL_PROJECTION);
    glPushMatrix();
    glLoadIdentity();
    glOrtho(0, 150, 600, 250, -1, 1); // Configurar una proyección ortográfica
  
    glMatrixMode(GL_MODELVIEW);
    glLoadIdentity();
    glEnable(GL_BLEND);
glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    // Dibujar la textura del jugador en la esquina inferior izquierda de la pantalla
    glEnable(GL_TEXTURE_2D);
    glBindTexture(GL_TEXTURE_2D, playerTexture);

    glBegin(GL_QUADS);
    // Definir las coordenadas del cuadrado (ajusta para colocar la imagen en la posición deseada)
    float xPos = 0;   // Ajusta para cambiar la posición horizontal
    float yPos = 500;  // Ajusta para cambiar la posición vertical
    float width = 150;  // Ajusta para cambiar el tamaño
    float height = 100; // Ajusta para cambiar el tamaño

    // Coordenadas de los vértices con las coordenadas de la textura
    glTexCoord2f(0, 0); glVertex2f(xPos, yPos);
    glTexCoord2f(1, 0); glVertex2f(xPos + width, yPos);
    glTexCoord2f(1, 1); glVertex2f(xPos + width, yPos + height);
    glTexCoord2f(0, 1); glVertex2f(xPos, yPos + height);

    glEnd();
    
    glDisable(GL_TEXTURE_2D);
    glDisable(GL_BLEND);
    // Restaurar la matriz de proyección
    glMatrixMode(GL_PROJECTION);
    glPopMatrix();
    
    glMatrixMode(GL_MODELVIEW);
    glPopMatrix();
}



    // Renderiza una pared en las coordenadas dadas
    private void renderWall(int x, int z) {
    glPushMatrix();
    glTranslatef(x, 0.0f, z);
    glBegin(GL_QUADS);
    
    // Normal para la pared delantera (hacia -Z)
    glNormal3f(0.0f, 0.0f, -1.0f);
    glColor3f(0.5f, 0.0f, 0.0f);
    glVertex3f(-0.5f, 0.0f, -0.5f);
    glVertex3f(0.5f, 0.0f, -0.5f);
    glVertex3f(0.5f, 2.0f, -0.5f);
    glVertex3f(-0.5f, 2.0f, -0.5f);

    // Normal para la pared trasera (hacia +Z)
    glNormal3f(0.0f, 0.0f, 1.0f);
    glColor3f(0.5f, 0.0f, 0.0f);
    glVertex3f(-0.5f, 0.0f, 0.5f);
    glVertex3f(0.5f, 0.0f, 0.5f);
    glVertex3f(0.5f, 2.0f, 0.5f);
    glVertex3f(-0.5f, 2.0f, 0.5f);

    // Normal para la pared izquierda (hacia -X)
    glNormal3f(-1.0f, 0.0f, 0.0f);
    glColor3f(0.5f, 0.0f, 0.0f);
    glVertex3f(-0.5f, 0.0f, -0.5f);
    glVertex3f(-0.5f, 0.0f, 0.5f);
    glVertex3f(-0.5f, 2.0f, 0.5f);
    glVertex3f(-0.5f, 2.0f, -0.5f);

    // Normal para la pared derecha (hacia +X)
    glNormal3f(1.0f, 0.0f, 0.0f);
    glColor3f(0.5f, 0.0f, 0.0f);
    glVertex3f(0.5f, 0.0f, -0.5f);
    glVertex3f(0.5f, 0.0f, 0.5f);
    glVertex3f(0.5f, 2.0f, 0.5f);
    glVertex3f(0.5f, 2.0f, -0.5f);

    glEnd();
    glPopMatrix();
}


    // Renderiza el suelo
    private void renderFloor() {
        glPushMatrix();
        glTranslatef(0.0f, -1.0f, 0.0f); // Colocar el suelo a la altura de la cámara
        glBegin(GL_QUADS);
        
        glColor3f(0.3f, 0.3f, 0.3f); // Color gris para el suelo

        // Suelo grande que cubre todo el laberinto
        glVertex3f(-4.0f, 0.0f, -4.0f);
        glVertex3f(4.0f, 0.0f, -4.0f);
        glVertex3f(4.0f, 0.0f, 4.0f);
        glVertex3f(-4.0f, 0.0f, 4.0f);

        glEnd();
        glPopMatrix();
    }

    public static void main(String[] args) {
        new SimplePlatformerGame().run();
    }
}
//javac -cp "lib/*" -d build src/SimplePlatformerGame.java
//java -cp "build:lib/*" -Djava.library.path=native/linux SimplePlatformerGame
