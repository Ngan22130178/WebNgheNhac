<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="profile-edit-container" style="max-width: 600px; animation: fadeIn 0.3s;">
    <h2 class="mb-4" style="color: #191c1f; font-weight: 700;">Thông tin tài khoản</h2>
    
    <!-- Hiện thông báo THÀNH CÔNG -->
    <c:if test="${not empty successMessage}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <i class="fa-solid fa-circle-check me-2"></i> ${successMessage}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <!-- Hiện thông báo LỖI nếu có -->
    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <i class="fa-solid fa-circle-exclamation me-2"></i> ${errorMessage}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <!-- 🛠️ ĐÃ SỬA: Bổ sung /user vào trước đường dẫn hx-post để lưu thông tin thành công -->
    <form hx-post="/user/api/user/update-profile" 
          hx-target="#profileContent" 
          hx-swap="innerHTML"
          <c:if test="${not empty _csrf.headerName}">
              hx-headers='{"${_csrf.headerName}": "${_csrf.token}"}'
          </c:if>>
          
        <!-- Vẫn giữ CSRF token dạng ô input ẩn để backup an toàn song song -->
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        
        <!-- KHU VỰC AVATAR -->
        <div class="d-flex align-items-center mb-4">
            <sec:authorize access="isAuthenticated()">
                <sec:authentication property="principal.fullName" var="fullName" />
                <c:set var="firstChar" value="${fn:substring(fullName, 0, 1)}" />

                <div class="d-flex align-items-center justify-content-center rounded-circle shadow-sm" 
                     style="width: 70px; height: 70px; background-color: #007bff; color: white; font-size: 28px; font-weight: bold;">
                    ${fn:toUpperCase(firstChar)}
                </div>
            </sec:authorize>
            
            <sec:authorize access="!isAuthenticated()">
                <div class="d-flex align-items-center justify-content-center rounded-circle" 
                     style="width: 70px; height: 70px; background-color: #007bff; color: white; font-size: 28px; font-weight: bold;">
                    U
                </div>
            </sec:authorize>
        </div>

        <!-- Tên hiển thị -->
        <div class="mb-3">
            <label class="form-label fw-semibold text-secondary">Tên hiển thị</label>
            <input type="text" name="displayName" class="form-control" 
                   value="<sec:authentication property='principal.fullName' htmlEscape='true' />" 
                   placeholder="Nhập tên hiển thị của bạn" required>
        </div>

        <!-- Địa chỉ Email  -->
        <div class="mb-3">
            <label class="form-label fw-semibold text-secondary">Địa chỉ Email</label>
            <input type="email" class="form-control bg-light" 
                   value="<sec:authentication property='principal.username' htmlEscape='true' />" 
                   readonly style="cursor: not-allowed;">
        </div>

        <!-- Vùng Đổi mật khẩu tách biệt -->
        <hr class="my-4" style="border-top: 1px dashed #ccc;">
        <h4 class="mb-3" style="color: #191c1f; font-weight: 600; font-size: 1.1rem;">
            Đổi mật khẩu 
        </h4>

        <!-- Mật khẩu hiện tại -->
        <div class="mb-3">
            <label class="form-label fw-semibold text-secondary">Mật khẩu hiện tại</label>
            <div class="input-group border rounded-3 overflow-hidden bg-white evaluation-group" style="transition: all 0.2s ease-in-out;">
                <input type="password" id="currentPassword" name="currentPassword" class="form-control border-0 shadow-none" placeholder="Nhập mật khẩu hiện tại" style="padding: 10px 12px; outline: none;">
                <button class="btn border-0 text-muted btn-toggle-pwd shadow-none" type="button" onclick="togglePasswordVisibility('currentPassword', this)" style="background: transparent;">
                    <i class="fa-regular fa-eye fs-5"></i>
                </button>
            </div>
        </div>

        <!-- Mật khẩu mới -->
        <div class="mb-3">
            <label class="form-label fw-semibold text-secondary">Mật khẩu mới</label>
            <div class="input-group border rounded-3 overflow-hidden bg-white evaluation-group" style="transition: all 0.2s ease-in-out;">
                <input type="password" id="newPassword" name="newPassword" class="form-control border-0 shadow-none" placeholder="Mật khẩu mới" style="padding: 10px 12px; outline: none;">
                <button class="btn border-0 text-muted btn-toggle-pwd shadow-none" type="button" onclick="togglePasswordVisibility('newPassword', this)" style="background: transparent;">
                    <i class="fa-regular fa-eye fs-5"></i>
                </button>
            </div>
        </div>

        <!-- Xác nhận mật khẩu mới -->
        <div class="mb-4">
            <label class="form-label fw-semibold text-secondary">Xác nhận mật khẩu mới</label>
            <div class="input-group border rounded-3 overflow-hidden bg-white evaluation-group" style="transition: all 0.2s ease-in-out;">
                <input type="password" id="confirmPassword" name="confirmPassword" class="form-control border-0 shadow-none" placeholder="Nhập lại mật khẩu mới" style="padding: 10px 12px; outline: none;">
                <button class="btn border-0 text-muted btn-toggle-pwd shadow-none" type="button" onclick="togglePasswordVisibility('confirmPassword', this)" style="background: transparent;">
                    <i class="fa-regular fa-eye fs-5"></i>
                </button>
            </div>
        </div>

        <!-- NÚT BẤM HÀNH ĐỘNG -->
        <div class="d-flex gap-2">
            <button type="submit" class="btn btn-success px-4 btn-submit-profile" 
                    style="background-color: #1db954; border-color: #1db954;">
                <span class="spinner-border spinner-border-sm me-2 save-indicator" role="status" aria-hidden="true" style="display: none;"></span>
                Lưu thay đổi
            </button>
            
            <!-- 🛠️ ĐÃ SỬA: Thêm tiền tố /user vào đường dẫn hx-get để nút hoạt động reset chính xác -->
            <button type="button" class="btn btn-outline-secondary px-4" 
                    hx-get="/user/api/user/account" 
                    hx-target="#profileContent"
                    hx-swap="innerHTML">
                Hủy bỏ
            </button>
        </div>
    </form>
</div>

<style>
.evaluation-group:focus-within {
    border-color: #86b7fe !important;
    box-shadow: 0 0 0 0.25rem rgba(13, 110, 253, 0.25);
}
.btn-toggle-pwd:hover {
    color: #212529 !important;
}

.htmx-request .save-indicator {
    display: inline-block !important;
}
.htmx-request .btn-submit-profile {
    pointer-events: none;
    opacity: 0.8;
}
</style>

<script>
function togglePasswordVisibility(inputId, button) {
    const input = document.getElementById(inputId);
    const icon = button.querySelector('i');
    
    if (input.type === 'password') {
        input.type = 'text';
        icon.classList.remove('fa-regular', 'fa-eye');
        icon.classList.add('fa-solid', 'fa-eye-slash');
    } else {
        input.type = 'password';
        icon.classList.remove('fa-solid', 'fa-eye-slash');
        icon.classList.add('fa-regular', 'fa-eye');
    }
}
</script>