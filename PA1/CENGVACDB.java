package ceng.ceng351.cengvacdb;
import ceng.ceng351.cengvacdb.*;
import java.sql.*;
import java.util.ArrayList;

public class CENGVACDB implements ICENGVACDB {

    private static String user = "e2304277"; // TODO: Your userName
    private static String password = "qCFWgDok#N0z"; //  TODO: Your password
    private static String host = "144.122.71.121"; // host name
    private static String database = "db2304277"; // TODO: Your database name
    private static int port = 8080; // port

    private static Connection conn = null;

    @Override
    public void initialize() {
        String url = "jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database + "?useSSL=false";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.conn =  DriverManager.getConnection(url, this.user, this.password);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int createTables() {

        String user = "CREATE TABLE IF NOT EXISTS User(" +
                "userID INT NOT NULL, " +
                "userName VARCHAR(30), " +
                "age INT, " +
                "address VARCHAR(150), " +
                "password VARCHAR(30), " +
                "status VARCHAR(15), " +
                "PRIMARY KEY (userID));";

        String vaccine = "CREATE TABLE IF NOT EXISTS Vaccine("
                +
                "code INT NOT NULL, "
                +
                "vaccinename VARCHAR(30), "
                +
                "type VARCHAR(30), "
                +
                "PRIMARY KEY (code));";

        String vaccination = "CREATE TABLE IF NOT EXISTS Vaccination("
                +
                "code INT NOT NULL, "
                +
                "userID INT NOT NULL, "
                +
                "dose INT, "
                +
                "vacdate DATE, "
                +
                "FOREIGN KEY (code) REFERENCES Vaccine(code), "
                +
                "FOREIGN KEY (userID) REFERENCES User(userID), "
                +
                "PRIMARY KEY (code, userID, dose));";

        String allergicsideeffect = "CREATE TABLE IF NOT EXISTS AllergicSideEffect(" +
                "effectcode INT, "
                +
                "effectname VARCHAR(50), "
                +
                "PRIMARY KEY (effectcode));";

        String seen = "CREATE TABLE IF NOT EXISTS Seen("
                +
                "effectcode INT, "
                +
                "code INT, "
                +
                "userID INT, "
                +
                "date DATE, "
                +
                "degree VARCHAR(30), "
                +
                "FOREIGN KEY (effectcode) REFERENCES AllergicSideEffect(effectcode), " +
                "FOREIGN KEY (code) REFERENCES Vaccine(code) "
                +
                "ON DELETE CASCADE, "
                +
                "FOREIGN KEY (userID) REFERENCES User(userID), "
                +
                "PRIMARY KEY (effectcode, code, userID));";

        Statement stmt = null;
        int updateCount = 0;

        try{
            stmt = conn.createStatement();
            stmt.executeUpdate(user);
            updateCount++;
            stmt.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }

        try{
            stmt = conn.createStatement();
            stmt.executeUpdate(vaccine);
            updateCount++;
            stmt.close();
        }
        catch (SQLException se) {
            se.printStackTrace();
        }

        try{
            stmt = conn.createStatement();
            stmt.executeUpdate(vaccination);
            updateCount++;
            stmt.close();
        }
        catch (SQLException se) {
            se.printStackTrace();
        }

        try{
            stmt = conn.createStatement();
            stmt.executeUpdate(allergicsideeffect);
            updateCount++;
            stmt.close();
        }
        catch (SQLException se) {
            se.printStackTrace();
        }

        try{
            stmt = conn.createStatement();
            stmt.executeUpdate(seen);
            updateCount++;
            stmt.close();
        }
        catch (SQLException se) {
            se.printStackTrace();
        }

        return updateCount;
    }

