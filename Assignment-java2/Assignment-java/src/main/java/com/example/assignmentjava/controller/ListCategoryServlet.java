package com.example.assignmentjava.controller;

import com.example.assignmentjava.entity.Category;
import com.example.assignmentjava.entity.Product;
import com.example.assignmentjava.repository.JpaRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ListCategoryServlet extends HttpServlet {
    JpaRepository<Category> categoryJpaRepository = new JpaRepository<>(Category.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int page = 1;
        int pageSize = 5;
        String pageReq = req.getParameter("page");
        String pageSizeReq = req.getParameter("pageSize");
        if (pageReq != null) {
            page = Integer.parseInt(pageReq);
        }
        if (pageSizeReq != null) {
            pageSize = Integer.parseInt(pageSizeReq);
        }

        int totalRecord = categoryJpaRepository.countData();
        List<Category> listByPage = categoryJpaRepository.findByPage(page, pageSize);

        req.setAttribute("list", listByPage);
        req.setAttribute("page", page);
        req.setAttribute("pageSize", pageSize);
        req.setAttribute("totalRecord", totalRecord);

        req.getRequestDispatcher("/admin/category/list.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int page = Integer.parseInt(req.getParameter("page"));
        int pageSize = Integer.parseInt(req.getParameter("pageSize"));

        resp.sendRedirect("/admin/category/list?page=" + page + "&pageSize=" + pageSize);
    }
}