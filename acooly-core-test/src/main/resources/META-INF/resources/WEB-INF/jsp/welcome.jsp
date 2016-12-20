<!DOCTYPE html>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html lang="en">

<body>
Spring URL: ${time}
<br>
<br>
Message: ${message}

<br>
${pageContext.request.queryString}
</body>

</html>