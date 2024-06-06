package repository.member;

import domain.Member;
import services.MemberRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class DbmsMemberRepo implements MemberRepository {
    private final String url = "jdbc:mysql://127.0.0.1:3306/cashier";
    private final String user = "root";
    private final String password = "0012";


    public DbmsMemberRepo() {
        String sql = "CREATE TABLE IF NOT EXISTS members (" +
                "member_id INT PRIMARY KEY AUTO_INCREMENT," +
                "name VARCHAR(255) NOT NULL," +
                "tel VARCHAR(255) NOT NULL UNIQUE," +
                "id_card VARCHAR(255) NOT NULL UNIQUE," +
                "point INT NOT NULL" +
                ")";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error creating table", e);
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    @Override
    public Member addMember(Member member) {
        String sql = "INSERT INTO members (name, tel, id_card, point) VALUES (?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, member.getName());
            statement.setString(2, member.getTel());
            statement.setString(3, member.getIdCard());
            statement.setInt(4, member.getPoint());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Failed to add member: " + e.getMessage());
            return null;
        }
        return member;
    }

    @Override
    public Member removeMember(int id) {
        Member deletedMember = null;
        String sqlSelect = "SELECT * FROM members WHERE member_id = ?";
        String sqlDelete = "DELETE FROM members WHERE member_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement selectStatement = connection.prepareStatement(sqlSelect);
             PreparedStatement deleteStatement = connection.prepareStatement(sqlDelete)) {
            selectStatement.setInt(1, id);
            try (ResultSet resultSet = selectStatement.executeQuery()) {
                if (resultSet.next()) {
                    deletedMember = new Member(
                            resultSet.getString("name"),
                            resultSet.getString("tel"),
                            resultSet.getString("id_card")
                    );
                }
            }
            deleteStatement.setInt(1, id);
            deleteStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Failed to remove member: " + e.getMessage());
        }
        return deletedMember;
    }

    @Override
    public int findIdByTel(String tel) {
        String sql = "SELECT member_id FROM members WHERE tel = ? LIMIT 1";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, tel);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("member_id");
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed to find member by tel: " + e.getMessage());
        }
        return 0;
    }

    @Override
    public int findIdByIdCard(String idCard) {
        String sql = "SELECT member_id FROM members WHERE id_card = ? LIMIT 1";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, idCard);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("member_id");
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed to find member by ID card: " + e.getMessage());
        }
        return 0;
    }

    @Override
    public Member findMember(String tel) {
        String sql = "SELECT * FROM members WHERE tel = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, tel);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Member(
                            resultSet.getString("name"),
                            resultSet.getString("tel"),
                            resultSet.getString("id_card")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed to find member by tel: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Member> findAllMembers() {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM members";
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                members.add(new Member(
                        resultSet.getString("name"),
                        resultSet.getString("tel"),
                        resultSet.getString("id_card")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Failed to fetch members: " + e.getMessage());
        }
        return members;
    }


    public Member updateMember(Member updatedMember) {
        String sql = "UPDATE members SET name = ?, tel = ? WHERE id_card = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, updatedMember.getName());
            statement.setString(2, updatedMember.getTel());
            statement.setString(3, updatedMember.getIdCard());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Failed to update member: " + e.getMessage());
            return null;
        }
        return updatedMember;
    }

    @Override
    public int increasePoint(String tel, int point) {
        int memberId = findIdByTel(tel);
        if (memberId != 0) {
            String sql = "UPDATE members SET point = point + ? WHERE member_id = ?";
            try (Connection connection = getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, point);
                statement.setInt(2, memberId);
                statement.executeUpdate();
                return findMember(tel).getPoint();
            } catch (SQLException e) {
                System.out.println("Failed to increase point: " + e.getMessage());
                return 0;
            }
        }
        return 0;
    }

    @Override
    public int decreasePoint(String tel, int point) {
        int memberId = findIdByTel(tel);
        if (memberId != 0) {
            String sql = "UPDATE members SET point = point - ? WHERE member_id = ?";
            try (Connection connection = getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, point);
                statement.setInt(2, memberId);
                statement.executeUpdate();
                return findMember(tel).getPoint();
            } catch (SQLException e) {
                System.out.println("Failed to decrease point: " + e.getMessage());
                return 0;
            }
        }
        return 0;
    }

    @Override
    public Stream<Member> getStream() {
        String sql = "SELECT * FROM members";
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            List<Member> members = new ArrayList<>();
            while (resultSet.next()) {
                Member member = new Member(
                        resultSet.getString("name"),
                        resultSet.getString("tel"),
                        resultSet.getString("id_card")
                );
                members.add(member);
            }
            return members.stream();
        } catch (SQLException e) {
            System.out.println("Failed to create stream: " + e.getMessage());
            return Stream.empty();
        }
    }

}
