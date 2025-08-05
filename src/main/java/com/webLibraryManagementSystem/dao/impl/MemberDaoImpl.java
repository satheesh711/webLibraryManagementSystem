package com.webLibraryManagementSystem.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.webLibraryManagementSystem.dao.MemberDao;
import com.webLibraryManagementSystem.domain.Member;
import com.webLibraryManagementSystem.utilities.ConnectionPoolingServlet;
import com.webLibraryManagementSystem.utilities.MemberGender;
import com.webLibraryManagementSystem.utilities.SQLQueries;
import com.webLibraryManagementSystemexceptions.InvalidException;

public class MemberDaoImpl implements MemberDao {

	@Override
	public int RegisterMember(Member member) throws InvalidException {
		Connection con;
		try {
			con = ConnectionPoolingServlet.getDataSource().getConnection();
			PreparedStatement stmt = con.prepareStatement(SQLQueries.MEMBER_INSERT);

			stmt.setString(1, member.getName());
			stmt.setString(2, member.getEmail());
			stmt.setLong(3, member.getMobile());
			stmt.setString(4, String.valueOf(member.getGender().toString().charAt(0)));
			stmt.setString(5, member.getAddress());

			int rows = stmt.executeUpdate();

			if (rows <= 0) {
				throw new InvalidException("Member not added to server");
			}
			return rows;

		} catch (SQLException e) {
			throw new InvalidException("Error adding member: " + e.getMessage());
		}

	}

	@Override
	public boolean getMemberByMobile(Long mobile) throws InvalidException {
		PreparedStatement stmt;
		Connection con;
		try {
			con = ConnectionPoolingServlet.getDataSource().getConnection();
			stmt = con.prepareStatement(SQLQueries.MEMBER_SELECT_BY_MOBILE);
			stmt.setLong(1, mobile);

			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {

				return true;
			}

			return false;

		} catch (SQLException e) {
			throw new InvalidException("Error in Server" + e.getMessage());
		}
	}

	@Override
	public boolean getMemberByEmail(String email) throws InvalidException {
		PreparedStatement stmt;
		Connection con;
		try {
			con = ConnectionPoolingServlet.getDataSource().getConnection();
			stmt = con.prepareStatement(SQLQueries.MEMBER_SELECT_BY_EMAIL);
			stmt.setString(1, email);

			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {

				return true;
			}

			return false;

		} catch (SQLException e) {
			throw new InvalidException("Error in Server" + e.getMessage());
		}
	}

	@Override
	public int UpdateMember(Member member, Member oldMember) throws InvalidException {
		Connection con;
		try {
			con = ConnectionPoolingServlet.getDataSource().getConnection();
			PreparedStatement stmt = con.prepareStatement(SQLQueries.MEMBER_UPDATE);

			stmt.setString(1, member.getName());
			stmt.setString(2, member.getEmail());
			stmt.setLong(3, member.getMobile());
			stmt.setString(4, String.valueOf(member.getGender().toString().charAt(0)));
			stmt.setString(5, member.getAddress());
			stmt.setLong(6, member.getMemberId());

			int rowsUpdated = stmt.executeUpdate();

			if (rowsUpdated < 0) {
				throw new InvalidException("Member not added to server");
			}

			memberLog(oldMember);

			return rowsUpdated;

		} catch (SQLException e) {
			throw new InvalidException("Error in Server" + e.getMessage());
		}
	}

	@Override
	public List<Member> getAllMembers() throws InvalidException {

		List<Member> members = new ArrayList<>();
		Connection con;
		try {
			con = ConnectionPoolingServlet.getDataSource().getConnection();
			PreparedStatement stmt = con.prepareStatement(SQLQueries.SELECT_ALL_MEMBERS);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				MemberGender gender = rs.getString("gender").equalsIgnoreCase("F") ? MemberGender.FEMALE
						: MemberGender.MALE;
				Member member = new Member(rs.getInt("member_id"), rs.getString("name"), rs.getString("email"),
						rs.getLong("mobile"), gender, rs.getString("address"));
				members.add(member);
			}

		} catch (SQLException e) {
			throw new InvalidException("Error in Server" + e.getMessage());
		}

		return members;
	}

	@Override
	public int deleteMember(Member memberData) throws InvalidException {
		Connection con;
		try {
			con = ConnectionPoolingServlet.getDataSource().getConnection();
			PreparedStatement stmt = con.prepareStatement(SQLQueries.MEMBER_DELETE);
			stmt.setInt(1, memberData.getMemberId());

			int rowsDeleted = stmt.executeUpdate();

			if (rowsDeleted <= 0) {

				throw new InvalidException("No Member found with Name: " + memberData.getName());
			}

			memberLog(memberData);

			return rowsDeleted;

		} catch (SQLException e) {
			throw new InvalidException("Error in Server" + e.getMessage());
		}

	}

	@Override
	public void memberLog(Member member) throws InvalidException {
		PreparedStatement stmt;
		Connection con;
		try {
			con = ConnectionPoolingServlet.getDataSource().getConnection();
			stmt = con.prepareStatement(SQLQueries.MEMBERS_LOG_INSERT);
			stmt.setInt(1, member.getMemberId());
			stmt.setString(2, member.getName());
			stmt.setString(3, member.getEmail());
			stmt.setLong(4, member.getMobile());
			stmt.setString(5, String.valueOf(member.getGender().toString().charAt(0)));
			stmt.setString(6, member.getAddress());

			int rowsInserted = stmt.executeUpdate();

			if (rowsInserted > 0) {
				System.out.println("log added ");
			}

		} catch (SQLException e) {

			throw new InvalidException("Error in Server" + e.getMessage());
		}

	}

	@Override
	public Member getMemberId(int memberId) throws InvalidException {
		Member member = null;
		Connection con;
		try {
			con = ConnectionPoolingServlet.getDataSource().getConnection();
			PreparedStatement stmt = con.prepareStatement(SQLQueries.MEMBER_SELECT_BY_ID);
			stmt.setInt(1, memberId);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				MemberGender gender = rs.getString("gender").equalsIgnoreCase("F") ? MemberGender.FEMALE
						: MemberGender.MALE;
				member = new Member(rs.getInt("member_id"), rs.getString("name"), rs.getString("email"),
						rs.getLong("mobile"), gender, rs.getString("address"));

			}

		} catch (SQLException e) {
			throw new InvalidException("Error in Server" + e.getMessage());
		}

		return member;
	}
}
