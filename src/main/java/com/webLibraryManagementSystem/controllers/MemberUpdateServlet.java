package com.webLibraryManagementSystem.controllers;

import java.io.IOException;
import java.util.List;

import com.webLibraryManagementSystem.domain.Member;
import com.webLibraryManagementSystem.services.MemberService;
import com.webLibraryManagementSystem.services.impl.MemberServiceImpl;
import com.webLibraryManagementSystem.utilities.MemberGender;
import com.webLibraryManagementSystemexceptions.InvalidException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/MemberUpdateServlet")
public class MemberUpdateServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final MemberService memberService = new MemberServiceImpl();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (request.getParameter("methodforward") == "post") {
			doPost(request, response);
		}
		loadMembers(request);
		request.getRequestDispatcher("updateMember.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String memberIdParam = request.getParameter("memberId");
		if (memberIdParam == null || memberIdParam.isEmpty()) {
			request.setAttribute("errorMessage", "Please select a member first");
			loadMembers(request);
			request.getRequestDispatcher("updateMember.jsp").forward(request, response);
			return;
		}

		int memberId = Integer.parseInt(memberIdParam);
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		long mobile = 0L;
		try {
			mobile = Long.parseLong(request.getParameter("mobile"));
		} catch (NumberFormatException ignored) {
		}
		MemberGender gender = request.getParameter("gender") != null && !request.getParameter("gender").isEmpty()
				? MemberGender.valueOf(request.getParameter("gender"))
				: null;
		String address = request.getParameter("address");

		try {
			List<Member> membersList = memberService.getMembers();
			Member oldMember = membersList.stream().filter(m -> m.getMemberId() == memberId).findFirst().orElse(null);

			if (oldMember == null) {
				request.setAttribute("errorMessage", "Member not found");
			} else {
				Member newMember = new Member(memberId, name, email, mobile, gender, address);
				if (!newMember.equals(oldMember)) {
					memberService.updateMember(newMember, oldMember);
					request.setAttribute("successMessage", oldMember.getName() + " updated successfully.");
				} else {
					request.setAttribute("errorMessage", "Please edit at least one field before updating.");
				}
			}

			request.setAttribute("membersList", membersList);
		} catch (InvalidException e) {
			request.setAttribute("errorMessage", e.getMessage());
			loadMembers(request);
		}

		doGet(request, response);
	}

	private void loadMembers(HttpServletRequest request) {
		try {
			List<Member> membersList = memberService.getMembers();
			request.setAttribute("membersList", membersList);
		} catch (InvalidException e) {
			System.out.println("here");
			request.setAttribute("errorMessage", e.getMessage());
		}
	}
}
