//package controlPanel;
//
//import controlPanel.Main.VIEW_TYPE;
//
//import javax.swing.*;
//
//class ProfileView extends AbstractUserView
//{
//    // *** DECLARE VARIABLES**
//    // --- ENUM ---
//    private VIEW_TYPE view_type;
//
//    /**
//     * Constructor for creating the Views of the application. The constructor sets the frame's name and set's up the
//     * View by defining Width and Height, default close operation and the Layout. The constructor also calls the
//     * createComponents() method which is defined in child classes.
//     *
//     * @param frame_name name of JFrame
//     */
//    public ProfileView(String frame_name)
//    {
//        super("Profile");
//        view_type = VIEW_TYPE.USER_VIEW;
//    }
//
//    @Override
//    VIEW_TYPE getEnum() {
//        return view_type;
//    }
//
//    @Override
//    void addUserPermissions()
//    {
//    }
//}
