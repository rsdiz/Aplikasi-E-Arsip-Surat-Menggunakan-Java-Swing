/*
 * The MIT License
 *
 * Copyright 2020 Universitas Teknologi Yogyakarta.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package arsip.surat.util;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import javax.swing.event.MouseInputAdapter;

/**
 * Utilitas untuk mengaktifkan fitur drag pada component
 * 
 * @author Muhammad Rosyid Izzulkhaq (rsdiz)
 */
public class DragWindowUtil {
    
    public DragWindowUtil() {
    }
    
    public static void enable(Component component) {
        DragWindowListener listener = new DragWindowListener();
        component.addMouseListener(listener);
        component.addMouseMotionListener(listener);
    }
    
    private static class DragWindowListener extends MouseInputAdapter {
        
        private Point pointLocation;
        private MouseEvent mousesPressed;

        @Override
        public void mousePressed(MouseEvent mouseEvent) {
            mousesPressed = mouseEvent;
        }

        @Override
        public void mouseDragged(MouseEvent mouseEvent) {
            Component component = mouseEvent.getComponent();
            pointLocation = component.getLocation(pointLocation);
            int x = pointLocation.x - mousesPressed.getX() + mouseEvent.getX();
            int y = pointLocation.y - mousesPressed.getY() + mouseEvent.getY();
            component.setLocation(x, y);
        }
        
    }
    
}
