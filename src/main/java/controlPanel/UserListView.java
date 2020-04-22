package controlPanel;

import observer.Subject;

import javax.swing.*;

public class UserListView extends AbstractListView
{
    // *** VARIABLES**
    // --- ENUM ---
    private Main.VIEW_TYPE view_type;

    public UserListView()
    {
        super("User List");
        view_type = Main.VIEW_TYPE.USER_LIST;
    }

    @Override
    Main.VIEW_TYPE getEnum() {
        return view_type;
    }

    @Override
    public void update(Subject s)
    {

    }
}
