/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package db;

import java.io.Serializable;

/**
 *
 * @author giorgio
 */
public class User implements Serializable
{
    private Integer userID;
    private String userName;
    private String userPwd;
    private String userAvatar;

    public Integer getUserID()
    {
        return userID;
    }

    public void setUserID(Integer userID)
    {
        this.userID = userID;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getUserPwd()
    {
        return userPwd;
    }

    public void setUserPwd(String userPwd)
    {
        this.userPwd = userPwd;
    }

    public String getUserAvatar()
    {
        return userAvatar;
    }

    public void setUserAvatar(String userAvater)
    {
        this.userAvatar = userAvater;
    }
    
}
