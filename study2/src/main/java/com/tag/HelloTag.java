package com.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

public class HelloTag extends TagSupport{

	private static final long serialVersionUID = 1L;
	
	@Override
	public int doStartTag() throws JspException {
		JspWriter out=pageContext.getOut();
		//JspWriter : JSP의 출력객체
		
		try {
			out.print("<b>반가워요</b>");
		} catch (Exception e) {
			throw new JspException();
		}
		
		return SKIP_BODY;
	}
}
