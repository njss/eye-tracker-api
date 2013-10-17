package com.hazydesigns.capstone.worldWindGazeInput;

import com.hazydesigns.capstone.worldWindGazeInput.ui.GazeControlsLayer;
import com.hazydesigns.capstone.worldWindGazeInput.ui.GazeControlsSelectListener;
import gov.nasa.worldwind.Model;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.event.PositionEvent;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.AnnotationAttributes;
import gov.nasa.worldwind.render.ScreenAnnotation;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 * Container panel for the World Wind view.
 *
 * @author Mark Hazlewood
 *
 * @see JPanel
 * @see WorldWindow
 */
public class WorldWindPanel extends JPanel
{

   private final WorldWindow mWorldWindow;

   private ScreenAnnotation mCursorImage;
   private GazeControlsLayer mGazeControlsLayer;

   /**
    * Creates new form WorldWindPanel
    *
    * @param canvasSize
    */
   public WorldWindPanel(Dimension canvasSize)
   {
      super(new BorderLayout());

      mWorldWindow = createWorldWindow();
      ((Component) mWorldWindow).setPreferredSize(canvasSize);

      // Create the default model as described in the current worldwind properties.
      Model m = (Model) WorldWind.createConfigurationComponent(AVKey.MODEL_CLASS_NAME);
      mWorldWindow.setModel(m);
      add((Component) mWorldWindow, BorderLayout.CENTER);
      //setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
      hideCursor();
      
      RenderableLayer cursorLayer = new RenderableLayer();      

      AnnotationAttributes ca = new AnnotationAttributes();
      ca.setAdjustWidthToText(AVKey.SIZE_FIXED);
      ca.setInsets(new Insets(0, 0, 0, 0));
      ca.setBorderWidth(0);
      ca.setCornerRadius(0);
      ca.setSize(new Dimension(64, 64));
      ca.setBackgroundColor(new Color(0, 0, 0, 0));
      ca.setImageOpacity(1);
      ca.setScale(0.25);            
      
      mCursorImage = new ScreenAnnotation("", new Point(0, 0), ca);
      mCursorImage.getAttributes().setImageSource("images/cursorImage_big.png");
      mCursorImage.getAttributes().setSize(new Dimension(64, 64));
      cursorLayer.addRenderable(mCursorImage);      
      
      mWorldWindow.addPositionListener((PositionEvent arg0) ->
      {
         if (arg0.getScreenPoint() != null)
         {
            // These two points mysteriously have different origins, even though
            // their accessors have the same name ("screen point"). Thanks NASA.
            int x = arg0.getScreenPoint().x;
            int y = Math.abs(arg0.getScreenPoint().y - mWorldWindow.getView().getViewport().height);
            
            mCursorImage.setScreenPoint(new Point(x, y));
            mWorldWindow.redraw();
         }
      });
      
      mGazeControlsLayer = new GazeControlsLayer();
      mGazeControlsLayer.setName("GazeControlLayer");
      GazeControlsSelectListener controlSelectListener = new GazeControlsSelectListener(mWorldWindow, mGazeControlsLayer, mCursorImage);
      mWorldWindow.addSelectListener(controlSelectListener);
      mWorldWindow.getModel().getLayers().add(mGazeControlsLayer);
      
      mWorldWindow.getModel().getLayers().add(cursorLayer);
   }
   
   public GazeControlsLayer getGazeControlsLayer()
   {
       return mGazeControlsLayer;
   }
   
   private void hideCursor()
   {
      // Transparent 16 x 16 pixel cursor image.
      BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

      // Create a new blank cursor.
      Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
          cursorImg, new Point(0, 0), "blank cursor");

      // Set the blank cursor to this JPanel.
      setCursor(blankCursor);
   }

   /**
    *
    * @return
    */
   private WorldWindow createWorldWindow()
   {
      return new WorldWindowGLCanvas();
   }

   /**
    *
    * @return
    */
   public WorldWindow getWorldWindow()
   {
      return mWorldWindow;
   }

   /**
    * This method is called from within the constructor to initialize the form.
    * WARNING: Do NOT modify this code. The content of this method is always
    * regenerated by the Form Editor.
    */
   @SuppressWarnings("unchecked")
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents()
   {

      javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
      this.setLayout(layout);
      layout.setHorizontalGroup(
         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGap(0, 400, Short.MAX_VALUE)
      );
      layout.setVerticalGroup(
         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGap(0, 300, Short.MAX_VALUE)
      );
   }// </editor-fold>//GEN-END:initComponents


   // Variables declaration - do not modify//GEN-BEGIN:variables
   // End of variables declaration//GEN-END:variables
}
