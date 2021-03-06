package net.machinemuse.general.gui.frame;

import net.machinemuse.general.geometry.Colour;
import net.machinemuse.general.geometry.DrawableMuseRect;
import net.machinemuse.general.geometry.MusePoint2D;
import net.machinemuse.utils.MuseMathUtils;
import net.machinemuse.utils.MuseRenderer;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class ScrollableFrame implements IGuiFrame {
    protected int totalsize;
    protected int currentscrollpixels;
    protected int buttonsize = 5;
    protected boolean scrollbarPicked = false;
    protected boolean scrolldownPicked = false;
    protected boolean scrollupPicked = false;
    protected int lastdWheel = Mouse.getDWheel();

    protected DrawableMuseRect border;

    public ScrollableFrame(MusePoint2D topleft, MusePoint2D bottomright,
                           Colour borderColour, Colour insideColour) {
        border = new DrawableMuseRect(topleft, bottomright, borderColour, insideColour);
    }

    protected double getScrollAmount() {
        return 8;
    }

    @Override
    public void update(double x, double y) {
        int dscroll = (lastdWheel - Mouse.getDWheel()) / 15;
        lastdWheel = Mouse.getDWheel();
        if (x > border.left() && x < border.right() && y > border.top() && y < border.bottom()) {
            if (Mouse.isButtonDown(0)) {
                if ((y - border.top()) < buttonsize && currentscrollpixels > 0) {
                    dscroll -= getScrollAmount();
                } else if ((border.bottom() - y) < buttonsize) {
                    dscroll += getScrollAmount();
                }
            }
            currentscrollpixels = (int) MuseMathUtils.clampDouble(currentscrollpixels + dscroll, 0, getMaxScrollPixels());
        } else {
            Mouse.getDWheel();
        }
    }

    @Override
    public void draw() {
        border.draw();
        MuseRenderer.texturelessOn();
        MuseRenderer.glowOn();
        GL11.glBegin(GL11.GL_TRIANGLES);
        Colour.LIGHTBLUE.doGL();
        // Can scroll down
        if (currentscrollpixels + border.height() < totalsize) {
            GL11.glVertex3d(border.left() + border.width() / 2, border.bottom(), 1);
            GL11.glVertex3d(border.left() + border.width() / 2 + 2, border.bottom() - 4, 1);
            GL11.glVertex3d(border.left() + border.width() / 2 - 2, border.bottom() - 4, 1);
        }
        // Can scroll up
        if (currentscrollpixels > 0) {
            GL11.glVertex3d(border.left() + border.width() / 2, border.top(), 1);
            GL11.glVertex3d(border.left() + border.width() / 2 - 2, border.top() + 4, 1);
            GL11.glVertex3d(border.left() + border.width() / 2 + 2, border.top() + 4, 1);
        }
        Colour.WHITE.doGL();
        GL11.glEnd();
        MuseRenderer.glowOff();
        MuseRenderer.texturelessOff();
    }

    @Override
    public void onMouseDown(double x, double y, int button) {
    }

    @Override
    public void onMouseUp(double x, double y, int button) {
    }

    public int getMaxScrollPixels() {
        return (int) Math.max(totalsize - border.height(), 0);
    }

    @Override
    public List<String> getToolTip(int x, int y) {
        // TODO Auto-generated method stub
        return null;
    }

}
