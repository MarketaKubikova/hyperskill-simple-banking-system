package banking;

import java.sql.*;

public class Select {
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

    public String findCard(String cardNumber) {
        String sql = "SELECT number FROM card WHERE number = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql)) {
            pstmt.setString(1, cardNumber);

            ResultSet rs  = pstmt.executeQuery();

            return rs.getString("number");
        } catch (SQLException e) {
            return null;
        }
    }

    public Account getCardEqualsToCardNumberAndPin(String cardNumber, String pin){
        String sql = "SELECT number, pin FROM card WHERE number = ? AND pin = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql)) {
            pstmt.setString(1, cardNumber);
            pstmt.setString(2, pin);

            ResultSet rs  = pstmt.executeQuery();

            Account account = new Account();

            account.setCardNumber(rs.getString("number"));
            account.setPin(rs.getString("pin"));

            return account;
        } catch (SQLException e) {
            return null;
        }
    }

    public Integer getBalance(String cardNumber, String pin){
        String sql = "SELECT balance FROM card WHERE number = ? AND pin = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql)) {
            pstmt.setString(1, cardNumber);
            pstmt.setString(2, pin);

            ResultSet rs  = pstmt.executeQuery();

            return rs.getInt("balance");
        } catch (SQLException e) {
            return null;
        }
    }
}
