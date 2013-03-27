/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL
 * license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */

package com.mirth.connect.connectors.dimse;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.dcm4che2.net.UserIdentity;
import org.dcm4che2.tool.dcmsnd.DcmSnd;

import com.mirth.connect.donkey.model.channel.ConnectorProperties;
import com.mirth.connect.donkey.model.message.ConnectorMessage;
import com.mirth.connect.donkey.model.message.Response;
import com.mirth.connect.donkey.model.message.Status;
import com.mirth.connect.donkey.server.DeployException;
import com.mirth.connect.donkey.server.StartException;
import com.mirth.connect.donkey.server.StopException;
import com.mirth.connect.donkey.server.UndeployException;
import com.mirth.connect.donkey.server.channel.DestinationConnector;
import com.mirth.connect.server.controllers.AlertController;
import com.mirth.connect.server.controllers.ControllerFactory;
import com.mirth.connect.server.controllers.MonitoringController;
import com.mirth.connect.server.controllers.MonitoringController.ConnectorType;
import com.mirth.connect.server.controllers.MonitoringController.Event;
import com.mirth.connect.server.util.AttachmentUtil;
import com.mirth.connect.server.util.TemplateValueReplacer;
import com.mirth.connect.util.ErrorConstants;
import com.mirth.connect.util.ErrorMessageBuilder;

public class DICOMDispatcher extends DestinationConnector {
    private Logger logger = Logger.getLogger(this.getClass());
    private DICOMDispatcherProperties connectorProperties;
    
    private MonitoringController monitoringController = ControllerFactory.getFactory().createMonitoringController();
    private AlertController alertController = ControllerFactory.getFactory().createAlertController();
    private TemplateValueReplacer replacer = new TemplateValueReplacer();
    private ConnectorType connectorType = ConnectorType.WRITER;

    @Override
    public void onDeploy() throws DeployException {
        this.connectorProperties = (DICOMDispatcherProperties) getConnectorProperties();
    }

    @Override
    public void onUndeploy() throws UndeployException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onStart() throws StartException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onStop() throws StopException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public ConnectorProperties getReplacedConnectorProperties(ConnectorMessage connectorMessage) {
        DICOMDispatcherProperties dicomDispatcherProperties = (DICOMDispatcherProperties) SerializationUtils.clone(connectorProperties);
        
        dicomDispatcherProperties.setTemplate(replacer.replaceValues(dicomDispatcherProperties.getTemplate(), connectorMessage));
        
        return dicomDispatcherProperties;
    }

