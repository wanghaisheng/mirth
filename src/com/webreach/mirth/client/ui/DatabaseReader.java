package com.webreach.mirth.client.ui;

import com.Ostermiller.Syntax.HighlightedDocument;
import java.util.Properties;

public class DatabaseReader extends ConnectorClass
{
    Frame parent;
    /** Creates new form DatabaseWriter */
    public DatabaseReader()
    {
        this.parent = PlatformUI.MIRTH_FRAME;
        name = "Database Reader";
        initComponents();
        HighlightedDocument mappingDoc = new HighlightedDocument();
        mappingDoc.setHighlightStyle(HighlightedDocument.SQL_STYLE);
        databaseSQLTextPane.setDocument(mappingDoc);
    }

    public Properties getProperties()
    {
        Properties properties = new Properties();
        properties.put("DataType", name);
        
        if(databaseDriverCombobox.getSelectedItem().equals("Sun JDBC-ODBC Bridge"))
            properties.put("Driver", "sun.jdbc.odbc.JdbcOdbcDriver");
        else if(databaseDriverCombobox.getSelectedItem().equals("ODBC - MySQL"))
            properties.put("Driver", "com.mysql.jdbc.Driver");
        else if(databaseDriverCombobox.getSelectedItem().equals("ODBC - PostgresSQL"))
            properties.put("Driver", "org.postgresql.Driver");
        else if(databaseDriverCombobox.getSelectedItem().equals("ODBC - SQL Server/Sybase"))
            properties.put("Driver", "net.sourceforge.jtds.jdbc.Driver");
        else if(databaseDriverCombobox.getSelectedItem().equals("ODBC - Oracle 10g Release 2"))
            properties.put("Driver", "oracle.jdbc.OracleDriver");
        
        properties.put("URL", databaseURLField.getText());
        properties.put("Username", databaseUsernameField.getText());
        properties.put("Password", new String(databasePasswordField.getPassword()));
        properties.put("SQLStatement", databaseSQLTextPane.getText());
        return properties;
    }

    public void setProperties(Properties props)
    {
        boolean visible = parent.channelEditTasks.getContentPane().getComponent(0).isVisible();
        
        if(props.get("Driver").equals("sun.jdbc.odbc.JdbcOdbcDriver"))
            databaseDriverCombobox.setSelectedItem("Sun JDBC-ODBC Bridge");
        else if(props.get("Driver").equals("com.mysql.jdbc.Driver"))
            databaseDriverCombobox.setSelectedItem("ODBC - MySQL");
        else if(props.get("Driver").equals("org.postgresql.Driver"))
            databaseDriverCombobox.setSelectedItem("ODBC - PostgresSQL");
        else if(props.get("Driver").equals("net.sourceforge.jtds.jdbc.Driver"))
            databaseDriverCombobox.setSelectedItem("ODBC - SQL Server/Sybase");
        else if(props.get("Driver").equals("oracle.jdbc.OracleDriver"))
            databaseDriverCombobox.setSelectedItem("ODBC - Oracle 10g Release 2");
        
        parent.channelEditTasks.getContentPane().getComponent(0).setVisible(visible);
        databaseURLField.setText((String)props.get("URL"));
        databaseUsernameField.setText((String)props.get("Username"));
        databasePasswordField.setText((String)props.get("Password"));
        databaseSQLTextPane.setText((String)props.get("SQLStatement"));
    }

    public void setDefaults()
    {
        databaseDriverCombobox.setSelectedItem(0);
        databaseURLField.setText("");
        databaseUsernameField.setText("");
        databasePasswordField.setText("");
        databaseSQLTextPane.setText("");
    }
    
    public Properties getDefaults()
    {
        Properties properties = new Properties();
        properties.put("DataType", name);
        properties.put("Driver", "sun.jdbc.odbc.JdbcOdbcDriver");
        properties.put("URL", "");
        properties.put("Username", "");
        properties.put("Password", "");
        properties.put("SQLStatement", "");
        return properties;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents()
    {
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        databaseDriverCombobox = new com.webreach.mirth.client.ui.MirthComboBox();
        databaseURLField = new com.webreach.mirth.client.ui.MirthTextField();
        databaseUsernameField = new com.webreach.mirth.client.ui.MirthTextField();
        databasePasswordField = new com.webreach.mirth.client.ui.MirthPasswordField();
        jScrollPane2 = new javax.swing.JScrollPane();
        databaseSQLTextPane = new com.webreach.mirth.client.ui.MirthTextPane();

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Database Reader", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 0, 0)));
        jLabel1.setText("Driver:");

        jLabel2.setText("Database URL:");

        jLabel3.setText("Username:");

        jLabel4.setText("Password:");

        jLabel5.setText("SQL Statement:");

        databaseDriverCombobox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Sun JDBC-ODBC Bridge", "ODBC - MySQL", "ODBC - PostgresSQL", "ODBC - SQL Server/Sybase", "ODBC - Oracle 10g Release 2" }));

        databasePasswordField.setFont(new java.awt.Font("Tahoma", 0, 11));

        jScrollPane2.setViewportView(databaseSQLTextPane);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jLabel2)
                    .add(jLabel1)
                    .add(jLabel5)
                    .add(jLabel3)
                    .add(jLabel4))
                .add(16, 16, 16)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(databaseDriverCombobox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, databaseURLField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, databaseUsernameField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, databasePasswordField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE))
                    .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 247, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(35, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(databaseDriverCombobox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel1))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel2)
                            .add(databaseURLField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel3)
                            .add(databaseUsernameField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(layout.createSequentialGroup()
                        .add(50, 50, 50)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel4)
                            .add(databasePasswordField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jLabel5)
                    .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 145, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.webreach.mirth.client.ui.MirthComboBox databaseDriverCombobox;
    private com.webreach.mirth.client.ui.MirthPasswordField databasePasswordField;
    private com.webreach.mirth.client.ui.MirthTextPane databaseSQLTextPane;
    private com.webreach.mirth.client.ui.MirthTextField databaseURLField;
    private com.webreach.mirth.client.ui.MirthTextField databaseUsernameField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables

}
