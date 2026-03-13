<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<!DOCTYPE html>
<html lang="en">
<head>
  <title>Global PayEX</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href='<c:url value="/resources/css/bootstrap.min.css"/>' />
  <link rel="stylesheet" href='<c:url value="/resources/css/multiple-select.css"/>' />
  <link rel="stylesheet" href='<c:url value="/resources/css/style.css"/>' />
   
  <link rel="stylesheet" href="https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.4/themes/smoothness/jquery-ui.css"> 
   <script src='<c:url value="/resources/js/jquery-1.9.1.js"/>' ></script>
  
  <script src='<c:url value="/resources/js/bootstrap.min.js"/>' ></script>
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
  <script src='<c:url value="/resources/js/multiple-select.js"/>' ></script>  
  <script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.4/jquery-ui.min.js"></script> 
  
</head>
<body>
<jsp:include page="header.jsp"></jsp:include>
<jsp:include page="${layout}.jsp"></jsp:include>
</body>
</html>