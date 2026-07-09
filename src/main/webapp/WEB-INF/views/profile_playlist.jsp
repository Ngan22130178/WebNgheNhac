<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="card shadow-sm border-0 w-100">
    <div class="table-responsive">
        <table class="table table-hover align-middle mb-0">
            
            <thead class="table-dark">
                <tr>
                    <th scope="col" style="padding-left: 15px;">Tên bài hát</th>
                    <th scope="col">Ca sĩ</th>
                    <th scope="col">Thể loại</th>
                    <th scope="col">Album</th>
                    <th scope="col" class="text-center">Nút</th>
                </tr>
            </thead>

            <jsp:include page="fragments/songs_table.jsp" />

        </table>
    </div>
</div>