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

package com.sain.commerce.payment.method.saderat.servlet;

import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.payment.engine.CommercePaymentEngine;

import com.liferay.commerce.service.CommerceOrderLocalService;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.module.configuration.ConfigurationException;
import com.liferay.portal.kernel.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.servlet.PortalSessionThreadLocal;
import com.liferay.portal.kernel.settings.GroupServiceSettingsLocator;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringUtil;
import com.sain.commerce.payment.method.saderat.configuration.SaderatGroupServiceConfiguration;
import com.sain.commerce.payment.method.saderat.constants.SaderatBankCommercePaymentMethodConstants;
import com.worldline.sips.model.PaypageResponse;
import com.worldline.sips.model.ResponseCode;
import com.worldline.sips.model.ResponseData;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Luca Pellizzon
 */
@Component(
	immediate = true,
	property = {
		"osgi.http.whiteboard.context.path=/" + SaderatBankCommercePaymentMethodConstants.SERVLET_PATH,
		"osgi.http.whiteboard.servlet.name=com.sain.commerce.payment.method.saderat.servlet.SaderatServlet",
		"osgi.http.whiteboard.servlet.pattern=/" + SaderatBankCommercePaymentMethodConstants.SERVLET_PATH + "/*"
	},
	service = Servlet.class
)
public class SaderatServlet extends HttpServlet {

