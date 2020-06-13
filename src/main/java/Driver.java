import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.NVCommandList.*;
import static org.lwjgl.opengl.GL46C.*;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Driver {
    private final long window;
    private int standardProgram;

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
        final String vertexShaderSource = "#version 450\n" +
                "" +
                "#extension GL_NV_command_list : enable\n" +
                "" +
                "layout(location = 0) in vec3 position_ia;" +
                "layout(location = 1) in vec2 texcoord_ia;" +
                "" +
                "layout(std140, binding=0) uniform scene_data {" +
                "    mat4 view_projection_matrix;" +
                "}" +
                "" +
                "layout(std140, binding=1) uniform object_data {" +
                "    mat4 model_matrix;" +
                "    sampler2D color_texture;" +
                "}" +
                "" +
                "layout(location = 0) out vec2 texcoord_vs;" +
                "" +
                "void main() {" +
                "    gl_Position = scene_data.view_projection_matrix * object_data.model_matrix * vec4(position_ia, 1);" +
                "    texvoord_vs = texcoord_ia;" +
                "}";

        final String fragmentShaderSource = "";

        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader, vertexShaderSource);
        glCompileShader(vertexShader);

        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader, fragmentShaderSource);
        glCompileShader(fragmentShader);

        standardProgram = glCreateProgram();

        glAttachShader(standardProgram, vertexShader);
        glAttachShader(standardProgram, fragmentShader);
        glLinkProgram(standardProgram);

        // Assume everything worked fine cause I'm the best
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
