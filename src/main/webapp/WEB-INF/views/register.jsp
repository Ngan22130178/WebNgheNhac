<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Đăng ký tài khoản</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { background-color: #f8f9fa; }
        .register-card {
            width: 100%;
            max-width: 450px;
            padding: 40px;
            border-radius: 20px;
            box-shadow: 0 10px 25px rgba(0,0,0,0.1);
            background: white;
            margin: 50px auto;
        }
        .btn-green { background-color: #28a745; color: white; border-radius: 10px; font-weight: 600; }
        .btn-green:hover { background-color: #218838; color: white; }
        .form-control { border-radius: 10px; padding: 12px; }
        h3 { color: #28a745; font-weight: 700; margin-bottom: 30px; }
    </style>
</head>
<body>

    <div class="container">
        <div class="register-card">
            <h3 class="text-center">Tạo tài khoản mới</h3>
            <form action="/register" method="POST">
                <div class="mb-3">
                    <label class="form-label">Họ và tên</label>
                    <input type="text" name="fullName" class="form-control" placeholder="Nhập họ tên của bạn" required>
                </div>
                <div class="mb-3">
                    <label class="form-label">Email</label>
                    <input type="email" name="email" class="form-control" placeholder="example@email.com" required>
                </div>
                <div class="mb-3">
                    <label class="form-label">Mật khẩu</label>
                    <input type="password" name="password" class="form-control" placeholder="••••••" required>
                </div>
                
                <button type="submit" class="btn btn-green w-100 py-2 mt-3">Đăng ký ngay</button>
                
                <p class="text-center mt-3">
                    Đã có tài khoản? <a href="/login" class="text-success text-decoration-none">Đăng nhập</a>
                </p>
                <p class="text-danger text-center mt-2">${error}</p>
            </form>
        </div>
    </div>

</body>
</html>