package controlPanel;

/**
 * User list view which displays list of all users in the database
 */
public class UserListView extends AbstractListView
{
    // *** VARIABLES**
    // --- ENUM ---
    private Main.VIEW_TYPE view_type;

    public UserListView()
    {
        super("User List");
        view_type = Main.VIEW_TYPE.USER_LIST;
        setListTitle("USER LIST");
    }

    @Override
    Main.VIEW_TYPE getEnum() {
        return view_type;
    }
}
