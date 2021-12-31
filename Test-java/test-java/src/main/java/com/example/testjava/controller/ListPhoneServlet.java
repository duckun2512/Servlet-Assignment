package com.example.testjava.controller;

import com.example.testjava.entity.Phone;
import com.example.testjava.repository.JpaRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ListPhoneServlet extends HttpServlet {
    JpaRepository<Phone> phoneJpaRepository = new JpaRepository<>(Phone.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Phone> phone = phoneJpaRepository.findAll();
        req.setAttribute("phone", phone);
        req.getRequestDispatcher("/admin/phone/list.jsp").forward(req, resp);
    }
}