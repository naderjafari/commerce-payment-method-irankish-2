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

import com.liferay.commerce.constants.CommerceOrderConstants;
import com.liferay.commerce.constants.CommercePaymentConstants;
import com.liferay.commerce.currency.model.CommerceCurrency;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.payment.method.CommercePaymentMethod;
//import com.liferay.commerce.payment.method.mercanet.internal.configuration.SaderatGroupServiceConfiguration;
//import com.liferay.commerce.payment.method.mercanet.internal.connector.Environment;
//import com.liferay.commerce.payment.method.mercanet.internal.connector.PaypageClient;
//import com.liferay.commerce.payment.method.mercanet.internal.constants.MercanetCommercePaymentMethodConstants;
import com.liferay.commerce.payment.request.CommercePaymentRequest;
import com.liferay.commerce.payment.result.CommercePaymentResult;
import com.liferay.commerce.service.CommerceOrderService;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.module.configuration.ConfigurationException;
import com.liferay.portal.kernel.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.settings.GroupServiceSettingsLocator;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;
import com.sain.commerce.payment.method.saderat.configuration.SaderatGroupServiceConfiguration;
import com.sain.commerce.payment.method.saderat.constants.SaderatBankCommercePaymentMethodConstants;
import com.sain.commerce.payment.method.saderat.ikc.ITokens;
import com.sain.commerce.payment.method.saderat.ikc.MakeTokenResponse;
import com.sain.commerce.payment.method.saderat.ikc.Service1;
import com.sain.commerce.payment.method.saderat.ikc.TokenResponse;
import com.worldline.sips.model.*;
import com.worldline.sips.model.Currency;
import org.omg.CORBA.Environment;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.xml.bind.JAXBElement;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author Luca Pellizzon
 */
@Component(
	immediate = true,
	property = "commerce.payment.engine.method.key=" + SaderatBankCommercePaymentMethod.KEY,
	service = CommercePaymentMethod.class
)
public class SaderatBankCommercePaymentMethod implements CommercePaymentMethod {

	public static final String KEY = "saderat";

	@Override
	public CommercePaymentResult completePayment(
			CommercePaymentRequest commercePaymentRequest)
		throws Exception {

//		MercanetCommercePaymentRequest authorizeNetCommercePaymentRequest =
//			(MercanetCommercePaymentRequest)commercePaymentRequest;
//
//		return new CommercePaymentResult(
//			null, authorizeNetCommercePaymentRequest.getCommerceOrderId(),
//			CommerceOrderConstants.PAYMENT_STATUS_PAID, false, null, null,
//			Collections.emptyList(), true);
//        System.out.println("completePayment ....");
//        return new CommercePaymentResult(
//                null, commercePaymentRequest.getCommerceOrderId(),
//                CommerceOrderConstants.PAYMENT_STATUS_PAID, false, null, null,
//                Collections.emptyList(), true);
		SaderatCommercePaymentRequest authorizeNetCommercePaymentRequest =
				(SaderatCommercePaymentRequest)commercePaymentRequest;

		return new CommercePaymentResult(
				null, authorizeNetCommercePaymentRequest.getCommerceOrderId(),
				CommerceOrderConstants.PAYMENT_STATUS_PAID, false, null, null,
				Collections.emptyList(), true);
	}

	@Override
	public String getDescription(Locale locale) {
		ResourceBundle resourceBundle = _getResourceBundle(locale);

		return LanguageUtil.get(resourceBundle, "mercanet-description");
	}

	@Override
	public String getKey() {
		return KEY;
	}

	@Override
	public String getName(Locale locale) {
		return LanguageUtil.get(locale, KEY);
	}

	@Override
	public int getPaymentType() {
		return CommercePaymentConstants.
				COMMERCE_PAYMENT_METHOD_TYPE_ONLINE_REDIRECT;
	}

	@Override
	public String getServletPath() {
		return SaderatBankCommercePaymentMethodConstants.SERVLET_PATH;
	}

