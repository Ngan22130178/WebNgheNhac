<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<div id="fav-container-${song.id}" style="display: inline-block; vertical-align: middle;">
    <!-- Tạo một biến cờ để kiểm tra xem ID bài hát có nằm trong danh sách yêu thích không -->
    <c:set var="isFavorite" value="false" />
    <c:forEach var="favId" items="${favoriteSongIds}">
        <c:if test="${favId == song.id}">
            <c:set var="isFavorite" value="true" />
        </c:if>
    </c:forEach>

    <c:choose>
        <%-- TRƯỜNG HỢP: ĐÃ YÊU THÍCH (Trái tim đỏ đầy) --%>
        <c:when test="${isFavorite}">
            <button class="btn btn-link p-0 border-0" style="text-decoration: none; color: #ff0000; font-size: 1.2rem;" 
                hx-post="/api/user/favorite/toggle" 
                hx-vals='{"songId": "${song.id}"}'
                hx-target="#fav-container-${song.id}" 
                hx-swap="outerHTML"
                onclick="if(typeof toggleHeartUI === 'function') toggleHeartUI('${song.id}', '${fn:escapeXml(song.title)}', true)">
                <i class="fa-solid fa-heart"></i>
            </button>
        </c:when>
        
        <%-- TRƯỜNG HỢP: CHƯA YÊU THÍCH (Trái tim viền đỏ, lòng rỗng) --%>
        <c:otherwise>
            <button class="btn btn-link p-0 border-0" style="text-decoration: none; color: #ff0000; font-size: 1.2rem;" 
                hx-post="/api/user/favorite/toggle" 
                hx-vals='{"songId": "${song.id}"}'
                hx-target="#fav-container-${song.id}" 
                hx-swap="outerHTML"
                onclick="if(typeof toggleHeartUI === 'function') toggleHeartUI('${song.id}', '${fn:escapeXml(song.title)}', false)">
                <i class="fa-regular fa-heart"></i>
            </button>
        </c:otherwise>
    </c:choose>
</div>