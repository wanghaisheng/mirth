/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is Mirth.
 *
 * The Initial Developer of the Original Code is
 * WebReach, Inc.
 * Portions created by the Initial Developer are Copyright (C) 2006
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 *   Gerald Bortis <geraldb@webreachinc.com>
 *
 * ***** END LICENSE BLOCK ***** */

package com.webreach.mirth.client.ui.editors;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import com.webreach.mirth.client.ui.BeanBinder;
import com.webreach.mirth.client.ui.PlatformUI;
import com.webreach.mirth.client.ui.beans.EDIProperties;

/** Creates the error dialog. */
public class BoundPropertiesSheetDialog extends javax.swing.JDialog
{
    Object bean;
    Properties properties;
    
    /** Creates new form AboutMirth */
    public BoundPropertiesSheetDialog(Properties p, Object bean)
    {
        super(PlatformUI.MIRTH_FRAME);
        this.bean = bean;
        this.properties = p;
        setBeanInfo();
        initComponents();
        BeanBinder beanBinder = new BeanBinder(bean, propertySheetPanel, null);
        beanBinder.setWriteEnabled(true);
        this.setLocationRelativeTo(PlatformUI.MIRTH_FRAME);
        //propertySheetPanel.setToolBarVisible(false);
        setVisible(true);
    }
    
    public void setBeanInfo()
    {
        BeanInfo info = null;
        try
        {
            info = Introspector.getBeanInfo(bean.getClass());
        }
        catch (IntrospectionException ex)
        {
            ex.printStackTrace();
        }
        
        for (PropertyDescriptor pd : info.getPropertyDescriptors())
        {
            if (!pd.getName().equals("class"))
            {
                try
                {
                    String value;
                    if(properties.get(pd.getName()) != null)
                        value = (String) properties.get(pd.getName());
                    else
                        value = "";
                    
                    pd.getWriteMethod().invoke(bean, new Object[] {value});
                }
                catch (IllegalArgumentException ex)
                {
                    ex.printStackTrace();
                }
                catch (IllegalAccessException ex)
                {
                    ex.printStackTrace();
                }
                catch (InvocationTargetException ex)
                {
                    ex.printStackTrace();
                }
            }
        }
    }
    
    public void getBeanInfo()
    {
        BeanInfo info = null;
        try
        {
            info = Introspector.getBeanInfo(EDIProperties.class);
        }
        catch (IntrospectionException ex)
        {
            ex.printStackTrace();
        }
        
        for (PropertyDescriptor pd : info.getPropertyDescriptors())
        {
            if (!pd.getName().equals("class"))
            {
                try
                {
                    properties.put(pd.getName(),pd.getReadMethod().invoke(bean, new Object[] {}));
                }
                catch (IllegalArgumentException ex)
                {
                    ex.printStackTrace();
                }
                catch (IllegalAccessException ex)
                {
                    ex.printStackTrace();
                }
                catch (InvocationTargetException ex)
                {
                    ex.printStackTrace();
                }
            }
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents()
    {
        propertySheetPanel = new com.l2fprod.common.propertysheet.PropertySheetPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Properties");
        setAlwaysOnTop(true);
        setBackground(java.awt.Color.white);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setModal(true);
        addWindowListener(new java.awt.event.WindowAdapter()
        {
            public void windowClosed(java.awt.event.WindowEvent evt)
            {
                formWindowClosed(evt);
            }
        });

        propertySheetPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        propertySheetPanel.setAutoscrolls(true);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(propertySheetPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(propertySheetPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 283, Short.MAX_VALUE)
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void formWindowClosed(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosed
    {//GEN-HEADEREND:event_formWindowClosed
        getBeanInfo();
    }//GEN-LAST:event_formWindowClosed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.l2fprod.common.propertysheet.PropertySheetPanel propertySheetPanel;
    // End of variables declaration//GEN-END:variables
    
}
