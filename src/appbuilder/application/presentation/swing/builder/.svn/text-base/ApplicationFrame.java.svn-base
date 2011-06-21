package appbuilder.application.presentation.swing.builder;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

/**
 * @author Bruno Gama Catão
 */
public class ApplicationFrame extends JFrame implements ActionListener {
    public static final int DEFAULT_WIDTH  = 800;
    public static final int DEFAULT_HEIGHT = 600;
    
    private static final String APPLICATION_TITLE         = "application.title";
    private static final String EXIT_CONFIRMATION_MESSAGE = "application.exit.confirmation.msg";
    private static final String EXIT_CONFIRMATION_TITLE   = "application.exit.confirmation.title";
    
    private MDIDesktopPane desktopPane;
    private JLabel status;

    public ApplicationFrame() {
        super("MessageUtil.getMessage(APPLICATION_TITLE)");

        initComponents();
        setupMenu();
        setupLayout();

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exit();
            }
        });
        
        this.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    protected void initComponents() {
        desktopPane = new MDIDesktopPane();
        status = new JLabel("Status");
    }
    
    protected void setupMenu() {
        JMenuBar menuBar = new JMenuBar();
        
        JMenu file    = new JMenu("File");
        JMenu mantain = new JMenu("Forms");
        JMenu help    = new JMenu("Help");
        
        file.add(createMenuItem("Exit", "Exits application", "exit"));
        mantain.add(createMenuItem("Table 1", "Mantain Table 1", "mantainTable1"));
        help.add(createMenuItem("About", "About application", "about"));
        
        menuBar.add(file);
        menuBar.add(mantain);
        menuBar.add(new WindowMenu(desktopPane));
        menuBar.add(help);
        
        this.setJMenuBar(menuBar);
    }

    protected void setupLayout() {
        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusBar.add(status);
        statusBar.setBorder(BorderFactory.createLoweredBevelBorder());
        
        JPanel mainPane = new JPanel(new BorderLayout());
        
        mainPane.add(BorderLayout.CENTER, new JScrollPane(desktopPane));
        mainPane.add(BorderLayout.SOUTH, statusBar);
        
        this.setContentPane(mainPane);
    }
    
    //Begin of the action handling methods
    public void about() {
        JOptionPane.showMessageDialog(this, "Application Description", "Title", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void exit() {
        if (JOptionPane.showConfirmDialog(this, 
                "MessageUtil.getMessage(EXIT_CONFIRMATION_MESSAGE)", 
                "MessageUtil.getMessage(EXIT_CONFIRMATION_TITLE)", 
                JOptionPane.YES_NO_OPTION) ==
                JOptionPane.OK_OPTION) {
            System.exit(0);
        }
    }
    
    public void showMantainTable1IFrame() {
        MantainsTable1IFrame iframe = new MantainsTable1IFrame();
        iframe.pack();
        desktopPane.add(iframe);
        iframe.setVisible(true);
    }
    
    public void setStatus(String message) {
        status.setText("Status: " + message);
    }
    
    public void resetStatus() {
        status.setText("Status");
    }
    //End of the action handling methods
    
    //Main event handling method
    public void actionPerformed(ActionEvent ae) {
        if (ae.getActionCommand().equals("about")) {
            about();
        } else if (ae.getActionCommand().equals("exit")) {
            exit();
        } else if (ae.getActionCommand().equals("mantainTable1")) {
            showMantainTable1IFrame();
        }
    }
    
    //Begin of the factory methods
    protected JMenuItem createMenuItem(String label, String statusMsg, String command) {
        JMenuItem item = new JMenuItem(label);
        
        item.setActionCommand(command);
        item.addActionListener(this);
        item.addMouseListener(createStatusMouseListener(statusMsg));
        
        return item;
    }
    
    protected MouseListener createStatusMouseListener(final String statusMsg) {
        return new MouseAdapter(){
            @Override
            public void mouseEntered(MouseEvent me) {
                setStatus(statusMsg);
            }
            
            @Override
            public void mouseExited(MouseEvent me) {
                resetStatus();
            }
        };
    }
    //End of the factory methods
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ApplicationFrame frame = new ApplicationFrame();
                frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
}
