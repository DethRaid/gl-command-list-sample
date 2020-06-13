import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.NVCommandList.*;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Driver {
    private final long window;

    public static void main(String[] args) {
        Driver driver = new Driver();

        driver.loop();

        driver.terminate();
    }

    private Driver() {
        GLFWErrorCallback.createPrint(System.err).set();

        if(!glfwInit()) {
            throw new IllegalStateException("Could not initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 5);

        window = glfwCreateWindow(640, 480, "OpenGL command list demo", NULL, NULL);
        if(window == NULL) {
            throw new IllegalStateException("Could not create window");
        }

        glfwSetWindowPos(window, 50, 50);

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);

        glfwShowWindow(window);

        GL.createCapabilities();

        loadShaders();
    }

    private void loadShaders() {

    }

    private void loop() {
        while(!glfwWindowShouldClose(window)) {
            // do OpenGL things
            int commands = glCreateCommandListsNV();

            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

    private void terminate() {
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
}
