package com.example.assignmentjava.controller;

import com.example.assignmentjava.entity.Category;
import com.example.assignmentjava.repository.JpaRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class EditCategoryServlet extends HttpServlet {
    private JpaRepository<Category> categoryJpaRepository = new JpaRepository<>(Category.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            Category category = categoryJpaRepository.findById(id);
            if(category == null){
                resp.getWriter().println("Category is not found!");
            }else {
                req.setAttribute("category",category);
                req.getRequestDispatcher("/admin/Category/edit.jsp").forward(req,resp);
            }
        }catch (Exception e){
            resp.getWriter().println("Bad request");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setCharacterEncoding("UTF-8");
            int id = Integer.parseInt(req.getParameter("id"));
            Category category = categoryJpaRepository.findById(id);
            if(category == null){
                resp.getWriter().println("Category is not found");
            }else {
                String name = req.getParameter("name");
                category.setName(name);
                categoryJpaRepository.update(category);
                resp.sendRedirect("/admin/category/list");
            }
        }catch (Exception e){
            resp.getWriter().println("Bad request");
        }
    }
}