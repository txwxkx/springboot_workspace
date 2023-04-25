package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.TodoDAO;
import com.example.demo.dto.TodoDTO;

@Service // 서비스 역할 하는 애 구분해주는 annotation
public class TodoServiceImp implements TodoService{
	
	@Autowired // 대충 연결해준다는 기능
	private TodoDAO todoDao;
	
	public TodoServiceImp() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<TodoDTO> search() throws Exception {
		// TODO Auto-generated method stub
		return todoDao.getToList();
	}

	@Override
	public int insert(TodoDTO dto) throws Exception {
		// TODO Auto-generated method stub
		return todoDao.insertTodoList(dto);
	}

	@Override
	public int update(TodoDTO dto) throws Exception {
		// TODO Auto-generated method stub
		return todoDao.updateTodoList(dto);
	}

	@Override
	public int delete(int id) throws Exception {
		// TODO Auto-generated method stub
		return todoDao.deleteTodoList(id);
	}

}
