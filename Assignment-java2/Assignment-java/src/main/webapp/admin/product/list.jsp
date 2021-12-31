<%@ page import="com.example.assignmentjava.entity.Product" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.example.assignmentjava.entity.Category" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    request.setCharacterEncoding("utf-8");
    ArrayList<Product> list = (ArrayList<Product>) request.getAttribute("list");
    ArrayList<Category> categories = (ArrayList<Category>) request.getAttribute("categoryList");
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    if (list == null) {
        list = new ArrayList<Product>();
    }
    if (categories == null) {
        categories = new ArrayList<Category>();
    }
    int currentPage = (Integer) request.getAttribute("page");
    int pageSize = (Integer) request.getAttribute("pageSize");
    int totalRecord = (Integer) request.getAttribute("totalRecord");
    int totalPage = (int) (Math.ceil((double)totalRecord / pageSize));

    request.setAttribute("totalPage", totalPage);
    for (Category category: categories) {
        System.out.println(category);
    }
%>

<!DOCTYPE html>
<html>
<head>
    <jsp:include page="/admin/include/header.jsp">
        <jsp:param name="title" value="My admin page"/>
        <jsp:param name="description" value="Admin area"/>
        <jsp:param name="keywords" value="admin, page,..."/>
    </jsp:include>
</head>
<body class="w3-light-grey">

<!-- Top container -->
<div class="w3-bar w3-top w3-black w3-large" style="z-index:4">
    <button class="w3-bar-item w3-button w3-hide-large w3-hover-none w3-hover-text-light-grey" onclick="w3_open();"><i
            class="fa fa-bars"></i>  Menu
    </button>
    <span class="w3-bar-item w3-right">List Page</span>
</div>

<jsp:include page="/admin/include/left-menu.jsp"/>

<!-- Overlay effect when opening sidebar on small screens -->
<div class="w3-overlay w3-hide-large w3-animate-opacity" onclick="w3_close()" style="cursor:pointer"
     title="close side menu" id="myOverlay"></div>

<!-- !PAGE CONTENT! -->
<div class="w3-main" style="margin-left:300px;margin-top:43px;">

    <!-- Header -->
    <header class="w3-container" style="padding-top:22px">
        <h5><b><i class="fa fa-list"></i> List Product</b></h5>
    </header>
    <div class="w3-center">
        <a href="/admin/product/list"><button id="listLink" class="w3-button w3-section w3-blue w3-ripple">List Product</button></a>
        <a href="/admin/product/create"><button id="createLink" class="w3-button w3-section w3-blue w3-ripple">Add Product</button></a>
    </div>

    <%
        if (list.size() > 0) {
    %>

    <table class="w3-table-all">
        <tr>
            <th>ID</th>
            <th>Image</th>
            <th>Name</th>
            <th>Category</th>
            <th>Price</th>
            <th>Started Date</th>
            <th>Edit Date</th>
            <th>Status</th>
            <th>Action</th>
        </tr>
        <%
            for (int i = 0; i < list.size(); i++) {
                int categoryId = list.get(i).getCategoryId();
        %>
        <tr>
            <th><%=list.get(i).getId()%></th>
            <th>
                <img src="<%=list.get(i).getThumbnail()%>" style="width: 100px" class="w3-padding" alt="pic">
            </th>
            <th><%=list.get(i).getName()%></th>
            <th><%=categories.stream().filter(category -> category.getId() == categoryId).findAny().get().getName()%></th>
            <th><%=list.get(i).getPrice()%></th>
            <th><%=formatter.format(list.get(i).getStartedDate())%></th>
            <th><%=formatter.format(list.get(i).getEditDate())%></th>
            <th><%=list.get(i).getStatus() == 1 ? "Đang bán" : list.get(i).getStatus() == 0 ? "Dừng bán" : "Không hoạt động"%></th>
            <th>
                <a href="/admin/product/detail?id=<%=list.get(i).getId()%>">Detail</a>&nbsp;
                <a href="/admin/product/edit?id=<%=list.get(i).getId()%>">Edit</a>&nbsp;
                <a class="btn-delete" onclick="deleteProduct(<%= list.get(i).getId()%>)"
                   href="#">Delete</a>
            </th>
        </tr>
        <%
            }
        %>
    </table>
    <div class="ml-3 mt-3">
        <nav aria-label="...">
            <ul class="pagination justify-content-center">
                <li class="page-item <%=currentPage == 1 ? "disabled" : ""%>">
                    <c:choose>
                        <c:when test="${requestScope.get('page') == 1 || requestScope.get('totalPage') == 1}">
                            <span class="page-link">Previous</span>
                        </c:when>
                        <c:otherwise>
                            <a class="page-link" href="/admin/product/list?page=<%=currentPage - 1%>&pageSize=<%=pageSize%>">Previous</a>
                        </c:otherwise>
                    </c:choose>
                </li>

                <c:forEach var="i" begin="1" end="${totalPage + 0.5}">
                    <c:choose>
                        <c:when test="${i == requestScope.get('page')}">
                            <li class="page-item active">
                            <span class="page-link">
                                <c:out value="${i}"/>
                            <span class="sr-only">(current)</span>
                            </span>
                            </li>
                        </c:when>
                        <c:otherwise>
                            <li class="page-item">
                                <a class="page-link"
                                   href="/admin/product/list?page=<c:out value="${i}" />&pageSize=<%=pageSize%>">
                                    <c:out value="${i}"/>
                                </a>
                            </li>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>

                <li class="page-item <%=currentPage == totalPage ? "disabled" : ""%>">
                    <c:choose>
                        <c:when test="${requestScope.get('page') == requestScope.get('totalPage') || requestScope.get('totalPage') == 1}">
                            <span class="page-link">Next</span>
                        </c:when>
                        <c:otherwise>
                            <a class="page-link" href="/admin/product/list?page=<%=currentPage + 1%>&pageSize=<%=pageSize%>">Next</a>
                        </c:otherwise>
                    </c:choose>
                </li>
            </ul>
        </nav>
    </div>
    <%
    } else {
    %>

    <p class="w3-center" style="font-weight: bold">No Records found</p>

    <%
        }
    %>

    <br>

    <!-- Footer -->
    <jsp:include page="/admin/include/footer.jsp"/>
    <!-- End page content -->
</div>

<jsp:include page="/admin/include/script.jsp"/>

<script>
    function deleteProduct(id) {
        if(confirm('Delete the product?')) {
            var xhr = new XMLHttpRequest();
            xhr.onreadystatechange = function () {
                if (xhr.readyState == 4 && xhr.status == 200) {
                    alert('Delete successful!');
                    window.location.reload();
                }
            };
            xhr.open('DELETE', '/admin/product/delete?id=' + id);
            xhr.send();
        }
    }
</script>
</body>
</html>

