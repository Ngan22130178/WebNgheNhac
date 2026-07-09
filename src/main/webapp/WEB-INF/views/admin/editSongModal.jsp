<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%-- Sử dụng requestScope.song để lấy đối tượng đã truyền vào --%>
<div class="modal fade" id="editSongModal${requestScope.song.id}" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <form action="/admin/songs/save/${requestScope.song.id}" method="POST" onsubmit="submitEditForm(event, this)">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                
                <div class="modal-header">
                    <h5 class="modal-title">Chỉnh sửa bài hát: ${requestScope.song.title}</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <input type="hidden" name="id" value="${requestScope.song.id}">
                    
                    <div class="mb-3">
                        <label>Tên bài hát</label>
                        <input type="text" name="title" class="form-control" value="${requestScope.song.title}" required>
                    </div>
                    <div class="mb-3">
                        <label>Ca sĩ</label>
                        <input type="text" name="artist" class="form-control" value="${requestScope.song.artist}">
                    </div>
                    <div class="mb-3">
                        <label>Thể loại</label>
                        <input type="text" name="genre" class="form-control" value="${requestScope.song.genre}">
                    </div>
                    <div class="mb-3">
                        <label>Album</label>
                        <input type="text" name="albumName" class="form-control" value="${requestScope.song.albumName}">
                    </div>
                    <div class="mb-3">
                        <label>URL (Chọn file từ máy tính)</label>
                        <input type="text" name="url" id="urlInput_${requestScope.song.id}" class="form-control" value="${requestScope.song.url}">
                        
                        <input type="file" class="form-control mt-2" onchange="updateUrl(this, '${requestScope.song.id}')">
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
<script>
    function updateUrl(input, id) {
        if (input.files && input.files[0]) {
            // Lấy tên file hoặc đường dẫn giả lập
            const fileName = input.files[0].name;
            // Điền vào ô input text tương ứng
            document.getElementById('urlInput_' + id).value = '/audio/' + fileName;
        }
    }
</script>