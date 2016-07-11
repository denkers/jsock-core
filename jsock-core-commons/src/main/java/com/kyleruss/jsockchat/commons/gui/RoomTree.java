//========================================
//  Kyle Russell
//  AUT University 2016
//  Distributed & Mobile Systems
//========================================

package com.kyleruss.jsockchat.commons.gui;

import com.kyleruss.jsockchat.commons.room.Room;
import com.kyleruss.jsockchat.commons.user.IUser;
import java.awt.Component;
import java.awt.Dimension;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;

public class RoomTree extends JTree
{
    protected DefaultMutableTreeNode rootNode;
    protected DefaultTreeModel treeModel;
    protected ImageIcon serverIcon, userIcon, roomIcon, lockedRoomIcon; 

    
    public RoomTree(String serverTitle)
    {
        rootNode    =   new DefaultMutableTreeNode(serverTitle);
        treeModel   =   new DefaultTreeModel(rootNode);
        
        setCellRenderer(new RoomTreeCellRenderer());
        setModel(treeModel);
    }
    
    
    public void initRooms(List<Room> rooms)
    {
        rootNode.removeAllChildren();
        for(Room room : rooms)
            addRoom(room);

        treeModel.reload(rootNode);
        expandRooms();
    }
    
    public void expandRooms()
    {
        for(int i = 0; i < getRowCount(); i++)
            expandRow(i);
    }
    
    public void setServerTitle(String title)
    {
        rootNode.setUserObject(title);
    }
    
    public void addRoom(Room room)
    {
        DefaultMutableTreeNode roomNode =   new DefaultMutableTreeNode(room);
        List<IUser> roomUsers           =   room.getUserList();
        for(IUser user : roomUsers)
            roomNode.add(new DefaultMutableTreeNode(user));
        
        rootNode.add(roomNode);
    }
    
    public void setRootNode(DefaultMutableTreeNode rootNode) 
    {
        this.rootNode = rootNode;
    }
    
    public DefaultMutableTreeNode getRootNode()
    {
        return rootNode;
    }

    public void setTreeModel(DefaultTreeModel treeModel) 
    {
        this.treeModel = treeModel;
    }

    public void setServerIcon(ImageIcon serverIcon)
    {
        this.serverIcon = serverIcon;
    }

    public void setUserIcon(ImageIcon userIcon)
    {
        this.userIcon = userIcon;
    }

    public void setRoomIcon(ImageIcon roomIcon) 
    {
        this.roomIcon = roomIcon;
    }

    public void setLockedRoomIcon(ImageIcon lockedRoomIcon)
    {
        this.lockedRoomIcon = lockedRoomIcon;
    }

    private class RoomTreeCellRenderer extends DefaultTreeCellRenderer
    {
        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
        {
            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            DefaultMutableTreeNode node =   (DefaultMutableTreeNode) value;
            
            if(node.isRoot() && serverIcon != null)
                setIcon(serverIcon);
            
            else
            {
                Object nodeObj  =   node.getUserObject();
                
                if(nodeObj instanceof Room)
                {
                    Room roomObj    =   (Room) nodeObj;
                    if(roomObj.isPassProtected() && lockedRoomIcon != null)
                        setIcon(lockedRoomIcon);
                    
                    else if(!roomObj.isPassProtected() && roomIcon != null)
                        setIcon(roomIcon);
                }
                
                else if(nodeObj instanceof IUser && userIcon != null)
                    setIcon(userIcon);
            }
            
            setPreferredSize(new Dimension(200, 15));
            return this;
        }
    }
}
