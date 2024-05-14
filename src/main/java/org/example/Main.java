package org.example;
import org.lwjgl.*;
import org.lwjgl.Version;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Main {

    // The window handle
    private long window;

    public void run() {
        init();
        loop();

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {

        GLFWErrorCallback.createPrint(System.err).set();


        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        // Create the window
        window = glfwCreateWindow(840, 620, "2D Gam", NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");


        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
        });

        // Get the thread stack and push a new frame
        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);
    }

    private void loop() {
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();


        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.


        float dyx = 0; // Down Y Axis
        float uyx = 0; // Up Y Axis
        float rx = 0; // Right X axis
        float lx = 0; // Left X axis


        while (!glfwWindowShouldClose(window)) {

            glfwPollEvents();

            if (glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS) {
                uyx += 0.005f;
            }

            if (glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS) {
                dyx -= 0.005f;
            }

            if (glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS) {
                rx += 0.005f;
            }

            if (glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS) {
                lx -= 0.005f;
            }

            if(glfwGetKey(window, GLFW_KEY_F) == GL_TRUE) {System.out.print("Test");}

            // Clear the color buffer
            glClear(GL_COLOR_BUFFER_BIT);

            // Draw the square
            glBegin(GL_QUADS);
            glColor4f(1.0f, 0.0f, 0.0f, 0.0f);
            glVertex2f(-0.5f + rx + lx, 0.5f + uyx + dyx); ;


            glColor4f(0.0f, 1.0f, 0.0f, 0.0f);
            glVertex2f(0.5f + rx + lx, 0.5f + uyx + dyx);

            glColor4f(0.0f, 0.0f, 1.0f, 0.0f);
            glVertex2f(0.5f + rx + lx, -0.5f + uyx + dyx);

            glColor4f(1.0f, 1.0f, 1.0f, 0.0f);
            glVertex2f(-0.5f + rx + lx, -0.5f + uyx + dyx);
            glEnd();

            // Swap buffers
            glfwSwapBuffers(window);
        }
    }

    public static void main(String[] args) {
        new Main().run();
    }
}
