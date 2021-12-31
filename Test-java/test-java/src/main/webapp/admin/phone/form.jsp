<%@ page import="com.example.testjava.entity.Phone" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    request.setCharacterEncoding("utf-8");
    Phone phone = (Phone) request.getAttribute("phone");
    HashMap<String, String> errors = (HashMap<String, String>) request.getAttribute("errors");
    if (phone == null) {
        phone = new Phone();
    }
    if (errors == null) {
        errors = new HashMap<>();
    }
%>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="/admin/include/header.jsp">
        <jsp:param name="title" value="Product Form"/>
        <jsp:param name="description" value="Product Form"/>
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
        <h5><b><i class="fa fa-plus-square-o" aria-hidden="true"></i> Product Form</b></h5>
    </header>

    <div class="w3-padding w3-margin-bottom">
        <%
            if(errors.size() > 0) {
        %>
        <div class="w3-panel w3-red">
            <strong>Error! Please fix the form below and try again!</strong>
            <ul>
                <%
                    for (Map.Entry<String, String> entry: errors.entrySet()) {
                %>
                <li><%= entry.getValue()%></li>
                <%
                    }
                %>
            </ul>
        </div>
        <%
            }
        %>
        <form action="/admin/phone/create" method="post" class="w3-container w3-card-4 w3-light-grey w3-text-blue w3-margin">

            <div class="w3-margin">
                <label>Name</label>
                <input class="w3-input" type="text" name="name" value="<%= phone.getName()%>">
                <%
                    if (errors.containsKey("name")) {
                %>
                <div class="w3-text-red w3-margin">* <%= errors.get("name")%></div>
                <%
                    }
                %>
            </div>

            <div class="w3-margin">
                <label>Price</label>
                <input class="w3-input" type="number" name="price" value="<%= phone.getPrice()%>">
                <%
                    if (errors.containsKey("price")) {
                %>
                <div class="w3-text-red w3-margin">* <%= errors.get("price")%></div>
                <%
                    }
                %>
            </div>

            <div class="w3-margin">
                <label>Description</label>
                <input class="w3-input" type="text" name="description" value="<%= phone.getDescription()%>">
                <%
                    if (errors.containsKey("description")) {
                %>
                <div class="w3-text-red w3-margin">* <%= errors.get("description")%></div>
                <%
                    }
                %>
            </div>

            <div class="w3-margin">
                <label>Brand</label>
                <input class="w3-input" type="text" name="brand" value="<%= phone.getBrand()%>">
                <%
                    if (errors.containsKey("brand")) {
                %>
                <div class="w3-text-red w3-margin">* <%= errors.get("brand")%></div>
                <%
                    }
                %>
            </div>

            <p class="w3-center">
                <button class="w3-button w3-section w3-blue w3-ripple"> Submit </button>
            </p>
            <p class="w3-center">
                <button class="w3-button w3-section w3-blue w3-ripple"> Reset </button>
            </p>
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
