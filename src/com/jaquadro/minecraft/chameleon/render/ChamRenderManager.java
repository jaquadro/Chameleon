package com.jaquadro.minecraft.chameleon.render;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.locks.ReentrantLock;

public class ChamRenderManager
{
    private final ReentrantLock lock;

    private WorldVertexBufferUploader vboUploader = new WorldVertexBufferUploader();
    private Stack<ChamRender> freeRenderers = new Stack<ChamRender>();
    private Map<WorldRenderer, ChamRender> renderers = new HashMap<WorldRenderer, ChamRender>();

    public ChamRenderManager () {
        lock = new ReentrantLock();
    }

    public ChamRender getRenderer (WorldRenderer worldRenderer) {
        lock.lock();

        try {
            if (renderers.containsKey(worldRenderer))
                return renderers.get(worldRenderer);

            if (freeRenderers.empty())
                freeRenderers.push(new ChamRender());

            ChamRender renderer = freeRenderers.pop();
            renderer.setWorldRenderer(worldRenderer);

            renderers.put(worldRenderer, renderer);

            return renderer;
        }
        finally {
            lock.unlock();
        }
    }

    public ChamRender startDrawing (WorldRenderer worldRenderer) {
        return startDrawing(worldRenderer, DefaultVertexFormats.BLOCK);
    }

    public ChamRender startDrawing (WorldRenderer worldRenderer, VertexFormat format) {
        ChamRender renderer = getRenderer(worldRenderer);
        try {
            worldRenderer.begin(GL11.GL_QUADS, format);
        }
        catch (IllegalStateException e) { }

        return renderer;
    }

    public void finishDrawing (ChamRender renderer) {
        lock.lock();

        try {
            WorldRenderer worldRenderer = renderer.getWorldRenderer();
            if (worldRenderer == null)
                return;

            try {
                worldRenderer.finishDrawing();
                this.vboUploader.draw(worldRenderer);
            } catch (IllegalStateException e) {
            }

            renderer.setWorldRenderer(null);
            renderers.remove(worldRenderer);
            freeRenderers.push(renderer);
        }
        finally {
            lock.unlock();
        }
    }
}
