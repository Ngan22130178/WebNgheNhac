<%-- fragments/songs_table.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:forEach var="song" items="${songs}">
    <tr>
        <td class="align-middle" style="cursor: pointer; color: #007bff;" 
            onclick="playNow('${song.url}', '${song.title}')">
            ${song.title}
        </td>
        <td class="align-middle">${song.artist}</td>
        <td class="align-middle"><span class="badge bg-primary">${song.genre}</span></td>
        <td class="align-middle">${song.albumName}</td>
        <td>
            <button class="btn btn-sm btn-outline-primary" 
                    onclick="addToQueue('${song.url}', '${song.title}')">+ Thêm</button>
        </td>
    </tr>
</c:forEach>

<c:if test="${empty songs && not empty keyword}">
    <tr>
        <td colspan="5" class="text-center text-warning py-4">
            ❌ Không có bài hát phù hợp với từ khóa: "<strong>${keyword}</strong>"
        </td>
    </tr>
</c:if>