	@Override
	protected void doGet(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws IOException, ServletException {

		try {
			HttpSession httpSession = httpServletRequest.getSession();

			System.out.println("HERER DO GETTTTTTTTTTT SERVLETTTTTTTTTTTTT.....");

			if (PortalSessionThreadLocal.getHttpSession() == null) {
				PortalSessionThreadLocal.setHttpSession(httpSession);
			}

			User user = _portal.getUser(httpServletRequest);

			PermissionChecker permissionChecker =
				PermissionCheckerFactoryUtil.create(user);

			PermissionThreadLocal.setPermissionChecker(permissionChecker);

			RequestDispatcher requestDispatcher =
				_servletContext.getRequestDispatcher(
					"/saderat_form/saderat-form.jsp");

			requestDispatcher.forward(httpServletRequest, httpServletResponse);
		}
		catch (Exception e) {
			_portal.sendError(e, httpServletRequest, httpServletResponse);
		}
	}

	@Override
	protected void doPost(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws IOException, ServletException {
		System.out.println("Here Do POST IN SERVLET ...");
		try {
			String type = ParamUtil.getString(httpServletRequest, "type");
			System.out.println("type = " + type);
//
//			if (Objects.equals("normal", type)) {
//				HttpSession httpSession = httpServletRequest.getSession();
//
//				if (PortalSessionThreadLocal.getHttpSession() == null) {
//					PortalSessionThreadLocal.setHttpSession(httpSession);
//				}
//
//				User user = _portal.getUser(httpServletRequest);
//
//				PermissionChecker permissionChecker =
//					PermissionCheckerFactoryUtil.create(user);
//
//				PermissionThreadLocal.setPermissionChecker(permissionChecker);
//
//				String redirect = ParamUtil.getString(
//					httpServletRequest, "redirect");
//
//				httpServletResponse.sendRedirect(redirect);
//			}
//
//			if (Objects.equals("automatic", type)) {
				long groupId = ParamUtil.getLong(httpServletRequest, "groupId");
				String uuid = ParamUtil.getString(httpServletRequest, "uuid");
				System.out.println("uuid = " + uuid);
				System.out.println("groupId = " + groupId);

				String referenceId = ParamUtil.getString(httpServletRequest, "referenceId");
				String resultCode = ParamUtil.getString(httpServletRequest, "resultCode");
				String invoiceNumber = ParamUtil.getString(httpServletRequest, "paymentId");
				String token = ParamUtil.getString(httpServletRequest, "token");
				String redirect = ParamUtil.getString(httpServletRequest, "redirect");
			System.out.println("referenceId = " + referenceId);
			System.out.println("resultCode = " + resultCode);
			System.out.println("invoiceNumber = " + invoiceNumber);
			System.out.println("redirect = " + redirect);
			System.out.println("token = " + token);
////
//				String data = httpServletRequest.getParameter("Data");
////
//				Map<String, String> parametersMap = _getResponseParameters(
//					data);

				CommerceOrder commerceOrder =
					_commerceOrderLocalService.getCommerceOrderByUuidAndGroupId(
						uuid, groupId);
//
//				SaderatGroupServiceConfiguration
//					mercanetGroupServiceConfiguration = _getConfiguration(
//						commerceOrder.getGroupId());
//
//				String environment =
//					mercanetGroupServiceConfiguration.environment();
//
//				String upperCaseEnvironment = StringUtil.toUpperCase(
//					environment);
//
//				String keyVersion =
//					mercanetGroupServiceConfiguration.keyVersion();
//
//				PaypageClient paypageClient = new PaypageClient(
//					Environment.valueOf(upperCaseEnvironment),
//					mercanetGroupServiceConfiguration.merchantId(),
//					Integer.valueOf(keyVersion),
//					mercanetGroupServiceConfiguration.secretKey());
//
//				String seal = httpServletRequest.getParameter("Seal");
//
//				Map<String, String> verifyMap = new HashMap<>();
//
//				verifyMap.put("Data", data);
//				verifyMap.put("Seal", seal);
//
//				PaypageResponse paypageResponse = paypageClient.decodeResponse(
//					verifyMap);
//
//				ResponseData responseData = paypageResponse.getData();
//
				StringBuilder transactionReference = new StringBuilder();

				transactionReference.append(commerceOrder.getCompanyId());
				transactionReference.append(commerceOrder.getGroupId());
				transactionReference.append(commerceOrder.getCommerceOrderId());
//
//				ResponseCode responseCode = responseData.getResponseCode();
//
//				if (Objects.equals(responseCode.getCode(), "00") &&
//					Objects.equals(
//						responseData.getMerchantId(),
//						mercanetGroupServiceConfiguration.merchantId()) &&
//				   Objects.equals(
//						parametersMap.get("customerId"),
//						String.valueOf(commerceOrder.getUserId())) &&
//				   Objects.equals(
//						parametersMap.get("orderId"),
//						String.valueOf(commerceOrder.getCommerceOrderId())) &&
//					Objects.equals(
//						responseData.getTransactionReference(),
//						transactionReference.toString())) {
//
			System.out.println("**************************** commerceOrder.getCommerceOrderId() = " + commerceOrder.getCommerceOrderId());
					_commercePaymentEngine.cancelPayment(
						commerceOrder.getCommerceOrderId(),
						transactionReference.toString(), httpServletRequest);

			httpServletResponse.sendRedirect(redirect);

			System.out.println("hereeeeeeeeeeee END Do gettttttttttt ");
//				}
//			}
		}
		catch (Exception e) {
			e.printStackTrace();
			_portal.sendError(e, httpServletRequest, httpServletResponse);
		}
	}

//	private MercanetGroupServiceConfiguration _getConfiguration(Long groupId)
//		throws ConfigurationException {
//
//		return _configurationProvider.getConfiguration(
//			MercanetGroupServiceConfiguration.class,
//			new GroupServiceSettingsLocator(
//				groupId, MercanetCommercePaymentMethodConstants.SERVICE_NAME));
//	}

	private Map<String, String> _getResponseParameters(String data) {
		String[] params = data.split(StringPool.BACK_SLASH + StringPool.PIPE);

		Map<String, String> map = new HashMap<>();

		for (String param : params)
		{
			String name = param.split(StringPool.EQUAL)[0];
			String value = param.split(StringPool.EQUAL)[1];

			System.out.println("name = " + name + " : "+value);

			map.put(name, value);
		}

		return map;
	}

	@Reference
	private CommerceOrderLocalService _commerceOrderLocalService;

	@Reference
	private CommercePaymentEngine _commercePaymentEngine;

	@Reference
	private ConfigurationProvider _configurationProvider;

	@Reference
	private Portal _portal;

	@Reference(
		target = "(osgi.web.symbolicname=com.sain.commerce.payment.method.saderat)"
	)
	private ServletContext _servletContext;

}