package com.example.assignmentjava.controller;

import com.example.assignmentjava.entity.Category;
import com.example.assignmentjava.repository.JpaRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CreateCategoryServlet extends HttpServlet {
    JpaRepository<Category> repository = new JpaRepository<>(Category.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/admin/category/form.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setCharacterEncoding("UTF-8");
            String name = req.getParameter("name");
            Category category = new Category(name);
            repository.save(category);
            resp.sendRedirect("/admin/category/list");
        }catch (Exception ex){
            resp.getWriter().println("Bad request");
        }
    }
}