package com.goobercorp.gooberlib.util;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseInputListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Eyedropper {
    private static final Lazy<Robot> robot = new Lazy<>(Robot::new);
    private static final Lazy<Boolean> initializer = new Lazy<>(() -> {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();

        boolean translucencySupported = gd.isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.TRANSLUCENT);
        if (!translucencySupported) {
            GlobalScreen.registerNativeHook();

            GlobalClickListener globalClickListener = new GlobalClickListener();
            GlobalScreen.addNativeMouseListener(globalClickListener);
        }

        return translucencySupported;
    });

    private static CompletableFuture<Integer> pendingFuture = null;

    public static CompletableFuture<Integer> grab() {
        boolean translucencySupported = initializer.get();
        SwingUtilities.invokeLater(() -> {
            GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice graphicsDevice = graphicsEnvironment.getDefaultScreenDevice();

            if (translucencySupported) {
                JFrame frame = new JFrame("");
                frame.setUndecorated(true);
                frame.setBackground(new Color(0, 0, 0, 0));
                frame.setAlwaysOnTop(true);
                frame.setOpacity(0);

                frame.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        complete(e.getX(), e.getY());
                        graphicsDevice.setFullScreenWindow(null);
                        frame.dispose();
                    }
                });

                graphicsDevice.setFullScreenWindow(frame);
            } // else global click handler will handle the click and we get no custom cursor
        });

        pendingFuture = new CompletableFuture<>();
        return pendingFuture;
    }

    private static boolean active() {
        return pendingFuture != null;
    }

    static void main() throws ExecutionException, InterruptedException {
        System.out.println(grab().get());
    }

    private static void complete(int mx, int my) {
        Rectangle rect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage screenshot = robot.get().createScreenCapture(rect);

        pendingFuture.complete(screenshot.getRGB(mx, my));
    }

    private static class GlobalClickListener implements NativeMouseInputListener {
        @Override
        public void nativeMousePressed(NativeMouseEvent nativeEvent) {
            if (!active() || !initializer.get()) return;

            complete(nativeEvent.getX(), nativeEvent.getY());
        }
    }

}
