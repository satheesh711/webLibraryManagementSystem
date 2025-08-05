package com.webLibraryManagementSystem.controllers;

import com.webLibraryManagementSystem.domain.Member;
import com.webLibraryManagementSystemexceptions.InvalidException;
import com.webLibraryManagementSystem.services.MemberService;
import com.webLibraryManagementSystem.services.impl.MemberServiceImpl;
import com.webLibraryManagementSystem.utilities.MemberGender;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/MemberUpdateServlet")
public class MemberUpdateServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final MemberService memberService = new MemberServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Fetch all members for dropdown
            List<Member> membersList = memberService.getMembers();
            request.setAttribute("membersList", membersList);

            // Forward to JSP page
            request.getRequestDispatcher("updateMember.jsp").forward(request, response);

        } catch (InvalidException e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("updateMember.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String memberIdParam = request.getParameter("memberId");
        if (memberIdParam == null || memberIdParam.isEmpty()) {
            request.setAttribute("errorMessage", "Please select a member first");
            doGet(request, response);
            return;
        }

        int memberId = Integer.parseInt(memberIdParam);

        // Collect updated form data
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String mobileStr = request.getParameter("mobile");
        String genderStr = request.getParameter("gender");
        String address = request.getParameter("address");

        long mobile = 0L;
        try {
            mobile = Long.parseLong(mobileStr);
        } catch (NumberFormatException ignored) {}

        MemberGender gender = null;
        if (genderStr != null && !genderStr.isEmpty()) {
            gender = MemberGender.valueOf(genderStr);
        }

        try {
            // Fetch the old member
            Member oldMember = memberService.getMembers().stream()
                    .filter(m -> m.getMemberId() == memberId)
                    .findFirst()
                    .orElse(null);

            if (oldMember == null) {
                request.setAttribute("errorMessage", "Member not found");
                doGet(request, response);
                return;
            }

            Member newMember = new Member(memberId, name, email, mobile, gender, address);

            if (!newMember.equals(oldMember)) {
                memberService.updateMember(newMember, oldMember);
                request.setAttribute("successMessage", oldMember.getName() + " updated successfully.");
            } else {
                request.setAttribute("errorMessage", "Please edit at least one field before updating.");
            }

        } catch (InvalidException e) {
            request.setAttribute("errorMessage", e.getMessage());
        }

        doGet(request, response);
    }
}
