package engine.services.rendering;

import engine.EngineTestHarness;
import engine.services.resources.AssetCacheService;
import engine.services.window.WindowService;
import jakarta.inject.Inject;
import org.joml.Matrix4f;
import org.junit.jupiter.api.Test;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.lwjgl.opengl.GL11.*;

public class RenderingServiceIT extends EngineTestHarness {

    @Inject
    private RenderingService renderingService;
    @Inject
    private AssetCacheService assetCacheService;
    @Inject
    private WindowService windowService;
    @Inject
    private Camera camera;

    @Test
    void testDirectRenderSubmission() {
        // Given: A procedural 1x1 white texture created from raw pixel data
        ByteBuffer whitePixel = MemoryUtil.memAlloc(4);
        whitePixel.put((byte) 255).put((byte) 255).put((byte) 255).put((byte) 255).flip();
        Texture whiteTexture = new Texture(1, 1, whitePixel);

        // And: A standard quad mesh and transform
        Mesh quadMesh = assetCacheService.resolveMeshHandle("quad");
        Matrix4f transform = new Matrix4f().identity();

        // When: We directly submit a draw call to the rendering service
        renderingService.beginScene(camera);
        renderingService.submit(quadMesh, whiteTexture, transform);
        renderingService.endScene();

        // Then: The center pixel of the back buffer should be white
        var pixelColor = readPixelColorFromBackBuffer(
                windowService.getWidth() / 2,
                windowService.getHeight() / 2
        );

        // Finally: Swap buffers to display the rendered frame (for manual debugging)
        windowService.swapBuffers();

        // Clean up resources
        whiteTexture.close();
        MemoryUtil.memFree(whitePixel);

        assertEquals(255, pixelColor.r, "Red channel should be 255");
        assertEquals(255, pixelColor.g, "Green channel should be 255");
        assertEquals(255, pixelColor.b, "Blue channel should be 255");
    }

    private PixelColor readPixelColorFromBackBuffer(int x, int y) {
        // Ensure we are reading from the back buffer, where the scene was just drawn
        glReadBuffer(GL_BACK);
        ByteBuffer buffer = ByteBuffer.allocateDirect(4);
        glReadPixels(x, y, 1, 1, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
        int r = buffer.get(0) & 0xFF;
        int g = buffer.get(1) & 0xFF;
        int b = buffer.get(2) & 0xFF;
        return new PixelColor(r, g, b);
    }

    private record PixelColor(int r, int g, int b) {
    }
}
