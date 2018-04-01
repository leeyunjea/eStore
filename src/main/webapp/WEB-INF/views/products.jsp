<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div class="container-wrapper">
	<!-- 테이블 -->
	<div class="container">
		<h2>All Porducts</h2>
		<p>착한 가격으로 상품을 살펴보세요!!</p>
		<table class="table table-striped">
			<thead>
				<tr class="bg-warning">
					<th>Name</th>
					<th>Category</th>
					<th>Price</th>
					<th>Manufacturer</th>
					<th>UnitInStock</th>
					<th>Description</th>
					<th> </th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="product" items="${products }">
					<tr>
						<td>${product.name }</td>
						<td>${product.category }</td>
						<td>${product.price }</td>
						<td>${product.manufacturer }</td>
						<td>${product.unitInStock }</td>
						<td>${product.description }</td>
						<td> <a href="<c:url value="/viewProduct/${product.id}" />"><i class="fa fa-info-circle"></i></a></td> 
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</div>

