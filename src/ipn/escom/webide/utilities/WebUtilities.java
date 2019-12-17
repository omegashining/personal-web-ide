package ipn.escom.webide.utilities;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class WebUtilities {
	
	public static User getUser() {
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authentication = context.getAuthentication();
		User user = (User) authentication.getPrincipal();
		
		return user;
	}

	public static Object getBean(String beanName, HttpServletRequest request) {
		HttpSession httpSession = request.getSession();
		ServletContext servletContext = httpSession.getServletContext();
		WebApplicationContext webApplicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
		Object object = webApplicationContext.getBean(beanName);
		
		return object;
	}
	
	public static String getRealPath(String path, HttpServletRequest request) {
		HttpSession httpSession = request.getSession();
		ServletContext servletContext = httpSession.getServletContext();
		String realPath = servletContext.getRealPath(path);

		return realPath;
	}
	
	public static String getMimeType(String name, HttpServletRequest request) {
		HttpSession httpSession = request.getSession();
		ServletContext context = httpSession.getServletContext();
		String mimeType = context.getMimeType(name);
		
		return mimeType;
	}
	
}
