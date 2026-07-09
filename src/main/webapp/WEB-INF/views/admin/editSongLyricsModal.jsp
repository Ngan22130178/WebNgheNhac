                <!-- Edit url lời bài hát-->

                <div class="mb-3">
                    <label class="fw-bold">Quản lý lời bài hát</label>
                    <div id="lyrics-management-area">
                        <c:forEach var="lyric" items="${song.songLyricsList}">
                            <div class="input-group mb-2">
                                <span class="input-group-text">${lyric.language}</span>
                                <input type="text" class="form-control" value="${lyric.fileUrl}" readonly>
                                <button class="btn btn-outline-danger" type="button">Xóa</button>
                            </div>
                        </c:forEach>
                        
                        <div class="card p-2 mt-2 bg-light">
                            <small class="text-primary fw-bold">Thêm bản lời mới</small>
                            <select name="new_lang" class="form-select form-select-sm mt-1">
                                <option value="vi">Tiếng Việt</option>
                                <option value="en">Tiếng Anh</option>
                            </select>
                            <input type="file" class="form-control form-control-sm mt-1" onchange="uploadNewLyric(this, '${song.id}')">
                        </div>
                    </div>
                </div>