	@Override
	public boolean isCompleteEnabled() {
		return true;
	}

	@Override
	public boolean isProcessPaymentEnabled() {
		return true;
	}

	@Override
	public CommercePaymentResult processPayment(
			CommercePaymentRequest commercePaymentRequest)
		throws Exception {

		SaderatCommercePaymentRequest saderatCommercePaymentRequest =
			(SaderatCommercePaymentRequest)commercePaymentRequest;

		CommerceOrder commerceOrder = _commerceOrderService.getCommerceOrder(
			saderatCommercePaymentRequest.getCommerceOrderId());

//		if(true){
//			throw new PortalException("Mercanet acccept only EUR currency..............");
//		}

		CommerceCurrency commerceCurrency = commerceOrder.getCommerceCurrency();

		System.out.println("commerceCurrency.getCode() = " + commerceCurrency.getCode());

//		if (!Objects.equals(commerceCurrency.getCode(), "EUR")) {
//			throw new Exception("Mercanet acccept only EUR currency");
//		}

		PaymentRequest paymentRequest = new PaymentRequest();

		int normalizedMultiplier = (int)Math.pow(
			10.00, commerceCurrency.getMaxFractionDigits());

		BigDecimal orderTotal = commerceOrder.getTotal();

		BigDecimal powOrderTotal = orderTotal.multiply(
			new BigDecimal(normalizedMultiplier));

		paymentRequest.setAmount(powOrderTotal.intValue());

		System.out.println("powOrderTotal = " + powOrderTotal);

		URL returnUrl = new URL(saderatCommercePaymentRequest.getReturnUrl());

		System.out.println("returnUrl = " + returnUrl);
		Map<String, String> parameters = _getQueryMap(returnUrl.getQuery());

		URL baseUrl = new URL(
			returnUrl.getProtocol(), returnUrl.getHost(), returnUrl.getPort(),
			returnUrl.getPath());

		System.out.println("baseUrl = " + baseUrl);

		StringBuilder normalUrl = new StringBuilder(baseUrl.toString());

		normalUrl.append(StringPool.QUESTION);
		normalUrl.append("type=normal");
		normalUrl.append(StringPool.AMPERSAND);
		normalUrl.append("redirect=");
		normalUrl.append(parameters.get("redirect"));

		System.out.println("parameters.get(\"redirect\") = " + parameters.get("redirect"));

		StringBuilder automaticUrl = new StringBuilder(baseUrl.toString());

		automaticUrl.append(StringPool.QUESTION);
		automaticUrl.append("type=automatic");
		automaticUrl.append(StringPool.AMPERSAND);
		automaticUrl.append("groupId=");
		automaticUrl.append(parameters.get("groupId"));
		automaticUrl.append(StringPool.AMPERSAND);
		automaticUrl.append("uuid=");
		automaticUrl.append(parameters.get("uuid"));

		System.out.println("normalUrl = " + normalUrl);
		System.out.println("automaticUrl = " + automaticUrl);

		URL normalURL = new URL(normalUrl.toString());

		URL automaticURL = new URL(automaticUrl.toString());

		paymentRequest.setAutomaticResponseUrl(automaticURL);

		paymentRequest.setNormalReturnUrl(normalURL);

		paymentRequest.setCaptureMode(CaptureMode.IMMEDIATE);
		paymentRequest.setCurrencyCode(Currency.EUR);
		paymentRequest.setCustomerId(String.valueOf(commerceOrder.getUserId()));
		paymentRequest.setOrderChannel(OrderChannel.INTERNET);
		paymentRequest.setOrderId(
			String.valueOf(commerceOrder.getCommerceOrderId()));

		StringBuilder transactionReference = new StringBuilder();

		transactionReference.append(commerceOrder.getCompanyId());
		transactionReference.append(commerceOrder.getGroupId());
		transactionReference.append(commerceOrder.getCommerceOrderId());

		paymentRequest.setTransactionReference(transactionReference.toString());

		SaderatGroupServiceConfiguration mercanetGroupServiceConfiguration =
			_getConfiguration(commerceOrder.getGroupId());

//		String environment = mercanetGroupServiceConfiguration.environment();
//
//		String upperCaseEnvironment = StringUtil.toUpperCase(environment);

//		String keyVersion = mercanetGroupServiceConfiguration.keyVersion();

//		PaypageClient paypageClient = new PaypageClient(
//			Environment.valueOf(upperCaseEnvironment),
//			mercanetGroupServiceConfiguration.merchantId(),
//			Integer.valueOf(keyVersion),
//			mercanetGroupServiceConfiguration.secretKey());
//
//		InitializationResponse initializationResponse =
//			paypageClient.initialize(paymentRequest);
//
//		URL redirectionUrl = initializationResponse.getRedirectionUrl();

		String url = StringBundler.concat(
			_getServletUrl(saderatCommercePaymentRequest), StringPool.QUESTION,
			"redirectUrl=", "http://google.com", StringPool.AMPERSAND,
			"redirectionData=",
			URLEncoder.encode(
				"gggg", "UTF-8"),
			StringPool.AMPERSAND, "seal=",
			URLEncoder.encode("gggg", "UTF-8"));
//
		List<String> resultMessage = Collections.singletonList(
			"okkkkkkkkkkkk");
//
		return new CommercePaymentResult(
			transactionReference.toString(), commerceOrder.getCommerceOrderId(),
			-1, true, url, null, resultMessage, true);
//        System.out.println("Processs .... ");
//        Service1 service1 = new Service1();
//        ITokens iTokens = service1.getBasicHttpBindingITokens();
//        TokenResponse tokenResponse = iTokens.makeToken("1000","BF40","111111","11111","","https://google.com","");
//        System.out.println("tokenResponse.getToken().getValue() = " + tokenResponse.getToken().getValue());
//        System.out.println("tokenResponse.getToken().getValue() = " + tokenResponse.getMessage().getValue());
////        System.out.println("tokenResponse.getToken().getValue() = " + tokenResponse.getMessage().getValue());
//        return new CommercePaymentResult(
//                null, commercePaymentRequest.getCommerceOrderId(),
//                CommerceOrderConstants.PAYMENT_STATUS_AUTHORIZED, false, null, null,
//                Collections.emptyList(), true);
	}

