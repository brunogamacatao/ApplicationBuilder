package appbuilder.application.presentation.swing.builder;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

/**
 * @author Bruno Gama Catão
 */
public class MantainsTable1IFrame extends JInternalFrame implements ActionListener {
    private Connection con;
    private JTextField field1;
    private JTextField field2;
    private JTable tableData;
    private MyTableModel tableModel;
    private Pessoa selectedData;
    
    public MantainsTable1IFrame() {
        super("Title", true, true, true, true);
        
        initComponents();
        setupLayout();
    }
    
    private void initComponents() {
        field1 = new JTextField();
        field2 = new JTextField();
        tableModel = new MyTableModel();
        tableData  = new JTable(tableModel);
        
        tableData.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (tableData.getSelectedRow() != -1) {
                    selectedData = tableModel.getValue(tableData.getSelectedRow());
                    field1.setText(selectedData.getNome());
                    field2.setText(selectedData.getTelefone());
                }
            }
        });
    }
    
    private void setupLayout() {
        String columns = "p,3dlu,300px";
        String rows = "p,3dlu,p,3dlu,p";
        
        FormLayout layout = new FormLayout(columns, rows);
        PanelBuilder builder = new PanelBuilder(layout);
        CellConstraints cc = new CellConstraints();

        builder.setDefaultDialogBorder();
        
        builder.addLabel("Field 1:", cc.xy(1, 1));
        builder.add(field1, cc.xy(3, 1));
        builder.addLabel("Field 2:", cc.xy(1, 3));
        builder.add(field2, cc.xy(3, 3));
        
        JPanel buttonPanel = ButtonBarFactory.buildCenteredBar(
                createButton("New", "new"),
                createButton("Save", "save"),
                createButton("Remove", "remove"));
        
        builder.add(buttonPanel, cc.xyw(1, 5, 3));
        
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(BorderLayout.CENTER, new JScrollPane(tableData));
        JSplitPane mainPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, builder.getPanel(), tablePanel);
        
        this.setContentPane(mainPanel);
    }
    
    private JButton createButton(String label, String action) {
        JButton button = new JButton(label);
        
        button.addActionListener(this);
        button.setActionCommand(action);
        
        return button;
    }
    
    public void actionPerformed(ActionEvent ae) {
        if (ae.getActionCommand().equals("new")) {
            newValue();
        } else if (ae.getActionCommand().equals("save")) {
            saveValue();
        } else if (ae.getActionCommand().equals("remove")) {
            removeValue();
        }
    }
    
    private void newValue() {
        field1.setText("");
        field2.setText("");
        field1.requestFocus();
        selectedData = null;
    }
    
    private void saveValue() {
        try {
            PreparedStatement pstmt = null;
            
            if (selectedData == null) {
                pstmt = con.prepareStatement("INSERT INTO PESSOA(NOME, TELEFONE) VALUES (?, ?)");
                pstmt.setString(1, field1.getText());
                pstmt.setString(2, field2.getText());
                
                newValue();
            } else {
                pstmt = con.prepareStatement("UPDATE PESSOA SET NOME = ?, TELEFONE = ? WHERE ID = ?");
                pstmt.setString(1, field1.getText());
                pstmt.setString(2, field2.getText());
                pstmt.setInt(3, selectedData.getId());
            }
            
            pstmt.executeUpdate();
            
            tableModel.invalidateCache();
            tableModel.fireTableDataChanged();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void removeValue() {
        if (selectedData != null) {
            try {
                PreparedStatement pstmt = con.prepareStatement("DELETE FROM PESSOA WHERE ID = ?");
                pstmt.setInt(1, selectedData.getId());
                pstmt.executeUpdate();

                tableModel.invalidateCache();
                tableModel.fireTableDataChanged();

                newValue();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private class MyTableModel extends AbstractTableModel {
        private boolean numberOfRowsCacheValid;
        private boolean tableDataCacheValid;
        private int numberOfRows;
        private List<Pessoa> tableData;
        
        public MyTableModel() {
            setupConnection();
            con = createConnection();
            numberOfRowsCacheValid = false;
            tableDataCacheValid = false;
        }
        
        public void invalidateCache() {
            numberOfRowsCacheValid = false;
            tableDataCacheValid = false;
        }
        
        public Pessoa getValue(int row) {
            if (!tableDataCacheValid) {
                try {
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT * FROM PESSOA ORDER BY NOME");

                    tableData = new ArrayList<Pessoa>();

                    while(rs.next()) {
                        tableData.add(new Pessoa(rs.getInt("ID"), rs.getString("NOME"), rs.getString("TELEFONE")));
                    }
                    
                    tableDataCacheValid = true;

                    rs.close();
                    stmt.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            return tableData.get(row);
        }
        
        private void setupConnection() {
            try {
                Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
                System.setProperty("derby.system.home", "C://Desenvolvimento/database/");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        
        private Connection createConnection() {
            try {
                return DriverManager.getConnection("jdbc:derby:demodb", "demodb", "demodb");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            
            return null;
        }
        
        public int getRowCount() {
            if (numberOfRowsCacheValid) {
                return numberOfRows;
            }
            
            try {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM PESSOA");
                
                if (rs.next()) {
                    numberOfRows = rs.getInt(1);

                    rs.close();
                    stmt.close();

                    numberOfRowsCacheValid = true;
                }
                
                return numberOfRows;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            
            return 0;
        }

        public int getColumnCount() {
            return 2;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            Pessoa pessoa = getValue(rowIndex);
            
            if (pessoa == null) {
                return null;
            }
            
            switch (columnIndex) {
                case 0:
                    return pessoa.getNome();
                case 1:
                    return pessoa.getTelefone();
            }
            
            return null;
        }
    }
    
    private static class Pessoa {
        private int id;
        private String nome;
        private String telefone;
        
        public Pessoa() {
            this(0, "", "");
        }
        
        public Pessoa(int id, String nome, String telefone) {
            this.id = id;
            this.nome = nome;
            this.telefone = telefone;
        }
        
        public int getId() {
            return id;
        }
        
        public void setId(int id) {
            this.id = id;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getTelefone() {
            return telefone;
        }

        public void setTelefone(String telefone) {
            this.telefone = telefone;
        }
    }
}