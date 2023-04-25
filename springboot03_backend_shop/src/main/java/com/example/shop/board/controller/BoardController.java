package com.example.shop.board.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.shop.board.dto.BoardDTO;
import com.example.shop.board.dto.PageDTO;
import com.example.shop.board.service.BoardService;
import com.example.shop.common.file.FileUpload;

@CrossOrigin(origins = {"http://localhost:3000"})
@RestController
public class BoardController {
	
	@Autowired
	private BoardService boardService;
	          
	@Value("${spring.servlet.multipart.location}")
	private String filePath;
	
	@Autowired
	private PageDTO pdto;
	
	private int currentPage;
	
	
	public BoardController() {
		
	}
	
	// http://localhost:8090/board/list/1
	
	@GetMapping("/board/list/{currentPage}")
	public Map<String, Object> listExecute(@PathVariable("currentPage") int currentPage, PageDTO pv) {
		Map<String, Object> map = new HashMap<>();
		int totalRecord = boardService.countProcess();
		if(totalRecord>=1) {
			this.currentPage = currentPage;
			
			this.pdto = new PageDTO(this.currentPage, totalRecord);
			
			map.put("aList", boardService.listProcess(this.pdto));
			map.put("pv", this.pdto);
		}
		return map;
	}
	
	//@RequestBody : json => 자바객체로 보낼 때
	// @ResponseBody : 자바객체를 => json 으로 보낼 때
	// @Pathvariable : /board/list/:num => /board/list/{num}
	// @RequestParam : /board/list?num=value => /board/list?num=1 => /board/list
	// multipart/form-data : @requerstbody 선언없이 그냥 받음 boardDTO dto
	@PostMapping("/board/write")
	public String writeProExecute(BoardDTO dto, PageDTO pv, HttpServletRequest req, HttpSession session) {
		MultipartFile file = dto.getFilename();
		
		//파일 첨부가 있으면...
		if(file!=null && !file.isEmpty()) {
			UUID random = FileUpload.saveCopyFile(file, filePath);
			dto.setUpload(random + "_" + file.getOriginalFilename());
		}
		
		dto.setIp(req.getRemoteAddr());
		
		boardService.insertProcess(dto);
		
		// 답변글이면
		if(dto.getRef()!=0) {
//			ratt.addAttribute("currentPage", pv.getCurrentPage());
			return String.valueOf(pv.getCurrentPage());
		}else {
			return String.valueOf(1);
		}
		
//		return "redirect:/board/list.do";
		
	}
	
	@GetMapping("/board/view/{num}")
	public BoardDTO viewExecute(@PathVariable("num") int num) {
		return boardService.contentProcess(num);
	}
	
	@PutMapping("/board/update")
	public void updateExecute(BoardDTO dto, HttpServletRequest request) throws IllegalStateException, IOException {
		MultipartFile file = dto.getFilename();
		if(file!=null && !file.isEmpty()) {
			UUID random = FileUpload.saveCopyFile(file, filePath);
			dto.setUpload(random + "_" + file.getOriginalFilename());
			//C:\k_digital\download\temp 경로에 첨부파일 저장
			file.transferTo(new File(random + "_" + file.getOriginalFilename()));
		}
		
		boardService.updateProcess(dto, filePath);	
	}
	
	@DeleteMapping("/board/delete/{num}")
	public void deleteExecute(@PathVariable("num") int num, HttpServletRequest request) {
		boardService.deleteProcess(num, filePath);
	}
	
	@GetMapping("/board/contentdownload/{filename}")
	public ResponseEntity<Resource> downloadExecute(@PathVariable("filename") String filename) throws IOException {
		
		String fileName = filename.substring(filename.indexOf("_") + 1);
		
		//파일명이 한글일 시 인코딩한다.
		String str = URLEncoder.encode(fileName, "UTF-8");
		
		// 원본파일명에 공백이 있을 때 + 표시가 되므로 공백으로 처리해줌
		str = str.replaceAll("\\+", "%20");
		Path path = Paths.get(filePath+"\\"+filename);
		Resource resource = new InputStreamResource(Files.newInputStream(path));
		
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename="+str+";")
				.body(resource);
	}
	

}
