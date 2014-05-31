/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package support;

import db.Groups;

/**
 *
 * @author giorgio
 */
public class GroupsForUser extends Groups
{
    private Boolean userIsAdmin;

    public Boolean getUserIsAdmin()
    {
        return userIsAdmin;
    }

    public void setUserIsAdmin(Boolean userIsAdmin)
    {
        this.userIsAdmin = userIsAdmin;
    }
}
