/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package db;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import javax.servlet.ServletException;
import support.GroupsForUser;
import support.PostsForGroup;
import support.Utils;

/**
 *
 * @author giorgio
 */
public class DbManager implements Serializable
{
    private transient Connection conn;
    
    public DbManager(String dbUrl) throws SQLException
    {
        try
        {
            Class.forName("org.apache.derby.jdbc.ClientDriver", true, getClass().getClassLoader());
        }
        catch (Exception e)
        {
            throw new RuntimeException(e.toString(), e);
        }
        
        Connection conn = DriverManager.getConnection(dbUrl);
        
        this.conn = conn;
    }
    
    public static void shutdown()
    {
        try
        {
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
        }
        catch(SQLException e)
        {
            Utils.toLog(DbManager.class.getName() + ": " + e.getMessage(), "e");
        }
    }
    
    public synchronized void acceptInvitations(Integer groupID, Integer userID, Boolean isAdmin) throws SQLException
    {
        PreparedStatement stm;
        stm = conn.prepareStatement
        (
            "INSERT INTO groupUser (groupID, userID, userIsAdmin) VALUES (?, ?, ?)"
        );
        
        stm.setInt(1, groupID);
        stm.setInt(2, userID);
        stm.setBoolean(3, isAdmin);
        
        try
        {
            Boolean rs = stm.execute();
            
        }
        finally
        {
            stm.close();
        }
        
        stm = conn.prepareStatement
        (
            "DELETE FROM invitations WHERE groupID = ? AND userID = ?"
        );
        
        stm.setInt(1, groupID);
        stm.setInt(2, userID);
        
        try
        {
            Boolean rs = stm.execute();
            
        }
        finally
        {
            stm.close();
        }
            
    }
    
    public synchronized User authenticate(String userName, String userPwd) throws SQLException
    {
        User user = null;
        PreparedStatement stm = conn.prepareStatement
        (
            "SELECT * FROM users WHERE userName = ? AND userPwd = ?"
        );
        
        try
        {
            stm.setString(1, userName);
            stm.setString(2, userPwd);
            
            ResultSet rs = stm.executeQuery();
            try
            {
                if(rs.next())
                {
                    user = new User();
                    
                    user.setUserID(rs.getInt("userId"));
                    user.setUserName(userName);
                    user.setUserAvatar(rs.getString("userAvatar"));
                    user.setUserPwd(rs.getString("userPwd"));
                }
            }
            finally
            {
                rs.close();
            }
        }
        finally
        {
            stm.close();
        }    
        
        return user;
    }
    
    public synchronized Integer createGroup(Integer userID, String groupName) throws SQLException
    {
        PreparedStatement stm;
        Integer maxGroupID = 0;
        
        stm = conn.prepareStatement("SELECT MAX(groupID) AS m FROM groups");
        try
        {
            ResultSet rs = stm.executeQuery();
            try
            {
                if (rs.next())
                {
                    maxGroupID = rs.getInt("m");
                }
            }
            finally
            {
                rs.close();
            }
        }
        finally
        {
            stm.close();
        }
        
        
        maxGroupID++;
        //---------------------------------------
        
        java.util.Date javaDate = Calendar.getInstance().getTime();
        java.sql.Date sqlDate = new java.sql.Date(javaDate.getTime());
        
        stm = conn.prepareStatement
        (
            "INSERT INTO groups (groupID, userID, groupName, groupDataCreation)" +
            " VALUES (?, ?, ?, ?)"
        );
        stm.setInt(1, maxGroupID);
        stm.setInt(2, userID);
        stm.setString(3, groupName);
        stm.setDate(4, sqlDate);
        
        try
        {
            Boolean rs = stm.execute();
        }
        finally
        {
            stm.close();
        }
        return maxGroupID;
    }
    
