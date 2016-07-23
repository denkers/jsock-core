//========================================
//  Kyle Russell
//  AUT University 2016
//  github.com/denkers/jsock-core
//========================================

package com.kyleruss.jsockchat.client.io;

import com.kyleruss.jsockchat.client.core.ClientConfig;
import com.kyleruss.jsockchat.client.core.ClientManager;
import com.kyleruss.jsockchat.commons.updatebean.UpdateBeanDump;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * A update listening server
 * Listens for updates and passes them to ClientManager to handle
 */
public class ListUpdateListener extends Thread
{
    private final DatagramSocket socket;
    
    public ListUpdateListener(DatagramSocket socket)
    {
        this.socket =   socket;
    }
    
    /**
     * Receives a UpdateBeanDump and pushes it to ClientManager for handeling
     */
    @Override
    public void run()
    {
        try
        {
            while(socket != null && !socket.isClosed())
            {
                byte[] buffer               =   new byte[ClientConfig.UPDATE_BUFFER_SIZE];
                DatagramPacket packet       =   new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                ByteArrayInputStream bais   =   new ByteArrayInputStream(buffer);   
                ObjectInputStream ois       =   new ObjectInputStream(bais);
                UpdateBeanDump updates      =   (UpdateBeanDump) ois.readObject();
                
                ClientManager.getInstance().handleUpdates(updates);
            } 
        }
        
        catch(IOException | ClassNotFoundException e)
        {
            System.out.println("[ListUpdateListener@run STOP]: " + e.getMessage());
        }
    }
}
