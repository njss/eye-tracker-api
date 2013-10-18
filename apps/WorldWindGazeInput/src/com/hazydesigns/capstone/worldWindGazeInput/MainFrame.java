package com.hazydesigns.capstone.worldWindGazeInput;

import com.hazydesigns.capstone.worldWindGazeInput.ui.ConfigTestDialog;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.event.SelectListener;
import gov.nasa.worldwind.exception.WWAbsentRequirementException;
import gov.nasa.worldwind.util.WWUtil;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import rit.eyeTrackingAPI.DataConstructs.GazePoint;
import rit.eyeTrackingAPI.EyeTrackerUtilities.eyeTrackerClients.EyeTrackerClientSimulator;
import rit.eyeTrackingAPI.SmoothingFilters.PassthroughFilter;

/**
 *
 * @author mhazlewood
 */
public class MainFrame extends JFrame
{
   private final Dimension mCanvasSize = new Dimension(800, 600);
   private WorldWindPanel mMainViewPanel;
   
   // Eye tracker connection stuff
   private GazePoint mGazePoint;
   private PassthroughFilter mSmoothingFilter;
   private EyeTrackerClientSimulator mEyeTrackerClient;
   private EyeTrackerListener mEyeTrackerListener;
   
   // UI stuff
   private JButton mStartSimulationButton;
   private JButton mConfigureTestButton;
   private ConfigTestDialog mConfigTestDialog;
   
   private static final String TEST_FILE_PATH = System.getProperty("java.io.tmpdir") + "\\simulatedEyeData.txt";

   /**
    * Creates new form MainFrame
    */
   public MainFrame()
   {
      mSmoothingFilter = new PassthroughFilter();
      mGazePoint = new GazePoint(mSmoothingFilter);
      
      mEyeTrackerListener = new EyeTrackerListener(mSmoothingFilter, null, false, 0);
   }
   
   /**
    * 
    */
   private void initialize()
   {
      mMainViewPanel = new WorldWindPanel(mCanvasSize);
      
      mConfigTestDialog = new ConfigTestDialog(this, true);
      
      getContentPane().setLayout(new BorderLayout());
      getContentPane().add(mMainViewPanel, BorderLayout.CENTER);
      
      mStartSimulationButton = new JButton("Start Sim");
      mConfigureTestButton = new JButton("Configure Test");
      
      JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      topPanel.add(mStartSimulationButton);
      topPanel.add(mConfigureTestButton);
      getContentPane().add(topPanel, BorderLayout.NORTH);
      
      
      mStartSimulationButton.addActionListener((ActionEvent ae) ->
      {
         mEyeTrackerListener.start();
         
         mEyeTrackerClient = new EyeTrackerClientSimulator(mGazePoint, TEST_FILE_PATH, (short)0, false);
         ((EyeTrackerClientSimulator)mEyeTrackerClient).setJitter(10);
         mEyeTrackerClient.start();
      });
      
      mConfigureTestButton.addActionListener((ActionEvent e) -> 
      {
          mConfigTestDialog.setVisible(true, 
                                       mMainViewPanel.getGazeControlsLayer().getShowEdgePan(),
                                       mMainViewPanel.getGazeControlsLayer().getShowCenterPan(),
                                       mMainViewPanel.getGazeControlsLayer().getShowZoomIn(), 
                                       mMainViewPanel.getGazeControlsLayer().getShowZoomOut());
          
          mMainViewPanel.getGazeControlsLayer().setShowZoomInZoomOutPan(mConfigTestDialog.getShowZoomIn(), 
                                                                        mConfigTestDialog.getShowZoomOut(),
                                                                        mConfigTestDialog.getShowEdgePan(),
                                                                        mConfigTestDialog.getShowCenterPan());
      });

      // Register a rendering exception listener that's notified when exceptions occur during rendering.
      mMainViewPanel.getWorldWindow().addRenderingExceptionListener((Throwable t) ->
      {
         if (t instanceof WWAbsentRequirementException)
         {
            String message = "Computer does not meet minimum graphics requirements.\n";
            message += "Please install up-to-date graphics driver and try again.\n";
            message += "Reason: " + t.getMessage() + "\n";
            message += "This program will end when you press OK.";
            
            JOptionPane.showMessageDialog(MainFrame.this, message, "Unable to Start Program",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
         }
      });

      // Search the layer list for layers that are also select listeners and register them with the World
      // Window. This enables interactive layers to be included without specific knowledge of them here.
      /*for (Layer layer : mMainViewPanel.getWorldWindow().getModel().getLayers())
      {
         if (layer instanceof SelectListener)
         {
            mMainViewPanel.getWorldWindow().addSelectListener((SelectListener) layer);
         }
      }*/
      
      mMainViewPanel.getWorldWindow().getModel().getLayers().stream().filter((layer) -> (layer instanceof SelectListener)).forEach((layer) ->
      {
         mMainViewPanel.getWorldWindow().addSelectListener((SelectListener) layer);
      });
      
      this.pack();

      // Center the application on the screen.
      WWUtil.alignComponent(null, this, AVKey.CENTER);
      this.setResizable(true);
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

      setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

      javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
      getContentPane().setLayout(layout);
      layout.setHorizontalGroup(
         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGap(0, 400, Short.MAX_VALUE)
      );
      layout.setVerticalGroup(
         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGap(0, 300, Short.MAX_VALUE)
      );

      pack();
   }// </editor-fold>//GEN-END:initComponents

   /**
    * Application entry point.
    * 
    * @param args the command line arguments
    */
   public static void main(String args[])
   {
      /* Set the Nimbus look and feel */
      //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
       * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
       */
      try
      {
         for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
         {
            if ("Nimbus".equals(info.getName()))
            {
               javax.swing.UIManager.setLookAndFeel(info.getClassName());
               break;
            }
         }
      }
      catch (ClassNotFoundException ex)
      {
         java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
      }
      catch (InstantiationException ex)
      {
         java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
      }
      catch (IllegalAccessException ex)
      {
         java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
      }
      catch (javax.swing.UnsupportedLookAndFeelException ex)
      {
         java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
      }
      //</editor-fold>

      /* Create and display the form */
      java.awt.EventQueue.invokeLater(() ->
      {
         MainFrame main = new MainFrame();
         main.setTitle("World Wind Gaze Input");
         main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         
         main.initialize();
         main.setVisible(true);
      });
   }

   // Variables declaration - do not modify//GEN-BEGIN:variables
   // End of variables declaration//GEN-END:variables

   
}
