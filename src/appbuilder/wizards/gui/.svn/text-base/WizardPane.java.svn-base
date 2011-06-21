package appbuilder.wizards.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.Toolkit;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import appbuilder.util.MessageUtil;

public class WizardPane extends JLayeredPane {
	private static final long serialVersionUID = -8168582013739663087L;
	
	private static final Color OVERLAY_BORDER_COLOR = new Color(22, 55, 122);
	private static final Color OVERLAY_COLOR        = new Color(255, 255, 255, 150);
	private static final Font OVERLAY_FONT          = new Font("Arial", Font.BOLD, 18);
	private static final String WAIT_IMAGE          = "wizard.data.wait.image";
	private static final int OVERLAY_OFFSET         = 10;
	private static final int OVERLAY_OFFSET_X       = 20;
	private static final int OVERLAY_OFFSET_Y       = 10;

	private JPanel content;
	private boolean showOverlay;
	private static Image waitImage;
	private String message;
	
	public WizardPane(JPanel content) {
		super();
		showOverlay = false;
		this.content = content;
		this.setLayout(new SimpleLayout());
		this.add(content, DEFAULT_LAYER);
		
		if (waitImage == null) {
			waitImage = Toolkit.getDefaultToolkit().createImage(
					MessageUtil.getMessage(WAIT_IMAGE));
			
			MediaTracker tracker = new MediaTracker(this);
			tracker.addImage(waitImage, 1);
			
			try {
				tracker.waitForAll();
			} catch (InterruptedException ex) {
				//Do nothing
			}
		}
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		//If is showing the overlay message
		if (showOverlay) {
			Graphics2D g2 = (Graphics2D)g; //Acquire the Graphics2D object
			
			//Enable antialiasing for geometric primitives and for text
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
					RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, 
					RenderingHints.VALUE_TEXT_ANTIALIAS_ON);			
			
			g.setColor(OVERLAY_COLOR);
			g.fillRect(0, 0, getBounds().width, getBounds().height);
			
			g.setFont(OVERLAY_FONT);
			
			int width = waitImage.getWidth(this) + OVERLAY_OFFSET + 
				g.getFontMetrics().stringWidth(message);
			int height = Math.max(g.getFontMetrics().getHeight(), 
				waitImage.getHeight(this));
			
			int totalWidth  = width + OVERLAY_OFFSET_X * 2;
			int totalHeight = height + OVERLAY_OFFSET_Y * 2;
			
			g.setColor(OVERLAY_BORDER_COLOR);
			g.fillRoundRect((getBounds().width - totalWidth) / 2, 
					(getBounds().height - totalHeight) / 2, 
					totalWidth, totalHeight, 20, 20);
			
			g.setColor(Color.WHITE);
			g.fillRoundRect(5 + (getBounds().width - totalWidth) / 2, 
					5 + (getBounds().height - totalHeight) / 2, 
					totalWidth - 10, totalHeight - 10, 20, 20);
			
			g.setColor(Color.BLACK);
			g.drawString(message, waitImage.getWidth(this) + OVERLAY_OFFSET + 
					(getBounds().width - width) / 2, 
					(getBounds().height + height / 2) / 2);
			g.drawImage(waitImage, (getBounds().width - width) / 2, 
					(getBounds().height - height) / 2, this);
		}
	}
	
	public void showOverlay(String message) {
		this.message = message;
                
                if (!showOverlay) {
                    showOverlay = true;

                    Runnable run = new Runnable() {
                            @Override
                            public void run() {
                                    while(showOverlay) {
                                            repaint();
                                            try {
                                                    Thread.sleep(5);
                                            } catch (InterruptedException ex) {
                                                    //Do nothing
                                            }
                                    }
                            }
                    };

                    new Thread(run).start();
                }
                
                repaint();
	}
	
	public void hideOverlay() {
		showOverlay = false;
		repaint();
	}
	
    // Layout *****************************************************************

    /**
     * Used to lay out the content layer in the icon feedback JLayeredPane.
     * The content fills the parent's space; minimum and preferred size of
     * this layout are requested from the content panel.
     */
    private final class SimpleLayout implements LayoutManager {

        /**
         * If the layout manager uses a per-component string,
         * adds the component <code>comp</code> to the layout,
         * associating it
         * with the string specified by <code>name</code>.
         *
         * @param name the string to be associated with the component
         * @param comp the component to be added
         */
        public void addLayoutComponent(String name, Component comp) {
            // components are well known by the container
        }

        /**
         * Removes the specified component from the layout.
         * @param comp the component to be removed
         */
        public void removeLayoutComponent(Component comp) {
            // components are well known by the container
        }

        /**
         * Calculates the preferred size dimensions for the specified
         * container, given the components it contains.
         *
         * @param parent the container to be laid out
         * @return the preferred size of the given container
         * @see #minimumLayoutSize(Container)
         */
        public Dimension preferredLayoutSize(Container parent) {
            return content.getPreferredSize();
        }

        /**
         * Calculates the minimum size dimensions for the specified
         * container, given the components it contains.
         *
         * @param parent the component to be laid out
         * @return the minimum size of the given container
         * @see #preferredLayoutSize(Container)
         */
        public Dimension minimumLayoutSize(Container parent) {
            return content.getMinimumSize();
        }

        /**
         * Lays out the specified container.
         *
         * @param parent the container to be laid out
         */
        public void layoutContainer(Container parent) {
            Dimension size = parent.getSize();
            content.setBounds(0, 0, size.width, size.height);
        }

    }
}
