package space.jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import space.common.DataSource;
import space.dto.Notice_Board;

public class JdbcNotice_BoardDao implements Notice_BoardDao {    
    
    @Override
    public List<Notice_Board> getAll() {
        List<Notice_Board> list = new ArrayList<>();
        String sql = "SELECT A.*, B.MEMBER_IDX, B.NAME AS WRITER FROM NOTICE_BOARD A JOIN MEMBER B\r\n"
        		+ "ON A.MEMBER_IDX = B.MEMBER_IDX";

        try (Connection conn = DataSource.getDataSource();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Notice_Board noticeBoard = new Notice_Board(
                    rs.getInt("NOTICE_IDX"),
                    rs.getString("TITLE"),
                    rs.getInt("MEMBER_IDX"),
                    rs.getString("CONTENT"),
                    rs.getTimestamp("REGIST_DATE"),
                    rs.getInt("VIEWS"),
                	rs.getString("WRITER"));
                
                list.add(noticeBoard);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Notice_Board get(int boardIdx) {
        Notice_Board noticeBoard = null;
        String sql = "SELECT * FROM Notice_Board WHERE BOARD_IDX = ?";

        try (Connection conn = DataSource.getDataSource();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, boardIdx);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                noticeBoard = new Notice_Board(
                    rs.getInt("BOARD_IDX"),
                    rs.getString("TITLE"),
                    rs.getInt("MEMBER_IDX"),
                    rs.getString("CONTENT"),
                    rs.getTimestamp("REGIST_DATE"),
                    rs.getInt("VIEWS")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return noticeBoard;
    }

    @Override
    public void insert(Notice_Board noticeBoard) {
        String sql = "INSERT INTO Notice_Board (TITLE, MEMBER_IDX, CONTENT, REGIST_DATE, VIEWS) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DataSource.getDataSource();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, noticeBoard.getTitle());
            pstmt.setInt(2, noticeBoard.getMemberIdx());
            pstmt.setString(3, noticeBoard.getContent());
            pstmt.setTimestamp(4, noticeBoard.getRegistDate());
            pstmt.setInt(5, noticeBoard.getViews());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Notice_Board noticeBoard) {
        String sql = "UPDATE Notice_Board SET TITLE = ?, MEMBER_IDX = ?, CONTENT = ?, VIEWS = ? WHERE BOARD_IDX = ?";

        try (Connection conn = DataSource.getDataSource();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, noticeBoard.getTitle());
            pstmt.setInt(2, noticeBoard.getMemberIdx());
            pstmt.setString(3, noticeBoard.getContent());
            pstmt.setInt(4, noticeBoard.getViews());
            pstmt.setInt(5, noticeBoard.getBoardIdx());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int boardIdx) {
        String sql = "DELETE FROM Notice_Board WHERE BOARD_IDX = ?";

        try (Connection conn = DataSource.getDataSource();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, boardIdx);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
