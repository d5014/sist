package com.memo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

public class MemoDAO {
	private Connection conn=DBConn.getConnection();
	
	public void insertMemo(MemoDTO dto) throws SQLException{
		PreparedStatement pstmt=null;
		String sql;
		
		try {
			sql="insert into memo(num, content, saveFilename, originalFilename, reg_date, update_date) values (memo_seq.nextval, ?, ?, ?, sysdate, sysdate)";
			pstmt=conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getContent());
			pstmt.setString(2, dto.getSaveFilename());
			pstmt.setString(3, dto.getOriginalFilename());
			
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
	}
	
	public int dataCount() {
		int result=0;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql;
		
		try {
			sql="select count(*) from memo";
			
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
	
	public List<MemoDTO> listMemo(int offset, int size){
		List<MemoDTO> list=new ArrayList<>();
		
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql;
		
		try {
			sql="select num, content, saveFilename, originalFilename, to_char(reg_date,'YYYY-MM-DD') reg_date, to_char(update_date,'YYYY-MM-DD') update_date "
					+ "from memo "
					+ "order by num desc "
					+ "offset ? rows fetch first ? rows only";
			
			pstmt=conn.prepareStatement(sql);
			
			pstmt.setInt(1, offset);
			pstmt.setInt(2, size);
			
			rs=pstmt.executeQuery();
			
			while(rs.next()) {
				MemoDTO dto=new MemoDTO();
				
				dto.setNum(rs.getLong("num"));
				dto.setContent(rs.getString("content"));
				dto.setSaveFilename(rs.getString("saveFilename"));
				dto.setOriginalFilename(rs.getString("originalFilename"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setUpdate_date(rs.getString("update_date"));
				
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
	
	public MemoDTO readMemo(long num) {
		MemoDTO dto=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql;
		
		try {
			sql="select num, content, saveFilename, originalFilename, reg_date, update_date "
					+ "from memo "
					+ "where num=?";
			
			pstmt=conn.prepareStatement(sql);
			pstmt.setLong(1, num);
			rs=pstmt.executeQuery();
			
			if(rs.next()) {
				dto=new MemoDTO();
				dto.setNum(rs.getLong("num"));
				dto.setContent(rs.getString("content"));
				dto.setSaveFilename(rs.getString("saveFilename"));
				dto.setOriginalFilename(rs.getString("originalFilename"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setUpdate_date(rs.getString("update_date"));
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
	
	public void updateMemo(MemoDTO dto) throws SQLException{
		PreparedStatement pstmt=null;
		String sql;
		
		try {
			sql="update memo set content=?, saveFilename=?, originalFilename=?, update_date=sysdate where num=?";
			pstmt=conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getContent());
			pstmt.setString(2, dto.getSaveFilename());
			pstmt.setString(3, dto.getOriginalFilename());
			pstmt.setLong(4, dto.getNum());
			
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
	
	public void deleteMemo(long num) throws SQLException{
		PreparedStatement pstmt=null;
		String sql;
		
		try {
			sql="delete from memo where num=?";
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
