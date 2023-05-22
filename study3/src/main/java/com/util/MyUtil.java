package com.util;

public class MyUtil {
	/**
	 * 전체 페이지 수 구하기
	 * @param dataCount		총데이터수
	 * @param size			한 화면에 출력할 데이터 개수 
	 * @return				전체 페이지 수
	 */
	public int pageCount(int dataCount, int size) {
		if(dataCount<=0) {
			return 0;
		}
		return dataCount/size+(dataCount%size>0?1:0);
	}
	/**
	 * 페이징 처리 (GET 방식)
	 * @param current_page	현재 화면에 표시되는 페이지
	 * @param total_page	전체 페이지 수
	 * @param list_url		링크를 설정할 주소
	 * @return				페이징 처리 결과
	 */
	public String paging(int current_page, int total_page, String list_url) {
		StringBuilder sb=new StringBuilder();
		
		int numPerBlock=10;
		int currentPageSetup;
		int n, page;
		
		if(current_page<1 || total_page<current_page) {
			return "";
		}
		
		if(list_url.indexOf("?")!=-1) {
			list_url+="&";
		} else {
			list_url+="?";
		}
		
		//currentPageSetup : 표시할 첫 페이지 -1
		currentPageSetup=(current_page/numPerBlock)*numPerBlock;
		if(current_page%numPerBlock==0) {
			currentPageSetup=currentPageSetup-numPerBlock;
		}
		
		sb.append("<div class='paginate'>");
		
		//처음, 이전(앞으로 10페이지)
		n=current_page-numPerBlock;
		if(total_page>numPerBlock && currentPageSetup>0) {
			//total_page>numPerBlock : 처음과 이전은 10페이지 이후에만 보이게 설정
			//currentPageSetup>0 : 
			sb.append("<a href='"+list_url+"page=1'>처음</a>");
			sb.append("<a href='"+list_url+"page="+ n +"'>이전</a>");
		}
		//페이징
		page=currentPageSetup+1;
		while(page<=total_page && page<=(currentPageSetup+numPerBlock)) {
			if(page==current_page) {
				sb.append("<span>"+page+"</span>");
			} else {
				sb.append("<a href='"+list_url+"page="+page+"'>"+page+"</a>");
			}
			page++;
		}
		
		//다음, 마지막(뒤로 10페이지)
		n=current_page+numPerBlock;
		if(n>total_page) n=total_page;
		if(total_page - currentPageSetup > numPerBlock) {
			sb.append("<a href='"+list_url+"page="+ n +"'>다음</a>");
			sb.append("<a href='"+list_url+"page="+total_page+"'>끝</a>");
		}
		
		sb.append("</div>");
		
		
		return sb.toString();
	}
	//구글 페이징 처리
	public String paging2(int current_page, int total_page, String list_url) {
		StringBuilder sb=new StringBuilder();
		
		int numPerBlock=10;
		int currentPageSetup;
		int n, page;
		
		if(current_page<1 || total_page<current_page) {
			return "";
		}
		
		if(list_url.indexOf("?")!=-1) {
			list_url+="&";
		} else {
			list_url+="?";
		}
		
		//currentPageSetup : 표시할 첫 페이지 -1
		//1~6페이지까지는 그대로, 7페이지부터 한칸씩 이동하도록 설정
		//마지막 페이지에서 
		if(current_page<7 || current_page>total_page-6) {
			currentPageSetup=(current_page/numPerBlock)*numPerBlock;
		} else {
			currentPageSetup=current_page-6;
		}

		
		sb.append("<div class='paginate'>");
		
		//처음, 이전(앞으로 1페이지)
		n=current_page-1>0?current_page-1:1;
		if(current_page>1) {
			sb.append("<a href='"+list_url+"page=1'>처음</a>");
			sb.append("<a href='"+list_url+"page="+ n +"'>이전</a>");
		}
		
		//페이징
		page=currentPageSetup+1;
		while(page<=total_page && page<=(currentPageSetup+numPerBlock)) {
			if(page==current_page) {
				sb.append("<span>"+page+"</span>");
			} else {
				sb.append("<a href='"+list_url+"page="+page+"'>"+page+"</a>");
			}
			page++;
		}

		//다음, 마지막(뒤로 1페이지)
		n=current_page+1;
		if(n>total_page) n=total_page;
		if(total_page>current_page) {
			sb.append("<a href='"+list_url+"page="+ n +"'>다음</a>");
			sb.append("<a href='"+list_url+"page="+total_page+"'>끝</a>");
		}
		
		sb.append("</div>");
		
		
		return sb.toString();
	}
	//구글 페이징처리2
	public String pagingUrl(int current_page, int total_page, String list_url) {
		StringBuilder sb=new StringBuilder();
		
		int numPerBlock=10;
		int n, page;
		
		if(current_page<1 || total_page < current_page) {
			return "";
		}
		
		if(list_url.indexOf("?")!=-1) {
			list_url+="&";
		} else {
			list_url+="?";
		}
		
		page=1;//출력 시작 페이지
		if(current_page>(numPerBlock/2)+1) {// 7부터는 시작페이지가 변경될 수 있도록
			page=current_page-(numPerBlock/2);
			
			n=total_page-page;//?
			if(n<numPerBlock) {
				page=total_page-numPerBlock+1;
			}
			
			if(page<1) page=1;//페이지가 1보다 작으면 1페이지로
		}
		
		sb.append("<div class='paginate'>");
		
		//처음
		if(page>1) {//2페이지부터 '처음'태그가 보임
			sb.append("<a href='"+list_url+"page=1' title='처음'>&#x226A</a>");
		}
		
		//이전(한페이지 앞)
		n=current_page-1;
		if(current_page>1) {//2페이지부터 '이전'태그가 보임
			sb.append("<a href='"+list_url+"page="+n+"' title='이전'>&#x003C</a>");
		}
		
		n=page;
		while(page<=total_page && page < n+numPerBlock) {//시작페이지로부터 10개(numPerBlock)를 while문으로 반복
			if(page==current_page) {//
				sb.append("<span>"+page+"</span>");
			} else {
				sb.append("<a href='"+list_url+"page="+page+"'>"+page+"</a>");
			}
			page++;
		}
		
		//다음(한페이지 뒤)
		n=current_page+1;
		if(current_page<total_page) {//전체페이지 이전까지 '다음'태그가 보임
			sb.append("<a href='"+list_url+"page="+n+"' title='다음'>&#x003E</a>");
		}
		
		//마지막 페이지
		if(page<=total_page) {//
			sb.append("<a href='"+list_url+"page="+total_page+"' title='마지막'>&#x226B</a>");
		}
		
		
		sb.append("</div>");
		
		return sb.toString();
	}
}