    public synchronized ArrayList<PostFile> getFilesForPost(Integer postID) throws SQLException
    {
        ArrayList<PostFile> postFiles = new ArrayList<PostFile>();
        
        PreparedStatement stm = conn.prepareStatement
        (
            "SELECT * FROM postFile WHERE postID = ?"
        );
        
        stm.setInt(1, postID);
        
        try
        {
            ResultSet rs = stm.executeQuery();
            try
            {
                while(rs.next())
                {
                    PostFile pf = new PostFile();
                    
                    pf.setPostFileID(rs.getInt("postFileID"));
                    pf.setPostID(postID);
                    pf.setPostFileOriginalFileName(rs.getString("postFileOriginalFileName"));
                    pf.setPostFileFileSystemName(rs.getString("postFileFileSystemName"));
                    pf.setPostFileType(rs.getString("postFileType"));
                    
                    postFiles.add(pf);
                }
            }
            finally
            {
                rs.close();
            }
        }
        finally
        {
            stm.close();
        }
        
        return postFiles;
    }
    
    public synchronized String getGroupName(Integer groupID) throws ServletException
    {
        String ret = "";
        try
        {
            PreparedStatement stm = conn.prepareStatement
            (
                "SELECT groupName FROM groups WHERE groupID = ?"
            );
        
            stm.setInt(1, groupID);
            try
            {
                ResultSet rs = stm.executeQuery();
                try
                {
                    if(rs.next())
                    {
                        ret = rs.getString("groupName");
                    }
                }
                finally
                {
                    rs.close();
                }
            }
            finally
            {
                stm.close();
            }
            
        }
        catch (SQLException e)
        {
            Utils.toLog(getClass().getName() + ": " + e.getMessage(), "e");
            throw new ServletException(e);
        }
        
        return ret;
    }
    
    public synchronized ArrayList<GroupsForUser> getGroupsForUser(Integer userID) throws SQLException
    {
        ArrayList<GroupsForUser> groups = new ArrayList<GroupsForUser>();
        PreparedStatement stm = conn.prepareStatement
        (
            "SELECT t1.groupID AS gID, t1.userID, t1.userIsAdmin AS uIA, t2.groupName AS gN, t2.groupDataCreation AS gDC" +
            " FROM groupUser AS t1 " +
            " JOIN groups AS t2 ON t1.groupID = t2.groupID" +
            " WHERE t1.userID = ?"
        );
        stm.setInt(1, userID);
        
        Utils.toLog(userID.toString(), "i");
        
        try
        {
            ResultSet rs = stm.executeQuery();
            try
            {
                while(rs.next())
                {
                    GroupsForUser group = new GroupsForUser();

                    group.setUserId(userID);
                    group.setGroupId(rs.getInt("gID"));
                    group.setGroupName(rs.getString("gN"));
                    group.setGroupDataCreation(rs.getDate("gDC"));
                    group.setUserIsAdmin(rs.getBoolean("uIA"));

                    groups.add(group);
                }
                    
            }
            finally
            {
                rs.close();
            }
        }
        finally
        {
            stm.close();
        }
        
        return groups;
    }
    
    public synchronized ArrayList<Invitation> getInvitationsForUser(Integer userID) throws SQLException
    {
        ArrayList<Invitation> invitations = new ArrayList<Invitation>();
        PreparedStatement stm = conn.prepareStatement
        (
            "SELECT * FROM invitation WHERE userID = ?"
        );
        
        stm.setInt(1, userID);
        try
        {
            ResultSet rs = stm.executeQuery();
            try
            {
                while(rs.next())
                {
                    Invitation invitation = new Invitation();
                    
                    invitation.setGroupID(rs.getInt("groupID"));
                    invitation.setUserID(rs.getInt("userID"));
                    invitations.add(invitation);
                }
            }
            finally
            {
                rs.close();
            }
        }
        finally
        {
            stm.close();
        }
        
        return invitations;
    }
    
    public synchronized String getLastestPostDate(Integer groupID) throws SQLException
    {
        String ret = "Non ci sono post!";
        
     
        PreparedStatement stm = conn.prepareStatement
        (
            "SELECT MAX(postDataCreation) AS m FROM post WHERE groupID = ?"
        );

        stm.setInt(1, groupID);
        try
        {
            ResultSet rs = stm.executeQuery();
            try
            {
                if (rs.next())
                {
                    Timestamp ts = rs.getTimestamp("m");
                    if (ts != null)
                        ret = Utils.timeStampToString(ts);
                }
            }
            finally
            {
                rs.close();
            }
        }
        finally
        {
            stm.close();
        }
        return ret;
    }
    