	private SaderatGroupServiceConfiguration _getConfiguration(Long groupId)
		throws ConfigurationException {

		return _configurationProvider.getConfiguration(
			SaderatGroupServiceConfiguration.class,
			new GroupServiceSettingsLocator(
				groupId, SaderatBankCommercePaymentMethodConstants.SERVICE_NAME));
	}

	private Map<String, String> _getQueryMap(String query)
	{

		String[] params = query.split(StringPool.AMPERSAND);


		Map<String, String> map = new HashMap();

		for (String param : params)
		{
			String name = param.split(StringPool.EQUAL)[0];
			String value = param.split(StringPool.EQUAL)[1];

			map.put(name, value);
		}

		return map;
	}

	private ResourceBundle _getResourceBundle(Locale locale) {
		return ResourceBundleUtil.getBundle(
			"content.Language", locale, getClass());
	}

	private String _getServletUrl(
		SaderatCommercePaymentRequest saderatCommercePaymentRequest) {

		return StringBundler.concat(
			_portal.getPortalURL(
				saderatCommercePaymentRequest.getHttpServletRequest()),
			_portal.getPathModule(), StringPool.SLASH,
				SaderatBankCommercePaymentMethodConstants.SERVLET_PATH);
	}

	@Reference
	private CommerceOrderService _commerceOrderService;

	@Reference
	private ConfigurationProvider _configurationProvider;

	@Reference
	private Portal _portal;

}