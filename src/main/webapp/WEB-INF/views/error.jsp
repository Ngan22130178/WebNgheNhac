<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<!DOCTYPE html>
<html>
<head>
    <title>Đã xảy ra lỗi</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light d-flex align-items-center justify-content-center min-vh-100">
    <div class="container text-center">
        <h1 class="display-1 text-danger">Oops!</h1>
        <h3>Đã xảy ra lỗi hệ thống</h3>
        <p class="text-muted">Trạng thái: ${pageContext.errorData.statusCode}</p>
        
        <div class="card my-4 text-start shadow-sm">
            <div class="card-body">
                <h5 class="card-title">Chi tiết lỗi:</h5>
                <pre class="bg-dark text-white p-3">
                    ${pageContext.exception}
                </pre>
            </div>
        </div>
        
        <a href="/" class="btn btn-primary">Quay về trang chủ</a>
    </div>
</body>
</html>