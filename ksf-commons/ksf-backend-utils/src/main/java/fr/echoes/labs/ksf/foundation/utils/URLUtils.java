package fr.echoes.labs.ksf.foundation.utils;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import com.google.common.collect.Lists;

public final class URLUtils {

	private URLUtils() {
		// static class
	}
	
	public static String removeLastSlash(final String url) {
		
		int length = url == null ? 0 : url.length();
		
		if (length > 0 && url.charAt(length-1) == '/') {
			return url.substring(0, length-1);
		}
		
		return url;
	}
	
	public static String addPath(final String url, final String path) {
		
		final StringBuilder sb = new StringBuilder();
		sb.append(removeLastSlash(url));
		
		if (!path.startsWith("/")) {
			sb.append('/');
		}
		
		return sb.append(path).toString();
	}
	
	public static String encodeParam(final String param) {
		
		try {
			return new URLCodec().encode(param);
		} catch (EncoderException ex) {
			throw new IllegalArgumentException("Cannot properly encode parameter "+param, ex);
		}
		
	}
	
	public static String buildQuery(final Map<String, String> params) {
		
		final List<NameValuePair> pairs = Lists.newArrayList();
		for (final Entry<String, String> entry : params.entrySet()) {
			pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		
		return URLEncodedUtils.format(pairs, Charset.defaultCharset());
	}

}
