<%@page import="java.util.List"%>
<%@page import="model.entity.Department"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
	String id = (String) request.getAttribute("id");
%>
<%@ include file="header.jsp"%>

<div class="container top-first">
	<h1>
		<div class="badge bg-warning text-dark text-wrap large col-12">
			<div class="row">
				<div class="text-start col" style="font-size: 35px;">
					Department Record
					<div class="badge bg-light text-success text-wrap">Add</div>
				</div>
			</div>
		</div>
	</h1>

	<br> <br>
	<h1>
		<center>Please fill in the following details.</center>
	</h1>
	<hr>
	<br> <br>
</div>

<div class="container wow bounceIn" data-wow-duration="1s" data-wow-delay="0.2s">
	<form method="post" action="MainServlet">
		<input type="hidden" name="table" value="department" />
		<div class="form-group row">
			<label class="control-label col-3 text-end">Department ID:</label>
			<div class="col-8">
				<div class="input-group mb-3">
				<input id="dept_id" type="text" class="form-control"
					placeholder="ID will be autogenerated." name="id" value="<%=id%>">
					<div class="input-group-append">
						<button class="btn btn-primary" type="button" onclick="
						  $.get('MainServlet?table=department&action=getAutoId', function(data, status){
							  var aid = $.parseJSON(data);
							  alert('The generated ID is [' + aid.autoId +'] which may not applicable during submit. (Use by sitmoutaneous user)');
							  $('#dept_id').val(aid.autoId);});
						">Auto ID</button>
					</div>
				</div>
			</div>

		</div>
		<br>
		<div class="form-group row">
			<label class="control-label col-3 text-end">Department name:</label>
			<div class="col-8">
				<input type="text" class="form-control" placeholder="Enter name"
					name="dept_name">

			</div>
		</div>
		<br>
		<div class="form-group row">
			<label class="control-label col-3 text-end"></label>
			<div class="col-8">
				<input type="submit" class="btn btn-success" name="action"
					value="add">
			</div>
		</div>
	</form>
</div>

<%@ include file="footer.jsp"%>
