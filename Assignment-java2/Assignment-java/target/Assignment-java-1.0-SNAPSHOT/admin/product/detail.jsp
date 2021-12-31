<%@ page import="com.example.assignmentjava.entity.Product" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="com.example.assignmentjava.entity.Category" %>
<%@ page import="java.util.logging.SimpleFormatter" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    request.setCharacterEncoding("utf-8");
    Product product = (Product) request.getAttribute("product");
    ArrayList<Category> categories = (ArrayList<Category>) request.getAttribute("categoryList");
    SimpleFormatter formatter = new SimpleFormatter("dd/MM/yyyy");
    HashMap<String, String> errors = (HashMap<String, String>) request.getAttribute("errors");
    if (product == null) {
        product = new Product();
    }
    if (errors == null) {
        errors = new HashMap<>();
    }
%>

<!DOCTYPE html>
<html>
<head>
    <jsp:include page="/admin/include/header.jsp">
        <jsp:param name="title" value="<%= product.getName()%>"/>
        <jsp:param name="description" value="<%= product.getDescription()%>"/>
        <jsp:param name="keywords" value="admin, page,..."/>
    </jsp:include>
</head>
<body class="w3-light-grey">

<!-- Top container -->
<div class="w3-bar w3-top w3-black w3-large" style="z-index:4">
    <button class="w3-bar-item w3-button w3-hide-large w3-hover-none w3-hover-text-light-grey" onclick="w3_open();"><i class="fa fa-bars"></i>  Menu</button>
    <span class="w3-bar-item w3-right">Admin Page</span>
</div>

<jsp:include page="/admin/include/left-menu.jsp"/>

<!-- Overlay effect when opening sidebar on small screens -->
<div class="w3-overlay w3-hide-large w3-animate-opacity" onclick="w3_close()" style="cursor:pointer" title="close side menu" id="myOverlay"></div>

<!-- !PAGE CONTENT! -->
<div class="w3-main" style="margin-left:300px;margin-top:43px;">

    <!-- Header -->
    <header class="w3-container" style="padding-top:22px">
        <h5><b><i class="fa fa-plus-square-o" aria-hidden="true"></i> Product Details</b></h5>
    </header>

    <div class="w3-center">
        <a href="/admin/product/list"><button id="listLink" class="w3-button w3-section w3-blue w3-ripple">List Product</button></a>
        <a href="/admin/product/create"><button id="createLink" class="w3-button w3-section w3-blue w3-ripple">Add Product</button></a>
    </div>

    <div class="w3-padding w3-margin-bottom">
        <form action="/admin/product/details" method="get" class="w3-container w3-card-4 w3-light-grey w3-text-blue w3-margin">
        <div class="w3-margin">
            <label>ID</label>
            <p><%= product.getId()%></p>
        </div>

        <div class="w3-margin">
            <label>Name</label>
            <p><%= product.getName()%></p>
        </div>

        <div class="w3-margin">
            <label>Category</label>
            <%
                for (Category category: categories) {
                    if (category.getId() == product.getCategoryId()) {
            %>
            <p type="text"><%=category.getName()%></p>
            <%
                    }
                }
            %>
        </div>

        <div class="w3-margin">
            <label>Price</label>
            <p><%= product.getPrice()%></p>
        </div>

        <div class="w3-margin">
            <label>Description</label>
            <p><%= product.getDescription()%></p>
        </div>

        <div class="w3-margin">
            <label>Thumbnail</label>
            <img src="<%= product.getThumbnail()%>" alt="<%= product.getName()%>" width="300px">
        </div>

        <div class="w3-margin">
            <label>Status</label>
            <p><%= product.getStatus()%></p>
        </div>
        </form>
    </div>

    <br>

    <!-- Footer -->
    <jsp:include page="/admin/include/footer.jsp"/>
    <!-- End page content -->
</div>

<jsp:include page="/admin/include/script.jsp"/>

</body>
</html>



