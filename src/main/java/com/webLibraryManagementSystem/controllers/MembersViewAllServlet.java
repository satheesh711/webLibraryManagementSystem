package com.webLibraryManagementSystem.controllers;

import com.webLibraryManagementSystem.domain.Member;
import com.webLibraryManagementSystemexceptions.InvalidException;
import com.webLibraryManagementSystem.services.MemberService;
import com.webLibraryManagementSystem.services.impl.MemberServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/MembersViewAllServlet")
public class MembersViewAllServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final MemberService memberService = new MemberServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        String memberIdParam = request.getParameter("memberId");

        // If Delete Request
        if ("delete".equalsIgnoreCase(action) && memberIdParam != null) {
            try {
                int memberId = Integer.parseInt(memberIdParam);
                Member member = new Member(memberId, memberIdParam, memberIdParam, memberId, null, memberIdParam);
                member.setMemberId(memberId);

                memberService.deleteMember(member);
                request.setAttribute("successMessage", "Member deleted successfully!");
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "Invalid Member ID for deletion.");
            } catch (InvalidException e) {
                request.setAttribute("errorMessage", e.getMessage());
            }
        }

        // Fetch Members List
        try {
            List<Member> membersList = memberService.getMembers();
            request.setAttribute("membersList", membersList);
        } catch (InvalidException e) {
            request.setAttribute("errorMessage", e.getMessage());
        }

        // Forward to JSP page
        request.getRequestDispatcher("membersViewAll.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Handling delete via POST if needed
        String memberIdParam = request.getParameter("memberId");
        if (memberIdParam != null) {
            try {
                int memberId = Integer.parseInt(memberIdParam);
                Member member = new Member(memberId, memberIdParam, memberIdParam, memberId, null, memberIdParam);
                member.setMemberId(memberId);

                memberService.deleteMember(member);
                request.setAttribute("successMessage", "Member deleted successfully!");
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "Invalid Member ID for deletion.");
            } catch (InvalidException e) {
                request.setAttribute("errorMessage", e.getMessage());
            }
        }

        // Reload member list
        doGet(request, response);
    }
}