    @Override
    public int dropTables() {
        String user = "DROP TABLE IF EXISTS User;";
        String vaccine = "DROP TABLE IF EXISTS Vaccine;";
        String vaccination = "DROP TABLE IF EXISTS Vaccination;";
        String allergicsideeffect = "DROP TABLE IF EXISTS AllergicSideEffect;";
        String seen = "DROP TABLE IF EXISTS Seen;";

        Statement stmt = null;
        int updateCount = 0;

        try {
            stmt = this.conn.createStatement();
            stmt.executeUpdate(seen);

            updateCount++;

        } catch (SQLException e) {
            e.printStackTrace();
        }try {
            stmt = this.conn.createStatement();
            stmt.executeUpdate(allergicsideeffect);

            updateCount++;

        } catch (SQLException e) {
            e.printStackTrace();
        }try {
            stmt = this.conn.createStatement();
            stmt.executeUpdate(vaccination);

            updateCount++;

        } catch (SQLException e) {
            e.printStackTrace();
        }try {
            stmt = this.conn.createStatement();
            stmt.executeUpdate(vaccine);

            updateCount++;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            stmt = this.conn.createStatement();
            stmt.executeUpdate(user);

            updateCount++;

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                // Close connection
                if (stmt != null) {
                    stmt.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return updateCount;
    }

    @Override
    public int insertUser(User[] users) {
        String insertU = "INSERT INTO User(userID, userName, age, address, password, status) "
                + "VALUES(?,?,?,?,?,?)";
        PreparedStatement stmt;
        int updateCount = 0;

        try {
            stmt = conn.prepareStatement(insertU);

            for (User x : users) {
                stmt.setInt(1, x.getUserID());
                stmt.setString(2, x.getUserName());
                stmt.setInt(3, x.getAge());
                stmt.setString(4, x.getAddress());
                stmt.setString(5, x.getPassword());
                stmt.setString(6, x.getStatus());

                stmt.addBatch();

            }
            int[] temp = stmt.executeBatch();
            updateCount = temp.length;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return updateCount;
    }

    @Override
    public int insertAllergicSideEffect(AllergicSideEffect[] sideEffects) {
        String insertA = "INSERT INTO AllergicSideEffect(effectcode, effectname) "
                + "VALUES(?,?)";
        PreparedStatement stmt;
        int updateCount = 0;

        try {
            stmt = conn.prepareStatement(insertA);

            for (AllergicSideEffect x : sideEffects) {
                stmt.setInt(1, x.getEffectCode());
                stmt.setString(2, x.getEffectName());
                stmt.addBatch();
            }

            int[] temp = stmt.executeBatch();
            updateCount = temp.length;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return updateCount;
    }

    @Override
    public int insertVaccine(Vaccine[] vaccines) {
        String insertVaccine1 = "INSERT INTO Vaccine(code, vaccinename, type) "
                + "VALUES(?,?,?)";
        PreparedStatement stmt;
        int updateCount = 0;

        try {
            stmt = conn.prepareStatement(insertVaccine1);

            for (Vaccine x : vaccines) {
                stmt.setInt(1, x.getCode());
                stmt.setString(2, x.getVaccineName());
                stmt.setString(3, x.getType());
                stmt.addBatch();
            }

            int[] temp = stmt.executeBatch();
            updateCount = temp.length;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return updateCount;
    }

    @Override
    public int insertVaccination(Vaccination[] vaccinations) {
        String insertVaccination1 = "INSERT INTO Vaccination(code, userID, dose, vacdate) "
                + "VALUES(?,?,?,?)";
        PreparedStatement stmt;
        int updateCount = 0;

        try {
            stmt = conn.prepareStatement(insertVaccination1);

            for (Vaccination x : vaccinations) {
                stmt.setInt(1, x.getCode());
                stmt.setInt(2, x.getUserID());
                stmt.setInt(3, x.getDose());
                stmt.setString(4, x.getVacdate());
                stmt.addBatch();
            }

            int[] temp = stmt.executeBatch();
            updateCount = temp.length;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return updateCount;
    }

    @Override
    public int insertSeen(Seen[] seens) {
        String insertS = "INSERT INTO Seen(effectcode, code, userID, date, degree) "
                + "VALUES(?,?,?,?,?)";
        PreparedStatement stmt;
        int updateCount = 0;

        try {
            stmt = conn.prepareStatement(insertS);

            for (Seen x : seens) {
                stmt.setInt(1, x.getEffectcode());
                stmt.setInt(2, x.getCode());
                stmt.setString(3, x.getUserID());
                stmt.setString(4, x.getDate());
                stmt.setString(5, x.getDegree());
                stmt.addBatch();
            }

            int[] temp = stmt.executeBatch();
            updateCount = temp.length;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return updateCount;
    }

    @Override
    public Vaccine[] getVaccinesNotAppliedAnyUser() {

        String q3 = "SELECT DISTINCT V.code, V.vaccinename, V.type " +
                    "FROM Vaccine V " +
                    "WHERE V.code NOT IN "+
                        "(SELECT V.code "+
                        "FROM Vaccine V, Vaccination V1 "+
                        "WHERE V.code = V1.code) " +
                        "ORDER BY V.code ASC; ";
        Statement stmt;
        ArrayList<Vaccine> resultList = new ArrayList<>();

        try {

            stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(q3);
            while (rs.next()) {
                int code = rs.getInt("code");
                String vaccinename = rs.getString("vaccinename");
                String type = rs.getString("type");
                resultList.add(new Vaccine(code, vaccinename, type));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        Vaccine[] result = new Vaccine[resultList.size()];
        result = resultList.toArray(result);

        return result;
    }

    @Override
    public QueryResult.UserIDuserNameAddressResult[] getVaccinatedUsersforTwoDosesByDate(String vacdate) {

        String q4 = "SELECT DISTINCT U.userID, U.userName, U.address " +
                "FROM Vaccination V, User U " +
                "WHERE U.userID = V.userID "+
                "AND V.vacdate >= \""+ vacdate +
                "\" " +
                "AND V.dose = 1 "+
                "AND EXISTS "+
                "(SELECT * " +
                "FROM Vaccination V2 " +
                "WHERE U.userID = V2.userID AND  V2.dose = 2) " +
                "ORDER BY U.userID ASC ";

        Statement stmt;
        ArrayList<QueryResult.UserIDuserNameAddressResult> resultList = new ArrayList<>();

        try {

            stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(q4);
            while (rs.next()) {
                String userID = rs.getString("userID");
                String username = rs.getString("username");
                String address = rs.getString("address");
                resultList.add(new QueryResult.UserIDuserNameAddressResult(userID, username, address));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        QueryResult.UserIDuserNameAddressResult[] result = new QueryResult.UserIDuserNameAddressResult[resultList.size()];
        result = resultList.toArray(result);

        return result;
    }

    @Override
    public Vaccine[] getTwoRecentVaccinesDoNotContainVac() {


        String q5 = "SELECT DISTINCT V.code, V.vaccinename, V.type " +
                "FROM Vaccine V, Vaccination Vac " +
                "WHERE V.code = Vac.code AND  V.code IN (SELECT DISTINCT  V1.code FROM Vaccine V1, Vaccination Vac1 WHERE V.vaccinename NOT LIKE '%vac%' AND V1.code = Vac1.code " +
                "ORDER BY Vac1.code DESC) " +
                "LIMIT 2 " ;


        Statement stmt;
        ArrayList<Vaccine> resultList = new ArrayList<>();

        try {

            stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(q5);
            while (rs.next()) {
                int code = rs.getInt("code");
                String vaccinename = rs.getString("vaccinename");
                String type = rs.getString("type");
                resultList.add(new Vaccine(code, vaccinename, type));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        Vaccine[] result = new Vaccine[resultList.size()];
        result = resultList.toArray(result);

        return result;
    }

    @Override
    public QueryResult.UserIDuserNameAddressResult[] getUsersAtHasLeastTwoDoseAtMostOneSideEffect() {

        String q6 = "SELECT DISTINCT U.userID, U.userName, U.address " +
                    "FROM Vaccination V, User U " +
                    "WHERE U.userID = V.userID AND V.dose >= 2 "+
                    "AND U.userID NOT IN "+
                        "(SELECT DISTINCT U.userID " +
                        "FROM Seen S, Seen S1 " +
                        "WHERE U.userID = S.userID AND U.userID = S1.userID " +
                        "AND S1.effectcode <> S.effectcode) " +
                        "ORDER BY U.userID ASC ";

        Statement stmt;
        ArrayList<QueryResult.UserIDuserNameAddressResult> resultList = new ArrayList<>();

        try {

            stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(q6);
            while (rs.next()) {
                String userID = rs.getString("userID");
                String username = rs.getString("username");
                String address = rs.getString("address");
                resultList.add(new QueryResult.UserIDuserNameAddressResult(userID, username, address));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        QueryResult.UserIDuserNameAddressResult[] result = new QueryResult.UserIDuserNameAddressResult[resultList.size()];
        result = resultList.toArray(result);

        return result;
    }

    @Override
    public QueryResult.UserIDuserNameAddressResult[] getVaccinatedUsersWithAllVaccinesCanCauseGivenSideEffect(String effectname) {

        String q7 = "SELECT DISTINCT U.userID, U.userName, U.address " +
                    "FROM User U, Vaccination V " +
                    "WHERE U.userID = V.userID  AND NOT EXISTS " +
                        "(SELECT DISTINCT V.code "+
                        "FROM Vaccination V, Seen S " +
                        "WHERE V.userID = U.userID AND  S.code = V.code AND NOT EXISTS " +
                            "(SELECT * " +
                            "FROM AllergicSideEffect A, Seen S1 " +
                            "WHERE S.effectcode = A.effectcode AND S1.effectcode = A.effectcode AND A.effectname = \""+ effectname +
                            "\" )) " +
                            "ORDER BY U.userID ASC ";



        Statement stmt;
        ArrayList<QueryResult.UserIDuserNameAddressResult> resultList = new ArrayList<>();

        try {

            stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(q7);
            while (rs.next()) {
                String userID = rs.getString("userID");
                String username = rs.getString("username");
                String address = rs.getString("address");
                resultList.add(new QueryResult.UserIDuserNameAddressResult(userID, username, address));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        QueryResult.UserIDuserNameAddressResult[] result = new QueryResult.UserIDuserNameAddressResult[resultList.size()];
        result = resultList.toArray(result);

        return result;
    }

    @Override
    public QueryResult.UserIDuserNameAddressResult[] getUsersWithAtLeastTwoDifferentVaccineTypeByGivenInterval(String startdate, String enddate) {
        String q8 = "SELECT DISTINCT U.userID, U.userName, U.address " +
                    "FROM User U, Vaccine v1, Vaccination V1, Vaccine v2, Vaccination V2 " +
                    "WHERE U.userID = V1.userID AND U.userID = V2.userID AND " +
                    "V1.vacdate >= ? AND V1.vacdate <= ? AND "+
                    "V1.code = v1.code AND V2.code = v2.code AND " +
                    "v1.code <> v2.code AND v1.type <> v2.type " +
                    "ORDER BY U.userID ASC ";

        PreparedStatement stmt;
        ArrayList<QueryResult.UserIDuserNameAddressResult> resultList = new ArrayList<>();

        try {
            stmt = conn.prepareStatement(q8);
            stmt.setString(1,startdate);
            stmt.setString(2,enddate);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String userID = rs.getString("U.userID");
                String userName = rs.getString("U.userName");
                String address = rs.getString("U.address");
                resultList.add(new QueryResult.UserIDuserNameAddressResult(userID, userName, address));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        QueryResult.UserIDuserNameAddressResult[] result = new QueryResult.UserIDuserNameAddressResult[resultList.size()];
        result = resultList.toArray(result);

        return result;
    }

    @Override
    public AllergicSideEffect[] getSideEffectsOfUserWhoHaveTwoDosesInLessThanTwentyDays() {
        String q9 = "SELECT DISTINCT A.effectcode, A.effectname " +
                    "FROM Vaccination V, AllergicSideEffect A, User U, Seen S " +
                    "WHERE U.userID = V.userID "+
                    "AND V.userID = S.userID "+
                    "AND S.effectcode = A.effectcode "+
                    "AND U.userID IN "+
                        "(SELECT DISTINCT U.userID " +
                        "FROM User U, Vaccination V1, Vaccination V2 " +
                        "WHERE U.userID = V1.userID AND  U.userID = V2.userID AND V1.dose = 1 AND V2.dose = 2 AND DATEDIFF(V2.vacdate, V1.vacdate) <= 20) " +
                        "ORDER BY A.effectcode ASC ";
        Statement stmt;
        ArrayList<AllergicSideEffect> resultList = new ArrayList<>();

        try {

            stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(q9);
            while (rs.next()) {
                int effectcode = rs.getInt("effectcode");
                String effectname = rs.getString("effectname");
                resultList.add(new AllergicSideEffect(effectcode, effectname));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        AllergicSideEffect[] result = new AllergicSideEffect[resultList.size()];
        result = resultList.toArray(result);

        return result;
    }

    @Override
    public double averageNumberofDosesofVaccinatedUserOverSixtyFiveYearsOld() {

        String q10 = "SELECT AVG(maxdose) AS avgofmax " +
                        "FROM (SELECT U.userID, MAX(V.dose) AS maxdose " +
                        "FROM  User U, Vaccination V " +
                        "WHERE U.userID = V.userID AND U.age > 65 "+
                        "GROUP BY U.userID) AS dosealias " ;
        ResultSet res;
        double result = 0;
        try{
            Statement statement = this.conn.createStatement();
            res = statement.executeQuery(q10);

            while(res.next()){
                result = res.getDouble("avgofmax");
            }

            statement.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int updateStatusToEligible(String givendate) {

        String st1 = "Eligible";
        String st2 = "Not_Eligible";

        String q11 = "UPDATE User " +
                        "SET status = \"" + st1 +"\" " +
                        "WHERE status = \"" + st2 + "\" AND userID IN (Select V.userID " +
                        "                                           FROM Vaccination V " +
                        "                                           WHERE V.userID = userID " +
                        "                                           GROUP BY V.userID " +
                        "                                           HAVING DATEDIFF( \"" +givendate+ "\" ,MAX(V.vacdate)) > 120);";



        PreparedStatement stmt;
        int updateCount = 0;
        try {
            stmt = conn.prepareStatement(q11);


            updateCount = stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return updateCount;
    }

    @Override
    public Vaccine deleteVaccine(String vaccineName) {
        ResultSet rs;
        ArrayList<Vaccine> reslist = new ArrayList<>();



        try {

            PreparedStatement stmt=this.conn.prepareStatement("SELECT DISTINCT V.code, V.vaccinename, V.type " +
                    "FROM Vaccine V WHERE V.vaccinename = ? ");

            stmt.setString(1,vaccineName);
            rs = stmt.executeQuery();

            rs.next();

            Integer code = rs.getInt("code");
            String vaccinename = rs.getString("vaccinename");
            String type = rs.getString("type");


            Vaccine obj = new Vaccine(code,vaccinename,type);

            try {

                PreparedStatement stmt2=this.conn.prepareStatement("delete from Vaccine where vaccinename=?");
                stmt2.setString(1,vaccineName);

                stmt2.executeUpdate();

                //close
                stmt2.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }

            reslist.add(obj);
            //Close
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reslist.get(0);
}
}