    public synchronized ArrayList<PostsForGroup> getPostsForGroup(Integer groupID) throws SQLException
    {
        ArrayList<PostsForGroup> postsFG = new ArrayList<PostsForGroup>();
        PreparedStatement stm;
        stm = conn.prepareStatement
        (
            "SELECT t1.postID AS pID, t1.groupID AS gID, t1.userID AS uID, t1.postDataCreation AS pDC, t1.postText as pT, " + 
            "       t2.userName AS uN, t2.userAvatar AS uA " +
            "   FROM post AS t1" +
            "   JOIN users AS t2 ON t1.userID = t2.userID " +
            "   WHERE t1.groupID = ? " + 
            "   ORDER BY pDC DESC"
        );
        stm.setInt(1, groupID);
        
        try
        {
            ResultSet rs = stm.executeQuery();
            try
            {
                while(rs.next())
                {
                    PostsForGroup p = new PostsForGroup();
        
                    p.setPostID(rs.getInt("pID"));
                    p.setGroupID(rs.getInt("gID"));
                    p.setUserID(rs.getInt("uID"));
                    p.setPostDataCreation(rs.getTimestamp("pDC"));
                    p.setPostText(rs.getString("pT"));
                    p.setUserName(rs.getString("uN"));
                    p.setUserAvatar(rs.getString("uA"));
                    
                    postsFG.add(p);
                }
            }
            finally
            {
                
            }
        }
        finally
        {
            stm.close();
        }
        
        return postsFG;
    }
    
    public synchronized User getUserFromUserName(String userName) throws SQLException
    {
        User user = new User();
        PreparedStatement stm;
        stm = conn.prepareStatement
        (
            "SELECT * FROM users WHERE userName = ?"
        );
        stm.setString(1, userName);
        
        try
        {
            ResultSet rs = stm.executeQuery();
            try
            {
                while(rs.next())
                {
                    user.setUserID(rs.getInt("userID"));
                    user.setUserName(rs.getString("userName"));
                    user.setUserPwd(rs.getString("userPwd"));
                    user.setUserAvatar(rs.getString("userAvatar"));
                  }
            }
            finally
            {
                rs.close();
            }
        }
        finally
        {
            stm.close();
        }
        
        return user;
    }
    
    public synchronized String getUserName(Integer userID) throws SQLException
    {
        String ret = "";
        PreparedStatement stm = conn.prepareStatement
        (
            "SELECT userName FROM users WHERE userID = ?"
        );
        stm.setInt(1, userID);
        
        try
        {
            ResultSet rs = stm.executeQuery();
            try
            {
                if (rs.next())
                {
                    ret = rs.getString("userName");
                }
            }
            finally
            {
                rs.close();
            }
        }
        finally
        {
            stm.close();
        }
        
        return ret;
    }
    
    public synchronized ArrayList<User> getUsers(Integer userID) throws SQLException
    {
        ArrayList<User> users = new ArrayList<User>();
        PreparedStatement stm;
        stm = conn.prepareStatement
        (
            "SELECT * FROM users WHERE userID <> ?"
        );
        stm.setInt(1, userID);
        
        try
        {
            ResultSet rs = stm.executeQuery();
            try
            {
                while(rs.next())
                {
                    User user = new User();
                    
                    user.setUserID(rs.getInt("userID"));
                    user.setUserName(rs.getString("userName"));
                    user.setUserPwd(rs.getString("userPwd"));
                    user.setUserAvatar(rs.getString("userAvatar"));
                    
                    users.add(user);
                }
            }
            finally
            {
                rs.close();
            }
        }
        finally
        {
            stm.close();
        }
        
        return users;
    }
    
