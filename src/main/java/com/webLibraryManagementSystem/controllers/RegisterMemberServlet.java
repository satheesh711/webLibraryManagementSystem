package com.webLibraryManagementSystem.controllers;

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

import java.io.IOException;

@WebServlet("/RegisterMemberServlet")
public class RegisterMemberServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private MemberService memberService;

    @Override
    public void init() throws ServletException {
 
        memberService = new MemberServiceImpl();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

  
//    	int id = Integer.parseInt(request.getParameter("id"));
    	String name = request.getParameter("name");
    	String email = request.getParameter("email");
    	Long mobile = Long.parseLong(request.getParameter("mobile"));
    	String genderStr = request.getParameter("gender"); 
    	MemberGender gender = MemberGender.valueOf(genderStr.toUpperCase()); 
    	String address = request.getParameter("address");


        
        Member member = new Member(-1, name, email, mobile, gender, address);
        member.setName(name);
        member.setEmail(email);
        member.setMobile(mobile);
        member.setGender(gender);
        member.setAddress(address);

        
        int isRegistered=0;
		try {
			isRegistered = memberService.registerMember(member);
		} catch (InvalidException e) {
			e.printStackTrace();
		}

        if (isRegistered>0) {
           
            request.setAttribute("successMessage", "Member registered successfully!");
            request.getRequestDispatcher("registerMember.jsp").forward(request, response);
        } else {
         
            request.setAttribute("errorMessage", "Registration failed. Try again.");
            request.getRequestDispatcher("registerMember.jsp").forward(request, response);
        }
    }
}
