package com.example.demo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.example.demo.dto.TodoDTO;

@Mapper // 쿼리문에서 메소드명을 찾아 실행해줄 수 있게 한다.
@Repository
public interface TodoDAO {
	public List<TodoDTO> getToList() throws Exception; 
	
	public int insertTodoList(TodoDTO dto) throws Exception;
	
	public int updateTodoList(TodoDTO dto) throws Exception;
	
	public int deleteTodoList(int id) throws Exception;

}
