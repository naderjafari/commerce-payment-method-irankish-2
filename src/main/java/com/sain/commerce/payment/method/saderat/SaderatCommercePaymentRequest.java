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

package com.sain.commerce.payment.method.saderat;

import com.liferay.commerce.payment.request.CommercePaymentRequest;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Locale;

/**
 * @author Luca Pellizzon
 */
public class SaderatCommercePaymentRequest extends CommercePaymentRequest {

	public SaderatCommercePaymentRequest(
		BigDecimal amount, String cancelUrl, long commerceOrderId,
		Locale locale, HttpServletRequest httpServletRequest, String returnUrl,
		String transactionId) {

		super(
			amount, cancelUrl, commerceOrderId, locale, returnUrl,
			transactionId);

		_httpServletRequest = httpServletRequest;
	}

	public HttpServletRequest getHttpServletRequest() {
		return _httpServletRequest;
	}

	private final HttpServletRequest _httpServletRequest;

}