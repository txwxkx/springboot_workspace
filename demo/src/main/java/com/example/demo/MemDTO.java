package com.example.demo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor //파라미터가 없는 생성자
@AllArgsConstructor //모든 멤버변수를 제공하는 생성자

@Getter //getter 메소드
@Setter //setter 메소드
public class MemDTO {
	private String name;
	private int age;
	private String loc;
	
}//end class
