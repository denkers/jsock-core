//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.server.io;

/**
 * A synced server that allows pausing/resuming
 */
public abstract class SyncedServer extends Thread
{
    protected boolean isServing;
    protected boolean isStopped;
    
    public SyncedServer()
    {
        isServing   =   true;
        isStopped   =   false;
    }
    
    /**
     * Should the servers desired tasks here
     * Used after getting lock
     */
    protected abstract void runServerOperations();
    
    public boolean isStopped()
    {
        return isStopped;
    }
    
    public void stopServer()
    {
        isStopped   =   false;
    }
    
    public boolean isServing()
    {
        return isServing;
    }
    
    public void setServing(boolean serving)
    {
        this.isServing  =   serving;
    }
    
    public synchronized void setServingSync(boolean serving)
    {
        setServing(serving);
        notify();
    }
    
    protected synchronized void getServingLock() throws InterruptedException
    {
        if(!isServing())
            wait();
    }
    
    @Override
    public void run()
    {
        while(!isStopped())
        {
            try
            {   
                getServingLock();
                runServerOperations();
            }
            
            catch(InterruptedException e)
            {
                System.out.println("[SyncedServer@run]: " + e.getMessage());
            }
        }
    }
}
