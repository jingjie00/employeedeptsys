<%@ page errorPage="error.jsp"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<jsp:useBean id="dub" type="model.javabean.DepartmentUseBean"
	scope="request" />


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
	<div class="text-danger text-center"><jsp:getProperty name="dub"
			property="overall_error" /></div>
	<br> <br>
</div>

<div class="container wow bounceIn" data-wow-duration="1s"
	data-wow-delay="0.2s">
	<form method="post" action="MainServlet">
		<input type="hidden" name="target" value="department" />
		<div class="form-group row">
			<label class="control-label col-3 text-end">Department ID:</label>
			<div class="col-8">
				<div class="input-group">
					<input id="dept_id" type="text" class="form-control"
						placeholder="ID will be autogenerated." name="id"
						value='<jsp:getProperty name="dub" property="id"/>' required>

					<div class="input-group-append">
						<button id="autoid" class="btn btn-primary" type="button"
							onclick="
								$('#autoid').attr('class', 'btn btn-primary spinner-border');
						  		$.get('MainServlet?target=department&action=getAutoId', function(data, status){
						  			$('#autoid').attr('class', 'btn btn-primary');
						  			var aid = $.parseJSON(data);
						  			if(aid.autoId=='allUsed'){
							  			alert('The usual department id (dxxx) is all used. Please manually key in');
							  		}else{
							  		
							  		alert('The generated ID is [' + aid.autoId +'] which may not applicable during submit. (Use by sitmoutaneous user)');
							  		$('#dept_id').val(aid.autoId);}});
						">Auto
							ID</button>
					</div>
				</div>
				<div class="text-danger"><jsp:getProperty name="dub"
						property="id_error" /></div>
			</div>

		</div>
		<br>
		<div class="form-group row">
			<label class="control-label col-3 text-end">Department name:</label>
			<div class="col-8">
				<input type="text" class="form-control" placeholder="Enter name"
					name="dept_name"
					value='<jsp:getProperty name="dub" property="dept_name"/>' required>
				<div class="text-danger"><jsp:getProperty name="dub"
						property="dept_name_error" /></div>
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