    public synchronized ArrayList<User> getUsersForModify(Integer userID) throws SQLException
    {
        ArrayList<User> users = new ArrayList<User>();
        PreparedStatement stm;
        stm = conn.prepareStatement
        (
            "SELECT * FROM users WHERE userID <> ?"
        );
        stm.setInt(1, userID);
        
        try
        {
            ResultSet rs = stm.executeQuery();
            try
            {
                while(rs.next())
                {
                    User user = new User();
                    
                    user.setUserID(rs.getInt("userID"));
                    user.setUserName(rs.getString("userName"));
                    user.setUserPwd(rs.getString("userPwd"));
                    user.setUserAvatar(rs.getString("userAvatar"));
                    
                    users.add(user);
                }
            }
            finally
            {
                rs.close();
            }
        }
        finally
        {
            stm.close();
        }
        
        return users;
    }
    
    public synchronized void insertIvites(Integer groupID, ArrayList<User> users) throws SQLException
    {
        PreparedStatement stm = conn.prepareStatement
        (
            "INSERT INTO invitation (groupID, userID) values (?, ?)"
        );
        
        try
        {
            Iterator<User> i = users.iterator();
            while(i.hasNext())
            {
                User u = i.next();
                stm.setInt(1, groupID);
                stm.setInt(2, u.getUserID());
                stm.execute();
            }
        }
        finally
        {
            stm.close();
        }
    }
    
    public synchronized Integer insertPost(Integer groupID, Integer userID, String postText) throws SQLException
    {
        PreparedStatement stm;
        Integer maxPostID = 0;
        
        stm = conn.prepareStatement("SELECT MAX(postID) AS m FROM post");
        try
        {
            ResultSet rs = stm.executeQuery();
            try
            {
                if (rs.next())
                {
                    maxPostID = rs.getInt("m");
                }
            }
            finally
            {
                rs.close();
            }
        }
        finally
        {
            stm.close();
        }
        
        
        maxPostID++;
        //---------------------------------------
        
        java.util.Date javaDate = new java.util.Date();
        java.sql.Timestamp sqlTimestamp = new java.sql.Timestamp(javaDate.getTime());
        
        stm = conn.prepareStatement
        (
            "INSERT INTO post (postID, groupID, userID, postDataCreation, postText)" +
            " VALUES (?, ?, ?, ?, ?)"
        );
        stm.setInt(1, maxPostID);
        stm.setInt(2, groupID);
        stm.setInt(3, userID);
        stm.setTimestamp(4, sqlTimestamp);
        stm.setString(5, postText);
        
        try
        {
            Boolean rs = stm.execute();
        }
        finally
        {
            stm.close();
        }
        return maxPostID;
    }
    
    public synchronized Integer insertPostFiles(ArrayList<PostFile> postFiles) throws SQLException
    {
        PreparedStatement stm;
        Integer maxPostFileID = 0;
        
        Iterator<PostFile> i = postFiles.iterator();
        
        while(i.hasNext())
        {
            PostFile p = i.next();
            
            stm = conn.prepareStatement("SELECT MAX(postFileID) AS m FROM postFile");
            try
            {
                ResultSet rs = stm.executeQuery();
                try
                {
                    if (rs.next())
                    {
                        maxPostFileID = rs.getInt("m");
                    }
                }
                finally
                {
                    rs.close();
                }
            }
            finally
            {
                stm.close();
            }


            maxPostFileID++;
            //---------------------------------------

            java.util.Date javaDate = new java.util.Date();
            java.sql.Timestamp sqlTimestamp = new java.sql.Timestamp(javaDate.getTime());

            stm = conn.prepareStatement
            (
                "INSERT INTO postFile (postFileID, postID, postFileOriginalFileName, postFileFileSystemName, postFileType)" +
                " VALUES (?, ?, ?, ?, ?)"
            );
            stm.setInt(1, maxPostFileID);
            stm.setInt(2, p.getPostID());
            stm.setString(3, p.getPostFileOriginalFileName());
            stm.setString(4, p.getPostFileFileSystemName());
            stm.setString(5, p.getPostFileType());

            try
            {
                Boolean rs = stm.execute();
            }
            finally
            {
                stm.close();
            }
        }
        return maxPostFileID;
    }
    
