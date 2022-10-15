import java.sql.*;

public class CreateDB {
    Connection con;

    {
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    Statement st;

    {
        try {
            st = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) {
        CreateDB createDB = new CreateDB();
        createDB.run();
    }




    public void run(){

        try {
            String createDB =
                    "CREATE DATABASE Assign1";
            st.executeUpdate(createDB);
            System.out.println("Created a DB");
        } catch (SQLException e) {
            System.out.println("ERROR: Could not create the DB");
            e.printStackTrace();
        }


        {
            try {
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Assign1", "root", "");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        {
            try {
                st = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }




        try {
            String create =
                    "CREATE TABLE `students` (" +
                            "  `SID` int(2) NOT NULL," +
                            "  `STUD_ID` int(8) NOT NULL," +
                            "  `FNAME` varchar(20) NOT NULL," +
                            "  `SNAME` varchar(20) NOT NULL," +
                            "  `TOT_REQ` int(8) NOT NULL" +
                            ")";
            st.executeUpdate(create);
            System.out.println("Created a table");
        } catch (SQLException e) {
            System.out.println("ERROR: Could not create the table");
            e.printStackTrace();
        }

        try {
            String add =
                    "INSERT INTO `students` (`SID`, `STUD_ID`, `FNAME`, `SNAME`, `TOT_REQ`) VALUES" +
                            "(0, 12345678, 'John', 'Doe', 0)," +
                            "(1, 11234567, 'Doe', 'John', 0)," +
                            "(2, 11123456, 'Joe', 'Bloggs', 0)," +
                            "(3, 11112345, 'Bloggs', 'Joe', 0)";
            st.executeUpdate(add);
            System.out.println("Added students");
        } catch (SQLException e) {
            System.out.println("ERROR: Could not add the students");
            e.printStackTrace();
        }

        try {
            String update =
                    "ALTER TABLE `students` ADD UNIQUE KEY `SID` (`SID`)";
            st.executeUpdate(update);
            System.out.println("Added unique key");
        } catch (SQLException e) {
            System.out.println("ERROR: Could not add the unique key");
            e.printStackTrace();
        }



    }
}
