package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
@Data : @NoArgsConstructor, @AllArgsConstructor, @Getter, @Setter 을 합친 녀석

*/
//@Data 모든 종류 constructor 생성, getter/ setter 생성

@NoArgsConstructor
@AllArgsConstructor

@Getter
@Setter
public class TodoDTO {
	private int id;
	private int completed;
	private String todoname;

}
