<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%-- Sử dụng requestScope.user để lấy đối tượng đã truyền vào --%>
<div class="modal fade" id="editUserModal${requestScope.user.id}" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <form action="/admin/managerUsers/save/${requestScope.user.id}" method="POST" onsubmit="submitEditForm(event, this)">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                
                <div class="modal-header">
                    <h5 class="modal-title">Chỉnh sửa người dùng: ${requestScope.user.fullName}</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <input type="hidden" name="id" value="${requestScope.user.id}">
                    
                    <div class="mb-3">
                        <label>Tên người dùng</label>
                        <input type="text" name="fullName" class="form-control" value="${requestScope.user.fullName}" required>
                    </div>
                    <div class="mb-3">
                        <label>Email</label>
                        <input type="email" name="email" class="form-control" value="${requestScope.user.email}" required>
                    </div>
                    <div class="mb-3">
                        <label>Google ID</label>
                        <input type="text" name="googleId" class="form-control" value="${requestScope.user.googleId}">
                    </div>
                    <div class="mb-3">
                        <label>Role</label>
                        <input type="text" name="role" class="form-control" value="${requestScope.user.role}">
                    </div>
                    <div class="mb-3">
                        <label>Trạng thái hoạt động</label>
                        <select name="enabled" class="form-select">
                            <option value="true" ${requestScope.user.enabled ? 'selected' : ''}>Hoạt động</option>
                            <option value="false" ${!requestScope.user.enabled ? 'selected' : ''}>Không hoạt động</option>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label>Provider</label>
                        <input type="text" name="provider" class="form-control" value="${requestScope.user.provider}">
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                    <button type="submit" class="btn btn-primary">Cập nhật</button>
                </div>
            </form>
        </div>
    </div>
</div>
