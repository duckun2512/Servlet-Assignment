package com.example.assignmentjava.controller;

import com.example.assignmentjava.entity.Category;
import com.example.assignmentjava.entity.Product;
import com.example.assignmentjava.repository.JpaRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;


public class ListProductServlet extends HttpServlet {

    private JpaRepository<Product> productJpaRepository = new JpaRepository<>(Product.class);
    private JpaRepository<Category> categoryJpaRepository = new JpaRepository<>(Category.class);

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

        int totalRecord = productJpaRepository.countData();
        List<Product> list = productJpaRepository.paginate(page, pageSize);
        List<Category> categoryList = categoryJpaRepository.findAll();

        req.setAttribute("list", list);
        req.setAttribute("categoryList", categoryList);
        req.setAttribute("page", page);
        req.setAttribute("pageSize", pageSize);
        req.setAttribute("totalRecord", totalRecord);
        req.getRequestDispatcher("/admin/product/list.jsp").forward(req, resp);
    }
}
