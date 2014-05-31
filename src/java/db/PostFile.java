/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package db;

/**
 *
 * @author giorgio
 */
public class PostFile
{
    private Integer postFileID;
    private Integer postID;
    private String postFileOriginalFileName;
    private String postFileFileSystemName;
    private String postFileType;

    public Integer getPostFileID()
    {
        return postFileID;
    }

    public void setPostFileID(Integer postFileID)
    {
        this.postFileID = postFileID;
    }

    public Integer getPostID()
    {
        return postID;
    }

    public void setPostID(Integer postID)
    {
        this.postID = postID;
    }

    public String getPostFileOriginalFileName()
    {
        return postFileOriginalFileName;
    }

    public void setPostFileOriginalFileName(String postFileOriginalFileName)
    {
        this.postFileOriginalFileName = postFileOriginalFileName;
    }

    public String getPostFileFileSystemName()
    {
        return postFileFileSystemName;
    }

    public void setPostFileFileSystemName(String postFileFileSystemName)
    {
        this.postFileFileSystemName = postFileFileSystemName;
    }

    public String getPostFileType()
    {
        return postFileType;
    }

    public void setPostFileType(String postFileType)
    {
        this.postFileType = postFileType;
    }
    
    
}
