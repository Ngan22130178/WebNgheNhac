<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Đăng nhập - MusicWeb</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        body { background-color: #f8f9fa; }
        .login-card { border: none; border-radius: 15px; box-shadow: 0 10px 25px rgba(0,0,0,0.1); }
        .btn-green { background-color: #28a745; color: white; border: none; }
        .btn-green:hover { background-color: #218838; color: white; }
        .btn-google { border: 1px solid #ddd; background: white; color: #555; }
        .btn-google:hover { background: #f1f1f1; }
        .divider { display: flex; align-items: center; text-align: center; color: #888; margin: 1.5rem 0; }
        .divider::before, .divider::after { content: ''; flex: 1; border-bottom: 1px solid #ddd; }
        .divider::before { margin-right: .5em; }
        .divider::after { margin-left: .5em; }
    </style>
</head>
<body>
    <div class="container d-flex justify-content-center align-items-center min-vh-100">
        <div class="card login-card p-4" style="width: 400px;">
            <h3 class="text-center mb-4" style="color: #28a745;">Chào mừng trở lại!</h3>
            
            <c:if test="${param.error != null}">
                <div class="alert alert-danger">Sai email hoặc mật khẩu!</div>
            </c:if>

            <form action="/login" method="POST">
                <div class="mb-3">
                    <label class="form-label">Email</label>
                    <input type="email" name="email" class="form-control" required placeholder="example@email.com">
                </div>
                <div class="mb-3">
                    <label class="form-label">Mật khẩu</label>
                    <input type="password" name="password" class="form-control" required placeholder="••••••••">
                </div>
                <button type="submit" class="btn btn-green w-100 py-2">Đăng nhập</button>
            </form>

            <div class="divider">Hoặc đăng nhập với</div>

            <a href="/oauth2/authorization/google" class="btn btn-google w-100 py-2 d-flex align-items-center justify-content-center">
                <img src="https://upload.wikimedia.org/wikipedia/commons/c/c1/Google_%22G%22_logo.svg" 
                    alt="Google logo" style="width: 20px; height: 20px; margin-right: 10px;">
                Đăng nhập bằng Google
            </a>

            <div class="mt-4 text-center">
                <p>Chưa có tài khoản? <a href="/register" class="text-decoration-none" style="color: #28a745; font-weight: 600;">Đăng ký ngay</a></p>
            </div>
        </div>
    </div>
</body>
</html>
