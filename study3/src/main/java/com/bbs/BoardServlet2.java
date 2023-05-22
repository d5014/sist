package com.bbs;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.util.MyUtil;

//@WebServlet("/bbs/*")
public class BoardServlet2 extends HttpServlet{

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		execute(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		execute(req, resp);
	}
	
	protected void forward(HttpServletRequest req, HttpServletResponse resp, String path) throws ServletException, IOException {
		//포워딩
		RequestDispatcher rd=req.getRequestDispatcher(path);
		rd.forward(req, resp);//req : 받아오는 것 resp : 보내는 것
	}
	
	protected void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		
		//주소 : localhost:9090/study3/bbs/list.do?page=1
		//uri : /study3/bbs/list.do
		
		//uri를 통해 줏소에 맞는 함수로 이동할 수 있도록 설정
		String uri=req.getRequestURI();
		if(uri.indexOf("list.do")!=-1) {
			list(req,resp);
		} else if(uri.indexOf("write.do")!=-1) {
			writeForm(req,resp);
		} else if(uri.indexOf("write_ok.do")!=-1) {
			writeSubmit(req,resp);
		} else if(uri.indexOf("article.do")!=-1) {
			article(req,resp);
		} else if(uri.indexOf("update.do")!=-1) {
			updateForm(req,resp);
		} else if(uri.indexOf("update_ok.do")!=-1) {
			updateSubmit(req,resp);
		} else if(uri.indexOf("delete.do")!=-1) {
			delete(req,resp);
		}
		
	}
	
	protected void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 게시글 리스트
		BoardDAO dao=new BoardDAO();
		MyUtil util=new MyUtil();
		
		String cp=req.getContextPath();
		
		try {
			//파라미터 : [페이지번호],[검색컬럼, 검색값]
			//페이지번호
			String page=req.getParameter("page");
			int current_page=1;
			if(page!=null) {
				current_page=Integer.parseInt(page);
			}
			
			//검색
			String condition=req.getParameter("condition");
			String keyword=req.getParameter("keyword");
			if(condition==null) {//검색상태가 아니면
				condition="all";
				keyword="";
			}
			
			//GET 방식이면 디코딩
			if(req.getMethod().equalsIgnoreCase("GET")){
				keyword=URLDecoder.decode(keyword,"utf-8");
			}
			
			//전체 데이터 개수
			int dataCount;
			if(keyword.length()==0) {
				dataCount=dao.dataCount();
			} else {
				dataCount=dao.dataCount(condition, keyword);
			}
			
			//전체 페이지 수
			int size=2;
			int total_page=util.pageCount(dataCount, size);
			if(current_page>total_page) {
				current_page=total_page;
			}
			
			//게시글 가져오기
			int offset=(current_page-1)*size;
			if(offset<0) offset=0;
			
			List<BoardDTO> list=null;
			
			if(keyword.length()==0) {//검색값이 없으면(검색이 아니면)
				list=dao.listBoard(offset, size);//페이징처리된 리스트 반환
			} else {//검색값이 있으면(검색하면)
				list=dao.listBoard(offset, size, condition, keyword);//페이징처리된 검색리스트 반환
			}
			
			//페이징 처리
			String query="";
			if(keyword.length()!=0) {//검색할때
				query="condition="+condition+"&keyword="+URLEncoder.encode(keyword,"utf-8");
			}
			
			String listUrl=cp+"/bbs/list.do";//리스트주소
			String articleUrl=cp+"/bbs/article.do?page="+current_page;//글보기주소
			
			if(query.length()!=0) {
				listUrl+="?"+query;//전체
				articleUrl+="&"+query;//검색
			}
			
			String paging=util.paging(current_page, total_page, listUrl);
			
			//포워딩할 JSP에 전달할 속성(attribute)
			req.setAttribute("list", list);
			req.setAttribute("page", current_page);
			req.setAttribute("dataCount", dataCount);
			req.setAttribute("size", size);
			req.setAttribute("total_page", total_page);
			req.setAttribute("articleUrl", articleUrl);
			req.setAttribute("paging", paging);
			req.setAttribute("condition", condition);
			req.setAttribute("keyword", keyword);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//포워딩에서는 / 는 cp까지를 의미한다
		forward(req, resp, "/WEB-INF/views/bbs/list.jsp");
	}

	protected void writeForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글쓰기폼
		req.setAttribute("mode", "write");
		forward(req, resp, "/WEB-INF/views/bbs/write.jsp");
	}
	
	protected void writeSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글저장
		BoardDAO dao=new BoardDAO();
		String cp=req.getContextPath();
		
		//GET방식으로 들어오는 경우, 주소줄을 조작하여 들어오는 상황이 생길 수 있음.(해킹..)
		//GET방식으로 접근하지 못하도록 설정
		if(! req.getMethod().equalsIgnoreCase("POST")) {
			//POST방식이 아니라면 list.do 로 리다이렉트 시킴.
			resp.sendRedirect(cp+"/bbs/list.do");
			return;
		}
		
		try {
			//폼 데이터 : 이름, 제목, 내용, 패스워드
			//번호-시퀀스, 등록일-SYSDATE, 조회수-0
			//아이피주소-클라이언트아이피
			
			BoardDTO dto=new BoardDTO();
			
			dto.setName(req.getParameter("name"));
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			dto.setPwd(req.getParameter("pwd"));
			dto.setIpAddr(req.getRemoteAddr());
			
			dao.insertBoard(dto);
			
		} catch (Exception e) {
		} 
		resp.sendRedirect(cp+"/bbs/list.do");//INSERT/ UPDATE / DELETE 이후 반드시 리다이렉트
		//포워드로 못넘기는 이유 : attribute에 넘긴 값이 없어 백지화 및 값이 보이지 않음.
	}
	
	protected void article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글보기
		BoardDAO dao=new BoardDAO();
		String cp=req.getContextPath();
		
		String page=req.getParameter("page");
		String query="page="+page;
		
		try {
			long num=Long.parseLong(req.getParameter("num"));
			String condition=req.getParameter("condition");
			String keyword=req.getParameter("keyword");
			if(condition==null) {
				condition="all";
				keyword="";//""은 디코딩에 문제 생기지 않음(null은 에러)
			}
			keyword=URLDecoder.decode(keyword,"utf-8");
			
			if(keyword.length()!=0) {
				query+="&condition="+condition+"&keyword="+URLEncoder.encode(keyword,"utf-8");
			}
			
			//조회수 증가
			dao.updateHitCount(num);
			
			//게시글 가져오기
			BoardDTO dto=dao.readBoard(num);
			if(dto==null) {
				resp.sendRedirect(cp+"/bbs/list.do?"+query);//검색이 풀리지 않은 상태로 돌아감
				return;
			}
			
			//글 내용 엔터를 <br>로
			dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
			
			//이전글 다음글
			BoardDTO preReadDto=dao.preReadBoard(num, condition, keyword);
			BoardDTO nextReadDto=dao.nextReadBoard(num, condition, keyword);
			
			
			//포워딩 할 JSP에 넘겨줄 속성
			req.setAttribute("dto", dto);
			req.setAttribute("preReadDto", preReadDto);
			req.setAttribute("nextReadDto", nextReadDto);

			req.setAttribute("page", page);
			req.setAttribute("query", query);
			
			forward(req, resp, "/WEB-INF/views/bbs/article.jsp");
			return;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/bbs/list.do?"+query);
	}
	
	protected void updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글 수정 폼
		BoardDAO dao=new BoardDAO();
		String cp=req.getContextPath();
		String page=req.getParameter("page");
		
		try {
			long num=Long.parseLong(req.getParameter("num"));
			
			BoardDTO dto= dao.readBoard(num);
			if(dto==null) {
				resp.sendRedirect(cp+"/bbs/list.do?page="+page);
				return;
			}
			
			req.setAttribute("mode", "update");
			req.setAttribute("dto", dto);
			req.setAttribute("page", page);
			
			forward(req, resp, "/WEB-INF/views/bbs/write.jsp");
			return;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/bbs/list.do?page="+page);
	}
	
	protected void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글 수정 완료
		BoardDAO dao=new BoardDAO();
		String cp=req.getContextPath();
		
		String page=req.getParameter("page");
		
		try {
			//폼데이터 : 이름, 제목, 내용, 패스워드, 번호, 페이지번호
			BoardDTO dto=new BoardDTO();
			
			dto.setName(req.getParameter("name"));
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			dto.setPwd(req.getParameter("pwd"));
			dto.setNum(Long.parseLong(req.getParameter("num")));
			
			dao.updateBoard(dto);
			resp.sendRedirect(cp+"/bbs/article.do?page="+page+"&num="+dto.getNum());
			return;
		} catch (Exception e) {
			e.printStackTrace();
		} 
		resp.sendRedirect(cp+"/bbs/list.do?page="+page);
	}
	
	protected void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 게시글 삭제
		BoardDAO dao=new BoardDAO();
		String cp=req.getContextPath();
		
		String page=req.getParameter("page");
		String query="page="+page;
		
		try {
			
			long num=Long.parseLong(req.getParameter("num"));
			String condition=req.getParameter("condition");
			String keyword=req.getParameter("keyword");
			if(condition==null) {
				condition="all";
				keyword="";//""은 디코딩에 문제 생기지 않음(null은 에러)
			}
			keyword=URLDecoder.decode(keyword,"utf-8");
			
			if(keyword.length()!=0) {
				query+="&condition="+condition+"&keyword="+URLEncoder.encode(keyword,"utf-8");
			}
			
			dao.deleteBoard(num);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp+"/bbs/list.do?"+query);
	}
}
