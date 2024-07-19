package internal.management.accounts.domain.h2;
import lombok.extern.slf4j.Slf4j;
import java.sql.*;
import java.util.Random;

@Slf4j
public class GenerateUserCode {
    private GenerateUserCode() { throw new IllegalStateException("Utility class"); }
    private static String url = "jdbc:h2:mem:public";
    private static String username ="sa";
    private static String password = "test";
    private static Random rand = new Random();

    public static String generateUserCode() throws SQLException {
        Connection conn = DriverManager.getConnection(url, username, password);
        Statement stmt = conn.createStatement();
        String userIdentifier = null;
        boolean isUnique = false, notAvailableMore = false;

        try{
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM TB_USER WHERE user_code like '%-00000-00000'");
            rs.next();
            if (rs.getInt(1) == 100000)
                throw new RuntimeException("Achieved max user_code!!!");

            while (!isUnique) {
                userIdentifier = String.format("%05d", rand.nextInt(100000));
                userIdentifier += "-00000-00000";

                rs = stmt.executeQuery("SELECT COUNT(*) FROM TB_USER WHERE user_code = '''" + userIdentifier + "'''");
                rs.next();
                if (rs.getInt(1) == 0)
                    isUnique = true;
            }
        } catch (SQLException | RuntimeException e) {
            log.error(e.getMessage());
        } finally {
            stmt.close();
            conn.close();
        }
        return userIdentifier;
    }
}
