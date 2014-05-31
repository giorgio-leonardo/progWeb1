/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package db;

import java.sql.Date;

/**
 *
 * @author giorgio
 */
public class Groups
{
    Integer groupId;
    Integer userId;
    String groupName;
    java.sql.Date groupDataCreation;

    public Integer getGroupId()
    {
        return groupId;
    }

    public void setGroupId(Integer groupId)
    {
        this.groupId = groupId;
    }

    public Integer getUserId()
    {
        return userId;
    }

    public void setUserId(Integer userId)
    {
        this.userId = userId;
    }

    public String getGroupName()
    {
        return groupName;
    }

    public void setGroupName(String groupName)
    {
        this.groupName = groupName;
    }

    public Date getGroupDataCreation()
    {
        return groupDataCreation;
    }

    public void setGroupDataCreation(Date groupDataCreation)
    {
        this.groupDataCreation = groupDataCreation;
    }
    
    
    
}
