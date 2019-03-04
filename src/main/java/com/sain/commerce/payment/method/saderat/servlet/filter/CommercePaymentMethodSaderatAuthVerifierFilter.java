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

package com.sain.commerce.payment.method.saderat.servlet.filter;

import com.liferay.portal.servlet.filters.authverifier.AuthVerifierFilter;
import com.sain.commerce.payment.method.saderat.constants.SaderatBankCommercePaymentMethodConstants;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Filter;

/**
 * @author Luca Pellizzon
 */
@Component(
	immediate = true,
	property = {
		"filter.init.auth.verifier.PortalSessionAuthVerifier.urls.includes=/" + SaderatBankCommercePaymentMethodConstants.SERVLET_PATH + "/*",
		"osgi.http.whiteboard.filter.name=com.sain.commerce.payment.method.saderat.servlet.filter.CommercePaymentMethodSaderatAuthVerifierFilter",
		"osgi.http.whiteboard.servlet.pattern=/" + SaderatBankCommercePaymentMethodConstants.SERVLET_PATH + "/*"
	},
	service = Filter.class
)
public class CommercePaymentMethodSaderatAuthVerifierFilter
	extends AuthVerifierFilter {
}