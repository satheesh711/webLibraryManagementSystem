package com.webLibraryManagementSystem.controllers;

import java.io.IOException;

import com.webLibraryManagementSystem.domain.Member;
import com.webLibraryManagementSystem.services.impl.MemberServiceImpl;
import com.webLibraryManagementSystemexceptions.InvalidException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class DeleteMember
 */
@WebServlet("/DeleteMember")
public class DeleteMemberServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public DeleteMemberServlet() {
		super();
	}

	MemberServiceImpl memberService = new MemberServiceImpl();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int memberId = Integer.parseInt(request.getParameter("memberId"));
		try {
			Member member = memberService.getMemberId(memberId);
			memberService.deleteMember(member);
			request.setAttribute("message", member.getName() + "deleted Successfully");

		} catch (InvalidException e) {
			request.setAttribute("message", e.getMessage());
		}

		RequestDispatcher dispatcher = request.getRequestDispatcher("MembersViewAllServlet");
		dispatcher.forward(request, response);

	}

}
