<%--
  Created by IntelliJ IDEA.
  User: shyri
  Date: 2021-12-31
  Time: 7:40 a.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

</body>
</html>
<!-- Sidebar/menu -->
<nav class="w3-sidebar w3-collapse w3-white w3-animate-left" style="z-index:3;width:300px;" id="mySidebar"><br>
    <div class="w3-container w3-row">
        <div class="w3-col s4">
            <img src="https://www.w3schools.com/w3images/avatar2.png" class="w3-circle w3-margin-right"
                 style="width:46px">
        </div>
        <div class="w3-col s8 w3-bar">
            <span>Welcome, <strong>Admin</strong></span><br>
            <a href="#" class="w3-bar-item w3-button"><i class="fa fa-envelope"></i></a>
            <a href="#" class="w3-bar-item w3-button"><i class="fa fa-user"></i></a>
            <a href="#" class="w3-bar-item w3-button"><i class="fa fa-cog"></i></a>
        </div>
    </div>
    <hr>
    <div class="w3-container">
        <h5>Dashboard</h5>
    </div>
    <div class="w3-bar-block">
        <a href="#" class="w3-bar-item w3-button w3-padding-16 w3-hide-large w3-dark-grey w3-hover-black"
           onclick="w3_close()" title="close menu"><i class="fa fa-remove fa-fw"></i>  Close Menu</a>
        <a href="/admin/product/list" class="w3-bar-item w3-button w3-padding w3-blue"><i class="fa fa-list " aria-hidden="true"></i>&nbsp;
            List Phone</a>
        <a href="/admin/product/create" class="w3-bar-item w3-button w3-padding"><i class="fa fa-plus"></i>&nbsp; Create New Phone</a>
        <br><br>
    </div>
</nav>
