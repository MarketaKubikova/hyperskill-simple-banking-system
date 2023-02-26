package banking;

import java.sql.*;

public class Update {
    private Connection connect() {
        String url = "jdbc:sqlite:card.s3db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void addIncome(int income, String cardNumber) {
        String sql = "UPDATE card SET balance = balance + ? WHERE number = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, income);
            pstmt.setString(2, cardNumber);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void transferMoney(int amount, String senderCardNumber, String recipientCardNumber) {
        String sqlOutgoingAccount = "UPDATE card SET balance = balance - ? WHERE number = ?";
        String sqlIncomingAccount = "UPDATE card SET balance = balance + ? WHERE number = ?";

        ResultSet rs = null;
        Connection conn = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;

        try {
            conn = this.connect();
            if(conn == null)
                return;

            conn.setAutoCommit(false);

            pstmt1 = conn.prepareStatement(sqlOutgoingAccount);

            pstmt1.setInt(1, amount);
            pstmt1.setString(2, senderCardNumber);
            int rowAffected = pstmt1.executeUpdate();

            if (rowAffected != 1) {
                conn.rollback();
            }

            pstmt2 = conn.prepareStatement(sqlIncomingAccount);
            pstmt2.setInt(1, amount);
            pstmt2.setString(2, recipientCardNumber);
            pstmt2.executeUpdate();

            conn.commit();

        } catch (SQLException e1) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException e2) {
                System.out.println(e2.getMessage());
            }
            System.out.println(e1.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt1 != null) {
                    pstmt1.close();
                }
                if (pstmt2 != null) {
                    pstmt2.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e3) {
                System.out.println(e3.getMessage());
            }
        }
    }
}
