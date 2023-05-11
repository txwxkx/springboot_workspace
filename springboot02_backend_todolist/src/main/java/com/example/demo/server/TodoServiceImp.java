package com.example.demo.server;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.TodoDAO;
import com.example.demo.dto.TodoDTO;

@Service
public class TodoServiceImp implements TodoService {
	
	@Autowired
	private TodoDAO todoDAO;
	
	public TodoServiceImp() {
		
	}
	
	@Override
	public List<TodoDTO> search() throws Exception {
		return todoDAO.getTodoList();
	}

	@Override
	public int insert(TodoDTO dto) throws Exception {
		return todoDAO.insertTodoList(dto);
	}

	@Override
	public int update(TodoDTO dto) throws Exception {
		return todoDAO.updateTodoList(dto);
	}

	@Override
	public int delete(int id) throws Exception {
		return todoDAO.deleteTodoList(id);
	}
	
}