    @Override
    public Response send(ConnectorProperties connectorProperties, ConnectorMessage connectorMessage) {        
        DICOMDispatcherProperties dicomDispatcherProperties = (DICOMDispatcherProperties) connectorProperties;
        
        String info = "Host: " + dicomDispatcherProperties.getHost();
        monitoringController.updateStatus(getChannelId(), getMetaDataId(), connectorType, Event.BUSY, info);
        
        String responseData = null;
        String responseError = null;
        String responseStatusMessage = null;
        Status responseStatus = Status.QUEUED;
        
        try {
            File tempFile = File.createTempFile("temp", "tmp");
            
            FileUtils.writeByteArrayToFile(tempFile, AttachmentUtil.reAttachMessage(dicomDispatcherProperties.getTemplate(), connectorMessage, null, true));
    
            DcmSnd dcmSnd = new DcmSnd();
            dcmSnd.setCalledAET("DCMRCV");
            dcmSnd.setRemoteHost(dicomDispatcherProperties.getHost());
            dcmSnd.setRemotePort(NumberUtils.toInt(dicomDispatcherProperties.getPort()));
    
            if ((dicomDispatcherProperties.getApplicationEntity() != null) && !dicomDispatcherProperties.getApplicationEntity().equals("")) {
                dcmSnd.setCalledAET(dicomDispatcherProperties.getApplicationEntity());
            }
    
            if ((dicomDispatcherProperties.getLocalApplicationEntity() != null) && !dicomDispatcherProperties.getLocalApplicationEntity().equals("")) {
                dcmSnd.setCalling(dicomDispatcherProperties.getLocalApplicationEntity());
            }
    
            if ((dicomDispatcherProperties.getLocalHost() != null) && !dicomDispatcherProperties.getLocalHost().equals("")) {
                dcmSnd.setLocalHost(dicomDispatcherProperties.getLocalHost());
                dcmSnd.setLocalPort(NumberUtils.toInt(dicomDispatcherProperties.getLocalPort()));
            }
    
            dcmSnd.addFile(tempFile);
    
            //TODO Allow variables
            int value = NumberUtils.toInt(dicomDispatcherProperties.getAcceptTo());
            if (value != 5)
                dcmSnd.setAcceptTimeout(value);
            
            value = NumberUtils.toInt(dicomDispatcherProperties.getAsync());
            if (value > 0)
                dcmSnd.setMaxOpsInvoked(value);
            
            value = NumberUtils.toInt(dicomDispatcherProperties.getBufSize());
            if (value != 1)
                dcmSnd.setTranscoderBufferSize(value);
            
            value = NumberUtils.toInt(dicomDispatcherProperties.getConnectTo());
            if (value > 0)
                dcmSnd.setConnectTimeout(value);
            if (dicomDispatcherProperties.getPriority().equals("med"))
                dcmSnd.setPriority(0);
            else if (dicomDispatcherProperties.getPriority().equals("low"))
                dcmSnd.setPriority(1);
            else if (dicomDispatcherProperties.getPriority().equals("high"))
                dcmSnd.setPriority(2);
            if (dicomDispatcherProperties.getUsername() != null && !dicomDispatcherProperties.getUsername().equals("")) {
                String username = dicomDispatcherProperties.getUsername();
                UserIdentity userId;
                if (dicomDispatcherProperties.getPasscode() != null && !dicomDispatcherProperties.getPasscode().equals("")) {
                    String passcode = dicomDispatcherProperties.getPasscode();
                    userId = new UserIdentity.UsernamePasscode(username, passcode.toCharArray());
                } else {
                    userId = new UserIdentity.Username(username);
                }
                userId.setPositiveResponseRequested(dicomDispatcherProperties.isUidnegrsp());
                dcmSnd.setUserIdentity(userId);
            }
            dcmSnd.setPackPDV(dicomDispatcherProperties.isPdv1());
            
            value = NumberUtils.toInt(dicomDispatcherProperties.getRcvpdulen());
            if (value != 16)
                dcmSnd.setMaxPDULengthReceive(value);
            
            value = NumberUtils.toInt(dicomDispatcherProperties.getReaper());
            if (value != 10)
                dcmSnd.setAssociationReaperPeriod(value);
            
            value = NumberUtils.toInt(dicomDispatcherProperties.getReleaseTo());
            if (value != 5)
                dcmSnd.setReleaseTimeout(value);
            
            value = NumberUtils.toInt(dicomDispatcherProperties.getRspTo());
            if (value != 60)
                dcmSnd.setDimseRspTimeout(value);
            
            value = NumberUtils.toInt(dicomDispatcherProperties.getShutdownDelay());
            if (value != 1000)
                dcmSnd.setShutdownDelay(value);
            
            value = NumberUtils.toInt(dicomDispatcherProperties.getSndpdulen());
            if (value != 16)
                dcmSnd.setMaxPDULengthSend(value);
            
            value = NumberUtils.toInt(dicomDispatcherProperties.getSoCloseDelay());
            if (value != 50)
                dcmSnd.setSocketCloseDelay(value);
            
            value = NumberUtils.toInt(dicomDispatcherProperties.getSorcvbuf());
            if (value > 0)
                dcmSnd.setReceiveBufferSize(value);
            
            value = NumberUtils.toInt(dicomDispatcherProperties.getSosndbuf());
            if (value > 0)
                dcmSnd.setSendBufferSize(value);
    
            dcmSnd.setStorageCommitment(dicomDispatcherProperties.isStgcmt());
            dcmSnd.setTcpNoDelay(!dicomDispatcherProperties.isTcpDelay());

            if (dicomDispatcherProperties.getTls() != null && !dicomDispatcherProperties.getTls().equals("notls")) {
                if (dicomDispatcherProperties.getTls().equals("without"))
                    dcmSnd.setTlsWithoutEncyrption();
                if (dicomDispatcherProperties.getTls().equals("3des"))
                    dcmSnd.setTls3DES_EDE_CBC();
                if (dicomDispatcherProperties.getTls().equals("aes"))
                    dcmSnd.setTlsAES_128_CBC();
                if (dicomDispatcherProperties.getTrustStore() != null && !dicomDispatcherProperties.getTrustStore().equals(""))
                    dcmSnd.setTrustStoreURL(dicomDispatcherProperties.getTrustStore());
                if (dicomDispatcherProperties.getTrustStorePW() != null && !dicomDispatcherProperties.getTrustStorePW().equals(""))
                    dcmSnd.setTrustStorePassword(dicomDispatcherProperties.getTrustStorePW());
                if (dicomDispatcherProperties.getKeyPW() != null && !dicomDispatcherProperties.getKeyPW().equals(""))
                    dcmSnd.setKeyPassword(dicomDispatcherProperties.getKeyPW());
                if (dicomDispatcherProperties.getKeyStore() != null && !dicomDispatcherProperties.getKeyStore().equals(""))
                    dcmSnd.setKeyStoreURL(dicomDispatcherProperties.getKeyStore());
                if (dicomDispatcherProperties.getKeyStorePW() != null && !dicomDispatcherProperties.getKeyStorePW().equals(""))
                    dcmSnd.setKeyStorePassword(dicomDispatcherProperties.getKeyStorePW());
                dcmSnd.setTlsNeedClientAuth(dicomDispatcherProperties.isNoClientAuth());
                if (!dicomDispatcherProperties.isNossl2())
                    dcmSnd.setTlsProtocol(new String[] {"TLSv1","SSLv3"});
                dcmSnd.initTLS();
            }
    
            dcmSnd.setOfferDefaultTransferSyntaxInSeparatePresentationContext(dicomDispatcherProperties.isTs1());
            dcmSnd.configureTransferCapability();
            dcmSnd.start();
        
            dcmSnd.open();
            dcmSnd.send();
            dcmSnd.close();
            
            dcmSnd.stop();
            tempFile.delete();
            
            responseStatusMessage = "DICOM message successfully sent";
            responseStatus = Status.SENT;
        } catch (Exception e) {
            responseStatusMessage = ErrorMessageBuilder.buildErrorResponse(e.getMessage(), e);
            responseError = ErrorMessageBuilder.buildErrorMessage(ErrorConstants.ERROR_415, e.getMessage(), null);
            alertController.sendAlerts(getChannelId(), ErrorConstants.ERROR_415, e.getMessage(), null);
        } finally {
            monitoringController.updateStatus(getChannelId(), getMetaDataId(), connectorType, Event.DONE);
        }
        
        return new Response(responseStatus, responseData, responseStatusMessage, responseError);
    }

}
