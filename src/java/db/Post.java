/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package db;

import java.sql.Timestamp;

/**
 *
 * @author giorgio
 */
public class Post
{
    Integer postID;
    Integer groupID;
    Integer userID;
    Timestamp postDataCreation;
    String postText;

    public Integer getPostID()
    {
        return postID;
    }

    public void setPostID(Integer postID)
    {
        this.postID = postID;
    }

    public Integer getGroupID()
    {
        return groupID;
    }

    public void setGroupID(Integer groupID)
    {
        this.groupID = groupID;
    }

    public Integer getUserID()
    {
        return userID;
    }

    public void setUserID(Integer userID)
    {
        this.userID = userID;
    }

    public Timestamp getPostDataCreation()
    {
        return postDataCreation;
    }

    public void setPostDataCreation(Timestamp postDataCreation)
    {
        this.postDataCreation = postDataCreation;
    }

    public String getPostText()
    {
        return postText;
    }

    public void setPostText(String postText)
    {
        this.postText = postText;
    }
}
