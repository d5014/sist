package com.bbs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

public class BoardDAO {
	private Connection conn=DBConn.getConnection();
	
	public void insertBoard(BoardDTO dto) throws SQLException{
		PreparedStatement pstmt=null;
		String sql;
		
		//INSERT INTO 테이블명(컬럼, 컬럼) VALUES(값,값)
		
		try {
			sql="insert into bbs(num, name, pwd, subject, content, ipAddr, hitCount, reg_date) values(bbs_seq.nextval,?,?,?,?,?,0,sysdate)";
			pstmt=conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getName());
			pstmt.setString(2, dto.getPwd());
			pstmt.setString(3, dto.getSubject());
			pstmt.setString(4, dto.getContent());
			pstmt.setString(5, dto.getIpAddr());
			
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(pstmt !=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
	}
	
	public int dataCount() {//페이징처리를 위한 데이터 개수 반환
		int result=0;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql;
		
		try {
			sql="select count(*) from bbs";
			
			pstmt=conn.prepareStatement(sql);
			rs=pstmt.executeQuery();
			
			if(rs.next()) {
				result=rs.getInt(1);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs !=null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			if(pstmt !=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		
		return result;
	}
	
	public int dataCount(String condition, String keyword) {//페이징처리를 위한 데이터 개수 반환(검색할때)
		//condition : 찾을 목록(날짜검색, 제목검색, 이름검색 ...) | keyword : 검색할 내용
		int result=0;
		
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql;
		
		try {
			sql="select count(*) from bbs";
			
			//condition에 따른 sql 설정
			if(condition.equals("all")) {//제목 또는 내용 검색(instr : 위치를 반환)
				sql+=" where instr(subject,?)>=1 or instr(content,?)>=1";
			} else if(condition.equals("reg_date")) {//날짜 검색
				keyword=keyword.replaceAll("(\\-|\\.|\\/)", "");
				sql+=" where to_char(reg_date, 'YYYYMMDD')=?";
			} else {//subject, content, name
				sql+=" where instr("+condition+",?)>=1";
			}
			
			pstmt=conn.prepareStatement(sql);
			
			pstmt.setString(1, keyword);
			if(condition.equals("all")) {
				pstmt.setString(2, keyword);
			}
		
			rs=pstmt.executeQuery();
			
			if(rs.next()) {
				result=rs.getInt(1);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs !=null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			if(pstmt !=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}

		return result;
	}
	
	public List<BoardDTO> listBoard(int offset, int size){//페이징처리된 리스트 반환
		List<BoardDTO> list=new ArrayList<>();
		
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql;
		
		try {
			sql="select num, name, subject, hitCount, to_char(reg_date,'YYYY-MM-DD') reg_date "
					+ "from bbs "
					+ "order by num desc "//최신게시글이 가장 위로 올라갈 수 있도록
					+ "offset ? rows fetch first ? rows only ";//?이후 ?개 뽑아내기
			
			pstmt=conn.prepareStatement(sql);
			
			pstmt.setInt(1, offset);
			pstmt.setInt(2, size);
			//BoardServlet에서 offset과 size=2를 지정해줌
			
			rs=pstmt.executeQuery();
			
			while(rs.next()) {
				BoardDTO dto=new BoardDTO();
				
				dto.setNum(rs.getLong("num"));
				dto.setName(rs.getString("name"));
				dto.setSubject(rs.getString("subject"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setReg_date(rs.getString("reg_date"));
				
				list.add(dto);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs !=null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			if(pstmt !=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}

		return list;
	}
	
	public List<BoardDTO> listBoard(int offset, int size, String condition, String keyword){//페이징처리된 리스트 반환(검색했을때)
		//condition : 찾을 목록(날짜검색, 제목검색, 이름검색 ...) | keyword : 검색할 내용
		List<BoardDTO> list=new ArrayList<>();
		
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql;
		
		try {
			sql = "SELECT num,name,subject,hitCount, "
		               + " TO_CHAR(reg_date,'YYYY-MM-DD') reg_date "
		               + " FROM bbs ";
		         if(condition.equals("all")) { //오라클 INSTR 위치를 반환(없으면 0 있으면 1부터)
		            sql += " WHERE INSTR(subject, ? ) >= 1 OR INSTR(content, ? ) >=1 ";
		         }else if(condition.equals("reg_date")) {
		            //20201010, 2020-10-10로도 검색가능하게끔 설정
		            keyword = keyword.replaceAll("(\\-|\\.|\\/)", "");//- . / 를 입력한날짜에서 없앰
		            //reg_date를 가져올때 년월일시분초가 같이 나오니깐 년월일만 뽑아주기
		            sql += " WHERE TO_CHAR(reg_date,'YYYYMMDD') = ? ";
		         }else { //subject, content, name
		            sql += " WHERE INSTR(" + condition + ", ?) >= 1";
		         }
		         
		         sql += " ORDER BY num DESC ";
		         sql += "OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ";
		         
		         pstmt = conn.prepareStatement(sql);
		         
		         if(condition.equals("all")) {
		            pstmt.setString(1,keyword);
		            pstmt.setString(2,keyword);
		            pstmt.setInt(3, offset);
		            pstmt.setInt(4, size);
		            
		         } else {
		        	 pstmt.setString(1, keyword);
		        	 pstmt.setInt(2, offset);
		        	 pstmt.setInt(3, size);
		         }
		         
		         rs=pstmt.executeQuery();
		         
		         while(rs.next()) {
		        	 BoardDTO dto=new BoardDTO();
		        	 
						dto.setNum(rs.getLong("num"));
						dto.setName(rs.getString("name"));
						dto.setSubject(rs.getString("subject"));
						dto.setHitCount(rs.getInt("hitCount"));
						dto.setReg_date(rs.getString("reg_date"));
						
						list.add(dto);
		         }
		         
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs !=null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			if(pstmt !=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}

		
		return list;
	}
	
	public BoardDTO readBoard(long num) {
		BoardDTO dto=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql;
		
		try {
			sql="select num, name, subject, content, pwd, ipAddr, hitCount, reg_date "
					+ "from bbs "
					+ "where num=?";
			
			pstmt=conn.prepareStatement(sql);
			
			pstmt.setLong(1, num);
			
			rs=pstmt.executeQuery();
			
			if(rs.next()) {
				dto=new BoardDTO();
				dto.setNum(rs.getLong("num"));
				dto.setName(rs.getString("name"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setPwd(rs.getString("pwd"));
				dto.setIpAddr(rs.getString("ipAddr"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setReg_date(rs.getString("reg_date"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs !=null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			if(pstmt !=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		return dto;
	}
	
	public BoardDTO preReadBoard(long num, String condition, String keyword) {
		//이전글
		BoardDTO dto=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		StringBuilder sb=new StringBuilder();
		
		
		try {
			if(keyword!=null && keyword.length()!=0) {
				//검색
				sb.append("select num, subject ");
				sb.append("from bbs ");
				sb.append("where num > ? ");
				
				if(condition.equals("all")) {
					sb.append(" and (instr(subject, ?)>=1 or instr(content, ?)>=1) ");
				} else if(condition.equals("reg_date")) {
					keyword=keyword.replaceAll("(\\-|\\.|\\/)", "");
					sb.append(" and (to_char(reg_date,'YYYYMMDD'))=?)");
				} else {
					sb.append(" and (instr("+condition+", ?)>=1)");
				}
				sb.append("order by num asc ");
				sb.append("fetch first 1 rows only");
				pstmt=conn.prepareStatement(sb.toString());
				pstmt.setLong(1, num);
				pstmt.setString(2, keyword);
				if(condition.equals("all")) {
					pstmt.setString(3, keyword);
				}
				
			} else {
				//검색이 아닐때
				sb.append("select num, subject ");
				sb.append("from bbs ");
				sb.append("where num > ? ");
				sb.append("order by num asc ");
				sb.append("fetch first 1 rows only");
				
				pstmt=conn.prepareStatement(sb.toString());
				pstmt.setLong(1, num);
			}
			rs=pstmt.executeQuery();
			
			if(rs.next()){
				dto=new BoardDTO();
				dto.setNum(rs.getLong("num"));
				dto.setSubject(rs.getString("subject"));
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		return dto;
	}

	public BoardDTO nextReadBoard(long num, String condition, String keyword) {
		//다음글
		BoardDTO dto=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		StringBuilder sb=new StringBuilder();
		
		
		try {
			if(keyword!=null && keyword.length()!=0) {
				//검색
				sb.append("select num, subject ");
				sb.append("from bbs ");
				sb.append("where num < ? ");
				
				if(condition.equals("all")) {
					sb.append(" and (instr(subject, ?)>=1 or instr(content, ?)>=1) ");
				} else if(condition.equals("reg_date")) {
					keyword=keyword.replaceAll("(\\-|\\.|\\/)", "");
					sb.append(" and (to_char(reg_date,'YYYYMMDD'))=?)");
				} else {
					sb.append(" and (instr("+condition+", ?)>=1)");
				}
				sb.append("order by num desc ");
				sb.append("fetch first 1 rows only");
				pstmt=conn.prepareStatement(sb.toString());
				pstmt.setLong(1, num);
				pstmt.setString(2, keyword);
				if(condition.equals("all")) {
					pstmt.setString(3, keyword);
				}
				
			} else {
				//검색이 아닐때
				sb.append("select num, subject ");
				sb.append("from bbs ");
				sb.append("where num < ? ");
				sb.append("order by num desc ");
				sb.append("fetch first 1 rows only");
				
				pstmt=conn.prepareStatement(sb.toString());
				pstmt.setLong(1, num);
			}
			rs=pstmt.executeQuery();
			
			if(rs.next()){
				dto=new BoardDTO();
				dto.setNum(rs.getLong("num"));
				dto.setSubject(rs.getString("subject"));
			}
			
			

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		return dto;
	}
	
	
	public void updateHitCount(long num) throws SQLException{
		PreparedStatement pstmt=null;
		String sql;
		
		try {
			sql="update bbs set hitCount=hitCount+1 where num=?";
			pstmt=conn.prepareStatement(sql);
			pstmt.setLong(1, num);
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(pstmt !=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
	}
	
	public void updateBoard(BoardDTO dto) throws Exception{
		PreparedStatement pstmt=null;
		String sql;
		
		try {
			sql="update bbs set name=?, subject=?, content=?, pwd=? where num=?";
			pstmt=conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getName());
			pstmt.setString(2, dto.getSubject());
			pstmt.setString(3, dto.getContent());
			pstmt.setString(4, dto.getPwd());
			pstmt.setLong(5, dto.getNum());
			
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(pstmt !=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
	}

	public void deleteBoard(long num) throws Exception{
		PreparedStatement pstmt=null;
		String sql;
		
		try {
			sql="delete from bbs where num=?";
			pstmt=conn.prepareStatement(sql);
			pstmt.setLong(1, num);
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(pstmt !=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
	}
	
	
}