    public synchronized void insertUserInGroup (Integer groupID, Integer userID) throws SQLException
    {
        PreparedStatement stm;
        stm = conn.prepareStatement
        (
            "INSERT INTO groupUser (groupID, userID, userIsAdmin) VALUES (?, ?, ?)"
        );
        
        stm.setInt(1, groupID);
        stm.setInt(2, userID);
        stm.setBoolean(3, true);
        
        try
        {
            Boolean rs = stm.execute();
            
        }
        finally
        {
            stm.close();
        }
    }
    
    public synchronized Boolean isUserInGroup(Integer userID) throws ServletException
    {
        Boolean ret = false;
        
        try
        {
            PreparedStatement stm;
            stm = conn.prepareStatement
            (
                "SELECT * FROM groupUser WHERE userID = ?"
            );
            stm.setInt(1, userID);
            
            try
            {
                ResultSet rs = stm.executeQuery();
                try
                {
                    if (rs.next())
                    {
                        ret = true;
                    }
                }
                finally
                {
                    rs.close();
                }
            }
            finally
            {
                stm.close();
            }
        }
        catch(SQLException e)
        {
            Utils.toLog(this.getClass().getName() + ": " + e.getMessage(), "e");
            throw new ServletException(e);
        }
        
        return ret;
    }
    public synchronized Boolean isUserAdmin(Integer userID, Integer groupID) throws ServletException
    {
        Boolean ret = false;
        
        try
        {
            PreparedStatement stm;
            stm = conn.prepareStatement
            (
                "SELECT userIsAdmin FROM groupUser WHERE userID = ? AND groupID = ?"
            );
            stm.setInt(1, userID);
            stm.setInt(2, groupID);
            
            try
            {
                ResultSet rs = stm.executeQuery();
                try
                {
                    if (rs.next())
                    {
                        ret = rs.getBoolean("userIsAdmin");
                    }
                }
                finally
                {
                    rs.close();
                }
            }
            finally
            {
                stm.close();
            }
        }
        catch(SQLException e)
        {
            Utils.toLog(this.getClass().getName() + ": " + e.getMessage(), "e");
            throw new ServletException(e);
        }
        
        return ret;
    }
    
    public synchronized void modfyGroupName(Integer groupID, String groupName) throws SQLException
    {
        PreparedStatement stm;
        stm = conn.prepareStatement
        (
            "UPDATE groups SET groupName = ? WHERE groupID = ?"
        );
        
        stm.setString(1, groupName);
        stm.setInt(2, groupID);
        try
        {
            Boolean ret = stm.execute();
            
        }
        finally
        {
            stm.close();
        }
    }
    
    public synchronized Boolean userHasInvitations(Integer userID) throws ServletException
    {
        Boolean ret = false;
        
        try
        {
            PreparedStatement stm;
            stm = conn.prepareStatement
            (
                "SELECT * FROM invitation WHERE userID = ?"
            );

            stm.setInt(1, userID);
            try
            {
                ResultSet rs = stm.executeQuery();
                try
                {
                    if (rs.next())
                    {
                        ret = true;
                    }
                }
                finally
                {
                    rs.close();
                }
            }
            finally
            {
                stm.close();
            }
        }
        catch (SQLException e)
        {
            Utils.toLog(this.getClass().getName() + ": " + e.getMessage(), "e");
            throw new ServletException(e);
        }
        return ret;
    }
    
    public synchronized void updateUserAvatar(Integer userID, String userAvatar) throws SQLException
    {
        userAvatar = "data:image/png;base64," + userAvatar;
        PreparedStatement stm;
        stm = conn.prepareStatement
        (
            "UPDATE users SET userAvatar = ? WHERE userID = ?"
        );
        
        stm.setString(1, userAvatar);
        stm.setInt(2, userID);
        
        try
        {
            Boolean rs = stm.execute();
        }
        finally
        {
            stm.close();
        }
    }

}
