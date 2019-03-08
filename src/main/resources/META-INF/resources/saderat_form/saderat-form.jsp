<%--
/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
--%>

<%@ include file="/init.jsp" %>

<%
String redirectUrl = (String)request.getAttribute("redirectUrl");
String merchantId = (String)request.getAttribute("merchantId");
String token = (String)request.getAttribute("token");
	System.out.println("merchantId = " + merchantId);
	System.out.println("redirectUrl = " + redirectUrl);
	System.out.println("token = " + token);
%>

<form action="<%= redirectUrl %>" id="formMercanet" method="post" name="formMercanet" style="display: none">
	<%--<input name="redirectionData" type="hidden" value="<%= redirectionData %>" />--%>
	<%--<input name="seal" type="hidden" value="<%= seal %>" />--%>
	<%--<input type="submit" value="Proceed to checkout" />--%>

	<input type="hidden" name="merchantId" id="<portlet:namespace />merchantId" value="<%= merchantId %>">
	<input type="hidden" name="token" id="<portlet:namespace />token" value="<%= token %>" >
</form>

<script>
	window.onload = function() {
		document.querySelector('form').submit();
	}
</script>